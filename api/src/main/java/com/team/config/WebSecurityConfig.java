package com.team.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.team.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.team.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.team.security.jwt.JwtAccessDeniedHandler;
import com.team.security.jwt.JwtAuthenticationEntryPoint;
import com.team.security.jwt.TokenProvider;
import com.team.user.OAuthUserService;
import com.team.util.CookieUtil;
import com.team.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.savedrequest.CookieRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ObjectMapper objectMapper;
    private final OAuthUserService userService;

    private static final String[] permitAllUrls = new String[]{
            "/oauth2/login", "/login", "/signup", "/verify/**"
    };
    private static final String[] permitGetUrls = new String[]{
            "/comments", "/post/{\\d+}/likes", "/search", "/user/followings", "/user/followers"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeRequests(authorize -> authorize
                        .antMatchers(permitAllUrls).permitAll()
                        .antMatchers(HttpMethod.GET, permitGetUrls).permitAll()
                        .anyRequest().authenticated()
                )
                .apply(new JwtSecurityConfig(tokenProvider, redisUtil, cookieUtil))
                .and()
                .oauth2Login(login -> login
                        .authorizationEndpoint(endPoint -> endPoint
                                .authorizationRequestRepository(customRequest())
                        )
                        .successHandler(successHandler())
                        .failureHandler(failureHandler())
                )
                .requestCache(RequestCacheConfigurer::disable);

    }

    @Bean
    public OAuth2AuthenticationSuccessHandler successHandler() {
        return OAuth2AuthenticationSuccessHandler.builder()
                .cookieUtil(cookieUtil)
                .redisUtil(redisUtil)
                .tokenProvider(tokenProvider)
                .userService(userService)
                .build();
    }

    @Bean
    public OAuth2AuthenticationFailureHandler failureHandler() {
        return new OAuth2AuthenticationFailureHandler();
    }

    @Bean
    public RequestCache cookieRequestCache() {
        return new CookieRequestCache();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> customRequest() {
        return new HttpCookieOAuth2AuthorizationRequestRepository(objectMapper, cookieUtil);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
