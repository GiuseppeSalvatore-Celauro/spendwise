package com.celauro.SpendWise.repositories;

import com.celauro.SpendWise.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(long id);
    Optional<Category> findByCategory(String category);
}
