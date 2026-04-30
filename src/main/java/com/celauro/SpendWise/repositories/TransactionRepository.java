package com.celauro.SpendWise.repositories;

import com.celauro.SpendWise.entity.Transaction;
import com.celauro.SpendWise.utils.CategoryTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTransactionById(long id);
    List<Transaction> findByUpdatedAtBetween(LocalDate start, LocalDate end);


    @Query(value = """
    SELECT c.category AS category,
    SUM(
        CASE
            WHEN t.type = "INCOME" THEN t.amount
            ELSE -t.amount
        END
    ) AS total
    FROM Transaction t
    JOIN t.category c
    WHERE MONTH(t.updatedAt) = :month AND YEAR(t.updatedAt) = :year
    GROUP BY c.category
""")
    List<CategoryTotal> getTotalsByCategory(
            @Param("month") int month,
            @Param("year") int year
    );
}
