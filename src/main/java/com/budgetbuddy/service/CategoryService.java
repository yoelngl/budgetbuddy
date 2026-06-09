package com.budgetbuddy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.budgetbuddy.exception.InvalidOperationException;
import com.budgetbuddy.exception.ResourceNotFoundException;
import com.budgetbuddy.model.Category;
import com.budgetbuddy.repository.CategoryRepository;


@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori", id));
    }

    public Category addCategory(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new InvalidOperationException("Nama kategori tidak boleh kosong");
        }
        if (categoryRepository.existsByNameIgnoreCase(name.trim())) {
            throw new InvalidOperationException("Kategori '" + name + "' sudah ada");
        }
        return categoryRepository.save(new Category(name.trim(), description));
    }

    public void deleteCategory(Long id) {
        getCategoryById(id); 
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(Long id, String name, String description) {
        Category existing = getCategoryById(id);
        existing.setName(name);
        existing.setDescription(description);
        return categoryRepository.save(existing);
    }
}
