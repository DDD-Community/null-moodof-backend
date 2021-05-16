package com.ddd.moodof.domain.model.board.photo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class BoardPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storagePhotoId;

    private Long boardId;

    private Long userId;

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedDate
    private LocalDateTime lastModifiedDate;

    public boolean isUserNotEqual(Long userId) {
        return !this.userId.equals(userId);
    }
}
