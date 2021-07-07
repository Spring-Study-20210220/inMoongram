package com.team.post.dto.input;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapGetInput {
    private Long userId;

    public PostScrapGetInput(Long userId) {
        this.userId = userId;
    }
}
