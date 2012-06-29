package com.tms.collab.project;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Project extends DefaultDataObject
{
	public static final String DEFAULT_DELIMITER = ",";

    private String projectId;
    private String projectName;
    private String projectDescription;
    private String projectCategory;
    private double projectValue;
    private User owner;
    private Collection roles = new ArrayList();
    private Collection milestones = new ArrayList();
	private Collection projectWorking = new ArrayList();
	private Collection files = new ArrayList();
	private String projectSummary;
	private boolean archived;
    private Date creationDate;
    private Date modifiedDate;

    //Convenience properties
    private Date projectStartDate;
    private Date projectEndDate;    

    private String projectCurrencyType;
    private String projectCurrencyValue;
    private Float projectStatus;
    
    //New
    private String clientName;
    private Date actStartDate;
    private Date actEndDate;
    private String actualEndDate;
    private String actualStartDate;
    private String startVarians;
    private String endVarians;
    private String estDuration;
    private String actDuration;
    
    //testing
    private Collection members = new ArrayList();
    
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Collection getMilestones()
    {
        return milestones;
    }

    public void setMilestones(Collection milestones)
    {
        this.milestones = milestones;
    }

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    public String getOwnerId()
    {
        String id = null;
        if(owner != null)
            id = owner.getId();
        return id;
    }

    public void setOwnerId(String id)
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try
        {
            owner = service.getUser(id);
        }
        catch(SecurityException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public String getOwnerName()
    {
        String name = "";
        if(owner != null)
            name = owner.getProperty("firstName").toString() + " " + owner.getProperty("lastName").toString();
        return name;
    }

    public String getProjectDescription()
    {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription)
    {
        this.projectDescription = projectDescription;
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public double getProjectValue()
    {
        return projectValue;
    }

    public void setProjectValue(double projectValue)
    {
        this.projectValue = projectValue;
    }

    public Collection getRoles()
    {
        return roles;
    }

    public void setRoles(Collection roles)
    {
        this.roles = roles;
    }

    public String getProjectCategory()
    {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory)
    {
        this.projectCategory = projectCategory;
    }

    public Date getProjectEndDate()
    {
        return projectEndDate;
    }

    public void setProjectEndDate(Date projectEndDate)
    {
        this.projectEndDate = projectEndDate;
    }

    public Date getProjectStartDate()
    {
        return projectStartDate;
    }

    public void setProjectStartDate(Date projectStartDate)
    {
        this.projectStartDate = projectStartDate;
    }

	public Collection getProjectWorking()
	{
		return projectWorking;
	}

	public void setProjectWorking(Collection projectWorking)
	{
		this.projectWorking = projectWorking;
	}

	public String getProjectWorkingDays()
	{
		String days = "";
		for (Iterator i = projectWorking.iterator(); i.hasNext();)
		{
			String day = (String) i.next();
			if(days.length() != 0)
				days += ",";
			days += day;
		}
		return days;
	}

	public void setProjectWorkingDays(String days)
	{
		projectWorking = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(days, DEFAULT_DELIMITER);
		while(tokenizer.hasMoreTokens())
			projectWorking.add(tokenizer.nextToken());
	}

	public String getProjectSummary()
	{
		return projectSummary;
	}

	public void setProjectSummary(String projectSummary)
	{
		this.projectSummary = projectSummary;
	}

	public boolean isArchived()
	{
		return archived;
	}

	public void setArchived(boolean archived)
	{
		this.archived = archived;
	}

	public char getArchived()
	{
		if(archived)
			return '1';
		return '0';
	}

	public Collection getFiles()
	{
		return files;
	}

	public void setFiles(Collection files)
	{
		this.files = files;
	}

    public void setProjectCurrencyType(String projectCurrencyType) {
        this.projectCurrencyType=projectCurrencyType;
    }

    public String getProjectCurrencyType() {
        return projectCurrencyType;
    }

    public String getProjectCurrencyValue() {
        DecimalFormat format = new DecimalFormat("##,###,##0.00");
        if(projectCurrencyType==null)
        	return format.format(projectValue);
        else
        return projectCurrencyType+" "+format.format(projectValue);
    }

	public Float getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(Float projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Date getActEndDate() {
		return actEndDate;
	}

	public void setActEndDate(Date actEndDate) {
		this.actEndDate = actEndDate;
	}

	public Date getActStartDate() {
		return actStartDate;
	}

	public void setActStartDate(Date actStartDate) {
		this.actStartDate = actStartDate;
	}

	public String getActualEndDate() {
		SimpleDateFormat end = new SimpleDateFormat("ddMMMyyyy");
		if("Not Started".equals(getActualStartDate())){
			return "Not Started";
		}else{
		if(actEndDate!=null)
		return end.format(actEndDate);
		else
			return"Ongoing";
		}
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getActualStartDate() {
		SimpleDateFormat end = new SimpleDateFormat("ddMMMyyyy");
		if(actStartDate!=null)
		return end.format(actStartDate);
		else
			return"Not Started";
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getEndVarians() {
		return endVarians;
	}

	public void setEndVarians(String endVarians) {
		this.endVarians = endVarians;
	}

	public String getStartVarians() {
		return startVarians;
	}

	public void setStartVarians(String startVarians) {
		this.startVarians = startVarians;
	}

	public String getActDuration() {
		return actDuration;
	}

	public void setActDuration(String actDuration) {
		this.actDuration = actDuration;
	}

	public String getEstDuration() {
		return estDuration;
	}

	public void setEstDuration(String estDuration) {
		this.estDuration = estDuration;
	}
	public Collection getMembers() {
		return members;
	}

	public void setMembers(Collection members) {
		this.members = members;
	}  
	
	
}
