package com.team.post.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapSaveRequest {
    @NotNull
    private Long postId;

    public PostScrapSaveRequest(Long postId) {
        this.postId = postId;
    }
}
