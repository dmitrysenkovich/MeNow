package com.sam_solutions.app.service;

import com.sam_solutions.app.dao.UserRelationRepository;
import com.sam_solutions.app.dao.UserRepository;
import com.sam_solutions.app.dto.UserFollowingDto;
import com.sam_solutions.app.dto.UserRelationDto;
import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.model.Follower;
import com.sam_solutions.app.model.Notification;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.model.UserRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * UserRelationService test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class UserRelationServiceTest {
    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;

    @Test
    public void testRelationUpdated() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test3");
        UserRelation userRelation = userRelationRepository.getRelationBetween(owner, viewer);
        userRelation.setIsFollower(Follower.IS_FOLLOWER);
        userRelation.setIsAllowed(Access.ALLOWED);
        userRelationService.updateUserRelation(userRelation);

        userRelation = userRelationRepository.getRelationBetween(owner, viewer);
        assertEquals(Follower.IS_FOLLOWER, userRelation.getIsFollower());
        assertEquals(Access.ALLOWED, userRelation.getIsAllowed());

        userRelation.setIsFollower(Follower.NOT_FOLLOWER);
        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelationService.updateUserRelation(userRelation);
    }

    @Test
    public void testNoFollowers() {
        User user = userRepository.findByLogin("test2");
        int followersCount = userRelationService.getFollowersCount(user);

        assertEquals(0, followersCount);
    }

    @Test
    public void testFollowerExists() {
        User user = userRepository.findByLogin("test1");
        int followersCount = userRelationService.getFollowersCount(user);

        assertEquals(1, followersCount);
    }

    @Test
    public void testNotFollower() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test3");
        Follower isFollower = userRelationService.isFollower(owner, viewer);

        assertEquals(Follower.NOT_FOLLOWER, isFollower);
    }

    @Test
    public void testIsFollower() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test2");
        Follower isFollower = userRelationService.isFollower(owner, viewer);

        assertEquals(Follower.IS_FOLLOWER, isFollower);
    }

    @Test
    public void testWasNotFollower() {
        User owner = userRepository.findByLogin("test2");
        User viewer = userRepository.findByLogin("test3");
        Follower isFollower = userRelationService.isFollower(owner, viewer);

        assertEquals(Follower.NOT_FOLLOWER, isFollower);
    }

    @Test
    public void testNotAllowed() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test3");
        Access isAllowed = userRelationService.isAllowed(owner, viewer);

        assertEquals(Access.DISALLOWED, isAllowed);
    }

    @Test
    public void testAllowed() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test2");
        Access isAllowed = userRelationService.isAllowed(owner, viewer);

        assertEquals(Access.ALLOWED, isAllowed);
    }

    @Test
    public void testAccessNotSet() {
        User owner = userRepository.findByLogin("test2");
        User viewer = userRepository.findByLogin("test3");
        Access isAllowed = userRelationService.isAllowed(owner, viewer);

        assertEquals(Access.ALLOWED, isAllowed);
    }

    @Test
    public void testGetNotExistingUsersRelations() {
        String nickPart = "test1";
        String login = "test1";
        Map<String, UserRelationDto> foundUsers = userRelationService.getUsersRelations(nickPart, login);

        assertEquals(0, foundUsers.size());
    }

    @Test
    public void testGetExistingUsersRelation() {
        String ownerLogin = "test1";
        String viewerLogin = "test2";
        UserRelationDto userRelationDto = userRelationService.getUsersRelation(ownerLogin, viewerLogin);

        assertNotNull(userRelationDto);
    }

    @Test
    public void testNoFollowed() {
        List<UserFollowingDto> followed = userRelationService.getFollowedUsers("test1");

        assertTrue(followed.isEmpty());
    }

    @Test
    public void testFollowedExist() {
        List<UserFollowingDto> followed = userRelationService.getFollowedUsers("test2");

        assertEquals(1, followed.size());
    }

    @Test
    public void testNoInterestedFollowers() {
        User user = userRepository.findByLogin("test1");
        List<String> interestedAllowedFollowersPhoneNumbers = userRelationService.getInterestedAllowedFollowersPhoneNumbers(user);

        assertTrue(interestedAllowedFollowersPhoneNumbers.isEmpty());
    }

    @Test
    public void testNoAllowedFollowers() {
        User follower = userRepository.findByLogin("test3");
        follower.setNotification(Notification.NOTIFY);
        userRepository.save(follower);
        User user = userRepository.findByLogin("test1");
        List<String> interestedAllowedFollowersPhoneNumbers = userRelationService.getInterestedAllowedFollowersPhoneNumbers(user);

        assertTrue(interestedAllowedFollowersPhoneNumbers.isEmpty());

        follower.setNotification(Notification.NOT_NOTIFY);
        userRepository.save(follower);
    }

    @Test
    public void testAllowedIsNull() {
        User follower = userRepository.findByLogin("test3");
        follower.setNotification(Notification.NOTIFY);
        userRepository.save(follower);
        User user = userRepository.findByLogin("test1");
        UserRelation userRelation = userRelationRepository.getRelationBetween(user, follower);
        userRelation.setIsAllowed(null);
        userRelation.setIsFollower(Follower.IS_FOLLOWER);
        userRelationRepository.save(userRelation);
        List<String> interestedAllowedFollowersPhoneNumbers = userRelationService.getInterestedAllowedFollowersPhoneNumbers(user);

        assertEquals(1, interestedAllowedFollowersPhoneNumbers.size());

        follower.setNotification(Notification.NOT_NOTIFY);
        userRepository.save(follower);
        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelation.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationRepository.save(userRelation);
    }

    @Test
    public void testNoFollowersAtAll() {
        User user = userRepository.findByLogin("test3");
        List<String> phoneNumbers = userRelationService.getInterestedAllowedFollowersPhoneNumbers(user);

        assertTrue(phoneNumbers.isEmpty());
    }
}
