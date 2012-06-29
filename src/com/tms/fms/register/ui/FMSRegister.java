package com.tms.fms.register.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Password;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorSelectBoxNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Encryption;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentDao;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.transport.model.FmsNotification;
import com.tms.ldap.LdapSyncBean;

public class FMSRegister extends Form
{
	public static final String STATUS = "New";
	 
	protected String id;
	protected TextField fname;
	protected TextField lname;	
	protected TextField designation;	
	protected TextField email;    
	protected Button saveButton, cancelButton;    
    protected Password password;
    protected Password confirmPassword;
    
    protected boolean editMode = false;
    protected boolean publicMode = false;
    
    public static final String SITE_SMPT = "siteSmtpServer";
	public static final String SITE_ADMIN_EMAIL = "siteAdminEmail" ;
	
	protected ValidatorMessage password_vmsg;
	protected ValidatorMessage confirmPassword_vmsg;
    protected ValidatorMessage username_vmsg;
    
    protected ValidatorNotEmpty validPassword;
    protected ValidatorNotEmpty validConfirmPassword;
    
    String username = "";
    
    protected SelectBox sbDepartment;
    protected SelectBox sbUnit;
    
    protected TextField tfStaffNo;
    protected TextField tfTelOffice;
    protected TextField tfTelMobile;
    
	
	public FMSRegister(){
		super();
	}
	
	public FMSRegister(String s){
		super(s);
	}
	
