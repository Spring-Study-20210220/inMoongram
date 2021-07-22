package com.team.tag;

import com.team.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTaggedKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_keyword_id")
    private TagKeyword tagKeyword;

    public PostTaggedKeyword(TagKeyword tagKeyword, Post post) {
        this.post = post;
        this.tagKeyword = tagKeyword;
    }
}
