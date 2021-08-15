package com.team.post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @EntityGraph(attributePaths = {"comments", "postTaggedUsers", "postTaggedKeywords", "postLikes", "postImages", "user"})
    @Query("select p from Post p where p.id = :postId")
    Optional<Post> getPostById(@Param("postId")Long postId);
}
