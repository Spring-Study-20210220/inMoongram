package com.team.post;

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
class PostImageServiceTest {
    @Mock
    private PostImageRepository postImageRepository;

    @InjectMocks
    private PostImageService postImageService;

    private User user;
    private PostImage postImage1;
    private PostImage postImage2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("testUser")
                .nickname("testNickname")
                .email("test@test.com")
                .password("testPassword")
                .build();

        postImage1 = new PostImage("image1.jpg", "11111111-1111-1111-1111-111111111111.jpg", "src/images/11111111-1111-1111-1111-111111111111.jpg");
        postImage2 = new PostImage("image2.jpg", "22222222-2222-2222-2222-222222222222.jpg", "src/images/22222222-2222-2222-2222-222222222222.jpg");
    }

    @Test
    void id로_image_조회() {
        given(postImageRepository.findById(any())).willReturn(Optional.of(postImage1)).willReturn(Optional.of(postImage2));
        List<PostImage> findImages = postImageService.findImagesByIds(Arrays.asList(1L, 2L));

        assertThat(findImages.size()).isEqualTo(2);
        assertThat(findImages.get(0).getUploadFileName()).isEqualTo(postImage1.getUploadFileName());
        assertThat(findImages.get(0).getStoreFileName()).isEqualTo(postImage1.getStoreFileName());
        assertThat(findImages.get(1).getUploadFileName()).isEqualTo(postImage2.getUploadFileName());
        assertThat(findImages.get(1).getStoreFileName()).isEqualTo(postImage2.getStoreFileName());
    }
}