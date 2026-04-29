package com.celauro.SpendWise.repositories;

import com.celauro.SpendWise.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTransactionById(long id);
    List<Transaction> findByDateBetween(LocalDate start, LocalDate end);
}
