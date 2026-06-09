package com.budgetbuddy.exception;

public class InvalidOperationException extends BudgetBuddyException {

    private static final long serialVersionUID = 1L;

    public InvalidOperationException(String message) {
        super(message);
    }
}
