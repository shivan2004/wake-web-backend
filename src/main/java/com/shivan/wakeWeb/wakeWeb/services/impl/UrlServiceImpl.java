package com.shivan.wakeWeb.wakeWeb.services.impl;

import com.shivan.wakeWeb.wakeWeb.dto.AdminUrlsResponseDTO;
import com.shivan.wakeWeb.wakeWeb.dto.UrlResponseDTO;
import com.shivan.wakeWeb.wakeWeb.entities.Url;
import com.shivan.wakeWeb.wakeWeb.entities.User;
import com.shivan.wakeWeb.wakeWeb.exceptions.AlreadyExistsException;
import com.shivan.wakeWeb.wakeWeb.exceptions.ResourceNotFoundException;
import com.shivan.wakeWeb.wakeWeb.repositories.UrlRepository;
import com.shivan.wakeWeb.wakeWeb.services.UrlService;
import com.shivan.wakeWeb.wakeWeb.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public List<UrlResponseDTO> getUrlsOfUser() {
        User user = userService.getCurrentUser();
        List<Url> urlsOfUser = urlRepository.findAllByUser(user);
        return urlsOfUser.stream()
                .map(url ->modelMapper.map(url, UrlResponseDTO.class))
                .toList();
    }

    @Override
    public void addUrl( String url, String title) {
        User user = userService.getCurrentUser();
        if(doesUrlExists(url)) throw new AlreadyExistsException("Url already exists, try deleting last entry");
        Url urlToBeSaved = Url.builder()
                .url(url)
                .pingLogs(new ArrayList<>())
                .negativePings(0)
                .positivePings(0)
                .user(user)
                .title(title)
                .isActive(true)
                .expiryDate(LocalDateTime.now().plusYears(3))
                .build();

        Url savedUrl = urlRepository.save(urlToBeSaved);
    }

    @Override
    public void retryScheduling(Long id) {
        Url url = getUrlById(id);
        User user = userService.getCurrentUser();
        if(!url.getUser().equals(user)) throw new RuntimeException("Illegal access");

        if(url.isActive()) throw new RuntimeException("Url is active, it might take some time to refresh");
        url.setActive(true);
        url.setNegativePings(0);
        url.setPositivePings(0);
        url.setExpiryDate(LocalDateTime.now().plusYears(3));
        url.getPingLogs().clear();

        urlRepository.save(url);
    }

    @Override
    public void deleteUrl(Long id) {
        Url url = getUrlById(id);
        User user = userService.getCurrentUser();
        if(!url.getUser().equals(user)) throw new RuntimeException("Illegal access");

        urlRepository.delete(url);
    }

    @Override
    public void pauseScheduling(Long id) {
        Url url = getUrlById(id);
        User user = userService.getCurrentUser();
        if(!url.getUser().equals(user)) throw new RuntimeException("Illegal access");

        if(!url.isActive()) throw new RuntimeException("Url is not active");

        url.setActive(false);
        urlRepository.save(url);

    }

    @Override
    public Long getActiveUrls() {
        return urlRepository.countAllByIsActiveIsTrue();
    }

    @Override
    public Long getTotalUrlsCount() {
        return urlRepository.count();
    }

    @Override
    public List<AdminUrlsResponseDTO> getAllUrls() {
        List<Url> allUrls =  urlRepository.findAll();

        //todo; chweck once
        return allUrls.stream()
                .map(url -> modelMapper.map(url, AdminUrlsResponseDTO.class))
                .toList();
    }

    private boolean doesUrlExists(String url) {
        List<Url> url1 = urlRepository.findAllByUrl(url);
        return !url1.isEmpty();
    }

    private Url getUrlById(Long id) {
        return urlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Url not found"));
    }
}
