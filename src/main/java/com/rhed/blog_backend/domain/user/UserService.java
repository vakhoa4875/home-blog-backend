package com.rhed.blog_backend.domain.user;

import com.rhed.blog_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_WRITER = "writer";
    private static final String ROLE_READER = "reader";

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void syncUserFromKeycloak() {
        Jwt jwt = getAuthenticatedJwt();
        String userId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String role = determinePrimaryRole(jwt);

        User user = userRepository.findById(userId).orElseGet(() -> createNewUser(userId, username, email, role));
        updateRoleIfChanged(user, role);
    }

    private Jwt getAuthenticatedJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new RuntimeException("No authenticated JWT user found");
        }
        return (Jwt) authentication.getPrincipal();
    }

    private String determinePrimaryRole(Jwt jwt) {
        List<String> roles = extractRoles(jwt);
        if (roles.contains(ROLE_ADMIN)) {
            return ROLE_ADMIN;
        } else if (roles.contains(ROLE_WRITER)) {
            return ROLE_WRITER;
        }
        return ROLE_READER; // Default
    }

    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> access = Optional.ofNullable(jwt.getClaimAsMap("realm_access"))
                .orElse(jwt.getClaimAsMap("resource_access"));
        return access != null && access.containsKey("roles") ? (List<String>) access.get("roles") : List.of();
    }

    private User createNewUser(String id, String username, String email, String role) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setRole(role);
        newUser.setCreatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    private void updateRoleIfChanged(User user, String newRole) {
        if (!user.getRole().equals(newRole)) {
            user.setRole(newRole);
            userRepository.save(user);
        }
    }
}