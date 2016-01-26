package com.sam_solutions.app.utils;

import com.sam_solutions.app.dao.UserRelationRepository;
import com.sam_solutions.app.dao.UserRepository;
import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.model.Follower;
import com.sam_solutions.app.model.PermissionType;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.model.UserRelation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * PermissionUtils test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class PermissionUtilsTest {
    @Autowired
    private PermissionUtils permissionUtils;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;

    private boolean setUpIsDone = false;
    private User owner;
    private User viewer;
    private UserRelation userRelation;

    @Before
    public void setUp() {
        if (!setUpIsDone) {
            owner = userRepository.findByLogin("test1");
            viewer = userRepository.findByLogin("test2");
            userRelation = userRelationRepository.getRelationBetween(owner, viewer);
            setUpIsDone = true;
        }
    }

    @Test
    public void testAllAllowedNotFollower() {
        owner.setPermissionType(PermissionType.ALL);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelation.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertTrue(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testAllDisallowedNotFollower() {
        owner.setPermissionType(PermissionType.ALL);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelation.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testAllAllowedIsFollower() {
        owner.setPermissionType(PermissionType.ALL);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelation.setIsFollower(Follower.IS_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertTrue(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testAllDisallowedIsFollower() {
        owner.setPermissionType(PermissionType.ALL);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelation.setIsFollower(Follower.IS_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testFollowersOnlyAllowedNotFollower() {
        owner.setPermissionType(PermissionType.FOLLOWERS_ONLY);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelation.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testFollowersOnlyDisallowedNotFollower() {
        owner.setPermissionType(PermissionType.FOLLOWERS_ONLY);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelation.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testFollowersOnlyAllowedIsFollower() {
        owner.setPermissionType(PermissionType.FOLLOWERS_ONLY);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelation.setIsFollower(Follower.IS_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertTrue(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testFollowersOnlyDisallowedIsFollower() {
        owner.setPermissionType(PermissionType.FOLLOWERS_ONLY);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelation.setIsFollower(Follower.IS_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testMeOnlyAllowedNotFollower() {
        owner.setPermissionType(PermissionType.ME_ONLY);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelation.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testMeOnlyDisallowedNotFollower() {
        owner.setPermissionType(PermissionType.ME_ONLY);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelation.setIsFollower(Follower.NOT_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testMeOnlyAllowedIsFollower() {
        owner.setPermissionType(PermissionType.ME_ONLY);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.ALLOWED);
        userRelation.setIsFollower(Follower.IS_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }

    @Test
    public void testMeOnlyDisallowedIsFollower() {
        owner.setPermissionType(PermissionType.ME_ONLY);
        userRepository.save(owner);

        userRelation.setIsAllowed(Access.DISALLOWED);
        userRelation.setIsFollower(Follower.IS_FOLLOWER);
        userRelationRepository.save(userRelation);

        assertFalse(permissionUtils.isAllowed(owner, viewer));
    }
}
