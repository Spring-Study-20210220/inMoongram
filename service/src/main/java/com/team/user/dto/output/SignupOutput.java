package com.team.user.dto.output;

import com.team.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupOutput {
    private Long userId;

    public SignupOutput(Long userId) {
        this.userId = userId;
    }

    public SignupOutput(User user) {
        this.userId = user.getId();
    }
}
