package com.urlshortener.controller;

import com.urlshortener.model.Url;
import com.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Controller
public class RedirectController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    public ResponseEntity<String> home() throws IOException {
        Resource resource = new ClassPathResource("static/index.html");
        String html = new String(Files.readAllBytes(resource.getFile().toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @GetMapping("/style.css")
    public ResponseEntity<String> getCss() throws IOException {
        Resource resource = new ClassPathResource("static/style.css");
        String css = new String(Files.readAllBytes(resource.getFile().toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/css"))
                .body(css);
    }

    @GetMapping("/script.js")
    public ResponseEntity<String> getJs() throws IOException {
        Resource resource = new ClassPathResource("static/script.js");
        String js = new String(Files.readAllBytes(resource.getFile().toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/javascript"))
                .body(js);
    }

    @GetMapping("/{shortCode}")
    public void redirectToOriginalUrl(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);
        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            urlService.incrementClickCount(url);
            response.sendRedirect(url.getOriginalUrl());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Short URL not found");
        }
    }
}