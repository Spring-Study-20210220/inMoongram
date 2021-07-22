package com.team.post;

import com.team.tag.*;
import com.team.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostTaggedKeywordServiceTest {
    @Mock
    private PostTaggedKeywordRepository postTaggedKeywordRepository;

    @Mock
    private TagKeywordRepository tagKeywordRepository;

    @InjectMocks
    private PostTaggedKeywordService postTaggedKeywordService;

    private User user;

    private Post post;

    private TagKeyword tag1;
    private TagKeyword tag2;

    private PostTaggedKeyword taggedKeyword1;
    private PostTaggedKeyword taggedKeyword2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("testUser1")
                .nickname("testNickname1")
                .email("test1@test.com")
                .password("testPassword1")
                .build();

        post = new Post("test-content", user);

        tag1 = new TagKeyword("inMoongram");
        tag2 = new TagKeyword("spring");

        taggedKeyword1 = new PostTaggedKeyword(tag1, post);
        taggedKeyword2 = new PostTaggedKeyword(tag2, post);
    }

    @Test
    void tagAll() {
        given(tagKeywordRepository.findByKeyword(any())).willReturn(Optional.of(tag1)).willReturn(Optional.of(tag2));
        given(postTaggedKeywordRepository.save(any())).willReturn(taggedKeyword1).willReturn(taggedKeyword2);
        List<PostTaggedKeyword> output = postTaggedKeywordService.tagAll(Arrays.asList("inMoongram", "spring"), post);

        assertThat(output.size()).isEqualTo(2);
        assertThat(output.get(0).getTagKeyword().getKeyword()).isEqualTo("inMoongram");
        assertThat(output.get(1).getTagKeyword().getKeyword()).isEqualTo("spring");
    }
}