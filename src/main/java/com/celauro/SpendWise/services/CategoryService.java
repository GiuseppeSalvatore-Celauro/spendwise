package com.celauro.SpendWise.services;

import com.celauro.SpendWise.dtos.CategoryDTO;
import com.celauro.SpendWise.entity.Category;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return toListOfDto_Category(categories);
    }

    public CategoryDTO saveCategory(CategoryDTO request) {
        Category category = new Category(request.getCategory());
        categoryRepository.save(category);
        return toDto(category);
    }

    public Category findOrThrowException(String category){
        return categoryRepository.findByCategory(category).orElseThrow(()->new NotFoundException("Category not found"));
    }

    private CategoryDTO toDto(Category category) {
        return new CategoryDTO(category.getCategory());
    }

    private List<CategoryDTO> toListOfDto_Category(List<Category> categories) {
        return categories.stream()
                .map(category -> new CategoryDTO(category.getCategory()))
                .toList();
    }
}
