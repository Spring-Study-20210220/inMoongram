package com.team.post.dto.input;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostInput {
    private Long userId;
    private String content;
    private List<Long> postImageIds;
    private List<Long> taggedUserIds;

    @Builder
    public SavePostInput(Long userId, String content, List<Long> postImageIds, List<Long> taggedUserIds) {
        this.userId = userId;
        this.content = content;
        this.postImageIds = postImageIds;
        this.taggedUserIds = taggedUserIds;
    }
}
