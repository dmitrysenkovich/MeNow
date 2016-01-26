package com.sam_solutions.app.model;

/**
 * Follower relation.
 */
public enum Follower {
    NOT_FOLLOWER(false), IS_FOLLOWER(true);

    private boolean isFollower = false;

    Follower(final boolean isFollower) {
        this.isFollower = isFollower;
    }

    public boolean getValue() {
        return isFollower;
    }
}
