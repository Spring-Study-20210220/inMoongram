package com.team.user;

import com.team.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;
    @Test
    void userEntityTest(){
        User user1 = User.builder()
                .email("murane@naver.com")
                .introduction("test")
                .name("정준수")
                .nickname("스프링")
                .password("1234")
                .phoneNumber("010-1111-2222")
                .profileImage("이미지")
                .sex(Sex.MALE)
                .website("www.instagram.com/murane")
                .build();
        User user2 = User.builder()
                .email("asdf12@naver.com")
                .introduction("test")
                .name("이용우")
                .nickname("토비")
                .password("1234")
                .phoneNumber("010-1111-2222")
                .profileImage("이미지")
                .sex(Sex.MALE)
                .website("www.instagram.com/murane")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        Follow follow = Follow.builder()
                .follower(user1)
                .followee(user2)
                .build();
        Follow savedFollow = followRepository.save(follow);

        assertThat(user1).isEqualTo(new ArrayList<Follow>(user2.getFollowers()).get(0).getFollower());
        assertThat(user2).isEqualTo(new ArrayList<Follow>(user1.getFollowees()).get(0).getFollowee());

    }
}
