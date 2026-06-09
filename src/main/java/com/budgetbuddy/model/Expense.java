package com.budgetbuddy.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EXPENSE")
public class Expense extends Transaction {

    @Column(name = "category_id")
    private Long   categoryId;

    @Column(name = "category_name")
    private String categoryName;

    public Expense() {}

    public Expense(double amount, LocalDate date, String description,
                   Long categoryId, String categoryName, Long userId) {
        super(amount, date, description, userId);
        this.categoryId   = categoryId;
        this.categoryName = categoryName;
    }

    @Override
    public String getType() {
        return "EXPENSE";
    }

    public Long   getCategoryId()                      { return categoryId; }
    public void   setCategoryId(Long categoryId)       { this.categoryId = categoryId; }

    public String getCategoryName()                    { return categoryName; }
    public void   setCategoryName(String categoryName) { this.categoryName = categoryName; }

    @Override
    public String toString() {
        return "Expense{id=" + getId() + ", amount=" + getAmount()
                + ", date=" + getDate() + ", category=" + categoryName + "}";
    }
}
