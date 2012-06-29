package com.tms.hr.competency;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Jan 25, 2006
 * Time: 9:58:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class CompetencyListObject extends DefaultDataObject
{
    String username;
    String competencyName; 
    String competencyType;
    String competencyDescription;
    String competencyLevel;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompetencyName() {
        return competencyName;
    }

    public void setCompetencyName(String competencyName) {
        this.competencyName = competencyName;
    }

    public String getCompetencyType() {
        return competencyType;
    }

    public void setCompetencyType(String competencyType) {
        this.competencyType = competencyType;
    }

    public String getCompetencyDescription() {
        return competencyDescription;
    }

    public void setCompetencyDescription(String competencyDescription) {
        this.competencyDescription = competencyDescription;
    }

    public String getCompetencyLevel() {
        return competencyLevel;
    }

    public void setCompetencyLevel(String competencyLevel) {
        this.competencyLevel = competencyLevel;
    }


}
