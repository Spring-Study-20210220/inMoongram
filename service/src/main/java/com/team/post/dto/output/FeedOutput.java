package com.team.post.dto.output;

import com.team.post.Post;
import com.team.post.PostImage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedOutput {
    private List<FeedInfo> feedInfos;

    public FeedOutput(List<Post> posts) {
        this.feedInfos = posts.stream()
                .map(FeedInfo::new)
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

        public FeedInfo(Post post) {
            this.userId = post.getUser().getId();
            this.postId = post.getId();
            this.nickname = post.getUser().getNickname();
            this.profileImage = post.getUser().getProfileImage();
            this.content = post.getContent();
            this.createdAt = post.getCreatedAt().toString();
            this.imagePaths = post.getPostImages().stream().map(PostImage::getPath).collect(Collectors.toList());
            this.uploadImageNames = post.getPostImages().stream().map(PostImage::getUploadFileName).collect(Collectors.toList());
            this.likeCount = post.getPostLikes().size();
            this.commentCount = post.getComments().size();
        }
    }
}
