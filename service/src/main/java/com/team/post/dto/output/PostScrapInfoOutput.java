package com.team.post.dto.output;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapInfoOutput {
    private Long postScrapId;

    public PostScrapInfoOutput(Long postScrapId) {
        this.postScrapId = postScrapId;
    }
}
