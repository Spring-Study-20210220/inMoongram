package com.team.search.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchOutput {

    private List<PostSearchInfo> postSearchInfos;
    private List<UserSearchInfo> userSearchInfos;

    public SearchOutput(List<PostSearchInfo> postSearchInfos, List<UserSearchInfo> userSearchInfos) {
        this.postSearchInfos = postSearchInfos;
        this.userSearchInfos = userSearchInfos;
    }
}
