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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String description;

    @Column(name = "payment_method")
    private String paymentMethod;


    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "last_update")
    private LocalDate updatedAt;

    public Transaction(String type, double amount, User user,Category category,  String description, String paymentMethod) {
        this.type = type;
        this.amount = amount;
        this.user = user;
        this.category = category;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.createdAt = LocalDate.now();
        this.updatedAt= LocalDate.now();
    }

    public void updateTransaction(){
        this.updatedAt = LocalDate.now();
    }
}
