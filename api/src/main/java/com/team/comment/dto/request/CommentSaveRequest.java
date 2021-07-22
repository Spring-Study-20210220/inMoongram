package com.team.comment.dto.request;

import com.team.comment.dto.input.CommentSaveInput;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveRequest {
    private Long writerId;
    private Long postId;
    private Long superCommentId;
    private String content;
    private List<String> commentTaggedKeywords;
    private List<Long> commentTaggedUserIds;

    @Builder
    public CommentSaveRequest(Long writerId, Long postId, Long superCommentId, String content,
                              List<String> commentTaggedKeywords, List<Long> commentTaggedUserIds) {
        this.writerId = writerId;
        this.postId = postId;
        this.superCommentId = superCommentId;
        this.content = content;
        this.commentTaggedKeywords = commentTaggedKeywords;
        this.commentTaggedUserIds = commentTaggedUserIds;
    }

    public CommentSaveInput toServiceDto(){
        return CommentSaveInput.builder()
                .writerId(writerId)
                .postId(postId)
                .content(content)
                .superCommentId(superCommentId)
                .commentTaggedKeywords(commentTaggedKeywords)
                .commentTaggedUserIds(commentTaggedUserIds)
                .build();
    }
}
