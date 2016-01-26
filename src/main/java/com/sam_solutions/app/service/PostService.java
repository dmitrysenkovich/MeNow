package com.sam_solutions.app.service;

import com.sam_solutions.app.converter.PostProfileDtoToPostConverter;
import com.sam_solutions.app.converter.PostToPostFeedDtoConverter;
import com.sam_solutions.app.converter.PostToPostProfileDtoConverter;
import com.sam_solutions.app.dao.PostRepository;
import com.sam_solutions.app.dao.UserRepository;
import com.sam_solutions.app.dto.PostDto;
import com.sam_solutions.app.dto.PostFeedDto;
import com.sam_solutions.app.dto.PostProfileDto;
import com.sam_solutions.app.model.Post;
import com.sam_solutions.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Post operations management.
 */
public class PostService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostToPostProfileDtoConverter postToPostProfileDtoConverter;
    @Autowired
    private PostToPostFeedDtoConverter postToPostFeedDtoConverter;
    @Autowired
    private PostProfileDtoToPostConverter postProfileDtoToPostConverter;

    /**
     * Adds new post.
     * @param postProfileDto post to be converted and added.
     * @param login post owner login.
     */
    public Long addPost(PostProfileDto postProfileDto, String login) {
        User user = userRepository.findByLogin(login);
        Post post = postProfileDtoToPostConverter.convert(postProfileDto);
        post.setUser(user);
        postRepository.save(post);
        postProfileDto.setId(post.getId());
        return post.getId();
    }

    /**
     * Deletes post from database by id.
     * @param id id of post to be deleted.
     */
    public void deletePost(Long id) {
        postRepository.delete(id);
    }

    /**
     * Checking if post with specified id exists.
     * @param id passed post id.
     * @return test result.
     */
    public boolean postExists(Long id) {
        return postRepository.exists(id);
    }

    /**
     * Returns all user posts.
     * @param login user login whose posts to be searched.
     * @return user posts.
     */
    public List<PostDto> getUserPosts(String login) {
        User user = userRepository.findByLogin(login);
        List<Post> posts = postRepository.getAllByUserOrderByCreatedDateDesc(user);
        List<PostDto> postsDto = new ArrayList<>();
        for (Post post : posts) {
            PostProfileDto postProfileDto = postToPostProfileDtoConverter.convert(post);
            postsDto.add(postProfileDto);
        }
        return postsDto;
    }

    /**
     * Returns latest posts.
     * @param login user login whose feed is to be retrieved.
     * @return latest feed.
     */
    public List<PostDto> getLatestFeed(String login) {
        List<User> followed = userRepository.getFollowedAllowedUsers(login);
        List<Post> posts = postRepository.getLatestFeed(followed);
        List<PostDto> feed = new ArrayList<>();
        for (Post post : posts) {
            PostFeedDto postFeedDto = postToPostFeedDtoConverter.convert(post);
            feed.add(postFeedDto);
        }
        return feed;
    }
}
