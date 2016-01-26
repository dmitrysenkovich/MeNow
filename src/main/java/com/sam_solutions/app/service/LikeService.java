package com.sam_solutions.app.service;

import com.sam_solutions.app.dao.LikeRepository;
import com.sam_solutions.app.dao.PostRepository;
import com.sam_solutions.app.dao.UserRepository;
import com.sam_solutions.app.dto.LikeDto;
import com.sam_solutions.app.dto.PostDto;
import com.sam_solutions.app.model.Like;
import com.sam_solutions.app.model.Post;
import com.sam_solutions.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Like operations management.
 */
public class LikeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;

    /**
     * Add like for post from user.
     * @param postId id of post that user likes.
     * @param login user login.
     */
    public void like(Long postId, String login) {
        Post post = postRepository.findOne(postId);
        User user = userRepository.findByLogin(login);
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
    }

    /**
     * Removes like for post from user.
     * @param postId post that user dislikes.
     * @param login user login.
     */
    public void dislike(Long postId, String login) {
        Post post = postRepository.findOne(postId);
        User user = userRepository.findByLogin(login);
        Like like = likeRepository.getLike(post, user);
        likeRepository.delete(like.getId());
    }

    /**
     * Finds all likes related to passed posts.
     * @param postsDto posts list.
     * @param login user login who views posts.
     * @return likes list.
     */
    public Map<Long, LikeDto> getLikes(List<PostDto> postsDto, String login) {
        List<Post> posts = getPostsFromDto(postsDto);
        User user = userRepository.findByLogin(login);
        Map<Long, LikeDto> likesDto = new HashMap<>();
        for (Post post : posts) {
            LikeDto likeDto = new LikeDto();
            int likesCount = likeRepository.getLikesCount(post);
            boolean liked = likeRepository.liked(post, user);
            likeDto.setLikesCount(likesCount);
            likeDto.setLiked(liked);
            likesDto.put(post.getId(), likeDto);
        }
        return likesDto;
    }

    /**
     * Checking if user liked post.
     * @param postId post id.
     * @param login user login.
     * @return test result.
     */
    public boolean liked(Long postId, String login) {
        Post post = postRepository.findOne(postId);
        User user = userRepository.findByLogin(login);
        boolean liked = likeRepository.liked(post, user);
        return liked;
    }

    /**
     * Retrieves posts by its dto.
     * @param postsDto dto of posts that will be found.
     * @return posts list.
     */
    private List<Post> getPostsFromDto(List<PostDto> postsDto) {
        List<Post> posts = new ArrayList<>();
        for (PostDto postDto : postsDto) {
            Post post = postRepository.findOne(postDto.getId());
            posts.add(post);
        }
        return posts;
    }
}
