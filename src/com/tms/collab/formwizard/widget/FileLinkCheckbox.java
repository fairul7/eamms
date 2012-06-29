package com.tms.collab.formwizard.widget;

import kacang.stdui.*;
import com.tms.collab.formwizard.model.FormConstants;




public class FileLinkCheckbox extends FormField {
    private FileUpload file;
    private Link link;
    private CheckBox checkBox;

    private String linkName;
    private String linkText;
    private String linkUrl;

    private String checkboxName;
    private String checkboxText;
    private String checkboxValue;

    private String fileUploadName;

    private boolean required;


    public FileLinkCheckbox() {
        super();
    }

    public  FileLinkCheckbox(String name) {
        super(name);

    }

    public String getDefaultTemplate() {
        return "formwizard/widget/fileLinkCheckbox";
    }


    public void init() {
        setLinkProperty();
        setCheckBoxProperty();
        setFileUploadProperty();
        addChild(checkBox);
        addChild(link);
        addChild(file);
    }

    public void  setCheckBoxProperty() {
        checkBox = new CheckBox( FormConstants.FIELD_CHECK_BOX_SUFFIX, getCheckboxText());
        checkBox.setValue(getCheckboxValue());

    }

    public void setFileUploadProperty() {
        file = new FileUpload( FormConstants.FIELD_FILE_UPLOAD_SUFFIX);
    }

    public void setLinkProperty() {
        link = new Link( FormConstants.FIELD_LINK_SUFFIX);
        link.setText(getLinkText());
        link.setUrl(getLinkUrl());
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public String getCheckboxName() {
        return checkboxName;
    }

    public void setCheckboxName(String checkboxName) {
        this.checkboxName = checkboxName;
    }

    public String getCheckboxText() {
        return checkboxText;
    }

    public void setCheckboxText(String checkboxText) {
        this.checkboxText = checkboxText;
    }

    public FileUpload getFile() {
        return file;
    }

    public void setFile(FileUpload file) {
        this.file = file;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getFileUploadName() {
        return fileUploadName;
    }

    public void setFileUploadName(String fileUploadName) {
        this.fileUploadName = fileUploadName;
    }

    public String getCheckboxValue() {
        return checkboxValue;
    }

    public void setCheckboxValue(String checkboxValue) {
        this.checkboxValue = checkboxValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
