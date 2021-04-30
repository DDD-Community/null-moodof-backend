package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoCreateService;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoragePhotoService {
    private final StoragePhotoRepository storagePhotoRepository;
    private final StoragePhotoCreateService storagePhotoCreateService;

    public StoragePhotoDTO.StoragePhotoResponse create(StoragePhotoDTO.CreateStoragePhoto request, Long userId) {
        StoragePhoto saved = storagePhotoCreateService.create(request.getUri(), request.getRepresentativeColor(), userId);
        return StoragePhotoDTO.StoragePhotoResponse.from(saved);
    }


    public StoragePhotoDTO.StoragePhotoPageResponse findPage(Long userId, int page, int size, String sortBy, boolean descending) {
        List<StoragePhoto> storagePhotos = storagePhotoRepository.findPageByUserId(userId, PageRequest.of(page, size, getSort(sortBy, descending)));
        long count = storagePhotoRepository.countByUserId(userId);
        return new StoragePhotoDTO.StoragePhotoPageResponse((long) Math.ceil((double) count / size), StoragePhotoDTO.StoragePhotoResponse.listFrom(storagePhotos));
    }

    private Sort getSort(String sortBy, boolean descending) {
        Sort sort = Sort.by(sortBy);
        if (descending) {
            return sort.descending();
        }
        return sort.ascending();

    }

    public void deleteById(Long userId, Long id) {
        StoragePhoto storagePhoto = storagePhotoRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("요청과 일치하는 보관함 사진이 없습니다. id / userId: " + id + " / " + userId));

        storagePhotoRepository.deleteById(storagePhoto.getId());
    }
}
