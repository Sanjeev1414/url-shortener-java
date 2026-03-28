package com.urlshortener.controller;

import com.urlshortener.model.Url;
import com.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shortenUrl(@Valid @RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "URL is required"));
        }

        Url url = urlService.shortenUrl(originalUrl);
        Map<String, String> response = new HashMap<>();
        response.put("originalUrl", url.getOriginalUrl());
        response.put("shortUrl", "http://localhost:8080/" + url.getShortCode());
        response.put("shortCode", url.getShortCode());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/shorten/custom")
    public ResponseEntity<Map<String, String>> shortenUrlWithCustomCode(@Valid @RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        String customCode = request.get("customCode");

        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "URL is required"));
        }
        if (customCode == null || customCode.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Custom code is required"));
        }

        try {
            Url url = urlService.shortenUrlWithCustomCode(originalUrl, customCode);
            Map<String, String> response = new HashMap<>();
            response.put("originalUrl", url.getOriginalUrl());
            response.put("shortUrl", "http://localhost:8080/" + url.getShortCode());
            response.put("shortCode", url.getShortCode());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<Map<String, Object>> getUrlStats(@PathVariable String shortCode) {
        Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);
        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            Map<String, Object> stats = new HashMap<>();
            stats.put("originalUrl", url.getOriginalUrl());
            stats.put("shortCode", url.getShortCode());
            stats.put("clickCount", url.getClickCount());
            stats.put("createdAt", url.getCreatedAt());
            return ResponseEntity.ok(stats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/docs")
    public ResponseEntity<Map<String, Object>> getApiDocs() {
        Map<String, Object> docs = new HashMap<>();
        docs.put("title", "URL Shortener API");
        docs.put("version", "1.0");
        docs.put("description", "A REST API for shortening URLs with click tracking");

        Map<String, Object> endpoints = new HashMap<>();

        Map<String, Object> shortenEndpoint = new HashMap<>();
        shortenEndpoint.put("method", "POST");
        shortenEndpoint.put("path", "/api/shorten");
        shortenEndpoint.put("description", "Shorten a URL");
        shortenEndpoint.put("body", Map.of("url", "string (required) - The URL to shorten"));
        shortenEndpoint.put("response", Map.of("shortUrl", "string", "shortCode", "string", "originalUrl", "string"));
        endpoints.put("shorten", shortenEndpoint);

        Map<String, Object> customShortenEndpoint = new HashMap<>();
        customShortenEndpoint.put("method", "POST");
        customShortenEndpoint.put("path", "/api/shorten/custom");
        customShortenEndpoint.put("description", "Shorten a URL with custom code");
        customShortenEndpoint.put("body", Map.of("url", "string (required)", "customCode", "string (required)"));
        customShortenEndpoint.put("response", Map.of("shortUrl", "string", "shortCode", "string", "originalUrl", "string"));
        endpoints.put("shortenCustom", customShortenEndpoint);

        Map<String, Object> redirectEndpoint = new HashMap<>();
        redirectEndpoint.put("method", "GET");
        redirectEndpoint.put("path", "/{shortCode}");
        redirectEndpoint.put("description", "Redirect to original URL");
        redirectEndpoint.put("response", "Redirects to original URL");
        endpoints.put("redirect", redirectEndpoint);

        Map<String, Object> statsEndpoint = new HashMap<>();
        statsEndpoint.put("method", "GET");
        statsEndpoint.put("path", "/api/stats/{shortCode}");
        statsEndpoint.put("description", "Get URL statistics");
        statsEndpoint.put("response", Map.of("originalUrl", "string", "shortCode", "string", "clickCount", "number", "createdAt", "timestamp"));
        endpoints.put("stats", statsEndpoint);

        docs.put("endpoints", endpoints);
        return ResponseEntity.ok(docs);
    }
}