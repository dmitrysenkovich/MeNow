package com.sam_solutions.web.controller;

import com.sam_solutions.app.converter.UserToUserProfileDtoConverter;
import com.sam_solutions.app.dto.PostDto;
import com.sam_solutions.app.dto.PostProfileDto;
import com.sam_solutions.app.dto.UserProfileDto;
import com.sam_solutions.app.dto.UserRelationDto;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.service.LikeService;
import com.sam_solutions.app.service.PostService;
import com.sam_solutions.app.service.UserRelationService;
import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import com.sam_solutions.app.utils.FileUtils;
import com.sam_solutions.app.utils.NotificationUtils;
import com.sam_solutions.app.utils.PermissionUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * ProfileController test class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private UserService userService;
    @Mock
    private UserRelationService userRelationService;
    @Mock
    private PostService postService;
    @Mock
    private LikeService likeService;

    @Mock
    private UserToUserProfileDtoConverter userToUserProfileDtoConverter;

    @Mock
    private AuthorizationUtils authorizationUtils;
    @Mock
    private FileUtils fileUtils;
    @Mock
    private PermissionUtils permissionUtils;
    @Mock
    public NotificationUtils notificationUtils;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testUserPageOwnerNotExist() throws Exception {
        String login = "test";
        String ownerLogin = "test1";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(new User());
        when(userService.getUserByLogin(ownerLogin)).thenReturn(null);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/404"))
                .andExpect(model().size(0));
    }

    @Test
     public void testUserPageUserProfileNoPosts() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        boolean allowed = true;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userRelationService.getFollowersCount(user)).thenReturn(0);
        when(permissionUtils.isAllowed(owner, user)).thenReturn(allowed);
        when(postService.getUserPosts(ownerLogin)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("newPost"))
                .andExpect(model().attributeExists("followersCount"))
                .andExpect(model().attributeExists("allowed"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().size(5));
    }

    @Test
    public void testUserPageUserProfilePostsExists() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        boolean allowed = true;
        List<PostDto> posts = new ArrayList<>();
        posts.add(new PostProfileDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userRelationService.getFollowersCount(user)).thenReturn(0);
        when(permissionUtils.isAllowed(owner, user)).thenReturn(allowed);
        when(postService.getUserPosts(ownerLogin)).thenReturn(posts);
        when(likeService.getLikes(posts, login)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("newPost"))
                .andExpect(model().attributeExists("followersCount"))
                .andExpect(model().attributeExists("allowed"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().size(5));
    }

    @Test
    public void testUserPageAnotherUserProfileNotAllowed() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        boolean allowed = false;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userRelationService.getFollowersCount(user)).thenReturn(0);
        when(permissionUtils.isAllowed(owner, user)).thenReturn(allowed);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("allowed"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().size(5));
    }

    @Test
    public void testUserPageAnotherUserProfileNoPosts() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        boolean allowed = true;
        List<PostDto> posts = new ArrayList<>();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userRelationService.getFollowersCount(user)).thenReturn(0);
        when(permissionUtils.isAllowed(owner, user)).thenReturn(allowed);
        when(postService.getUserPosts(ownerLogin)).thenReturn(posts);
        when(likeService.getLikes(posts, login)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("allowed"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().size(5));
    }

    @Test
    public void testUserPageAnotherUserProfilePostsExist() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        boolean allowed = true;
        List<PostDto> posts = new ArrayList<>();
        posts.add(new PostProfileDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userRelationService.getFollowersCount(user)).thenReturn(0);
        when(permissionUtils.isAllowed(owner, user)).thenReturn(allowed);
        when(postService.getUserPosts(ownerLogin)).thenReturn(posts);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("allowed"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().size(5));
    }

    @Test
    public void testRefreshError() throws Exception {
        String login = "test";
        UserProfileDto ownerProfileDto = new UserProfileDto();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        MvcResult result = mockMvc.perform(post("/profile/refresh").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nick\":\"\", \"status\":\"\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("nickHasError", content);

        verify(userService, times(0)).updateUser(ownerProfileDto);
    }

    @Test
    public void testRefreshNoErrors() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        boolean allowed = false;
        String nick = "test";
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setNick(nick);
        userProfileDto.setStatus("");

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userRelationService.getFollowersCount(user)).thenReturn(0);
        when(permissionUtils.isAllowed(owner, user)).thenReturn(allowed);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        MvcResult result = mockMvc.perform(post("/profile/refresh").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nick\":\"test\", \"status\":\"\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotEquals("nickHasError", content);
        assertNotEquals("statusHasError", content);

        verify(userService, times(1)).updateUser(ownerProfileDto);
    }

    @Test
    public void testTellError() throws Exception {
        String login = "test";
        PostProfileDto postProfileDto = new PostProfileDto();

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        MvcResult result = mockMvc.perform(post("/profile/tell").contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("postHasError", content);

        verify(postService, times(0)).addPost(postProfileDto, login);
    }

    @Test
    public void testTellNoErrors() throws Exception {
        String login = "test";
        User user = new User();
        PostProfileDto postProfileDto = new PostProfileDto();
        postProfileDto.setMessage("test");


        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userRelationService.getInterestedAllowedFollowersPhoneNumbers(user)).thenReturn(new ArrayList<>());

        MvcResult result = mockMvc.perform(post("/profile/tell").contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"test\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotEquals("postHasError", content);
    }

    @Test
    public void testFollowNullDto() throws Exception {
        String login = "test";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/follow")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/404"))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    public void testFollowInvalidDto() throws Exception {
        String login = "test";
        String ownerLogin = "test1";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/follow")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/404"))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    public void testFollow() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userRelationService.getUsersRelation(ownerProfileDto.getLogin(), login)).thenReturn(new UserRelationDto());
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/follow")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile/" + ownerLogin))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    public void testUnfollowNullDto() throws Exception {
        String login = "test";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/unfollow")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/404"))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    public void testUnfollowInvalidDto() throws Exception {
        String login = "test";
        String ownerLogin = "test1";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/unfollow")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/404"))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    public void testUnfollow() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userRelationService.getUsersRelation(ownerProfileDto.getLogin(), login)).thenReturn(new UserRelationDto());
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/unfollow")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile/" + ownerLogin))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    public void testDisableNullDto() throws Exception {
        String login = "test";
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin("test1");

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/disable")
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(0)).getUsersRelation(login, ownerProfileDto.getLogin());
    }

    @Test
    public void testDisableLoginNotExists() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/disable")
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(0)).getUsersRelation(login, ownerProfileDto.getLogin());
    }

    @Test
    public void testDisableSameLogin() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userService.loginExists(ownerProfileDto.getLogin())).thenReturn(false);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/disable")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void testDisable() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userRelationService.getUsersRelation(login, ownerProfileDto.getLogin())).thenReturn(new UserRelationDto());
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userService.loginExists(ownerProfileDto.getLogin())).thenReturn(true);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/disable")
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(2)).getUsersRelation(login, ownerProfileDto.getLogin());
    }

    @Test
    public void testEnableNullDto() throws Exception {
        String login = "test";
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin("test1");

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/enable")
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(0)).getUsersRelation(login, ownerProfileDto.getLogin());
    }

    @Test
    public void testEnableLoginNotExists() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/enable")
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(0)).getUsersRelation(login, ownerProfileDto.getLogin());
    }

    @Test
    public void testEnableSameLogin() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userService.loginExists(ownerProfileDto.getLogin())).thenReturn(false);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/enable")
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(0)).getUsersRelation(login, ownerProfileDto.getLogin());
    }

    @Test
    public void testEnable() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userRelationService.getUsersRelation(login, ownerProfileDto.getLogin())).thenReturn(new UserRelationDto());
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(userService.loginExists(ownerProfileDto.getLogin())).thenReturn(true);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/enable")
        )
                .andExpect(status().isOk());

        verify(userRelationService, times(2)).getUsersRelation(login, ownerProfileDto.getLogin());
    }

    @Test
    public void testDeleteNullDto() throws Exception {
        String login = "test";
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/delete/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(postService, times(0)).deletePost(postId);
    }

    @Test
    public void testDeleteNoPost() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(postService.postExists(postId)).thenReturn(false);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/delete/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(postService, times(0)).deletePost(postId);
    }

    @Test
    public void testDeletePostExistAnotherUser() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(postService.postExists(postId)).thenReturn(true);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/delete/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(postService, times(0)).deletePost(postId);
    }

    @Test
    public void testDeletePostExistSameUser() throws Exception {
        String login = "test";
        String ownerLogin = "test";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(postService.postExists(postId)).thenReturn(true);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/delete/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(postService, times(1)).deletePost(postId);
    }

    @Test
    public void testLikeNullDto() throws Exception {
        String login = "test";
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/like/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(0)).like(postId, login);
    }

    @Test
    public void testLikeNoPost() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(postService.postExists(postId)).thenReturn(false);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/like/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(0)).like(postId, login);
    }

    @Test
    public void testLikePostExist() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(postService.postExists(postId)).thenReturn(true);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/like/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(1)).like(postId, login);
    }

    @Test
    public void testDislikeNullDto() throws Exception {
        String login = "test";
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);

        mockMvc.perform(get("/profile/dislike/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(0)).dislike(postId, login);
    }

    @Test
    public void testDislikeNoPost() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(postService.postExists(postId)).thenReturn(false);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/dislike/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(0)).dislike(postId, login);
    }

    @Test
    public void testDislikePostExist() throws Exception {
        String login = "test";
        String ownerLogin = "test1";
        User user = new User();
        User owner = new User();
        UserProfileDto ownerProfileDto = new UserProfileDto();
        ownerProfileDto.setLogin(ownerLogin);
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getUserByLogin(login)).thenReturn(user);
        when(userService.getUserByLogin(ownerLogin)).thenReturn(owner);
        when(userToUserProfileDtoConverter.convert(owner)).thenReturn(ownerProfileDto);
        when(postService.postExists(postId)).thenReturn(true);

        mockMvc.perform(get("/profile/{ownerLogin}", ownerLogin));

        mockMvc.perform(get("/profile/dislike/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(1)).dislike(postId, login);
    }
}
