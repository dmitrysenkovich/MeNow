package com.sam_solutions.app.dto;

import com.sam_solutions.app.validator.Phone;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User DTO in representation in sign up.
 */
public class UserSignUpDto {
    /**
     * User nick. Must be set while singing up.
     */
    @NotNull
    @Size(min = 4, max = 15, message = "{sign_up.nick.size}")
    private String nick;

    /**
     * User email. Must be valid.
     */
    @NotNull
    @Email(message = "{sign_up.email.valid}")
    private String email;

    /**
     * User phone number.
     */
    @NotNull
    @Size(min = 8, max = 16, message = "{sign_up.phone_number.size}")
    @Phone(message = "{sign_up.phone_number.size}")
    private String phoneNumber;

    /**
     * User login. Must be unique.
     */
    @NotNull
    @Size(min = 4, max = 32, message = "{sign_up.login.size}")
    private String login;

    /**
     * Password.
     */
    @NotNull
    @Size(min = 4, max = 64, message = "{sign_up.password.size}")
    private String password;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
