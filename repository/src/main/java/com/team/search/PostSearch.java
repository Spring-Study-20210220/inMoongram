package com.team.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class PostSearch {
    private String keyword;
    private Long count;

    @QueryProjection
    public PostSearch(String keyword, Long count) {
        this.keyword = keyword;
        this.count = count;
    }
}
