package com.shivan.wakeWeb.wakeWeb.entities;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
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
    @OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PingLogs> pingLogs;
}