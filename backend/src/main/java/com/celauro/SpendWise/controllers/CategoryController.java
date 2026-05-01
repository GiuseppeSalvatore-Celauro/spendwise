package com.celauro.SpendWise.controllers;

import com.celauro.SpendWise.dtos.CategoryDTO;
import com.celauro.SpendWise.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PostMapping("/category")
    public CategoryDTO saveCategory(@RequestBody @Valid CategoryDTO request){
        return categoryService.saveCategory(request);
    }
}
