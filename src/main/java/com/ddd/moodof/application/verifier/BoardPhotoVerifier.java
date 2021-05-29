package com.ddd.moodof.application.verifier;

import com.ddd.moodof.domain.model.board.BoardRepository;
import com.ddd.moodof.domain.model.board.photo.BoardPhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BoardPhotoVerifier {
    private final StoragePhotoRepository storagePhotoRepository;
    private final BoardRepository boardRepository;

    public List<BoardPhoto> toEntities(Long userId, List<Long> storagePhotoIds, Long boardId) {
        if (storagePhotoRepository.existsByIdInAndUserId(storagePhotoIds, userId) && boardRepository.existsByIdAndUserId(boardId, userId)) {
            return storagePhotoIds.stream()
                    .map(storagePhotoId -> new BoardPhoto(null, storagePhotoId, boardId, userId, null, null, null))
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("보관함 사진 또는 보드가 존재하지 않습니다. storagePhotoIds : " +
                storagePhotoIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(",")) +
                ", boardId : " + boardId);
    }
}
