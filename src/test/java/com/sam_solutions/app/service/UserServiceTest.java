package com.sam_solutions.app.service;

import com.sam_solutions.app.converter.UserToUserAdminDtoConverter;
import com.sam_solutions.app.converter.UserToUserProfileDtoConverter;
import com.sam_solutions.app.dao.UserRelationRepository;
import com.sam_solutions.app.dao.UserRepository;
import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.model.PermissionType;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.model.UserRelation;
import com.sam_solutions.app.dto.UserAdminDto;
import com.sam_solutions.app.dto.UserFeedDto;
import com.sam_solutions.app.dto.UserProfileDto;
import com.sam_solutions.app.dto.UserSearchDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * UserService test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;

    @Autowired
    private UserToUserProfileDtoConverter userToUserProfileDtoConverter;
    @Autowired
    private UserToUserAdminDtoConverter userToUserAdminDtoConverter;

    @Test
    public void testUserProfileUpdated() {
        User user = userRepository.findByLogin("test3");
        UserProfileDto userProfileDto = userToUserProfileDtoConverter.convert(user);
        userProfileDto.setNick("test");
        userService.updateUser(userProfileDto);

        user = userRepository.findByLogin("test3");
        assertEquals("test", user.getNick());

        userProfileDto.setNick("test3");
        userService.updateUser(userProfileDto);
    }

    @Test
    public void testUserAccessUpdated() {
        User user = userRepository.findByLogin("test3");
        UserAdminDto userAdminDto = userToUserAdminDtoConverter.convert(user);
        userAdminDto.setAccess(Access.DISALLOWED);
        userService.updateUser(userAdminDto);

        user = userRepository.findByLogin("test3");
        assertEquals(Access.DISALLOWED, user.getAccess());

        userAdminDto.setAccess(Access.ALLOWED);
        userService.updateUser(userAdminDto);
    }

    @Test
    public void testGetExistingUserByLogin() {
        User user = userService.getUserByLogin("test1");

        assertNotNull(user);
    }

    @Test
    public void testGetNotExistingUserByLogin() {
        User user = userService.getUserByLogin("test4");

        assertNull(user);
    }

    @Test
    public void testLoginExists(){
        boolean exists = userService.loginExists("test1");

        assertTrue(exists);
    }

    @Test
    public void testLoginNotExists(){
        boolean exists = userService.loginExists("test4");

        assertFalse(exists);
    }

    @Test
    public void testGetSearchedUsersByNickPartExists() {
        String nickPart = "test";
        String login = "test1";
        List<UserSearchDto> foundUsers = userService.getSearchedUsersByNickPart(nickPart, login);

        assertEquals(2, foundUsers.size());
    }

    @Test
    public void testGetSearchedUsersByNickPartNotExists() {
        String nickPart = "test4";
        String login = "test1";
        List<UserSearchDto> foundUsers = userService.getSearchedUsersByNickPart(nickPart, login);

        assertEquals(0, foundUsers.size());
    }

    @Test
    public void testGetSearchedUsersByNickPartSameNick() {
        String nickPart = "test1";
        String login = "test1";
        List<UserSearchDto> foundUsers = userService.getSearchedUsersByNickPart(nickPart, login);

        assertEquals(0, foundUsers.size());
    }

    @Test
    public void testGetUsersByNickPartExists() {
        String nickPart = "test";
        User user = userRepository.findByLogin("test1");
        List<User> foundUsers = userService.getUsersByNickPart(nickPart, user);

        assertEquals(2, foundUsers.size());
    }

    @Test
    public void testGetUsersByNickPartNotExists() {
        String nickPart = "test4";
        User user = userRepository.findByLogin("test1");
        List<User> foundUsers = userService.getUsersByNickPart(nickPart, user);

        assertEquals(0, foundUsers.size());
    }

    @Test
    public void testGetUsersByNickPartSameNick() {
        String nickPart = "test1";
        User user = userRepository.findByLogin("test1");
        List<User> foundUsers = userService.getUsersByNickPart(nickPart, user);

        assertEquals(0, foundUsers.size());
    }

    @Test
    public void testGetUsersByNickOrLoginPartExists() {
        String nickPart = "test";
        Map<String, UserAdminDto> foundUsers = userService.getUsersByNickOrLoginPart(nickPart);

        assertEquals(3, foundUsers.size());
    }

    @Test
    public void testGetUsersByNickOrLoginPartNotExists() {
        String nickPart = "test4";
        Map<String, UserAdminDto> foundUsers = userService.getUsersByNickOrLoginPart(nickPart);

        assertEquals(0, foundUsers.size());
    }

    @Test
    public void testFollowedAndAllowedUsersExist() {
        Map<String, UserFeedDto> followedAllowedUsers = userService.getFollowedAllowedUsers("test2");

        assertEquals(1, followedAllowedUsers.size());
    }

    @Test
    public void testAllowedIsNull() {
        String ownerLogin = "test1";
        User owner = userRepository.findByLogin(ownerLogin);
        String viewerLogin = "test2";
        User viewer = userRepository.findByLogin(viewerLogin);
        UserRelation userRelation = userRelationRepository.getRelationBetween(owner, viewer);
        userRelation.setIsAllowed(null);
        userRelationRepository.save(userRelation);
        Map<String, UserFeedDto> followedAllowedUsers = userService.getFollowedAllowedUsers(viewerLogin);

        assertEquals(1, followedAllowedUsers.size());

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelationRepository.save(userRelation);
    }

    @Test
    public void testNoFollowedUsers() {
        Map<String, UserFeedDto> followedAllowedUsers = userService.getFollowedAllowedUsers("test1");

        assertTrue(followedAllowedUsers.isEmpty());
    }

    @Test
    public void testMeOnlyPermissionTypeSet() {
        User user = userRepository.findByLogin("test1");
        user.setPermissionType(PermissionType.ME_ONLY);
        userRepository.save(user);
        Map<String, UserFeedDto> followedAllowedUsers = userService.getFollowedAllowedUsers("test2");

        assertTrue(followedAllowedUsers.isEmpty());

        user.setPermissionType(PermissionType.ALL);
        userRepository.save(user);
    }

    @Test
    public void testFollowedAreNotAllowedUsers() {
        String ownerLogin = "test1";
        User owner = userRepository.findByLogin(ownerLogin);
        String viewerLogin = "test2";
        User viewer = userRepository.findByLogin(viewerLogin);
        UserRelation userRelation = userRelationRepository.getRelationBetween(owner, viewer);
        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelationRepository.save(userRelation);
        Map<String, UserFeedDto> followedAllowedUsers = userService.getFollowedAllowedUsers(viewerLogin);

        assertTrue(followedAllowedUsers.isEmpty());

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelationRepository.save(userRelation);
    }
}
