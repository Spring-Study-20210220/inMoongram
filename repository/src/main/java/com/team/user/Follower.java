package com.team.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
public class Follower {
    private Long userId;
    private String nickName;
    private String name;
    private String profileImage;
    private Long followerId;
}
