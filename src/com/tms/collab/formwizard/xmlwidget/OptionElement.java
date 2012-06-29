package com.tms.collab.formwizard.xmlwidget;

import com.tms.collab.formwizard.model.FormException;
import org.jdom.Element;


public class OptionElement extends Element{
    public OptionElement(String name, String value, String text, String checkedStr) throws FormException {
        this(name, value, text, Boolean.valueOf(checkedStr).booleanValue(), ButtonGroupElement.CHECKBOX_ELEMENT_NAME);
    }

    public OptionElement(String name, String value, String text, boolean checked, String type) throws FormException {
        super(type);

        if (isNullOrEmpty(name)) {
            throw new FormException("Name can not be null");
        }
        setAttribute("name", name);
        if (!isNullOrEmpty(value)) {
            setAttribute("value", value);
        }
        if (!isNullOrEmpty(text)) {
            setAttribute("text", text);
        }
        if (checked) {
            setAttribute("checked", "true");
        }
    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.length() < 1);
    }
}
