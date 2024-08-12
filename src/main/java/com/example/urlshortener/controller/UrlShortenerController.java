package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        String shortUrl = urlShortenerService.shortenUrl(originalUrl);
        return ResponseEntity.ok(shortUrl);
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateUrl(@RequestParam String shortUrl, @RequestParam String newDestinationUrl) {
        boolean updated = urlShortenerService.updateUrl(shortUrl, newDestinationUrl);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{shortCode}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortCode) {
        String fullUrl = urlShortenerService.getOriginalUrl(shortCode);
        try {
            response.sendRedirect(fullUrl);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }

    @PostMapping("/update-expiry")
    public ResponseEntity<Boolean> updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd) {
        boolean updated = urlShortenerService.updateExpiry(shortUrl, daysToAdd);
        return ResponseEntity.ok(updated);
    }
}