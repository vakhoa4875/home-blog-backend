package com.rhed.blog_backend.api.graphql;

import com.rhed.blog_backend.domain.post.Post;
import com.rhed.blog_backend.domain.post.PostService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private PostService postService;

    // Queries
    public List<Post> posts() {
        return postService.findAll();
    }

    public Post post(String id) {
        return postService.findById(id);
    }

    public Post postBySlug(String slug) {
        return postService.findBySlug(slug);
    }

    public List<Post> postsByCategory(String categoryId) {
        return postService.findByCategoryId(categoryId);
    }

    public List<Post> postsByTag(String tag) {
        return postService.findByTag(tag);
    }

    // Mutations
    @PreAuthorize("hasRole('writer')")
    public Post createPost(String title, String content, List<String> categoryIds, List<String> tags) {
        String authorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return postService.createPost(title, content, categoryIds, tags, authorId);
    }

    @PreAuthorize("hasRole('writer')")
    public Post updatePost(String id, String title, String content, String status, List<String> categoryIds, List<String> tags) {
        String authorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return postService.updatePost(id, title, content, status, categoryIds, tags, authorId);
    }

    @PreAuthorize("hasRole('admin')")
    public Boolean deletePost(String id) {
        return postService.deletePost(id);
    }
}