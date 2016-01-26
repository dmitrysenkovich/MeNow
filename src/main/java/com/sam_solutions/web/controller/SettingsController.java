package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.UserSettingsDto;
import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.MessageFormat;

/**
 * Dispatches settings handling.
 */
@Controller
public class SettingsController {
    private static final Logger logger = Logger.getLogger(SettingsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationUtils authorizationUtils;

    /**
     * Settings the only view dispatcher.
     * @return main settings view.
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView settings() {
        logger.info("Settings GET request");

        String login = authorizationUtils.getCurrentUserLogin();
        String message = MessageFormat.format("User login: {0}", login);
        logger.info(message);

        UserSettingsDto userSettingsDto = userService.getUserSettingsByLogin(login);

        ModelAndView model = new ModelAndView();
        model.addObject("userSettingsDto", userSettingsDto);
        model.setViewName("settings");
        return model;
    }

    /**
     * The only button handler. Saves new privacy settings.
     * @param userSettingsDto settings representation.
     * @return error if form has errors.
     */
    @RequestMapping(value = "/settings/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@Valid @RequestBody UserSettingsDto userSettingsDto, BindingResult bindingResult) {
        logger.info("Save settings POST request");

        String login = authorizationUtils.getCurrentUserLogin();

        if (bindingResult.hasErrors()) {
            logger.info("Settings form has errors");
            return "phoneHasError";
        }
        else {
            userService.updateUser(userSettingsDto, login);
            return null;
        }
    }
}
