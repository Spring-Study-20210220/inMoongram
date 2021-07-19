package com.team.user;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {

    List<Follower> findFollowerUserById(Long id);
}
