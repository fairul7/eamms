package com.tms.cms.maillist.ui;

import com.tms.cms.maillist.model.MailListException;
import com.tms.cms.maillist.model.MailListModule;
import com.tms.cms.maillist.model.MailLog;
import kacang.Application;
import kacang.stdui.Form;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class MailLogForm extends Form {

    private boolean newForm;
    private String id;

    private MailLog mailLog;

    // === [ constructors ] ====================================================
    public MailLogForm() {
    }

    public MailLogForm(String name) {
        this();
        setName(name);
    }


    // === [ widgets ] =========================================================
    public void init() {
        removeChildren();
        setInvalid(false);

        // reset as newForm
        newForm = true;
        mailLog = new MailLog();
        id = UuidGenerator.getInstance().getUuid();

    }

    public String getTemplate() {
        return "maillist/mailLogForm";
    }


    // === [ getters & setters ] ===============================================
    public String getId() {
        return id;
    }

    /**
     * Special setter for form.
     * @param id
     */
    public void setId(String id) {
        try {
            init();
            this.id = id;
            MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
            mailLog = module.getMailLog(id);
            newForm = false;

        } catch(MailListException e) {
            Log log = Log.getLog(this.getClass());
            log.error(e);
            init();
        }
    }

    public MailLog getMailLog() {
        return mailLog;
    }

    public void setMailLog(MailLog mailLog) {
        this.mailLog = mailLog;
    }

    public boolean isNewForm() {
        return newForm;
    }

    public void setNewForm(boolean newForm) {
        this.newForm = newForm;
    }

}
