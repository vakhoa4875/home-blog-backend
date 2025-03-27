package com.rhed.blog_backend.api.graphql;

import com.rhed.blog_backend.domain.category.Category;
import com.rhed.blog_backend.repository.CategoryRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryResolver implements GraphQLQueryResolver {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    public Category category(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Category> categoriesByPath(String pathPrefix) {
        return categoryRepository.findByPathStartingWith(pathPrefix);
    }
}