package com.sam_solutions.web.controller;

import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * AuthorizationController test class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorizationControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private AuthorizationController authorizationController;

    @Mock
    private MessageSource validationMessageSource;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationUtils authorizationUtils;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(authorizationController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testGetLoginByDefault() throws Exception {
        mockMvc.perform(get("/")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().size(0));
    }

    @Test
    public void testGetLogin() throws Exception {
        mockMvc.perform(get("/login")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().size(0));
    }

    @Test
    public void testLoginError() throws Exception {
        mockMvc.perform(get("/login")
                        .param("error", "")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().size(1));
    }

    @Test
    public void testLoginLogout() throws Exception {
        mockMvc.perform(get("/login")
                        .param("logout", "")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("msg"))
                .andExpect(model().size(1));
    }

    @Test
    public void testGetSignUp() throws Exception {
        mockMvc.perform(get("/sign_up")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("sign_up"))
                .andExpect(model().attributeExists("newUser"))
                .andExpect(model().size(1));
    }

    @Test
    public void testSignUpFormHasErrors() throws Exception {
        String phoneNumber = "test";

        mockMvc.perform(post("/sign_up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("phoneNumber", phoneNumber)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("sign_up"))
                .andExpect(model().size(1));
    }

    @Test
    public void testSignUpLoginExists() throws Exception {
        String login = "test";
        String nick = "test";
        String email = "test@test";
        String phoneNumber = "+375299420344";
        String password = "test";

        when(userService.loginExists(login)).thenReturn(true);
        when(validationMessageSource.getMessage("", null, null)).thenReturn("test");

        mockMvc.perform(post("/sign_up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nick", nick)
                        .param("email", email)
                        .param("phoneNumber", phoneNumber)
                        .param("login", login)
                        .param("password", password)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("sign_up"))
                .andExpect(model().size(1));
    }

    @Test
    public void testSignUpNewUserAdding() throws Exception {
        String login = "test";
        String nick = "test";
        String email = "test@test";
        String phoneNumber = "+375299420344";
        String password = "test";

        when(userService.loginExists(login)).thenReturn(false);
        when(validationMessageSource.getMessage("", null, null)).thenReturn("test");

        mockMvc.perform(post("/sign_up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nick", nick)
                        .param("email", email)
                        .param("phoneNumber", phoneNumber)
                        .param("login", login)
                        .param("password", password)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/default"))
                .andExpect(model().size(1));
    }

    @Test
    public void testGetDenied() throws Exception {
        mockMvc.perform(get("/403")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("denied"));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get("/404")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("not_found"));
    }
}
