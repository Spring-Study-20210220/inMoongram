package com.team.post;

import com.team.post.dto.input.SavePostInput;
import com.team.post.dto.output.SavePostOutput;
import com.team.tag.PostTaggedKeyword;
import com.team.tag.PostTaggedUser;
import com.team.tag.TagKeyword;
import com.team.user.User;
import com.team.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostImageService postImageService;

    @Mock
    private PostTaggedUserService postTaggedUserService;

    @Mock
    private PostTaggedKeywordService postTaggedKeywordService;

    @InjectMocks
    private PostService postService;

    private User user1;
    private User user2;
    private User user3;

    private Post post;

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

        post = new Post("test-content", user1);
        post.setIdForTest(1L);
    }

    @Test
    void 게시글_저장() {
        PostImage postImage1 = new PostImage("image1.jpg", "11111111-1111-1111-1111-111111111111.jpg", "src/images/11111111-1111-1111-1111-111111111111.jpg");
        PostImage postImage2 = new PostImage("image2.jpg", "22222222-2222-2222-2222-222222222222.jpg", "src/images/22222222-2222-2222-2222-222222222222.jpg");;
        List<PostImage> postImages = Arrays.asList(postImage1, postImage2);

        PostTaggedUser userTag1 = new PostTaggedUser(user2, post);
        PostTaggedUser userTag2 = new PostTaggedUser(user3, post);
        List<PostTaggedUser> userTags = Arrays.asList(userTag1, userTag2);

        PostTaggedKeyword keywordTag1 = new PostTaggedKeyword(new TagKeyword("inMoongram"), post);
        PostTaggedKeyword keywordTag2 = new PostTaggedKeyword(new TagKeyword("spring"), post);
        List<PostTaggedKeyword> keywordTags = Arrays.asList(keywordTag1, keywordTag2);

        given(userService.findUserById(any())).willReturn(user1).willReturn(user2).willReturn(user3);
        given(postImageService.storeImages(any())).willReturn(postImages);
        given(postRepository.save(any())).willReturn(post);
        given(postTaggedUserService.tagAll(any(), any())).willReturn(userTags);
        given(postTaggedKeywordService.tagAll(any(), any())).willReturn(keywordTags);

        SavePostInput input = SavePostInput.builder()
                .userId(user1.getId())
                .content("test-content")
                .taggedUserIds(Arrays.asList(user2.getId(), user3.getId()))
                .taggedKeywords(Arrays.asList("inMoongram", "spring"))
                .build();

        SavePostOutput output = postService.save(input);

        assertThat(output.getPostId()).isEqualTo(post.getId());
    }

    @Test
    void 게시글_삭제() {
        given(postRepository.findById(any())).willReturn(Optional.of(post));
        postService.delete(post.getId());

        assertThat(user1.getPosts().size()).isEqualTo(0);
    }
}