package com.team.dbutil;

import com.team.post.Post;
import com.team.post.PostScrap;
import com.team.post.PostScrapRepository;
import com.team.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostScrapData {

    @Autowired
    private PostScrapRepository postScrapRepository;

    public PostScrap savePostScrap(User user, Post post) {
        PostScrap postScrap = new PostScrap(user, post);
        postScrapRepository.save(postScrap);
        return postScrap;
    }
}
