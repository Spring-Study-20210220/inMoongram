package com.team.user.dto.request;

import com.team.user.dto.input.FollowInfoInput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowRequest {
    @NotNull
    private Long followerId;
    @NotNull
    private Long followeeId;

    public FollowRequest(Long followerId, Long followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public FollowInfoInput toInput() {
        return new FollowInfoInput(followerId, followeeId);
    }
}
