package com.sam_solutions.app.dto;

/**
 * User DTO representation in followings.
 */
public class UserFollowingDto {
    /**
     * User login.
     */
    private String login;

    /**
     * User nick.
     */
    private String nick;

    /**
     * Avatar image file name.
     */
    private String avatarImageName;

    /**
     * Avatar image.
     */
    private byte[] avatarImage;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatarImageName() {
        return avatarImageName;
    }

    public void setAvatarImageName(String avatarImageName) {
        this.avatarImageName = avatarImageName;
    }

    public byte[] getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(byte[] avatarImage) {
        this.avatarImage = avatarImage;
    }
}
