package com.forum.service;

import com.forum.model.admin.dto.CategoryDTO;
import com.forum.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories();
    void createCategory(CategoryDTO categoryDTO);
    void updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
