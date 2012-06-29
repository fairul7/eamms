package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.CheckAvailabilityModule;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;

/**
 * 
 * @author fahmi
 *
 */
public class CheckAvailability extends Form {
	
	protected String requestId;
	
	private Button btnContinue;
	private Button btnCancel;
	
	private Collection facilities 		= new ArrayList();
	private Collection postService 		= new ArrayList();
	private Collection manPowService	= new ArrayList();
	private Collection studioService	= new ArrayList();
	private Collection otherService		= new ArrayList();
	private Collection tvroService		= new ArrayList();
	private Collection vtrService		= new ArrayList();
	
	public void onRequest(Event event) {
		this.setInvalid(false);
		requestId 		= event.getRequest().getParameter("id");
		facilities 		= new ArrayList(); 
		postService 	= new ArrayList();
		manPowService	= new ArrayList();
		studioService 	= new ArrayList();
		otherService	= new ArrayList();
		tvroService		= new ArrayList();
		vtrService		= new ArrayList();
		getTables(requestId);
	}
	
	public void getTables(String requestId) {
		try {
			Application application = Application.getInstance();
			CheckAvailabilityModule mod = (CheckAvailabilityModule)Application.getInstance().getModule(CheckAvailabilityModule.class);
			
			facilities 		= new ArrayList(); 
			postService 	= new ArrayList();
			manPowService	= new ArrayList();
			studioService 	= new ArrayList();
			otherService	= new ArrayList();
			tvroService		= new ArrayList();
			vtrService 		= new ArrayList();
			
			facilities 		= mod.getAllFacility(requestId, "SCP");	
			postService 	= mod.getAllFacility(requestId, "POST");
			manPowService	= mod.getAllFacility(requestId, "MANPOWER");
			studioService 	= mod.getAllFacility(requestId, "STUDIO");
			otherService	= mod.getAllFacility(requestId, "OTHER");
			tvroService		= mod.getAllFacility(requestId, "TVRO");
			vtrService		= mod.getAllFacility(requestId, "VTR");
			
			btnContinue = new Button("btnContinue", application.getMessage("fms.facility.continue"));
			btnCancel	= new Button("btnCancel", application.getMessage("fms.facility.cancel"));
			addChild(btnContinue);
			addChild(btnCancel);
			
		}catch (Exception e) {			
			Log.getLog(getClass()).error(e.toString()); 
		} 
	}	
	
	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
			return new Forward("cancel");
		} else {
			EngineeringModule module	= (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			
			EngineeringRequest request = module.getRequest(requestId);
			if (EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus())) {
				module.submitModifyRequest(requestId);
			} else {
				module.submitRequest(requestId, true);
			}
			return new Forward("continue");
		}
	}
	
	public String getDefaultTemplate(){
		return "fms/checkavailability";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Collection getFacilities() {
		return facilities;
	}

	public void setFacilities(Collection facilities) {
		this.facilities = facilities;
	}

	public Collection getPostService() {
		return postService;
	}

	public void setPostService(Collection postService) {
		this.postService = postService;
	}

	public Collection getManPowService() {
		return manPowService;
	}

	public void setManPowService(Collection manPowService) {
		this.manPowService = manPowService;
	}

	public Collection getStudioService() {
		return studioService;
	}

	public void setStudioService(Collection studioService) {
		this.studioService = studioService;
	}

	public Collection getOtherService() {
		return otherService;
	}

	public void setOtherService(Collection otherService) {
		this.otherService = otherService;
	}

	public Collection getTvroService() {
		return tvroService;
	}

	public void setTvroService(Collection tvroService) {
		this.tvroService = tvroService;
	}

	public Button getBtnContinue() {
		return btnContinue;
	}

	public void setBtnContinue(Button btnContinue) {
		this.btnContinue = btnContinue;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Collection getVtrService() {
		return vtrService;
	}

	public void setVtrService(Collection vtrService) {
		this.vtrService = vtrService;
	}

	
}
