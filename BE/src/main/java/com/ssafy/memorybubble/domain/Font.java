package com.ssafy.memorybubble.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Font {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "font_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "font_name", nullable = false, length = 50)
    private String name;

    @Column(name = "font_path", nullable = false)
    private String path;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FontStatus fontStatus; // 폰트 생성 상태

    @Builder
    public Font(User user, String name, String path) {
        this.user = user;
        this.name = name;
        this.path = path;
        this.fontStatus = FontStatus.REQUESTED;
    }

    public void updateStatus() {
        this.fontStatus = FontStatus.DONE;
    }
}
