package com.team.user;

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

    // 사용자를 팔로우 하는 사람들
    @OneToMany(mappedBy = "followee")
    private Set<Follow> followers = new LinkedHashSet<>();

    // 사용자가 팔로우 하는 사람들
    @OneToMany(mappedBy = "follower")
    private Set<Follow> followees = new LinkedHashSet<>();

    @Builder
    public User(String email, String password, String nickname, String name,
                String phoneNumber, String introduction, Sex sex, String website, String profileImage) {
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
}

