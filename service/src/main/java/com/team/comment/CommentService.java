package com.team.comment;

import com.team.comment.dto.input.CommentSaveInput;
import com.team.comment.dto.output.CommentInfoOutput;
import com.team.comment.dto.output.CommentSaveOutput;
import com.team.exception.IdNotFoundException;
import com.team.post.Post;
import com.team.post.PostService;
import com.team.user.User;
import com.team.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final CommentTaggedKeywordService commentTaggedKeywordService;
    private final CommentTaggedUserService commentTaggedUserService;

    @Transactional
    public CommentSaveOutput saveComment(Long userId, CommentSaveInput input) {
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(input.getPostId());
        Comment superComment = getSuperComment(input);
        Comment comment = Comment.builder()
                .content(input.getContent())
                .user(user)
                .post(post)
                .superComment(superComment)
                .build();
        Comment savedComment = commentRepository.save(comment);
        if (input.getCommentTaggedKeywords() != null)
            tagKeywords(savedComment, input);
        if (input.getCommentTaggedUserIds() != null)
            tagUsers(savedComment, input);
        return new CommentSaveOutput(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    @Transactional
    public List<CommentInfoOutput> getComments(Long postId) {
        Post post = postService.findPostById(postId);
        Set<Comment> comments = post.getComments();
        return comments.stream()
                .filter(it -> it.getSuperComment() == null)
                .map(CommentInfoOutput::new)
                .collect(Collectors.toList());
    }

    private Comment getSuperComment(CommentSaveInput input) {
        Comment superComment;
        if (input.getSuperCommentId() == null) {
            superComment = null;
        } else superComment = findCommentById(input.getSuperCommentId());
        return superComment;
    }

    private void tagUsers(Comment comment, CommentSaveInput input) {
        var taggedUsers =
                commentTaggedUserService.tagAllUsers(comment, input.getCommentTaggedUserIds());
        comment.addAllTaggedUsers(taggedUsers);
    }

    private void tagKeywords(Comment comment, CommentSaveInput input) {
        var taggedKeyswords =
                commentTaggedKeywordService.tagAllKeywords(comment, input.getCommentTaggedKeywords());
        comment.addAllTaggedKeywords(taggedKeyswords);
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(IdNotFoundException::new);
    }
}
