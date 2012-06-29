package com.tms.collab.messaging.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import com.tms.collab.messaging.model.*;

public class EditPop3AccountForm extends NewPop3AccountForm {
    private String accountId;

    public void onRequest(Event event) {
        MessagingModule mm;
        Pop3Account pop3;

        accountId = event.getRequest().getParameter("accountId");
        if(accountId==null) {
            Log.getLog(getClass()).error("AccountId not specified");
            return;
        }

        try {
            mm = Util.getMessagingModule();
            pop3 = (Pop3Account) mm.getAccount(accountId);

            getTfName().setValue(pop3.getName());
            getSbIndicator().setSelectedOptions(
                    new String[] {
                        Integer.toString(pop3.getIndicator())
                    }
            );
            
            
            // NOTE: Commented out cause we no longer have a SbDeliveryFolerId
            //       Select Box
            // NOTE: uncommented due to (enhancement) BUG #2308
            getSbDeliveryFolderId().setSelectedOptions(
                    new String[] {
                        pop3.getDeliveryFolderId()
                    }
            );
            
            getTfServerName().setValue(pop3.getServerName());
            getTfServerPort().setValue(Integer.toString(pop3.getServerPort()));
            getTfUsername().setValue(pop3.getUsername());
            getPwPassword().setValue(pop3.getPassword());
            getCbLeaveMailOnServer().setChecked(pop3.isLeaveMailOnServer());

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            return;
        }
    }

    public Forward onValidate(Event event) {
        MessagingModule mm;
        Pop3Account pop3;

        if (accountId == null) {
            // accountId should be set by previous onRequest()
            String msg = "AccountId not specified";
            Log.getLog(getClass()).error(msg);
            event.getRequest().getSession().setAttribute("error", msg);
            return new Forward(FORWARD_ERROR);
        }

        try {
            mm = Util.getMessagingModule();
            pop3 = (Pop3Account) mm.getAccount(accountId);
            setPop3AccountAttributes(pop3);

            mm.updateAccount(pop3);
            return new Forward(FORWARD_SUCCESS);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }
    }

    // === [ getters/setters ] =================================================
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
