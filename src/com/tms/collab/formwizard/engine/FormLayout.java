package com.tms.collab.formwizard.engine;

import java.util.List;
import java.io.Serializable;

public class FormLayout implements Serializable {
    private String formName;
    private String columns;
    private String width;
    private String templateId;


    private List fieldList;


    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public List getFieldList() {
        return fieldList;
    }

    public void setFieldList(List fieldList) {
        this.fieldList = fieldList;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }


}
