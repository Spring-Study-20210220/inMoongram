package com.team.search;

import com.team.authUtil.TestAuthProvider;
import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.PostData;
import com.team.dbutil.UserData;
import com.team.post.dto.request.SavePostRequest;
import com.team.search.dto.response.SearchResponse;
import com.team.user.User;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = {"dev"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchAcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private TestAuthProvider testAuthProvider;

    @Autowired
    private PostData postData;

    @Autowired
    private UserData userData;
    private User user1;
    private User user2;
    private User user3;

    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    @BeforeEach
    void setup() throws IOException {
        user1 = userData.saveUser("junsu", "springUser", "test1@test.com");
        user2 = userData.saveUser("yongwoo", "springExpert", "test2@test.com");
        user3 = userData.saveUser("seounghwa", "SpringMasterUser", "test3@test.com");

        createPost(user2, List.of("아이유", "짭이유", "헤이즈"));
        createPost(user3, List.of("아이유", "spring", "ed sherran"));
    }

    public void createPost(User user, List<String> keywordList) throws IOException {
        String path = "src/test/resources/images";
        String absolutePath = new File(path).getAbsolutePath();
        MultipartFile postImage1 = new MockMultipartFile("apple.jpeg", new FileInputStream(absolutePath + "/apple.jpeg"));
        MultipartFile postImage2 = new MockMultipartFile("grape.jpeg", new FileInputStream(absolutePath + "/grape.jpeg"));
        SavePostRequest request = SavePostRequest.builder()
                .postImages(Arrays.asList(postImage1, postImage2))
                .content("test-content")
                .taggedKeywords(keywordList)
                .build();

        Cookie cookie = testAuthProvider.getAccessTokenCookie(user);

        given()
                .port(port)
                .cookie(cookie)
                .accept(ContentType.JSON)
                .multiPart("content", request.getContent())
                .multiPart("taggedKeywords", request.getTaggedKeywords())
                .multiPart("postImages", new File(absolutePath + "/apple.jpeg"))
                .multiPart("postImages", new File(absolutePath + "/grape.jpeg"))
                .when()
                .post("/post")
                .then()
                .statusCode(201);
    }

    @Test
    void 검색() {
        String keyword = "spring";
        Cookie cookie = testAuthProvider.getAccessTokenCookie(user1);
        var response = given()
                .cookie(cookie)
                .param("keyword", keyword)
                .port(port)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/search")
                .thenReturn();

        assertThat(response.getStatusCode()).isEqualTo(200);

        var searchResponse = response.as(SearchResponse.class);

        assertThat(searchResponse.getPostSearchInfos().size()).isEqualTo(1);
        assertThat(searchResponse.getUserSearchInfos())
                .extracting("nickName")
                .containsAll(List.of(user1.getNickname(), user2.getNickname()));
    }
}
