package com.team.dbutil;

import com.team.post.Post;
import com.team.post.PostRepository;
import com.team.user.User;

import org.springframework.stereotype.Component;

@Component
public class PostData {
    private final PostRepository postRepository;

    public PostData(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(User user) {
        Post post = new Post("content", user);
        postRepository.save(post);
        return post;
    }
}
