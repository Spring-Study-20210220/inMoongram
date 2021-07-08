package com.team.post;

import com.team.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostTaggedUserService {
    private final PostTaggedUserRepository taggedUserRepository;

    public List<PostTaggedUser> tagAll(List<User> users, Post post) {
        return users.stream()
                .map(it -> taggedUserRepository.save(new PostTaggedUser(it, post)))
                .collect(Collectors.toList());
    }
}
