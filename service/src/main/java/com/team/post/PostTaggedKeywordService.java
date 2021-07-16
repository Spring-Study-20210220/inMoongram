package com.team.post;

import com.team.tag.PostTaggedKeyword;
import com.team.tag.PostTaggedKeywordRepository;
import com.team.tag.TagKeyword;
import com.team.tag.TagKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostTaggedKeywordService {
    private final TagKeywordRepository tagKeywordRepository;
    private final PostTaggedKeywordRepository postTaggedKeywordRepository;

    public List<PostTaggedKeyword> tagAll(List<String> keywords, Post post) {
        if (keywords == null) {
            return null;
        }

        List<PostTaggedKeyword> postTaggedKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            TagKeyword tagKeyword = getTagKeyword(keyword); // 키워드가 존재한다면 오브젝트 반환, 존재하지 않으면 null 반환
            if (tagKeyword == null) {
                tagKeyword = tagKeywordRepository.save(new TagKeyword(keyword)); // 키워드로 등록
            }
            PostTaggedKeyword postTaggedKeyword = postTaggedKeywordRepository.save(new PostTaggedKeyword(tagKeyword, post));
            postTaggedKeywords.add(postTaggedKeyword);
        }
        return postTaggedKeywords;
    }

    private TagKeyword getTagKeyword(String keyword) {
        return tagKeywordRepository.findByKeyword(keyword)
                .orElse(null);
    }
}
