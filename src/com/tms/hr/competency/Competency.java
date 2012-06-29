package com.tms.hr.competency;

import kacang.model.DefaultDataObject;

public class Competency extends DefaultDataObject
{
    public static final String PROPERTY_COMPETENCY_LEVELS = "com.tms.hr.competency.CompetencyLevels";
    public static final String DEFAULT_DELIMITER = ",";

    private String competencyId;
    private String competencyName;
    private String competencyType;
    private String competencyDescription;
    private String competencyLevel;
    private String unitId;

    public String getCompetencyId()
    {
        return competencyId;
    }

    public void setCompetencyId(String competencyId)
    {
        this.competencyId = competencyId;
    }

    public String getCompetencyName()
    {
        return competencyName;
    }

    public void setCompetencyName(String competencyName)
    {
        this.competencyName = competencyName;
    }

    public String getCompetencyType()
    {
        return competencyType;
    }

    public void setCompetencyType(String competencyType)
    {
        this.competencyType = competencyType;
    }

    public String getCompetencyDescription()
    {
        return competencyDescription;
    }

    public void setCompetencyDescription(String competencyDescription)
    {
        this.competencyDescription = competencyDescription;
    }

    public boolean equals(Object o)
    {
            boolean equals = false;
            if(o instanceof Competency)
                if(((Competency) o).getCompetencyId().equals(competencyId))
                    equals = true;
            return equals;
        }

	public String getCompetencyLevel() {
		return competencyLevel;
	}

	public void setCompetencyLevel(String competencyLevel) {
		this.competencyLevel = competencyLevel;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	
}
