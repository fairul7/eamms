package com.tms.report.model;

import java.util.Date;

import kacang.model.DefaultDataObject;
import kacang.services.security.User;


public class ReportObject extends DefaultDataObject {
    User user = null;
    String module="";
    String moduleName="";
    int totalLogin=0;
    int uniqueCount=0;
    int totalCount=0;
    
    //attribute for resource usage summary report
    private String eventId;
    private String instanceId;
    private String resourceId;
    private Date startDate;
    private Date endDate;
    private String resourceName;
    
    //attribute for subscribed resources report
    private String source;
    private double totalHour; 
    
    public ReportObject(){

    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleName() {
        if (moduleName==null || moduleName.equals(""))
            return module;
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getTotalLogin() {
        return totalLogin;
    }

    public void setTotalLogin(int totalLogin) {
        this.totalLogin = totalLogin;
    }

    public int getUniqueCount() {
        return uniqueCount;
    }

    public void setUniqueCount(int uniqueCount) {
        this.uniqueCount = uniqueCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public double getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(double totalHour) {
		this.totalHour = totalHour;
	}
    
}
