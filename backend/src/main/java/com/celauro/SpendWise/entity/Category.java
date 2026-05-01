package com.celauro.SpendWise.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Category(String category, User user){
        this.category = category;
        this.user = user;
    }
}
