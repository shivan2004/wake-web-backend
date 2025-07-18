package com.shivan.wakeWeb.wakeWeb.dto;

import com.shivan.wakeWeb.wakeWeb.entities.Url;
import com.shivan.wakeWeb.wakeWeb.entities.enums.PingStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PingLogsDTO {

    private Long id;

    private PingStatus pingStatus;

    private LocalDateTime timeStamp;

}
