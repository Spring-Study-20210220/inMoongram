package com.team.security;

import com.team.security.jwt.TokenProvider;
import com.team.user.dto.input.OAuth2SignupInput;
import com.team.util.CookieUtil;
import com.team.util.RedisUtil;
import lombok.Builder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private final TokenProvider tokenProvider;

    private final CookieUtil cookieUtil;

    private final RedisUtil redisUtil;

    private final com.team.user.OAuth2UserService userService;

    @lombok.Builder
    public OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider,
                                              CookieUtil cookieUtil,
                                              RedisUtil redisUtil,
                                              com.team.user.OAuth2UserService userService) {
        this.tokenProvider = tokenProvider;
        this.cookieUtil = cookieUtil;
        this.redisUtil = redisUtil;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String email = user.getAttribute("email");
        if(!userService.findByEmail(email)) {
            userService.oAuth2Signup(new OAuth2SignupInput(user.getAttributes()));
        }
        else {
            logger.debug("이미 등록된 유저입니다");
        }

        String accessToken = tokenProvider.createAccessToken(email, authentication.getName());
        long accessTokenExpireTimeInSeconds = TokenProvider.ACCESS_TOKEN_VALID_TIME / 1000;
        Cookie accessTokenCookie = cookieUtil.createCookie("accessToken", accessToken, accessTokenExpireTimeInSeconds);

        String refreshToken = tokenProvider.createRefreshToken(email, authentication.getName());
        long refreshTokenExpireTimeInSeconds = TokenProvider.REFRESH_TOKEN_VALID_TIME / 1000;
        Cookie refreshTokenCookie = cookieUtil.createCookie("refreshToken", refreshToken, refreshTokenExpireTimeInSeconds);

        redisUtil.setDataExpire(refreshToken, email, refreshTokenExpireTimeInSeconds);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.sendRedirect("http://localhost:3000");
    }
}

