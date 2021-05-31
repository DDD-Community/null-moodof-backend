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

    private String sharedKey;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public void changePreviousBoardId(Long previousBoardId, Long userId) {
        verify(userId);
        if (this.previousBoardId.equals(previousBoardId)) {
            throw new IllegalArgumentException("previousBoardId가 같습니다.");
        }
        this.previousBoardId = previousBoardId;
    }

    public void changeName(String name, Long userId) {
        verify(userId);
        this.name = name;
    }

    public void updateSequence(Long previousBoardId, Long categoryId, Long userId) {
        verify(userId);
        this.previousBoardId = previousBoardId;
        this.categoryId = categoryId;
    }

    public void updateSharedkey(String sharedKey, Long userId){
        verify(userId);
        this.sharedKey = sharedKey;
    }

    private void verify(Long userId) {
        if (isUserNotEqual(userId)) {
            throw new IllegalArgumentException("userId가 일치하지 않습니다.");
        }
    }

    public boolean isUserNotEqual(Long userId) {
        return !this.userId.equals(userId);
    }

    public boolean isCategoryNotEqual(Long categoryId) {
        return !this.categoryId.equals(categoryId);
    }
}
