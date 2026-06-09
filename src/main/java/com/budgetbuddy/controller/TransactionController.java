package com.budgetbuddy.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.budgetbuddy.model.Transaction;
import com.budgetbuddy.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAll(@RequestParam Long userId) {
        return transactionService.getAllTransactions(userId);
    }

    @PostMapping("/income")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction addIncome(@RequestParam Long userId,
                                 @RequestBody Map<String, String> body) {
        double    amount = Double.parseDouble(body.get("amount"));
        LocalDate date   = LocalDate.parse(body.get("date"));
        return transactionService.addIncome(
                userId, amount, date,
                body.getOrDefault("description", ""),
                body.getOrDefault("source", "")
        );
    }

    @PostMapping("/expense")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction addExpense(@RequestParam Long userId,
                                  @RequestBody Map<String, String> body) {
        double    amount     = Double.parseDouble(body.get("amount"));
        LocalDate date       = LocalDate.parse(body.get("date"));
        Long      categoryId = Long.parseLong(body.get("categoryId"));
        return transactionService.addExpense(
                userId, amount, date,
                body.getOrDefault("description", ""),
                categoryId
        );
    }

    @PutMapping("/{id}")
    public Transaction update(@PathVariable Long id,
                              @RequestBody Map<String, String> body) {
        double    amount = Double.parseDouble(body.get("amount"));
        LocalDate date   = LocalDate.parse(body.get("date"));
        return transactionService.updateTransaction(
                id, amount, date,
                body.getOrDefault("description", "")
        );
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return Map.of("message", "Transaksi berhasil dihapus");
    }

    @GetMapping("/filter")
    public List<Transaction> filter(
            @RequestParam Long userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String keyword) {

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end   = endDate   != null ? LocalDate.parse(endDate)   : null;
        return transactionService.filterTransactions(userId, type, categoryId, start, end, keyword);
    }
}
