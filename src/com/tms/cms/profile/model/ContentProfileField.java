package com.tms.cms.profile.model;

import kacang.model.DefaultDataObject;

public class ContentProfileField extends DefaultDataObject {

    public static final String FORM_TEXTFIELD = "textfield";
    public static final String FORM_TEXTBOX = "textbox";
    public static final String FORM_RICHTEXTBOX = "richtextbox";
    public static final String FORM_SELECTBOX = "selectbox";
    public static final String FORM_LISTBOX = "listbox";
    public static final String FORM_CHECKBOX = "checkbox";
    public static final String FORM_RADIO = "radio";
    public static final String FORM_DATEFIELD = "datefield";
    public static final String FORM_HIDDEN = "hidden";
    public static final String FORM_NONE = "none";

    public static final String VALIDATOR_NOT_EMPTY = "notempty";

    private String name;
    private String type;
    private String validator;
    private String options;
    private String label;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
