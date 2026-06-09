package com.budgetbuddy.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.budgetbuddy.exception.ResourceNotFoundException;
import com.budgetbuddy.model.Category;
import com.budgetbuddy.model.Expense;
import com.budgetbuddy.model.Income;
import com.budgetbuddy.model.Transaction;
import com.budgetbuddy.repository.TransactionRepository;
import com.budgetbuddy.util.AppConstants;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService           userService;
    private final CategoryService       categoryService;

    public TransactionService(TransactionRepository transactionRepository,
                               UserService userService,
                               CategoryService categoryService) {
        this.transactionRepository = transactionRepository;
        this.userService           = userService;
        this.categoryService       = categoryService;
    }

    public Income addIncome(Long userId, double amount, LocalDate date,
                            String description, String source) {
        userService.getUserById(userId);  // validates user exists (throws if not)
        Income income = new Income(amount, date, description, source, userId);
        return (Income) transactionRepository.save(income);
    }

    public Expense addExpense(Long userId, double amount, LocalDate date,
                              String description, Long categoryId) {
        userService.getUserById(userId);  // validates user exists
        Category category = categoryService.getCategoryById(categoryId);
        Expense expense = new Expense(amount, date, description,
                categoryId, category.getName(), userId);
        return (Expense) transactionRepository.save(expense);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByDateDesc(userId);
    }

    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaksi", id));
    }

    public Transaction updateTransaction(Long id, double amount, LocalDate date, String description) {
        Transaction existing = getTransactionById(id);
        existing.setAmount(amount);
        existing.setDate(date);
        existing.setDescription(description);
        return transactionRepository.save(existing);
    }

    public void deleteTransaction(Long id) {
        getTransactionById(id);  // throws if not found
        transactionRepository.deleteById(id);
    }

    /**
     * Unified filter method — applies type, categoryId, date range, and keyword filters.
     * Demonstrates polymorphism: filters on Transaction but checks instanceof for Expense.
     */
    @Transactional(readOnly = true)
    public List<Transaction> filterTransactions(Long userId, String type, Long categoryId,
                                                 LocalDate startDate, LocalDate endDate,
                                                 String keyword) {
        List<Transaction> base;

        if (startDate != null && endDate != null) {
            base = transactionRepository
                    .findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
        } else if (keyword != null && !keyword.isBlank()) {
            base = transactionRepository
                    .findByUserIdAndDescriptionContainingIgnoreCaseOrderByDateDesc(userId, keyword.trim());
        } else {
            base = transactionRepository.findByUserIdOrderByDateDesc(userId);
        }

        return base.stream()
                .filter(t -> type == null || type.isBlank()
                        || t.getType().equalsIgnoreCase(type))
                .filter(t -> categoryId == null
                        || (t instanceof Expense
                            && categoryId.equals(((Expense) t).getCategoryId())))
                .collect(Collectors.toList());
    }

    /** Returns total income amount for a user in the given month/year */
    @Transactional(readOnly = true)
    public double getTotalIncomeForMonth(Long userId, int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end   = start.withDayOfMonth(start.lengthOfMonth());
        return transactionRepository
                .findByUserIdAndDateBetweenOrderByDateDesc(userId, start, end)
                .stream()
                .filter(t -> AppConstants.TX_TYPE_INCOME.equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
