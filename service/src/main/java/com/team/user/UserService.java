package com.team.user;

import com.team.event.SignupEvent;
import com.team.exception.IdNotFoundException;
import com.team.user.dto.input.*;
import com.team.user.dto.input.UserProfileModificationInput;
import com.team.user.dto.output.FollowListOutput;
import com.team.user.dto.output.FollowerInfoListOutput;
import com.team.user.dto.output.SignupOutput;
import com.team.user.dto.output.UserInfoOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
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

        return new SignupOutput(saveUser);
    }

    @Transactional
    public UserInfoOutput getUserInfo(UserInfoInput input) {
        User user = userRepository.findById(input.getUserId())
                .orElseThrow(IdNotFoundException::new);
        return new UserInfoOutput(user);
    }

    @Transactional
    public void modifyUserProfile(Long userId, UserProfileModificationInput payload) {
        User user = findUserById(userId);
        user.modifyProfile(payload.getEmail(), payload.getNickname(), payload.getName(),
                payload.getPhoneNumber(), payload.getIntroduction(), payload.getSex(),
                payload.getWebsite(), payload.getProfileImage()
        );
    }

    @Transactional
    public FollowerInfoListOutput getFollowerList(Long userId) {
        List<Follower> followers = userRepository.findFollowerUserById(userId);

        return new FollowerInfoListOutput(followers, userId);
    }

    @Transactional(readOnly = true)
    public FollowListOutput getFollowList(Long userId) {
        User user = userRepository.findFollowingUserById(userId).orElseThrow(
                IdNotFoundException::new
        );
        Set<Follow> followees = user.getFollowees();

        List<FollowListOutput.UserInfo> userInfos = followees.stream()
                .map(follow -> FollowListOutput.UserInfo.builder()
                        .userId(follow.getFollowee().getId())
                        .name(follow.getFollowee().getName())
                        .nickname(follow.getFollowee().getNickname())
                        .profileImage(follow.getFollowee().getProfileImage())
                        .followId(follow.getId())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));

//        TODO: add hashtag info
        return new FollowListOutput(userInfos, null);
    }

    @Transactional
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(IdNotFoundException::new);
    }

    @Transactional
    public List<User> findUsersByIds(List<Long> userIds) {
        return userIds.stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    @Transactional
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }
}
