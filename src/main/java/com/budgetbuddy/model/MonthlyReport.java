package com.budgetbuddy.model;

import java.util.ArrayList;
import java.util.List;

public class MonthlyReport {

    private int               month;
    private int               year;
    private double            totalIncome;
    private double            totalExpense;
    private List<Transaction> transactions;

    public MonthlyReport() {}

    public MonthlyReport(int month, int year) {
        this.month        = month;
        this.year         = year;
        this.totalIncome  = 0;
        this.totalExpense = 0;
        this.transactions = new ArrayList<>();
    }

    public void generate(List<Transaction> allTransactions) {
        this.transactions.clear();
        this.totalIncome  = 0;
        this.totalExpense = 0;

        for (Transaction t : allTransactions) {
            if (t.getDate().getMonthValue() == month && t.getDate().getYear() == year) {
                transactions.add(t);
                if (t instanceof Income) {
                    totalIncome += t.getAmount();
                } else if (t instanceof Expense) {
                    totalExpense += t.getAmount();
                }
            }
        }
    }

    public int               getMonth()        { return month; }
    public int               getYear()         { return year; }
    public double            getTotalIncome()   { return totalIncome; }
    public double            getTotalExpense()  { return totalExpense; }
    public double            getBalance()       { return totalIncome - totalExpense; }
    public List<Transaction> getTransactions()  { return transactions; }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
