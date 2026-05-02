package com.celauro.SpendWise.repositories;

import com.celauro.SpendWise.entity.Category;
import com.celauro.SpendWise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUser(User user);
    Optional<Category> findByCategory(String category);
}
