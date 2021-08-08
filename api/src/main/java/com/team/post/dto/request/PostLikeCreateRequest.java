package com.team.post.dto.request;

import com.team.post.dto.input.PostLikeCreateInput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeCreateRequest {
    private Long userId;

    public PostLikeCreateRequest(Long userId) {
        this.userId = userId;
    }

    public static PostLikeCreateInput toServiceDto(Long userId, Long postId) {
        return new PostLikeCreateInput(userId, postId);
    }
}
