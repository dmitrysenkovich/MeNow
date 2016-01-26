package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.SearchDto;
import com.sam_solutions.app.dto.UserRelationDto;
import com.sam_solutions.app.dto.UserSearchDto;
import com.sam_solutions.app.model.Follower;
import com.sam_solutions.app.service.UserRelationService;
import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Dispatches user search.
 */
@Controller
public class SearchController {
    private static final Logger logger = Logger.getLogger(SearchController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private AuthorizationUtils authorizationUtils;

    /**
     * Main search entry point.
     * @return search page with user list.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search() {
        logger.info("Search GET request");

        String login = authorizationUtils.getCurrentUserLogin();
        String message = MessageFormat.format("User login: {0}", login);
        logger.info(message);

        ModelAndView model = new ModelAndView();
        model.addObject("search", new SearchDto());
        model.setViewName("search");
        return model;
    }

    /**
     * Searches for users when button is pressed.
     * @param search contains string part that is searched for in nick.
     * @return search with found user and relations.
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView search(@ModelAttribute("search") SearchDto search) {
        logger.info("Search POST request");

        ModelAndView model = new ModelAndView();

        String nickPart = search.getPart();
        if (!nickPart.isEmpty()) {
            String login = authorizationUtils.getCurrentUserLogin();

            // retrieving users which nick contains string part from search..
            List<UserSearchDto> foundUsers = userService.getSearchedUsersByNickPart(nickPart, login);
            model.addObject("foundUsers", foundUsers);

            if (foundUsers.isEmpty()) {
                model.addObject("usersNotFound", true);
            }

            if (!foundUsers.isEmpty()) {
                // ..and their relations.
                Map<String, UserRelationDto> foundUsersRelations = userRelationService.getUsersRelations(nickPart, login);
                model.addObject("foundUsersRelations", foundUsersRelations);
            }
        }

        model.addObject("search", search);
        model.setViewName("search");
        return model;
    }
    /**
     * When user pressed follow button.
     */
    @RequestMapping(value = "/search/follow/{target}", method = RequestMethod.POST)
    @ResponseBody
    public void follow(@PathVariable String target) {
        logger.info("Follow POST request");

        String login = authorizationUtils.getCurrentUserLogin();

        if (!userService.loginExists(target)) {
            String error = MessageFormat.format("Error trying follow user {0}", target);
            logger.error(error);
            return;
        }

        UserRelationDto userRelationDto = userRelationService.getUsersRelation(target, login);
        userRelationDto.setIsFollower(Follower.IS_FOLLOWER);
        userRelationService.addOrUpdateUserRelation(userRelationDto);
    }

    /**
     * When user pressed unfollow button.
     */
    @RequestMapping(value = "/search/unfollow/{target}", method = RequestMethod.POST)
    @ResponseBody
    public void unfollow(@PathVariable String target) {
        logger.info("Unfollow POST request");

        String login = authorizationUtils.getCurrentUserLogin();

        if (!userService.loginExists(target)) {
            String error = MessageFormat.format("Error trying follow user {0}", target);
            logger.error(error);
            return;
        }

        UserRelationDto userRelationDto = userRelationService.getUsersRelation(target, login);
        userRelationDto.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationService.addOrUpdateUserRelation(userRelationDto);
    }
}
