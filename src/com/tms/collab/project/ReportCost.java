package com.tms.collab.project;

import kacang.model.DefaultDataObject;

public class ReportCost extends DefaultDataObject{
	private String reportId;
    private String costId;
    private String costName;
    private String estimated;
    private String actual;
    private String variance;
    private int costOrder;
    
	public String getActual() {
		return actual;
	}
	public void setActual(String actual) {
		this.actual = actual;
	}
	public String getCostId() {
		return costId;
	}
	public void setCostId(String costId) {
		this.costId = costId;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public String getEstimated() {
		return estimated;
	}
	public void setEstimated(String estimated) {
		this.estimated = estimated;
	}
	public int getCostOrder() {
		return costOrder;
	}
	public void setCostOrder(int costOrder) {
		this.costOrder = costOrder;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getVariance() {
		return variance;
	}
	public void setVariance(String variance) {
		this.variance = variance;
	}

    
}
