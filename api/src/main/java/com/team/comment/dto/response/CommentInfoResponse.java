package com.team.comment.dto.response;

import com.team.comment.dto.output.CommentInfoOutput;
import com.team.comment.dto.output.CommentTaggedKeywordInfo;
import com.team.comment.dto.output.CommentTaggedUserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInfoResponse {

    private List<CommentInfo> commentInfos;

    public CommentInfoResponse(List<CommentInfoOutput> outputs) {
        this.commentInfos = outputs.stream()
                .map(
                        it -> CommentInfo.builder()
                                .id(it.getId())
                                .content(it.getContent())
                                .createdAt(it.getCreatedAt())
                                .subComments(it.getSubComments())
                                .taggedKeywords(it.getTaggedKeywords())
                                .taggedUsers(it.getTaggedUsers())
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentInfo {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private List<CommentInfoOutput> subComments;
        private List<CommentTaggedKeywordInfo> taggedKeywords;
        private List<CommentTaggedUserInfo> taggedUsers;

        @Builder
        public CommentInfo(Long id, String content, LocalDateTime createdAt,
                           List<CommentInfoOutput> subComments, List<CommentTaggedKeywordInfo> taggedKeywords, List<CommentTaggedUserInfo> taggedUsers) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
            this.subComments = subComments;
            this.taggedKeywords = taggedKeywords;
            this.taggedUsers = taggedUsers;
        }
    }
}
