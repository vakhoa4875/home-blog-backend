package com.rhed.blog_backend.repository;

import com.rhed.blog_backend.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Tìm user theo email
    User findByEmail(String email);
}