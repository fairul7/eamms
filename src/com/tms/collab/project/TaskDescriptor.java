package com.tms.collab.project;

import kacang.model.DefaultDataObject;

public class TaskDescriptor extends DefaultDataObject
{
	private String descId;
	private String milestoneId;
	private String descName;
	private String descDescription;
	private int descStart;
    private int descDuration;

	public String getDescId()
	{
		return descId;
	}

	public void setDescId(String descId)
	{
		this.descId = descId;
	}

	public String getMilestoneId()
	{
		return milestoneId;
	}

	public void setMilestoneId(String milestoneId)
	{
		this.milestoneId = milestoneId;
	}

	public String getDescName()
	{
		return descName;
	}

	public void setDescName(String descName)
	{
		this.descName = descName;
	}

	public String getDescDescription()
	{
		return descDescription;
	}

	public void setDescDescription(String descDescription)
	{
		this.descDescription = descDescription;
	}

	public int getDescStart()
	{
		return descStart;
	}

	public void setDescStart(int descStart)
	{
		this.descStart = descStart;
	}

	public int getDescDuration()
	{
		return descDuration;
	}

	public void setDescDuration(int descDuration)
	{
		this.descDuration = descDuration;
	}
}
