package com.tms.report.ui;

import java.util.Collection;
import java.util.Date;

import com.tms.report.model.ReportModule;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Widget;

public class ContentUsageDetailsReport extends Widget{
	
	private String sectionId;
	private String groupId;
	private Date startDate;
	private Date endDate;
	
	private Collection results;
	
	public ContentUsageDetailsReport() {
		
	}
	
	public ContentUsageDetailsReport(String name) {
		super(name);
	}
	
	public void init(){
		
	}
	
	public void onRequest(Event event){
		ReportModule rm = (ReportModule)Application.getInstance().getModule(ReportModule.class);
		results = rm.getContentUsageReportDetails(sectionId, groupId, startDate, endDate);
	}
	
	public String getDefaultTemplate() {
		return "cms/admin/detailsReport";
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Collection getResults() {
		return results;
	}

	public void setResults(Collection results) {
		this.results = results;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	

}
