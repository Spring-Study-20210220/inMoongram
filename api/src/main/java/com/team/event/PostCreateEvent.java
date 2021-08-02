package com.team.event;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PostCreateEvent {
    private Long postId;
    private List<MultipartFile> multipartFiles;

    public PostCreateEvent(Long postId, List<MultipartFile> multipartFiles) {
        this.postId = postId;
        this.multipartFiles = multipartFiles;
    }
}
