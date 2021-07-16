package com.team.comment.dto.response;

import com.team.comment.dto.output.CommentLikePlusOutput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikePlusResponse {
    private Long commentLikeId;

    public CommentLikePlusResponse(Long commentLikeId) {
        this.commentLikeId = commentLikeId;
    }

    public CommentLikePlusResponse(CommentLikePlusOutput output) {
        this.commentLikeId = output.getCommentLikeId();
    }
}
