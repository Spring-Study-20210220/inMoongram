package com.team.user;

import com.team.security.jwt.TokenProvider;
import com.team.user.dto.output.SignupOutput;
import com.team.user.dto.request.LoginRequest;
import com.team.user.dto.request.SignupRequest;
import com.team.user.dto.response.LoginResponse;
import com.team.user.dto.response.SignupResponse;
import com.team.util.CookieUtil;
import com.team.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.Arrays;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupOutput output = userService.signup(request.toInput());
        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(AuthController.class).signup(request))
                .build();
        return ResponseEntity.created(uriComponents.toUri())
                .body(new SignupResponse(output.getUserId()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletResponse httpResponse) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(authentication);
        long accessTokenExpireTimeInSeconds = TokenProvider.ACCESS_TOKEN_VALID_TIME / 1000;
        Cookie accessTokenCookie = cookieUtil.createCookie("accessToken", accessToken, accessTokenExpireTimeInSeconds);

        String refreshToken = tokenProvider.createRefreshToken(authentication);
        long refreshTokenExpireTimeInSeconds = TokenProvider.REFRESH_TOKEN_VALID_TIME / 1000;
        Cookie refreshTokenCookie = cookieUtil.createCookie("refreshToken", refreshToken, refreshTokenExpireTimeInSeconds);

        redisUtil.setDataExpire(refreshToken, request.getEmail(), refreshTokenExpireTimeInSeconds);

        httpResponse.addCookie(accessTokenCookie);
        httpResponse.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new LoginResponse(accessToken));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Arrays.stream(httpRequest.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst()
                .ifPresent(redisUtil::deleteData);

        expireTokenCookie(httpResponse, "accessToken");
        expireTokenCookie(httpResponse, "refreshToken");

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/verify/{key}")
    public ResponseEntity<String> verifyEmail(@PathVariable("key") String key) {
        try {
            authService.verifyEmail(key);
            return ResponseEntity.ok("이메일을 성공적으로 인증했습니다.");
        } catch(Exception e) {
            return ResponseEntity.badRequest()
                    .body("이메일 인증에 실패했습니다.");
        }
    }

    private void expireTokenCookie(HttpServletResponse httpResponse, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        httpResponse.addCookie(cookie);
    }
}
