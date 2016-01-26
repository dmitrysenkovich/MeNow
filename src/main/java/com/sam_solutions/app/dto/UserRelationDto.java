package com.sam_solutions.app.dto;

import com.sam_solutions.app.model.Access;
import com.sam_solutions.app.model.Follower;
import com.sam_solutions.app.model.User;

/**
 * UserRelation DTO representation in profile.
 */
public class UserRelationDto {
    /**
     * Page owner.
     */
    private User owner;

    /**
     * Page viewer.
     */
    private User viewer;

    /**
     * Access.
     */
    private Access isAllowed;

    /**
     * Follower relation.
     */
    private Follower isFollower;

    public Access getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(Access isAllowed) {
        this.isAllowed = isAllowed;
    }

    public Follower getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(Follower isFollower) {
        this.isFollower = isFollower;
    }

    public User getViewer() {
        return viewer;
    }

    public void setViewer(User viewer) {
        this.viewer = viewer;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
