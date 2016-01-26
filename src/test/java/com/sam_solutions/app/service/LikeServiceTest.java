package com.sam_solutions.app.service;

import com.sam_solutions.app.dao.LikeRepository;
import com.sam_solutions.app.model.Like;
import com.sam_solutions.app.dto.LikeDto;
import com.sam_solutions.app.dto.PostDto;
import com.sam_solutions.app.dto.PostProfileDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * LikeService test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class LikeServiceTest {
    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    public void testGetLikes() {
        PostProfileDto postDto = new PostProfileDto();
        postDto.setId(1L);
        List<PostDto> postsDto = new ArrayList<>();
        postsDto.add(postDto);
        Map<Long, LikeDto> likes = likeService.getLikes(postsDto, "test1");

        assertEquals(1, likes.size());
    }

    @Test
    public void testNoLikesForPost() {
        PostProfileDto postDto = new PostProfileDto();
        postDto.setId(2L);
        List<PostDto> postsDto = new ArrayList<>();
        postsDto.add(postDto);
        Map<Long, LikeDto> likes = likeService.getLikes(postsDto, "test1");

        assertEquals(1, likes.size());
    }

    @Test
    public void testDislike() {
        Long postId = 1L;
        likeService.dislike(postId, "test2");

        Like like = likeRepository.findOne(2L);
        assertNull(like);
    }
}
