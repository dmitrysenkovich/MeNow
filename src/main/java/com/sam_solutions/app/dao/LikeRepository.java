package com.sam_solutions.app.dao;

import com.sam_solutions.app.model.Like;
import com.sam_solutions.app.model.Post;
import com.sam_solutions.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Manages all database activities related
 * to updating likes table in database.
 */
public interface LikeRepository extends JpaRepository<Like, Long> {
    /**
     * Returns likes count for passed post.
     * @param post post which likes we're searching.
     * @return post likes count.
     */
    @Query("select count(*) from Like where post = :post")
    int getLikesCount(@Param("post") Post post);

    /**
     * Checks if passed user liked specified post.
     * @param post post to test.
     * @param user user.
     * @return test result.
     */
    @Query("select case when count(*) > 0 then True else False end from Like where post = :post and user = :user")
    boolean liked(@Param("post") Post post, @Param("user") User user);

    /**
     * Finds like by user and post.
     * @param post liked post.
     * @param user user that liked post.
     */
    @Query("from Like where post = :post and user = :user")
    Like getLike(@Param("post") Post post, @Param("user") User user);
}
