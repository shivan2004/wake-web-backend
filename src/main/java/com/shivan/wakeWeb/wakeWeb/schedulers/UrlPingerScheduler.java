package com.shivan.wakeWeb.wakeWeb.schedulers;

import com.shivan.wakeWeb.wakeWeb.entities.Url;
import com.shivan.wakeWeb.wakeWeb.entities.enums.PingStatus;
import com.shivan.wakeWeb.wakeWeb.entities.PingLogs;
import com.shivan.wakeWeb.wakeWeb.repositories.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlPingerScheduler {

    private final UrlRepository urlRepository;

    private final WebClient webClient = WebClient.builder()
            .build();

    @Transactional
    @Scheduled(fixedRate = 600_0) // every 10 minutes
//    @Scheduled(cron = "0 * * * * *") // every 1 minute at 0 seconds
    public void pingUrls() {
        log.info("Running URL ping scheduler...");

        List<Url> activeUrls = urlRepository.findAllByIsActiveIsTrue();

        for (Url url : activeUrls) {
            log.info("{}", url);
            //todo; transactional, after each iteration it shuld store in db
            try {
                var response = webClient.get()
                        .uri(url.getUrl())
                        .retrieve()
                        .toBodilessEntity()
                        .timeout(Duration.ofSeconds(10))
                        .block(); // 🔁 makes it synchronous

                boolean isSuccess = response != null && response.getStatusCode().value() == 200;

                PingLogs logEntry = PingLogs.builder()
                        .pingStatus(isSuccess ? PingStatus.OK : PingStatus.ERROR)
                        .timeStamp(LocalDateTime.now(ZoneOffset.UTC))
                        .url(url)
                        .build();

                if (url.getPingLogs().size() == 4) url.getPingLogs().remove(3);
                url.getPingLogs().add(logEntry);

                if (isSuccess) {
                    url.setPositivePings(Math.min(url.getPositivePings() + 1, 4));
                    url.setNegativePings(0);
                } else {
                    url.setNegativePings(Math.min(url.getNegativePings() + 1, 2));
                }

                if (url.getNegativePings() >= 2) {
                    url.setActive(false);
                }

                log.info("{}", url);
                urlRepository.save(url); // saves both Url + cascade PingLogs

            } catch (Exception e) {
//                log.error("Ping failed for URL: {}", url.getUrl(), e);

                PingLogs logEntry = PingLogs.builder()
                        .pingStatus(PingStatus.ERROR)
                        .timeStamp(LocalDateTime.now(ZoneOffset.UTC))
                        .url(url)
                        .build();

                if (url.getPingLogs().size() == 4) url.getPingLogs().remove(3);
                url.getPingLogs().add(logEntry);

                url.setNegativePings(Math.min(url.getNegativePings() + 1, 2));

                if (url.getNegativePings() >= 2) {
                    url.setActive(false);
                }
                log.info("{}", url);
                urlRepository.save(url);
            }
        }
    }
}