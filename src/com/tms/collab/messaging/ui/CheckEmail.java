package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.util.Iterator;
import java.util.Collection;

/**
 * This is ACTUALLY DOWNLOAD POP3 emails.
 */
public class CheckEmail extends LightWeightWidget {
    private int accountCount;

    public void onRequest(Event event) {
        MessagingModule mm;
        Collection accounts;

        accountCount = 0;
        try {
            mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            accounts = mm.getAccounts(Util.getUser(event).getId());

            for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
                Account account = (Account) iterator.next();

                if(account instanceof  Pop3Account) {
                    accountCount++;
                }
            }

            mm.downloadPop3Messages(Util.getUser(event).getId());

        } catch (MessagingException e) {
            Log.getLog(getClass()).error("Error downloading POP3 emails", e);
        }

    }

    public String getTemplate() {
        return "messaging/checkEmail";
    }

    // === [ getters/setters ] =================================================
    public int getAccountCount() {
        return accountCount;
    }

    public void setAccountCount(int accountCount) {
        this.accountCount = accountCount;
    }

}
