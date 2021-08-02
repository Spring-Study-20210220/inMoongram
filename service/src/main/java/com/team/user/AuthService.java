package com.team.user;

import com.team.event.SignupEvent;
import com.team.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void sendVerificationMail(SignupEvent event) {
        String VERIFICATION_LINK = "http://localhost:8080/verify/";
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), event.getNickname(), 60 * 20); // 20 minutes
        emailService.mailSend(event.getEmail(), "[inMoongram] 회원가입 인증메일입니다.", VERIFICATION_LINK + uuid.toString());
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
