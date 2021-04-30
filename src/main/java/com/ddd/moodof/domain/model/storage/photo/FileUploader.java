package com.ddd.moodof.domain.model.storage.photo;

public interface FileUploader {
    String upload(String base64, Long userId);
}
