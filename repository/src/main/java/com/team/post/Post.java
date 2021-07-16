package com.team.post;

import com.team.comment.Comment;
import com.team.tag.PostTaggedKeyword;
import com.team.tag.PostTaggedUser;
import com.team.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<PostImage> postImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<PostLike> postLikes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<PostTaggedUser> postTaggedUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<PostTaggedKeyword> postTaggedKeywords = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<Comment> comments = new LinkedHashSet<>();

    public Post(String content, User user) {
        this.content = content;
        setAuthor(user);
    }
    public void setIdForTest(Long id){
        this.id=id;
    }
    private void setAuthor(User user){
        this.user = user;
        user.getPosts().add(this);
    }

    public void addLike(PostLike postLike){
        this.postLikes.add(postLike);
    }

    public void addTaggedUsers(List<PostTaggedUser> postTaggedUsers) {
        this.postTaggedUsers.addAll(postTaggedUsers);
    }

    public void addTaggedKeywords(List<PostTaggedKeyword> postTaggedKeywords){
        this.postTaggedKeywords.addAll(postTaggedKeywords);
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void addImages(List<PostImage> postImages){
        this.postImages.addAll(postImages);
        postImages.forEach(
                it->it.setPost(this)
        );
    }

    public void delete() {
        this.user.getPosts().remove(this);
    }
}
