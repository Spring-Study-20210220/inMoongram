package com.team.dbutil;

import com.team.comment.Comment;
import com.team.comment.CommentLike;
import com.team.comment.CommentLikeRepository;
import com.team.user.User;
import org.springframework.stereotype.Component;

@Component
public class CommentLikeData {

    private CommentLikeRepository commentLikeRepository;

    public CommentLikeData(CommentLikeRepository commentLikeRepository) {
        this.commentLikeRepository = commentLikeRepository;
    }

    public CommentLike saveCommentLike(Comment comment, User user) {
        return commentLikeRepository.save(new CommentLike(comment, user));
    }
}
