package com.rhed.blog_backend.domain.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;              // Khóa chính (từ Keycloak sub)
    private String username;        // Tên hiển thị
    private String email;           // Email (duy nhất)
    private String role;            // Vai trò (reader, writer, admin)
    private LocalDateTime createdAt;// Thời gian tạo
}