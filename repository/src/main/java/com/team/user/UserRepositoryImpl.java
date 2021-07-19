package com.team.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public List<Follower> findFollowerUserById(Long id) {
        QUser user = QUser.user;
        QUser user2 = new QUser("user2");
        QFollow follow = QFollow.follow;
        QFollow follow2 = new QFollow("follow2");
        return jpaQueryFactory
                .select(Projections.fields(Follower.class,
                        follow.follower.id.as("userId"), user.nickname, user.name, user.profileImage, follow2.follower.id.as("followerId")
                ))
                .from(user)
                .leftJoin(follow).on(user.id.eq(follow.followee.id))
                .leftJoin(user2).on(follow.follower.id.eq(user2.id))
                .leftJoin(follow2).on(user2.id.eq(follow2.followee.id))
                .where(user.id.eq(id))
                .fetch();
    }
}
