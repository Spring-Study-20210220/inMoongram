package com.team.user.dto.input;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowInfoInput {
    private Long followeeId;

    public FollowInfoInput(Long followeeId) {
        this.followeeId = followeeId;
    }
}
