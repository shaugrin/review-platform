// CategoryService.java
package com.reviewplatform.service;

import com.reviewplatform.dto.CategoryDto;
import com.reviewplatform.exception.ResourceAlreadyExistsException;
import com.reviewplatform.exception.ResourceNotFoundException;
import com.reviewplatform.model.Category;
import com.reviewplatform.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
    }

    @Transactional
    public Category createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ResourceAlreadyExistsException("Category already exists with name: " + categoryDto.getName());
        }

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setIcon(categoryDto.getIcon());

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = getCategoryById(categoryId);

        if (categoryDto.getName() != null && !category.getName().equals(categoryDto.getName())) {
            if (categoryRepository.existsByName(categoryDto.getName())) {
                throw new ResourceAlreadyExistsException("Category already exists with name: " + categoryDto.getName());
            }
            category.setName(categoryDto.getName());
        }

        if (categoryDto.getDescription() != null) {
            category.setDescription(categoryDto.getDescription());
        }

        if (categoryDto.getIcon() != null) {
            category.setIcon(categoryDto.getIcon());
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }
}