package com.team.user.dto.input;

import lombok.Data;

@Data
public class UserInfoInput {
    private Long userId;

    public UserInfoInput(Long userId) {
        this.userId = userId;
    }
}
