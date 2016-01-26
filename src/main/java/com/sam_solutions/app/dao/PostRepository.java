package com.sam_solutions.app.dao;

import com.sam_solutions.app.model.Post;
import com.sam_solutions.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Manages all database activities related
 * to updating posts table in database.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Returns all user posts.
     * @param user user whose posts to be searched.
     * @return posts
     */
    List<Post> getAllByUserOrderByCreatedDateDesc(User user);

    /**
     * Returns latest posts.
     * @param users users which posts are to be retrieved.
     * @return latest feed.
     */
    @Query("from Post where user in (:users) and datediff(current_date, createdDate) < 20 order by createdDate")
    List<Post> getLatestFeed(@Param("users") List<User> users);
}
