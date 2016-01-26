package com.sam_solutions.app.dao;

import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.model.Follower;
import com.sam_solutions.app.model.User;
import com.sam_solutions.app.model.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Manages all database activities related to
 * updating user_relations table in database.
 */
public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
    /**
     * Returns user followers count.
     * @param owner to be searched for followers.
     * @return followers count.
     */
    @Query("select count(*) from UserRelation where isFollower = 1 and owner = :owner")
    int getFollowersCount(@Param("owner") User owner);

    /**
     * Get two users relation.
     * @param owner page owner.
     * @param viewer page viewer.
     * @return users relation.
     */
    @Query("from UserRelation where owner = :owner and viewer = :viewer")
    UserRelation getRelationBetween(@Param("owner") User owner, @Param("viewer") User viewer);

    /**
     * Check if viewer is owner follower.
     * @param owner page owner.
     * @param viewer page viewer.
     * @return test result.
     */
    @Query("select isFollower from UserRelation where owner = :owner and viewer = :viewer")
    Follower isFollower(@Param("owner") User owner, @Param("viewer") User viewer);

    /**
     * Check if viewer allowed to view owner posts.
     * @param owner page owner.
     * @param viewer page viewer.
     * @return test result.
     */
    @Query("select isAllowed from UserRelation where owner = :owner and viewer = :viewer")
    Access isAllowed(@Param("owner") User owner, @Param("viewer") User viewer);

    /**
     * Finds followed users.
     * @param user user whose followings will be retrieved.
     * @return followed users.
     */
    @Query("select userRelation.owner from UserRelation userRelation " +
            "where userRelation.viewer = :user and userRelation.isFollower = 1")
    List<User> getFollowedUsers(@Param("user") User user);

    /**
     * Returns phones interested in
     * notifying followers to send them sms.
     * @param user user whose made new post.
     * @return phone numbers.
     */
    @Query("select viewer.phoneNumber from UserRelation " +
            "where owner = :user and isFollower = 1 " +
            "and (isAllowed = 1 or isAllowed is null)" +
            "and viewer.notification = 1")
    List<String> getInterestedAllowedFollowersPhoneNumbers(@Param("user") User user);
}
