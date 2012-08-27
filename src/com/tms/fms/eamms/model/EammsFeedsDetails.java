package com.tms.fms.eamms.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class EammsFeedsDetails extends DefaultDataObject
{
	private String id;
	private String requestId;
	private String submittedBy;
	private Date requestedDate;
	private String location;
	private String telco;
	private String oblink;
	private String networkStatus;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getRequestId()
	{
		return requestId;
	}
	public void setRequestId(String requestId)
	{
		this.requestId = requestId;
	}
	public String getSubmittedBy()
	{
		return submittedBy;
	}
	public void setSubmittedBy(String submittedBy)
	{
		this.submittedBy = submittedBy;
	}
	public Date getRequestedDate()
	{
		return requestedDate;
	}
	public void setRequestedDate(Date requestedDate)
	{
		this.requestedDate = requestedDate;
	}
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
	public String getTelco()
	{
		return telco;
	}
	public void setTelco(String telco)
	{
		this.telco = telco;
	}
	public String getOblink()
	{
		return oblink;
	}
	public void setOblink(String oblink)
	{
		this.oblink = oblink;
	}
	public String getNetworkStatus()
	{
		return networkStatus;
	}
	public void setNetworkStatus(String networkStatus)
	{
		this.networkStatus = networkStatus;
	}
}
