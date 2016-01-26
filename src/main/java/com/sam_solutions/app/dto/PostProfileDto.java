package com.sam_solutions.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Post DTO representation in profile.
 */
public class PostProfileDto implements PostDto {
    /**
     * Post id.
     */
    private Long id;

    /**
     * Message.
     */
    @NotNull
    @Size(min = 1, max = 90)
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
