package com.rhed.blog_backend.repository;

import com.rhed.blog_backend.domain.post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    // Tìm bài viết theo danh mục
    List<Post> findByCategoryIds(String categoryId);

    // Tìm bài viết theo tag
    List<Post> findByTags(String tag);

    // Tìm bài viết theo slug
    Post findBySlug(String slug);
}