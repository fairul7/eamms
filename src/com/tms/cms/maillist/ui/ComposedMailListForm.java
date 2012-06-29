package com.tms.cms.maillist.ui;

import com.tms.cms.maillist.model.*;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.*;

public class ComposedMailListForm extends Form {

    private boolean formSaved;
    private boolean newForm;
    private String id;

    private ComposedMailList mailList;

    private TextField ffName;
    private TextBox ffDescription;
    private Radio ffHtmlYes, ffHtmlNo;
    private TextField ffSenderEmail;
    private TextField ffSubject;
    private TextBox ffRecipientsEmail;
    private ComboSelectBox ffGroups;
    private SelectBox ffTemplateId;
    private TextBox ffBody;

    // === [ constructors ] ====================================================
    public ComposedMailListForm() {
    }

    public ComposedMailListForm(String name) {
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
        mailList = new ComposedMailList();
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

        ffSenderEmail = new TextField("ffSenderEmail");
        ffSenderEmail.addChild(new ValidatorNotEmpty("fvSenderEmail"));
        addChild(ffSenderEmail);

        ffSubject = new TextField("ffSubject");
        ffSubject.addChild(new ValidatorNotEmpty("fvSubject"));
        addChild(ffSubject);

        ffRecipientsEmail = new TextBox("ffRecipientsEmail");
        addChild(ffRecipientsEmail);

        ffGroups = new ComboSelectBox("ffGroups");
        addChild(ffGroups);
        ffGroups.init();
        Map map = new HashMap();
        populateLeftGroups(map);
        ffGroups.setLeftValues(map);

        ffTemplateId = new SelectBox("ffTemplateId");
        MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
        populateFfTemplateId(module, ffTemplateId);
        addChild(ffTemplateId);

        ffBody = new RichTextBox("ffBody");
        addChild(ffBody);
    }

    public Forward onValidate(Event evt) {
        Log log = Log.getLog(this.getClass());

        mailList.setId(getId());
        mailList.setName((String)ffName.getValue());
        mailList.setDescription((String)ffDescription.getValue());
        mailList.setHtml(ffHtmlYes.isChecked());
        mailList.setSenderEmail((String)ffSenderEmail.getValue());
        mailList.setSubject((String)ffSubject.getValue());
        mailList.setRecipientsEmail((String)ffRecipientsEmail.getValue());
        setRecipientsEmailGroupIds(mailList, ffGroups);

        // get first selected value
        Map m = ffTemplateId.getSelectedOptions();
        mailList.setTemplateId((String)m.keySet().iterator().next());

        mailList.setMailListType(MailList.MAIL_LIST_TYPE_COMPOSED);

        mailList.setBody((String)ffBody.getValue());

        try {
            MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
            if(newForm) {
                module.createMailList(mailList);
                log.info("ComposedMailList created " + getId());
            } else {
                module.updateMailList(mailList);
                log.info("ComposedMailList updated " + getId());
            }
            formSaved = true;
            evt.setType("saved");
        } catch(MailListException e) {
            log.error("Error saving MailList " + getId(), e);
        }

        return super.onValidate(evt);
    }

    public Forward onValidationFailed(Event evt) {
        formSaved = false;
        return super.onValidationFailed(evt);
    }

    public String getTemplate() {
        return "maillist/composedMailListForm";
    }

    protected static void populateLeftGroups(Map map) {
        try {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection list = ss.getGroups(new DaoQuery(), 0, -1, null, false);
            Group group;
            for(Iterator iter = list.iterator(); iter.hasNext();) {
                group = (Group) iter.next();
                map.put(group.getId(), group.getGroupName());
            }
        } catch(SecurityException e) {
            Log.getLog(ComposedMailListForm.class).error("Error getting groups from security service", e);
        }
    }

    protected static void setRecipientsEmailGroupIds(MailList mailList, ComboSelectBox ffGroups) {
        for(Iterator iter=ffGroups.getRightValues().keySet().iterator(); iter.hasNext(); ) {
            mailList.getRecipientList().add("{" + iter.next() + "}");
        }
    }

