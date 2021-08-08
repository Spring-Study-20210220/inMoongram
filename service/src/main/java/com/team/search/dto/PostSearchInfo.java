package com.team.search.dto;

import com.team.search.PostSearch;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSearchInfo {
    private String keyword;
    private Long count;

    public PostSearchInfo(String keyword, Long count) {
        this.keyword = keyword;
        this.count = count;
    }

    public PostSearchInfo(PostSearch postSearch) {
        this.count = postSearch.getCount();
        this.keyword = postSearch.getKeyword();
    }
}
