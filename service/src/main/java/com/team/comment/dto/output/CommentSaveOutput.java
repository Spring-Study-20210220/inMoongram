package com.team.comment.dto.output;

import com.team.comment.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentSaveOutput {
    private Long commentId;

    @Builder
    public CommentSaveOutput(Long commentId) {
        this.commentId = commentId;
    }

    public CommentSaveOutput(Comment comment) {
        this.commentId = comment.getId();
    }
}
