package com.sam_solutions.app.service;

import com.sam_solutions.app.dao.PostRepository;
import com.sam_solutions.app.dao.UserRepository;
import com.sam_solutions.app.dto.PostDto;
import com.sam_solutions.app.model.Post;
import com.sam_solutions.app.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * PostService test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    public void testPostDeleted() {
        User user = userRepository.findByLogin("test3");
        List<Post> posts = postRepository.getAllByUserOrderByCreatedDateDesc(user);
        Post post = posts.get(0);
        postService.deletePost(post.getId());

        posts = postRepository.getAllByUserOrderByCreatedDateDesc(user);
        assertEquals(0, posts.size());
    }

    @Test
    public void testNoPosts() {
        String login = "test1";
        List<PostDto> posts = postService.getUserPosts(login);

        assertEquals(0, posts.size());
    }

    @Test
    public void testGetAllUserPosts() {
        String login = "test2";
        List<PostDto> posts = postService.getUserPosts(login);

        assertEquals(3, posts.size());
    }
}
