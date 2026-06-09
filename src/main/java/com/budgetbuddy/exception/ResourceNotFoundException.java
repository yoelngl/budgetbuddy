package com.budgetbuddy.exception;

public class ResourceNotFoundException extends BudgetBuddyException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " dengan ID " + id + " tidak ditemukan");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
