package com.sam_solutions.app.dto;

/**
 * User representation in search.
 */
public class UserSearchDto {
    /**
     * User id.
     */
    private Long id;

    /**
     * Login.
     */
    private String login;

    /**
     * Nick.
     */
    private String nick;

    /**
     * Status
     */
    private String status;

    /**
     * Avatar image name.
     */
    private String avatarImageName;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatarImageName() {
        return avatarImageName;
    }

    public void setAvatarImageName(String avatarImageName) {
        this.avatarImageName = avatarImageName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
