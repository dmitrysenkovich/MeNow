package com.sam_solutions.app.model;

import javax.persistence.*;

/**
 * Representation of likes table.
 */
@Entity
@Table(name = "LIKES")
public class Like {
    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * Post id.
     */
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    /**
     * User login.
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
