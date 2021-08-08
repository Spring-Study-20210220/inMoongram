package com.team.comment;

import com.team.authUtil.TestAuthProvider;
import com.team.comment.dto.request.CommentLikePlusRequest;
import com.team.dbutil.*;
import com.team.post.Post;
import com.team.user.User;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@ActiveProfiles(value = {"dev"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentLikeAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbClean;

    @Autowired
    private TestAuthProvider testAuthProvider;

    @Autowired
    private UserData userData;

    @Autowired
    private PostData postData;

    @Autowired
    private CommentData commentData;

    @Autowired
    private CommentLikeData commentLikeData;

    private User user1;
    private Post post1;
    private Comment comment1;

    @BeforeEach
    void setUp() {
        user1 = userData.saveUser("승화", "peach", "a@naver.com");
        post1 = postData.savePost(user1);
        comment1 = commentData.saveComment(user1, post1);
    }

    @AfterEach
    void tearDown() {
        dbClean.execute();
    }

    @Test
    void 좋아요() {
        Cookie cookie = testAuthProvider.getAccessTokenCookie();
        CommentLikePlusRequest request = new CommentLikePlusRequest(comment1.getId());

        given()
                .port(port)
                .cookie(cookie)
                .accept("application/json")
                .contentType("application/json")
                .body(request)
                .when()
                .post("/comment-like")
                .then()
                .statusCode(201)
                .body("commentLikeId", is(1));
    }

    @Test
    void 좋아요_취소() {
        Cookie cookie = testAuthProvider.getAccessTokenCookie();
        CommentLike saved = commentLikeData.saveCommentLike(comment1, user1);

        given()
                .port(port)
                .cookie(cookie)
                .accept("application/json")
                .when()
                .delete("/comment-like/{comment-like-id}", saved.getId())
                .then()
                .statusCode(204);
    }
}
