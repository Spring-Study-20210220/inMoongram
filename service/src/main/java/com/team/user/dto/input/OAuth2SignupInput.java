package com.team.user.dto.input;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2SignupInput {
    private String name;
    private String email;

    public OAuth2SignupInput(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public OAuth2SignupInput(Map<String, Object> attributes) {
        this.name = attributes.get("name").toString();
        this.email = attributes.get("email").toString();
    }
}
