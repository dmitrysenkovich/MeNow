package com.sam_solutions.web.controller;

import com.sam_solutions.app.converter.UserToUserProfileDtoConverter;
import com.sam_solutions.app.dto.LikeDto;
import com.sam_solutions.app.dto.PostDto;
import com.sam_solutions.app.dto.PostProfileDto;
import com.sam_solutions.app.dto.UserProfileDto;
import com.sam_solutions.app.dto.UserRelationDto;
import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.model.Follower;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.service.LikeService;
import com.sam_solutions.app.service.PostService;
import com.sam_solutions.app.service.UserRelationService;
import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import com.sam_solutions.app.utils.FileUtils;
import com.sam_solutions.app.utils.NotificationUtils;
import com.sam_solutions.app.utils.PermissionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Dispatches all work related to profile handling.
 */
@Controller
public class ProfileController {
    private static final Logger logger = Logger.getLogger(ProfileController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeService likeService;

    @Autowired
    private UserToUserProfileDtoConverter userToUserProfileDtoConverter;

    @Autowired
    private AuthorizationUtils authorizationUtils;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private PermissionUtils permissionUtils;
    @Autowired
    private NotificationUtils notificationUtils;

    private Map<String, UserProfileDto> ownerProfileDtoByLogin = new HashMap<>();

    /**
     * Dispatches user successful authorization.
     * @return profile ModelAndView.
     */
    @RequestMapping(value = "/profile/{ownerLogin}", method = RequestMethod.GET)
    public ModelAndView userPage(@PathVariable String ownerLogin) {
        logger.info("Profile GET request");

        String login = authorizationUtils.getCurrentUserLogin();
        String message = MessageFormat.format("User login: {0}", login);
        logger.info(message);

        // getting page viewer and owner.
        User user = userService.getUserByLogin(login);

        User owner = userService.getUserByLogin(ownerLogin);
        if (owner == null) {
            String error = MessageFormat.format("No such user: {0}", ownerLogin);
            logger.error(error);
            return new ModelAndView("redirect:/404");
        }

        // getting owner profile info.
        UserProfileDto ownerProfileDto = userToUserProfileDtoConverter.convert(owner);
        ownerProfileDtoByLogin.put(login, ownerProfileDto);

        fileUtils.saveUserImage(ownerLogin, owner.getAvatarImage(), owner.getAvatarImageName());

        ModelAndView model = new ModelAndView();

        // retrieving user relations info and filling profile page.
        if (login.equals(ownerLogin)) {
            Integer followersCount = userRelationService.getFollowersCount(user);
            model.addObject("followersCount", followersCount);

            model.addObject("newPost", new PostProfileDto());
        }
        else {
            UserRelationDto ownerUserUserRelationDto = userRelationService.getUsersRelation(ownerLogin, login);
            model.addObject("ownerUserUserRelation", ownerUserUserRelationDto);

            UserRelationDto userOwnerUserRelationDto = userRelationService.getUsersRelation(login, ownerLogin);
            model.addObject("userOwnerUserRelation", userOwnerUserRelationDto);
        }

        // checking for viewer access to owner page.
        boolean allowed = permissionUtils.isAllowed(owner, user);
        if (allowed) {
            model.addObject("allowed", true);

            List<PostDto> posts = postService.getUserPosts(ownerLogin);
            if (!posts.isEmpty()) {
                model.addObject("posts", posts);

                Map<Long, LikeDto> likes = likeService.getLikes(posts, login);
                model.addObject("likes", likes);
            }
            else
                model.addObject("postsNotFound", true);
        }
        else {
            model.addObject("allowed", false);
        }

        model.addObject("owner", ownerProfileDto);
        model.addObject("viewerLogin", user.getLogin());
        model.setViewName("profile");
        return model;
    }

    /**
     * Called when user changes his avatar image.
     * @param newAvatarFile new avatar image file to upload.
     */
    @RequestMapping(value = "/profile/change_avatar", method = RequestMethod.POST)
    @ResponseBody
    public void upload(@RequestParam("newAvatar") CommonsMultipartFile newAvatarFile) throws UnsupportedEncodingException {
        logger.info("Change avatar POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        if (newAvatarFile == null || newAvatarFile.isEmpty()) {
            String error = MessageFormat.format("Error while trying change user {0} avatar", login);
            logger.error(error);
            return;
        }

        String newAvatarName = newAvatarFile.getOriginalFilename();
        newAvatarName = new String(newAvatarName.getBytes("ISO-8859-1"), "UTF-8");
        fileUtils.saveUserImage(login, newAvatarFile.getBytes(), newAvatarName);
        UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);
        ownerProfileDto.setAvatarImageName(newAvatarName);
        userService.updateUser(ownerProfileDto);
    }

    /**
     * Refreshes user nick and/or status.
     * @param newOwnerProfileDto user info to be updated.
     * @param result validation result.
     * @return error if one exists.
     */
    @RequestMapping(value = "/profile/refresh", method = RequestMethod.POST)
    @ResponseBody
    public String refresh(@Valid @RequestBody UserProfileDto newOwnerProfileDto, BindingResult result) {
        logger.info("Refresh POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        if (result.hasErrors()) {
            logger.info("New user info form has errors");
            if (result.hasFieldErrors("nick"))
                return "nickHasError";
            else if (result.hasFieldErrors("status"))
                return "statusHasError";
        }
        else {
            UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);
            ownerProfileDto.setNick(newOwnerProfileDto.getNick());
            ownerProfileDto.setStatus(newOwnerProfileDto.getStatus());
            userService.updateUser(ownerProfileDto);
        }

