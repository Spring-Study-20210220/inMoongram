package com.team.comment.dto.input;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentSaveInput {
    private final Long postId;
    private final Long superCommentId;
    private final String content;
    private final List<String> commentTaggedKeywords;
    private final List<Long> commentTaggedUserIds;

    @Builder
    public CommentSaveInput(Long postId, Long superCommentId, String content,
                            List<String> commentTaggedKeywords, List<Long> commentTaggedUserIds) {
        this.postId = postId;
        this.superCommentId = superCommentId;
        this.content = content;
        this.commentTaggedKeywords = commentTaggedKeywords;
        this.commentTaggedUserIds = commentTaggedUserIds;
    }
}
