package com.sam_solutions.app.model;

import com.sam_solutions.app.utils.FileUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Representation of users table.
 */
@Entity
@Table(name = "USERS")
public class User implements Cloneable {
    /**
     * Defaults for status and avatar.
     */
    public static final String DEFAULT_AVATAR_IMAGE_NAME = "default_avatar.jpg";

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * User nick.
     */
    @Column(name = "NICK")
    private String nick;

    /**
     * User email.
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * User status.
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * User login.
     */
    @Column(name = "LOGIN")
    private String login;

    /**
     * Password.
     */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * Phone number.
     */
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    /**
     * Whether to notify
     * user via sms.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NOTIFY")
    private Notification notification;

    /**
     * Used to restrict access
     * to application.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ACCESS")
    private Access access;

    /**
     * There are two roles: admins and users.
     * Different functional is available for
     * each of them.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    /**
     * Used to manage default permissions
     * for users on viewing another user
     * accounts.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "PERMISSION_TYPE")
    private PermissionType permissionType;

    /**
     * Avatar image file name.
     */
    @Column(name = "AVATAR_IMAGE_NAME")
    private String avatarImageName;

    /**
     * Raw avatar image.
     */
    @Lob
    @Column(name = "AVATAR_IMAGE", columnDefinition = "LONGBLOB")
    private byte[] avatarImage;

    /**
     * User posts.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Post> posts;

    /**
     * User likes
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Like> likes;

    /**
     * Profile owners relations.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<UserRelation> ownerUserRelations;

    /**
     * Profile viewers relations.
     */
    @OneToMany(mappedBy = "viewer", cascade = CascadeType.ALL)
    private Set<UserRelation> viewerUserRelations;

    /**
     * Initially every user has access
     * to application, his/her type is USER
     * and default permissions - account is
     * visible for everyone.
     */
    public User() {
        this.notification = Notification.NOT_NOTIFY;
        this.access = Access.ALLOWED;
        this.role = Role.USER;
        this.permissionType = PermissionType.ALL;
        this.avatarImageName = DEFAULT_AVATAR_IMAGE_NAME;
        FileUtils fileUtils = new FileUtils();
        String avatarPath = FileUtils.RESOURCES_PATH + FileUtils.USERS_PATH + this.avatarImageName;
        this.avatarImage = fileUtils.loadImage(avatarPath);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
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

    public Set<Post> getPosts() {
        return posts;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public Set<UserRelation> getOwnerUserRelations() {
        return ownerUserRelations;
    }

    public Set<UserRelation> getViewerUserRelations() {
        return viewerUserRelations;
    }
}
