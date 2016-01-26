package com.sam_solutions.app.dao;

import com.sam_solutions.app.model.Post;
import com.sam_solutions.app.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * PostRepository test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class PostRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    public void testNoPosts() {
        User user = userRepository.findByLogin("test1");
        List<Post> posts = postRepository.getAllByUserOrderByCreatedDateDesc(user);

        assertTrue(posts.isEmpty());
    }

    @Test
    public void testPostsExist() {
        User user = userRepository.findByLogin("test2");
        List<Post> posts = postRepository.getAllByUserOrderByCreatedDateDesc(user);

        assertEquals(3, posts.size());
    }
}
