package com.tms.fms.register.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
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


public class RejectForm extends Form
{
    private TextBox reasonTB;
    private Button rejectButton,cancelButton;   
    private String id;
    

    public RejectForm()
    {
    }

    public RejectForm(String s)
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
        Date today = new Date();
        
        if(rejectButton.getAbsoluteName().equals(buttonClicked)){
        	
        	FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
        	if(id != null){
	        	try{        		        			
	        		manager.updateFMSUser(manager.REJECTED_FMS_USER, (String)reasonTB.getValue(), today, id);
	        		rejectNofication(event, id); 
	        	}catch(Exception e){
	        		Log.getLog(getClass()).error("Error when cancel request"+e);
	        	}
        	}
        	
        	id=null;
        	init();
            return new Forward("rejected");
        }else if(cancelButton.getAbsoluteName().equals(buttonClicked))
            return new Forward("cancel");
        return null;
    }
    
    private void rejectNofication(Event event, String userId){
		
		Application app = Application.getInstance();
		String typeProb = "";		
		String link = "";
		String footer = "";
		String body = "";
		
		FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
		FMSDepartmentManager deptman = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
		User user = new User();
		user = null;
		Map map = new HashMap();
		String dept = "";
		String unit = "";
		
		
		try{
			user = manager.getFMSUser(userId);
		}catch(Exception er){}
		
		
		if(null != user){
			
			
			try{
			dept = (String) user.getProperty("department");
			unit = (String) user.getProperty("unit");
			}catch(Exception er){}
			
			if(!(null == dept || null == unit)){
				
				try{
					
					map =  deptman.getDeptUnitUser(user.getId());
					
					for(Iterator it = map.keySet().iterator(); it.hasNext(); ){
						it.next();
						unit = (String)map.get("unit");
						dept = (String)map.get("department");
						
					}
				}catch(Exception er){}
			}    			
			
			try {
				    	       
		        body = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">" +
	            "</head><body>" +
	            "<style>" +
	            ".contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}" +
	            "</style>" +
	            "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">" +
	            "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">" +
	            "<b><U>" +
	            app.getMessage("fms.notification.yourRegRejected") +
	            "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>" +
	            "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>" +
	            "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">" +
	            "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
	            
	            "<td class=\"contentBgColorMail\" width=\"90%\">" + app.getMessage("fms.notification.yourRegRejected")+app.getMessage("calendar.label.reasonforRejec")+" :"+reasonTB.getValue()+"<BR/><BR/>"+app.getMessage("fms.notification.plsContactAdmin") + "</td></tr>" +
	            "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +            
	            "</td></tr></table></td></tr><tr><td colspan=\"2\" valign=\"TOP\"  class=\"contentBgColorMail\">&nbsp;</td>" +
	            "</tr></table><p>&nbsp; </p></body></html>";
				    					
				
			}catch(Exception e){
				Log.getLog(getClass()).error(e.toString(), e);
			}		
			
			
			String emailTo[] = {(String) (user.getProperty("email1"))};    		
			String subject = app.getMessage("fms.notification.yourRegRejected");    	    		
			FmsNotification notification = new FmsNotification();
			notification.send(emailTo, subject, body);
		}
	
	}
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefaultTemplate()
    {
        return "fms/rejectuser";
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

}

