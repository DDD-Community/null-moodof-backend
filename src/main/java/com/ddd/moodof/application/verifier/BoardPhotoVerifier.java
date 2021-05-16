package com.ddd.moodof.application.verifier;

import com.ddd.moodof.domain.model.board.BoardRepository;
import com.ddd.moodof.domain.model.board.photo.BoardPhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BoardPhotoVerifier {
    private final StoragePhotoRepository storagePhotoRepository;
    private final BoardRepository boardRepository;

    public BoardPhoto toEntity(Long userId, Long storagePhotoId, Long boardId) {
        if (storagePhotoRepository.existsByIdAndUserId(storagePhotoId, userId) && boardRepository.existsByIdAndUserId(boardId, userId)) {
            return new BoardPhoto(null, storagePhotoId, boardId, userId, null, null);
        }
        throw new IllegalArgumentException("보관함 사진 또는 보드가 존재하지 않습니다. storagePhotoId : " + storagePhotoId + ", boardId : " + boardId);
    }
}
