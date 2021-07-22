package com.team.post.util;

import com.team.post.PostImage;
import com.team.post.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUploader {
    private final PostImageService postImageService;

    @Value("${file.dir}")
    private String location;

    public PostImage storeImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String uploadFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(uploadFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return postImageService.save(new PostImage(uploadFileName, storeFileName));
    }

    public List<PostImage> storeImages(List<MultipartFile> multipartFiles) {
        List<PostImage> postImages = new ArrayList<>();
        multipartFiles.stream()
                .filter(it -> !it.isEmpty())
                .forEach(it -> {
                    try {
                        postImages.add(storeImage(it));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return postImages;
    }

    private String createStoreFileName(String uploadFileName) {
        String extension = extractExtension(uploadFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + extension;
    }

    // 파일 확장자를 가져오는 메소드
    private String extractExtension(String uploadFileName) {
        int idx = uploadFileName.lastIndexOf(".");
        return uploadFileName.substring(idx + 1);
    }

    private String getFullPath(String filename) {
        String absolutePath = new File(location).getAbsolutePath();
        return absolutePath + "/" + filename;
    }
}
