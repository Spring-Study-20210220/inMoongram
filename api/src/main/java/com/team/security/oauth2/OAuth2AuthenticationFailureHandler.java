package com.team.security.oauth2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    protected final Log logger = LogFactory.getLog(getClass());

    private String defaultFailureUrl;

    private boolean forwardToDestination = false;

    private boolean allowSessionCreation = true;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Value("${redirect.fail}")
    private String FAIL_REDIRECT_URL;

    public OAuth2AuthenticationFailureHandler() {
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (this.defaultFailureUrl == null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Sending 401 Unauthorized error since no failure URL is set");
            } else {
                this.logger.debug("Sending 401 Unauthorized error");
            }
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }
        saveException(request, exception);
        if (this.forwardToDestination) {
            this.logger.debug("Forwarding to " + this.defaultFailureUrl);
            request.getRequestDispatcher(this.defaultFailureUrl).forward(request, response);
        } else {
            this.redirectStrategy.sendRedirect(request, response, FAIL_REDIRECT_URL);
        }
    }

    protected final void saveException(HttpServletRequest request, AuthenticationException exception) {
        if (this.forwardToDestination) {
            request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
            return;
        }
        HttpSession session = request.getSession(false);
        if (session != null || this.allowSessionCreation) {
            request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
        }
    }
}
