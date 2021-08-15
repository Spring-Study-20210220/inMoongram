package com.team.post.dto.output;

import com.team.comment.Comment;
import com.team.post.Post;
import com.team.post.PostImage;
import com.team.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostGetOutput {

    private Long postId;
    private String content;
    private String createdAt;
    private List<String> imagePaths;
    private List<String> uploadImageNames;
    private List<Comment> comments;
    private int likeCount;
    private int commentCount;
    private UserInfo userInfo;

    public PostGetOutput(Post post) {
        this.commentCount = post.getComments().size();
        this.likeCount = post.getPostLikes().size();
        this.postId = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt().toString();
        this.imagePaths = post.getPostImages().stream().map(PostImage::getPath).collect(Collectors.toList());
        this.uploadImageNames = post.getPostImages().stream().map(PostImage::getUploadFileName).collect(Collectors.toList());
        this.comments = new ArrayList<>(post.getComments());
        this.userInfo = new UserInfo(post.getUser());
    }

    @Data
    public static class UserInfo {
        private Long userId;
        private String nickname;
        private String profileImage;

        public UserInfo(User user) {
            this.userId = user.getId();
            this.nickname = user.getNickname();
            this.profileImage = user.getProfileImage();
        }
    }
}
