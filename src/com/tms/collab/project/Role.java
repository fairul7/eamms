package com.tms.collab.project;

import com.tms.hr.competency.Competency;
import kacang.model.DefaultDataObject;
import kacang.services.security.User;

import java.util.ArrayList;
import java.util.Collection;

public class Role extends DefaultDataObject
{
    private String roleId;
    private String roleName;
    private String roleDescription;
    private String projectId;
    private Collection competencies;
    private Collection personnel;

    public Role()
    {
        competencies = new ArrayList();
        personnel = new ArrayList();
    }

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleDescription()
    {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription)
    {
        this.roleDescription = roleDescription;
    }

    public Collection getCompetencies()
    {
        return competencies;
    }

    public void setCompetencies(Collection competencies)
    {
        this.competencies = competencies;
    }

    public void addCompetency(Competency competency)
    {
        this.competencies.add(competency);
    }

    public void removeCompetency(Competency competency)
    {
        this.competencies.remove(competency);
    }

    public Collection getPersonnel()
    {
        return personnel;
    }

    public void setPersonnel(Collection personnel)
    {
        this.personnel = personnel;
    }

    public void addPersonnel(User personnel)
    {
        this.personnel.add(personnel);
    }

    public void removePersonnel(User personnel)
    {
        this.personnel.remove(personnel);
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public boolean equals(Object o)
    {
        boolean equals = false;
        if(o instanceof Role)
            if(((Role)o).getRoleId().equals(roleId))
                equals = true;
        return equals;
    }
}