    protected static void setFfGroupLeftValues(MailList mailList, ComboSelectBox ffGroups) {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        DaoQuery properties = new DaoQuery();
        properties.addProperty(new OperatorIn("id", mailList.getRecipientsEmailGroupIds(), DaoOperator.OPERATOR_NAN));
        try {
            Collection list = service.getGroups(properties, 0, -1, null, false);
            Map m = new HashMap();
            Group g;
            for(Iterator i = list.iterator(); i.hasNext();) {
                g = (Group) i.next();
                m.put(g.getId(), g.getGroupName());
            }
            ffGroups.setLeftValues(m);

        } catch(SecurityException e) {
            Log.getLog(ComposedMailListForm.class).error(e.getMessage(), e);
        }
    }

    protected static void setFfGroupRightValues(MailList mailList, ComboSelectBox ffGroups) {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        DaoQuery properties = new DaoQuery();

        if(mailList.getRecipientsEmailGroupIds().length ==0) {
            return;
        }

        properties.addProperty(new OperatorIn("id", mailList.getRecipientsEmailGroupIds(), DaoOperator.OPERATOR_AND));
        try {
            Collection list = service.getGroups(properties, 0, -1, null, false);
            Map m = new HashMap();
            Group g;
            for(Iterator i = list.iterator(); i.hasNext();) {
                g = (Group) i.next();
                m.put(g.getId(), g.getGroupName());
            }
            ffGroups.setRightValues(m);

        } catch(SecurityException e) {
            Log.getLog(ComposedMailListForm.class).error(e.getMessage(), e);
        }
    }

    protected static void populateFfTemplateId(MailListModule module, SelectBox ffTemplateId) {
        MailTemplate template;
        String value;

        try {
            ffTemplateId.addOption("", "-No Template-");
            Collection col = module.getMailTemplates("*", "name", false, 0, -1);
            for(Iterator iter=col.iterator(); iter.hasNext(); ) {
                template = (MailTemplate) iter.next();
                value = template.getName();
                value += template.isHtml() ? " (HTML)" : " (Text)";
                ffTemplateId.addOption(template.getId(), value);
            }
        } catch(MailListException e) {
            Log.getLog(ComposedMailListForm.class).error(e.getMessage(), e);
        }

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
            mailList = (ComposedMailList) module.getMailList(id);
            newForm = false;

            ffName.setValue(mailList.getName());
            ffDescription.setValue(mailList.getDescription());
            if(mailList.isHtml()) {
                ffHtmlYes.setChecked(true);
                ffHtmlNo.setChecked(false);
            } else {
                ffHtmlYes.setChecked(false);
                ffHtmlNo.setChecked(true);
            }
            ffSenderEmail.setValue(mailList.getSenderEmail());
            ffSubject.setValue(mailList.getSubject());
            ffRecipientsEmail.setValue(mailList.getRecipientsEmailWithoutGroupId());
            setFfGroupLeftValues(mailList, ffGroups);
            setFfGroupRightValues(mailList, ffGroups);

            List list = new ArrayList();
            list.add(mailList.getTemplateId());
            ffTemplateId.setValue(list);

            ffBody.setValue(mailList.getBody());

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

    public TextBox getFfBody() {
        return ffBody;
    }

    public void setFfBody(TextBox ffBody) {
        this.ffBody = ffBody;
    }

    public TextBox getFfRecipientsEmail() {
        return ffRecipientsEmail;
    }

    public void setFfRecipientsEmail(TextBox ffRecipientsEmail) {
        this.ffRecipientsEmail = ffRecipientsEmail;
    }

    public ComboSelectBox getFfGroups() {
        return ffGroups;
    }

    public void setFfGroups(ComboSelectBox ffGroups) {
        this.ffGroups = ffGroups;
    }

    public TextField getFfSenderEmail() {
        return ffSenderEmail;
    }

    public void setFfSenderEmail(TextField ffSenderEmail) {
        this.ffSenderEmail = ffSenderEmail;
    }

    public TextField getFfSubject() {
        return ffSubject;
    }

    public void setFfSubject(TextField ffSubject) {
        this.ffSubject = ffSubject;
    }

    public SelectBox getFfTemplateId() {
        return ffTemplateId;
    }

    public void setFfTemplateId(SelectBox ffTemplateId) {
        this.ffTemplateId = ffTemplateId;
    }

    public ComposedMailList getMailList() {
        return mailList;
    }

    public void setMailList(ComposedMailList mailList) {
        this.mailList = mailList;
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
