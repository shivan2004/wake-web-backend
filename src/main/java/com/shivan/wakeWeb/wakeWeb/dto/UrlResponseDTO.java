package com.shivan.wakeWeb.wakeWeb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.List;

@Data
@NoArgsConstructor
public class UrlResponseDTO {
    private Long id;
    private String title;
    private String url;
    private boolean isActive;
    private List<PingLogsDTO> pingLogs;
}
