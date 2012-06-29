package com.tms.crm.helpdesk;

import kacang.model.DefaultDataObject;

public class IncidentSettings extends DefaultDataObject {

    private String severityOptions;
    private String resolutionStateOptions;
    private String contactedByOptions;
    private String incidentTypeOptions;
    private String property1Label;
    private String property2Label;
    private String property3Label;
    private String property4Label;
    private String property5Label;
    private String property6Label;
    private String property1Options;
    private String property2Options;
    private String property3Options;
    private String property4Options;
    private String property5Options;
    private String property6Options;

    public String[] getProperties() {
        return new String[] {
            "severityOptions",
            "resolutionStateOptions",
            "contactedByOptions",
            "incidentTypeOptions",
            "property1Label",
            "property2Label",
            "property3Label",
            "property4Label",
            "property5Label",
            "property6Label",
            "property1Options",
            "property2Options",
            "property3Options",
            "property4Options",
            "property5Options",
            "property6Options",
        };
    }

    public String getSeverityOptions() {
        return severityOptions;
    }

    public void setSeverityOptions(String severityOptions) {
        this.severityOptions = severityOptions;
    }

    public String getResolutionStateOptions() {
        return resolutionStateOptions;
    }

    public void setResolutionStateOptions(String resolutionStateOptions) {
        this.resolutionStateOptions = resolutionStateOptions;
    }

    public String getContactedByOptions() {
        return contactedByOptions;
    }

    public void setContactedByOptions(String contactedByOptions) {
        this.contactedByOptions = contactedByOptions;
    }

    public String getIncidentTypeOptions() {
        return incidentTypeOptions;
    }

    public void setIncidentTypeOptions(String incidentTypeOptions) {
        this.incidentTypeOptions = incidentTypeOptions;
    }

    public String getProperty1Label() {
        return property1Label;
    }

    public void setProperty1Label(String property1Label) {
        this.property1Label = property1Label;
    }

    public String getProperty2Label() {
        return property2Label;
    }

    public void setProperty2Label(String property2Label) {
        this.property2Label = property2Label;
    }

    public String getProperty3Label() {
        return property3Label;
    }

    public void setProperty3Label(String property3Label) {
        this.property3Label = property3Label;
    }

    public String getProperty4Label() {
        return property4Label;
    }

    public void setProperty4Label(String property4Label) {
        this.property4Label = property4Label;
    }

    public String getProperty5Label() {
        return property5Label;
    }

    public void setProperty5Label(String property5Label) {
        this.property5Label = property5Label;
    }

    public String getProperty6Label() {
        return property6Label;
    }

    public void setProperty6Label(String property6Label) {
        this.property6Label = property6Label;
    }

    public String getProperty1Options() {
        return property1Options;
    }

    public void setProperty1Options(String property1Options) {
        this.property1Options = property1Options;
    }

    public String getProperty2Options() {
        return property2Options;
    }

    public void setProperty2Options(String property2Options) {
        this.property2Options = property2Options;
    }

    public String getProperty3Options() {
        return property3Options;
    }

    public void setProperty3Options(String property3Options) {
        this.property3Options = property3Options;
    }

    public String getProperty4Options() {
        return property4Options;
    }

    public void setProperty4Options(String property4Options) {
        this.property4Options = property4Options;
    }

    public String getProperty5Options() {
        return property5Options;
    }

    public void setProperty5Options(String property5Options) {
        this.property5Options = property5Options;
    }

    public String getProperty6Options() {
        return property6Options;
    }

    public void setProperty6Options(String property6Options) {
        this.property6Options = property6Options;
    }

}
