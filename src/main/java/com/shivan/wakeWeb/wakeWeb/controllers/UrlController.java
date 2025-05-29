package com.shivan.wakeWeb.wakeWeb.controllers;


import com.shivan.wakeWeb.wakeWeb.dto.AddUrlDTO;
import com.shivan.wakeWeb.wakeWeb.dto.UrlResponseDTO;
import com.shivan.wakeWeb.wakeWeb.services.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @GetMapping("/getAllUrls")
    public ResponseEntity<List<UrlResponseDTO>> getAllUrlsOfUser() {
        return ResponseEntity.ok(urlService.getUrlsOfUser());
    }

    @PostMapping("/addUrl")
    public ResponseEntity<?> addUrl(@RequestBody AddUrlDTO addUrlDTO) {
        urlService.addUrl(addUrlDTO.getUrl(), addUrlDTO.getTitle());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/retry/{id}")
    public ResponseEntity<?> retryScheduling(@PathVariable Long id) {
        urlService.retryScheduling(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUrl(@PathVariable Long id) {
        urlService.deleteUrl(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/pause/{id}")
    public ResponseEntity<?> pauseScheduling(@PathVariable Long id) {
        urlService.deleteUrl(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
