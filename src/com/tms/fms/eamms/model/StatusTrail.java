package com.tms.fms.eamms.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class StatusTrail extends DefaultDataObject
{
	private String id; 
	private String feedsDetailsId;
	private String status;
	private String createdBy;
	private Date createdDate;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getFeedsDetailsId()
	{
		return feedsDetailsId;
	}
	public void setFeedsDetailsId(String feedsDetailsId)
	{
		this.feedsDetailsId = feedsDetailsId;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getCreatedBy()
	{
		return createdBy;
	}
	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}
	public Date getCreatedDate()
	{
		return createdDate;
	}
	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}
}
