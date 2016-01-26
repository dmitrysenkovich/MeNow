package com.sam_solutions.app.dao;

import com.sam_solutions.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Manages all database activities related
 * to updating users table in database.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds user by login.
     * @param login login of sought-for user.
     * @return found user
     */
    User findByLogin(String login);

    /**
     * Checks if user with passed login exists.
     * @param login sought-for login.
     * @return check result.
     */
    @Query("select case when count(*) > 0 then True else False end from User where login = :login")
    boolean loginExists(@Param("login") String login);

    /**
     * Returns users with nick is similar to specified.
     * @param nickPart specified nick part.
     * @param login searching for users user login.
     * @return users with similar nick.
     */
    @Query("from User where nick like %:nickPart% and login <> :login and role = 'USER'")
    List<User> getUsersByNickPart(@Param("nickPart") String nickPart, @Param("login") String login);

    /**
     * Returns users with nick or login containing string.
     * @param nickOrLoginPart specified nick or login part part.
     * @return users with similar nick or login part.
     */
    @Query("from User where (nick like %:nickOrLoginPart% " +
            "or login like %:nickOrLoginPart%) and role = 'USER'")
        List<User> getUsersByNickOrLoginPart(@Param("nickOrLoginPart") String nickOrLoginPart);

    /**
     * Returns users that user with passed login is following.
     * @param login specified user login.
     * @return users followed by the user.
     */
    @Query("from User user where exists (from UserRelation userRelation " +
            "where owner.login = user.login and viewer.login = :login " +
            "and userRelation.isFollower = 1 and (userRelation.isAllowed = 1 " +
            "or userRelation.isAllowed is null) and " +
            "owner.permissionType <> 2)")
    List<User> getFollowedAllowedUsers(@Param("login") String login);
}
