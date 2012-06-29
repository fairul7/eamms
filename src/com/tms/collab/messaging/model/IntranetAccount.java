package com.tms.collab.messaging.model;

/**
 * Data object to represent an intranet messaging account.
 * <p>
 * The <code>fromAddress</code> is the <i>Internet email address</i> used to
 * represent the user, when sending emails to Internet email users.
 * <p>
 * The <code>intranetUsername</code> is the intranet messaging account's
 * username.
 */
public class IntranetAccount extends Account {
    private String fromAddress;
    private String intranetUsername;
    private String signature;

    public int getAccountType() {
        return ACCOUNT_TYPE_INTRANET;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getIntranetUsername() {
        return intranetUsername;
    }

    public void setIntranetUsername(String intranetUsername) {
        this.intranetUsername = intranetUsername;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
