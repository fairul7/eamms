package com.tms.cms.profile.model;

import kacang.model.DefaultDataObject;

import java.util.List;

public class ContentProfile extends DefaultDataObject {

    private String name;
    private String description;
    private String definition;
    private List fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List getFields() {
        return fields;
    }

    public void setFields(List fields) {
        this.fields = fields;
    }
}
