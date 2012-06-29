package com.tms.collab.timesheet.model;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsUtil;

import kacang.Application;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

public class TimeSheetDWRModule implements Serializable {
	public boolean insertMandayReportRemarks(String taskId, String assigneeUserId, String remarks) {
		TimeSheetModule module = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
		return module.insertMandayReportRemarks(taskId, assigneeUserId, remarks);
    }

    public String getMandayReportRemarks(String taskId, String assigneeUserId) {
    	TimeSheetModule module = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
    	String remarks = module.getMandayReportRemarks(taskId, assigneeUserId);
    	return remarks;
    }
    
    public int getWorkingDays(Date due, Date start, String projectId) {
    	WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
    	Project project;
		try {
			project = wm.getProject(projectId);
			int day= WormsUtil.getWorkingDays(project.getProjectWorking(), start, due);
			return day;
		} catch (WormsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
    	
    }
    
    public boolean setSelectedItems(String checkedItemsString) {
    	WebContext ctx = WebContextFactory.get();
    	HttpServletRequest request = ctx.getHttpServletRequest();
    	HttpSession session = request.getSession(true);
    	if(session != null) {
    		session.setAttribute("tsMandaysReportCheckedItemsString", checkedItemsString);
    		return true;
    	}
    	else { 
    		return false;
    	}
    }
}
