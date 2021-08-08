package com.team.user;

import com.team.post.Post;
import com.team.post.PostScrap;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String introduction;

    private Sex sex;

    private String website;

    @Column(name = "profile_image")
    private String profileImage;

    @ManyToOne(fetch = FetchType.LAZY)
    private Authority authority;

    // 사용자를 팔로우 하는 사람들
    @OneToMany(mappedBy = "followee")
    private Set<Follow> followers = new LinkedHashSet<>();

    // 사용자가 팔로우 하는 사람들
    @OneToMany(mappedBy = "follower")
    private Set<Follow> followees = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post){
        posts.add(post);
    }

    @OneToMany(mappedBy = "user")
    private Set<PostScrap> postScraps = new LinkedHashSet<>();


    @Builder
    public User(Long id, String email, String password, String nickname, String name,
                String phoneNumber, String introduction, Sex sex, String website, String profileImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.sex = sex;
        this.website = website;
        this.profileImage = profileImage;
    }

    public void modifyProfile(String email, String nickname, String name,
                              String phoneNumber, String introduction, Sex sex, String website, String profileImage){
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.sex = sex;
        this.website = website;
        this.profileImage = profileImage;
    }

    public void modifyRole(UserRole role) {
        this.authority.updateRole(role);
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }
}

