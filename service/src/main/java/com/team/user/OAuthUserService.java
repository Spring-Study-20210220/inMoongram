package com.team.user;

import com.team.user.dto.input.OAuth2SignupInput;
import com.team.user.dto.output.SignupOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthUserService {

    private final UserRepository userRepository;

    @Transactional
    public SignupOutput oAuth2Signup(OAuth2SignupInput input) {
        User user = User.builder()
                .email(input.getEmail())
                .name(input.getName())
                .nickname(UUID.randomUUID().toString())
                .build();
        User saved = userRepository.save(user);
        return new SignupOutput(saved);
    }

    @Transactional
    public boolean findByEmail(String email) {
        return null != userRepository.findByEmail(email)
                .orElse(null);
    }
}
