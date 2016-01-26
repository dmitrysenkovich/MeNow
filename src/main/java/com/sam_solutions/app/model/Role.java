package com.sam_solutions.app.model;

/**
 * User roles.
 */
public enum Role {
    ADMIN("ADMIN"), USER("USER");

    private final String role;

    Role(final String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }
}
