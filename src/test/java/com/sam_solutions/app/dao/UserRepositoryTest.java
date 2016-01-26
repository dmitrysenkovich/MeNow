package com.sam_solutions.app.dao;

import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.model.PermissionType;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.model.UserRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * UserRepository test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;

    @Test
    public void testNoUserWithLogin() {
        User user = userRepository.findByLogin("test");

        assertNull(user);
    }

    @Test
    public void testUserWithLoginExists() {
        User user = userRepository.findByLogin("test1");

        assertNotNull(user);
    }

    @Test
    public void testIfLoginNotExists() {
        Boolean exists = userRepository.loginExists("test");

        assertFalse(exists);
    }

    @Test
    public void testIfLoginExists() {
        Boolean exists = userRepository.loginExists("test1");

        assertTrue(exists);
    }

    @Test
    public void testUsersByNickPartNotExists() {
        String nickPart = "nick";
        String login = "nick";
        List<User> users = userRepository.getUsersByNickPart(nickPart, login);

        assertTrue(users.isEmpty());
    }

    @Test
    public void testUsersByNickPartExists() {
        String nickPart = "test";
        String login = "test";
        List<User> users = userRepository.getUsersByNickPart(nickPart, login);

        assertEquals(3, users.size());
    }

    @Test
    public void testUsersByNickPartExistsIncludingLogin() {
        String nickPart = "test";
        String login = "test1";
        List<User> users = userRepository.getUsersByNickPart(nickPart, login);

        assertEquals(2, users.size());
    }

    @Test
    public void testFollowedAndAllowedUsersExist() {
        List<User> followedAllowedUsers = userRepository.getFollowedAllowedUsers("test2");

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
        List<User> followedAllowedUsers = userRepository.getFollowedAllowedUsers(viewerLogin);

        assertEquals(1, followedAllowedUsers.size());

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelationRepository.save(userRelation);
    }

    @Test
    public void testNoFollowedUsers() {
        List<User> followedAllowedUsers = userRepository.getFollowedAllowedUsers("test1");

        assertTrue(followedAllowedUsers.isEmpty());
    }

    @Test
    public void testMeOnlyPermissionTypeSet() {
        User user = userRepository.findByLogin("test1");
        user.setPermissionType(PermissionType.ME_ONLY);
        userRepository.save(user);
        List<User> followedAllowedUsers = userRepository.getFollowedAllowedUsers("test2");

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
        List<User> followedAllowedUsers = userRepository.getFollowedAllowedUsers(viewerLogin);

        assertTrue(followedAllowedUsers.isEmpty());

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelationRepository.save(userRelation);
    }
}
