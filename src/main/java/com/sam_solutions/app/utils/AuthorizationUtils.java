package com.sam_solutions.app.utils;

import com.sam_solutions.app.dto.UserSignUpDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Manages authorization stuff.
 */
public class AuthorizationUtils {
    /**
     * Returns login as username of current user.
     * @return current user login.
     */
    public String getCurrentUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String login = userDetails.getUsername();
        return login;
    }

    /**
     * Creates authentication for new signed up user.
     * @param userSignUpDto new user dto.
     * @param authenticationManager authenticate new user.
     * @param request user request.
     */
    public void authenticateUserAndSetSession(UserSignUpDto userSignUpDto,
                                               AuthenticationManager authenticationManager,
                                               HttpServletRequest request) {
        String username = userSignUpDto.getLogin();
        String password = userSignUpDto.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
}
