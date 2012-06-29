package com.tms.tmsPIMSync.model;

import java.io.Serializable;

public class PIMSyncPrincipal implements Serializable {
    private String id;
    private String username;
    private String device;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

}
