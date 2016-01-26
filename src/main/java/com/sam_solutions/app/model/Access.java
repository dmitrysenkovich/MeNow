package com.sam_solutions.app.model;

/**
 * User access.
 */
public enum Access {
    DISALLOWED(false), ALLOWED(true);

    private boolean isAllowed = true;

    Access(final boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    public boolean getValue() {
        return isAllowed;
    }
}
