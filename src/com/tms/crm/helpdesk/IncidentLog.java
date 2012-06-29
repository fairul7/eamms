package com.tms.crm.helpdesk;

import kacang.model.DefaultDataObject;

import java.util.Date;

public class IncidentLog extends DefaultDataObject
{
	private String logId;
	private String incidentId;
	private Date incidentDate;
	private String action;
    private String resolutionState;
	private String user;
    private String userId;
    private String receipient;
    

	public String getLogId()
	{
		return logId;
	}

	public void setLogId(String logId)
	{
		this.logId = logId;
	}

	public String getIncidentId()
	{
		return incidentId;
	}

	public void setIncidentId(String incidentId)
	{
		this.incidentId = incidentId;
	}

	public Date getIncidentDate()
	{
		return incidentDate;
	}

	public void setIncidentDate(Date incidentDate)
	{
		this.incidentDate = incidentDate;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getUser()
	{
		return user;
	}

    public String getResolutionState()
    {
        return resolutionState;
    }

    public void setResolutionState(String resolutionState)
    {
        this.resolutionState = resolutionState;
    }

    public void setUser(String user)
	{
		this.user = user;
	}

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

	public String getReceipient() {
		if(receipient==null){
			return "";
		}else
		return receipient;
	}

	public void setReceipient(String receipient) {
		this.receipient = receipient;
	}

	
}
