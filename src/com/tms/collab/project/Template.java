package com.tms.collab.project;

import kacang.model.DefaultDataObject;

import java.util.Collection;

public class Template extends DefaultDataObject
{
    private String templateId;
    private String templateName;
    private String templateDescription;
    private String templateCategory;
    private Collection milestones;
    private Collection roles;

    public Collection getMilestones()
    {
        return milestones;
    }

    public void setMilestones(Collection milestones)
    {
        this.milestones = milestones;
    }

    public String getTemplateCategory()
    {
        return templateCategory;
    }

    public void setTemplateCategory(String templateCategory)
    {
        this.templateCategory = templateCategory;
    }

    public String getTemplateDescription()
    {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription)
    {
        this.templateDescription = templateDescription;
    }

    public String getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    public Collection getRoles()
    {
        return roles;
    }

    public void setRoles(Collection roles)
    {
        this.roles = roles;
    }
}
