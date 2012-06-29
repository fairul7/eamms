package com.tms.fms.transport.ui;

import java.util.Date;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.AssignmentObject;
import com.tms.fms.transport.model.TransportModule;

public class NotFulfilledForm extends Form {
    private TextBox reasonTB;
    private Button rejectButton,cancelButton;   
    private String id;
    private String vehicleNum;

    public NotFulfilledForm(){
    }

    public NotFulfilledForm(String s){
        super(s);
    }

    public void onRequest(Event event){
    	Log.getLog(getClass()).info(id + ' ' + vehicleNum);
        super.onRequest(event);
        init();
    }

    public Forward onSubmit(Event event){
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(event)))
            return new Forward("cancel");
        return super.onSubmit(event);
    }

    public void init() {
        super.init();
        setMethod("POST");
        reasonTB= new TextBox("reasontb");
        reasonTB.addChild(new ValidatorNotEmpty("notempty"));
        reasonTB.setRows("8");
               
        rejectButton = new Button("rejectButton",Application.getInstance().getMessage("fms.tran.submit","Submit"));
        cancelButton = new Button("cancelButton",Application.getInstance().getMessage("fms.tran.cancel","Cancel"));
        addChild(cancelButton);
        addChild(reasonTB);
        addChild(rejectButton);
    }

    public Forward onValidate(Event event) {
        super.onValidate(event);
        String buttonClicked = findButtonClicked(event);
        if(rejectButton.getAbsoluteName().equals(buttonClicked)){
        	
        	TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
        	SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);        	
        	
        	String reason = (String) reasonTB.getValue();
        	String status = SM.UNFULFILLED_STATUS;
        	if(!(id == null || "".equals(id)) && (vehicleNum != null && !"".equals(vehicleNum))){
	        	        		        			
	        	AssignmentObject ao = new AssignmentObject();
	        	ao.setAssgId(id);
	        	ao.setVehicle_num(vehicleNum);
	        	ao.setStatus(status);
	        	ao.setRemarks(reason);
	        	ao.setCreatedBy(Application.getInstance().getCurrentUser().getId());
	        	ao.setCreatedDate(new Date());
	        	ao.setUpdatedBy(Application.getInstance().getCurrentUser().getId());
	        	ao.setUpdatedDate(new Date());
	        	
	        	if (TM.upsertUnfulfilledAssignment(ao)) {
	        		return new Forward("rejected");
	        	} else {
	        		return new Forward("error-unfulfill");
	        	}
	        	
        	}
            
        	init();
            return new Forward("error-unfulfill");
        } else if (cancelButton.getAbsoluteName().equals(buttonClicked))
            return new Forward("cancel");
        return null;
    }

    public String getDefaultTemplate()
    {
        return "fms/unfulfilledform";
    }

    public TextBox getReasonTB()
    {
        return reasonTB;
    }

    public void setReasonTB(TextBox reasonTB)
    {
        this.reasonTB = reasonTB;
    }

    public Button getRejectButton()
    {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton)
    {
        this.rejectButton = rejectButton;
    }

    
    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button canButton)
    {
        this.cancelButton = canButton;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

}

