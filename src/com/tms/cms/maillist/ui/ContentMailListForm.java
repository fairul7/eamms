package com.tms.cms.maillist.ui;

import com.tms.cms.core.ui.ContentPopupSelectBox;
import com.tms.cms.maillist.model.ContentMailList;
import com.tms.cms.maillist.model.MailList;
import com.tms.cms.maillist.model.MailListException;
import com.tms.cms.maillist.model.MailListModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.*;

// TODO: SortableComboSelectBox don't work because template don't display childs
public class ContentMailListForm extends Form{

    private boolean formSaved;
    private boolean newForm;
    private String id;

    private ContentMailList mailList;

    private TextField ffName;
    private TextBox ffDescription;
    private Radio ffHtmlYes, ffHtmlNo;
    private TextField ffSenderEmail;
    private TextField ffSubject;
    private TextBox ffRecipientsEmail;
    private ComboSelectBox ffGroups;
    private SelectBox ffTemplateId;

    private TextBox ffHeader;
    private TextBox ffFooter;
    private ContentPopupSelectBox ffContentIds;

    // === [ constructors ] ====================================================
    public ContentMailListForm() {
    }

    public ContentMailListForm(String name) {
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
        mailList = new ContentMailList();
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
        ComposedMailListForm.populateLeftGroups(map);
        ffGroups.setLeftValues(map);

        ffTemplateId = new SelectBox("ffTemplateId");
        MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
        ComposedMailListForm.populateFfTemplateId(module, ffTemplateId);
        addChild(ffTemplateId);

        ffHeader = new TextBox("ffHeader");
        addChild(ffHeader);

        ffFooter = new TextBox("ffFooter");
        addChild(ffFooter);

        ffContentIds = new ContentPopupSelectBox("ffContentIds");
        ffContentIds.setShowParentFilter(false);
        addChild(ffContentIds);
        ffContentIds.init();

    }

/*
    private Map getContentIdMap() {
        String text;

        ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        Map leftMap = new SequencedHashMap();
        try {
            Collection co = cp.viewList(null, new String[]{Article.class.getName()}, null, null, Boolean.FALSE, null, false, 0, -1, null, null);
            for(Iterator iter = co.iterator(); iter.hasNext();) {
                ContentObject c = (ContentObject) iter.next();
                text = c.getName();
                if(text.length()>25) {
                    text = text.substring(0, 21) + "...";
                }
                leftMap.put(c.getId(), text);
            }
        } catch(ContentException e) {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }

        return leftMap;
    }
*/

    public Forward onValidate(Event evt) {
        Log log = Log.getLog(this.getClass());
        mailList.setId(getId());
        mailList.setName((String)ffName.getValue());
        mailList.setDescription((String)ffDescription.getValue());
        mailList.setHtml(ffHtmlYes.isChecked());
        mailList.setSenderEmail((String)ffSenderEmail.getValue());
        mailList.setSubject((String)ffSubject.getValue());
        mailList.setMailListType(MailList.MAIL_LIST_TYPE_CONTENT);
        mailList.setRecipientsEmail((String) ffRecipientsEmail.getValue());
        ComposedMailListForm.setRecipientsEmailGroupIds(mailList, ffGroups);

        // get first selected value
        Map m = ffTemplateId.getSelectedOptions();
        mailList.setTemplateId((String) m.keySet().iterator().next());

        mailList.setHeader((String)ffHeader.getValue());
        mailList.setFooter((String)ffFooter.getValue());

        mailList.setContentIdList(new ArrayList());
        String[] selectedIds = ffContentIds.getIds();
        for (int i=0; i<selectedIds.length; i++) {
            mailList.getContentIdList().add(selectedIds[i]);
        }

        try {
            MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
            if(newForm) {
                module.createMailList(mailList);
                log.info("ContentMailList created " + getId());
            } else {
                module.updateMailList(mailList);
                log.info("ContentMailList updated " + getId());
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
        return "maillist/contentMailListForm";
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
            mailList = (ContentMailList) module.getMailList(id);
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

            List list = new ArrayList();
            list.add(mailList.getTemplateId());
            ffTemplateId.setValue(list);

            ffRecipientsEmail.setValue(mailList.getRecipientsEmailWithoutGroupId());
            ComposedMailListForm.setFfGroupLeftValues(mailList, ffGroups);
            ComposedMailListForm.setFfGroupRightValues(mailList, ffGroups);

            ffHeader.setValue(mailList.getHeader());
            ffFooter.setValue(mailList.getFooter());

            // set contentIds
            String[] contentIds = (String[])mailList.getContentIdList().toArray(new String[0]);
            ffContentIds.setIds(contentIds);

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

    public TextBox getFfRecipientsEmail() {
        return ffRecipientsEmail;
    }

    public void setFfRecipientsEmail(TextBox ffRecipientsEmail) {
        this.ffRecipientsEmail = ffRecipientsEmail;
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

    public ComboSelectBox getFfGroups() {
        return ffGroups;
    }

    public void setFfGroups(ComboSelectBox ffGroups) {
        this.ffGroups = ffGroups;
    }

    public SelectBox getFfTemplateId() {
        return ffTemplateId;
    }

    public void setFfTemplateId(SelectBox ffTemplateId) {
        this.ffTemplateId = ffTemplateId;
    }

    public ContentPopupSelectBox getFfContentIds() {
        return ffContentIds;
    }

    public void setFfContentIds(ContentPopupSelectBox ffContentIds) {
        this.ffContentIds = ffContentIds;
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

    public ContentMailList getMailList() {
        return mailList;
    }

    public void setMailList(ContentMailList mailList) {
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
