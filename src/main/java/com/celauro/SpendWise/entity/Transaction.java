package com.celauro.SpendWise.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;
    private int amount;
    private String category;
    private long date;
    private String description;

    public Transaction(String type, int amount, String category, long date,  String description) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }
}
