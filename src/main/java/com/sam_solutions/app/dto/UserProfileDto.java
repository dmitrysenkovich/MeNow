package com.sam_solutions.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User DTO representation in profile.
 */
public class UserProfileDto {
    /**
     * User login.
     */
    private String login;

    /**
     * User nick.
     */
    @NotNull
    @Size(min = 4, max = 15)
    private String nick;

    /**
     * Status.
     */
    @Size(max = 70)
    private String status;

    /**
     * Avatar image file name.
     */
    private String avatarImageName;

    public String getAvatarImageName() {
        return avatarImageName;
    }

    public void setAvatarImageName(String avatarImageName) {
        this.avatarImageName = avatarImageName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
