package com.tms.fms.register.ui;

import java.util.Map;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.SelectBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Encryption;
import kacang.util.Log;

import com.tms.fms.register.model.FMSRegisterManager;

public class EditFMSRegister extends FMSRegister
{
	public static final String STATUS = "New";	 
	private User user;
	//private id;
		
	public EditFMSRegister(){
		super();
	}
	
	public EditFMSRegister(String s){
		super(s);
	}
	
	public void init() {
        				
		//initUser();
		this.initFMS();
		cancelButton.setText(Application.getInstance().getMessage("fms.tran.backToList", "Submit"));
    }

    public void onRequest(Event evt)
    {
    	id = evt.getRequest().getParameter("id");
        if (!(id == null || "".equals(id)))
        {
            super.onRequest(evt);
            initUser();
        }
    }
    
    public Forward actionPerformed(Event event)
    {
		Forward forward = new Forward();				
        String butt = findButtonClicked(event);
                      
        if(cancelButton.getAbsoluteName().equals(butt)){
            init();
            return new Forward("Cancel");
        }
        
        
        validPassword = new ValidatorNotEmpty("validPassword");
        validConfirmPassword = new ValidatorNotEmpty("validCurrentPassword");

        password.addChild(validPassword);
        confirmPassword.addChild(validConfirmPassword);
        
		forward = super.actionPerformed(event);
        return forward;
    }

    private void initUser()
    {
        //SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
    	FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
    	
        user = new User();
        try {
            user = manager.getFMSUser(id);
            
            if(user != null){
            	
            fname.setValue(user.getProperty("firstName"));
            lname.setValue(user.getProperty("lastName"));            
            designation.setValue(user.getProperty("designation"));
            email.setValue(user.getProperty("email1"));  
            username = (user.getUsername());
            tfStaffNo.setValue(user.getProperty("staffID"));
            tfTelMobile.setValue(user.getProperty("telMobile"));
            tfTelOffice.setValue(user.getProperty("telOffice"));

            sbDepartment.setSelectedOption((String)user.getProperty("department"));
            sbUnit.setSelectedOption((String)user.getProperty("unit"));
        	//sbUnit;
            //emailSelectBox.setSelectedOptions(new String[]{email.getEmailTypeId()});
            //department.setSelectedOptions(new String[]{(String)user.getProperty("department")});
            //unit.setSelectedOptions(new String[]{(String)user.getProperty("unit")});
            
            }
        } catch (Exception e) {
            Log.getLog(EditFMSRegister.class).error(e.toString(), e);
        }
    }

    public Forward onValidate(Event evt) {
    	
    	Log.getLog(getClass()).info("okie");
    	
    	try{
			//SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
    		FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
			User user = new User();		
			user.setId(id);
	        user.setProperty("firstName", (String) fname.getValue());
	        user.setProperty("lastName", (String) lname.getValue());
	        //user.setProperty("active", false);
	        user.setUsername(username);
	        user.setPassword(Encryption.encrypt((String) password.getValue()));
	        user.setProperty("weakpass", Encryption.encrypt((String)password.getValue()));
	        user.setProperty("department", getSelectBoxValue(sbDepartment));
	        user.setProperty("unit", getSelectBoxValue(sbUnit));
	        user.setProperty("designation", (String)designation.getValue());
	        user.setProperty("email1", email.getValue());		        
	        try {
	        	manager.addFMSUser(user,true);
			} catch (SecurityException e) {
				Log.getLog(getClass()).error( e, e);
			}
    	}catch(Exception e){
    		
    	}
    	
    	return new Forward("fwd_edit");
    }

    private String getSelectBoxValue(SelectBox sb) {
	    if (sb != null) {
	    	Map selected = sb.getSelectedOptions();
	    	if (selected.size() == 1) {
	    		return (String)selected.keySet().iterator().next();
	    	}
	    }
	    return null;
	}
    
    public String getDefaultTemplate() {
        if (id.equals(""))
            return "";
        else
        	return "fms/editregister";				
    }

    public void setId(String id)
    {
        super.setId(id);
        initUser();
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
