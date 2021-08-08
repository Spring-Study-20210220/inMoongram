package com.team.post;

import com.team.authUtil.TestAuthProvider;
import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.PostData;
import com.team.dbutil.UserData;
import com.team.post.dto.request.PostLikeCreateRequest;
import com.team.post.dto.response.PostLikeCreateResponse;
import com.team.post.dto.response.PostLikeInfoResponse;
import com.team.user.User;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = {"dev"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostLikeAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private TestAuthProvider testAuthProvider;

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

        Cookie cookie = testAuthProvider.getAccessTokenCookie(likeUser);

        Post post = postData.savePost(author);

        var request = new PostLikeCreateRequest(likeUser.getId());

        given()
                .port(port)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/post/{post_id}/like", post.getId())
                .then()
                .statusCode(201);
    }

    @Test
    void 좋아요취소() {
        User author = userData.saveUser("승화", "suenhwa", "test1@naver.com");
        User likeUser = userData.saveUser("준수", "junsu", "test2@naver.com");
        Post post = postData.savePost(author);
        Long postLikeId = getPostLikeTest(post, likeUser);
        assertThat(postLikeId).isPositive();

        Cookie cookie = testAuthProvider.getAccessTokenCookie(likeUser);

        given()
                .cookie(cookie)
                .port(port)
                .when()
                .delete("/post/{post_id}/like/{postLikeId}", post.getId(), postLikeId)
                .then()
                .statusCode(204);
    }

    @Test
    void 좋아요조회() {
        User author = userData.saveUser("승화", "suenhwa", "test1@naver.com");
        Post post = postData.savePost(author);
        Long postLikeId1 = getPostLikeTest("준수", "Jung", "jung@gmail.com", post.getId());
        Long postLikeId2 = getPostLikeTest("용우", "yong", "yong@gmail.com", post.getId());
        Long postLikeId3 = getPostLikeTest("카리나", "karina", "karina@gamil.com", post.getId());

        Response response = given()
                .port(port)
                .when()
                .get("/post/{postId}/likes", post.getId())
                .thenReturn();

        assertThat(response.getStatusCode()).isEqualTo(200);
        var responseList = response.as(PostLikeInfoResponse.class).getPostLikes();
        assertThat(responseList.size()).isEqualTo(3);
    }

    public Long getPostLikeTest(Post post, User likeUser) {
        Cookie cookie = testAuthProvider.getAccessTokenCookie(likeUser);
        var request = new PostLikeCreateRequest(likeUser.getId());

        Response response = given()
                .port(port)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/post/{post_id}/like", post.getId())
                .thenReturn();

        assertThat(response.getStatusCode()).isEqualTo(201);

        return response.getBody()
                .as(PostLikeCreateResponse.class)
                .getPostLikeId();
    }

    public Long getPostLikeTest(String userName, String nickName, String email, Long testPostId) {
        User likeUser = userData.saveUser(userName, nickName, email);
        Cookie cookie = testAuthProvider.getAccessTokenCookie(likeUser);
        var request = new PostLikeCreateRequest(likeUser.getId());

        Response response = given()
                .port(port)
                .cookie(cookie)
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
}
