package com.tms.collab.messaging.model;

import java.util.Date;

/**
 * Data object to represent an intranet messaging account.
 *
 */
public class Pop3Account extends Account {
    private String serverName;
    private int serverPort;
    private String username;
    private String password;
    private boolean leaveMailOnServer;

    private Date lastCheckDate;
    private int checkCount;
    private Date lastDownloadDate;
    private int downloadCount;

    private String accountLog;

    public int getAccountType() {
        return ACCOUNT_TYPE_POP3;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

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

    public boolean isLeaveMailOnServer() {
        return leaveMailOnServer;
    }

    public void setLeaveMailOnServer(boolean leaveMailOnServer) {
        this.leaveMailOnServer = leaveMailOnServer;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
    }

    public Date getLastDownloadDate() {
        return lastDownloadDate;
    }

    public void setLastDownloadDate(Date lastDownloadDate) {
        this.lastDownloadDate = lastDownloadDate;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getAccountLog() {
        return accountLog;
    }

    public void setAccountLog(String accountLog) {
        this.accountLog = accountLog;
    }
}
