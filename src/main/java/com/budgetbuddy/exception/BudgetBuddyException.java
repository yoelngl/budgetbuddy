package com.budgetbuddy.exception;

/**
 * Base exception for all BudgetBuddy domain errors.
 * All custom exceptions extend this class (Inheritance).
 */
public class BudgetBuddyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BudgetBuddyException(String message) {
        super(message);
    }

    public BudgetBuddyException(String message, Throwable cause) {
        super(message, cause);
    }
}
