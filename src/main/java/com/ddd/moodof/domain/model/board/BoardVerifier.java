package com.ddd.moodof.domain.model.board;

import com.ddd.moodof.domain.model.category.Category;
import com.ddd.moodof.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BoardVerifier {
    private final CategoryRepository categoryRepository;

    public Board toEntity(Long previousBoardId, Long categoryId, String name, Long userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 category, id = " + categoryId));

        if (category.isNotEqual(userId)) {
            throw new IllegalArgumentException("카테고리 생성자와 로그인 유저의 아이디가 다릅니다.");
        }

        return new Board(null, previousBoardId, userId, name, categoryId, null, null);
    }
}
