package com.ddd.moodof.adapter.infrastructure.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ddd.moodof.domain.model.storage.photo.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class S3FileUploader implements FileUploader {

    private static final String BUCKET_NAME = "moodof-images";

    private final AWSCredentials awsCredentials;

    @Override
    public String upload(String base64, Long userId) {
        byte[] decode = Base64.getDecoder().decode(base64.substring(base64.indexOf(",") + 1));
        InputStream inputStream = new ByteArrayInputStream(decode);

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();

        ObjectMetadata metadata = new ObjectMetadata();
        setMetadata(base64, decode, metadata);

        String fileName = userId + "-" + LocalDateTime.now().toString();
        amazonS3.putObject(BUCKET_NAME, fileName, inputStream, metadata);
        amazonS3.setObjectAcl(BUCKET_NAME, fileName, CannedAccessControlList.PublicRead);

        return amazonS3.getUrl(BUCKET_NAME, fileName).toString();
    }

    private void setMetadata(String base64, byte[] decode, ObjectMetadata metadata) {
        metadata.setContentLength(decode.length);
        String contentType = base64.substring(base64.indexOf(':') + 1, base64.indexOf(';'));
        metadata.setContentType(contentType);
        metadata.setCacheControl("public, max-age=31536000");
    }
}
