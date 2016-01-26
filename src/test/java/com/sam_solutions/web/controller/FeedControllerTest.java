package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.PostDto;
import com.sam_solutions.app.dto.PostFeedDto;
import com.sam_solutions.app.dto.UserFeedDto;
import com.sam_solutions.app.service.LikeService;
import com.sam_solutions.app.service.PostService;
import com.sam_solutions.app.service.UserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * FeedController test class.
 */
@RunWith(MockitoJUnitRunner.class)
public class FeedControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private FeedController feedController;

    @Mock
    private UserService userService;
    @Mock
    private PostService postService;
    @Mock
    private LikeService likeService;

    @Mock
    private AuthorizationUtils authorizationUtils;
    @Mock
    private FileUtils fileUtils;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(feedController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testNoFollowedUsers() throws Exception {
        String login = "test";

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getFollowedAllowedUsers(login)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/feed"))
                .andExpect(status().isOk())
                .andExpect(view().name("feed"))
                .andExpect(model().attribute("noFeed", is(true)))
                .andExpect(model().size(1));
    }

    @Test
    public void testNoFeed() throws Exception {
        String login = "test";
        Map<String, UserFeedDto> followed = new HashMap<>();
        followed.put("test1", new UserFeedDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getFollowedAllowedUsers(login)).thenReturn(followed);
        when(postService.getLatestFeed(login)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/feed"))
                .andExpect(status().isOk())
                .andExpect(view().name("feed"))
                .andExpect(model().attribute("noFeed", is(true)))
                .andExpect(model().size(1));
    }

    @Test
    public void testFeedExist() throws Exception {
        String login = "test";
        Map<String, UserFeedDto> followed = new HashMap<>();
        followed.put("test1", new UserFeedDto());
        List<PostDto> feed = new ArrayList<>();
        feed.add(new PostFeedDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(userService.getFollowedAllowedUsers(login)).thenReturn(followed);
        when(postService.getLatestFeed(login)).thenReturn(feed);
        when(likeService.getLikes(feed, login)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/feed"))
                .andExpect(status().isOk())
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("feed"))
                .andExpect(model().attributeExists("followed"))
                .andExpect(model().attributeExists("likes"))
                .andExpect(model().size(3));
    }

    @Test
    public void testLikeWhenNoPosts() throws Exception {
        String login = "test";
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(postService.getLatestFeed(login)).thenReturn(null);
        when(postService.postExists(postId)).thenReturn(false);

        mockMvc.perform(get("/feed/like/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(0)).like(postId, login);
    }

    @Test
    public void testLikePostWithInvalidNegativeIndex() throws Exception {
        String login = "test";
        Long postId = -1L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(postService.getLatestFeed(login)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/feed/like/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(0)).like(postId, login);
    }

    @Test
    public void testLike() throws Exception {
        String login = "test";
        Long postId = 1L;
        List<PostDto> posts = new ArrayList<>();
        posts.add(new PostFeedDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(postService.getLatestFeed(login)).thenReturn(posts);
        when(postService.postExists(postId)).thenReturn(true);

        mockMvc.perform(get("/feed/like/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(1)).like(postId, login);
    }

    @Test
    public void testDislikeWhenNoPosts() throws Exception {
        String login = "test";
        Long postId = 0L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(postService.getLatestFeed(login)).thenReturn(null);
        when(postService.postExists(postId)).thenReturn(false);

        mockMvc.perform(get("/feed/dislike/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(0)).dislike(postId, login);
    }

    @Test
    public void testDislikePostWithInvalidNegativeIndex() throws Exception {
        String login = "test";
        Long postId = -1L;

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(postService.getLatestFeed(login)).thenReturn(new ArrayList<>());
        when(postService.postExists(postId)).thenReturn(false);

        mockMvc.perform(get("/feed/dislike/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(0)).dislike(postId, login);
    }

    @Test
    public void testDislike() throws Exception {
        String login = "test";
        Long postId = 1L;
        List<PostDto> posts = new ArrayList<>();
        posts.add(new PostFeedDto());

        when(authorizationUtils.getCurrentUserLogin()).thenReturn(login);
        when(postService.getLatestFeed(login)).thenReturn(posts);
        when(postService.postExists(postId)).thenReturn(true);

        mockMvc.perform(get("/feed/dislike/{id}", postId)
        )
                .andExpect(status().isOk());

        verify(likeService, times(1)).dislike(postId, login);
    }
}
