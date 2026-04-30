package com.celauro.SpendWise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private LocalDate date;
    private String description;

    public Transaction(String type, double amount, Category category,  String description) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = LocalDate.now();
        this.description = description;
    }
}
