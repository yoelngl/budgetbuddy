package com.budgetbuddy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.budgetbuddy.model.MonthlyReport;
import com.budgetbuddy.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/monthly")
    public MonthlyReport getMonthlyReport(
            @RequestParam Long userId,
            @RequestParam int  month,
            @RequestParam int  year) {
        return reportService.generateMonthlyReport(userId, month, year);
    }
}
