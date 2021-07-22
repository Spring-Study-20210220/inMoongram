package com.team.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagKeywordRepository extends JpaRepository<TagKeyword, Long> {
    Optional<TagKeyword> findByKeyword(String keyword);
}
