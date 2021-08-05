package com.team.user;

import com.team.user.dto.input.UserInfoInput;
import com.team.user.dto.output.FollowListOutput;
import com.team.user.dto.output.UserInfoOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;
    private User user1;
    private User user2;
    private User user3;

    private Follow follow1;
    private Follow follow2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("testUser")
                .nickname("testNickName")
                .email("test@test.com")
                .password("1234")
                .sex(Sex.MALE)
                .phoneNumber("010-2222-3333")
                .website("www.naver.com")
                .introduction("안녕하세요")
                .profileImage("1111")
                .build();
        user.setIdForTest(1L);
        user1 = User.builder()
                .name("testUser1")
                .nickname("testNickname1")
                .email("test1@test.com")
                .password("testPassword1")
                .build();
        user2 = User.builder()
                .name("testUser2")
                .nickname("testNickname2")
                .email("test2@test.com")
                .password("testPassword2")
                .build();
        user3 = User.builder()
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
    void 팔로우_목록_조회() {
        given(userRepository.findFollowingUserById(any())).willReturn(Optional.of(user1));
        FollowListOutput followList = userService.getFollowList(1L);

        assertThat(followList.getUsers().size()).isEqualTo(2);
        assertThat(followList.getUsers().get(0).getName()).isEqualTo(user2.getName());
        assertThat(followList.getUsers().get(0).getNickname()).isEqualTo(user2.getNickname());
        assertThat(followList.getUsers().get(1).getName()).isEqualTo(user3.getName());
        assertThat(followList.getUsers().get(1).getNickname()).isEqualTo(user3.getNickname());

//        TODO: follow Hashtag test
//        assertThat(followList.getHashtags().size()).isEqualTo(1);
    }

    @Test
    void 유저_정보_조회() {
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        UserInfoOutput result = userService.getUserInfo(new UserInfoInput(userId));

        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }
}
