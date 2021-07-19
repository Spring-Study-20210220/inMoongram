package com.team.user;

import com.team.QueryConfig;
import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.FollowData;
import com.team.dbutil.UserData;
import com.team.user.dto.output.FollowListOutput;
import com.team.user.dto.request.UserProfileModificationRequest;
import com.team.user.dto.response.FollowerInfoListResponse;
import com.team.user.dto.response.FollowerInfoResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import javax.management.Query;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(QueryConfig.class)
class UserAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private UserData userData;

    @Autowired
    private FollowData followData;

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

        List<FollowerInfoResponse> actual = getFollowerTest(user1.getId());
        actual.sort(Comparator.comparingLong(FollowerInfoResponse::getUserId));
        List<User> expected = Arrays.asList(user2, user3, user4);
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            Assertions.assertThat(actual.get(i).getUserId()).isEqualTo(expected.get(i).getId());
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

        List<FollowerInfoResponse> actual = getFollowerTest(user1.getId());
        actual.sort(Comparator.comparingLong(FollowerInfoResponse::getUserId));
        List<User> expected = Arrays.asList(user2);
        Assertions.assertThat(actual.size()).isEqualTo(2);
        for (FollowerInfoResponse followerInfoResponse : actual) {
            if(followerInfoResponse.getUserId().equals(user2.getId())) {
                Assertions.assertThat(followerInfoResponse.isFollowBack()).isTrue();
            }
            if(followerInfoResponse.getUserId().equals(user3.getId())) {
                Assertions.assertThat(followerInfoResponse.isFollowBack()).isFalse();
            }
        }
    }

    List<FollowerInfoResponse> getFollowerTest(Long id) {
        Response response =
                given()
                        .port(port)
                .when()
                        .get("user/{id}/followers", id)
                .thenReturn();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
        return response.getBody()
                .as(FollowerInfoListResponse.class)
                .getFollowerInfoResponses();
    }

    @Test
    void 유저정보변경_정상() {
        User testUser = userData.saveUser("정준수", "test1", "jungjunsu@naver.com");
        Long testUserId = testUser.getId();

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
        assertThat(testUserId).isEqualTo(1L);
        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                port(port).
                body(reqDto).
        when().
                patch("/user/{user_id}/profile", testUserId).
        then().
                statusCode(204);
    }

    @Test
    void 유저정보변경_잘못된요청데이터() {
        User testUser = userData.saveUser("정준수", "test1", "jungjunsu@naver.com");
        Long testUserId = testUser.getId();

        var reqDto = UserProfileModificationRequest.builder()
                .name("testuser")
                .nickname("testspring")
                .website("test.com")
                .introduction("this is test")
                .phoneNumber("010-1234-5678")
                .profileImage("image")
                .sex(Sex.MALE)
                .build();
        assertThat(testUserId).isEqualTo(1L);
        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                port(port).
                body(reqDto).
        when().
                patch("/user/{user_id}/profile", testUserId).
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

        FollowListOutput response =
                given()
                        .port(port)
                        .accept("application/json")
                        .contentType("application/json")
                .when()
                        .get("user/{user-id}/followings",user1.getId())
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
}
