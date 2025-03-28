package com.rhed.blog_backend.api.graphql;

import com.rhed.blog_backend.domain.post.Post;
import com.rhed.blog_backend.domain.post.PostService;
import com.rhed.blog_backend.domain.user.UserService;
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
    @Autowired
    private UserService userService;

    // Single helper method to sync the current authenticated user
    private void syncAuthenticatedUser() {
        userService.syncUserFromKeycloak();
    }

    // Single helper method to get authenticated user ID
    private String getAuthenticatedUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<Post> posts() {
        syncAuthenticatedUser();
        return postService.findAll();
    }

    public Post post(String id) {
        syncAuthenticatedUser();
        return postService.findById(id);
    }

    public Post postBySlug(String slug) {
        syncAuthenticatedUser();
        return postService.findBySlug(slug);
    }

    public List<Post> postsByCategory(String categoryId) {
        syncAuthenticatedUser();
        return postService.findByCategoryId(categoryId);
    }

    public List<Post> postsByTag(String tag) {
        syncAuthenticatedUser();
        return postService.findByTag(tag);
    }

    @PreAuthorize("hasRole('writer')")
    public Post createPost(String title, String content, List<String> categoryIds, List<String> tags) {
        syncAuthenticatedUser();
        return postService.createPost(title, content, categoryIds, tags, getAuthenticatedUserId());
    }

    @PreAuthorize("hasRole('writer')")
    public Post updatePost(String id, String title, String content, String status, List<String> categoryIds, List<String> tags) {
        syncAuthenticatedUser();
        return postService.updatePost(id, title, content, status, categoryIds, tags, getAuthenticatedUserId());
    }

    @PreAuthorize("hasRole('admin')")
    public Boolean deletePost(String id) {
        syncAuthenticatedUser();
        return postService.deletePost(id);
    }
}