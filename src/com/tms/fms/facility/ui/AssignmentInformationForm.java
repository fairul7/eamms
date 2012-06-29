package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;

import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Event;

public class AssignmentInformationForm extends Form {
	private Collection col;
	private static long refreshRate;
	private String footerMessage;
	
	public void onRequest(Event event) {
		EngineeringRequest er = new EngineeringRequest();
		EngineeringModule mod = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);

		int noOfDays = 1; 
		refreshRate = 1;
		footerMessage = "";
		
		er = mod.getGlobalAssignment();
		if (er != null) {
			noOfDays = er.getNoOfDays();
			refreshRate = er.getRefreshRate()>0?er.getRefreshRate():1;
			footerMessage = er.getFooterMessage();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date startDate = cal.getTime();
		Date endDate = cal.getTime(); 

		if (noOfDays > 1) {
			cal.add(Calendar.DATE, noOfDays-1);
			endDate = cal.getTime();
		}
		
		col = mod.getAssignmentInformation(startDate, endDate);
	}
	
	public String getDefaultTemplate() {
		return "fms/assignmentInformationTpl";
	}

	public Collection getCol() {
		return col;
	}

	public void setCol(Collection col) {
		this.col = col;
	}

	public static long getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(long refreshRate) {
		this.refreshRate = refreshRate;
	}

	public String getFooterMessage() {
		return footerMessage;
	}

	public void setFooterMessage(String footerMessage) {
		this.footerMessage = footerMessage;
	}
}
