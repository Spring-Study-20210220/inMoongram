package com.team.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupResponse {
    private Long userId;

    public SignupResponse(Long userId) {
        this.userId = userId;
    }
}
