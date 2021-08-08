package com.team.post.dto.input;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedInput {
    private Long userId;
    private int page;

    public FeedInput(Long userId, int page) {
        this.userId = userId;
        this.page = page;
    }

    public PageRequest of() {
        return PageRequest.of(page - 1, 10);
    }
}
