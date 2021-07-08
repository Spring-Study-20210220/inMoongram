package com.team.post;

import com.team.dbutil.DatabaseCleanup;
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

    private User user1;
    private User user2;
    private User user3;

    private MultipartFile postImage1;
    private MultipartFile postImage2;

    @BeforeEach
    void setUp() throws IOException {
        user1 = userData.saveUser("testUser1", "testNickname1", "test1@test.com");
        user2 = userData.saveUser("testUser2", "testNickname2", "test2@test.com");
        user3 = userData.saveUser("testUser3", "testNickname3", "test3@test.com");
        postImage1 = new MockMultipartFile("apple.jpeg", new FileInputStream("images/apple.jpeg"));
        postImage2 = new MockMultipartFile("grape.jpeg", new FileInputStream("images/grape.jpeg"));
    }

    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    @Test
    void 게시글_저장() {
        SavePostRequest request = SavePostRequest.builder()
                .userId(user1.getId())
                .content("test-content")
                .postImages(Arrays.asList(postImage1, postImage2))
                .taggedUserIds(Arrays.asList(user2.getId(), user3.getId()))
                .build();

        SavePostResponse response =
                given()
                        .port(port)
                        .accept(ContentType.JSON)
                        .queryParam("userId", request.getUserId())
                        .queryParam("content", request.getContent())
                        .queryParam("taggedUserIds", request.getTaggedUserIds())
                        .multiPart("postImages", new File("images/apple.jpeg"))
                        .multiPart("postImages", new File("images/grape.jpeg"))
                .when()
                        .post("/post")
                .then()
                        .statusCode(201)
                        .extract()
                        .as(SavePostResponse.class);

        assertThat(response.getUserId()).isEqualTo(user1.getId());
        assertThat(response.getPostId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("test-content");
        assertThat(response.getPostImages().size()).isEqualTo(2);
        assertThat(response.getPostImages().get(0)).isEqualTo("apple.jpeg");
        assertThat(response.getPostImages().get(1)).isEqualTo("grape.jpeg");
        assertThat(response.getTaggedUserIds().size()).isEqualTo(2);
        assertThat(response.getTaggedUserIds().get(0)).isEqualTo(user2.getId());
        assertThat(response.getTaggedUserIds().get(1)).isEqualTo(user3.getId());

        // 테스트로 인해 저장된 파일 삭제
    }
}