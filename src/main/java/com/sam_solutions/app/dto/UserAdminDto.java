package com.sam_solutions.app.dto;

import com.sam_solutions.app.model.Access;

/**
 * User representation at admin page.
 */
public class UserAdminDto {
    /**
     * User id.
     */
    private Long id;
    /**
     * User login.
     */
    private String login;

    /**
     * User email.
     */
    private String email;

    /**
     * User access.
     */
    private Access access;

    /**
     * Avatar image name.
     */
    private String avatarImageName;

    /**
     * Avatar image.
     */
    private byte[] avatarImage;

    public byte[] getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(byte[] avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getAvatarImageName() {
        return avatarImageName;
    }

    public void setAvatarImageName(String avatarImageName) {
        this.avatarImageName = avatarImageName;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
