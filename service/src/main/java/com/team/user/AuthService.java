package com.team.user;

import com.team.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public void sendVerificationMail(String email, String nickname) {
        String VERIFICATION_LINK = "http://localhost:8080/verify/";
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), nickname, 60 * 20); // 20 minutes
        emailService.mailSend(email, "[inMoongram] 회원가입 인증메일입니다.", VERIFICATION_LINK + uuid.toString());
    }

    public Boolean verifyEmail(String key) {
        String savedNickname = redisUtil.getData(key);
        if (userRepository.findByNickname(savedNickname).orElse(null) == null) {
            return false;
        }
        redisUtil.deleteData(key);
        return true;
    }

}
