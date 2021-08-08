package com.team.post.dto.input;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostInput {
    private Long userId;
    private String content;
    private List<MultipartFile> postImages;
    private List<Long> taggedUserIds;
    private List<String> taggedKeywords;

    @Builder
    public SavePostInput(Long userId, String content, List<MultipartFile> postImages, List<Long> taggedUserIds, List<String> taggedKeywords) {
        this.userId = userId;
        this.content = content;
        this.postImages = postImages;
        this.taggedUserIds = taggedUserIds;
        this.taggedKeywords = taggedKeywords;
    }
}
