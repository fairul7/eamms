package com.tms.cms.maillist.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.section.Section;
import com.tms.cms.section.ui.SectionSelectBox;
import com.tms.cms.vsection.VSection;
import com.tms.cms.maillist.model.MailList;
import com.tms.cms.maillist.model.MailListException;
import com.tms.cms.maillist.model.MailListModule;
import com.tms.cms.maillist.model.ScheduledMailList;
import kacang.Application;
import kacang.services.scheduling.JobSchedule;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class ScheduledMailListForm extends Form{

    private boolean formSaved;
    private boolean newForm;
    private String id;

    private ScheduledMailList mailList;

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

    private Radio ffActiveYes, ffActiveNo;
    private DateField ffStartDate;
    private DateField ffEndDate;
    private SelectBox ffContentId;

    private Radio ffScheduleRepeatIntervalDaily, ffScheduleRepeatIntervalWeekly;
    private SelectBox ffScheduleDay;
    private TimeField ffScheduleStartTime;

    private TextBox ffUnsubscribedEmails;


    // === [ constructors ] ====================================================
    public ScheduledMailListForm() {
    }

    public ScheduledMailListForm(String name) {
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
        mailList = new ScheduledMailList();
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

        ffActiveYes = new Radio("ffActiveYes");
        ffActiveYes.setGroupName("ffActive");
        ffActiveYes.setText(application.getMessage("general.label.yes", "Yes"));
        ffActiveYes.setChecked(true);
        addChild(ffActiveYes);

        ffActiveNo = new Radio("ffActiveNo");
        ffActiveNo.setGroupName("ffActive");
        ffActiveNo.setText(application.getMessage("general.label.no", "No"));
        ffActiveNo.setChecked(false);
        addChild(ffActiveNo);

        ffStartDate = new DateField("ffStartDate");
        addChild(ffStartDate);

        ffEndDate = new DateField("ffEndDate");
        addChild(ffEndDate);

        ffContentId = new SectionSelectBox("ffContentId");
        ffContentId.setOptionMap(getContentIdMap());
        addChild(ffContentId);

        ffScheduleRepeatIntervalDaily = new Radio("ffScheduleRepeatIntervalDaily");
        ffScheduleRepeatIntervalDaily.setGroupName("ffScheduleRepeatInterval");
        ffScheduleRepeatIntervalDaily.setText(application.getMessage("general.label.daily", "Daily"));
        ffScheduleRepeatIntervalDaily.setChecked(true);
        addChild(ffScheduleRepeatIntervalDaily);

        ffScheduleRepeatIntervalWeekly = new Radio("ffScheduleRepeatIntervalWeekly");
        ffScheduleRepeatIntervalWeekly.setGroupName("ffScheduleRepeatInterval");
        ffScheduleRepeatIntervalWeekly.setText(application.getMessage("general.label.weekly", "Weekly"));
        ffScheduleRepeatIntervalWeekly.setChecked(false);
        addChild(ffScheduleRepeatIntervalWeekly);

        ffScheduleDay = new SelectBox("ffScheduleDay");
        ffScheduleDay.addOption(Integer.toString(JobSchedule.MONDAY), application.getMessage("general.label.monday", "Monday"));
        ffScheduleDay.addOption(Integer.toString(JobSchedule.TUESDAY), application.getMessage("general.label.tuesday", "Tuesday"));
        ffScheduleDay.addOption(Integer.toString(JobSchedule.WEDNESDAY), application.getMessage("general.label.wednesday", "Wednesday"));
        ffScheduleDay.addOption(Integer.toString(JobSchedule.THURSDAY), application.getMessage("general.label.thursday", "Thursday"));
        ffScheduleDay.addOption(Integer.toString(JobSchedule.FRIDAY), application.getMessage("general.label.friday", "Friday"));
        ffScheduleDay.addOption(Integer.toString(JobSchedule.SATURDAY), application.getMessage("general.label.saturday", "Saturday"));
        ffScheduleDay.addOption(Integer.toString(JobSchedule.SUNDAY), application.getMessage("general.label.sunday", "Sunday"));
        addChild(ffScheduleDay);

        ffScheduleStartTime = new TimeField("ffScheduleStartTime");
        addChild(ffScheduleStartTime);

        ffUnsubscribedEmails = new TextBox("ffUnsubscribedEmails");
        addChild(ffUnsubscribedEmails);
    }

    private Map getContentIdMap() {
        Map contentIdMap = new SequencedHashMap();

        ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        ContentObject c;
        try {
            Collection co = cp.viewList(null, new String[]{Section.class.getName(), VSection.class.getName()}, null, null, Boolean.FALSE, "name", false, 0, -1, null, null);
            for(Iterator iter = co.iterator(); iter.hasNext();) {
                c = (ContentObject) iter.next();
                contentIdMap.put(c.getId(), c.getName());
            }
        } catch(ContentException e) {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }

        return contentIdMap;
    }

    public Forward onValidate(Event evt) {
        Log log = Log.getLog(this.getClass());
        mailList.setId(getId());
        mailList.setName((String)ffName.getValue());
        mailList.setDescription((String)ffDescription.getValue());
        mailList.setHtml(ffHtmlYes.isChecked());
        mailList.setSenderEmail((String)ffSenderEmail.getValue());
        mailList.setSubject((String)ffSubject.getValue());
        mailList.setMailListType(MailList.MAIL_LIST_TYPE_SCHEDULED);
        mailList.setRecipientsEmail((String) ffRecipientsEmail.getValue());
        ComposedMailListForm.setRecipientsEmailGroupIds(mailList, ffGroups);

        // get first selected value
        Map m = ffTemplateId.getSelectedOptions();
        mailList.setTemplateId((String) m.keySet().iterator().next());

        mailList.setHeader((String)ffHeader.getValue());
        mailList.setFooter((String)ffFooter.getValue());

        mailList.setActive(ffActiveYes.isChecked());
        mailList.setStartDate(ffStartDate.getDate());
        mailList.setEndDate(ffEndDate.getDate());

        m = ffContentId.getSelectedOptions();
        mailList.setContentId((String)m.keySet().iterator().next());

        if(ffScheduleRepeatIntervalDaily.isChecked()) {
            mailList.setScheduleRepeatInterval(JobSchedule.DAILY);
        } else {
            mailList.setScheduleRepeatInterval(JobSchedule.WEEKLY);
        }

        // get first selected value
        m = ffScheduleDay.getSelectedOptions();
        mailList.setScheduleDay(Integer.parseInt((String)m.keySet().iterator().next()));

        mailList.setScheduleStartTime(ffScheduleStartTime.getDate());

        mailList.setUnsubscribedEmails((String)ffUnsubscribedEmails.getValue());

        try {
            MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
            if(newForm) {
                module.createMailList(mailList);
                log.info("ScheduledMailList created " + getId());
            } else {
                module.updateMailList(mailList);
                log.info("ScheduledMailList updated " + getId());
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
        return "maillist/scheduledMailListForm";
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
            mailList = (ScheduledMailList) module.getMailList(id);
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

            if(mailList.isActive()) {
                ffActiveYes.setChecked(true);
                ffActiveNo.setChecked(false);
            } else {
                ffActiveYes.setChecked(false);
                ffActiveNo.setChecked(true);
            }

            ffStartDate.setDate(mailList.getStartDate());
            ffEndDate.setDate(mailList.getEndDate());

            list = new ArrayList();
            list.add(mailList.getContentId());
            ffContentId.setValue(list);

            if(JobSchedule.DAILY.equals(mailList.getScheduleRepeatInterval())) {
                ffScheduleRepeatIntervalDaily.setChecked(true);
                ffScheduleRepeatIntervalWeekly.setChecked(false);
            } else {
                ffScheduleRepeatIntervalDaily.setChecked(false);
                ffScheduleRepeatIntervalWeekly.setChecked(true);
            }

            list = new ArrayList();
            list.add(new Integer(mailList.getScheduleDay()));
            ffScheduleDay.setValue(list);

            ffScheduleStartTime.setDate(mailList.getScheduleStartTime());

            ffUnsubscribedEmails.setValue(mailList.getUnsubscribedEmails());

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

    public Radio getFfActiveNo() {
        return ffActiveNo;
    }

    public void setFfActiveNo(Radio ffActiveNo) {
        this.ffActiveNo = ffActiveNo;
    }

    public Radio getFfActiveYes() {
        return ffActiveYes;
    }

    public void setFfActiveYes(Radio ffActiveYes) {
        this.ffActiveYes = ffActiveYes;
    }

    public SelectBox getFfContentId() {
        return ffContentId;
    }

    public void setFfContentId(SelectBox ffContentId) {
        this.ffContentId = ffContentId;
    }

    public DateField getFfEndDate() {
        return ffEndDate;
    }

    public void setFfEndDate(DateField ffEndDate) {
        this.ffEndDate = ffEndDate;
    }

    public DateField getFfStartDate() {
        return ffStartDate;
    }

    public void setFfStartDate(DateField ffStartDate) {
        this.ffStartDate = ffStartDate;
    }

    public SelectBox getFfScheduleDay() {
        return ffScheduleDay;
    }

    public void setFfScheduleDay(SelectBox ffScheduleDay) {
        this.ffScheduleDay = ffScheduleDay;
    }

    public Radio getFfScheduleRepeatIntervalDaily() {
        return ffScheduleRepeatIntervalDaily;
    }

    public void setFfScheduleRepeatIntervalDaily(Radio ffScheduleRepeatIntervalDaily) {
        this.ffScheduleRepeatIntervalDaily = ffScheduleRepeatIntervalDaily;
    }

    public Radio getFfScheduleRepeatIntervalWeekly() {
        return ffScheduleRepeatIntervalWeekly;
    }

    public void setFfScheduleRepeatIntervalWeekly(Radio ffScheduleRepeatIntervalWeekly) {
        this.ffScheduleRepeatIntervalWeekly = ffScheduleRepeatIntervalWeekly;
    }

    public TimeField getFfScheduleStartTime() {
        return ffScheduleStartTime;
    }

    public void setFfScheduleStartTime(TimeField ffScheduleStartTime) {
        this.ffScheduleStartTime = ffScheduleStartTime;
    }

    public TextBox getFfUnsubscribedEmails() {
        return ffUnsubscribedEmails;
    }

    public void setFfUnsubscribedEmails(TextBox ffUnsubscribedEmails) {
        this.ffUnsubscribedEmails = ffUnsubscribedEmails;
    }

    public ScheduledMailList getMailList() {
        return mailList;
    }

    public void setMailList(ScheduledMailList mailList) {
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
