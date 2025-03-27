package com.rhed.blog_backend.domain.user;

import com.rhed.blog_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Đồng bộ user từ Keycloak khi đăng nhập lần đầu
    public User syncUserFromKeycloak(String id, String username, String email, String role) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(role);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
        return user;
    }
}