package com.team.comment.dto.input;

import lombok.Data;

@Data
public class CommentLikePlusInput {
    private Long userId;
    private Long commentId;

    public CommentLikePlusInput(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
