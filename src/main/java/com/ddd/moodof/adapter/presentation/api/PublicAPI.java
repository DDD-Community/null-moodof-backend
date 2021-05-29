package com.ddd.moodof.adapter.presentation.api;
import com.ddd.moodof.adapter.presentation.LoginUserId;
import com.ddd.moodof.application.dto.BoardPhotoDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PublicAPI {
    @GetMapping("/boards/{sharedKey}")
    ResponseEntity<List<BoardPhotoDTO.BoardPhotoResponse>> findAllByBoard(@PathVariable String sharedKey);

//    @GetMapping("/boards/{sharedId}")
//    ResponseEntity<List<CategoryDTO.CategoryWithBoardResponse>> getSharedBoard(@PathVariable String sharedId);

    @GetMapping("/boards/{sharedId}/detail/{id}")
    ResponseEntity<StoragePhotoDTO.StoragePhotoDetailResponse> getSharedBoardDetail(@PathVariable String sharedId,@PathVariable Long id);


}
