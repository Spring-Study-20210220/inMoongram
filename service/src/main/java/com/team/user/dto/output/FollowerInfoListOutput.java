package com.team.user.dto.output;

import com.team.user.Follower;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowerInfoListOutput {
    private List<FollowerInfoOutput> followerInfoOutputList;

    /*
    public FollowerInfoListOutput(List<FollowerInfoOutput> followerInfoOutputList) {
        this.followerInfoOutputList = followerInfoOutputList;
    }
     */

    public FollowerInfoListOutput(List<Follower> followers, Long id) {

        this.followerInfoOutputList = followers.stream()
                .map(f -> FollowerInfoOutput.builder()
                        .name(f.getName())
                        .nickName(f.getNickName())
                        .profileImage(f.getProfileImage())
                        .userId(f.getUserId())
                        .followBack(
                                f.getFollowerId() != null && (f.getFollowerId().equals(id))
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }
}
