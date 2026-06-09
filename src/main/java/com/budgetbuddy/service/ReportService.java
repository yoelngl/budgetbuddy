package com.budgetbuddy.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.budgetbuddy.model.MonthlyReport;
import com.budgetbuddy.model.Transaction;
import com.budgetbuddy.repository.TransactionRepository;

@Service
public class ReportService {

    private final TransactionRepository transactionRepository;

    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public MonthlyReport generateMonthlyReport(Long userId, int month, int year) {
        LocalDate start     = LocalDate.of(year, month, 1);
        LocalDate end       = start.withDayOfMonth(start.lengthOfMonth());
        List<Transaction> txs = transactionRepository
                .findByUserIdAndDateBetweenOrderByDateDesc(userId, start, end);
        MonthlyReport report = new MonthlyReport(month, year);
        report.generate(txs);
        return report;
    }
}
