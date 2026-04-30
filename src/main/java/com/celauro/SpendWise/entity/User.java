package com.celauro.SpendWise.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "last_update")
    private LocalDate updatedAt;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public void disableAccount(){
        this.isActive = false;
    }

    public void updateAccount(){
        this.updatedAt = LocalDate.now();
    }
}
