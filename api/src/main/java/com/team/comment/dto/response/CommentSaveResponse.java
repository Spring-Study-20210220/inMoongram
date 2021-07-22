package com.team.comment.dto.response;

import com.team.comment.dto.output.CommentSaveOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveResponse {
    private Long commentId;

    public CommentSaveResponse(CommentSaveOutput output) {
        this.commentId = output.getCommentId();
    }
}
