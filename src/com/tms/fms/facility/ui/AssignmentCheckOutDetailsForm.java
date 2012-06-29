package com.tms.fms.facility.ui;

import java.util.Date;

import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class AssignmentCheckOutDetailsForm extends Form {
	public String groupId;
	private Assignment asg;
	protected DatePopupField reportDateFrom;
	protected DatePopupField reportDateTo;
	protected Button checkOut;
	private Date startDate=new Date();
	private Date endDate=new Date();
	private String requestId;
	
	public void init() {
		groupId = null;
		asg = null;
		
		//FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
		try{
			reportDateFrom = new DatePopupField("reportDateFrom");
			reportDateFrom.setOptional(true);
			//reportDateFrom.setDate(dao.selectAssignmentCheckOutDetailsDate(requestId, "FROM"));
			reportDateFrom.setHidden(true);
			
			reportDateTo = new DatePopupField("reportDateTo");
			reportDateTo.setOptional(true);
			//reportDateTo.setDate(dao.selectAssignmentCheckOutDetailsDate(requestId, "TO"));
			reportDateTo.setHidden(true);
		}catch(Exception e){
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
		checkOut = new Button("checkOut", Application.getInstance().getMessage("fms.facility.reprint"));
		
		// hide reprint button
		checkOut.setHidden(true);
		
		addChild(reportDateFrom);
		addChild(reportDateTo);
		addChild(checkOut);
		
	}
	
	public void onRequest(Event evt) {
		FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
		FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
		asg = module.selectAssignmentCheckOutDetails(requestId);
		evt.getRequest().getSession().getAttribute("dataReqId");
		//requestId = asg.getRequestId();
//		startDate=(Date)evt.getRequest().getSession().getAttribute("fromDateTransport");
//		endDate=(Date)evt.getRequest().getSession().getAttribute("toDateTransport");
		try {
			reportDateFrom.setDate(dao.selectAssignmentCheckOutDetailsDate(requestId, "FROM"));
			reportDateTo.setDate(dao.selectAssignmentCheckOutDetailsDate(requestId, "TO"));
			startDate=dao.selectAssignmentCheckOutDetailsDate(requestId, "FROM");
			endDate=dao.selectAssignmentCheckOutDetailsDate(requestId, "TO");
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
	}
	
	@Override
	public Forward onValidate(Event arg0) {
		String buttonName = findButtonClicked(arg0);
		if (buttonName != null && checkOut.getAbsoluteName().equals(buttonName)) {
			startDate = reportDateFrom.getDate();
			endDate = reportDateTo.getDate();
			arg0.getRequest().getSession().setAttribute("fromDateTransport", reportDateFrom.getDate());
			arg0.getRequest().getSession().setAttribute("toDateTransport", reportDateTo.getDate());
			arg0.getRequest().getSession().setAttribute("dataReqId", requestId);
			return new Forward("PRINTOUT");
		
		}
		return new Forward("");
	}
	
	public String getDefaultTemplate() {
		return "fms/facility/assignmentCheckOutDetailsFormTpl";
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Assignment getAsg() {
		return asg;
	}

	public void setAsg(Assignment asg) {
		this.asg = asg;
	}
	public DatePopupField getReportDateFrom() {
		return reportDateFrom;
	}

	public void setReportDateFrom(DatePopupField reportDateFrom) {
		this.reportDateFrom = reportDateFrom;
	}

	public DatePopupField getReportDateTo() {
		return reportDateTo;
	}

	public void setReportDateTo(DatePopupField reportDateTo) {
		this.reportDateTo = reportDateTo;
	}

	public Button getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Button checkOut) {
		this.checkOut = checkOut;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
