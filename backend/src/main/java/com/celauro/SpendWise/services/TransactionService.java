package com.celauro.SpendWise.services;

import com.celauro.SpendWise.dtos.*;
import com.celauro.SpendWise.entity.Transaction;
import com.celauro.SpendWise.entity.User;
import com.celauro.SpendWise.exceptions.ErrorDateException;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.exceptions.NotValidType;
import com.celauro.SpendWise.exceptions.UnauthorizedUserException;
import com.celauro.SpendWise.repositories.TransactionRepository;
import com.celauro.SpendWise.utils.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public List<TransactionDTO> getAllTransactions(){
        User currentUser = userService.getCurrentUser();

        List<Transaction> transactions = transactionRepository.findAllByUserOrderByUpdatedAtDescIdDesc(currentUser);
        log.info("Requested all user transactions - transactionLength={} userId={} email={}", transactions.size(), currentUser.getId(), currentUser.getEmail());
        return toListOfResponse_transactions(transactions);
    }

    public TransactionDTO saveTransaction(TransactionDTO request){
        User currentUser = userService.getCurrentUser();
        log.info("Requested to create new transaction - userId={} email={}", currentUser.getId(), currentUser.getEmail());

        Transaction transaction = createTransactionEntity(request);
        transactionRepository.save(transaction);

        log.info("New transaction created successfully - transactionId={} userId={} email={}", transaction.getId(), currentUser.getId(), currentUser.getEmail());
        return toTransactionDTO(transaction);
    }

    public TransactionDTO getTransaction(long id) {
        User currentUser = userService.getCurrentUser();
        log.info("Requested to receive a transaction by id - transactionId={} userId={} email={}", id, currentUser.getId(), currentUser.getEmail());

        Transaction transaction = getByIdOrThrowException(id);
        validUserOrThrowException(transaction);

        log.info("Sending the required transaction - transactionId={} userId={} email={}", transaction.getId(), currentUser.getId(), currentUser.getEmail());
        return toTransactionDTO(transaction);
    }


    public TransactionDTO deleteTransaction(long id) {
        User currentUser = userService.getCurrentUser();
        log.info("Requested to delete a transaction by id - transactionId={} userId={} email={}", id, currentUser.getId(), currentUser.getEmail());

        Transaction transaction = getByIdOrThrowException(id);
        validUserOrThrowException(transaction);

        transactionRepository.delete(transaction);
        log.info("Deleted the required transaction - transactionId={} userId={} email={}", transaction.getId(), currentUser.getId(), currentUser.getEmail());
        return toTransactionDTO(transaction);
    }

    public TransactionDTO updateTransaction(long id, TransactionDTO request){
        User currentUser = userService.getCurrentUser();
        log.info("Requested to update a transaction by id - transactionId={} userId={} email={}", id, currentUser.getId(), currentUser.getEmail());

        Transaction transaction = getByIdOrThrowException(id);

        validUserOrThrowException(transaction);

        checkIfTypeIsValid(transaction.getType());

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(categoryService.findOrThrowException(request.getCategory()));
        transaction.setDescription(request.getDescription());

        transaction.updateTransaction();

        transactionRepository.save(transaction);
        log.info("Updated the required transaction - transactionId={} userId={} email={}", transaction.getId(), currentUser.getId(), currentUser.getEmail());
        return toTransactionDTO(transaction);
    }

    public NetStatsDTO getMonthlyNet(int month, int year){
        User currentUser = userService.getCurrentUser();
        log.info("Requested the monthly net stats - userId={} email={}", currentUser.getId(), currentUser.getEmail());

        validMonthOrThrowException(month);

        double income = getMonthlyIncome(month, year);
        double expenses = getMonthlyExpenses(month, year);
        double balance = income - expenses;

        log.info("Sending the monthly net stats - userId={} email={}",currentUser.getId(), currentUser.getEmail());
        return new NetStatsDTO(expenses, income, balance);
    }

    public List<CategoryTotalDTO> getCategoryTotals(int month, int year){
        User currentUser = userService.getCurrentUser();
        log.info("Requested the transactions filtered by their category in a given month - month={} userId={} email={}", month, currentUser.getId(), currentUser.getEmail());

        validMonthOrThrowException(month);

        log.info("Sending the transactions filtered by their category in a given month - month={} userId={} email={}", month, currentUser.getId(), currentUser.getEmail());
        return transactionRepository.getTotalsByCategory(userService.getCurrentUser(), month, year)
                .stream()
                .map(total -> new CategoryTotalDTO(total.getCategory(), total.getTotal()))
                .toList();
    }


    public List<MonthlyExpensesDTO> getTransactionTrend(int year) {
        User currentUser = userService.getCurrentUser();
        log.info("Requested the transactions trend in a given year - year={} userId={} email={}", year, currentUser.getId(), currentUser.getEmail());

        List<MonthlyExpensesDTO> list = new ArrayList<>();
        int currentMonth = LocalDate.now().getMonth().getValue();
        int startingPoint = 1;
        while(startingPoint <= currentMonth){
            double expenses = getMonthlyExpenses(startingPoint, year);
            MonthlyExpensesDTO dto = new MonthlyExpensesDTO(startingPoint, expenses);
            list.add(dto);
            startingPoint++;
        }

        log.info("Sending the transactions trend in a given year - year={} userId={} email={}", year, currentUser.getId(), currentUser.getEmail());
        return list;
    }

