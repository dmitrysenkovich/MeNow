package com.sam_solutions.app.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * AuthorizationUtils test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class AuthorizationUtilsTest {
    @Autowired
    private AuthorizationUtils authorizationUtils;

    /**
     * Tests retrieving current user login.
     */
    @Test
    @WithMockUser(username = "test", password = "test")
    public void testGetCurrentUserLogin() {
        String login = authorizationUtils.getCurrentUserLogin();

        assertEquals("test", login);
    }

    /**
     * Tests method failing without authentication.
     */
    @Test(expected = NullPointerException.class)
    public void testGetCurrentUserLoginFail() {
        authorizationUtils.getCurrentUserLogin();
    }
}
