package com.shivan.wakeWeb.wakeWeb.controllers;


import com.shivan.wakeWeb.wakeWeb.dto.AdminUrlsResponseDTO;
import com.shivan.wakeWeb.wakeWeb.dto.UrlResponseDTO;
import com.shivan.wakeWeb.wakeWeb.services.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UrlService urlService;

    @GetMapping("/getActiveUrlsCount")
    public ResponseEntity<Long> getActiveUrls() {
        return ResponseEntity.ok(urlService.getActiveUrls());
    }

    @GetMapping("/getTotalUrlsCount")
    public ResponseEntity<Long> getTotalUrlsCount() {
        return ResponseEntity.ok(urlService.getTotalUrlsCount());
    }

    @GetMapping("/getAllUrls")
    public ResponseEntity<List<AdminUrlsResponseDTO>> getAllUrls() {
        return ResponseEntity.ok(urlService.getAllUrls());
    }
}
