package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.SearchDto;
import com.sam_solutions.app.dto.UserAdminDto;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * AdminController test class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationUtils authorizationUtils;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testGetAdmin() throws Exception {
        String login = "test";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/admin")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().size(1));
    }

    @Test
    public void testAdminEmptyNickPart() throws Exception {
        String nickOrLoginPart = "";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("search", searchDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().size(2));
    }

    @Test
    public void testAdminNoUsers() throws Exception {
        String login = "test";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUsersByNickOrLoginPart(nickOrLoginPart)).thenReturn(new HashMap<>());

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("search", searchDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attributeExists("usersNotFound"))
                .andExpect(model().size(3));
    }

    @Test
    public void testAdminUsersExist() throws Exception {
        String login = "test";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);
        Map<String, UserAdminDto>  foundUsers = new HashMap<>();
        foundUsers.put("test1", new UserAdminDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUsersByNickOrLoginPart(nickOrLoginPart)).thenReturn(foundUsers);

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("search", searchDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attributeExists("foundUsers"))
                .andExpect(model().size(3));
    }

    @Test
    public void testDeleteUserNoLogin() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/admin/delete/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(0)).getUserAdminDto(target);
    }

    @Test
    public void testDeleteUserLoginExistNoUsers() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(false);

        mockMvc.perform(post("/admin")
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/admin/delete/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(0)).getUserAdminDto(target);
    }

    @Test
    public void testDeleteUserLoginExistUsersExist() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);
        Map<String, UserAdminDto> foundUsers = new HashMap<>();
        foundUsers.put("test1", new UserAdminDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(true);
        when(userService.getUsersByNickOrLoginPart(nickOrLoginPart)).thenReturn(foundUsers);

        mockMvc.perform(post("/admin")
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/admin/delete/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserAdminDto(target);
    }

    @Test
    public void testBanUserNoLogin() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/admin/ban/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(0)).getUserAdminDto(target);
    }

    @Test
    public void testBanUserLoginExistNoUsers() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(false);

        mockMvc.perform(post("/admin")
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/admin/ban/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(0)).getUserAdminDto(target);
    }

    @Test
    public void testBanUserLoginExistUsersExist() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);
        UserAdminDto userAdminDto = new UserAdminDto();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(true);
        when(userService.getUserAdminDto(target)).thenReturn(userAdminDto);

        mockMvc.perform(post("/admin")
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/admin/ban/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserAdminDto(target);
    }

    @Test
    public void testUnbanUserNoLogin() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/admin/unban/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(0)).getUserAdminDto(target);
    }

    @Test
    public void testUnbanUserLoginExistNoUsers() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(false);

        mockMvc.perform(post("/admin")
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/admin/unban/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(0)).getUserAdminDto(target);
    }

    @Test
    public void testUnbanUserLoginExistUsersExist() throws Exception {
        String login = "test";
        String target = "test1";
        String nickOrLoginPart = "test";
        SearchDto searchDto = new SearchDto();
        searchDto.setPart(nickOrLoginPart);
        UserAdminDto userAdminDto = new UserAdminDto();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.loginExists(target)).thenReturn(true);
        when(userService.getUserAdminDto(target)).thenReturn(userAdminDto);

        mockMvc.perform(post("/admin")
                .flashAttr("search", searchDto));

        mockMvc.perform(get("/admin/ban/{target}", target)
        )
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserAdminDto(target);
    }
}
