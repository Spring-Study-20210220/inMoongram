package com.team.post;

import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.PostData;
import com.team.dbutil.PostScrapData;
import com.team.dbutil.UserData;
import com.team.post.dto.request.PostScrapSaveRequest;
import com.team.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostScrapAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private PostData postData;

    @Autowired
    private PostScrapData postScrapData;

    @Autowired
    private UserData userData;

    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    @Test
    void 스크랩_저장() {
        User user = userData.saveUser("승화", "peach", "a@naver.com");
        Post post = postData.savePost("안녕하세요", user);

        given()
                .port(port)
                .accept("application/json")
                .contentType("application/json")
                .body(new PostScrapSaveRequest(user.getId(), post.getId()))
        .when()
                .post("/scrap")
        .then()
                .statusCode(201)
                .body("postScrapId", is(1));
    }
}
