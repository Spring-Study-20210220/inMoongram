package com.team.comment;

import com.team.post.Post;
import com.team.tag.CommentTaggedKeyword;
import com.team.tag.CommentTaggedUser;
import com.team.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @JoinColumn(name = "super_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment superComment;

    @OneToMany(mappedBy = "superComment", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> subComments = new ArrayList<>();

    @OneToMany(mappedBy = "comment", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<CommentTaggedKeyword> commentTaggedKeywords = new LinkedHashSet<>();

    @OneToMany(mappedBy = "comment", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<CommentTaggedUser> commentTaggedUsers = new LinkedHashSet<>();
  
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<CommentLike> commentLikes = new LinkedHashSet<>();

    @Builder
    public Comment(Long id, String content, User user, Post post, Comment superComment) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.post = post;
        this.superComment = superComment;
        if (superComment != null) {
            superComment.addSubComment(this);
        }
        post.addComment(this);
    }

    public void addAllTaggedKeywords(List<CommentTaggedKeyword> commentTaggedKeywords) {
        this.commentTaggedKeywords.addAll(commentTaggedKeywords);
    }

    public void addAllTaggedUsers(List<CommentTaggedUser> commentTaggedUsers) {
        this.commentTaggedUsers.addAll(commentTaggedUsers);
    }

    public void addSubComment(Comment comment) {
        subComments.add(comment);
    }

    public void deleteSubComment(Comment comment) {
        subComments.remove(comment);
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }
}
