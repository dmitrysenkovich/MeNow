package com.sam_solutions.app.service;

import com.sam_solutions.app.converter.UserRelationDtoToUserRelationConverter;
import com.sam_solutions.app.converter.UserRelationToUserRelationDtoConverter;
import com.sam_solutions.app.converter.UserToUserFollowingDtoConverter;
import com.sam_solutions.app.dao.UserRelationRepository;
import com.sam_solutions.app.dao.UserRepository;
import com.sam_solutions.app.dto.UserFollowingDto;
import com.sam_solutions.app.dto.UserRelationDto;
import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.model.Follower;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.model.UserRelation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User relation operations management.
 */
public class UserRelationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;

    @Autowired
    private UserToUserFollowingDtoConverter userToUserFollowingDtoConverter;
    @Autowired
    private UserRelationDtoToUserRelationConverter userRelationDtoToUserRelationConverter;
    @Autowired
    private UserRelationToUserRelationDtoConverter userRelationToUserRelationDtoConverter;

    /**
     * Adds new user relation to database.
     * @param userRelationDto user relation dto
     * to convert new relation from.
     */
    public void addUserRelation(UserRelationDto userRelationDto) {
        UserRelation userRelation = userRelationDtoToUserRelationConverter.convert(userRelationDto);
        userRelationRepository.save(userRelation);
    }

    /**
     * Updates user relation.
     * @param userRelation user relation to be updated.
     */
    public void updateUserRelation(UserRelation userRelation) {
        userRelationRepository.save(userRelation);
    }

    /**
     * Adds or updates user relation depending on its existing.
     * @param userRelationDto new/updated relation dto.
     */
    public void addOrUpdateUserRelation(UserRelationDto userRelationDto) {
        UserRelation userRelation = userRelationRepository.getRelationBetween(userRelationDto.getOwner(), userRelationDto.getViewer());
        if (userRelation != null) {
            userRelation.setIsFollower(userRelationDto.getIsFollower());
            userRelation.setIsAllowed(userRelationDto.getIsAllowed());
            updateUserRelation(userRelation);
        }
        else
            addUserRelation(userRelationDto);
    }

    /**
     * Returns user followers count.
     * @param user to be searched for followers.
     * @return followers count.
     */
    public int getFollowersCount(User user) {
        int followersCount = userRelationRepository.getFollowersCount(user);
        return followersCount;
    }

    /**
     * Check if viewer is owner follower.
     * @param owner page owner.
     * @param viewer page viewer.
     * @return test result.
     */
    public Follower isFollower(User owner, User viewer) {
        Follower follower = userRelationRepository.isFollower(owner, viewer);
        follower = follower == null ? Follower.NOT_FOLLOWER : follower;
        return follower;
    }

    /**
     * Check if viewer allowed to view owner posts.
     * @param owner page owner.
     * @param viewer page viewer.
     * @return test result.
     */
    public Access isAllowed(User owner, User viewer) {
        Access access = userRelationRepository.isAllowed(owner, viewer);
        access = access == null ? Access.ALLOWED : access;
        return access;
    }

    /**
     * Finds relations between user and others by part of nick.
     * @param nickPart specified nick part.
     * @param login user login to search relation with.
     * @return map of relations to user login.
     */
    public Map<String, UserRelationDto> getUsersRelations(String nickPart, String login) {
        List<User> usersByNickPart = userRepository.getUsersByNickPart(nickPart, login);
        Map<String, UserRelationDto> usersRelationsDto = new LinkedHashMap<>();
        for (User foundUser : usersByNickPart) {
            UserRelationDto userRelationDto = getUsersRelation(foundUser.getLogin(), login);
            usersRelationsDto.put(foundUser.getLogin(), userRelationDto);
        }
        return usersRelationsDto;
    }

    /**
     * Returns relation dto between two users.
     * @param ownerLogin owner user login.
     * @param viewerLogin viewer user login.
     * @return relation dto.
     */
    public UserRelationDto getUsersRelation(String ownerLogin, String viewerLogin) {
        User owner = userRepository.findByLogin(ownerLogin);
        User viewer = userRepository.findByLogin(viewerLogin);
        UserRelation userRelation = userRelationRepository.getRelationBetween(owner, viewer);
        UserRelationDto userRelationDto;
        if (userRelation != null)
            userRelationDto = userRelationToUserRelationDtoConverter.convert(userRelation);
        else {
            userRelationDto = new UserRelationDto();
            userRelationDto.setOwner(owner);
            userRelationDto.setViewer(viewer);
            userRelationDto.setIsAllowed(Access.ALLOWED);
            userRelationDto.setIsFollower(Follower.NOT_FOLLOWER);
            this.addOrUpdateUserRelation(userRelationDto);
        }
        return userRelationDto;
    }

    /**
     * Finds followed users by user login.
     * @param login user login whose followings will be retrieved.
     * @return followed users.
     */
    public List<UserFollowingDto> getFollowedUsers(String login) {
        User user = userRepository.findByLogin(login);
        List<User> followedUsers = userRelationRepository.getFollowedUsers(user);
        List<UserFollowingDto> followedUserDto = new ArrayList<>();
        for (User followedUser : followedUsers) {
            UserFollowingDto userFollowingDto = userToUserFollowingDtoConverter.convert(followedUser);
            followedUserDto.add(userFollowingDto);
        }
        return followedUserDto;
    }

    /**
     * Returns phones interested in
     * notifying followers to send them sms.
     * @param user user whose made new post.
     * @return phone numbers.
     */
    public List<String> getInterestedAllowedFollowersPhoneNumbers(User user) {
        return userRelationRepository.getInterestedAllowedFollowersPhoneNumbers(user);
    }
}
