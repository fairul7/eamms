package com.tms.collab.project;

import kacang.model.DefaultDataObject;

public class ReportDefects extends DefaultDataObject{
	private String reportId;
    private String defectTypeId;
    private String defectTypeName;
    private int resolved;
    private int unresolved;
    private int total;
    private int defectOrder;
    private double severity;

	public int getDefectOrder() {
		return defectOrder;
	}
	public void setDefectOrder(int defectOrder) {
		this.defectOrder = defectOrder;
	}
	public String getDefectTypeId() {
		return defectTypeId;
	}
	public void setDefectTypeId(String defectTypeId) {
		this.defectTypeId = defectTypeId;
	}
	public String getDefectTypeName() {
		return defectTypeName;
	}
	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public int getResolved() {
		return resolved;
	}
	public void setResolved(int resolved) {
		this.resolved = resolved;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getUnresolved() {
		return unresolved;
	}
	public void setUnresolved(int unresolved) {
		this.unresolved = unresolved;
	}
	public double getSeverity() {
		return severity;
	}
	public void setSeverity(double severity) {
		this.severity = severity;
	}   
}
