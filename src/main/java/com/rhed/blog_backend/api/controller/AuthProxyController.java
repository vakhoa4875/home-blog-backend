package com.rhed.blog_backend.api.controller;

import com.rhed.blog_backend.service.AuthProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthProxyController {

    private final AuthProxyService authProxyService;

    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(@RequestBody Map<String, String> credentials) {
        return authProxyService.getAccessToken(credentials);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> body) {
        return authProxyService.refreshAccessToken(body);
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<?> exchangeCode(@RequestBody Map<String, String> body) {
        return authProxyService.exchangeCode(body);
    }

    @PostMapping("/oauth/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        return authProxyService.logout(body);
    }
}