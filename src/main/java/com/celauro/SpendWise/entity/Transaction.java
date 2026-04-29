package com.celauro.SpendWise.entity;

import com.celauro.SpendWise.utils.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;
    private double amount;
    private String category;
    private LocalDate date;
    private String description;

    public Transaction(String type, double amount, String category,  String description) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = LocalDate.now();
        this.description = description;
    }
}
