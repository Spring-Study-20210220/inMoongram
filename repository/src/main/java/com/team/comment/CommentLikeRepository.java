package com.team.comment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @EntityGraph(attributePaths = {"comment.commentLikes"})
    @Query("select c from CommentLike c where c.id = :id")
    Optional<CommentLike> findCommentLikeById(@Param("id") Long id);
}
