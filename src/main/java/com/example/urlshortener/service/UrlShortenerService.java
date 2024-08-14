package com.example.urlshortener.service;

import com.example.urlshortener.entity.ShortenedUrl;
import com.example.urlshortener.repository.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
//import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

@Service
public class UrlShortenerService {
    @Autowired
    private ShortenedUrlRepository repository;

    public String shortenUrl(String originalUrl) {
        String shortCode = generateShortCode();
        System.out.println("ShortCode: " + shortCode);
        LocalDateTime expiresAt = LocalDateTime.now().plusMonths(10);
        ShortenedUrl shortenedUrl = new ShortenedUrl(originalUrl, shortCode, LocalDateTime.now(), expiresAt);
        ShortenedUrl saved = repository.save(shortenedUrl);
        System.out.println("Saved: " + saved);
        repository.save(shortenedUrl);
        return shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found"))
                .getOriginalUrl();
    }

    public boolean updateUrl(String shortCode, String newDestinationUrl) {
        ShortenedUrl shortenedUrl = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found"));
        shortenedUrl.setOriginalUrl(newDestinationUrl);
        repository.save(shortenedUrl);
        return true;
    }

    public boolean updateExpiry(String shortCode, int daysToAdd) {
        ShortenedUrl shortenedUrl = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found"));
        shortenedUrl.setExpiresAt(shortenedUrl.getExpiresAt().plusDays(daysToAdd));
        repository.save(shortenedUrl);
        return true;
    }

    private String generateShortCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        StringBuilder shortCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            shortCode.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortCode.toString();
    }
}