package com.sam_solutions.app.dto;

/**
 * User DTO representation in feed.
 */
public class UserFeedDto {
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

    public byte[] getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(byte[] avatarImage) {
        this.avatarImage = avatarImage;
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
}
