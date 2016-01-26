package com.sam_solutions.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Representation of user_relations table.
 */
@Entity
@Table(name = "USER_RELATIONS")
public class UserRelation {
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
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    /**
     * Viewer id.
     */
    @ManyToOne
    @JoinColumn(name = "VIEWER_ID")
    private User viewer;

    /**
     * Overrides default permission behaviour.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "IS_ALLOWED")
    private Access isAllowed;

    /**
     * Follower relation.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "IS_FOLLOWER")
    private Follower isFollower;

    /**
     * Initializes relation as
     * not follower without special
     * behaviour.
     */
    public UserRelation() {
        this.isFollower = Follower.NOT_FOLLOWER;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getViewer() {
        return viewer;
    }

    public void setViewer(User viewer) {
        this.viewer = viewer;
    }

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
}
