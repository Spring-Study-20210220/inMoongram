package com.team.post.dto.input;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapInput {
    private Long postId;
    private Long userId;

    public PostScrapInput(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
