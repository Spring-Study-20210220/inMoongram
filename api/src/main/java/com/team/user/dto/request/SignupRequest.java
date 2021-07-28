package com.team.user.dto.request;

import com.team.user.dto.input.SignupInput;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {
    @NotEmpty @Email
    private String email;

    @NotEmpty
    private String nickname;

    private String name;

    @NotEmpty
    @Pattern(regexp="(?=.*[a-z])(?=.*[A-Z]).{8,20}")
    private String password;

    @Builder
    public SignupRequest(String email, String nickname, String name, String password) {
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.password = password;
    }

    public SignupInput toInput() {
        return SignupInput.builder()
                .email(email)
                .nickname(nickname)
                .name(name)
                .password(password)
                .build();
    }
}
