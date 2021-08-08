package com.team.post.dto.request;

import com.team.post.dto.input.SavePostInput;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostRequest {
    private String content;

    private List<MultipartFile> postImages;

    private List<Long> taggedUserIds;

    private List<String> taggedKeywords;

    @Builder
    public SavePostRequest(String content, List<MultipartFile> postImages, List<Long> taggedUserIds, List<String> taggedKeywords) {
        this.content = content;
        this.postImages = postImages;
        this.taggedUserIds = taggedUserIds;
        this.taggedKeywords = taggedKeywords;
    }

    public SavePostInput toInput() {
        return SavePostInput.builder()
                .content(content)
                .postImages(postImages)
                .taggedUserIds(taggedUserIds)
                .taggedKeywords(taggedKeywords)
                .build();
    }
}
