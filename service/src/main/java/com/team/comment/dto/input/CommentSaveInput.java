package com.team.comment.dto.input;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentSaveInput {
    private Long writerId;
    private Long postId;
    private Long superCommentId;
    private String content;
    private List<String> commentTaggedKeywords;
    private List<Long> commentTaggedUserIds;

    @Builder
    public CommentSaveInput(Long writerId, Long postId, Long superCommentId, String content,
                            List<String> commentTaggedKeywords, List<Long> commentTaggedUserIds) {
        this.writerId = writerId;
        this.postId = postId;
        this.superCommentId = superCommentId;
        this.content = content;
        this.commentTaggedKeywords = commentTaggedKeywords;
        this.commentTaggedUserIds = commentTaggedUserIds;
    }
}
