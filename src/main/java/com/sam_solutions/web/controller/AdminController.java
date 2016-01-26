package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.SearchDto;
import com.sam_solutions.app.dto.UserAdminDto;
import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import com.sam_solutions.app.utils.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Manages all work related to admin requests dispatching.
 */
@Controller
public class AdminController {
    private static final Logger logger = Logger.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private AuthorizationUtils authorizationUtils;

    /**
     * Main admin page controller function.
     * @return admin ModelAndView
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView adminPage() {
        logger.info("Admin GET request");

        String login = authorizationUtils.getCurrentUserLogin();
        String message = MessageFormat.format("User login: {0}", login);
        logger.info(message);

        ModelAndView model = new ModelAndView();
        model.addObject("search", new SearchDto());
        model.setViewName("admin");
        return model;
    }

    /**
     * Main search function. Called when admin presses search button.
     * @param search contains part of nick or login of users to find.
     * @return admin ModelAndView with found users list.
     */
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public ModelAndView search(@ModelAttribute("search") SearchDto search) {
        logger.info("Admin POST request");

        ModelAndView model = new ModelAndView();
        String nickOrLoginPart = search.getPart();
        if (!nickOrLoginPart.isEmpty()) {
            Map<String, UserAdminDto> foundUsers = userService.getUsersByNickOrLoginPart(nickOrLoginPart);
            for (Map.Entry<String, UserAdminDto> entry : foundUsers.entrySet()) {
                String currentLogin = entry.getKey();
                byte[] avatarImage = entry.getValue().getAvatarImage();
                String avatarImageName = entry.getValue().getAvatarImageName();
                fileUtils.saveUserImage(currentLogin, avatarImage, avatarImageName);
            }

            if (foundUsers.isEmpty())
                model.addObject("usersNotFound", true);
            else
                model.addObject("foundUsers", foundUsers);
        }
        model.addObject("admin", new SearchDto());
        model.setViewName("admin");
        return model;
    }

    /**
     * Called when admin wants to delete user. Deletes user by login.
     * @param target login of user to be deleted.
     */
    @RequestMapping(value = "/admin/delete/{target}", method = RequestMethod.POST)
    @ResponseBody
    public void delete(@PathVariable String target, HttpServletRequest request) {
        logger.info("Delete POST request");

        if (!request.isUserInRole("ADMIN") || !userService.loginExists(target)) {
            String error = MessageFormat.format("Error trying delete user: {0}", target);
            logger.error(error);
            return;
        }

        UserAdminDto userAdminDto = userService.getUserAdminDto(target);
        userService.deleteUser(userAdminDto);
    }

    /**
     * Called when admin bans user. Sets disabled access to user.
     * @param target login of user to be banned.
     */
    @RequestMapping(value = "/admin/ban/{target}", method = RequestMethod.POST)
    @ResponseBody
    public void ban(@PathVariable String target, HttpServletRequest request) {
        logger.info("Delete POST request");

        if (!request.isUserInRole("ADMIN") || !userService.loginExists(target)) {
            String error = MessageFormat.format("Error trying ban user: {0}", target);
            logger.error(error);
            return;
        }

        UserAdminDto userAdminDto = userService.getUserAdminDto(target);
        userAdminDto.setAccess(Access.DISALLOWED);
        userService.updateUser(userAdminDto);
    }

    /**
     * Called when admin unbans user. Sets enabled access to user.
     * @param target login of user to be unbanned.
     */
    @RequestMapping(value = "/admin/unban/{target}", method = RequestMethod.POST)
    @ResponseBody
    public void unban(@PathVariable String target, HttpServletRequest request) {
        logger.info("Unban POST request");

        if (!request.isUserInRole("ADMIN") || !userService.loginExists(target)) {
            String error = MessageFormat.format("Error trying unban user: {0}", target);
            logger.error(error);
            return;
        }

        UserAdminDto userAdminDto = userService.getUserAdminDto(target);
        userAdminDto.setAccess(Access.ALLOWED);
        userService.updateUser(userAdminDto);
    }
}
