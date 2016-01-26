package com.sam_solutions.app.dto;

import com.sam_solutions.app.validator.Phone;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User DTO representation in settings view.
 */
public class UserSettingsDto {
    /**
     * Global user permission type.
     */
    private int permissionType;

    /**
     * User phone number.
     */
    @NotNull
    @Size(min = 8, max = 16, message = "{sign_up.phone_number.size}")
    @Phone(message = "{sign_up.phone_number.size}")
    private String phoneNumber;

    /**
     * Whether to notify user via sms.
     */
    private String notification;

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
