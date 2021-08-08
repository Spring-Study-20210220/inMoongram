package com.team.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.user.QUser;
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
        QUser postAuthor = new QUser("author");
        return jpaQueryFactory.selectFrom(post)
                .join(post.user, postAuthor).fetchJoin()
                .join(follow).on(postAuthor.eq(follow.followee)).fetchJoin()
                .join(user).on(follow.follower.eq(user)).fetchJoin()
                .where(userIdEq(userId))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        if (userId == null || userId < 0) {
            return null;
        }
        return user.id.eq(userId);
    }
}
