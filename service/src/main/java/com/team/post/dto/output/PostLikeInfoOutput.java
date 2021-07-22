package com.team.post.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class PostLikeInfoOutput {
    List<PostLikeInfo> postLikeInfoList;

    public PostLikeInfoOutput(List<PostLikeInfo> postLikeInfoList) {
        this.postLikeInfoList = postLikeInfoList;
    }

    @Getter
    public static class PostLikeInfo {
        private Long postLikeId;
        private Long postId;
        private Long userId;

        @Builder
        public PostLikeInfo(Long postLikeId, Long postId, Long userId) {
            this.postLikeId = postLikeId;
            this.postId = postId;
            this.userId = userId;
        }
    }

}
