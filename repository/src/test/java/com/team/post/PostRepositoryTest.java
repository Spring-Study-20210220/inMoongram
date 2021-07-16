package com.team.post;

import com.team.config.TestConfig;
import com.team.user.Follow;
import com.team.user.FollowRepository;
import com.team.user.User;
import com.team.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Test
    void 피드_조회() throws InterruptedException {
        User user1 = createUser("nickname1", "password1", "email1.com");
        User user2 = createUser("nickname2", "password2", "email2.com");
        User user3 = createUser("nickname3", "password3", "email3.com");
        User user4 = createUser("nickname4", "password4", "email4.com");

        Follow follow1 = createFollow(user1, user2);
        Follow follow2 = createFollow(user1, user3);

        Post post1 = createPost("first", user2);
        Post post2 = createPost("second", user3);
        Post post3 = createPost("third", user4);
        Post post4 = createPost("fourth", user2);

        Pageable pageable = PageRequest.of(0, 10);

        List<Post> feed = postRepository.getFeed(user1.getId(), pageable);
        assertThat(feed.size()).isEqualTo(3);
    }

    private Post createPost(String content ,User user) {
        Post post = new Post(content, user);
        return postRepository.save(post);
    }

    private Follow createFollow(User follower, User followee) {
        Follow follow = new Follow(follower, followee);
        return followRepository.save(follow);
    }

    private User createUser(String nickname, String password, String email) {
        User user = User.builder()
                .nickname(nickname)
                .password(password)
                .email(email)
                .build();
        return userRepository.save(user);
    }

}