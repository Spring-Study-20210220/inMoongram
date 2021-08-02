package com.team.post.util;

import com.team.event.PostCreateEvent;
import com.team.post.Post;
import com.team.post.PostImage;
import com.team.post.PostImageService;
import com.team.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional
public class ImageUploader {
    private final PostService postService;
    private final PostImageService postImageService;

    @Value("${file.dir}")
    private String location;

    public PostImage storeImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String uploadFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(uploadFileName);
        String storePath = getFullPath(storeFileName);
        multipartFile.transferTo(new File(storePath));
        return postImageService.save(new PostImage(uploadFileName, storeFileName, storePath));
    }

    @Async
    @EventListener
    public void storeImages(PostCreateEvent event) {
        List<PostImage> postImages = new ArrayList<>();
        event.getMultipartFiles().stream()
                .filter(it -> !it.isEmpty())
                .forEach(it -> {
                    try {
                        postImages.add(storeImage(it));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        Post post = postService.findPostById(event.getPostId());
        post.addImages(postImages);
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
