package com.ddd.moodof.adapter.presentation;


import com.ddd.moodof.adapter.infrastructure.configuration.EncryptConfig;
import com.ddd.moodof.adapter.infrastructure.security.encrypt.EncryptUtil;
import com.ddd.moodof.adapter.presentation.api.PublicAPI;
import com.ddd.moodof.application.BoardService;
import com.ddd.moodof.application.CategoryService;
import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// todo 존재하는 id값인지 확인하기
// todo public 경로로 direct를 진행한다.
// todo public 경로의 cors mapping을 풀어준다.
@RequiredArgsConstructor
@RequestMapping(PublicController.API_PUBLIC)
@RestController
public class PublicController implements PublicAPI {
    public static final String API_PUBLIC = "/api/public";
    public static final String BOARDS = "/boards/{id}";
    private final EncryptConfig encryptConfig;
    private final BoardService boardService;
    private final CategoryService categoryService;

    @GetMapping(BOARDS)
    public ResponseEntity<List<CategoryDTO.CategoryResponse>> getSharedBoard(@PathVariable String sharedKey){
        List<CategoryDTO.CategoryResponse> responses = null;
//                = boardService.findBySharedKey(sharedKey);
        return ResponseEntity.ok(responses);
    }
}
