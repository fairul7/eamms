package com.tms.collab.project;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class ReportMilestone extends DefaultDataObject{
	private String reportId;
    private String milestoneId;
    private String milestoneName;
    private String estStartDate;
    private String estEndDate;
    private String actStartDate;
    private String actEndDate;
    private int milestoneOrder;
    private String estVariance;
    private String actVariance;
    private String variance;
    
	public String getActEndDate() {
		return actEndDate;
	}
	public void setActEndDate(String actEndDate) {
		this.actEndDate = actEndDate;
	}
	public String getActStartDate() {
		return actStartDate;
	}
	public void setActStartDate(String actStartDate) {
		this.actStartDate = actStartDate;
	}
	public String getEstEndDate() {
		return estEndDate;
	}
	public void setEstEndDate(String estEndDate) {
		this.estEndDate = estEndDate;
	}
	public String getEstStartDate() {
		return estStartDate;
	}
	public void setEstStartDate(String estStartDate) {
		this.estStartDate = estStartDate;
	}
	public String getMilestoneId() {
		return milestoneId;
	}
	public void setMilestoneId(String milestoneId) {
		this.milestoneId = milestoneId;
	}
	public String getMilestoneName() {
		return milestoneName;
	}
	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public int getMilestoneOrder() {
		return milestoneOrder;
	}
	public void setMilestoneOrder(int milestoneOrder) {
		this.milestoneOrder = milestoneOrder;
	}
	public String getVariance() {
		return variance;
	}
	public void setVariance(String variance) {
		this.variance = variance;
	}
	public String getActVariance() {
		return actVariance;
	}
	public void setActVariance(String actVariance) {
		this.actVariance = actVariance;
	}
	public String getEstVariance() {
		return estVariance;
	}
	public void setEstVariance(String estVariance) {
		this.estVariance = estVariance;
	}    
}
