package com.celauro.SpendWise.services;

import com.celauro.SpendWise.dtos.*;
import com.celauro.SpendWise.entity.Transaction;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.repositories.TransactionRepository;
import com.celauro.SpendWise.utils.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public List<TransactionDTO> getAllTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        return toListOfResponse_transactions(transactions);
    }

    public TransactionDTO saveTransaction(TransactionDTO request){
        Transaction transaction = createTransactionEntity(request);
        transactionRepository.save(transaction);
        return toTransactionDTO(transaction);
    }

    public TransactionDTO getTransaction(long id) {
        Transaction transaction = getByIdOrThrowException(id);
        return toTransactionDTO(transaction);
    }

    public TransactionDTO deleteTransaction(long id) {
        Transaction transaction = getByIdOrThrowException(id);
        transactionRepository.delete(transaction);
        return toTransactionDTO(transaction);
    }

    public TransactionDTO updateTransaction(long id, TransactionDTO request){
        Transaction transaction = getByIdOrThrowException(id);

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType().name());
        transaction.setCategory(categoryService.findOrThrowException(request.getCategory()));
        transaction.setDescription(request.getDescription());

        transaction.updateTransaction();

        transactionRepository.save(transaction);

        return toTransactionDTO(transaction);
    }

    public NetStatsDTO getMonthlyNet(int month, int year){
        double income = getMonthlyIncome(month, year);
        double expenses = getMonthlyExpenses(month, year);
        double balance = income - expenses;
        return new NetStatsDTO(expenses, income, balance);
    }

    public List<CategoryTotalDTO> getCategoryTotals(int month, int year){
        return transactionRepository.getTotalsByCategory(month, year)
                .stream()
                .map(total -> new CategoryTotalDTO(total.getCategory(), total.getTotal()))
                .toList();
    }

    public List<MonthlyExpensesDTO> getTransactionTrend(int year) {
        List<MonthlyExpensesDTO> list = new ArrayList<>();
        int currentMonth = LocalDate.now().getMonth().getValue();
        int startingPoint = 1;
        while(startingPoint <= currentMonth){
            double expenses = getMonthlyExpenses(startingPoint, year);
            MonthlyExpensesDTO dto = new MonthlyExpensesDTO(startingPoint, expenses);
            list.add(dto);
            startingPoint++;
        }

        return list;
    }

//  Helper methods
    private Transaction getByIdOrThrowException(long id){
        return transactionRepository.findTransactionById(id).orElseThrow(() -> new NotFoundException ("transaction not found"));
    }

    private TransactionDTO toTransactionDTO(Transaction transaction){
        return new TransactionDTO(
                TransactionType.valueOf(transaction.getType()),
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
                            TransactionType.valueOf(transaction.getType()),
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
        return new Transaction(
                dto.getType().name(),
                dto.getAmount(),
                userService.getCurrentUser(),
                categoryService.findOrThrowException(dto.getCategory()),
                dto.getDescription(),
                dto.getPaymentMethod()
        );
    }

    private List<Transaction> getFilteredListWithMonthAndYear(int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return transactionRepository.findByUpdatedAtBetween(start, end);
    }

    private double getMonthlyExpenses(int month, int year){
        return getFilteredListWithMonthAndYear(month, year)
                .stream()
                .filter(transaction -> transaction.getType().equals(TransactionType.EXPENSES.name()))
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

}
