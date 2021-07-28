package com.team.user.dto.input;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupInput {
    private String email;
    private String nickname;
    private String name;
    private String password;

    @Builder
    public SignupInput(String email, String nickname, String name, String password) {
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.password = password;
    }
}