//  Helper methods
    private void validMonthOrThrowException(int month) {
        if(month > 12 || month < 1){
            throw new ErrorDateException("not valid month");
        }
    }

    private void validUserOrThrowException(Transaction transaction) {
        User currentUser = userService.getCurrentUser();

        if(transaction.getUser() != currentUser){
            log.warn("Tried to access a not owned transaction - userId={} email={}", currentUser.getId(), currentUser.getEmail());
            throw new UnauthorizedUserException("you are not the owner of this transaction");
        }
    }

    private Transaction getByIdOrThrowException(long id){
        return transactionRepository.findTransactionById(id).orElseThrow(() -> new NotFoundException("transaction not found"));
    }

    private TransactionDTO toTransactionDTO(Transaction transaction){
        return new TransactionDTO(
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCategory().getCategory(),
                transaction.getUpdatedAt(),
                transaction.getDescription(),
                transaction.getPaymentMethod()
        );
    }

    private List<TransactionDTO> toListOfResponse_transactions(List<Transaction> transactions){
        return transactions.stream()
                .map(transaction ->
                    new TransactionDTO(
                            transaction.getType(),
                            transaction.getAmount(),
                            transaction.getCategory().getCategory(),
                            transaction.getUpdatedAt(),
                            transaction.getDescription(),
                            transaction.getPaymentMethod()
                    )
                )
                .collect(Collectors.toList());
    }

    private Transaction createTransactionEntity(TransactionDTO dto){
        User currentUser = userService.getCurrentUser();

        checkIfTypeIsValid(dto.getType());

        return new Transaction(
                dto.getType(),
                dto.getAmount(),
                currentUser,
                categoryService.findOrThrowException(dto.getCategory()),
                dto.getDescription(),
                dto.getPaymentMethod()
        );
    }

    private List<Transaction> getFilteredListWithMonthAndYear(int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return transactionRepository.findByUserAndUpdatedAtBetween(userService.getCurrentUser(), start, end);
    }

    private double getMonthlyExpenses(int month, int year){
        return getFilteredListWithMonthAndYear(month, year)
                .stream()
                .filter(transaction -> transaction.getType().equals(TransactionType.EXPENSE.name()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private double getMonthlyIncome(int month, int year){
        return getFilteredListWithMonthAndYear(month, year)
                .stream()
                .filter(transaction -> transaction.getType().equals(TransactionType.INCOME.name()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private void checkIfTypeIsValid(String type){
        User currentUser = userService.getCurrentUser();

        if(!TransactionType.findByName(type)){
            log.warn("Used an invalid type - userId={} email={}", currentUser.getId(), currentUser.getEmail());
            throw new NotValidType("not valid type");
        }
    }

}
