package com.team.comment.dto.output;

import lombok.Data;

@Data
public class CommentLikePlusOutput {
    private Long commentLikeId;

    public CommentLikePlusOutput(Long commentLikeId) {
        this.commentLikeId = commentLikeId;
    }
}
