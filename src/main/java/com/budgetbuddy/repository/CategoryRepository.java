package com.budgetbuddy.repository;

import com.budgetbuddy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for Category.
 * Default categories are seeded via DataInitializer on first startup.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);
}
