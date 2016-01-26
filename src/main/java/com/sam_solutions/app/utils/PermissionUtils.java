package com.sam_solutions.app.utils;

import com.sam_solutions.app.model.User;
import com.sam_solutions.app.service.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Cares about user permissions.
 */
public class PermissionUtils {
    @Autowired
    private UserRelationService userRelationService;

    /**
     * Checks if viewer is allowed to see owner page.
     * @param owner owner user.
     * @param viewer viewer user.
     * @return check test result
     */
    public boolean isAllowed(User owner, User viewer) {
        if (owner.getLogin().equals(viewer.getLogin()))
            return true;

        boolean allowed = true;

        // first check global permissions.
        switch (owner.getPermissionType()) {
            case ALL:
                break;
            case FOLLOWERS_ONLY:
                allowed = userRelationService.isFollower(owner, viewer).getValue();
                break;
            case ME_ONLY:
                return false;
        }

        // second check if viewer is disabled by owner.
        boolean permission = userRelationService.isAllowed(owner, viewer).getValue();
        allowed &= permission;

        return allowed;
    }
}
