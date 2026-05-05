package com.celauro.SpendWise.services;

import com.celauro.SpendWise.dtos.CategoryDTO;
import com.celauro.SpendWise.entity.Category;
import com.celauro.SpendWise.entity.User;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public List<CategoryDTO> getAllCategories() {
        User currentUser = userService.getCurrentUser();
        log.info("Requested all user categories - userId={} email={}", currentUser.getId(), currentUser.getEmail());

        List<Category> categories = categoryRepository.findAllByUser(currentUser);
        log.info("Sending all user categories - categoriesSize={} userId={} email={}", categories.size(), currentUser.getId(), currentUser.getEmail());
        return toListOfDto_Category(categories);
    }

    public CategoryDTO saveCategory(CategoryDTO request) {
        User currentUser = userService.getCurrentUser();
        log.info("Requested to create a new category - userId={} email={}", currentUser.getId(), currentUser.getEmail());

        Category category = new Category(
                request.getCategory(),
                currentUser
        );

        categoryRepository.save(category);
        log.info("New category created successfully - categoryId={} userId={} email={}", category.getId(), currentUser.getId(), currentUser.getEmail());
        return toDto(category);
    }


    // Helper methods
    public Category findOrThrowException(String category){
        User currentUser = userService.getCurrentUser();
        log.info("Requested to find a category - userId={} email={}", currentUser.getId(), currentUser.getEmail());
        Category foundCategory =  categoryRepository.findByCategory(category).orElseThrow(()->new NotFoundException("Category not found"));

        if(currentUser != foundCategory.getUser()){
            log.warn("Tried using a not owned category - userId={} email={}", currentUser.getId(), currentUser.getEmail());
            throw new NotFoundException("this category does not exist");
        }

        log.info("Sending required category - categoryId={} userId={} email={}", foundCategory.getId(), currentUser.getId(), currentUser.getEmail());
        return foundCategory;
    }

    public CategoryDTO toDto(Category category) {
        return new CategoryDTO(
                category.getCategory()
        );
    }

    private List<CategoryDTO> toListOfDto_Category(List<Category> categories) {
        return categories.stream()
                .map(category -> new CategoryDTO(
                        category.getCategory()
                ))
                .toList();
    }
}
