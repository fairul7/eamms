package com.tms.collab.project;

import kacang.model.DefaultDataObject;

public class ReportTask extends DefaultDataObject{
	private String reportId;
    private String taskId;
    private String taskName;
    private double estimatedMandays;
    private String actualMandays;
    private String variance;
    private int taskOrder;

	public int getTaskOrder() {
		return taskOrder;
	}
	public void setTaskOrder(int taskOrder) {
		this.taskOrder = taskOrder;
	}	
	public String getActualMandays() {
		return actualMandays;
	}
	public void setActualMandays(String actualMandays) {
		this.actualMandays = actualMandays;
	}
	
	public double getEstimatedMandays() {
		return estimatedMandays;
	}
	public void setEstimatedMandays(double estimatedMandays) {
		this.estimatedMandays = estimatedMandays;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getVariance() {
		return variance;
	}
	public void setVariance(String variance) {
		this.variance = variance;
	}

    

}
