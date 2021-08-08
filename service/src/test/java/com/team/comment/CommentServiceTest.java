package com.team.comment;

import com.team.comment.dto.input.CommentSaveInput;
import com.team.post.Post;
import com.team.post.PostService;
import com.team.tag.CommentTaggedKeyword;
import com.team.tag.CommentTaggedUser;
import com.team.tag.TagKeyword;
import com.team.user.User;
import com.team.user.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("CommentService 테스트")
class CommentServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private PostService postService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentTaggedUserService commentTaggedUserService;
    @Mock
    private CommentTaggedKeywordService commentTaggedKeywordService;
    @InjectMocks
    private CommentService commentService;

    private User postAuthor;
    private User commentWriter;

    private Post post;
    private Comment comment;

    private CommentTaggedUser commentTaggedUser1;
    private CommentTaggedUser commentTaggedUser2;
    private CommentTaggedKeyword commentTaggedKeyword1;
    private CommentTaggedKeyword commentTaggedKeyword2;

    @BeforeEach
    void setUp() {
        postAuthor = User.builder()
                .id(1L)
                .name("testPostAuthor")
                .nickname("testNickForPostAuthor")
                .email("testAuthor@email.com")
                .password("test1234~!")
                .build();
        commentWriter = User.builder()
                .id(2L)
                .name("testCommentWriter")
                .nickname("testNickForCommentWriter")
                .email("testCommentWriter@email.com")
                .password("test1234~!")
                .build();
        User taggedUser1 = User.builder()
                .id(3L)
                .name("testTaggedUser1")
                .nickname("testNickForTaggedUser1")
                .email("testTaggedUser1@email.com")
                .password("test1234~!")
                .build();
        User taggedUser2 = User.builder()
                .id(4L)
                .name("testTaggedUser2")
                .nickname("testNickForTaggedUser2")
                .email("testTaggedUser2@email.com")
                .password("test1234~!")
                .build();

        TagKeyword tagKeyword1 = new TagKeyword(4L, "아이유");
        TagKeyword tagKeyword2 = new TagKeyword(5L, "헤이즈");

        post = new Post("postContent", postAuthor);
        post.setIdForTest(4L);

        comment = Comment.builder()
                .id(5L)
                .user(commentWriter)
                .post(post)
                .content("commentContent")
                .build();

        commentTaggedUser1 = new CommentTaggedUser(taggedUser1, comment);
        commentTaggedUser2 = new CommentTaggedUser(taggedUser2, comment);
        comment.addAllTaggedUsers(List.of(commentTaggedUser1, commentTaggedUser2));
        commentTaggedKeyword1 = new CommentTaggedKeyword(comment, tagKeyword1);
        commentTaggedKeyword2 = new CommentTaggedKeyword(comment, tagKeyword2);
        comment.addAllTaggedKeywords(List.of(commentTaggedKeyword1, commentTaggedKeyword2));
    }

    @Test
    void 댓글_작성() {
        CommentSaveInput input = CommentSaveInput.builder()
                .postId(post.getId())
                .content("testContent")
                .commentTaggedKeywords(Lists.list("아이유", "헤이즈"))
                .commentTaggedUserIds(Lists.list(3L))
                .build();

        given(userService.findUserById(any())).willReturn(commentWriter);
        given(postService.findPostById(any())).willReturn(post);
        given(commentRepository.save(any())).willReturn(comment);

        given(commentTaggedUserService.tagAllUsers(any(), any()))
                .willReturn(List.of(commentTaggedUser1, commentTaggedUser2));
        given(commentTaggedKeywordService.tagAllKeywords(any(), any()))
                .willReturn(List.of(commentTaggedKeyword1, commentTaggedKeyword2));

        var output = commentService.saveComment(postAuthor.getId(), input);

        assertThat(output.getCommentId()).isEqualTo(5L);
    }

    @Test
    @Disabled
    void 댓글_삭제() {

    }

    @Test
    void 댓글_조회() {
        given(postService.findPostById(any())).willReturn(post);
        var output = commentService.getComments(post.getId());

        assertThat(output.size()).isEqualTo(post.getComments().size());
        assertThat(output.get(0).getContent()).isEqualTo(comment.getContent());
        assertThat(output.get(0).getTaggedKeywords())
                .extracting("keyword")
                .contains("아이유", "헤이즈");
        assertThat(output.get(0).getTaggedUsers())
                .extracting("taggedUserId")
                .contains(3L, 4L);
    }
}
