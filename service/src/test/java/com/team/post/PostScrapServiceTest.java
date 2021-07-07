package com.team.post;

import com.team.post.dto.input.PostScrapInput;
import com.team.post.dto.output.PostScrapInfoOutput;
import com.team.user.User;
import com.team.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostScrapServiceTest {
    @Mock
    private PostScrapRepository postScrapRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private PostScrapService postScrapService;

    private User user;
    private Post post;
    private PostScrap postScrap;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("백승화")
                .email("a@naver.com")
                .nickname("peach")
                .build();
        post = new Post("hello" ,user);
        postScrap = new PostScrap(user, post);
        postScrap.setIdForTest(1L);
    }

    @Test
    void 스크랩_저장() {
        /*
        스크랩 저장 시나리오
        1. 자신이 북마크 하고싶은 게시물 번호와 자신의 아이디를 날린다
        2. PostRepository에서 원하는 게시물을 찾는다
        3. UserRepository에서 원하는 유저를 찾는다
        4. 게시물과 유저의 관계를 만든다
        5. 저장한뒤 나오는 아이디 값을 반환해준다
        */
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(userService.findUserById(anyLong())).willReturn(user);
        given(postScrapRepository.save(any())).willReturn(postScrap);

        PostScrapInfoOutput result = postScrapService.postScrap(new PostScrapInput(1L, 1L));

        Assertions.assertThat(result.getPostScrapId()).isEqualTo(1L);
    }

    @Test
    void 스크랩_리스트_불러오기() {
        /*
        스크랩 리스트 불러오기 시나리오
        1. 
        */
    }
}
