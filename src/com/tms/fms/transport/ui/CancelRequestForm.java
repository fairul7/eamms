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

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.FmsNotification;
import com.tms.fms.transport.model.TransportModule;


public class CancelRequestForm extends Form
{
    private TextBox reasonTB;
    private Button rejectButton,cancelButton;   
    private String requestId;
    

    public CancelRequestForm()
    {
    }

    public CancelRequestForm(String s)
    {
        super(s);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        
    }

    public Forward onSubmit(Event event)
    {
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(event)))
            return new Forward("cancel");
        return super.onSubmit(event);
    }

    public void init()
    {
        super.init();
        reasonTB= new TextBox("reasontb");
        reasonTB.addChild(new ValidatorNotEmpty("notempty"));
        reasonTB.setRows("8");
               
        rejectButton = new Button("rejectButton",Application.getInstance().getMessage("fms.tran.submit","Submit"));
        cancelButton = new Button("cancelButton",Application.getInstance().getMessage("fms.tran.cancel","Cancel"));
        addChild(cancelButton);
        addChild(reasonTB);
        addChild(rejectButton);
    }

    public Forward onValidate(Event event)
    {
        super.onValidate(event);
        String buttonClicked = findButtonClicked(event);
        if(rejectButton.getAbsoluteName().equals(buttonClicked)){
        	
        	TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
        	SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);
        	FMSRegisterManager FRM = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
        	
        	
        	String reason = (String) reasonTB.getValue();
        	String status = SM.CANCELLED_STATUS;
        	String userId = getWidgetManager().getUser().getId();
        	if(!(requestId == null || "".equals(requestId))){
	        	try{        		        			
	        		TM.updateStatus(status, reason, requestId, userId, new Date());
	        		TM.insertRequestStatus(requestId, status, reason);
	        		
	        		try{
						String department = FRM.getUserDepartment(userId);
						String wordings = Application.getInstance().getMessage("fms.label.requestIdCancelled");
						TM.sendNotificationToApprovers(department,requestId,wordings);
					}catch(Exception er){
						Log.getLog(getClass()).error("ERROR sendNotification "+er);
					}
	        	}catch(Exception e){
	        		Log.getLog(getClass()).error("Error when cancel request"+e);
	        	}
        	}
            
        	Log.getLog(getClass()).info(requestId);
        	init();
            return new Forward("rejected");
        }else if(cancelButton.getAbsoluteName().equals(buttonClicked))
            return new Forward("cancel");
        return null;
    }

    
    public String getDefaultTemplate()
    {
        return "fms/cancelform";
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

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}

