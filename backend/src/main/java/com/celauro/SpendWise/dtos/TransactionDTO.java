package com.celauro.SpendWise.dtos;

import com.celauro.SpendWise.utils.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {
    @NotEmpty(message = "type required")
    private String type;

    @NotNull(message = "amount required")
    @Positive(message = "amount should always be positive")
    private double amount;

    @NotBlank(message = "category required")
    private String category;

    private LocalDate date;

    @NotBlank(message = "description required")
    private String description;

    @NotBlank(message = "payment method required")
    private String paymentMethod;
}
