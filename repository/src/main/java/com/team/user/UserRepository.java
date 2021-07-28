package com.team.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    @EntityGraph(attributePaths = {"followees.followee"})
    @Query("select u from User u where u.id = :id")
    Optional<User> findFollowingUserById(@Param("id") Long id);
}