	public void init()
	{
		initFMS();
	}
	public void initFMS()
	{
		
		Map cmap = null;
        try {
            cmap = ((FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class)).getFMSDepartments();
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        
        for (Iterator i = cmap.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
           
        }
        
       try {
	    	FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
			Collection lstUnit = dao.selectUnit();
			//unit.addOption("-1", "-- Please Select --");			
		    if (lstUnit.size() > 0) {
		    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
		        	FMSUnit o = (FMSUnit)i.next();
		        	//unit.addOption(o.getId(),o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		
		

		
		fname = new TextField("fname");
		fname.setSize("60");		
		fname.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
			
        lname = new TextField("lname");
        lname.setSize("60");		
        lname.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
        		
        email = new TextField("email");
        email.setSize("60");		
        email.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
        email.addChild(new ValidatorEmail("validEmail"));
		
        designation = new TextField("designation");
        designation.setSize("60");		
           
        saveButton = new Button("saveButton", Application.getInstance().getMessage("security.label.submit", "Submit"));   
        cancelButton = new Button("cancelbutton", Application.getInstance().getMessage("security.label.cancel", "Cancel"));        
        
        password = new Password("password");
        password_vmsg = new ValidatorMessage("password_vmsg");
        password.addChild(password_vmsg);
        password.setSize("15");
 
        confirmPassword = new Password("confirmPassword");
        confirmPassword_vmsg = new ValidatorMessage("confirmPassword_vmsg");
        confirmPassword.addChild(confirmPassword_vmsg);
        confirmPassword.setSize("15");
        
        //addChild(department);
        //addChild(unit);
        addChild(fname);
        addChild(lname);
        addChild(email);
        addChild(designation);        
        addChild(saveButton);
        addChild(cancelButton);  
        addChild(password);
        addChild(confirmPassword);
        
        ////
        String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
        String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
        
        sbDepartment = new SelectBox("sbDepartment");
	    sbDepartment.setOptions(initialSelect);
	    try {
			FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
			Collection lstDepartment = dao.selectDepartment();
		    if (lstDepartment.size() > 0) {
		    	for (Iterator i=lstDepartment.iterator(); i.hasNext();) {
		        	FMSDepartment o = (FMSDepartment)i.next();
		        	sbDepartment.setOptions(o.getId()+"="+o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		sbDepartment.addChild(new ValidatorSelectBoxNotEmpty("sbDepartmentNotEmpty", msgNotEmpty));
		sbDepartment.setOnChange("setDepartmentChange()");
	    addChild(sbDepartment);
	    
	    sbUnit = new SelectBox("sbUnit");
	    sbUnit.setOptions(initialSelect);
	    sbUnit.addChild(new ValidatorSelectBoxNotEmpty("sbUnitNotEmpty", msgNotEmpty));
	    addChild(sbUnit);
        ////
        
	    tfStaffNo = new TextField("tfStaffNo");
	    tfStaffNo.setSize("30");		
	    tfStaffNo.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
        addChild(tfStaffNo);
        
        tfTelMobile = new TextField("tfTelMobile");
        tfTelMobile.setSize("30");		
        tfTelMobile.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
        addChild(tfTelMobile);
        
        tfTelOffice = new TextField("tfTelOffice");
        tfTelOffice.setSize("30");		
        tfTelOffice.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
        addChild(tfTelOffice);
	}	
	
	public String getDefaultTemplate() {    
				
		return "fms/register";				
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
	
	
	public Forward onSubmit(Event event) {
        Forward fwd = super.onSubmit(event);
        String pwd = (String) password.getValue();
        String cpwd = (String) confirmPassword.getValue();

        Application app = Application.getInstance();
        
        if (!pwd.equals(cpwd)) {
			this.setInvalid(true);
			password_vmsg.showError(app.getMessage("fms.password.notsame.error"));
			confirmPassword_vmsg.showError(app.getMessage("fms.password.notsame.error"));
		}
       
        return fwd;
    }
	
	public Forward onValidate(Event evt) {				
				
		FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		username = (String)email.getValue();
		String[] ext = username.split("@");		
		for(int i=0; i<ext.length; i++){
			username = ext[i];
        	break;
        }					

		Collection existuser = new ArrayList();	
		Collection exist = new ArrayList();
		try{			
			existuser = manager.selectExistUser(username,manager.PENDING_FMS_USER, manager.ACCEPTED_FMS_USER);	//rejected user still can register
			exist = service.getUsersByUsername(username);
			
		}catch(Exception er){Log.getLog(getClass()).error("Error on check Existing User:"+er);}
		
		
		if(existuser.size() == 0 && exist.size() == 0){
			LdapSyncBean ldap = new LdapSyncBean();		
			try{
				if(ldap.ADUserExist(username)){
					
					User user = new User();		
					
					user.setId(UuidGenerator.getInstance().getUuid());
			        user.setProperty("firstName", (String) fname.getValue());
			        user.setProperty("lastName", (String) lname.getValue());		        
			        user.setUsername(username);
			        user.setPassword(Encryption.encrypt((String) password.getValue()));
			        user.setProperty("weakpass", Encryption.encrypt((String)password.getValue()));
			        user.setProperty("unit", getSelectBoxValue(sbUnit));
			        user.setProperty("department", getSelectBoxValue(sbDepartment));
			        user.setProperty("designation", (String)designation.getValue());
			        user.setProperty("email1", email.getValue());		        
			        user.setProperty("active", false);
			        user.setProperty("statusDate", new Date());
			        user.setProperty("staffID", (String)tfStaffNo.getValue());
			        user.setProperty("telMobile", (String)tfTelMobile.getValue());
			        user.setProperty("telOffice", (String)tfTelOffice.getValue());
			        
			        try {
			        	manager.addFMSUser(user,false);
			        	pendingNofication(evt, user);
					} catch (SecurityException e) {
						Log.getLog(getClass()).error( e, e);
					}
					initFMS();
					return new Forward("AD Exist");
				}
				else
					return new Forward("AD Not Exist");
			
			}catch(Exception er){
				Log.getLog(getClass()).error("LdapSyncBean: "+er);
			}
		}else
			return new Forward("Registered");
		
		return new Forward();	
	}
	
	public void onRequest(Event event){
		
		super.onRequest(event);
		
		String existUser = getWidgetManager().getUser().getId();		
		if (!(existUser == null || "anonymous".equals(existUser))) {			
			
			//setEditMode(true);
		}else
			setPublicMode(true);		
		
		
	}
	
	protected User buildUser(String id) {
        User user = new User();

        username = (String)email.getValue();
		String[] ext = username.split("@");		
		for(int i=0; i<ext.length; i++){
			username = ext[i];
        	break;
        }
        user.setId(id);
        user.setUsername(username);
        user.setPassword(Encryption.encrypt((String) password.getValue()));
        user.setProperty("firstName", fname.getValue());
        user.setProperty("lastName", lname.getValue());     
        user.setProperty("department", getSelectBoxValue(sbDepartment));
        user.setProperty("unit", getSelectBoxValue(sbUnit));
        user.setProperty("designation", (String)designation.getValue());
        user.setProperty("email1", email.getValue());        
       
        return user;
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
	
	private void pendingNofication(Event event, User user){
		
		Application app = Application.getInstance();
		String typeProb = "";		
		String link = "";
		String footer = "";
		String body = "";
		String dept = "";
		String unit = "";
		String wordings = "";
		String subject = "";
		
		try{
		dept = (String) user.getProperty("department");
		unit = (String) user.getProperty("unit");
		}catch(Exception er){}
		
		FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
		FMSDepartmentManager deptman = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
		Map map = new HashMap();
		if(!(null == dept || null == unit)){
			
			try{
				
				map =  deptman.getDeptUnitUser(user.getId());
				
				for(Iterator it = map.keySet().iterator(); it.hasNext(); ){
					it.next();
					unit = (String)map.get("unit");
					dept = (String)map.get("department");
					wordings = app.getMessage("fms.notification.yourRegPending");   
					subject = wordings.replace("{NAME}", user.getName());
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
            subject  +
            "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>" +
            "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>" +
            "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">" +
            "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
            
            "<b>" + app.getMessage("fms.notification.name") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + user.getName() + "</td></tr>" +
            "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
            "<b>" + app.getMessage("fms.notification.username") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + user.getUsername() + "</td></tr>" +
            "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
            "<b>" + app.getMessage("fms.notification.department") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + dept + "</td></tr>" +
            "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
            "<b>" + app.getMessage("fms.notification.unit") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + unit + "</td></tr>" +
            "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
            "<b>"  + app.getMessage("fms.notification.designation") +"</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + user.getProperty("designation") + "</td></tr>" +
            "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +            
            "</td></tr></table></td></tr><tr><td colspan=\"2\" valign=\"TOP\"  class=\"contentBgColorMail\">&nbsp;</td>" +
            "</tr></table><p>&nbsp; </p></body></html>";
			    					
			
		}catch(Exception e){
			Log.getLog(getClass()).error(e.toString(), e);
		}		
		    		
		//send notification to all sys admin
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		Collection getAdminUsers = new ArrayList();
		
		int i = 0;
		try{
			getAdminUsers = service.getGroupUsers(FMSDepartmentManager.SYSTEM_ADMIN_GROUP);
		
		}catch(Exception er){}
		
		String emailTo[] = new String[getAdminUsers.size()];
		for(Iterator it = getAdminUsers.iterator(); it.hasNext(); ){
			
			User adminUser = (User) it.next();
			emailTo[i] = (String)adminUser.getProperty("email1");
			i++;
		}		   
		
		if(emailTo.length > 0){
			wordings = app.getMessage("fms.notification.yourRegPending");   
			subject = wordings.replace("{NAME}", user.getName());
			FmsNotification notification = new FmsNotification();
			notification.send(emailTo, subject, body);
		}
		
	}
		
	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public TextField getReporter() {
		return fname;
	}

	public void setReporter(TextField reporter) {
		this.fname = reporter;
	}


	public TextField getFname() {
		return fname;
	}

	public void setFname(TextField fname) {
		this.fname = fname;
	}

	public TextField getLname() {
		return lname;
	}

	public void setLname(TextField lname) {
		this.lname = lname;
	}

	public TextField getEmail() {
		return email;
	}

	public void setEmail(TextField email) {
		this.email = email;
	}

	
	public TextField getDesignation() {
		return designation;
	}

	public void setDesignation(TextField designation) {
		this.designation = designation;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isPublicMode() {
		return publicMode;
	}

	public void setPublicMode(boolean publicMode) {
		this.publicMode = publicMode;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	public Password getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(Password confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public SelectBox getSbDepartment() {
		return sbDepartment;
	}

	public void setSbDepartment(SelectBox sbDepartment) {
		this.sbDepartment = sbDepartment;
	}

	public SelectBox getSbUnit() {
		return sbUnit;
	}

	public void setSbUnit(SelectBox sbUnit) {
		this.sbUnit = sbUnit;
	}

	public TextField getTfStaffNo() {
		return tfStaffNo;
	}

	public void setTfStaffNo(TextField tfStaffNo) {
		this.tfStaffNo = tfStaffNo;
	}

	public TextField getTfTelOffice() {
		return tfTelOffice;
	}

	public void setTfTelOffice(TextField tfTelOffice) {
		this.tfTelOffice = tfTelOffice;
	}

	public TextField getTfTelMobile() {
		return tfTelMobile;
	}

	public void setTfTelMobile(TextField tfTelMobile) {
		this.tfTelMobile = tfTelMobile;
	}
}
