package com.team.post.dto.input;

import lombok.Data;

@Data
public class PostGetInput {

    private Long postId;

    public PostGetInput(Long postId) {
        this.postId = postId;
    }
}
