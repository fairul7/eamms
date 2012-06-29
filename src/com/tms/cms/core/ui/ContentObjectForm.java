package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentUtil;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.stdui.validator.Validator;

import java.util.Calendar;

public class ContentObjectForm extends Form {

    protected ContentObject contentObject;

    protected TextField idField;
    protected TextField nameField;
    protected Validator nameFieldValidator;
    protected TextBox descriptionBox;
    protected TextBox summaryBox;
    protected TextField authorField;
    protected CheckBox scheduleCheckBox;
    protected DateField startDate;
    protected TimeField startTime;
    protected CheckBox scheduleEndCheckBox;
    protected DateField endDate;
    protected TimeField endTime;

    public ContentObjectForm() {
    }

    public ContentObjectForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentObjectForm";
    }

    public void init(Event evt) {

        // set layout
        setColumns(2);

        // remove existing children
        removeChildren();

        // add children
        idField = new TextField("idField");
        addChild(idField);

        nameField = new TextField("nameField");
        nameFieldValidator = new ValidatorNotEmpty("valid1");
        nameField.addChild(nameFieldValidator);
        addChild(nameField);

        descriptionBox = new TextBox("descriptionBox");
        addChild(descriptionBox);

        authorField = new TextField("authorField");
        addChild(authorField);

        summaryBox = new RichTextBox("summaryBox");
        summaryBox.setCols("60");
        summaryBox.setRows("20");
        if (getContentObject() != null && getContentObject().getId() != null) {
            ((RichTextBox)summaryBox).setImageUrl(evt.getRequest().getContextPath() + "/cmsadmin/content/imageSelectorNew.jsp");
        }
        ((RichTextBox)summaryBox).setLinkUrl(evt.getRequest().getContextPath() + "/cmsadmin/content/linkSelectorNew.jsp");
        addChild(summaryBox);

        scheduleCheckBox = new CheckBox("scheduleCheckBox");
        addChild(scheduleCheckBox);

        startDate = new DateField("startDate");
        addChild(startDate);

        startTime = new TimeField("startTime");
        addChild(startTime);

        scheduleEndCheckBox = new CheckBox("scheduleEndCheckBox");
        addChild(scheduleEndCheckBox);

        endDate = new DateField("endDate");
        addChild(endDate);

        endTime = new TimeField("endTime");
        addChild(endTime);
    }

    public ContentObject getContentObject() {
        return contentObject;
    }

    public void setContentObject(ContentObject contentObject) {
        this.contentObject = contentObject;
    }

    public Forward onValidate(Event evt) {
        ContentObject contentObject = getContentObject();
        if (contentObject != null) {
            populateContentObject(evt, contentObject);
        }
        return null;
    }

    protected void populateFields(Event evt) {
        // populate values?
        ContentObject contentObject = getContentObject();
        if (contentObject != null) {
            if (idField != null) {
                idField.setValue(contentObject.getId());
            }
            if (nameField != null) {
                nameField.setValue(contentObject.getName());
            }
            if (descriptionBox != null) {
                descriptionBox.setValue(contentObject.getDescription());
            }
            if (summaryBox != null) {
                summaryBox.setValue(ContentUtil.makeAbsolute(evt.getRequest(), contentObject.getSummary()));
            }
            if (authorField != null) {
                authorField.setValue(contentObject.getAuthor());
            }
            if (startDate != null && contentObject.getStartDate() != null) {
                startDate.setDate(contentObject.getStartDate());
                startTime.setDate(contentObject.getStartDate());
                scheduleCheckBox.setChecked(true);
            }
            if (endDate != null && contentObject.getEndDate() != null) {
                endDate.setDate(contentObject.getEndDate());
                endTime.setDate(contentObject.getEndDate());
                scheduleEndCheckBox.setChecked(true);
            }
        }
    }

    protected void populateContentObject(Event evt, ContentObject contentObject) {
        if (idField != null && idField.getValue() != null) {
            contentObject.setId(idField.getValue().toString());
        }
        if (nameField != null) {
            contentObject.setName((nameField.getValue() == null) ? "" : nameField.getValue().toString());
        }
        if (descriptionBox != null) {
            contentObject.setDescription((descriptionBox.getValue() == null) ? "" : descriptionBox.getValue().toString());
        }
        if (summaryBox != null) {
            contentObject.setSummary(ContentUtil.makeRelative(evt.getRequest(),  (summaryBox.getValue() == null) ? "" : summaryBox.getValue().toString()));
        }
        if (authorField != null && authorField.getValue() != null) {
            contentObject.setAuthor(authorField.getValue().toString());
        }
        if (scheduleCheckBox != null && scheduleCheckBox.isChecked()) {
            Calendar cDate = Calendar.getInstance();
            Calendar cTime = Calendar.getInstance();
            cDate.setTime(startDate.getDate());
            cTime.setTime(startTime.getDate());
            cDate.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
            cDate.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
            cDate.set(Calendar.SECOND, 0);
            contentObject.setStartDate(cDate.getTime());
        }
        else {
            contentObject.setStartDate(null);
        }
        if (scheduleEndCheckBox != null && scheduleEndCheckBox.isChecked()) {
            Calendar cDate = Calendar.getInstance();
            Calendar cTime = Calendar.getInstance();
            cDate.setTime(endDate.getDate());
            cTime.setTime(endTime.getDate());
            cDate.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
            cDate.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
            cDate.set(Calendar.SECOND, 0);
            contentObject.setEndDate(cDate.getTime());
        }
        else {
            contentObject.setEndDate(null);
        }
    }

}
