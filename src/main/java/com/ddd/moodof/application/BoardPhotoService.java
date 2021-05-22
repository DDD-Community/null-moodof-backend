package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.BoardPhotoDTO;
import com.ddd.moodof.application.verifier.BoardPhotoVerifier;
import com.ddd.moodof.domain.model.board.photo.BoardPhoto;
import com.ddd.moodof.domain.model.board.photo.BoardPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardPhotoService {
    private final BoardPhotoRepository boardPhotoRepository;
    private final BoardPhotoVerifier boardPhotoVerifier;

    public BoardPhotoDTO.BoardPhotoResponse addPhoto(Long userId, BoardPhotoDTO.AddBoardPhoto request) {
        BoardPhoto boardPhoto = boardPhotoVerifier.toEntity(userId, request.getStoragePhotoId(), request.getBoardId());
        BoardPhoto saved = boardPhotoRepository.save(boardPhoto);
        return BoardPhotoDTO.BoardPhotoResponse.from(saved);
    }

    public void removePhoto(Long userId, Long id) {
        BoardPhoto boardPhoto = boardPhotoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 BoardPhoto, id : " + id));

        if (boardPhoto.isUserNotEqual(userId)) {
            throw new IllegalArgumentException("로그인한 유저와 BoardPhoto를 생성한 유저의 아이디가 다릅니다.");
        }

        boardPhotoRepository.deleteById(id);
    }
}
