package com.team.user;

import com.team.security.CurrentUser;
import com.team.user.dto.output.FollowInfoOutput;
import com.team.user.dto.request.FollowRequest;
import com.team.user.dto.response.FollowInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @DeleteMapping("/{follow_id}/unfollow")
    public ResponseEntity<Void> unfollow(@PathVariable("follow_id") Long followId) {
        followService.unfollow(followId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("")
    public ResponseEntity<FollowInfoResponse> follow(@CurrentUser Long userId,
                                                     @Valid @RequestBody FollowRequest request) {
        FollowInfoOutput output = followService.follow(userId, request.toInput());

        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(FollowController.class)
                        .follow(userId, request))
                .build();
        return ResponseEntity
                .created(uriComponents.toUri())
                .body(new FollowInfoResponse(output));
    }
}
