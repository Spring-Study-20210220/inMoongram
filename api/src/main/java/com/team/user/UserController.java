package com.team.user;

import com.team.post.PostService;
import com.team.post.dto.input.FeedInput;
import com.team.post.dto.output.FeedOutput;
import com.team.post.dto.response.FeedResponse;
import com.team.security.jwt.JwtFilter;
import com.team.security.jwt.TokenProvider;
import com.team.user.dto.input.FollowerInfoListInput;
import com.team.user.dto.output.SignupOutput;
import com.team.user.dto.request.LoginRequest;
import com.team.user.dto.request.SignupRequest;
import com.team.user.dto.request.UserProfileModificationRequest;
import com.team.user.dto.response.FollowListResponse;
import com.team.user.dto.response.FollowerInfoListResponse;
import com.team.user.dto.response.LoginResponse;
import com.team.user.dto.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;
    private final PostService postService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupOutput output = userService.signup(request.toInput());
        // TODO 이메일 인증
        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(UserController.class).signup(request))
                .build();
        return ResponseEntity.created(uriComponents.toUri())
                .body(new SignupResponse(output.getUserId()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createAccessToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new LoginResponse(jwt), httpHeaders, HttpStatus.OK);
    }

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
