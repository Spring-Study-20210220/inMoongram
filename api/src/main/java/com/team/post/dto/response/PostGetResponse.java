package com.team.post.dto.response;

import com.team.comment.Comment;
import com.team.post.dto.output.PostGetOutput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostGetResponse {
    private UserInfo userInfo;
    private Long postId;
    private String content;
    private String createdAt;
    private List<String> imagePaths;
    private List<String> uploadImageNames;
    private List<Comment> comments;
    private int likeCount;
    private int commentCount;

    public PostGetResponse(PostGetOutput output) {
        this.userInfo = new UserInfo(output.getUserInfo());
        this.postId = output.getPostId();
        this.content = output.getContent();
        this.createdAt = output.getCreatedAt();
        this.imagePaths = output.getImagePaths();
        this.uploadImageNames = output.getUploadImageNames();
        this.comments = output.getComments();
        this.likeCount = output.getLikeCount();
        this.commentCount = output.getCommentCount();
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserInfo {
        private Long userId;
        private String nickname;
        private String profileImage;

        public UserInfo(PostGetOutput.UserInfo userInfo) {
            this.userId = userInfo.getUserId();
            this.nickname = userInfo.getNickname();
            this.profileImage = userInfo.getProfileImage();
        }
    }
}
