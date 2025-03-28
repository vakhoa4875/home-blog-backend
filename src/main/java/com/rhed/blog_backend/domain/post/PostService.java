package com.rhed.blog_backend.domain.post;

import com.rhed.blog_backend.domain.category.CategoryService;
import com.rhed.blog_backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final CategoryService categoryService;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(String id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found: " + id));
    }

    public Post findBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    public List<Post> findByCategoryId(String categoryId) {
        return postRepository.findByCategoryIds(categoryId);
    }

    public List<Post> findByTag(String tag) {
        return postRepository.findByTags(tag);
    }

    public Post createPost(String title, String content, List<String> categoryIds, List<String> tags, String authorId) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthorId(authorId);
        post.setStatus("draft");
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setCategoryIds(categoryIds);
        // Lấy categoryPaths từ CategoryService
        post.setCategoryPaths(categoryIds.stream()
                .map(id -> categoryService.findById(id).getPath())
                .collect(Collectors.toList()));
        post.setTags(tags);
        post.setSlug(generateSlug(title));
        post.setViews(0);
        post.setLikes(0);

        // Tăng postCount cho các danh mục
        categoryIds.forEach(categoryService::incrementPostCount);

        return postRepository.save(post);
    }

    public Post updatePost(String id, String title, String content, String status, List<String> categoryIds, List<String> tags, String authorId) {
        Post post = findById(id);
        // Kiểm tra quyền sở hữu
        if (!post.getAuthorId().equals(authorId)) {
            throw new RuntimeException("Unauthorized: You can only edit your own posts");
        }
        if (title != null) post.setTitle(title);
        if (content != null) post.setContent(content);
        if (status != null) post.setStatus(status);
        if (categoryIds != null) {
            post.setCategoryIds(categoryIds);
            post.setCategoryPaths(categoryIds.stream()
                    .map(catId -> categoryService.findById(catId).getPath())
                    .collect(Collectors.toList()));
        }
        if (tags != null) post.setTags(tags);
        if (title != null) post.setSlug(generateSlug(title));
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public boolean deletePost(String id) {
        postRepository.deleteById(id);
        return true;
    }

    private String generateSlug(String title) {
        return title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-+$", "");
    }
}