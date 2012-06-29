package com.tms.cms.maillist.ui;

import com.tms.cms.maillist.model.MailListException;
import com.tms.cms.maillist.model.MailListModule;
import com.tms.cms.maillist.model.MailTemplate;
import kacang.Application;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class MailTemplateForm extends Form{

    private boolean formSaved;
    private boolean newForm;
    private String id;

    private MailTemplate mailTemplate;

    private TextField ffName;
    private TextBox ffDescription;
    private Radio ffHtmlYes, ffHtmlNo;
    private TextBox ffHeader;
    private TextBox ffFooter;

    // === [ constructors ] ====================================================

    public MailTemplateForm() {
    }

    public MailTemplateForm(String name) {
        this();
        setName(name);
    }


    // === [ widgets ] =========================================================
    public void init() {
        removeChildren();
        setInvalid(false);

        formSaved = false;

        // reset as newForm
        newForm = true;
        mailTemplate = new MailTemplate();
        id = UuidGenerator.getInstance().getUuid();

        // add childs
        ffName = new TextField("ffName");
        ffName.addChild(new ValidatorNotEmpty("fvName"));
        addChild(ffName);

        ffDescription = new TextBox("ffDescription");
        addChild(ffDescription);

        Application application = Application.getInstance();
        ffHtmlYes = new Radio("ffHtmlYes");
        ffHtmlYes.setGroupName("ffHtml");
        ffHtmlYes.setText(application.getMessage("general.label.yes", "Yes"));
        ffHtmlYes.setChecked(true);
        addChild(ffHtmlYes);

        ffHtmlNo = new Radio("ffHtmlNo");
        ffHtmlNo.setGroupName("ffHtml");
        ffHtmlNo.setText(application.getMessage("general.label.no", "No"));
        ffHtmlNo.setChecked(false);
        addChild(ffHtmlNo);

        ffHeader = new TextBox("ffHeader");
        addChild(ffHeader);

        ffFooter = new TextBox("ffFooter");
        addChild(ffFooter);
    }

    public Forward onValidate(Event evt) {
        Log log = Log.getLog(this.getClass());
        mailTemplate.setId(getId());
        mailTemplate.setName((String)ffName.getValue());
        mailTemplate.setDescription((String)ffDescription.getValue());
        mailTemplate.setHtml(ffHtmlYes.isChecked());
        mailTemplate.setHeader((String)ffHeader.getValue());
        mailTemplate.setFooter((String)ffFooter.getValue());

        try {
            MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
            if(newForm) {
                module.createMailTemplate(mailTemplate);
                log.info("MailTemplate created " + getId());
            } else {
                module.updateMailTemplate(mailTemplate);
                log.info("MailTemplate updated " + getId());
            }
            formSaved = true;
            evt.setType("saved");

        } catch(MailListException e) {
            log.error("Error saving MailTemplate " + getId(), e);
        }

        return super.onValidate(evt);
    }

    public Forward onValidationFailed(Event evt) {
        formSaved = false;
        return super.onValidationFailed(evt);
    }

    public String getTemplate() {
        return "maillist/mailTemplateForm";
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
            mailTemplate = module.getMailTemplate(id);
            newForm = false;

            ffName.setValue(mailTemplate.getName());
            ffDescription.setValue(mailTemplate.getDescription());
            if(mailTemplate.isHtml()) {
                ffHtmlYes.setChecked(true);
                ffHtmlNo.setChecked(false);
            } else {
                ffHtmlYes.setChecked(false);
                ffHtmlNo.setChecked(true);
            }
            ffHeader.setValue(mailTemplate.getHeader());
            ffFooter.setValue(mailTemplate.getFooter());

        } catch(MailListException e) {
            Log log = Log.getLog(this.getClass());
            log.error(e);
            init();
        }
    }

    public TextBox getFfDescription() {
        return ffDescription;
    }

    public void setFfDescription(TextBox ffDescription) {
        this.ffDescription = ffDescription;
    }

    public Radio getFfHtmlNo() {
        return ffHtmlNo;
    }

    public void setFfHtmlNo(Radio ffHtmlNo) {
        this.ffHtmlNo = ffHtmlNo;
    }

    public Radio getFfHtmlYes() {
        return ffHtmlYes;
    }

    public void setFfHtmlYes(Radio ffHtmlYes) {
        this.ffHtmlYes = ffHtmlYes;
    }

    public TextField getFfName() {
        return ffName;
    }

    public void setFfName(TextField ffName) {
        this.ffName = ffName;
    }

    public TextBox getFfFooter() {
        return ffFooter;
    }

    public void setFfFooter(TextBox ffFooter) {
        this.ffFooter = ffFooter;
    }

    public TextBox getFfHeader() {
        return ffHeader;
    }

    public void setFfHeader(TextBox ffHeader) {
        this.ffHeader = ffHeader;
    }

    public MailTemplate getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public boolean isNewForm() {
        return newForm;
    }

    public void setNewForm(boolean newForm) {
        this.newForm = newForm;
    }

    public boolean isFormSaved() {
        return formSaved;
    }

    public void setFormSaved(boolean formSaved) {
        this.formSaved = formSaved;
    }

}
