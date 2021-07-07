package com.team.post.dto.output;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapSaveOutput {
    private Long postScrapId;

    public PostScrapSaveOutput(Long postScrapId) {
        this.postScrapId = postScrapId;
    }
}
