package com.team.comment.dto.output;

import com.team.comment.Comment;
import com.team.tag.CommentTaggedKeyword;
import com.team.tag.CommentTaggedUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInfoOutput {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentInfoOutput> subComments;
    private List<CommentTaggedKeywordInfo> taggedKeywords;
    private List<CommentTaggedUserInfo> taggedUsers;

    @Builder
    public CommentInfoOutput(Long id, String content, LocalDateTime createdAt, List<CommentInfoOutput> subComments,
                             List<CommentTaggedKeyword> taggedKeywords, List<CommentTaggedUser> taggedUsers) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.subComments = subComments;
        this.taggedKeywords = taggedKeywords.stream()
                .map(CommentTaggedKeywordInfo::new)
                .collect(Collectors.toList());
        this.taggedUsers = taggedUsers.stream()
                .map(CommentTaggedUserInfo::new)
                .collect(Collectors.toList());
    }

    public CommentInfoOutput(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.subComments = comment.getSubComments()
                .stream()
                .map(CommentInfoOutput::new)
                .collect(Collectors.toList());
        this.taggedKeywords = comment.getCommentTaggedKeywords().stream()
                .map(CommentTaggedKeywordInfo::new)
                .collect(Collectors.toList());
        this.taggedUsers = comment.getCommentTaggedUsers().stream()
                .map(CommentTaggedUserInfo::new)
                .collect(Collectors.toList());
    }
}

