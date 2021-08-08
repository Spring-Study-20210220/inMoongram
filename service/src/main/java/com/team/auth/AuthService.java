package com.team.auth;

import com.team.event.SignupEvent;
import com.team.user.User;
import com.team.user.UserRepository;
import com.team.user.UserRole;
import com.team.user.UserService;
import com.team.user.dto.input.SignupInput;
import com.team.user.dto.output.SignupOutput;
import com.team.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ApplicationEventPublisher publisher;
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupOutput signup(SignupInput input) {
        if (userRepository.findByEmail(input.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 사용된 이메일 주소입니다.");
        }
        if (userRepository.findByNickname(input.getNickname()).orElse(null) != null) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        User saveUser = userRepository.save(
                User.builder()
                        .email(input.getEmail())
                        .nickname(input.getNickname())
                        .name(input.getName())
                        .password(passwordEncoder.encode(input.getPassword()))
                        .build()
        );
        publisher.publishEvent(new SignupEvent(saveUser.getEmail(), saveUser.getNickname()));
        return new SignupOutput(saveUser);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    @Transactional
    public void sendVerificationMail(SignupEvent event) {
        String VERIFICATION_LINK = "http://localhost:8080/verify/";
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), event.getNickname(), 60 * 20); // 20 minutes
        emailService.mailSend(event.getEmail(), "[inMoongram] 회원가입 인증메일입니다.", VERIFICATION_LINK + uuid);
    }

    @Transactional
    public void verifyEmail(String key) {
        String savedNickname = redisUtil.getData(key);
        User user = userService.findByNickname(savedNickname);
        user.modifyRole(UserRole.ROLE_USER);
        redisUtil.deleteData(key);
    }

}
