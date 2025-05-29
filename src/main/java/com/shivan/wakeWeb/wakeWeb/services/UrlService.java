package com.shivan.wakeWeb.wakeWeb.services;


import com.shivan.wakeWeb.wakeWeb.dto.AdminUrlsResponseDTO;
import com.shivan.wakeWeb.wakeWeb.dto.UrlResponseDTO;

import java.util.List;

public interface UrlService {
    List<UrlResponseDTO> getUrlsOfUser();

    void addUrl(String url, String title);

    void retryScheduling(Long id);

    void deleteUrl(Long id);

    void pauseScheduling(Long id);

    Long getActiveUrls();

    Long getTotalUrlsCount();

    List<AdminUrlsResponseDTO> getAllUrls();
}
