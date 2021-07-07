package com.team.dbutil;

import com.team.post.Post;
import com.team.post.PostRepository;
import com.team.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostData {

    @Autowired
    private PostRepository postRepository;

    public Post savePost(String content, User user) {
        Post post = new Post(content, user);
        postRepository.save(post);
        return post;
    }
}
