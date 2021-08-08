package com.team.search.dto;

import com.team.search.UserSearch;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearchInfo {
    private String name;
    private String nickName;
    private String imageUrl;

    @Builder
    public UserSearchInfo(String name, String nickName, String imageUrl) {
        this.name = name;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
    }

    public UserSearchInfo(UserSearch userSearch) {
        this.imageUrl = userSearch.getImageUrl();
        this.name = userSearch.getName();
        this.nickName = userSearch.getNickName();
    }
}
