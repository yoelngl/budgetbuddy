package com.budgetbuddy.model;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("INCOME")
public class Income extends Transaction {

    private String source;

    public Income() {}

    public Income(double amount, LocalDate date, String description, String source, Long userId) {
        super(amount, date, description, userId);
        this.source = source;
    }

    @Override
    public String getType() {
        return "INCOME";
    }

    public String getSource()              { return source; }
    public void   setSource(String source) { this.source = source; }

    @Override
    public String toString() {
        return "Income{id=" + getId() + ", amount=" + getAmount()
                + ", date=" + getDate() + ", source=" + source + "}";
    }
}
