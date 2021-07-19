package com.team.post.dto.output;

import com.team.post.Post;
import com.team.post.PostImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostOutput {
    private Long userId;
    private Long postId;
    private String content;
    private List<String> postImages;
    private List<Long> taggedUserIds;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    @Builder
    public SavePostOutput(Long userId, Long postId, String content, List<String> postImages, List<Long> taggedUserIds, LocalDateTime createdAt, LocalDateTime lastModified) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.postImages = postImages;
        this.taggedUserIds = taggedUserIds;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    public SavePostOutput(Post post) {
        this.userId = post.getUser().getId();
        this.postId = post.getId();
        this.content = post.getContent();
        this.postImages = post.getPostImages().stream().map(PostImage::getUploadFileName).collect(Collectors.toList());
        this.taggedUserIds = post.getPostTaggedUsers().stream().map(it -> it.getUser().getId()).collect(Collectors.toList());
        this.createdAt = post.getCreatedAt();
    }
}
