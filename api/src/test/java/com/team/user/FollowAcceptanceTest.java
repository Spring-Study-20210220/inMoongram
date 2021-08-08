package com.team.user;

import com.team.authUtil.TestAuthProvider;
import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.FollowData;
import com.team.dbutil.UserData;
import com.team.user.dto.request.FollowRequest;
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
class FollowAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private TestAuthProvider testAuthProvider;

    @Autowired
    private UserData userData;

    @Autowired
    private FollowData followData;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userData.saveUser("testUser1", "testNickname1", "test1@test.com");
        user2 = userData.saveUser("testUser2", "testNickname2", "test2@test.com");
    }

    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    @Test
    void 팔로우_취소() {
        Cookie cookie = testAuthProvider.getAccessTokenCookie("test@test.com", "userName", "userNickName");
        Follow follow = followData.saveFollow(user1, user2);

        given()
                .port(port)
                .cookie(cookie)
                .accept("application/json")
                .contentType("application/json")
                .when()
                .delete("follow/" + follow.getId() + "/unfollow")
                .then()
                .statusCode(204);
    }

    @Test
    void 팔로우() {
        Cookie cookie = testAuthProvider.getAccessTokenCookie(user1);

        given()
                .port(port)
                .cookie(cookie)
                .accept("application/json")
                .contentType("application/json")
                .body(new FollowRequest(user2.getId()))
                .when()
                .post("/follow")
                .then()
                .statusCode(201)
                .body("followId", is(1))
                .body("followerId", is(user1.getId().intValue()))
                .body("followeeId", is(user2.getId().intValue()));
    }
}