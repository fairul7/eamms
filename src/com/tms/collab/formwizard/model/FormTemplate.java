package com.tms.collab.formwizard.model;

public class FormTemplate {
    private String formTemplateId;
    private String templateName;
    private String previewXml;
    private String formXml;
    private String tableColumn = "2";

    public String getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(String formTemplateId) {
        this.formTemplateId = formTemplateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPreviewXml() {
        return previewXml;
    }

    public void setPreviewXml(String previewXml) {
        this.previewXml = previewXml;
    }

    public String getFormXml() {
        return formXml;
    }

    public void setFormXml(String formXml) {
        this.formXml = formXml;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }
}
