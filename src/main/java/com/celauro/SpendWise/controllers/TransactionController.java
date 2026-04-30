package com.celauro.SpendWise.controllers;

import com.celauro.SpendWise.dtos.NetStatsDTO;
import com.celauro.SpendWise.dtos.TransactionDTO;
import com.celauro.SpendWise.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spendwise/api")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/transaction")
    public TransactionDTO save(@RequestBody @Valid TransactionDTO request){
        return transactionService.saveTransaction(request);
    }

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transaction/{id}")
    public TransactionDTO getTransaction(@PathVariable long id){
        return transactionService.getTransaction(id);
    }

    @DeleteMapping("/transaction/{id}")
    public TransactionDTO deleteTransaction(@PathVariable long id){
        return transactionService.deleteTransaction(id);
    }

    @PutMapping("/transaction/{id}")
    public TransactionDTO editTransaction(
            @PathVariable long id,
            @RequestBody @Valid TransactionDTO request
    ){
        return transactionService.updateTransaction(id, request);
    }

    @GetMapping("/transactions/month")
    public NetStatsDTO getFilteredList(
            @RequestParam("month") int month,
            @RequestParam("year") int year
    ){
        return transactionService.getMonthlyNet(month, year);
    }
}
