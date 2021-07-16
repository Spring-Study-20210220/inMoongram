package com.team.post;

import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.PostData;
import com.team.dbutil.UserData;
import com.team.post.dto.request.PostLikeCreateRequest;
import com.team.post.dto.response.PostLikeCreateResponse;
import com.team.post.dto.response.PostLikeInfoListResponse;
import com.team.user.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostLikeAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private UserData userData;

    @Autowired
    private PostData postData;

    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    private Long testPostId;

    @Test
    void 좋아요생성() {
        User author = userData.saveUser("승화", "a", "a@naver.com");
        User likeUser = userData.saveUser("준수", "b", "b@naver.com");

        Post post = postData.savePost(author);

        var request = new PostLikeCreateRequest(likeUser.getId());

        given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
        .when()
                .post("/post/{post_id}/like", post.getId())
        .then()
                .statusCode(201);
    }

    public Long getPostLikeTest(String userName,String nickName){
        testPostId = getPostTest("승화", "a");
        return getPostLikeTest(userName,nickName,testPostId);
    }
    public Long getPostLikeTest(String userName,String nickName, Long testPostId){
        User likeUser = userData.saveUser(userName, nickName, "b@naver.com");

        var request = new PostLikeCreateRequest(likeUser.getId());

        Response response = given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/post/{post_id}/like", testPostId)
                .thenReturn();

        assertThat(response.getStatusCode()).isEqualTo(201);

        return response.getBody()
                .as(PostLikeCreateResponse.class)
                .getPostLikeId();
    }

    public Long getPostTest(String userName,String nickName){
        User author = userData.saveUser(userName, nickName, "a@naver.com");
        Post post = postData.savePost(author);
        return post.getId();
    }

    @Test
    void 좋아요취소() {
        Long postLikeId = getPostLikeTest("준수","Jung");
        assertThat(postLikeId).isPositive();

        given()
                .port(port)
        .when()
                .delete("/post/{post_id}/like/{postLikeId}", testPostId, postLikeId)
        .then()
                .statusCode(204);
    }

    @Test
    void 좋아요조회() {
        testPostId = getPostTest("승화", "a");
        Long postLikeId1 = getPostLikeTest("준수","Jung",testPostId);
        Long postLikeId2 = getPostLikeTest("용우","yong",testPostId);
        Long postLikeId3 = getPostLikeTest("카리나","karina",testPostId);

        Response response = given()
                .port(port)
        .when()
                .get("/post/{postId}/likes",testPostId)
        .thenReturn();

        assertThat(response.getStatusCode()).isEqualTo(200);
        var responseList = response.as(PostLikeInfoListResponse.class).getPostLikes();
        assertThat(responseList.size()).isEqualTo(3);
    }
}
