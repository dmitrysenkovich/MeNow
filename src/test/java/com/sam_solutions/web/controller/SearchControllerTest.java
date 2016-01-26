package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.SearchDto;
import com.sam_solutions.app.dto.UserRelationDto;
import com.sam_solutions.app.dto.UserSearchDto;
import com.sam_solutions.app.service.UserRelationService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * SearchController test class.
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private SearchController searchController;

    @Mock
    private UserService userService;
    @Mock
    private UserRelationService userRelationService;

    @Mock
    private AuthorizationUtils authorizationUtils;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(searchController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testGetSearch() throws Exception {
        String login = "test";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/search")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().size(1));
    }

    @Test
    public void testAdminEmptyNickPart() throws Exception {
        String nickPart = "";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("search", searchDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().size(1));
    }

    @Test
    public void testSearch() throws Exception {
        String login = "test";
        String nickPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getSearchedUsersByNickPart(nickPart, login)).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("search", searchDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("foundUsers"))
                .andExpect(model().attributeExists("usersNotFound"))
                .andExpect(model().size(3));
    }

    @Test
    public void testSearchPostUsersExist() throws Exception {
        String login = "test";
        String nickPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);
        List<UserSearchDto> foundUsers = new ArrayList<>();
        foundUsers.add(new UserSearchDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getSearchedUsersByNickPart(nickPart, login)).thenReturn(foundUsers);
        when(userRelationService.getUsersRelations(nickPart, login)).thenReturn(new LinkedHashMap<>());

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("search", searchDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("foundUsers"))
                .andExpect(model().attributeExists("foundUsersRelations"))
                .andExpect(model().size(3));
    }

    @Test
    public void testFollowUserNoLogin() throws Exception {
        String login = "test";
        String target = "test1";
        String nickPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/search/follow/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(0)).getUsersRelation(target, login);
    }

    @Test
    public void testFollowUserLoginExistNoUsers() throws Exception {
        String login = "test";
        String target = "test1";
        String nickPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);
        UserRelationDto userRelationDto = new UserRelationDto();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(true);
        when(userRelationService.getUsersRelation(target, login)).thenReturn(userRelationDto);

        mockMvc.perform(post("/search", target)
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/search/follow/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(1)).getUsersRelation(target, login);
    }

    @Test
    public void testFollowUserLoginExistUsersExist() throws Exception {
        String login = "test";
        String target = "test1";
        String nickPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);
        UserRelationDto userRelationDto = new UserRelationDto();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(true);
        when(userRelationService.getUsersRelation(target, login)).thenReturn(userRelationDto);

        mockMvc.perform(post("/search", target)
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/search/follow/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(1)).getUsersRelation(target, login);
    }

    @Test
    public void testUnfollowUserNoLogin() throws Exception {
        String login = "test";
        String target = "test1";
        String nickPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getSearchedUsersByNickPart(nickPart, login)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/search/unfollow/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(0)).getUsersRelation(target, login);
    }

    @Test
    public void testUnfollowUserLoginExistNoUsers() throws Exception {
        String login = "test";
        String target = "test1";
        String nickPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);
        UserRelationDto userRelationDto = new UserRelationDto();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(true);
        when(userRelationService.getUsersRelation(target, login)).thenReturn(userRelationDto);

        mockMvc.perform(post("/search", target)
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/search/unfollow/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(1)).getUsersRelation(target, login);
    }

    @Test
    public void testUnfollowUserLoginExistUsersExist() throws Exception {
        String login = "test";
        String target = "test1";
        String nickPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickPart);
        UserRelationDto userRelationDto = new UserRelationDto();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(true);
        when(userRelationService.getUsersRelation(target, login)).thenReturn(userRelationDto);

        mockMvc.perform(post("/search", target)
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/search/unfollow/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(1)).getUsersRelation(target, login);
    }
}
