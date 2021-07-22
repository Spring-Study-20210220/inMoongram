package com.team.comment;

import com.team.post.Post;
import com.team.post.PostRepository;
import com.team.user.User;
import com.team.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CommentLikeTest {

    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    private User user;
    private Post post;
    private Comment comment;
    private CommentLike commentLike;
    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .email("a@naver.com")
                .name("승화")
                .password("1234")
                .nickname("peach")
                .build());
        post = postRepository.save(new Post("hello", user));
        comment = commentRepository.save(Comment.builder()
                .content("hello")
                .post(post)
                .user(user)
                .build());
        commentLike = commentLikeRepository.save(new CommentLike(comment, user));
    }

    @Test
    void 댓글_좋아요_양방향() {
        Assertions.assertThat(comment.getCommentLikes().size()).isEqualTo(1);
    }

    @Test
    void 좋아요_취소() {

        commentLike.deleteCommentLike();
        commentLikeRepository.deleteById(commentLike.getId());

        Assertions.assertThat(comment.getCommentLikes().size()).isEqualTo(0);
    }


}
