package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;

public class PrintAssignmentDetailsForm extends Form {
	
	protected String requestId;
	protected EngineeringRequest request=new EngineeringRequest();
	protected PrintServiceAssignmentDetailsForm[] serviceForms; 
	protected Button print;
	protected Button cancel;
	
	protected boolean isHOU=false;
	
	public PrintAssignmentDetailsForm() {
	}

	public PrintAssignmentDetailsForm(String s) {super(s);}

	public void init() {
		Application app=Application.getInstance();
		print = new Button("print", app.getMessage("fms.facility.print"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		
		addChild(print);
		addChild(cancel);
		
	}
	
	public void onRequest(Event arg0) {
		String userId=Application.getInstance().getCurrentUser().getId();
		isHOU=UnitHeadModule.isUnitApprover(userId);
		init();
		populateRequest();
	}

	private void populateRequest(){
		setMethod("post");
		String userId=Application.getInstance().getCurrentUser().getId();
		EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
		request=module.getRequestWithService(requestId);
		boolean viewMode=request.isViewMode();
		Collection manpowerAssignments=uDao.getManpowerAssignments(requestId);
		Collection facilityAssignments=uDao.getFacilityAssignments(requestId);
		Collection services=request.getServices();
		serviceForms=new PrintServiceAssignmentDetailsForm[services.size()];
		int i=0;
		for(Iterator itr=services.iterator();itr.hasNext();i++){
			Service service=(Service)itr.next();
 			serviceForms[i]=new PrintServiceAssignmentDetailsForm("serviceDetails"+i);
			serviceForms[i].setService(service);
			serviceForms[i].setServiceId(service.getServiceId());
			serviceForms[i].setViewMode(viewMode);
			serviceForms[i].setRequestId(requestId);
			//Itereat for Service Assignments
			Collection serviceAssignments=new ArrayList();
			for(Iterator<Assignment> manItr=manpowerAssignments.iterator();manItr.hasNext();){
				Assignment a=(Assignment)manItr.next();
				
				if(a.getServiceType().equals(service.getServiceId())){
					if (a.getUnitId() != null){// && a.getServiceType().equals(ServiceAssignmentDetailsForm.SERVICE_MANPOWER)) {
						if (UnitHeadModule.isUnitApproverByUnitId(userId, a.getUnitId())){
							a.setUnitApprover("1");
						}
					}	
				
					serviceAssignments.add(a);
				}
			}
			manpowerAssignments.removeAll(serviceAssignments);
			for(Iterator<Assignment> facItr=facilityAssignments.iterator();facItr.hasNext();){
				Assignment a=(Assignment)facItr.next();
				if(a.getServiceType().equals(service.getServiceId())){
					serviceAssignments.add(a);
				}
			}
			facilityAssignments.removeAll(serviceAssignments);
			serviceForms[i].setAssignments(serviceAssignments);
			addChild(serviceForms[i]);
		}
	}
	
	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			return new Forward(Form.CANCEL_FORM_ACTION, "requestDetails.jsp", true);
		}  else {return result;}
	}
	@Override
	public Forward onValidate(Event arg0) {
		String buttonName = findButtonClicked(arg0);
		if (buttonName != null && print.getAbsoluteName().equals(buttonName)) {
			return new Forward("PRINT");
		} 
		
		return new Forward("");
	}
	
	public String getDefaultTemplate() {
		return "fms/engineering/printAssignmentDetailsFormTemp";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public EngineeringRequest getRequest() {
		return request;
	}

	public void setRequest(EngineeringRequest request) {
		this.request = request;
	}

	public boolean getIsHOU() {
		return isHOU;
	}

	public void setHOU(boolean isHOU) {
		this.isHOU = isHOU;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getPrint() {
		return print;
	}

	public void setPrint(Button print) {
		this.print = print;
	}

	public PrintServiceAssignmentDetailsForm[] getServiceForms() {
		return serviceForms;
	}

	public void setServiceForms(PrintServiceAssignmentDetailsForm[] serviceForms) {
		this.serviceForms = serviceForms;
	}	

}
