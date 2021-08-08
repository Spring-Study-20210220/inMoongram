package com.team.comment;

import com.team.comment.dto.input.CommentLikeCancelInput;
import com.team.comment.dto.input.CommentLikePlusInput;
import com.team.comment.dto.output.CommentLikePlusOutput;
import com.team.exception.IdNotFoundException;
import com.team.user.User;
import com.team.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentLikePlusOutput like(Long userId, CommentLikePlusInput input) {
        Comment comment = commentRepository.findById(input.getCommentId())
                .orElseThrow(IdNotFoundException::new);
        User user = userRepository.findById(userId)
                .orElseThrow(IdNotFoundException::new);
        CommentLike saved = commentLikeRepository.save(new CommentLike(comment, user));
        return new CommentLikePlusOutput(saved.getId());
    }

    @Transactional
    public void cancel(CommentLikeCancelInput input) {
        CommentLike commentLike = commentLikeRepository.findCommentLikeById(input.getCommentLikeId())
                .orElseThrow(IdNotFoundException::new);
        commentLike.deleteCommentLike();
        commentLikeRepository.deleteById(input.getCommentLikeId());
    }
}
