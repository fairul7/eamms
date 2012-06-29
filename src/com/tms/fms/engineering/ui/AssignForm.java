package com.tms.fms.engineering.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;

/**
 * 
 * @author fahmi
 *
 */
public class AssignForm extends Form {	
	private String id;
	private String success = "";
	protected String act = "";
	protected String asgId = "";
	protected String userId = "";
	
	protected CheckBox chargeBack;
	protected CheckBox callBack;
	protected Button submit;
	protected Label manpower;
	
	// added for issue #173 
	// : automatically assign same manpower to remaining block booking assignment 
	protected Radio rdYes;
	protected Radio rdNo;
	
	protected String requestId;
	protected boolean blockBooking = false;
		
	public void onRequest(Event event) {
		Application app = Application.getInstance();
		EngineeringModule module = (EngineeringModule)app.getModule(EngineeringModule.class); 
		EngineeringRequest eRequest = new EngineeringRequest();
		
		this.setInvalid(false);
		initForm();
		
		if (userId != null && !"".equals(userId)){
			manpower.setText(module.getUser(userId));
			
			if (asgId!=null && !"".equals(asgId) ){
				eRequest = module.getAssignment(asgId);
				
				if (eRequest != null){
					if (eRequest.getManpowerName() != null && eRequest.getManpowerId().equals(userId)){
						manpower.setText(eRequest.getManpowerName());
						chargeBack.setChecked(eRequest.getChargeBack().equals("1")?true:false);
						callBack.setChecked(eRequest.getCallBack().equals("1")?true:false);
					}
				}
				
				blockBooking = module.isBlockBooking(asgId);
			}	
		}	
	}
	
	public void populateButtons() {
		Application app = Application.getInstance();
		
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		
		addChild(submit);
	}

	public void initForm() {
		setMethod("post");		
		Application app = Application.getInstance();
				
		chargeBack = new CheckBox("chargeBack");
		chargeBack.setText(app.getMessage("fms.label.chargeBack"));
		chargeBack.setValue("charge");
		addChild(chargeBack);
		
		callBack = new CheckBox("callBack");
		callBack.setText(app.getMessage("fms.label.callBack"));
		callBack.setValue("call");
		addChild(callBack);
		
		manpower = new Label("manpower");
		addChild(manpower);
		
		rdYes = new Radio("rdYes", app.getMessage("fms.facility.form.yes"));
		rdYes.setGroupName("blockAssignment");
		addChild(rdYes);
		
		rdNo = new Radio("rdNo", app.getMessage("fms.facility.form.no"));
		rdNo.setGroupName("blockAssignment");
		rdNo.setChecked(true);
		addChild(rdNo);
		
		populateButtons();
		
	}
	
	public Forward onValidate(Event event) {
		String charge = chargeBack.isChecked()?"1":"0";
		String call = callBack.isChecked()?"1":"0";
		
		if (act.equals("assign")) {
			if (asgId!=null && userId!=null){
				if (!"".equals(asgId) && !"".equals(userId)){
					EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
					
					if (rdYes.isChecked()) {
						
						String[] asgIds = module.getOtherAssignmentId(asgId, getRequestId());
						
						if (asgIds != null && asgIds.length > 0) {
							for (int i=0; i<asgIds.length; i++) {
								module.updateManpowerAssignment(asgIds[i], userId, charge, call);
							}
						}
						
					} else {
						module.updateManpowerAssignment(asgId, userId, charge, call);
					}
				}
			}
		}
		
		return new Forward("SUCCESS", "assign.jsp?act=close&requestId=" + getRequestId(), true);
	}

	public String getDefaultTemplate(){
		return "fms/assignTemp";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getAsgId() {
		return asgId;
	}

	public void setAsgId(String asgId) {
		this.asgId = asgId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public CheckBox getChargeBack() {
		return chargeBack;
	}

	public void setChargeBack(CheckBox chargeBack) {
		this.chargeBack = chargeBack;
	}

	public CheckBox getCallBack() {
		return callBack;
	}

	public void setCallBack(CheckBox callBack) {
		this.callBack = callBack;
	}

	public Label getManpower() {
		return manpower;
	}

	public void setManpower(Label manpower) {
		this.manpower = manpower;
	}

	public Radio getRdYes() {
		return rdYes;
	}

	public void setRdYes(Radio rdYes) {
		this.rdYes = rdYes;
	}

	public Radio getRdNo() {
		return rdNo;
	}

	public void setRdNo(Radio rdNo) {
		this.rdNo = rdNo;
	}

	public boolean isBlockBooking() {
		return blockBooking;
	}

	public void setBlockBooking(boolean blockBooking) {
		this.blockBooking = blockBooking;
	}

}
