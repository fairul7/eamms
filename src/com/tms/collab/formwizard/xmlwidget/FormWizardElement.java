package com.tms.collab.formwizard.xmlwidget;

import org.jdom.Element;

public class FormWizardElement extends Element implements XmlWidgetAttributes {

    public FormWizardElement(String s) {
        super(s);
    }

    public int getSize() {
        return 0;
    }

    public void removeMetaData() {
        removeAttribute("template");
    }

    public int getType() {
        return FormElement.FORM_VARCHAR_TYPE;
    }

    protected boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() < 1);
    }


}
