package com.celauro.SpendWise.services;

import com.celauro.SpendWise.dtos.TransactionDTO;
import com.celauro.SpendWise.entity.Transaction;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<TransactionDTO> getAllTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        return toListOfResponse_transactions(transactions);
    }

    public TransactionDTO saveTransaction(TransactionDTO request){
        transactionRepository.save(createTransactionEntity(request));
        return request;
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
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setDescription(request.getDescription());

        transactionRepository.save(transaction);

        return toTransactionDTO(transaction);
    }

//  Helper methods
    private Transaction getByIdOrThrowException(long id){
        return transactionRepository.findTransactionById(id).orElseThrow(() -> new NotFoundException ("Transazione inesistente"));
    }

    private TransactionDTO toTransactionDTO(Transaction transaction){
        return new TransactionDTO(
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDate(),
                transaction.getDescription()
        );
    }

    private List<TransactionDTO> toListOfResponse_transactions(List<Transaction> transactions){
        return transactions.stream()
                .map(transaction ->
                    new TransactionDTO(
                            transaction.getType(),
                            transaction.getAmount(),
                            transaction.getCategory(),
                            transaction.getDate(),
                            transaction.getDescription()
                    )
                )
                .collect(Collectors.toList());
    }

    private Transaction createTransactionEntity(TransactionDTO dto){
        return new Transaction(
                dto.getType(),
                dto.getAmount(),
                dto.getCategory(),
                dto.getDate(),
                dto.getDescription()
        );
    }


}
