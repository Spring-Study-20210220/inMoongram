package com.team.post;

import com.team.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<PostImage> postImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostLike> postLikes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostTaggedUser> postTaggedUsers = new LinkedHashSet<>();

    public Post(String content, User user) {
        this.content = content;
        setAuthor(user);
    }

    private void setAuthor(User user){
        this.user = user;
        user.getPosts().add(this);
    }

    public void addLike(PostLike postLike){
        this.postLikes.add(postLike);
    }

    public void addTaggedUsers(List<PostTaggedUser> postTaggedUsers){
        this.postTaggedUsers.addAll(postTaggedUsers);
        postTaggedUsers.forEach(
                it->it.setPost(this)
        );
    }

    public void addImages(List<PostImage> postImages){
        this.postImages.addAll(postImages);
        postImages.forEach(
                it->it.setPost(this)
        );
    }
}
