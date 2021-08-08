package com.team.post;

import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.PostData;
import com.team.dbutil.UserData;
import com.team.post.dto.request.SavePostRequest;
import com.team.post.dto.response.SavePostResponse;
import com.team.user.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private UserData userData;

    @Autowired
    private PostData postData;

    private User user1;
    private User user2;
    private User user3;

    String absolutePath;
    @BeforeEach
    void setUp() {

        String path = "src/test/resources/images";
        absolutePath = new File(path).getAbsolutePath();
        user1 = userData.saveUser("testUser1", "testNickname1", "test1@test.com");
        user2 = userData.saveUser("testUser2", "testNickname2", "test2@test.com");
        user3 = userData.saveUser("testUser3", "testNickname3", "test3@test.com");
    }

    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    @Test
    void 게시글_저장() throws IOException {
        MultipartFile postImage1 = new MockMultipartFile("apple.jpeg", new FileInputStream(absolutePath+"/apple.jpeg"));
        MultipartFile postImage2 = new MockMultipartFile("grape.jpeg", new FileInputStream(absolutePath+"/grape.jpeg"));
        String keyword = "inMoongram";
        SavePostRequest request = SavePostRequest.builder()
                .userId(user1.getId())
                .content("test-content")
                .postImages(Arrays.asList(postImage1, postImage2))
                .taggedUserIds(Arrays.asList(user2.getId(), user3.getId()))
                .taggedKeywords(Arrays.asList(keyword))
                .build();

        SavePostResponse response =
                given()
                        .port(port)
                        .accept(ContentType.JSON)
                        .multiPart("userId", request.getUserId())
                        .multiPart("content", request.getContent())
                        .multiPart("taggedUserIds", user2.getId())
                        .multiPart("taggedUserIds", user3.getId())
                        .multiPart("taggedKeywords", keyword)
                        .multiPart("postImages", new File(absolutePath+"/apple.jpeg"))
                        .multiPart("postImages", new File(absolutePath+"/grape.jpeg"))
                        .log().all()
                .when()
                        .post("/post")
                .then()
                        .statusCode(201)
                        .extract()
                        .as(SavePostResponse.class);

        assertThat(response.getPostId()).isEqualTo(1L);
    }

    @Test
    void 게시글_삭제() {
        Post post = postData.savePost(user1);

        given()
                .port(port)
                .accept("application/json")
        .when()
                .delete("/post/{post-id}", post.getId())
        .then()
                .statusCode(204);
    }
}