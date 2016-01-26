package com.sam_solutions.app.model;

import java.util.HashMap;
import java.util.Map;

/**
 * User account access permissions.
 */
public enum PermissionType {
    ALL(0), FOLLOWERS_ONLY(1), ME_ONLY(2);

    private int permissionType;

    private static Map<Integer, PermissionType> map = new HashMap<>();

    static {
        for (PermissionType permissionType : PermissionType.values()) {
            map.put(permissionType.permissionType, permissionType);
        }
    }

    PermissionType(final int permissionType) { this.permissionType = permissionType; }

    public int getValue() {
        return permissionType;
    }

    public static PermissionType valueOf(int permissionType) {
        return map.get(permissionType);
    }
}
