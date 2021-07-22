package com.team.comment.dto.output;

import com.team.tag.CommentTaggedUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentTaggedUserInfo {
    private Long id;
    private Long taggedUserId;

    public CommentTaggedUserInfo(CommentTaggedUser commentTaggedUser) {
        this.id = commentTaggedUser.getId();
        this.taggedUserId = commentTaggedUser.getUser().getId();
    }
}
