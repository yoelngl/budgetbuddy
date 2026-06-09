package com.budgetbuddy.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Income.class,  name = "INCOME"),
    @JsonSubTypes.Type(value = Expense.class, name = "EXPENSE")
})
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Jumlah harus lebih dari 0")
    @Column(nullable = false)
    private double amount;

    @NotNull(message = "Tanggal tidak boleh kosong")
    @Column(nullable = false)
    private LocalDate date;

    private String description;

    @NotNull(message = "UserId tidak boleh kosong")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Transaction() {}

    public Transaction(double amount, LocalDate date, String description, Long userId) {
        this.amount      = amount;
        this.date        = date;
        this.description = description;
        this.userId      = userId;
    }

   
    @JsonIgnore
    public abstract String getType();

    public Long      getId()                      { return id; }
    public void      setId(Long id)               { this.id = id; }

    public double    getAmount()                  { return amount; }
    public void      setAmount(double amount)     { this.amount = amount; }

    public LocalDate getDate()                    { return date; }
    public void      setDate(LocalDate date)      { this.date = date; }

    public String    getDescription()             { return description; }
    public void      setDescription(String desc)  { this.description = desc; }

    public Long      getUserId()                  { return userId; }
    public void      setUserId(Long userId)       { this.userId = userId; }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", type=" + getType()
                + ", amount=" + amount + ", date=" + date + "}";
    }
}
