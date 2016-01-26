package com.sam_solutions.app.dao;

import com.sam_solutions.app.model.Like;
import com.sam_solutions.app.model.Post;
import com.sam_solutions.app.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * LikeRepository test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class LikeRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;

    @Test
    public void testLikesCount() {
        Post post = postRepository.findOne(1L);
        int likesCount = likeRepository.getLikesCount(post);

        assertEquals(2, likesCount);
    }

    @Test
    public void testNoLikesCount() {
        Post post = postRepository.findOne(2L);
        int likesCount = likeRepository.getLikesCount(post);

        assertEquals(0, likesCount);
    }

    @Test
    public void testPostLiked() {
        Post post = postRepository.findOne(1L);
        User user = userRepository.findOne(1L);
        boolean liked = likeRepository.liked(post, user);

        assertTrue(liked);
    }

    @Test
    public void testPostNotLiked() {
        Post post = postRepository.findOne(1L);
        User user = userRepository.findOne(3L);
        boolean liked = likeRepository.liked(post, user);

        assertFalse(liked);
    }

    @Test
    public void testLikeExists() {
        Post post = postRepository.findOne(1L);
        User user = userRepository.findOne(1L);
        Like like = likeRepository.getLike(post, user);

        assertNotNull(like);
    }

    @Test
    public void testLikeNotExists() {
        Post post = postRepository.findOne(1L);
        User user = userRepository.findOne(3L);
        Like like = likeRepository.getLike(post, user);

        assertNull(like);
    }
}
