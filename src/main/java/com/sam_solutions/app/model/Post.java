package com.sam_solutions.app.model;

import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

/**
 * Representation of posts table.
 */
@Entity
@Table(name = "POSTS")
public class Post {
    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * Owner id.
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    /**
     * Message.
     */
    @Column(name = "MESSAGE")
    private String message;

    /**
     * Post creation date.
     */
    @Column(name = "CREATED_DATE")
    @Type(type = "timestamp")
    private Date createdDate;

    /**
     * User likes.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Like> likes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Like> getLikes() {
        return likes;
    }
}
