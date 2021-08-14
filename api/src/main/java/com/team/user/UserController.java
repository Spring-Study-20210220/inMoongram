package com.team.user;

import com.team.post.PostService;
import com.team.post.dto.input.FeedInput;
import com.team.post.dto.output.FeedOutput;
import com.team.post.dto.response.FeedResponse;
import com.team.security.CurrentUser;
import com.team.user.dto.input.UserInfoInput;
import com.team.user.dto.output.UserInfoOutput;
import com.team.user.dto.request.UserProfileModificationRequest;
import com.team.user.dto.response.FollowListResponse;
import com.team.user.dto.response.FollowerInfoListResponse;
import com.team.user.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getInfo(@CurrentUser Long userId) {
        UserInfoOutput userInfo = userService.getUserInfo(new UserInfoInput(userId));

        return ResponseEntity.ok(
                new UserInfoResponse(userInfo)
        );
    }

    @GetMapping("/followings")
    public ResponseEntity<FollowListResponse> getFollowList(@CurrentUser Long userId) {
        return ResponseEntity.ok(
                new FollowListResponse(userService.getFollowList(userId)));
    }

    @GetMapping("/followers")
    public ResponseEntity<FollowerInfoListResponse> getFollowerList(@CurrentUser Long userId) {
        return ResponseEntity.ok(
                new FollowerInfoListResponse(
                        userService.getFollowerList(userId)
                )
        );
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void>
    profileModification(@CurrentUser Long userId, @Valid @RequestBody UserProfileModificationRequest reqDto) {
        userService.modifyUserProfile(userId, UserProfileModificationRequest.toServiceDto(reqDto));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedResponse> getFeed(@CurrentUser Long userId,
                                                @Valid @Positive @RequestParam("page-no") int page) {
        FeedOutput output = postService.getFeed(new FeedInput(userId, page));
        return ResponseEntity.ok(new FeedResponse(output));
    }
}
