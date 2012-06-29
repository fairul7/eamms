package com.tms.collab.project.ui;

import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.collab.project.Report;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;

public class ProjectPerformanceMetricReportPrint extends Form {
    protected String reportId;
    protected Report report;
    protected boolean summary=false;
    protected boolean schedule=false;
    protected boolean effort=false;
    protected boolean defects=false;
    protected boolean cost=false;
    
    public String getDefaultTemplate() {
        return "project/projectPerformanceMetricReportPrint";
    }

    public void init() {
        super.init();
        setMethod("POST");     
    }

    public void onRequest(Event ev) {
        removeChildren();
        init();   
        WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        try {
        	summary=false;
        	schedule=false;
        	effort=false;
        	defects=false;
        	cost=false;
        	if(reportId!=null){
			report=wm.getReport(reportId, true);
			if(report.getProjects()!=null){
				summary=true;
			}if(report.getMilestones().size()>0){
				schedule=true;
			}if(report.getTasks().size()>0){
				effort=true;
			}if(report.getDefects().size()>0){
				defects=true;
			}if(report.getCost().size()>0){
				cost=true;
			}			
        	}
		} catch (WormsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        	
    }
    
    public Forward onValidate(Event evt) {
        Forward forward = super.onValidate(evt);       
        return forward;
    }

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public boolean isCost() {
		return cost;
	}

	public void setCost(boolean cost) {
		this.cost = cost;
	}

	public boolean isDefects() {
		return defects;
	}

	public void setDefects(boolean defects) {
		this.defects = defects;
	}

	public boolean isEffort() {
		return effort;
	}

	public void setEffort(boolean effort) {
		this.effort = effort;
	}

	public boolean isSchedule() {
		return schedule;
	}

	public void setSchedule(boolean schedule) {
		this.schedule = schedule;
	}

	public boolean isSummary() {
		return summary;
	}

	public void setSummary(boolean summary) {
		this.summary = summary;
	}

   
}

