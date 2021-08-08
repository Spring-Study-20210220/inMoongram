package com.team.post.dto.output;

import com.team.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostOutput {
    private Long postId;

    public SavePostOutput(Long postId) {
        this.postId = postId;
    }

    public SavePostOutput(Post post) {
        this.postId = post.getId();
    }
}
