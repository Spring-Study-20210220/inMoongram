package com.team.post;

import com.team.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public PostScrap(User user, Post post) {
        this.user = user;
        this.post = post;
        setPost(user);
    }

    private void setPost(User user) {
        user.getPostScraps().add(this);
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }
}
