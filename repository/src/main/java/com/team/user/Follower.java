package com.team.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Follower {
    private Long userId;
    private String nickname;
    private String name;
    private String profileImage;
    private Long followerId;
}
