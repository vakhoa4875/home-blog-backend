package com.rhed.blog_backend.repository;

import com.rhed.blog_backend.domain.category.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    // Truy vấn danh mục con dựa trên path
    List<Category> findByPathStartingWith(String pathPrefix);
}