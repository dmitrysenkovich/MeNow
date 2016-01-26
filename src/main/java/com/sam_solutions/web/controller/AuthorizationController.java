package com.sam_solutions.web.controller;

import com.sam_solutions.app.dto.UserSignUpDto;
import com.sam_solutions.app.service.UserService;
import com.sam_solutions.app.utils.AuthorizationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;


/**
 * Dispatches all work related to user authorization.
 */
@Controller
public class AuthorizationController {
    private static final Logger logger = Logger.getLogger(AuthorizationController.class);

    @Resource(name = "validationMessageSource")
    private MessageSource validationMessageSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationUtils authorizationUtils;

    /**
     * Dispatches login requests.
     * @param error error while authorizing.
     * @param logout message while login out.
     * @return login ModelAndView.
     */
	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
            HttpServletRequest request) {
        logger.info("Login GET request");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String login = authorizationUtils.getCurrentUserLogin();
            if (login != null) {
                if (request.isUserInRole("USER"))
                    return new ModelAndView("forward:/profile/" + login);
                else if (request.isUserInRole("ADMIN"))
                    return new ModelAndView("forward:/admin");
                else
                    return new ModelAndView("redirect:/404");
            }
        }

		ModelAndView model = new ModelAndView();
		if (error != null) {
            logger.info("User login failed");
			model.addObject("error", "0");
		}
		if (logout != null) {
            logger.info("User logged out");
			model.addObject("msg", "0");
		}
		model.setViewName("login");
		return model;
	}

    /**
     * Dispatches sign up requests. Adds new usr dto.
     * @return sign_up ModelAndView object.
     */
	@RequestMapping(value = "/sign_up", method = RequestMethod.GET)
	public ModelAndView signUp(HttpServletRequest request) {
        logger.info("Sign up GET request");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String login = authorizationUtils.getCurrentUserLogin();
            if (login != null) {
                if (request.isUserInRole("USER"))
                    return new ModelAndView("forward:/profile/" + login);
                else if (request.isUserInRole("ADMIN"))
                    return new ModelAndView("forward:/admin");
                else
                    return new ModelAndView("redirect:/404");
            }
        }

        UserSignUpDto newUser = new UserSignUpDto();
        ModelAndView model = new ModelAndView();
        model.addObject("newUser", newUser);
		model.setViewName("sign_up");
		return model;
	}

    /**
     * Dispatches sign up submitting requests. Adds error to response
     * or saves new user and redirects to default where role issues are managed.
     * @param userSignUpDto userSignUpDto from jsp.
     * @param bindingResult validation result.
     * @param locale current locale.
     * @param request request object.
     */
    @RequestMapping(value = "/sign_up", method = RequestMethod.POST)
    @ResponseBody
    public String signUp(@Valid @RequestBody UserSignUpDto userSignUpDto,
                       BindingResult bindingResult, Locale locale,
                       HttpServletRequest request) throws IOException {
        logger.info("Sign up POST request");

        if (bindingResult.hasErrors()) {
            logger.info("Sign up form has errors");
            List<FieldError> errors = bindingResult.getFieldErrors();
            FieldError firstError = errors.get(0);
            String errorMessage = firstError.getDefaultMessage();
            return errorMessage;
        }
        else {
            boolean loginExists = userService.loginExists(userSignUpDto.getLogin());
            if (loginExists) {
                String message = MessageFormat.format("Login {0} exist", userSignUpDto.getLogin());
                logger.info(message);
                String errorMessage = validationMessageSource.getMessage("sign_up.exists", null, locale);
                return errorMessage;
            }
            else {
                String message = MessageFormat.format("Adding new user with login {0}", userSignUpDto.getLogin());
                logger.info(message);

                userService.addUser(userSignUpDto);
                authorizationUtils.authenticateUserAndSetSession(userSignUpDto, authenticationManager, request);
                return null;
            }
        }
    }

    /**
     * Redirects to certain page depending on user role.
     * @param request request to be dispatched.
     * @return redirect path.
     */
    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public String defaultAfterLogin(HttpServletRequest request) {
        logger.info("Default GET request");

        if (request.isUserInRole("ADMIN")) {
            logger.info("Forwarding to admin view");
            return "forward:/admin";
        }
        else if (request.isUserInRole("USER")) {
            logger.info("Forwarding to profile view");

            // to redirect user to his home page.
            String login = authorizationUtils.getCurrentUserLogin();
            return "forward:/profile/" + login;
        }
        logger.info("Redirecting to denied view");
        return "redirect:/403";
    }

    /**
     * Dispatches access denying.
     * @return denied ModelAndView.
     */
    @RequestMapping(value = "/403", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView accessDenied() {
        logger.info("403 request");

        ModelAndView model = new ModelAndView();
        model.setViewName("denied");
        return model;
    }

    /**
     * Dispatches not found error.
     * @return 404 ModelAndView.
     */
    @RequestMapping(value = "/404", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView pageNotFound() {
        logger.info("404 request");

        ModelAndView model = new ModelAndView();
        model.setViewName("not_found");
        return model;
    }
}