package com.sam_solutions.app.service;

import com.sam_solutions.app.converter.UserSignUpDtoToUserConverter;
import com.sam_solutions.app.converter.UserToUserAdminDtoConverter;
import com.sam_solutions.app.converter.UserToUserFeedDtoConverter;
import com.sam_solutions.app.converter.UserToUserSearchDtoConverter;
import com.sam_solutions.app.converter.UserToUserSettingsDtoConverter;
import com.sam_solutions.app.dao.UserRepository;
import com.sam_solutions.app.dto.UserAdminDto;
import com.sam_solutions.app.dto.UserFeedDto;
import com.sam_solutions.app.dto.UserProfileDto;
import com.sam_solutions.app.dto.UserSearchDto;
import com.sam_solutions.app.dto.UserSettingsDto;
import com.sam_solutions.app.dto.UserSignUpDto;
import com.sam_solutions.app.model.Notification;
import com.sam_solutions.app.model.PermissionType;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User operations management.
 */
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSignUpDtoToUserConverter userSignUpDtoToUserConverter;
    @Autowired
    private UserToUserSearchDtoConverter userToUserSearchDtoConverter;
    @Autowired
    private UserToUserAdminDtoConverter userToUserAdminDtoConverter;
    @Autowired
    private UserToUserFeedDtoConverter userToUserFeedDtoConverter;
    @Autowired
    private UserToUserSettingsDtoConverter userToUserSettingsDtoConverter;

    @Autowired
    private FileUtils fileUtils;

    /**
     * Adds new signed up user.
     * @param userSignUpDto form data.
     */
    public void addUser(UserSignUpDto userSignUpDto) {
        User user = userSignUpDtoToUserConverter.convert(userSignUpDto);
        userRepository.save(user);
    }

    /**
     * Updates user in database.
     * @param userProfileDto updated user.
     */
    public void updateUser(UserProfileDto userProfileDto) {
        User user = userRepository.findByLogin(userProfileDto.getLogin());
        user.setNick(userProfileDto.getNick());
        user.setStatus(userProfileDto.getStatus());
        user.setAvatarImageName(userProfileDto.getAvatarImageName());
        byte[] avatarImage = fileUtils.loadUserImage(userProfileDto.getLogin(), userProfileDto.getAvatarImageName());
        user.setAvatarImage(avatarImage);
        userRepository.save(user);
    }

    /**
     * Updates user in database.
     * @param userAdminDto updated user.
     */
    public void updateUser(UserAdminDto userAdminDto) {
        User user = userRepository.findByLogin(userAdminDto.getLogin());
        user.setAccess(userAdminDto.getAccess());
        userRepository.save(user);
    }

    /**
     * Updates user in database.
     * @param userSettingsDto updated user.
     * @param login user login.
     */
    public void updateUser(UserSettingsDto userSettingsDto, String login) {
        User user = userRepository.findByLogin(login);
        user.setPermissionType(PermissionType.valueOf(userSettingsDto.getPermissionType()));
        user.setPhoneNumber(userSettingsDto.getPhoneNumber());
        user.setNotification(Notification.fromString(userSettingsDto.getNotification()));
        userRepository.save(user);
    }

    /**
     * Deletes used from database.
     * @param userAdminDto user to be deleted.
     */
    public void deleteUser(UserAdminDto userAdminDto) {
        userRepository.delete(userAdminDto.getId());
    }

    /**
     * Finds user by login.
     * @param login login of sought-for user.
     * @return found user
     */
    public User getUserByLogin(String login) {
        User user = userRepository.findByLogin(login);
        return user;
    }

    /**
     * Returns user settings in dto object.
     * @param login user login.
     * @return user settings.
     */
    public UserSettingsDto getUserSettingsByLogin(String login) {
        User user = userRepository.findByLogin(login);
        UserSettingsDto userSettingsDto = userToUserSettingsDtoConverter.convert(user);
        return userSettingsDto;
    }

    /**
     * Checks if user with passed login exists.
     * @param login sought-for login.
     * @return check result.
     */
    public boolean loginExists(String login) {
        boolean loginExists = userRepository.loginExists(login);
        return loginExists;
    }

    /**
     * Returns users converted for view representation.
     * @param nickPart specified nick part.
     * @param login searching user login.
     * @return users with similar nick.
     */
    public List<UserSearchDto> getSearchedUsersByNickPart(String nickPart, String login) {
        List<User> usersByNickPart = userRepository.getUsersByNickPart(nickPart, login);
        ArrayList<UserSearchDto> usersByNickPartDto = new ArrayList<>();
        for (User foundUser : usersByNickPart) {
            UserSearchDto userSearchDto = userToUserSearchDtoConverter.convert(foundUser);
            fileUtils.saveUserImage(foundUser.getLogin(), foundUser.getAvatarImage(), foundUser.getAvatarImageName());
            usersByNickPartDto.add(userSearchDto);
        }
        return usersByNickPartDto;
    }

    /**
     * Returns users with nick is similar to specified.
     * @param nickPart specified nick part.
     * @param user searching user.
     * @return users with similar nick.
     */
    public List<User> getUsersByNickPart(String nickPart, User user) {
        List<User> usersByNickPart = userRepository.getUsersByNickPart(nickPart, user.getLogin());
        return usersByNickPart;
    }

    /**
     * Finds users by nick or login part and return them in map.
     * @param nickOrLoginPart nick or login part of searched for users.
     * @return users by nick or login part.
     */
    public Map<String, UserAdminDto> getUsersByNickOrLoginPart(String nickOrLoginPart) {
        List<User> usersByNickOrLoginPart = userRepository.getUsersByNickOrLoginPart(nickOrLoginPart);
        Map<String, UserAdminDto> usersByNickOrLoginPartDto = new HashMap<>();
        for (User user : usersByNickOrLoginPart) {
            UserAdminDto userAdminDto = userToUserAdminDtoConverter.convert(user);
            usersByNickOrLoginPartDto.put(user.getLogin(), userAdminDto);
        }
        return usersByNickOrLoginPartDto;
    }

    /**
     * Returns users that user with passed login is following.
     * whose posts he/she can view.
     * @param login specified user login.
     * @return users followed by the user.
     */
    public Map<String, UserFeedDto> getFollowedAllowedUsers(String login) {
        List<User> followed = userRepository.getFollowedAllowedUsers(login);
        Map<String, UserFeedDto> followedDto = new HashMap<>();
        for (User user : followed) {
            UserFeedDto userFeedDto = userToUserFeedDtoConverter.convert(user);
            followedDto.put(user.getLogin(), userFeedDto);
        }
        return followedDto;
    }

    /**
     * Returns UserAdminDto object corresponding
     * to user with passed login.
     * @param login user login.
     * @return searched for UserAdminDto object.
     */
    public UserAdminDto getUserAdminDto(String login) {
        User user = userRepository.findByLogin(login);
        UserAdminDto userAdminDto = userToUserAdminDtoConverter.convert(user);
        return userAdminDto;
    }
}
