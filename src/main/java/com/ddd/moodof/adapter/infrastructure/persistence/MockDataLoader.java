package com.ddd.moodof.adapter.infrastructure.persistence;

import com.ddd.moodof.application.*;
import com.ddd.moodof.application.dto.*;
import com.ddd.moodof.domain.model.user.AuthProvider;
import com.ddd.moodof.domain.model.user.User;
import com.ddd.moodof.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;


@RequiredArgsConstructor
@Profile("dev")
@Component
public class MockDataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final BoardService boardService;
    private final CategoryService categoryService;
    private final StoragePhotoService storagePhotoService;
    private final TagAttachmentService tagAttachmentService;
    private final TagService tagService;
    private final TrashPhotoService trashPhotoService;

    @Value("${dev.user.email}")
    private String email;
    @Value("${dev.user.nickname}")
    private String nickname;
    @Value("${dev.user.profileUrl}")
    private String profileUrl;
    @Value("${dev.user.providerId}")
    private String providerId;

    @Override
    public void run(ApplicationArguments args) {
        User user = userRepository.save(new User(null, email, null, nickname, profileUrl, null, null, AuthProvider.google, providerId));
        Long userId = user.getId();
        IntStream.range(0, 10)
                .forEach(__ -> createPhotos(userId));

        StoragePhotoDTO.StoragePhotoResponse 사진1 = storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("https://pbs.twimg.com/media/ExUElF7VcAMx7jx.jpg", "representativeColor"), userId);
        StoragePhotoDTO.StoragePhotoResponse 사진2 = storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2020/10/PS20100800026.jpg", "representativeColor"), userId);
        StoragePhotoDTO.StoragePhotoResponse 사진3 = storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("https://file2.nocutnews.co.kr/newsroom/image/2021/03/25/202103251659468108_0.jpg", "representativeColor"), userId);
        StoragePhotoDTO.StoragePhotoResponse 사진4 = storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("http://image.kmib.co.kr/online_image/2019/1126/611816110013967774_1.jpg", "representativeColor"), userId);
        StoragePhotoDTO.StoragePhotoResponse 사진5 = storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("http://www.jejuilbo.net/news/photo/202102/157830_101125_2752.jpg", "representativeColor"), userId);
        CategoryDTO.CategoryResponse 카테고리1 = categoryService.create(new CategoryDTO.CreateCategoryRequest("카테고리 1", 0L), userId);
        CategoryDTO.CategoryResponse 카테고리2 = categoryService.create(new CategoryDTO.CreateCategoryRequest("카테고리 2", 카테고리1.getId()), userId);
        CategoryDTO.CategoryResponse 카테고리3 = categoryService.create(new CategoryDTO.CreateCategoryRequest("카테고리 3", 카테고리2.getId()), userId);
        BoardDTO.BoardResponse 보드1 = boardService.create(userId, new BoardDTO.CreateBoard(0L, 카테고리1.getId(), "보드 1"));
        BoardDTO.BoardResponse 보드2 = boardService.create(userId, new BoardDTO.CreateBoard(보드1.getId(), 카테고리1.getId(), "보드 2"));
        BoardDTO.BoardResponse 보드3 = boardService.create(userId, new BoardDTO.CreateBoard(보드2.getId(), 카테고리1.getId(), "보드 3"));
        BoardDTO.BoardResponse 보드4 = boardService.create(userId, new BoardDTO.CreateBoard(0L, 카테고리2.getId(), "보드 4"));
        BoardDTO.BoardResponse 보드5 = boardService.create(userId, new BoardDTO.CreateBoard(보드4.getId(), 카테고리2.getId(), "보드 5"));
        TagDTO.TagResponse 태그1 = tagService.create(new TagDTO.CreateRequest("태그 1"), userId);
        TagDTO.TagResponse 태그2 = tagService.create(new TagDTO.CreateRequest("태그 2"), userId);
        TagDTO.TagResponse 태그3 = tagService.create(new TagDTO.CreateRequest("태그 3"), userId);
        TagDTO.TagResponse 태그4 = tagService.create(new TagDTO.CreateRequest("태그 4"), userId);
        tagAttachmentService.create(userId, new TagAttachmentDTO.CreateTagAttachment(사진1.getId(), 태그1.getId()));
        tagAttachmentService.create(userId, new TagAttachmentDTO.CreateTagAttachment(사진1.getId(), 태그2.getId()));
        tagAttachmentService.create(userId, new TagAttachmentDTO.CreateTagAttachment(사진1.getId(), 태그3.getId()));
        tagAttachmentService.create(userId, new TagAttachmentDTO.CreateTagAttachment(사진2.getId(), 태그1.getId()));
        tagAttachmentService.create(userId, new TagAttachmentDTO.CreateTagAttachment(사진2.getId(), 태그2.getId()));
        tagAttachmentService.create(userId, new TagAttachmentDTO.CreateTagAttachment(사진3.getId(), 태그3.getId()));
        tagAttachmentService.create(userId, new TagAttachmentDTO.CreateTagAttachment(사진3.getId(), 태그1.getId()));
        tagAttachmentService.create(userId, new TagAttachmentDTO.CreateTagAttachment(사진4.getId(), 태그4.getId()));
        trashPhotoService.add(userId, new TrashPhotoDTO.CreateTrashPhotos(List.of(사진4.getId(), 사진5.getId())));
    }

    private void createPhotos(Long userId) {
        storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("https://cdn.mhnse.com/news/photo/202103/71190_41523_2339.jpg", "representativeColor"), userId);
        storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("https://dimg.donga.com/wps/NEWS/IMAGE/2021/01/28/105164933.2.jpg", "representativeColor"), userId);
        storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("http://img.vogue.co.kr/vogue/2021/04/style_607fb99974fc2-751x930.jpg", "representativeColor"), userId);
        storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("https://image.ytn.co.kr/general/jpg/2020/0723/202007231130167111_d.jpg", "representativeColor"), userId);
        storagePhotoService.create(new StoragePhotoDTO.CreateStoragePhoto("https://www.enewstoday.co.kr/news/photo/201909/1336361_400469_529.jpg", "representativeColor"), userId);
    }
}
