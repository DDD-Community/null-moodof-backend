package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.BoardPhotoDTO;
import com.ddd.moodof.application.verifier.BoardPhotoVerifier;
import com.ddd.moodof.domain.model.board.photo.BoardPhoto;
import com.ddd.moodof.domain.model.board.photo.BoardPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardPhotoService {
    private static final long FIRST_PREVIOUS_BOARD_PHOTO_ID = 0L;

    private final BoardPhotoRepository boardPhotoRepository;
    private final BoardPhotoVerifier boardPhotoVerifier;

    public List<BoardPhotoDTO.BoardPhotoResponse> addPhotos(Long userId, BoardPhotoDTO.AddBoardPhoto request) {
        // TODO: 2021/05/25 StoragePhoto 삭제 대응
        List<BoardPhoto> boardPhotos = boardPhotoVerifier.toEntities(userId, request.getStoragePhotoIds(), request.getBoardId());
        Optional<BoardPhoto> recentest = boardPhotoRepository.findFirstByBoardIdOrderByLastModifiedDateDesc(request.getBoardId());
        if (recentest.isPresent()) {
            return BoardPhotoDTO.BoardPhotoResponse.listFrom(saveWithSequence(boardPhotos, recentest.get().getId()));
        }
        return BoardPhotoDTO.BoardPhotoResponse.listFrom(saveWithSequence(boardPhotos, FIRST_PREVIOUS_BOARD_PHOTO_ID));
    }

    private List<BoardPhoto> saveWithSequence(List<BoardPhoto> boardPhotos, Long previousId) {
        List<BoardPhoto> result = new ArrayList<>();
        Long previousBoardPhotoId = previousId;

        for (BoardPhoto boardPhoto : boardPhotos) {
            boardPhoto.setPreviousBoardPhotoId(previousBoardPhotoId);
            BoardPhoto saved = boardPhotoRepository.save(boardPhoto);

            result.add(saved);
            previousBoardPhotoId = saved.getId();
        }
        return result;
    }

    public void removePhoto(Long userId, BoardPhotoDTO.RemoveBoardPhotos request) {
        List<BoardPhoto> boardPhotos = boardPhotoRepository.findAllById(request.getBoardPhotoIds());

        if (boardPhotos.stream().anyMatch(boardPhoto -> boardPhoto.isUserNotEqual(userId))) {
            throw new IllegalArgumentException("로그인한 유저와 BoardPhoto를 생성한 유저의 아이디가 다릅니다.");
        }

        boardPhotoRepository.deleteAll(boardPhotos);
    }

    public List<BoardPhotoDTO.BoardPhotoResponse> findAllByBoardId(Long boardId, Long userId) {
        List<BoardPhoto> boardPhotos = boardPhotoRepository.findAllByBoardIdAndUserId(boardId, userId);
        return BoardPhotoDTO.BoardPhotoResponse.listFrom(boardPhotos);
    }
}
