package com.team.post;

import com.team.post.dto.input.PostScrapDeleteInput;
import com.team.post.dto.input.PostScrapGetInput;
import com.team.post.dto.input.PostScrapSaveInput;
import com.team.post.dto.output.PostScrapGetOutput;
import com.team.post.dto.output.PostScrapSaveOutput;
import com.team.user.User;
import com.team.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
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
    private Post post2;
    private PostScrap postScrap;
    private PostScrap postScrap2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("백승화")
                .email("a@naver.com")
                .nickname("peach")
                .build();
        post = new Post("hello" ,user);
        post2 = new Post("hello2" ,user);
        postScrap = new PostScrap(user, post);
        postScrap.setIdForTest(1L);
        postScrap2 = new PostScrap(user, post2);
        postScrap2.setIdForTest(2L);
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

        PostScrapSaveOutput result = postScrapService.postScrap(new PostScrapSaveInput(1L, 1L));

        Assertions.assertThat(result.getPostScrapId()).isEqualTo(1L);
    }

    @Test
    void 스크랩_리스트_불러오기() {
        /*
        스크랩 리스트 불러오기 시나리오
        1. 유저 자신의 id를 path-variable로 날린다
        2. postScrapRepository에서 user-id와 일치하는 모든 post를 찾는다
        3. 리스트로 만들어서 반환하면 된다
        */
        List<PostScrap> postScraps = Arrays.asList(postScrap, postScrap2);
        given(postScrapRepository.findAllByUserId(1L)).willReturn(postScraps);

        PostScrapGetOutput output = postScrapService.getScrap(new PostScrapGetInput(1L));

        Assertions.assertThat(output.getPostScrapList().size()).isEqualTo(2);
    }
}
