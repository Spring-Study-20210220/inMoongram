package com.team.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserSearch {
    private String name;
    private String nickName;
    private String imageUrl;

    @QueryProjection
    public UserSearch(String name, String nickName, String imageUrl) {
        this.name = name;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
    }
}
