package com.sam_solutions.app.dao;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * UserRelationRepository test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class UserRelationRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;

    @Test
    public void testNoFollowers() {
        User user = userRepository.findByLogin("test2");
        int followersCount = userRelationRepository.getFollowersCount(user);

        assertEquals(0, followersCount);
    }

    @Test
    public void testFollowerExists() {
        User user = userRepository.findByLogin("test1");
        int followersCount = userRelationRepository.getFollowersCount(user);

        assertEquals(1, followersCount);
    }

    @Test
    public void testNoRelation() {
        User owner = userRepository.findByLogin("test2");
        User viewer = userRepository.findByLogin("test1");
        UserRelation userRelation = userRelationRepository.getRelationBetween(owner, viewer);

        assertNull(userRelation);
    }

    @Test
    public void testRelationExists() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test2");
        UserRelation userRelation = userRelationRepository.getRelationBetween(owner, viewer);

        assertNotNull(userRelation);
    }

    @Test
    public void testNotFollower() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test3");
        Follower isFollower = userRelationRepository.isFollower(owner, viewer);

        assertEquals(Follower.NOT_FOLLOWER, isFollower);
    }

    @Test
    public void testIsFollower() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test2");
        Follower isFollower = userRelationRepository.isFollower(owner, viewer);

        assertEquals(Follower.IS_FOLLOWER, isFollower);
    }

    @Test
    public void testWasNotFollower() {
        User owner = userRepository.findByLogin("test2");
        User viewer = userRepository.findByLogin("test3");
        Follower isFollower = userRelationRepository.isFollower(owner, viewer);

        assertNull(isFollower);
    }

    @Test
    public void testNotAllowed() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test3");
        Access isAllowed = userRelationRepository.isAllowed(owner, viewer);

        assertEquals(Access.DISALLOWED, isAllowed);
    }

    @Test
    public void testAllowed() {
        User owner = userRepository.findByLogin("test1");
        User viewer = userRepository.findByLogin("test2");
        Access isAllowed = userRelationRepository.isAllowed(owner, viewer);

        assertEquals(Access.ALLOWED, isAllowed);
    }

    @Test
    public void testAccessNotSet() {
        User owner = userRepository.findByLogin("test2");
        User viewer = userRepository.findByLogin("test3");
        Access isAllowed = userRelationRepository.isAllowed(owner, viewer);

        assertNull(isAllowed);
    }

    @Test
    public void testNoFollowed() {
        User user = userRepository.findByLogin("test1");
        List<User> followed = userRelationRepository.getFollowedUsers(user);

        assertTrue(followed.isEmpty());
    }

    @Test
    public void testFollowedExist() {
        User user = userRepository.findByLogin("test2");
        List<User> followed = userRelationRepository.getFollowedUsers(user);

        assertEquals(1, followed.size());
    }

    @Test
    public void testNoInterestedFollowers() {
        User user = userRepository.findByLogin("test1");
        List<String> interestedAllowedFollowersPhoneNumbers = userRelationRepository.getInterestedAllowedFollowersPhoneNumbers(user);

        assertTrue(interestedAllowedFollowersPhoneNumbers.isEmpty());
    }

    @Test
    public void testNoAllowedFollowers() {
        User follower = userRepository.findByLogin("test3");
        follower.setNotification(Notification.NOTIFY);
        userRepository.save(follower);
        User user = userRepository.findByLogin("test1");
        List<String> interestedAllowedFollowersPhoneNumbers = userRelationRepository.getInterestedAllowedFollowersPhoneNumbers(user);

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
        List<String> interestedAllowedFollowersPhoneNumbers = userRelationRepository.getInterestedAllowedFollowersPhoneNumbers(user);

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
        List<String> phoneNumbers = userRelationRepository.getInterestedAllowedFollowersPhoneNumbers(user);

        assertTrue(phoneNumbers.isEmpty());
    }
}
