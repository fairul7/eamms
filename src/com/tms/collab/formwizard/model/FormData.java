package com.tms.collab.formwizard.model;

public class FormData {
    private Object value;
    private String label;
    private Object displayValue;
    private Object type;
    private Object widget;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(Object displayValue) {
        this.displayValue = displayValue;
    }

    public Object getWidget() {
        return widget;
    }

    public void setWidget(Object widget) {
        this.widget = widget;
    }

}
