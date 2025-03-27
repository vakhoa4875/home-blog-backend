package com.rhed.blog_backend.domain.category;

import com.rhed.blog_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(String id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found: " + id));
    }

    public List<Category> findByPathPrefix(String pathPrefix) {
        return categoryRepository.findByPathStartingWith(pathPrefix);
    }

    // Tăng postCount khi thêm bài viết
    public void incrementPostCount(String categoryId) {
        Category category = findById(categoryId);
        category.setPostCount(category.getPostCount() + 1);
        categoryRepository.save(category);
    }
}