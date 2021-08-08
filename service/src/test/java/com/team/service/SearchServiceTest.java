package com.team.service;

import com.team.post.Post;
import com.team.search.PostSearch;
import com.team.search.SearchQueryRepository;
import com.team.search.SearchService;
import com.team.search.UserSearch;
import com.team.tag.PostTaggedKeyword;
import com.team.tag.TagKeyword;
import com.team.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchQueryRepository searchQueryRepository;

    @InjectMocks
    private SearchService searchService;

    private TagKeyword tag1;
    private TagKeyword tag2;

    private PostTaggedKeyword taggedKeyword1;
    private PostTaggedKeyword taggedKeyword2;

    private Post post1;
    private Post post2;
    private Post post3;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = User.builder()
                .id(1L)
                .name("junsu")
                .nickname("testNickname1")
                .email("test1@test.com")
                .password("testPassword1")
                .introduction("i m spring user")
                .build();
        user2 = User.builder()
                .id(2L)
                .name("yongwoo")
                .nickname("testNickname2")
                .email("test2@test.com")
                .password("testPassword2")
                .introduction("testText")
                .build();

        post1 = new Post("test-content", user1);
        post2 = new Post("testcont", user1);
        post3 = new Post("testcont", user1);

        tag1 = new TagKeyword("springSecurity");
        tag2 = new TagKeyword("spring");

        taggedKeyword1 = new PostTaggedKeyword(tag1, post1);
        taggedKeyword2 = new PostTaggedKeyword(tag2, post2);
        taggedKeyword2 = new PostTaggedKeyword(tag2, post3);
    }

    @Test
    void 검색() {
        given(searchQueryRepository.findUsersByKeyword(any()))
                .willReturn(
                        List.of(new UserSearch(user1.getName(), user1.getNickname(), user1.getProfileImage()),
                                new UserSearch(user2.getName(), user2.getNickname(), user2.getProfileImage()))
                );
        given(searchQueryRepository.findPostTaggedKeywordByKeyword(any()))
                .willReturn(
                        List.of(new PostSearch(tag1.getKeyword(), 1L),
                                new PostSearch(tag2.getKeyword(), 2L))
                );

        var result = searchService.search("spring");

        assertThat(result.getPostSearchInfos())
                .extracting("keyword")
                .contains(tag1.getKeyword(), tag2.getKeyword());
        assertThat(result.getPostSearchInfos().size()).isEqualTo(2);
        assertThat(result.getUserSearchInfos())
                .extracting("name")
                .contains(user1.getName(), user2.getName());
        assertThat(result.getUserSearchInfos().size()).isEqualTo(2);
    }
}
