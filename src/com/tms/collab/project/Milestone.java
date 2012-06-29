package com.tms.collab.project;

import kacang.model.DefaultDataObject;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;

import com.tms.util.FormatUtil;

public class Milestone extends DefaultDataObject
{
    private String milestoneId;
    private String projectId;
    private String milestoneName;
    private String milestoneDescription;
    private int milestoneProgress;
    private int milestoneOrder;
    private Collection tasks = new ArrayList();
	private Collection meetings = new ArrayList();
    private Collection descriptors = new ArrayList();
    private Date endDate;
    private Date startDate;
    private Date actEndDate;
    private Date actStartDate;
    private String actualEndDate;
    private String actualStartDate;
    private String duration;
    private String actualDuration;
    private String variance;
    private String startVariance;
    private String endVariance;
    
    public String getMilestoneId()
    {
        return milestoneId;
    }

    public void setMilestoneId(String milestoneId)
    {
        this.milestoneId = milestoneId;
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public String getMilestoneName()
    {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName)
    {
        this.milestoneName = milestoneName;
    }

    public String getMilestoneDescription()
    {
        return milestoneDescription;
    }

    public void setMilestoneDescription(String milestoneDescription)
    {
        this.milestoneDescription = milestoneDescription;
    }

    public int getMilestoneProgress()
    {
        return milestoneProgress;
    }

    public void setMilestoneProgress(int milestoneProgress)
    {
        this.milestoneProgress = milestoneProgress;
    }

    public int getMilestoneOrder()
    {
        return milestoneOrder;
    }

    public void setMilestoneOrder(int milestoneOrder)
    {
        this.milestoneOrder = milestoneOrder;
    }

    public Collection getTasks()
    {
        return tasks;
    }

    public void setTasks(Collection tasks)
    {
        this.tasks = tasks;
    }

	public Collection getDescriptors()
	{
		return descriptors;
	}

	public void setDescriptors(Collection descriptors)
	{
		this.descriptors = descriptors;
	}

	public Collection getMeetings()
	{
		return meetings;
	}

	public void setMeetings(Collection meetings)
	{
		this.meetings = meetings;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
			return"Not Started";
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
		SimpleDateFormat start = new SimpleDateFormat("ddMMMyyyy");
		if(actStartDate!=null)
		return start.format(actStartDate);
		else
			return"Not Started";
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getVariance() {
		return variance;
	}

	public void setVariance(String variance) {
		this.variance = variance;
	}

	public String getActualDuration() {
		return actualDuration;
	}

	public void setActualDuration(String actualDuration) {
		this.actualDuration = actualDuration;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getEndVariance() {
		return endVariance;
	}

	public void setEndVariance(String endVariance) {
		this.endVariance = endVariance;
	}

	public String getStartVariance() {
		return startVariance;
	}

	public void setStartVariance(String startVariance) {
		this.startVariance = startVariance;
	}
	
	
}
