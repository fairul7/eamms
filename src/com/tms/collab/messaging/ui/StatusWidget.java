package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;
import kacang.Application;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class StatusWidget extends Widget {
    public static final long DEFAULT_CHECK_MAIL_DELAY = 1000*60*3; // three minutes
    public static final String DEFAULT_MAX_MESSAGES = "5";

    private long checkMailDelay;    // in millies

    private long lastCheck; // in millies
    private int emailCount;
    private int accountCount;

    public void init() {
        super.init();
        lastCheck = 0;
        emailCount = 0;
        accountCount = 0;
    }

    public void onRequest(Event event) {
        MessagingModule mm;
        User user;

        // make sure user has activated messaging module
        mm = Util.getMessagingModule();
        user = Util.getUser(event);
        try {
            if(mm.getIntranetAccountByUserId(user.getId())==null) {
                event.getRequest().setAttribute("notActivated", Boolean.TRUE);
                return;
            }
        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

        // TODO: set checkMailDelay from preferences
        checkMailDelay = DEFAULT_CHECK_MAIL_DELAY;

        checkNewMail(event);
    }

    private void checkNewMail(Event event) {
        boolean shouldCheck = true;

        if(checkMailDelay==0) {
            // never check POP3 emails
            shouldCheck = false;
        }

        long now = System.currentTimeMillis();

        if (!((lastCheck+checkMailDelay) < now)) {
            // not time to check yet
            shouldCheck = false;
        }

        accountCount = 0;
        emailCount = 0;

        MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        User user = user = Util.getUser(event);
        try {
            Collection accounts = mm.getAccounts(user.getId());
            for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
                Account account = (Account) iterator.next();
                if(account instanceof Pop3Account) {
                    Pop3Account pop3Account = (Pop3Account) account;
                    if(shouldCheck) {
                        // queue this userId for checking emails
                        mm.checkPop3Messages(user.getId());
                        lastCheck = System.currentTimeMillis();
                    }
                    accountCount += 1;
                    emailCount += pop3Account.getCheckCount();
                }
            }

        } catch (MessagingException e) {
            Log.getLog(getClass()).error("Error getting user's messaging accounts", e);
        }
    }

    public String getTemplate() {
        return ""; // no template
    }

    public Date getLastCheckDate() {
        return new Date(lastCheck);
    }

    // === [ getters/setters ] =================================================
    public long getCheckMailDelay() {
        return checkMailDelay;
    }

    public void setCheckMailDelay(long checkMailDelay) {
        this.checkMailDelay = checkMailDelay;
    }

    public long getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(long lastCheck) {
        this.lastCheck = lastCheck;
    }

    public int getEmailCount() {
        return emailCount;
    }

    public void setEmailCount(int emailCount) {
        this.emailCount = emailCount;
    }

    public int getAccountCount() {
        return accountCount;
    }

    public void setAccountCount(int accountCount) {
        this.accountCount = accountCount;
    }

}
