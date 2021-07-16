package com.team.post;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getFeed(Long userId, Pageable pageable);
}
