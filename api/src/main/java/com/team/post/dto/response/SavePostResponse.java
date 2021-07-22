package com.team.post.dto.response;

import com.team.post.dto.output.SavePostOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostResponse {
    private Long userId;
    private Long postId;
    private String content;
    private List<String> postImages;
    private List<Long> taggedUserIds;
    private LocalDateTime createdAt;

    public SavePostResponse(SavePostOutput output) {
        this.userId = output.getUserId();
        this.postId = output.getPostId();
        this.content = output.getContent();
        this.postImages = output.getPostImages();
        this.taggedUserIds = output.getTaggedUserIds();
        this.createdAt = output.getCreatedAt();
    }
}
