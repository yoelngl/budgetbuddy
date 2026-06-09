package com.budgetbuddy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.budgetbuddy.model.Category;
import com.budgetbuddy.repository.CategoryRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            log.info("Seeding {} default categories...", Category.DEFAULT_CATEGORIES.size());
            for (String[] data : Category.DEFAULT_CATEGORIES) {
                categoryRepository.save(new Category(data[0], data[1]));
            }
            log.info("Default categories seeded successfully.");
        }
    }
}
