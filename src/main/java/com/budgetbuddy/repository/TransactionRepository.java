package com.budgetbuddy.repository;

import com.budgetbuddy.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for Transaction (polymorphic: Income + Expense).
 * Query method names are parsed by Spring Data into JPQL automatically.
 *
 * Static collection: results are returned as List<Transaction> —
 * at runtime each element is Income or Expense (polymorphism via JPA SINGLE_TABLE).
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /** All transactions for a user, newest first */
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    /** Transactions in a date range, newest first */
    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);

    /** Case-insensitive description search, newest first */
    List<Transaction> findByUserIdAndDescriptionContainingIgnoreCaseOrderByDateDesc(
            Long userId, String keyword);
}