        return null;
    }

    /**
     * Handles new post creation.
     * @param newPost new post.
     * @param result validation result.
     * @return error if exists.
     */
    @RequestMapping(value = "/profile/tell", method = RequestMethod.POST)
    @ResponseBody
    public String tell(@Valid @RequestBody PostProfileDto newPost, BindingResult result) {
        logger.info("Tell POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        if (result.hasErrors()) {
            logger.info("New post form has errors");
            return "postHasError";
        }
        else {
            newPost.setCreatedDate(new Date());
            Long id = postService.addPost(newPost, login);

            User user = userService.getUserByLogin(login);
            List<String> phoneNumbers = userRelationService.getInterestedAllowedFollowersPhoneNumbers(user);
            String notification = MessageFormat.format("Check you feed: user {0} told something new!", user.getNick());
            notificationUtils.notifyUsers(phoneNumbers, notification);

            return Long.toString(id);
        }
    }

    /**
     * When user pressed follow button.
     * @return redirect to profile with follower relation saved.
     */
    @RequestMapping(value = "/profile/follow", method = RequestMethod.GET)
    public String follow() {
        logger.info("Follow GET request");

        String login = authorizationUtils.getCurrentUserLogin();
        UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);

        if (ownerProfileDto == null || ownerProfileDto.getLogin().equals(login)) {
            logger.info("Error while trying to follow user");
            return "redirect:/404";
        }

        UserRelationDto ownerUserUserRelationDto = userRelationService.getUsersRelation(ownerProfileDto.getLogin(), login);
        ownerUserUserRelationDto.setIsFollower(Follower.IS_FOLLOWER);
        userRelationService.addOrUpdateUserRelation(ownerUserUserRelationDto);

        return "redirect:/profile/" + ownerProfileDto.getLogin();
    }

    /**
     * When user pressed unfollow button.
     * @return redirect to profile with follower relation removed.
     */
    @RequestMapping(value = "/profile/unfollow", method = RequestMethod.GET)
    public String unfollow() {
        logger.info("Unfollow GET request");

        String login = authorizationUtils.getCurrentUserLogin();
        UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);

        if (ownerProfileDto == null || ownerProfileDto.getLogin().equals(login)) {
            logger.info("Error while trying to unfollow user");
            return "redirect:/404";
        }

        UserRelationDto ownerUserUserRelationDto = userRelationService.getUsersRelation(ownerProfileDto.getLogin(), login);
        ownerUserUserRelationDto.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationService.addOrUpdateUserRelation(ownerUserUserRelationDto);

        return "redirect:/profile/" + ownerProfileDto.getLogin();
    }

    /**
     * Called to add user in black list.
     */
    @RequestMapping(value = "/profile/disable", method = RequestMethod.POST)
    @ResponseBody
    public void disable() {
        logger.info("Disable POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);

        if (ownerProfileDto == null || !userService.loginExists(ownerProfileDto.getLogin())
                || login.equals(ownerProfileDto.getLogin())) {
            logger.info("Error while trying to disable user");
            return;
        }

        UserRelationDto userOwnerUserRelationDto = userRelationService.getUsersRelation(login, ownerProfileDto.getLogin());
        userOwnerUserRelationDto.setIsAllowed(Access.DISALLOWED);
        userRelationService.addOrUpdateUserRelation(userOwnerUserRelationDto);
    }

    /**
     * Called to remove user from black list.
     */
    @RequestMapping(value = "/profile/enable", method = RequestMethod.POST)
    @ResponseBody
    public void enable() {
        logger.info("Enable POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);

        if (ownerProfileDto == null || !userService.loginExists(ownerProfileDto.getLogin())
                || login.equals(ownerProfileDto.getLogin())) {
            logger.info("Error while trying to disable user");
            return;
        }

        UserRelationDto userOwnerUserRelationDto = userRelationService.getUsersRelation(login, ownerProfileDto.getLogin());
        userOwnerUserRelationDto.setIsAllowed(Access.ALLOWED);
        userRelationService.addOrUpdateUserRelation(userOwnerUserRelationDto);
    }

    /**
     * Removes post from user posts.
     * @param id post id on profile page.
     */
    @RequestMapping(value = "/profile/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public void delete(@PathVariable Long id) {
        logger.info("Delete POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);

        if (ownerProfileDto == null || !postService.postExists(id)
                || !login.equals(ownerProfileDto.getLogin())) {
            String error = MessageFormat.format("Error trying to delete post with id {0}", id);
            logger.error(error);
            return;
        }

        postService.deletePost(id);
    }

    /**
     * Adds like to post.
     * @param id post id on profile page.
     */
    @RequestMapping(value = "/profile/like/{id}", method = RequestMethod.POST)
    @ResponseBody
    public void like(@PathVariable Long id) {
        logger.info("Like POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);

        if (ownerProfileDto == null || !postService.postExists(id)
                || likeService.liked(id, login)) {
            String error = MessageFormat.format("Error trying like post with id {0}", id);
            logger.error(error);
            return;
        }

        likeService.like(id, login);
    }

    /**
     * Removes like from post.
     * @param id post id on profile page.
     */
    @RequestMapping(value = "/profile/dislike/{id}", method = RequestMethod.POST)
    @ResponseBody
    public void dislike(@PathVariable Long id) {
        logger.info("Dislike POST request");

        String login = authorizationUtils.getCurrentUserLogin();
        UserProfileDto ownerProfileDto = ownerProfileDtoByLogin.get(login);

        if (ownerProfileDto == null || !postService.postExists(id)) {
            String error = MessageFormat.format("Error trying like post with id {0}", id);
            logger.error(error);
            return;
        }

        likeService.dislike(id, login);
    }
}