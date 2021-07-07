package com.team.post.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapSaveRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long postId;

    public PostScrapSaveRequest(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
