package com.tms.fms.eamms.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class EammsAssignment extends DefaultDataObject	
{
	private String id; 
	private String assignmentId;
	private String feedsDetailsId;
	private String tvroServiceId;
	private Date requiredDateFrom;
	private Date requiredDateTo; 
	private String requiredTimeFrom;
	private String requiredTimeTo;
	private String timezone;
	private String totalTimeReq;
	private String timeMeasure;
	private String remarks;
	private String bookingStatus;
	private String networkRemarks;
	private String attachment;
	private String status;
	private boolean blockbooking;
	private String createdBy;
	private Date createdDate;
	
	private String title;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getAssignmentId()
	{
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId)
	{
		this.assignmentId = assignmentId;
	}
	public String getFeedsDetailsId()
	{
		return feedsDetailsId;
	}
	public void setFeedsDetailsId(String feedsDetailsId)
	{
		this.feedsDetailsId = feedsDetailsId;
	}
	public String getRequiredTimeFrom()
	{
		return requiredTimeFrom;
	}
	public void setRequiredTimeFrom(String requiredTimeFrom)
	{
		this.requiredTimeFrom = requiredTimeFrom;
	}
	public String getRequiredTimeTo()
	{
		return requiredTimeTo;
	}
	public void setRequiredTimeTo(String requiredTimeTo)
	{
		this.requiredTimeTo = requiredTimeTo;
	}
	public String getTimezone()
	{
		return timezone;
	}
	public void setTimezone(String timezone)
	{
		this.timezone = timezone;
	}
	public String getTotalTimeReq()
	{
		return totalTimeReq;
	}
	public void setTotalTimeReq(String totalTimeReq)
	{
		this.totalTimeReq = totalTimeReq;
	}
	public String getTimeMeasure()
	{
		return timeMeasure;
	}
	public void setTimeMeasure(String timeMeasure)
	{
		this.timeMeasure = timeMeasure;
	}
	public String getRemarks()
	{
		return remarks;
	}
	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}
	public String getBookingStatus()
	{
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus)
	{
		this.bookingStatus = bookingStatus;
	}
	public String getNetworkRemarks()
	{
		return networkRemarks;
	}
	public void setNetworkRemarks(String networkRemarks)
	{
		this.networkRemarks = networkRemarks;
	}
	public String getAttachment()
	{
		return attachment;
	}
	public void setAttachment(String attachment)
	{
		this.attachment = attachment;
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
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getTvroServiceId()
	{
		return tvroServiceId;
	}
	public void setTvroServiceId(String tvroServiceId)
	{
		this.tvroServiceId = tvroServiceId;
	}
	public Date getRequiredDateFrom()
	{
		return requiredDateFrom;
	}
	public void setRequiredDateFrom(Date requiredDateFrom)
	{
		this.requiredDateFrom = requiredDateFrom;
	}
	public Date getRequiredDateTo()
	{
		return requiredDateTo;
	}
	public void setRequiredDateTo(Date requiredDateTo)
	{
		this.requiredDateTo = requiredDateTo;
	}
	public boolean isBlockbooking()
	{
		return blockbooking;
	}
	public void setBlockbooking(boolean blockbooking)
	{
		this.blockbooking = blockbooking;
	}
}
