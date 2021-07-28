package com.team.user;

import com.team.post.PostService;
import com.team.post.dto.input.FeedInput;
import com.team.post.dto.output.FeedOutput;
import com.team.post.dto.response.FeedResponse;
import com.team.user.dto.input.FollowerInfoListInput;
import com.team.user.dto.request.UserProfileModificationRequest;
import com.team.user.dto.response.FollowListResponse;
import com.team.user.dto.response.FollowerInfoListResponse;
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
    private final FollowService followService;
    private final PostService postService;

    @GetMapping("/{user-id}/followings")
    public ResponseEntity<FollowListResponse> getFollowList(@PathVariable(name="user-id") Long userId) {
        return ResponseEntity.ok(
                new FollowListResponse(userService.getFollowList(userId)));
    }

    @GetMapping("/{user-id}/followers")
    public ResponseEntity<FollowerInfoListResponse> getFollowerList(@PathVariable(name="user-id") Long userId) {
        return ResponseEntity.ok(
                new FollowerInfoListResponse(
                        userService.getFollowerList(new FollowerInfoListInput(userId))
                )
        );
    }

    @PatchMapping("/{id}/profile")
    public ResponseEntity<Void>
    profileModification(@PathVariable("id") Long userId, @Valid @RequestBody UserProfileModificationRequest reqDto) {
        userService.modifyUserProfile(userId, UserProfileModificationRequest.toServiceDto(reqDto));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{user-id}/feed")
    public ResponseEntity<FeedResponse> getFeed(@PathVariable("user-id") Long userId,
                                                @Valid @Positive @RequestParam("page-no") int page) {
        FeedOutput feedOutput = getFeedOutput(userId, page);
        return ResponseEntity.ok(new FeedResponse(feedOutput));
    }

    private FeedOutput getFeedOutput(Long userId, int page) {
        FeedInput feedInput = FeedInput.builder()
                .userId(userId)
                .page(page)
                .build();
        return postService.getFeed(feedInput);
    }
}
