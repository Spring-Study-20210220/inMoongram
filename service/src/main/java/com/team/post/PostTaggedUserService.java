package com.team.post;

import com.team.tag.PostTaggedUser;
import com.team.tag.PostTaggedUserRepository;
import com.team.user.User;
import com.team.user.UserService;
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
    private final UserService userService;

    public List<PostTaggedUser> tagAll(List<User> users, Post post) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(it -> taggedUserRepository.save(new PostTaggedUser(it, post)))
                .collect(Collectors.toList());
    }
}
