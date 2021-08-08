package com.team.search;

import com.team.config.TestConfig;
import com.team.post.Post;
import com.team.post.PostRepository;
import com.team.tag.PostTaggedKeyword;
import com.team.tag.PostTaggedKeywordRepository;
import com.team.tag.TagKeyword;
import com.team.tag.TagKeywordRepository;
import com.team.user.User;
import com.team.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestConfig.class, SearchQueryRepository.class})
class SearchQueryRepositoryTest {
    @Autowired
    private SearchQueryRepository searchQueryRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagKeywordRepository tagKeywordRepository;
    @Autowired
    private PostTaggedKeywordRepository postTaggedKeywordRepository;

    public User createUser(String name, String nickName, String introduction) {
        return userRepository.save(
                User.builder()
                        .email("test@email.com")
                        .password("testPw")
                        .name(name)
                        .nickname(nickName)
                        .introduction(introduction)
                        .build()
        );
    }

    public Post createPost(User author, List<String> taggedKewords) {
        Post post = postRepository.save(
                new Post("testContent", author)
        );
        List<TagKeyword> tagKeywords = taggedKewords.stream()
                .map(it -> tagKeywordRepository.save(new TagKeyword(it)))
                .collect(Collectors.toList());
        List<PostTaggedKeyword> postTaggedKeywords = tagKeywords.stream()
                .map(it -> postTaggedKeywordRepository.save(new PostTaggedKeyword(it, post)))
                .collect(Collectors.toList());
        post.addTaggedKeywords(postTaggedKeywords);
        return post;
    }

    @Test
    void 유저키워드검색() {
        User user1 = createUser("junsu", "springRunner", "i'm codeMonkey");
        User user2 = createUser("yongwoo", "springSprinter", "i'm ironman");
        User user3 = createUser("seounghwa", "springMaster", "i'm king of world");

        var result = searchQueryRepository.findUsersByKeyword("spring");
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void 태그키워드포스트검색() {
        User user = createUser("junsu", "springRunner", "i'm codeMonkey");

        Post post1 = createPost(user, List.of("spring", "springSecurity", "springWebflux"));
        Post post2 = createPost(user, List.of("apple", "pineapple", "springapple"));

        var result = searchQueryRepository.findPostTaggedKeywordByKeyword("apple");
        assertThat(result.size()).isEqualTo(3);
    }
}
