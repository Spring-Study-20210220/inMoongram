package com.team.user;

import com.team.user.dto.input.OAuth2SignupInput;
import com.team.user.dto.output.SignupOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {

    private final UserRepository userRepository;
    @Transactional
    public SignupOutput oAuth2Signup(OAuth2SignupInput input) {
        User user = User.builder()
                .email(input.getEmail())
                .name(input.getName())
                .nickname(input.getName())
                .build();
        User saved = userRepository.save(user);
        return new SignupOutput(saved);
    }
}
