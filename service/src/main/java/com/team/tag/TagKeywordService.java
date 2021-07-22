package com.team.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TagKeywordService {
    private final TagKeywordRepository tagKeywordRepository;

    public List<TagKeyword> getTagKeywordByKeywords(List<String> keywords) {
        return keywords.stream()
                .map(this::getTagKeywordByKeyword)
                .collect(Collectors.toList());
    }

    public TagKeyword getTagKeywordByKeyword(String keyword) {
        return tagKeywordRepository.findByKeyword(keyword)
                .orElseGet(
                        () -> save(keyword)
                );
    }

    public TagKeyword save(String keyword) {
        return tagKeywordRepository.save(
                TagKeyword.builder()
                        .keyword(keyword)
                        .build()
        );
    }
}
