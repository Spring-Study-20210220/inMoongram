package com.team.comment;

import com.team.comment.dto.request.CommentLikePlusRequest;
import com.team.dbutil.*;
import com.team.post.Post;
import com.team.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentLikeAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbClean;

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
        CommentLikePlusRequest request = new CommentLikePlusRequest(user1.getId(), comment1.getId());

        given()
                .port(port)
                .accept("application/json")
                .contentType("application/json")
                .body(request)
        .when()
                .post("/comment-like")
        .then()
                .statusCode(201)
                .body("commentLikeId", is(1));
    }
}
