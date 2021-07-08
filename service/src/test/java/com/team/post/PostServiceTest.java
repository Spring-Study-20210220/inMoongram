package com.team.post;

import com.team.post.dto.input.SavePostInput;
import com.team.post.dto.output.SavePostOutput;
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

    @InjectMocks
    private PostService postService;

    private User user1;
    private User user2;
    private User user3;

    private Post post;

    private PostImage postImage1;
    private PostImage postImage2;
    private List<PostImage> postImages;

    private PostTaggedUser userTag1;
    private PostTaggedUser userTag2;
    private List<PostTaggedUser> userTags;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("testUser1")
                .nickname("testNickname1")
                .email("test1@test.com")
                .password("testPassword1")
                .build();
        user1.setIdForTest(1L);

        user2 = User.builder()
                .name("testUser2")
                .nickname("testNickname2")
                .email("test2@test.com")
                .password("testPassword2")
                .build();
        user2.setIdForTest(2L);

        user3 = User.builder()
                .name("testUser3")
                .nickname("testNickname3")
                .email("test3@test.com")
                .password("testPassword3")
                .build();
        user3.setIdForTest(3L);

        post = new Post("test-content", user1);

        postImage1 = new PostImage("image1.jpg", "11111111-1111-1111-1111-111111111111.jpg");
        postImage2 = new PostImage("image2.jpg", "22222222-2222-2222-2222-222222222222.jpg");
        postImages = Arrays.asList(postImage1, postImage2);

        userTag1 = new PostTaggedUser(user2, post);
        userTag2 = new PostTaggedUser(user3, post);
        userTags = Arrays.asList(userTag1, userTag2);
    }

    @Test
    void 게시글_저장() {
        given(userService.findUserById(any())).willReturn(user1).willReturn(user2).willReturn(user3);
        given(postImageService.findImagesByIds(any())).willReturn(postImages);
        given(postRepository.save(any())).willReturn(post);
        given(postTaggedUserService.tagAll(any(), any())).willReturn(userTags);

        SavePostInput input = SavePostInput.builder()
                .userId(user1.getId())
                .content("test-content")
                .postImageIds(postImages.stream().map(PostImage::getId).collect(Collectors.toList()))
                .taggedUserIds(Arrays.asList(user2.getId(), user3.getId()))
                .build();

        SavePostOutput output = postService.save(input);

        assertThat(output.getContent()).isEqualTo(input.getContent());
        assertThat(output.getPostImages().size()).isEqualTo(2);
        assertThat(output.getPostImages().get(0)).isEqualTo(postImage1.getUploadFileName());
        assertThat(output.getPostImages().get(1)).isEqualTo(postImage2.getUploadFileName());
        assertThat(output.getTaggedUserIds().size()).isEqualTo(2);
        assertThat(output.getTaggedUserIds().get(0)).isEqualTo(user2.getId());
        assertThat(output.getTaggedUserIds().get(1)).isEqualTo(user3.getId());
    }
}