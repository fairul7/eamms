package com.tms.collab.formwizard.engine;

public class FileLinkCheckboxField extends Field {
    private String linkName;
    private String linkText;
    private String linkUrl;

    private String checkBoxName;
    private String checkBoxText;
    private String checkBoxValue;
    private boolean checked;

    private String fileUploadName;
    private String fileUploadValue;

    private boolean required;

    private ValidatorFileLinkCheckboxField validatorFileLinkCheckboxField;



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

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }


    public String getCheckBoxName() {
        return checkBoxName;
    }

    public void setCheckBoxName(String checkBoxName) {
        this.checkBoxName = checkBoxName;
    }

    public String getCheckBoxText() {
        return checkBoxText;
    }

    public void setCheckBoxText(String checkBoxText) {
        this.checkBoxText = checkBoxText;
    }

    public String getFileUploadName() {
        return fileUploadName;
    }

    public void setFileUploadName(String fileUploadName) {
        this.fileUploadName = fileUploadName;
    }

    public String getCheckBoxValue() {
        return checkBoxValue;
    }

    public void setCheckBoxValue(String checkBoxValue) {
        this.checkBoxValue = checkBoxValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getFileUploadValue() {
        return fileUploadValue;
    }

    public void setFileUploadValue(String fileUploadValue) {
        this.fileUploadValue = fileUploadValue;
    }

    public ValidatorFileLinkCheckboxField getValidatorFileLinkCheckboxField() {
        return validatorFileLinkCheckboxField;
    }

    public void setValidatorFileLinkCheckboxField(ValidatorFileLinkCheckboxField validatorFileLinkCheckboxField) {
        this.validatorFileLinkCheckboxField = validatorFileLinkCheckboxField;
    }
}
