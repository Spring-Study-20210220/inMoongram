package com.team.post;

import com.team.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostTaggedUserServiceTest {

    @Mock
    private PostTaggedUserRepository postTaggedUserRepository;

    @InjectMocks
    private PostTaggedUserService postTaggedUserService;

    private User user1;
    private User user2;
    private User user3;

    private Post post;

    private PostTaggedUser tag1;
    private PostTaggedUser tag2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("testUser1")
                .nickname("testNickname1")
                .email("test1@test.com")
                .password("testPassword1")
                .build();
        user1.setIdForTest(1L);
        user2 = User.builder()
                .name("testUser2")
                .nickname("testNickname2")
                .email("test2@test.com")
                .password("testPassword2")
                .build();
        user2.setIdForTest(2L);
        user3 = User.builder()
                .name("testUser3")
                .nickname("testNickname3")
                .email("test3@test.com")
                .password("testPassword3")
                .build();
        user3.setIdForTest(3L);

        post = new Post("test-content", user1);

        tag1 = new PostTaggedUser(user2, post);
        tag2 = new PostTaggedUser(user3, post);
    }

    @Test
    void tagAll() {
        given(postTaggedUserRepository.save(any())).willReturn(tag1).willReturn(tag2);
        List<PostTaggedUser> output = postTaggedUserService.tagAll(Arrays.asList(user2, user3), post);

        assertThat(output.size()).isEqualTo(2);
        assertThat(output.get(0).getUser().getId()).isEqualTo(2L);
        assertThat(output.get(1).getUser().getId()).isEqualTo(3L);
    }
}