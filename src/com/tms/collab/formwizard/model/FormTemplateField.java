package com.tms.collab.formwizard.model;

public class FormTemplateField {
    private String formId;
    private String formName;
    private String formTemplateId;
    private String templateNodeName;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(String formTemplateId) {
        this.formTemplateId = formTemplateId;
    }

    public String getTemplateNodeName() {
        return templateNodeName;
    }

    public void setTemplateNodeName(String templateNodeName) {
        this.templateNodeName = templateNodeName;
    }
}
