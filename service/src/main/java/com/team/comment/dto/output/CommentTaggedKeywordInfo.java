package com.team.comment.dto.output;

import com.team.tag.CommentTaggedKeyword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentTaggedKeywordInfo {
    private Long id;
    private Long tagKeywordId;
    private String keyword;

    public CommentTaggedKeywordInfo(CommentTaggedKeyword commentTaggedKeyword) {
        this.id = commentTaggedKeyword.getId();
        this.keyword = commentTaggedKeyword.getTagKeyword().getKeyword();
        this.tagKeywordId = commentTaggedKeyword.getTagKeyword().getId();
    }
}
