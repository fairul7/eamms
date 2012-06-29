package com.tms.cms.maillist.ui;

import com.tms.cms.maillist.model.ComposedMailList;
import com.tms.cms.maillist.model.MailList;
import com.tms.cms.maillist.model.MailListException;
import com.tms.cms.maillist.model.MailListModule;
import kacang.Application;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

public class MailListSend extends LightWeightWidget {
    private Log log = Log.getLog(this.getClass());

    private MailListModule module;
    private MailList mailList;
    private static boolean busy;    // busy is a static!
    private String message;

    // === [ constructors ] ====================================================
    public MailListSend() {
        module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
    }

    public MailListSend(String name) {
        this();
        setName(name);
    }


    // === [ widgets ] =========================================================
    public String getTemplate() {
        return "maillist/mailListSend";
    }


    // === [ getters & setters ] ===============================================
    /**
     * Special setter for form.
     * @param id
     */
    public void setId(String id) {
        if(isBusy()) {
            // if busy, don't send
            return;

        } else {

	        Application application = Application.getInstance();
            try {
                setBusy(true);
                mailList = module.getMailList(id);
                module.sendMailingList(mailList);
                setMessage(application.getMessage("maillist.message.mailingListProcessed", "Send mailing list processed. Please check mailing list log"));

            } catch(MailListException e) {
                log.error(e);
                mailList = new ComposedMailList();
                setMessage(application.getMessage("maillist.message.mailingListSendError", "Error occurred while sending mailing list. Please check mailing list log"));
            } finally {
                setBusy(false);
            }
        }
    }

    public MailListModule getModule() {
        return module;
    }

    public void setModule(MailListModule module) {
        this.module = module;
    }

    public MailList getMailList() {
        return mailList;
    }

    public void setMailList(MailList mailList) {
        this.mailList = mailList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

}
