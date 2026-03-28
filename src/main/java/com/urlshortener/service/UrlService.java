package com.urlshortener.service;

import com.urlshortener.model.Url;
import com.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UrlService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private UrlRepository urlRepository;

    public Url shortenUrl(String originalUrl) {
        // Check if URL already exists
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        // Generate unique short code
        String shortCode;
        do {
            shortCode = generateShortCode();
        } while (urlRepository.existsByShortCode(shortCode));

        Url url = new Url(originalUrl, shortCode);
        return urlRepository.save(url);
    }

    public Url shortenUrlWithCustomCode(String originalUrl, String customCode) {
        if (urlRepository.existsByShortCode(customCode)) {
            throw new IllegalArgumentException("Custom short code already exists");
        }

        Url url = new Url(originalUrl, customCode);
        return urlRepository.save(url);
    }

    public Optional<Url> getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }

    @Transactional
    public void incrementClickCount(Url url) {
        url.incrementClickCount();
        urlRepository.save(url);
    }

    private String generateShortCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}