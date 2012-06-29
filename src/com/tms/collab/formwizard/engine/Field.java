package com.tms.collab.formwizard.engine;

import java.io.Serializable;

public class Field implements Serializable {
    private String name;
    private String hidden;
    private String require;
    private String colspan;
    private String rowspan;
    private String align;
    private String valign;
    private Object value;

    private String invalid;
    private Object widget;

    public static String FIELD_INVALID = "invalid";
    public static String FIELD_VALID = "valid";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public String getColspan() {
        return colspan;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public String getRowspan() {
        return rowspan;
    }

    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getValign() {
        return valign;
    }

    public void setValign(String valign) {
        this.valign = valign;
    }

    public String isInvalid() {
        return invalid;
    }

    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getWidget() {
        return widget;
    }

    public void setWidget(Object widget) {
        this.widget = widget;
    }
}
