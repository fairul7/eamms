package com.tms.collab.isr.ui;

import kacang.Application;
import kacang.stdui.Panel;
import kacang.ui.Event;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.StatusObject;

public class ISRStatusPortlet extends Panel {
	private int requesterNewRequestCount = 0;
	private int requesterInProgressRequestCount = 0;
	private int requesterClarificationRequestCount = 0;
	private int requesterResolvedRequestCount = 0;
	private int attendantNewRequestCount = 0;
	private int attendantInProgressRequestCount = 0;
	private int attendantClarificationRequestCount = 0;
	private int attendantResolvedRequestCount = 0;
	public static final String DEFAULT_TEMPLATE = "isr/isrStatusPortlet";
	
	public void init() {
	}
	
	public void onRequest(Event evt){
		Application app = Application.getInstance();
		RequestModel requestModel = (RequestModel) app.getModule(RequestModel.class);
		
		requesterNewRequestCount = requestModel.selectRequestCount(null, StatusObject.STATUS_ID_NEW, null);
		requesterInProgressRequestCount = requestModel.selectRequestCount(null, StatusObject.STATUS_ID_IN_PROGRESS, null);
		requesterClarificationRequestCount = requestModel.selectRequestCount(null, StatusObject.STATUS_ID_CLARIFICATION, null);
		requesterResolvedRequestCount = requestModel.selectRequestCount(null, StatusObject.STATUS_ID_COMPLETED, null);
		
		attendantNewRequestCount = requestModel.selectAttendingRequestCount(null, null, StatusObject.STATUS_ID_NEW, null, null, null, null);
		attendantInProgressRequestCount = requestModel.selectAttendingRequestCount(null, null, StatusObject.STATUS_ID_IN_PROGRESS, null, null, null, null);
		attendantClarificationRequestCount = requestModel.selectAttendingRequestCount(null, null, StatusObject.STATUS_ID_CLARIFICATION, null, null, null, null);
		attendantResolvedRequestCount = requestModel.selectAttendingRequestCount(null, null, StatusObject.STATUS_ID_COMPLETED, null, null, null, null);
	}

	public String getDefaultTemplate(){
        return DEFAULT_TEMPLATE;
    }

	public int getAttendantClarificationRequestCount() {
		return attendantClarificationRequestCount;
	}

	public int getAttendantInProgressRequestCount() {
		return attendantInProgressRequestCount;
	}

	public int getAttendantNewRequestCount() {
		return attendantNewRequestCount;
	}

	public int getAttendantResolvedRequestCount() {
		return attendantResolvedRequestCount;
	}

	public int getRequesterClarificationRequestCount() {
		return requesterClarificationRequestCount;
	}

	public int getRequesterInProgressRequestCount() {
		return requesterInProgressRequestCount;
	}

	public int getRequesterNewRequestCount() {
		return requesterNewRequestCount;
	}

	public int getRequesterResolvedRequestCount() {
		return requesterResolvedRequestCount;
	}
}
