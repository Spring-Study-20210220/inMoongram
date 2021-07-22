package com.team.comment;

import com.team.tag.CommentTaggedUser;
import com.team.user.User;
import com.team.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentTaggedUserService {
    private final UserService userService;

    @Transactional
    public List<CommentTaggedUser> tagAllUsers(Comment comment, List<Long> userIds) {
        List<User> users = userService.findUsersByIds(userIds);
        return users.stream()
                .map(it -> new CommentTaggedUser(it, comment))
                .collect(Collectors.toList());
    }
}
