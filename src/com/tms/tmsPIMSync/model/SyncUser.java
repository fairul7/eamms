package com.tms.tmsPIMSync.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Jun 25, 2006
 * Time: 7:11:17 PM
 * Data Object which represent a tmsPIMSync User
 */
public class SyncUser implements Serializable {
    private String username;
    private String password;
    private String email;
    private String first_name;
    private String last_name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }


}
