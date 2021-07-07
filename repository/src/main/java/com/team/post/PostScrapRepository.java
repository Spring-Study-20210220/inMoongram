package com.team.post;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {
    List<PostScrap> findAllByUserId(Long id);
}
