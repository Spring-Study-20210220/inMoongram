package com.team.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "followee_id")
    private User followee;

    @Builder
    public Follow(User follower, User followee) {
        this.follower = follower;
        this.followee = followee;
        setFollow(follower, followee);
    }

    private void setFollow(User follower, User followee){
        follower.getFollowees().add(this);
        followee.getFollowers().add(this);
        //
    }
}