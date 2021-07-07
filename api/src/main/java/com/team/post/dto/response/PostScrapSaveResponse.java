package com.team.post.dto.response;

import com.team.post.dto.output.PostScrapSaveOutput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapSaveResponse {
    private Long postScrapId;

    public PostScrapSaveResponse(Long postScrapId) {
        this.postScrapId = postScrapId;
    }

    public PostScrapSaveResponse(PostScrapSaveOutput output) {
        this.postScrapId = output.getPostScrapId();
    }
}
