package com.sam_solutions.app.handler;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles failed user authentication.
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger logger = Logger.getLogger(CustomAuthenticationFailureHandler.class);

    /**
     * Actual failed authentication handler.
     * @param request request while authentication.
     * @param response response.
     * @param exception was thrown to disable user authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                    AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof DisabledException) {
            logger.error("Access disabled");
            response.sendRedirect("/menow/403");
            return;
        }
        logger.info("Error while authentication");
        response.sendRedirect("/menow/login?error");
    }
}
