package com.team.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.team.post.QPost.post;
import static com.team.user.QFollow.follow;
import static com.team.user.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getFeed(Long userId, Pageable pageable) {
        return jpaQueryFactory.select(post)
                .from(post, user, follow)
                .where(
                        userIdEq(userId),
                        follow.followee.eq(post.user),
                        follow.follower.eq(user)
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        if(userId == null || userId < 0) {
            return null;
        }
        return user.id.eq(userId);
    }
}
