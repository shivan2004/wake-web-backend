package com.shivan.wakeWeb.wakeWeb.dto;

import com.shivan.wakeWeb.wakeWeb.entities.Url;
import com.shivan.wakeWeb.wakeWeb.entities.enums.PingStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class PingLogsDTO {

    private Long id;

    private PingStatus pingStatus;

    private LocalDateTime timeStamp;

}
