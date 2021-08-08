package com.team.post;

import com.team.exception.IdNotFoundException;
import com.team.post.dto.input.PostLikeCreateInput;
import com.team.post.dto.input.PostLikeInfoInput;
import com.team.post.dto.output.PostLikeCreateOutput;
import com.team.post.dto.output.PostLikeInfoOutput;
import com.team.user.User;
import com.team.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserService userService;

    public PostLikeCreateOutput createLike(PostLikeCreateInput input) {
        User author = userService.findUserById(input.getUserId());
        Post post = postRepository.findById(input.getPostId())
                .orElseThrow(() -> new IdNotFoundException("존재하지 않는 게시물입니다."));
        PostLike savedPostLike = postLikeRepository.save(
                PostLike.builder()
                        .post(post)
                        .user(author)
                        .build()
        );
        return new PostLikeCreateOutput(savedPostLike.getId());
    }

    public void cancelLike(Long postLikeId) {
        PostLike postLike = postLikeRepository.findById(postLikeId)
                .orElseThrow(() -> new IdNotFoundException("존재하지 않는 좋아요입니다."));
        postLikeRepository.delete(postLike);
    }

    public PostLikeInfoOutput getPostLikeList(PostLikeInfoInput inputDto) {
        Post post = postRepository.findById(inputDto.getPostId())
                .orElseThrow(() -> new IdNotFoundException("존재하지 않는 게시물입니다."));
        return new PostLikeInfoOutput(
                post.getPostLikes().stream()
                        .map(it -> PostLikeInfoOutput.PostLikeInfo.builder()
                                .postId(it.getPost().getId())
                                .userId(it.getUser().getId())
                                .postLikeId(it.getId())
                                .build()
                        ).collect(Collectors.toList())
        );
    }
}
