package com.team.post;

import com.team.config.TestConfig;
import com.team.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@DataJpaTest
@Import(TestConfig.class)
public class PostScrapRepositoryTest {

    @Autowired
    private PostScrapRepository postScrapRepository;

    @Autowired
    private TestEntityManager em;

    private User user, user2;
    private Post post1, post2, post3;
    private PostScrap postScrap1, postScrap2, postScrap3;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .nickname("peach")
                .name("승화")
                .email("a@naver.com")
                .password("1234")
                .phoneNumber("010-2222-3333")
                .build();
        user2 = User.builder()
                .nickname("dyddn")
                .name("용우")
                .email("b@naver.com")
                .password("12344")
                .phoneNumber("010-2224-3334")
                .build();
        post1 = new Post("안녕1",user);
        post2 = new Post("안녕2",user);
        post3 = new Post("안녕3", user2);
        postScrap1 = new PostScrap(user, post1);
        postScrap2 = new PostScrap(user, post2);
        postScrap3 = new PostScrap(user2, post3);
        em.persist(user);
        em.persist(user2);
        em.persist(post1);
        em.persist(post2);
        em.persist(post3);
        em.persist(postScrap1);
        em.persist(postScrap2);
        em.persist(postScrap3);
    }

    @Test
    void 특정_유저의_스크랩_가져오기() {
        List<PostScrap> postScraps = postScrapRepository.findAllByUserId(user.getId());

        Collections.sort(postScraps, Comparator.comparingLong(PostScrap::getId));
        List<PostScrap> expected = Arrays.asList(postScrap1, postScrap2);
        for(int i = 0; i < postScraps.size(); i++) {
            Assertions.assertThat(postScraps.get(i).getId()).isEqualTo(expected.get(i).getId());
        }
    }

    @Test
    void 특정_유저의_특정_포스트의_스크랩_가져오기() {
        PostScrap postScrap = postScrapRepository.findByUserIdAndPostId(user.getId(), post1.getId())
                .orElseThrow(RuntimeException::new);

        Assertions.assertThat(postScrap.getId()).isEqualTo(postScrap1.getId());
    }
}
