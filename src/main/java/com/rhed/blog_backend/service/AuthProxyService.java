package com.rhed.blog_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthProxyService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.logout-uri}")
    private String logoutUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.redirect-uri}")
    private String redirectUri;

    public ResponseEntity<?> getAccessToken(Map<String, String> credentials) {
        return proxyToKeycloak(buildPasswordGrantForm(credentials));
    }

    public ResponseEntity<?> refreshAccessToken(Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        if (isBlank(refreshToken)) {
            return ResponseEntity.badRequest().body("Missing refresh_token");
        }
        return proxyToKeycloak(buildRefreshGrantForm(refreshToken));
    }

    public ResponseEntity<?> exchangeCode(Map<String, String> body) {
        String code = body.get("code");
        if (isBlank(code)) {
            return ResponseEntity.badRequest().body("Missing authorization code");
        }
        return postForm(tokenUri, buildHeaders(), buildAuthorizationCodeGrantForm(code));
    }

    public ResponseEntity<?> logout(Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        if (isBlank(refreshToken)) {
            return ResponseEntity.badRequest().body("Missing refresh_token");
        }
        return postForm(logoutUri, buildHeaders(), buildLogoutForm(refreshToken));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> buildFormBase() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        return form;
    }

    private MultiValueMap<String, String> buildPasswordGrantForm(Map<String, String> body) {
        MultiValueMap<String, String> form = buildFormBase();
        form.add("grant_type", "password");
        form.add("username", body.get("username"));
        form.add("password", body.get("password"));
        return form;
    }

    private MultiValueMap<String, String> buildRefreshGrantForm(String refreshToken) {
        MultiValueMap<String, String> form = buildFormBase();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);
        return form;
    }

    private MultiValueMap<String, String> buildAuthorizationCodeGrantForm(String code) {
        MultiValueMap<String, String> form = buildFormBase();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUri);
        return form;
    }

    private MultiValueMap<String, String> buildLogoutForm(String refreshToken) {
        MultiValueMap<String, String> form = buildFormBase();
        form.add("refresh_token", refreshToken);
        return form;
    }

    private ResponseEntity<?> proxyToKeycloak(MultiValueMap<String, String> form) {
        return postForm(tokenUri, buildHeaders(), form);
    }

    private ResponseEntity<?> postForm(String url, HttpHeaders headers, MultiValueMap<String, String> form) {
        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            log.warn("Keycloak request failed: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unexpected error while calling Keycloak: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}