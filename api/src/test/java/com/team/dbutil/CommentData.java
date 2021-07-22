package com.team.dbutil;

import com.team.comment.Comment;
import com.team.comment.CommentRepository;
import com.team.post.Post;
import com.team.user.User;
import org.springframework.stereotype.Component;

@Component
public class CommentData {

    private CommentRepository commentRepository;

    public CommentData(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(User user, Post post) {
        return commentRepository.save(Comment.builder()
                .post(post)
                .user(user)
                .content("안녕하세요")
                .build());
    }
}
