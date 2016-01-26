package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.UserSettingsDto;
import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * SettingsController test class.
 */
@RunWith(MockitoJUnitRunner.class)
public class SettingsControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private SettingsController settingsController;

    @Mock
    private UserService userService;
    @Mock
    private AuthorizationUtils authorizationUtils;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(settingsController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testGetSettings() throws Exception {
        String login = "test";
        UserSettingsDto userSettingsDto = new UserSettingsDto();
        userSettingsDto.setPhoneNumber("test");

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserSettingsByLogin(login)).thenReturn(userSettingsDto);

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"))
                .andExpect(model().attribute("userSettingsDto", hasProperty("phoneNumber", is("test"))));
    }

    @Test
    public void testSaveInvalidSettings() throws Exception {
        String login = "test";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        MvcResult result = mockMvc.perform(post("/settings/save").contentType(MediaType.APPLICATION_JSON)
                .content("{\"phoneNumber\":\"test\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("phoneHasError", content);
    }

    @Test
    public void testSaveValidSettings() throws Exception {
        String login = "test";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        MvcResult result = mockMvc.perform(post("/settings/save").contentType(MediaType.APPLICATION_JSON)
                .content("{\"phoneNumber\":\"+375299420344\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("", content);
    }
}
