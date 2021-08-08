package com.team.event;

import lombok.Getter;

@Getter
public class SignupEvent {
    private String email;
    private String nickname;

    public SignupEvent(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
