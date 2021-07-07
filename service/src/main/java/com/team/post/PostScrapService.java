package com.team.post;

import com.team.exception.IdNotFoundException;
import com.team.post.dto.input.PostScrapInput;
import com.team.post.dto.output.PostScrapInfoOutput;
import com.team.user.User;
import com.team.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostScrapService {
    private final PostScrapRepository postScrapRepository;
    private final PostRepository postRepository; //나중에 PostService로 교체해도 된다
    private final UserService userService;

    public PostScrapInfoOutput postScrap(PostScrapInput postScrapInput) {
        Post post = postRepository.findById(postScrapInput.getPostId())
                .orElseThrow(IdNotFoundException::new);
        User user = userService.findUserById(postScrapInput.getUserId());
        PostScrap saved = postScrapRepository.save(new PostScrap(user, post));
        return new PostScrapInfoOutput(saved.getId());
    }
}
