package com.team.comment.dto.input;

import lombok.Data;

@Data
public class CommentLikeCancelInput {
    private Long commentLikeId;

    public CommentLikeCancelInput(Long commentLikeId) {
        this.commentLikeId = commentLikeId;
    }
}
