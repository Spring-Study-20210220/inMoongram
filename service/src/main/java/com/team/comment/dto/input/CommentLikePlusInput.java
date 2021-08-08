package com.team.comment.dto.input;

import lombok.Data;

@Data
public class CommentLikePlusInput {
    private Long commentId;

    public CommentLikePlusInput(Long commentId) {
        this.commentId = commentId;
    }
}
