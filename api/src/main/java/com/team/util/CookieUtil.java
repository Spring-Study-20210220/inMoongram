package com.team.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class CookieUtil {
    public Cookie createCookie(String cookieName, String accessToken, long maxAge) {
        Cookie token = new Cookie(cookieName, accessToken);
        token.setHttpOnly(true);
        token.setSecure(true);
        token.setMaxAge((int) maxAge);
        return token;
    }

    public Cookie getCookie(HttpServletRequest httpRequest, String cookieName) {
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }
}
