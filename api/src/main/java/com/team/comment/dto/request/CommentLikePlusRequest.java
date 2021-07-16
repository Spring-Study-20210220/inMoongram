package com.team.comment.dto.request;

import com.team.comment.Comment;
import com.team.comment.dto.input.CommentLikePlusInput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikePlusRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long commentId;

    public CommentLikePlusRequest(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    public CommentLikePlusInput toInput() {
        return new CommentLikePlusInput(userId, commentId);
    }
}
