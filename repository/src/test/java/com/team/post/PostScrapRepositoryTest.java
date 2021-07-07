package com.team.post;

import com.team.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@DataJpaTest
public class PostScrapRepositoryTest {

    @Autowired
    private PostScrapRepository postScrapRepository;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void setUp() {

    }

    @Test
    void 특정_유저의_스크랩_가져오기() {
        User user = User.builder()
                .nickname("peach")
                .name("승화")
                .email("a@naver.com")
                .password("1234")
                .phoneNumber("010-2222-3333")
                .build();
        User user2 = User.builder()
                .nickname("dyddn")
                .name("용우")
                .email("b@naver.com")
                .password("12344")
                .phoneNumber("010-2224-3334")
                .build();
        Post post1 = new Post("안녕1",user);
        Post post2 = new Post("안녕2",user);
        Post post3 = new Post("안녕3", user2);
        PostScrap postScrap1 = new PostScrap(user, post1);
        PostScrap postScrap2 = new PostScrap(user, post2);
        PostScrap postScrap3 = new PostScrap(user2, post3);
        em.persist(user);
        em.persist(user2);
        em.persist(post1);
        em.persist(post2);
        em.persist(post3);
        em.persist(postScrap1);
        em.persist(postScrap2);
        em.persist(postScrap3);
        List<PostScrap> postScraps = postScrapRepository.findAllByUserId(user.getId());

        Collections.sort(postScraps, Comparator.comparingLong(PostScrap::getId));
        List<PostScrap> expected = Arrays.asList(postScrap1, postScrap2);
        for(int i = 0; i < postScraps.size(); i++) {
            Assertions.assertThat(postScraps.get(i).getId()).isEqualTo(expected.get(i).getId());
        }
    }
}
