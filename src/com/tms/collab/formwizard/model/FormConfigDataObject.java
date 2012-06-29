package com.tms.collab.formwizard.model;

import kacang.model.DefaultDataObject;
import com.tms.collab.formwizard.xmlwidget.FormElement;


public class FormConfigDataObject extends DefaultDataObject{
    private String formConfigId;
    private String formId;
    private String previewXml;
    private String formXml;
    private FormElement formElement;

    public String getFormConfigId() {
        return formConfigId;
    }

    public void setFormConfigId(String formConfigId) {
        this.formConfigId = formConfigId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
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

    public FormElement getFormElement() {
        return formElement;
    }

    public void setFormElement(FormElement formElement) {
        this.formElement = formElement;
    }
}
