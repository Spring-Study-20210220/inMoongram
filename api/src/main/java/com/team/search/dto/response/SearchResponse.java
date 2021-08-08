package com.team.search.dto.response;

import com.team.search.dto.PostSearchInfo;
import com.team.search.dto.SearchOutput;
import com.team.search.dto.UserSearchInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchResponse {

    private List<UserSearchInfo> userSearchInfos;
    private List<PostSearchInfo> postSearchInfos;

    public SearchResponse(List<UserSearchInfo> userSearchInfos, List<PostSearchInfo> postSearchInfos) {
        this.userSearchInfos = userSearchInfos;
        this.postSearchInfos = postSearchInfos;
    }

    public SearchResponse(SearchOutput output) {
        this.postSearchInfos = output.getPostSearchInfos();
        this.userSearchInfos = output.getUserSearchInfos();
    }
}
