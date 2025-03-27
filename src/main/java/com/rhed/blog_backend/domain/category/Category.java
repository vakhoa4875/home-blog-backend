package com.rhed.blog_backend.domain.category;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private String id;              // Khóa chính (_id trong MongoDB)
    private String name;            // Tên danh mục (e.g., "Web Development")
    private String slug;            // URL thân thiện (e.g., "web-development")
    private String path;            // Đường dẫn cây (e.g., "/technology/programming/web-development")
    private String parentId;        // ID danh mục cha (null nếu gốc)
    private int level;              // Độ sâu trong cây (0, 1, 2, ...)
    private String description;     // Mô tả ngắn (cho SEO)
    private int postCount;          // Số bài viết trong danh mục
}