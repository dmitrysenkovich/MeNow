package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.UserFollowingDto;
import com.sam_solutions.app.service.UserRelationService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import com.sam_solutions.app.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * MyFollowingsController test class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MyFollowingsControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private MyFollowingsController myFollowingsController;

    @Mock
    private UserRelationService userRelationService;

    @Mock
    private AuthorizationUtils authorizationUtils;
    @Mock
    private FileUtils fileUtils;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(myFollowingsController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testNoFollowed() throws Exception {
        String login = "test";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userRelationService.getFollowedUsers(login)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/my_followings")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("my_followings"))
                .andExpect(model().attributeExists("followingsNotFound"));
    }

    @Test
    public void testFollowedExist() throws Exception {
        String login = "test";
        List<UserFollowingDto> followedUsers = new ArrayList<>();
        followedUsers.add(new UserFollowingDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userRelationService.getFollowedUsers(login)).thenReturn(followedUsers);

        mockMvc.perform(get("/my_followings")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("my_followings"))
                .andExpect(model().attributeExists("followedUsers"));
    }
}
