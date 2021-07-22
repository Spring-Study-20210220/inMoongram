package com.team.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.team.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public List<Follower> findFollowerUserById(Long id) {
        QFollow follow = QFollow.follow;
        QFollow follow2 = new QFollow("follow2");
        return jpaQueryFactory
                .select(Projections.fields(Follower.class,
                        follow.follower.id.as("userId"), user.nickname, user.name, user.profileImage, follow2.follower.id.as("followerId")
                ))
                .from(follow)
                .leftJoin(user).on(follow.follower.id.eq(user.id))
                .leftJoin(follow2).on(user.id.eq(follow2.followee.id))
                .where(follow.followee.id.eq(id))
                .fetch();

        //entity id만 가지는 테이블 생성....
    }
}
