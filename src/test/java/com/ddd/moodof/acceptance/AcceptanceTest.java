package com.ddd.moodof.acceptance;

import com.ddd.moodof.adapter.infrastructure.security.TokenProvider;
import com.ddd.moodof.application.dto.*;
import com.ddd.moodof.domain.model.user.AuthProvider;
import com.ddd.moodof.domain.model.user.User;
import com.ddd.moodof.domain.model.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static com.ddd.moodof.adapter.presentation.BoardController.API_BOARD;
import static com.ddd.moodof.adapter.presentation.BoardPhotoController.API_BOARD_PHOTO;
import static com.ddd.moodof.adapter.presentation.CategoryController.API_CATEGORY;
import static com.ddd.moodof.adapter.presentation.StoragePhotoController.API_STORAGE_PHOTO;
import static com.ddd.moodof.adapter.presentation.TagAttachmentController.API_TAG_ATTACHMENT;
import static com.ddd.moodof.adapter.presentation.TagController.API_TAG;
import static com.ddd.moodof.adapter.presentation.TrashPhotoController.API_TRASH_PHOTO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    private static final String BEARER = "Bearer ";

    protected Long userId;

    protected String nickName;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        User user = signUp();
        userId = user.getId();
        nickName = user.getNickname();
    }

    protected User signUp() {
        return userRepository.save(new User(null, "test@test.com", "password", "nickname", "profileUrl", null, null, AuthProvider.google, "providerId"));
    }

    protected CategoryDTO.CategoryResponse 카테고리_생성(Long userId, String title, Long previousId){
        CategoryDTO.CreateCategoryRequest request = new CategoryDTO.CreateCategoryRequest(title,previousId);
        return postWithLogin(request, API_CATEGORY, CategoryDTO.CategoryResponse.class, userId);
    }

    protected CategoryDTO.CategoryResponse 카테고리_순서_변경(Long id,Long previousId, Long userId, String property){
        CategoryDTO.UpdateOrderCategoryRequest request = new CategoryDTO.UpdateOrderCategoryRequest(previousId);
        return putPropertyWithLogin(request, id, API_CATEGORY, CategoryDTO.CategoryResponse.class, userId, property);
    }

    protected StoragePhotoDTO.StoragePhotoResponse 보관함사진_생성(Long userId, String photoUri, String representativeColor) {
        StoragePhotoDTO.CreateStoragePhoto request = new StoragePhotoDTO.CreateStoragePhoto(photoUri, representativeColor);
        return postWithLogin(request, API_STORAGE_PHOTO, StoragePhotoDTO.StoragePhotoResponse.class, userId);
    }

    protected List<TrashPhotoDTO.TrashPhotoCreatedResponse> 보관함사진_휴지통_이동(List<Long> storagePhotoIds, Long userId) {
        return postListWithLogin(new TrashPhotoDTO.CreateTrashPhotos(storagePhotoIds), API_TRASH_PHOTO, TrashPhotoDTO.TrashPhotoCreatedResponse.class, userId);
    }

    protected BoardDTO.BoardSharedResponse 보드_공유하기_생성(Long id, Long userId){
        BoardDTO.BoardSharedRequest request = new BoardDTO.BoardSharedRequest(id);
        return postWithLogin(request, API_BOARD+ "/shared", BoardDTO.BoardSharedResponse.class, userId);
    }

    protected TagDTO.TagCreatedResponse 태그_생성(Long userId, Long storagePhotoId, String name) {
        TagDTO.CreateRequest request = new TagDTO.CreateRequest(storagePhotoId, name);
        try {
            String token = tokenProvider.createToken(userId);
            String body = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(API_TAG)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(body)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            return objectMapper.readValue(result.getResponse().getContentAsString(), TagDTO.TagCreatedResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected TagDTO.TagResponse 태그_수정(Long id, Long userId, String name) {
        TagDTO.UpdateRequest request = new TagDTO.UpdateRequest(name);
        return putWithLogin(request, id, API_TAG, TagDTO.TagResponse.class, userId);
    }

    protected BoardDTO.BoardResponse 보드_생성(Long userId, Long previousBoardId, Long categoryId, String name) {
        BoardDTO.CreateBoard request = new BoardDTO.CreateBoard(previousBoardId, categoryId, name);
        return postWithLogin(request, API_BOARD, BoardDTO.BoardResponse.class, userId);
    }

    protected <T, U> U postWithLogin(T request, String uri, Class<U> response, Long userId) {
        try {
            String token = tokenProvider.createToken(userId);
            String body = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(body)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.matchesRegex(uri + "/\\d*")))
                    .andReturn();

            return objectMapper.readValue(result.getResponse().getContentAsString(), response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected <T, U> List<U> postListWithLogin(T request, String uri, Class<U> response, Long userId) {
        try {
            String token = tokenProvider.createToken(userId);
            String body = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(body)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, response);

            return objectMapper.readValue(result.getResponse().getContentAsString(), collectionType);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected <T, U> U putWithLogin(T request, Long resourceId, String uri, Class<U> response, Long userId) {
        try {
            String token = tokenProvider.createToken(userId);
            String body = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(uri + "/{id}", resourceId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(body)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            return objectMapper.readValue(result.getResponse().getContentAsString(), response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected <T, U> U putPropertyWithLogin(T request, Long resourceId, String uri, Class<U> response, Long userId, String property) {
        try {
            String token = tokenProvider.createToken(userId);
            String body = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(uri + "/{id}/{property}", resourceId, property)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(body)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            return objectMapper.readValue(result.getResponse().getContentAsString(), response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected <T> T getWithLogin(String uri, Class<T> response, Long userId) {
        try {
            String token = tokenProvider.createToken(userId);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            return objectMapper.readValue(result.getResponse().getContentAsString(), response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected <T> List<T> getListWithLogin(String uri, Class<T> response, Long userId) {
        try {
            String token = tokenProvider.createToken(userId);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, response);

            return objectMapper.readValue(result.getResponse().getContentAsString(), collectionType);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected void deleteWithLogin(String uri, Long resourceId, Long userId) {
        try {
            String token = tokenProvider.createToken(userId);

            mockMvc.perform(MockMvcRequestBuilders.delete(uri + "/{id}", resourceId)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected <T> void deleteListWithLogin(String uri, T request, Long userId) {
        try {
            String token = tokenProvider.createToken(userId);


            mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER + token))
                    .andExpect(MockMvcResultMatchers.status().isNoContent())
                    .andReturn();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }


    protected <T> List<T> getListNotLogin(String uri, Class<T> response, String property) {
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri + "/{property}", property)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, response);

            return objectMapper.readValue(result.getResponse().getContentAsString(), collectionType);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }
    protected <T> T getNotLogin(String uri, Class<T> response) {
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            return objectMapper.readValue(result.getResponse().getContentAsString(), response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AssertionError("test fails");
        }
    }

    protected TagAttachmentDTO.TagAttachmentResponse 태그붙이기_생성(Long userId, Long storagePhotoId, Long tagId) {
        TagAttachmentDTO.CreateTagAttachment request = new TagAttachmentDTO.CreateTagAttachment(storagePhotoId, tagId);
        return postWithLogin(request, API_TAG_ATTACHMENT, TagAttachmentDTO.TagAttachmentResponse.class, userId);
    }

    protected List<BoardPhotoDTO.BoardPhotoResponse> 보드_사진_복수_생성(Long userId, List<Long> storagePhotoIds, Long boardId) {
        BoardPhotoDTO.AddBoardPhoto request = new BoardPhotoDTO.AddBoardPhoto(storagePhotoIds, boardId);
        return postListWithLogin(request, API_BOARD_PHOTO, BoardPhotoDTO.BoardPhotoResponse.class, userId);
    }
}
