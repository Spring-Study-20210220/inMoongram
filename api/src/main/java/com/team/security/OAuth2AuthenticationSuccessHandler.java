package com.team.security;

import com.team.security.jwt.TokenProvider;
import com.team.user.dto.input.OAuth2SignupInput;
import com.team.util.CookieUtil;
import com.team.util.RedisUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
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

    private final OAuth2AuthorizedClientRepository clientRepository;

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    private final com.team.user.OAuth2UserService userService;

    public OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider,
                                              CookieUtil cookieUtil,
                                              RedisUtil redisUtil,
                                              OAuth2AuthorizedClientRepository clientRepository,
                                              OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService,
                                              com.team.user.OAuth2UserService userService) {
        this.tokenProvider = tokenProvider;
        this.cookieUtil = cookieUtil;
        this.redisUtil = redisUtil;
        this.clientRepository = clientRepository;
        this.oAuth2UserService = oAuth2UserService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User user = getOAuth2User(request, authentication);

        userService.oAuth2Signup(new OAuth2SignupInput(user.getAttributes()));
        String accessToken = tokenProvider.createAccessToken(user.getAttribute("email"), authentication.getName());
        long accessTokenExpireTimeInSeconds = TokenProvider.ACCESS_TOKEN_VALID_TIME / 1000;
        Cookie accessTokenCookie = cookieUtil.createCookie("accessToken", accessToken, accessTokenExpireTimeInSeconds);

        String refreshToken = tokenProvider.createRefreshToken(user.getAttribute("email"), authentication.getName());
        long refreshTokenExpireTimeInSeconds = TokenProvider.REFRESH_TOKEN_VALID_TIME / 1000;
        Cookie refreshTokenCookie = cookieUtil.createCookie("refreshToken", refreshToken, refreshTokenExpireTimeInSeconds);

        redisUtil.setDataExpire(refreshToken, user.getAttribute("email"), refreshTokenExpireTimeInSeconds);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.sendRedirect("http://localhost:3000");
    }

    private OAuth2User getOAuth2User(HttpServletRequest request, Authentication authentication) {
        OAuth2AuthorizedClient client = clientRepository.loadAuthorizedClient("facebook", authentication, request);
        OAuth2UserRequest userRequest = new OAuth2UserRequest(client.getClientRegistration(), client.getAccessToken());
        return oAuth2UserService.loadUser(userRequest);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private TokenProvider tokenProvider;

        private CookieUtil cookieUtil;

        private RedisUtil redisUtil;

        private OAuth2AuthorizedClientRepository clientRepository;

        private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

        private com.team.user.OAuth2UserService userService;

        Builder() {}
        public OAuth2AuthenticationSuccessHandler.Builder tokenProvider(TokenProvider tokenProvider) {
            this.tokenProvider = tokenProvider;
            return this;
        }

        public OAuth2AuthenticationSuccessHandler.Builder cookieUtil(CookieUtil cookieUtil) {
            this.cookieUtil = cookieUtil;
            return this;
        }

        public OAuth2AuthenticationSuccessHandler.Builder redisUtil(RedisUtil redisUtil) {
            this.redisUtil = redisUtil;
            return this;
        }

        public OAuth2AuthenticationSuccessHandler.Builder clientRepository(OAuth2AuthorizedClientRepository clientRepository) {
            this.clientRepository = clientRepository;
            return this;
        }

        public OAuth2AuthenticationSuccessHandler.Builder oAuth2UserService(OAuth2UserService userService) {
            this.oAuth2UserService = userService;
            return this;
        }

        public OAuth2AuthenticationSuccessHandler.Builder userService(com.team.user.OAuth2UserService userService) {
            this.userService = userService;
            return this;
        }

        public OAuth2AuthenticationSuccessHandler build() {
            return new OAuth2AuthenticationSuccessHandler(this.tokenProvider, this.cookieUtil, this.redisUtil, this.clientRepository, this.oAuth2UserService, this.userService);
        }
    }
}

