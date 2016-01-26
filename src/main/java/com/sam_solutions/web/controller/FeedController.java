package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.LikeDto;
import com.sam_solutions.app.dto.PostDto;
import com.sam_solutions.app.dto.UserFeedDto;
import com.sam_solutions.app.service.LikeService;
import com.sam_solutions.app.service.PostService;
import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import com.sam_solutions.app.utils.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Manages feed viewing.
 */
@Controller
public class FeedController {
    private static final Logger logger = Logger.getLogger(FeedController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeService likeService;

    @Autowired
    private AuthorizationUtils authorizationUtils;
    @Autowired
    private FileUtils fileUtils;

    /**
     * Simply fetches and shows latest feed.
     * @return feed ModelAndView.
     */
    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public ModelAndView feed() {
        logger.info("Feed GET request");

        ModelAndView model = new ModelAndView();

        String login = authorizationUtils.getCurrentUserLogin();

        Map<String, UserFeedDto> followed = userService.getFollowedAllowedUsers(login);
        boolean noFeed = false;
        // if no followed users that enabled access to us
        // we don't need to fetch their posts.
        if (!followed.isEmpty()) {
            for (Map.Entry<String, UserFeedDto> entry : followed.entrySet()) {
                String currentLogin = entry.getKey();
                byte[] avatarImage = entry.getValue().getAvatarImage();
                String avatarImageName = entry.getValue().getAvatarImageName();
                fileUtils.saveUserImage(currentLogin, avatarImage, avatarImageName);
            }

            List<PostDto> feed = postService.getLatestFeed(login);
            if (!feed.isEmpty()) {
                model.addObject("feed", feed);
                model.addObject("followed", followed);

                Map<Long, LikeDto> likes = likeService.getLikes(feed, login);
                model.addObject("likes", likes);
            }
            else
                noFeed = true;
        }
        else
            noFeed = true;

        if (noFeed) {
            model.addObject("noFeed", true);
        }

        model.setViewName("feed");
        return model;
    }

    /**
     * Adds like to post.
     * @param id post id on feed page.
     */
    @RequestMapping(value = "/feed/like/{id}", method = RequestMethod.POST)
    @ResponseBody
    public void like(@PathVariable Long id) {
        logger.info("Like POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        if (!postService.postExists(id) || likeService.liked(id, login)) {
            String error = MessageFormat.format("Error trying like post with id: {0}", id);
            logger.error(error);
            return;
        }

        likeService.like(id, login);
    }

    /**
     * Removes like from post.
     * @param id post id on feed page.
     */
    @RequestMapping(value = "/feed/dislike/{id}", method = RequestMethod.POST)
    @ResponseBody
    public void dislike(@PathVariable Long id) {
        logger.info("Dislike POST request");

        if (!postService.postExists(id)) {
            String error = MessageFormat.format("No post with such id: {0}", id);
            logger.error(error);
            return;
        }

        String login = authorizationUtils.getCurrentUserLogin();
        likeService.dislike(id, login);
    }
}
