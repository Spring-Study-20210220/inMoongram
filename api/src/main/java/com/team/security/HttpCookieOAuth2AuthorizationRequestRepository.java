package com.team.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.util.CookieUtil;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final String cookieAttributeName = "OAUTH2REQUEST";

    private final ObjectMapper mapper;

    private final CookieUtil cookieUtil;

    private final Base64.Encoder encoder;

    private final Base64.Decoder decoder;

    public HttpCookieOAuth2AuthorizationRequestRepository(ObjectMapper objectMapper, CookieUtil cookieUtil) {
        this.mapper = objectMapper;
        this.cookieUtil = cookieUtil;
        this.encoder = Base64.getEncoder();
        this.decoder = Base64.getDecoder();
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");
        String stateParameter = this.getStateParameter(request);
        if (stateParameter == null) {
            return null;
        }
        Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
        return authorizationRequests.get(stateParameter);
    }

    @SneakyThrows
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
                                         HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        if (authorizationRequest == null) {
            this.removeAuthorizationRequest(request, response);
            return;
        }
        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");
        byte[] serialize = SerializationUtils.serialize(authorizationRequest);
        String encoded = encoder.encodeToString(serialize);
        Cookie cookie = cookieUtil.createCookie(this.cookieAttributeName, encoded, 10000);
        response.addCookie(cookie);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");
        return null;
    }

    @SneakyThrows
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        String stateParameter = this.getStateParameter(request);
        if (stateParameter == null) {
            return null;
        }
        Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
        OAuth2AuthorizationRequest originalRequest = authorizationRequests.remove(stateParameter);
        if (authorizationRequests.size() == 0) {
            Cookie cookie = cookieUtil.createCookie(this.cookieAttributeName, null, 0);
            response.addCookie(cookie);
        }
        else if (authorizationRequests.size() == 1) {
            String value = mapper.writeValueAsString(authorizationRequests.values().iterator().next());
            byte[] bytes = value.getBytes();
            byte[] encoded = encoder.encode(bytes);
            Cookie cookie = cookieUtil.createCookie(this.cookieAttributeName, new String(encoded), 10000);
            response.addCookie(cookie);
        }
        else {
            String value = mapper.writeValueAsString(authorizationRequests);
            byte[] bytes = value.getBytes();
            byte[] encoded = encoder.encode(bytes);
            Cookie cookie = cookieUtil.createCookie(this.cookieAttributeName, new String(encoded), 10000);
            response.addCookie(cookie);
        }
        return originalRequest;
    }

    private String getStateParameter(HttpServletRequest request) {
        return request.getParameter(OAuth2ParameterNames.STATE);
    }

    private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
        Cookie requestCookie = null;
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(this.cookieAttributeName)) {
                requestCookie = cookie;
                break;
            }
        }
        String cookieValue = (requestCookie != null) ? requestCookie.getValue() : null;
        if(cookieValue == null) {
            return new HashMap<>();
        }

        byte[] decode = decoder.decode(cookieValue);
        try {
            OAuth2AuthorizationRequest auth2AuthorizationRequest = (OAuth2AuthorizationRequest) SerializationUtils.deserialize(decode);
            Map<String, OAuth2AuthorizationRequest> authorizationRequests = new HashMap<>(1);
            authorizationRequests.put(auth2AuthorizationRequest.getState(), auth2AuthorizationRequest);
            return authorizationRequests;
        }
        catch (Exception ex) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, OAuth2AuthorizationRequest> authorizationRequests = (Map<String, OAuth2AuthorizationRequest>) SerializationUtils.deserialize(decode);
                return authorizationRequests;
            }
            catch (Exception mapEx) {
                throw new IllegalStateException(
                        "authorizationRequests is supposed to be a Map or OAuth2AuthorizationRequest but actually is a "
                                + cookieValue.getClass());
            }
        }
    }
}
