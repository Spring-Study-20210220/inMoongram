package com.team.post.dto.response;

import com.team.post.dto.output.FeedOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class FeedResponse {
    private List<FeedResponse.FeedInfo> feedInfos;

    public FeedResponse(FeedOutput feed) {
        this.feedInfos = feed.getFeedInfos().stream()
                .map(FeedResponse.FeedInfo::new)
                .collect(Collectors.toList());
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FeedInfo {
        private Long userId;
        private Long postId;
        private String nickname;
        private String profileImage;
        private String content;
        private String createdAt;
        private List<String> imagePaths;
        private List<String> uploadImageNames;
        private int likeCount;
        private int commentCount;

        public FeedInfo(FeedOutput.FeedInfo output) {
            this.userId = output.getUserId();
            this.postId = output.getPostId();
            this.nickname = output.getNickname();
            this.profileImage = output.getProfileImage();
            this.content = output.getContent();
            this.createdAt = output.getCreatedAt();
            this.imagePaths = output.getImagePaths();
            this.uploadImageNames = output.getUploadImageNames();
            this.likeCount = output.getLikeCount();
            this.commentCount = output.getCommentCount();
        }
    }
}
