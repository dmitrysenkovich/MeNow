package com.sam_solutions.app.model;

/**
 * Whether to notify user via sms.
 */
public enum Notification {
    NOT_NOTIFY(false), NOTIFY(true);

    private boolean notify = true;

    Notification(final boolean notify) {
        this.notify = notify;
    }

    public boolean getValue() {
        return notify;
    }

    public static Notification fromString(String notify) {
        if (notify == null)
            return NOT_NOTIFY;
        return NOTIFY;
    }

    @Override
    public String toString() {
        if (notify) {
            return "on";
        }
        return "off";
    }
}
