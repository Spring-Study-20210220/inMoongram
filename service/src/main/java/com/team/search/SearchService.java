package com.team.search;

import com.team.search.dto.PostQueryOutput;
import com.team.search.dto.PostSearchInfo;
import com.team.search.dto.SearchOutput;
import com.team.search.dto.UserSearchInfo;
import com.team.tag.PostTaggedKeywordRepository;
import com.team.tag.TagKeywordRepository;
import com.team.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final TagKeywordRepository tagKeywordRepository;
    private final PostTaggedKeywordRepository postTaggedKeywordRepository;
    private final SearchQueryRepository searchQueryRepository;

    @Transactional
    public SearchOutput search(String keyword) {
        if (keyword.startsWith("#")) {
            return new SearchOutput(searchPostTagKeyword(keyword), null);
        } else if (keyword.startsWith("@")) {
            return new SearchOutput(null, searchUser(keyword));
        } else {
            return new SearchOutput(searchPostTagKeyword(keyword), searchUser(keyword));
        }
    }

    public List<PostSearchInfo> searchPostTagKeyword(String keyword) {
        List<PostSearch> results = searchQueryRepository.findPostTaggedKeywordByKeyword(keyword);
        return results.stream()
                .map(PostSearchInfo::new)
                .collect(Collectors.toList());
    }

    public List<UserSearchInfo> searchUser(String keyword) {
        List<UserSearch> results = searchQueryRepository.findUsersByKeyword(keyword);
        return results.stream()
                .map(UserSearchInfo::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostQueryOutput postQuery(String keyword, Pageable pageable) {
        return new PostQueryOutput();
    }

}
