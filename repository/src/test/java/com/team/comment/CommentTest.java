package com.team.comment;

import com.team.post.Post;
import com.team.post.PostRepository;
import com.team.user.User;
import com.team.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void datetime테스트() {
        User postAuthor = userRepository.save(
                User.builder()
                        .name("postWriter")
                        .nickname("nickname321")
                        .email("email2@com")
                        .password("pass1234")
                        .build()
        );
        User commentWriter = userRepository.save(
                User.builder()
                        .name("commentWriter")
                        .nickname("nickname123")
                        .email("email1@com")
                        .password("pass1234")
                        .build()
        );
        Post post = postRepository.save(
                new Post("postcontent", postAuthor)
        );
        Comment comment = commentRepository.save(
                Comment.builder()
                        .content("댓글테스트123")
                        .user(commentWriter)
                        .post(post)
                        .build()
        );
        System.out.println(comment.getCreatedAt());
        assertThat(comment.getCreatedAt()).isBefore(LocalDateTime.now());
    }
}
