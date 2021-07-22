package com.team.comment;

import com.team.comment.dto.request.CommentSaveRequest;
import com.team.comment.dto.response.CommentInfoResponse;
import com.team.comment.dto.response.CommentSaveResponse;
import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.PostData;
import com.team.dbutil.UserData;
import com.team.post.Post;
import com.team.user.User;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private UserData userData;

    @Autowired
    private PostData postData;

    private Long testPostId;
    private Post post;
    private User postAuthor;
    private User commentWriter;
    private User taggedUser1;
    private User taggedUser2;

    @BeforeEach
    void setup(){
        commentWriter = userData.saveUser("승화", "a", "a@naver.com");
        postAuthor = userData.saveUser("준수", "b", "b@naver.com");
        taggedUser1 = userData.saveUser("용우", "c", "c@naver.com");
        taggedUser2 = userData.saveUser("원식", "d", "d@naver.com");
        post = postData.savePost("content", postAuthor);
        testPostId = post.getId();
    }


    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    @Test
    void 댓글생성() {
        User author = userData.saveUser("승화", "a", "a@naver.com");
        User commentWriter = userData.saveUser("준수", "b", "b@naver.com");
        Post post = postData.savePost("content", author);

        var request = CommentSaveRequest.builder()
                .writerId(commentWriter.getId())
                .postId(post.getId())
                .content("댓글내용")
                .build();
        given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/comment")
                .then()
                .statusCode(201);
    }

    @Test
    void 댓글조회() {
        List<String> taggedKeywords = List.of("아이유", "카더가든","헤이즈");
        List<Long> taggedUserIds = List.of(taggedUser1.getId(),taggedUser2.getId());
        Long superCommentId = getCommentTestData(null,taggedKeywords,null);
        Long subCommentId1 = getCommentTestData(superCommentId,null,taggedUserIds);
        Long subCommentId2 = getCommentTestData(superCommentId,null,null);
        Response response = given()
                .port(port)
                .param("postId", testPostId)
        .when()
                .get("/comments")
        .thenReturn();

        assertThat(response.getStatusCode()).isEqualTo(200);
        var responseList = response.as(CommentInfoResponse.class)
                .getCommentInfos();
        assertThat(responseList.get(0).getTaggedKeywords())
                .extracting("keyword")
                .containsAll(taggedKeywords);
        assertThat(responseList.get(0).getSubComments())
                .extracting("id")
                .contains(subCommentId1,subCommentId2);
    }

    @Test
    void 댓글삭제() {
        Long commentId = getCommentTestData(null,null,null);
        given()
                .port(port)
        .when()
                .delete("/comment/{commentId}",commentId)
        .then()
                .statusCode(204);
    }

    public Long getCommentTestData(Long superCommentId, List<String> taggedKeywords, List<Long> userIds) {
        var request = CommentSaveRequest.builder()
                .writerId(commentWriter.getId())
                .postId(post.getId())
                .content("댓글내용")
                .superCommentId(superCommentId)
                .commentTaggedKeywords(taggedKeywords)
                .commentTaggedUserIds(userIds)
                .build();

        Response response = given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/comment")
                .thenReturn();

        return response.getBody()
                .as(CommentSaveResponse.class)
                .getCommentId();
    }
}
