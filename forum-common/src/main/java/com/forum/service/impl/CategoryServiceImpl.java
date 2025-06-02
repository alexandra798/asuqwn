// CategoryServiceImpl.java
package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.mapper.CategoryMapper;
import com.forum.model.admin.dto.CategoryDTO;
import com.forum.model.entity.Category;
import com.forum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        return categoryMapper.findAll();
    }

    @Override
    @Transactional
    public void createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setGuidelines(categoryDTO.getGuidelines());
        category.setPostCount(0);
        category.setCreatedAt(LocalDateTime.now());
        category.setCreatedBy(1L); // 这里应该从当前用户获取

        categoryMapper.insert(category);
        log.info("分类创建成功: categoryId={}, name={}", category.getId(), category.getName());
    }

    @Override
    @Transactional
    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryMapper.findById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        category.setName(categoryDTO.getName());
        category.setGuidelines(categoryDTO.getGuidelines());

        categoryMapper.update(category);
        log.info("分类更新成功: categoryId={}", id);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryMapper.findById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        if (category.getPostCount() > 0) {
            throw new BusinessException("该分类下还有帖子，无法删除");
        }

        categoryMapper.deleteById(id);
        log.info("分类删除成功: categoryId={}", id);
    }
}