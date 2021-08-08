package com.team.search;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.team.tag.QPostTaggedKeyword.postTaggedKeyword;
import static com.team.tag.QTagKeyword.tagKeyword;
import static com.team.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class SearchQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<PostSearch> findPostTaggedKeywordByKeyword(String keyword) {
        return jpaQueryFactory.select(new QPostSearch(tagKeyword.keyword, tagKeyword.keyword.count()))
                .from(postTaggedKeyword)
                .innerJoin(postTaggedKeyword.tagKeyword, tagKeyword)
                .where(
                        tagKeywordContainsKeyword(keyword)
                )
                .groupBy(tagKeyword.keyword)
                .orderBy(postTaggedKeyword.id.desc())
                .limit(50)
                .fetch();
    }

    public List<UserSearch> findUsersByKeyword(String keyword) {
        return jpaQueryFactory.select(new QUserSearch(user.name, user.nickname, user.profileImage))
                .from(user)
                .where(
                        userNameContainsKeyword(keyword)
                                .or(userNickNameContainsUserKeyword(keyword))
                                .or(userIntroductionContainsKeyword(keyword))
                )
                .orderBy(user.id.desc())
                .limit(50)
                .fetch();
    }

    public BooleanExpression ltTagKeywordId(Long id) {
        if (ObjectUtils.isEmpty(id))
            return null;
        return tagKeyword.id.lt(id);
    }

    public BooleanExpression ltUserId(Long id) {
        if (ObjectUtils.isEmpty(id))
            return null;
        return user.id.lt(id);
    }

    public BooleanExpression tagKeywordContainsKeyword(String keyword) {
        if (ObjectUtils.isEmpty(keyword))
            return null;
        return tagKeyword.keyword.contains(keyword);
    }

    public BooleanExpression userNickNameContainsUserKeyword(String keyword) {
        if (ObjectUtils.isEmpty(keyword))
            return null;
        return user.nickname.contains(keyword);
    }

    public BooleanExpression userNameContainsKeyword(String keyword) {
        if (ObjectUtils.isEmpty(keyword))
            return null;
        return user.name.contains(keyword);
    }

    public BooleanExpression userIntroductionContainsKeyword(String keyword) {
        if (ObjectUtils.isEmpty(keyword))
            return null;
        return user.introduction.contains(keyword);
    }
}
