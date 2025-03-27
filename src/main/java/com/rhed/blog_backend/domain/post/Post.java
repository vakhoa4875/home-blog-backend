package com.rhed.blog_backend.domain.post;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String id;                  // Khóa chính
    private String title;               // Tiêu đề bài viết
    private String content;             // Nội dung
    private String authorId;            // ID tác giả (từ Keycloak)
    private String status;              // Trạng thái (draft, published)
    private LocalDateTime createdAt;    // Thời gian tạo
    private LocalDateTime updatedAt;    // Thời gian cập nhật
    private List<String> categoryIds;   // Danh sách ID danh mục
    private List<String> categoryPaths; // Đường dẫn danh mục (cho breadcrumb)
    private List<String> tags;          // Danh sách tag
    private String slug;                // URL thân thiện (e.g., "hoc-react-2025")
    private int views;                  // Lượt xem
    private int likes;                  // Lượt thích
}