package com.team.user;

import com.team.authUtil.TestAuthProvider;
import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.FollowData;
import com.team.dbutil.UserData;
import com.team.post.dto.response.FeedResponse;
import com.team.post.dto.response.SavePostResponse;
import com.team.user.dto.output.FollowListOutput;
import com.team.user.dto.request.UserProfileModificationRequest;
import com.team.user.dto.response.FollowerInfoListResponse;
import com.team.user.dto.response.FollowerInfoResponse;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = {"dev"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private UserData userData;

    @Autowired
    private FollowData followData;

    @Autowired
    private TestAuthProvider testAuthProvider;

    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    @Test
    @DisplayName("팔로워 리스트 가져오기")
    void getFollowerList() {
        User user1 = userData.saveUser("승화", "a", "a@naver.com");
        User user2 = userData.saveUser("준수", "b", "b@naver.com");
        User user3 = userData.saveUser("용우", "c", "c@naver.com");
        User user4 = userData.saveUser("용우1", "c1", "c1@naver.com");
        Follow follow1 = followData.saveFollow(user2, user1);
        Follow follow2 = followData.saveFollow(user3, user1);
        Follow follow3 = followData.saveFollow(user4, user1);

        Cookie cookie = testAuthProvider.getAccessTokenCookie(user1);

        List<FollowerInfoResponse> actual = getFollowerTest(cookie);
        actual.sort(Comparator.comparingLong(FollowerInfoResponse::getUserId));
        List<User> expected = Arrays.asList(user2, user3, user4);
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            Assertions.assertThat(actual.get(i).getUserId()).isEqualTo(expected.get(i).getId());
            Assertions.assertThat(actual.get(i).getName()).isEqualTo(expected.get(i).getName());
            Assertions.assertThat(actual.get(i).getNickName()).isEqualTo(expected.get(i).getNickname());
        }
    }

    @Test
    @Disabled
    void 유저_1만건_팔로워_테스트() {
        User user = userData.saveUser("승화", "a", "a@naver.com");
        int max = 10000;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            users.add(userData.saveUser("승화" + i, "a" + i, "a" + i + "@naver.com"));
        }
        List<Follow> follows = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            follows.add(followData.saveFollow(users.get(i), user));
        }
        Cookie cookie = testAuthProvider.getAccessTokenCookie(user);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<FollowerInfoResponse> actual = getFollowerTest(cookie);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        actual.sort(Comparator.comparingLong(FollowerInfoResponse::getUserId));
        Assertions.assertThat(actual.size()).isEqualTo(users.size());
        for (int i = 0; i < actual.size(); i++) {
            Assertions.assertThat(actual.get(i).getUserId()).isEqualTo(users.get(i).getId());
            Assertions.assertThat(actual.get(i).getName()).isEqualTo(users.get(i).getName());
            Assertions.assertThat(actual.get(i).getNickName()).isEqualTo(users.get(i).getNickname());
        }
    }

    @Test
    @DisplayName("맞팔로우 확인")
    void followBackCheck() {
        User user1 = userData.saveUser("승화", "a", "a@naver.com");
        User user2 = userData.saveUser("준수", "b", "b@naver.com");
        User user3 = userData.saveUser("용우", "c", "c@naver.com");
        Follow follow1 = followData.saveFollow(user2, user1);
        Follow follow2 = followData.saveFollow(user3, user1);
        Follow follow3 = followData.saveFollow(user1, user2);
        Cookie cookie = testAuthProvider.getAccessTokenCookie(user1);
        List<FollowerInfoResponse> actual = getFollowerTest(cookie);
        actual.sort(Comparator.comparingLong(FollowerInfoResponse::getUserId));
        List<User> expected = Arrays.asList(user2);
        Assertions.assertThat(actual.size()).isEqualTo(2);
        for (FollowerInfoResponse followerInfoResponse : actual) {
            if (followerInfoResponse.getUserId().equals(user2.getId())) {
                Assertions.assertThat(followerInfoResponse.isFollowBack()).isTrue();
            }
            if (followerInfoResponse.getUserId().equals(user3.getId())) {
                Assertions.assertThat(followerInfoResponse.isFollowBack()).isFalse();
            }
        }
    }

    List<FollowerInfoResponse> getFollowerTest(Cookie cookie) {
        Response response =
                given()
                        .cookie(cookie)
                        .port(port)
                        .when()
                        .get("user/followers")
                        .thenReturn();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
        return response.getBody()
                .as(FollowerInfoListResponse.class)
                .getFollowerInfoResponses();
    }

    @Test
    void 유저정보변경_정상() {
        Cookie cookie = testAuthProvider.getAccessTokenCookie("test@test.com", "testUser", "myNick");

        var reqDto = UserProfileModificationRequest.builder()
                .email("test@test.com")
                .name("testuser")
                .nickname("testspring")
                .website("test.com")
                .introduction("this is test")
                .phoneNumber("010-1234-5678")
                .profileImage("image")
                .sex(Sex.MALE)
                .build();

        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                cookie(cookie).
                port(port).
                body(reqDto).
                when().
                patch("/user/profile").
                then().
                statusCode(204);
    }

    @Test
    void 유저정보변경_잘못된요청데이터() {
        Cookie cookie = testAuthProvider.getAccessTokenCookie("test@test.com", "testUser", "myNick");

        var reqDto = UserProfileModificationRequest.builder()
                .name("testuser")
                .nickname("testspring")
                .website("test.com")
                .introduction("this is test")
                .phoneNumber("010-1234-5678")
                .profileImage("image")
                .sex(Sex.MALE)
                .build();

        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                cookie(cookie).
                port(port).
                body(reqDto).
                when().
                patch("/user/profile").
                then().
                statusCode(400);
    }

    @Test
    void 팔로우_목록_조회() {
        User user1 = userData.saveUser("testUser1", "testNickname1", "test1@test.com");
        User user2 = userData.saveUser("testUser2", "testNickname2", "test2@test.com");
        User user3 = userData.saveUser("testUser3", "testNickname3", "test3@test.com");
        Follow follow1 = followData.saveFollow(user1, user2);
        Follow follow2 = followData.saveFollow(user1, user3);

        Cookie cookie = testAuthProvider.getAccessTokenCookie(user1);

        FollowListOutput response =
                given()
                        .port(port)
                        .cookie(cookie)
                        .accept("application/json")
                        .contentType("application/json")
                        .when()
                        .get("user/followings")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(FollowListOutput.class);

        assertThat(response.getUsers().size()).isEqualTo(2);
        List<FollowListOutput.UserInfo> actual = response.getUsers();
        List<User> expected = Arrays.asList(user2, user3);
        Collections.sort(actual, Comparator.comparingLong(FollowListOutput.UserInfo::getUserId));
        assertThat(response.getUsers().get(0).getName()).isEqualTo(user2.getName());
        assertThat(response.getUsers().get(0).getNickname()).isEqualTo(user2.getNickname());
        assertThat(response.getUsers().get(0).getFollowId()).isEqualTo(follow1.getId());
        assertThat(response.getUsers().get(1).getName()).isEqualTo(user3.getName());
        assertThat(response.getUsers().get(1).getNickname()).isEqualTo(user3.getNickname());
        assertThat(response.getUsers().get(1).getFollowId()).isEqualTo(follow2.getId());
    }

    @Test
    void 피드_조회() {
        User user1 = userData.saveUser("testUser1", "testNickname1", "test1@test.com");
        User user2 = userData.saveUser("testUser2", "testNickname2", "test2@test.com");
        User user3 = userData.saveUser("testUser3", "testNickname3", "test3@test.com");
        User user4 = userData.saveUser("testUser4", "testNickname4", "test4@test.com");

        Follow follow1 = followData.saveFollow(user1, user2);
        Follow follow2 = followData.saveFollow(user1, user3);

        Cookie cookie = testAuthProvider.getAccessTokenCookie(user1);

        savePost(user2, "first");
        savePost(user3, "second");
        savePost(user4, "third");
        savePost(user2, "fourth");

        FeedResponse response =
                given()
                        .port(port)
                        .cookie(cookie)
                        .accept("application/json")
                        .param("page-no", 1)
                        .when()
                        .get("/user/feed")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(FeedResponse.class);

        assertThat(response.getFeedInfos().size()).isEqualTo(3);
        assertThat(response.getFeedInfos().get(0).getContent()).isEqualTo("fourth");
        assertThat(response.getFeedInfos().get(1).getContent()).isEqualTo("second");
        assertThat(response.getFeedInfos().get(2).getContent()).isEqualTo("first");
    }

    private void savePost(User user, String content) {
        String path = "src/test/resources/images";
        String absolutePath = new File(path).getAbsolutePath();
        Cookie cookie = testAuthProvider.getAccessTokenCookie(user);
        SavePostResponse response =
                given()
                        .port(port)
                        .cookie(cookie)
                        .accept(ContentType.JSON)
                        .multiPart("content", content)
                        .multiPart("taggedUserIds", 2L)
                        .multiPart("taggedUserIds", 3L)
                        .multiPart("taggedUserIds", 4L)
                        .multiPart("postImages", new File(absolutePath + "/apple.jpeg"))
                        .when()
                        .post("/post")
                        .then()
                        .extract()
                        .as(SavePostResponse.class);
    }
}
