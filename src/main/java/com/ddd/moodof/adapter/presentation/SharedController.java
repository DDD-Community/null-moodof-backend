package com.ddd.moodof.adapter.presentation;

import com.ddd.moodof.adapter.presentation.api.SharedAPI;
import com.ddd.moodof.application.BoardService;
import com.ddd.moodof.application.dto.SharedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping(SharedController.API_SHARED)
@RestController
public class SharedController implements SharedAPI {
    public static final String API_SHARED = "/api/shared";
    public static final String BOARDS = "/boards";

    private final BoardService boardService;

    @PostMapping(BOARDS)
    public ResponseEntity<SharedDTO.SharedBoardResponse> create(@RequestBody SharedDTO.SharedBoardRequest request, @LoginUserId Long userId, HttpServletRequest httpServletRequest){
        SharedDTO.SharedBoardResponse response = boardService.createSharedKey(request.getId(), userId, httpServletRequest);
        return ResponseEntity.created(URI.create(API_SHARED + BOARDS + "/" + response.getId())).body(response);
    }

}
