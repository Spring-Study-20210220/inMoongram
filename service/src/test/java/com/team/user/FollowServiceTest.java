package com.team.user;

import com.team.user.dto.input.FollowInfoInput;
import com.team.user.dto.output.FollowInfoOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FollowService followService;

    private User user1;
    private User user2;
    private User user3;

    private Follow follow1;
    private Follow follow2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .name("testUser1")
                .nickname("testNickname1")
                .email("test1@test.com")
                .password("testPassword1")
                .build();
        user2 = User.builder()
                .id(2L)
                .name("testUser2")
                .nickname("testNickname2")
                .email("test2@test.com")
                .password("testPassword2")
                .build();
        user3 = User.builder()
                .id(3L)
                .name("testUser3")
                .nickname("testNickname3")
                .email("test3@test.com")
                .password("testPassword3")
                .build();

        follow1 = Follow.builder()
                .follower(user1)
                .followee(user2)
                .build();
        follow2 = Follow.builder()
                .follower(user1)
                .followee(user3)
                .build();
    }

    @Test
    void 팔로우_취소() {
        given(userService.findByNickname(user1.getNickname())).willReturn(user1);
        given(userService.findByNickname(user2.getNickname())).willReturn(user2);
        given(followRepository.findById(any())).willReturn(Optional.of(follow1));

        int followerCount = user1.getFollowees().size();
        int followeeCount = user2.getFollowers().size();

        followService.unfollow(1L);

        assertThat(user1.getFollowees().size()).isEqualTo(followerCount - 1);
        assertThat(user2.getFollowers().size()).isEqualTo(followeeCount - 1);
    }

    @Test
    void 팔로우() {
        /*
        1.followerId와 followeeId가 들어온다
        2.id를 가지고 user를 찾는다
        3.id가 존재하지 않는다면 IdNotExistException을 던진다
        4.존재한다면 Follow 인스턴스를 생성한다
        5.Repository에 생성한 인스턴스를 저장한다
        6.FollowInfoOuput에 담아서 반환한다
        */
        given(followRepository.save(any())).willReturn(follow1);
        given(userService.findUserById(any()))
                .willReturn(user1)
                .willReturn(user2);

        FollowInfoOutput result = followService.follow(user1.getId(), new FollowInfoInput(user2.getId()));

        assertThat(result.getFolloweeId()).isEqualTo(user2.getId());
        assertThat(result.getFollowerId()).isEqualTo(user1.getId());
    }
}