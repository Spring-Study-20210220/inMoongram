package com.team.user.dto.response;

import com.team.user.Sex;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponse {
    private String email;

    private String password;

    private String nickname;

    private String name;

    private String phoneNumber;

    private String introduction;

    private Sex sex;

    private String website;

    public UserInfoResponse(String email, String password, String nickname, String name, String phoneNumber, String introduction, Sex sex, String website) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.sex = sex;
        this.website = website;
    }
}
