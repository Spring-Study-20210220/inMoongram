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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupOutput output = userService.signup(request.toInput());
        // TODO 이메일 인증
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
    public ResponseEntity<Void> logout(HttpServletResponse httpResponse) {
        expireTokenCookie(httpResponse, "accessToken");
        expireTokenCookie(httpResponse, "refreshToken");
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    private void expireTokenCookie(HttpServletResponse httpResponse, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        httpResponse.addCookie(cookie);
    }
}
