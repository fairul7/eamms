package com.tms.collab.messaging.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Pop3Account;
import com.tms.collab.messaging.model.MessagingException;

import java.util.Date;
import java.util.Calendar;

public class Pop3AccountLog extends LightWeightWidget {
    private String accountId;
    private Pop3Account pop3Account;

    public void onRequest(Event event) {
        MessagingModule mm;

        if (accountId != null) {
            mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            try {
                pop3Account = (Pop3Account) mm.getAccount(accountId);

                if("1".equals(event.getParameter("clearLog"))) {
                    Date now = Calendar.getInstance().getTime();
                    pop3Account.setAccountLog(now + " : " + "Clear POP3 account log\n");
                    mm.updateAccount(pop3Account);
                }

            } catch (MessagingException e) {
                Log.getLog(getClass()).error("Cannot get POP3 account", e);
            }
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Pop3Account getPop3Account() {
        return pop3Account;
    }

    public void setPop3Account(Pop3Account pop3Account) {
        this.pop3Account = pop3Account;
    }

    public String getDefaultTemplate() {
        return "messaging/pop3AccountLog";
    }
}
