package com.team.post.dto.output;

import com.team.post.PostScrap;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapGetOutput {
    private List<PostScrapInfo> postScrapList;

    public PostScrapGetOutput(List<PostScrapInfo> postScrapList) {
        this.postScrapList = postScrapList;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostScrapInfo {
        private Long postScrapId;
        private Long postId;
        private Long userId;

        @Builder
        public PostScrapInfo(Long postScrapId, Long postId, Long userId) {
            this.postScrapId = postScrapId;
            this.postId = postId;
            this.userId = userId;
        }

        public PostScrapInfo(PostScrap postScrap) {
            this.postScrapId = postScrap.getId();
            this.postId = postScrap.getPost().getId();
            this.userId = postScrap.getUser().getId();
        }
    }
}
