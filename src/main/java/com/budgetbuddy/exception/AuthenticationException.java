package com.budgetbuddy.exception;

public class AuthenticationException extends BudgetBuddyException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }
}
