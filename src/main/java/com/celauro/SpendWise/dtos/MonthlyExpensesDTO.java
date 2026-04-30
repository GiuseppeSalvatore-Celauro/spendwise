package com.celauro.SpendWise.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyExpensesDTO {
    private int month;
    private Double expenses;
}
