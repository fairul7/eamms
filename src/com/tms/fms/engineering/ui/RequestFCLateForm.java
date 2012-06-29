package com.tms.fms.engineering.ui;

import java.util.Date;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;

/**
 * @author fahmi
 *
 */
public class RequestFCLateForm extends Form {
	protected Label idLbl;
	protected Hidden hdRequestId;
	protected Label nameLbl;
	protected TextField lateCharges;
	protected String requestId;   
	
	protected Hidden stat;	
	
	protected Button submitButton;
	protected Button cancelButton;
	
	private String cancelUrl = "outsourceRequestFC.jsp?requestId=";
	
	private String action = "";
	
	public RequestFCLateForm() {

    }
   
	public void init()
	{
		setColumns(1);
		setMethod("POST");
		String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");
		
		idLbl = new Label("idLbl");
		addChild(idLbl);
		
		nameLbl = new Label("nameLbl");
		addChild(nameLbl);
		
		lateCharges = new TextField("lateCharges");
		lateCharges.setSize("7");
		lateCharges.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		lateCharges.addChild(new ValidatorIsNumeric("lateChargesIsNumeric", msgIsNumberic, false));
		addChild(lateCharges);
		
		hdRequestId = new Hidden("hdRequestId");
		addChild(hdRequestId);
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("fms.facility.submit", "Submit"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));    
        cancelButton.setOnClick("window.close();");
        
        addChild(submitButton);
		addChild(cancelButton);	
		
	}	 
	
	public void populateForm(String id){
		Application app = Application.getInstance();
		FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)app.getModule(FacilitiesCoordinatorModule.class);
		try{
			EngineeringRequest er = fcm.getRequestById(id);		
			
			idLbl.setText(er.getRequestId());
			
			nameLbl.setText(er.getTitle());			
			
			hdRequestId.setValue(er.getRequestId());
		} catch(Exception e){
		}
		
	}
	
	public String getDefaultTemplate() {
		return "fms/requestFClateform";
    }
	
	public void onRequest(Event evt){
		this.setInvalid(false);
		//requestId = evt.getRequest().getParameter("requestId");
		action = evt.getRequest().getParameter("do");		
		init();
		populateForm(getRequestId());
	}
	
	public Forward onValidate(Event evt){
		Application application = Application.getInstance();
		FacilitiesCoordinatorModule module = (FacilitiesCoordinatorModule)application.getModule(FacilitiesCoordinatorModule.class);
		
		Date now = new Date();
		EngineeringRequest er = new EngineeringRequest();
		er.setLateCharges((String)lateCharges.getValue());
		er.setRequestId((String)hdRequestId.getValue());
		er.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
		er.setModifiedOn(now);
		
		module.updateRequestLate(er);
		
		Forward fwd = new Forward("UPDATE");
		init();
		return fwd;
	}		
	

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}
	public String getCancelUrl(){
		return cancelUrl;
	}	
	public void setCancelUrl(String cancelUrl){
		this.cancelUrl=cancelUrl;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Label getNameLbl() {
		return nameLbl;
	}

	public void setNameLbl(Label nameLbl) {
		this.nameLbl = nameLbl;
	}

	public Label getIdLbl() {
		return idLbl;
	}

	public void setIdLbl(Label idLbl) {
		this.idLbl = idLbl;
	}

	public TextField getLateCharges() {
		return lateCharges;
	}

	public void setLateCharges(TextField lateCharges) {
		this.lateCharges = lateCharges;
	}

	public Hidden getHdRequestId() {
		return hdRequestId;
	}

	public void setHdRequestId(Hidden hdRequestId) {
		this.hdRequestId = hdRequestId;
	}
	
}
