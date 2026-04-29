package com.celauro.SpendWise.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NetStatsDTO {
    private TotalExpensesDTO totalExpenses;
    private TotalIncomeDTO totalIncome;
}
