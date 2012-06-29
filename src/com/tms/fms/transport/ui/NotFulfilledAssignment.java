package com.tms.fms.transport.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.ekms.manpowertemp.model.ManpowerAssignmentObject;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;


public class NotFulfilledAssignment extends Form
{
    private TextBox reasonTB;
    private Button rejectButton,cancelButton;   
    private String id;
    private TransportRequest reQ;
    private String userId;
    
    public TransportRequest getReQ() {
		return reQ;
	}

	public void setReQ(TransportRequest reQ) {
		this.reQ = reQ;
	}

	public NotFulfilledAssignment()
    {
    }

    public NotFulfilledAssignment(String s)
    {
        super(s);
    }

    public void onRequest(Event event)
    {
    	userId = getWidgetManager().getUser().getId();
    	if(!(event.getRequest().getParameter("userId")==null || "".equals(event.getRequest().getParameter("userId"))))
    		userId = event.getRequest().getParameter("userId");
    	
    	id = event.getRequest().getParameter("id");    	
    	TransportModule tM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
    	reQ = new TransportRequest();
    	
    	try{
    		reQ = tM.getDriverAssignmentByAssgIdUserId(id, userId);
    	}catch(Exception er){
    		
    	}
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
        ManpowerAssignmentObject manPower = new ManpowerAssignmentObject();
        if(rejectButton.getAbsoluteName().equals(buttonClicked)){
        	
        	TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
        	SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);
        	        	
        	String reason = (String) reasonTB.getValue();
        	String status = SM.UNFULFILLED_STATUS;
        	manPower.setReason(reason);
        	manPower.setStatus(status);
        	manPower.setId(id);
        	manPower.setManpowerId(userId);
        	if(!(id == null || "".equals(id))){
	        	try{        		        			
	        		TM.updateUnfulfilledAssignment(manPower);
	        	}catch(Exception e){
	        		Log.getLog(getClass()).error("Error when cancel request"+e);
	        	}
        	}
            
        	
        	init();
            return new Forward("rejected");
        }else if(cancelButton.getAbsoluteName().equals(buttonClicked))
            return new Forward("cancel");
        return null;
    }

    public String getDefaultTemplate()
    {
        return "fms/unfulfilledAssignment";
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}

