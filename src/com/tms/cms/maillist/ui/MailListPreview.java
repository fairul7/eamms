package com.tms.cms.maillist.ui;

import com.tms.cms.maillist.model.ComposedMailList;
import com.tms.cms.maillist.model.MailList;
import com.tms.cms.maillist.model.MailListException;
import com.tms.cms.maillist.model.MailListModule;
import kacang.Application;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

public class MailListPreview extends LightWeightWidget {
    private Log log = Log.getLog(this.getClass());

    private MailListModule module;
    private MailList mailList;

    // === [ constructors ] ====================================================
    public MailListPreview() {
        module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
    }

    public MailListPreview(String name) {
        this();
        setName(name);
    }


    // === [ widgets ] =========================================================
    public String getTemplate() {
        return "maillist/mailListPreview";
    }


    // === [ getters & setters ] ===============================================
    /**
     * Special setter for form.
     * @param id
     */
    public void setId(String id) {
        try {
            mailList = module.getMailList(id);

        } catch(MailListException e) {
            log.error(e);
            mailList = new ComposedMailList();
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

}
