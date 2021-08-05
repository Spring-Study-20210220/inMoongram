package com.team.user.dto.output;

import com.team.user.Sex;
import com.team.user.User;
import lombok.Data;

@Data
public class UserInfoOutput {
    private String email;

    private String password;

    private String nickname;

    private String name;

    private String phoneNumber;

    private String introduction;

    private Sex sex;

    private String website;

    public UserInfoOutput(String email, String password, String nickname, String name, String phoneNumber, String introduction, Sex sex, String website) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.sex = sex;
        this.website = website;
    }

    public UserInfoOutput(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.introduction = user.getIntroduction();
        this.sex = user.getSex();
        this.website = user.getWebsite();
    }
}
