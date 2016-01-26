package com.sam_solutions.app.dto;

import java.util.Date;

/**
 * Post DTO representation in feed.
 */
public class PostFeedDto implements PostDto {
    /**
     * post id.
     */
    private Long id;

    /**
     * Owner login
     */
    private String login;

    /**
     * Post message.
     */
    private String message;

    /**
     * Post creation date.
     */
    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
}
