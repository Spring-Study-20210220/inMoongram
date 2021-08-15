package com.team.user.dto.response;

import com.team.user.Sex;
import com.team.user.dto.output.UserInfoOutput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponse {
    private String email;

    private String nickname;

    private String name;

    private String phoneNumber;

    private String introduction;

    private Sex sex;

    private String website;

    public UserInfoResponse(String email, String nickname, String name, String phoneNumber, String introduction, Sex sex, String website) {
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.sex = sex;
        this.website = website;
    }

    public UserInfoResponse(UserInfoOutput output) {
        this.email = output.getEmail();
        this.nickname = output.getNickname();
        this.name = output.getName();
        this.phoneNumber = output.getPhoneNumber();
        this.introduction = output.getIntroduction();
        this.sex = output.getSex();
        this.website = output.getWebsite();
    }
}
