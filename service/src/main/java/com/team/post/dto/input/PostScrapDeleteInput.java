package com.team.post.dto.input;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapDeleteInput {
    private Long userId;
    private Long postId;

    public PostScrapDeleteInput(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
