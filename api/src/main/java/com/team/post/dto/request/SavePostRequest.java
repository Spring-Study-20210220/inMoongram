package com.team.post.dto.request;

import com.team.post.dto.input.SavePostInput;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostRequest {
    @NotNull
    private Long userId;

    private String content;

    private List<MultipartFile> postImages;

    private List<Long> taggedUserIds;

    private List<String> taggedKeywords;

    @Builder
    public SavePostRequest(Long userId, String content, List<MultipartFile> postImages, List<Long> taggedUserIds, List<String> taggedKeywords) {
        this.userId = userId;
        this.content = content;
        this.postImages = postImages;
        this.taggedUserIds = taggedUserIds;
        this.taggedKeywords = taggedKeywords;
    }

    public SavePostInput toInput() {
        return SavePostInput.builder()
                .userId(userId)
                .content(content)
                .taggedUserIds(taggedUserIds)
                .taggedKeywords(taggedKeywords)
                .build();
    }
}
