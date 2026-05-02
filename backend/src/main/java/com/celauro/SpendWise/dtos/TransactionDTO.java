package com.celauro.SpendWise.dtos;

import com.celauro.SpendWise.utils.TransactionType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {
    @NotBlank(message = "type required")
    private TransactionType type;

    @NotBlank(message = "amount required")
    private double amount;

    @NotBlank(message = "category required")
    private String category;

    private LocalDate date;

    @NotBlank(message = "description required")
    private String description;

    @NotBlank(message = "payment method required")
    private String paymentMethod;
}
