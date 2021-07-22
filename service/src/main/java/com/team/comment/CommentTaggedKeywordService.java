package com.team.comment;

import com.team.tag.CommentTaggedKeyword;
import com.team.tag.TagKeyword;
import com.team.tag.TagKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentTaggedKeywordService {

    private final TagKeywordService tagKeywordService;

    @Transactional
    public List<CommentTaggedKeyword> tagAllKeywords(Comment comment, List<String> tagKeywords) {
        List<TagKeyword> keywords = tagKeywordService.getTagKeywordByKeywords(tagKeywords);
        return keywords.stream()
                .map(it -> new CommentTaggedKeyword(comment, it))
                .collect(Collectors.toList());
    }
}
