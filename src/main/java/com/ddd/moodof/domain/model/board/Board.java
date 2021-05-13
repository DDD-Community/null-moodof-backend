package com.ddd.moodof.domain.model.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long previousBoardId;

    private Long userId;

    private String name;

    private Long categoryId;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public void updatePreviousBoardId(Long userId, Long previousBoardId) {
        verify(userId);
        this.previousBoardId = previousBoardId;
    }

    private void verify(Long userId) {
        if (isNotEqual(userId)) {
            throw new IllegalArgumentException("userId가 일치하지 않습니다.");
        }
    }

    public void changeName(String name, Long userId) {
        verify(userId);
        this.name = name;
    }

    public boolean isNotEqual(Long userId) {
        return !this.userId.equals(userId);
    }
}
