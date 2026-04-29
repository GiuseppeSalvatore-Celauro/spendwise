package com.celauro.SpendWise.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {
    @NotBlank(message = "type required")
    private String type;

    private int amount;

    @NotBlank(message = "category required")
    private String category;

    private long date;

    @NotBlank(message = "description required")
    private String description;
}
