package com.team.post;

import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.PostData;
import com.team.dbutil.PostScrapData;
import com.team.dbutil.UserData;
import com.team.post.dto.request.PostScrapSaveRequest;
import com.team.post.dto.response.PostScrapGetResponse;
import com.team.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        Post post = postData.savePost(user);

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

    @Test
    void 스크랩_불러오기() {
        User user = userData.saveUser("승화", "peach", "a@naver.com");
        Post post1 = postData.savePost(user);
        Post post2 = postData.savePost(user);
        PostScrap postScrap1 = postScrapData.savePostScrap(user, post1);
        PostScrap postScrap2 = postScrapData.savePostScrap(user, post2);

        PostScrapGetResponse response =
                given()
                        .port(port)
                        .accept("application/json")
                .when()
                        .get("/scrap/{user-id}", user.getId())
                .thenReturn()
                        .body()
                        .as(PostScrapGetResponse.class);

        List<PostScrapGetResponse.PostScrapInfoResponse> actual = response.getPostScrapInfoResponseList();
        Collections.sort(actual, Comparator.comparingLong(PostScrapGetResponse.PostScrapInfoResponse::getPostScrapId));
        List<PostScrap> expected = Arrays.asList(postScrap1, postScrap2);
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++) {
            Assertions.assertThat(actual.get(i).getPostScrapId()).isEqualTo(expected.get(i).getId());
        }
    }

    @Test
    void 스크랩_삭제하기() {
        User user = userData.saveUser("승화", "peach", "a@naver.com");
        Post post1 = postData.savePost(user);
        Post post2 = postData.savePost(user);
        PostScrap postScrap1 = postScrapData.savePostScrap(user, post1);
        PostScrap postScrap2 = postScrapData.savePostScrap(user, post2);

        given()
                .port(port)
                .accept("application/json")
        .when()
                .delete("/scrap/{user-id}/{post-id}",user.getId(), post1.getId())
        .then()
                .statusCode(204);

    }
}
