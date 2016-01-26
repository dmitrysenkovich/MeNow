package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.UserFollowingDto;
import com.sam_solutions.app.service.UserRelationService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import com.sam_solutions.app.utils.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Shows current user followings.
 */
@Controller
public class MyFollowingsController {
    private static final Logger logger = Logger.getLogger(MyFollowingsController.class);

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private AuthorizationUtils authorizationUtils;
    @Autowired
    private FileUtils fileUtils;

    /**
     * Just shows user followings.
     * @return my_followings ModelAndView.
     */
    @RequestMapping(value = "/my_followings", method = RequestMethod.GET)
    public ModelAndView myFollowings() {
        logger.info("My followings GET request");

        ModelAndView model = new ModelAndView();

        // insure followings is clear yet to be updated.
        String login = authorizationUtils.getCurrentUserLogin();

        List<UserFollowingDto> followedUsers = userRelationService.getFollowedUsers(login);
        if (!followedUsers.isEmpty()) {
            for (UserFollowingDto user: followedUsers) {
                String currentLogin = user.getLogin();
                byte[] avatarImage = user.getAvatarImage();
                String avatarImageName = user.getAvatarImageName();
                fileUtils.saveUserImage(currentLogin, avatarImage, avatarImageName);
            }
            model.addObject("followedUsers", followedUsers);
        }
        else {
            model.addObject("followingsNotFound", true);
        }

        model.setViewName("my_followings");
        return model;
    }
}
