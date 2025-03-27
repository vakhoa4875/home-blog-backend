package com.rhed.blog_backend.api.graphql;

import com.rhed.blog_backend.domain.user.User;
import com.rhed.blog_backend.repository.UserRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserResolver implements GraphQLQueryResolver {

    @Autowired
    private UserRepository userRepository;

    public List<User> users() {
        return userRepository.findAll();
    }

    public User user(String id) {
        return userRepository.findById(id).orElse(null);
    }
}