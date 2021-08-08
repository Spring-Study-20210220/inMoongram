package com.team.post.dto.response;

import com.team.post.dto.output.SavePostOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostResponse {
    private Long postId;

    public SavePostResponse(SavePostOutput output) {
        this.postId = output.getPostId();
    }
}
