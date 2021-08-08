package com.team.comment;

import com.team.comment.dto.input.CommentLikePlusInput;
import com.team.comment.dto.output.CommentLikePlusOutput;
import com.team.post.Post;
import com.team.user.User;
import com.team.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;

    @InjectMocks
    private CommentLikeService commentLikeService;

    private Comment comment1;
    private User user1;
    private Post post1;
    private CommentLike commentLike1;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("백승화")
                .email("a@naver.com")
                .nickname("peach")
                .build();
        user1.setIdForTest(1L);
        post1 = new Post("hello", user1);
        post1.setIdForTest(1L);
        comment1 = Comment.builder()
                .content("안녕하세요")
                .user(user1)
                .post(post1)
                .build();
        comment1.setIdForTest(1L);
        commentLike1 = new CommentLike(comment1, user1);
        commentLike1.setIdForTest(1L);
    }

    @Test
    void 좋아요_추가() {
        /*
        좋아요 시나리오
        1. 유저의 id랑 comment의 id를 주입받는다
        2. 유저랑 코멘트를 찾는다
        3. commentLike 객체를 생성한다
        4. 생성된 인스턴스 저장
        5. 반환
         */
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment1));
        given(commentLikeRepository.save(any())).willReturn(commentLike1);

        CommentLikePlusOutput output = commentLikeService.like(user1.getId(), new CommentLikePlusInput(comment1.getId()));

        Assertions.assertThat(output.getCommentLikeId()).isEqualTo(commentLike1.getId());
    }

    @Test
    void 특정_댓글의_좋아요_가져오기() {
        /*
        좋아요 가져오기 시나리오
        1. 댓글 리스트를 불러온다
        2. 해당 댓글에 자신이 좋아요를 눌렀는지 확인한다
        3. 눌렀다면 boolean으로 표시후 반환
         */
    }
}
