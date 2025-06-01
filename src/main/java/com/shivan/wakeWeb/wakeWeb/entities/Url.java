package com.shivan.wakeWeb.wakeWeb.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "urls_table")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // <- move nullable here
    private User user;

    @Column(length = 25)
    private String title;

    @Column(unique = true, nullable = false)
    private String url;

    private boolean isActive;

    private Integer positivePings;

    private Integer negativePings;

    private LocalDateTime expiryDate;

    // Persisted: All logs associated with this URL
    @OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("timeStamp DESC") // or ASC for oldest first
    private List<PingLogs> pingLogs;

    @CreationTimestamp
    private LocalDateTime timeStamp;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ", " +
                "url = " + url + ", " +
                "isActive = " + isActive + ", " +
                "positivePings = " + positivePings + ", " +
                "negativePings = " + negativePings + ", " +
                "expiryDate = " + expiryDate + ")";
    }
}