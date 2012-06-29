package com.tms.fms.facility.ui;

import com.tms.fms.facility.model.FacilityModule;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;

public class AssignmentInfo extends Form {
	private Label requestTitleLabel;
	private Label requestTitle;
	private Label requestorLabel;
	private Label requestor;
	private Label remarksLabel;
	private Label remarks;
	private Label itemNotCheckinLabel;
	
	private String requestId;
	
	public void init() {
		removeChildren();
		
		requestTitleLabel = new Label("requestTitleLabel", Application.getInstance().getMessage("fms.facility.label.requestTitle", "Request Title"));
		addChild(requestTitleLabel);
		
		requestTitle = new Label("requestTitle");
		addChild(requestTitle);
		
		requestorLabel = new Label("requestorLabel", Application.getInstance().getMessage("fms.facility.label.requestor", "Requestor"));
		addChild(requestorLabel);
		
		requestor = new Label("requestor");
		addChild(requestor);
		
		remarksLabel = new Label("remarksLabel", Application.getInstance().getMessage("fms.facility.label.remarks", "Remarks"));
		addChild(remarksLabel);
		
		remarks = new Label("remarks");
		addChild(remarks);
		
		itemNotCheckinLabel = new Label("itemNotCheckinLabel", Application.getInstance().getMessage("fms.facility.label.itemNotCheckin", "Item(s) not yet check in"));
		addChild(itemNotCheckinLabel);
		
		
	}
	
	public void onRequest(Event evt) {
		FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		DefaultDataObject obj = mod.getAssignmentInfo(requestId);
		requestTitle.setText((String)obj.getProperty("requestTitle"));
		requestor.setText((String)obj.getProperty("requestor"));
		remarks.setText((String)obj.getProperty("remarks"));
		super.onRequest(evt);
	}
	
	public String getDefaultTemplate() {
		return "fms/facility/assignmentInfoTemplate";
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

		
	

}
