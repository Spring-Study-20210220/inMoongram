package com.team.post.dto.response;

import com.team.post.dto.output.PostScrapGetOutput;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapGetResponse {
    private List<PostScrapInfoResponse> postScrapInfoResponseList;

    public PostScrapGetResponse(List<PostScrapInfoResponse> postScrapInfoResponseList) {
        this.postScrapInfoResponseList = postScrapInfoResponseList;
    }

    public PostScrapGetResponse(PostScrapGetOutput output) {
        this.postScrapInfoResponseList = output.getPostScrapList().stream()
                .map(PostScrapInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostScrapInfoResponse {
        private Long postScrapId;
        private Long postId;
        private Long userId;

        @Builder
        public PostScrapInfoResponse(Long postScrapId, Long postId, Long userId) {
            this.postScrapId = postScrapId;
            this.postId = postId;
            this.userId = userId;
        }

        public PostScrapInfoResponse(PostScrapGetOutput.PostScrapInfo info) {
            this.postScrapId = info.getPostScrapId();
            this.postId = info.getPostId();
            this.userId = info.getUserId();
        }
    }
}
