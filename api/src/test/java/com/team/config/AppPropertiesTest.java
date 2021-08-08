package com.team.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = {"dev"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppPropertiesTest {
    @Autowired
    private AppProperties appProperties;

    @Test
    void file_dir확인() {
        String dir = appProperties.getFile().getDir();
        assertThat(dir).isEqualTo("src/test/resources/images");
    }
}
