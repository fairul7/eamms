package com.tms.collab.formwizard.engine;

public class LabelField extends Field {
    private String text;
    private String type;
    private String escapeXml;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEscapeXml() {
        return escapeXml;
    }

    public void setEscapeXml(String escapeXml) {
        this.escapeXml = escapeXml;
    }

}
