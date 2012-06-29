package com.tms.collab.messaging.model;

import kacang.model.DefaultDataObject;

/**
 * Data object to represent an SMTP account.
 */
public class SmtpAccount extends DefaultDataObject {
    private String smtpAccountId;
    private String userId;
    private String name;
    private String serverName;
    private int serverPort;
    private boolean anonymousAccess;
    private String username;
    private String password;

    /**
     * SMTP account's ID.
     * @return
     */
    public String getId() {
        return getSmtpAccountId();
    }

    /**
     * SMTP account's ID.
     * @param s
     */
    public void setId(String s) {
        setSmtpAccountId(s);
    }


    // === [ getters/setters ] =================================================
    /**
     * SMTP account's ID.
     * @return
     */
    public String getSmtpAccountId() {
        return smtpAccountId;
    }

    /**
     * SMTP account's ID.
     * @param smtpAccountId
     */
    public void setSmtpAccountId(String smtpAccountId) {
        this.smtpAccountId = smtpAccountId;
    }

    /**
     * UserId that this SMTP account belongs to.
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * UserId that this SMTP account belongs to.
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Name of this SMTP account.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Name of this SMTP account.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * SMTP server name.
     * @return
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * SMTP server name.
     * @param serverName
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * SMTP server port number.
     * @return
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * SMTP server port number.
     * @param serverPort
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Flag to indicate whether this SMTP account will assume an anonymous
     * user or not. If true, the username/password should be ignored.
     * @return
     */
    public boolean isAnonymousAccess() {
        return anonymousAccess;
    }

    /**
     * Flag to indicate whether this SMTP account will assume an anonymous
     * user or not. If true, the username/password should be ignored.
     * @param anonymousAccess
     */
    public void setAnonymousAccess(boolean anonymousAccess) {
        this.anonymousAccess = anonymousAccess;
    }

    /**
     * Username to use for the SMTP account. Should only be used for
     * non-anonymous connections.
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Username to use for the SMTP account. Should only be used for
     * non-anonymous connections.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Password to use for the SMTP account. Should only be used for
     * non-anonymous connections.
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Password to use for the SMTP account. Should only be used for
     * non-anonymous connections.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
