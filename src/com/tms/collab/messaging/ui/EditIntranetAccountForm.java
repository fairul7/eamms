package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.Application;

import java.util.List;

public class EditIntranetAccountForm extends ActivateForm {
    private IntranetAccount intranetAccount;
    private SmtpAccount smtpAccount;

    
    public String getDefaultTemplate() {
    	return "messaging/editIntranetAccount";
    }
    
    
    
    public void init() {
        MessagingModule mm;

        super.init();
        try {
            String userId = getWidgetManager().getUser().getId();

            mm = Util.getMessagingModule();
            intranetAccount = mm.getIntranetAccountByUserId(userId);
            smtpAccount = mm.getSmtpAccountByUserId(userId);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public void onRequest(Event event) {
        MessagingModule mm;

        try {
            String userId = Util.getUser(event).getId();

            mm = Util.getMessagingModule();
            intranetAccount = mm.getIntranetAccountByUserId(userId);
            smtpAccount = mm.getSmtpAccountByUserId(userId);

            // update form fields values
            getFromAddress().setValue(intranetAccount.getFromAddress());
            getIndicator().setSelectedOptions(
                    new String[]{
                        Integer.toString(intranetAccount.getIndicator())
                    }
            );
            getSignature().setValue(intranetAccount.getSignature());
            getSmtpServer().setValue(smtpAccount.getServerName());
            getSmtpPort().setValue(Integer.toString(smtpAccount.getServerPort()));
            getAnonymous().setChecked(smtpAccount.isAnonymousAccess());
            getUsername().setValue(smtpAccount.getUsername());
            getPassword().setValue(smtpAccount.getPassword());

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public Forward onValidate(Event event) {
        User user = Util.getUser(event);

        if (!Util.hasIntranetAccount(event)) {
            // account does not exist
            String msg = Application.getInstance().getMessage("messaging.label.Intranetaccountdoesnotexist","Cannot update intranet account. Intranet account does not exist");
            Log.getLog(this.getClass()).error(msg);
            event.getRequest().getSession().setAttribute("error", msg);
            return new Forward(FORWARD_ERROR);
        }

        MessagingModule mm;
        try {
            mm = Util.getMessagingModule();
            // load user's intranet and smtp account
            intranetAccount = mm.getIntranetAccountByUserId(user.getId());
            smtpAccount = mm.getSmtpAccountByUserId(user.getId());

            // update user's intranet username
            intranetAccount.setIntranetUsername(user.getUsername());
            intranetAccount.setFilterEnabled(false);
            intranetAccount.setFromAddress(getFromAddress().getValue().toString());
            int indi;
            try {
                List valueList = (List) getIndicator().getValue();
                indi = Integer.parseInt(valueList.get(0).toString());
            } catch(Exception e) {
                indi = 1;
            }
            intranetAccount.setIndicator(indi);
            intranetAccount.setSignature(getSignature().getValue().toString());

            smtpAccount.setServerName(getSmtpServer().getValue().toString());
            try {
                smtpAccount.setServerPort(Integer.parseInt(getSmtpPort().getValue().toString()));
            } catch (NumberFormatException e) {
                // default port number
                smtpAccount.setServerPort(25);
            }
            smtpAccount.setAnonymousAccess(getAnonymous().isChecked());
            smtpAccount.setUsername(getUsername().getValue().toString());
            smtpAccount.setPassword(getPassword().getValue().toString());

            // update the intranet account
            mm.updateAccount(intranetAccount);
            mm.updateSmtpAccount(smtpAccount);
            return new Forward(FORWARD_SUCCESS);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }
    }

    // === [ getters/setters ] =================================================
    public IntranetAccount getIntranetAccount() {
        return intranetAccount;
    }

    public void setiAccount(IntranetAccount iAccount) {
        this.intranetAccount = iAccount;
    }

    public SmtpAccount getSmtpAccount() {
        return smtpAccount;
    }

    public void setSmtpAccount(SmtpAccount smtpAccount) {
        this.smtpAccount = smtpAccount;
    }
}
