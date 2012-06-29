package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Link;
import kacang.stdui.TextBox;
import kacang.stdui.TimeField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;
import com.tms.ekms.manpowertemp.model.ManpowerAssignmentObject;
import com.tms.ekms.manpowertemp.model.ManpowerLeaveObject;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.FmsNotification;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleObject;


public class CompleteAssignmentForm extends Form
{
	
	
	public static final String COMPLETE_TEMPLATE = "fms/completeAssignment";
	public static final String DOCUMENT_PATH = "/Transport/Assignment/";
		
	private String mode;
	private String id;
	private String status;	
	protected TimeField time;
	
	private Link requestLink;
	private Label program;
	private Label assignmentDatetime;
	private DatePopupField completionDate;
	private TimeField completionTime;
	private TextBox remarks;
	private FileUpload docFileUpload;
	
	private ManPowerSelect manPowerSelect;
	
	private Button submitButton, cancelButton;
	private Button completeButton, unfulfilledButton, reassignButton, backButton;
	
	private TransportRequest trequest;
	private Map req;
	
	private EngineeringRequest engReq;
	
	private boolean ableToAssign;
	private boolean engineering;
	private String userId;
	
	public TransportRequest getTrequest() {
		return trequest;
	}

	public void setTrequest(TransportRequest trequest) {
		this.trequest = trequest;
	}

	public CompleteAssignmentForm()
    {
    }

    public CompleteAssignmentForm(String s)
    {
        super(s);
    }

	public void init()
	{
		removeChildren();
		time = new TimeField("time");
        time.setTemplate("calendar/timefield");
              
		cancelButton = new Button("cancelButton");
		cancelButton.setText(Application.getInstance().getMessage("fms.tran.cancel", "Cancel"));
		
		submitButton = new Button("submitButton");
		submitButton.setText(Application.getInstance().getMessage("fms.tran.submit", "Submit"));
		
		completeButton = new Button("completeButton");
		completeButton.setText(Application.getInstance().getMessage("fms.label.transport.completionAssg", "Submit"));
		
		unfulfilledButton = new Button("unfulfilledButton");
		unfulfilledButton.setText(Application.getInstance().getMessage("fms.label.transport.unfulfilledAssg", "Submit"));
		
		reassignButton = new Button("reassignButton");
		reassignButton.setText(Application.getInstance().getMessage("fms.label.transport.reassign", "Submit"));
		
		backButton = new Button("backButton");
		backButton.setText(Application.getInstance().getMessage("fms.facility.backToListing"));
		
		requestLink = new Link("requestLink");		
		
		program = new Label("program");
		assignmentDatetime = new Label("assignmentDatetime");
		completionDate = new DatePopupField("completionDate");
		completionDate.setDate(new Date());
		
		completionTime = new TimeField("completionTime");
		remarks = new TextBox("remarks");
		docFileUpload = new FileUpload("docFileUpload");
		
		manPowerSelect = new ManPowerSelect("manPowerSelect");
	    manPowerSelect.setSortable(false);
	        
		addChild(time);				
		addChild(program);
		addChild(requestLink);
		addChild(assignmentDatetime);
		addChild(completionDate);
		addChild(completionTime);
		addChild(remarks);
		addChild(docFileUpload);
		addChild(submitButton);
		addChild(cancelButton);
		addChild(completeButton);
		addChild(unfulfilledButton);
		addChild(reassignButton);
		addChild(backButton);
		addChild(manPowerSelect);
		manPowerSelect.init();
		////
		
		//ENG:
		
		program = new Label("program");
		addChild(program);
				
	}
	
	public Forward onSubmit(Event evt) {
		 
		 
		 kacang.ui.Forward result = super.onSubmit(evt);
		 EngineeringModule eng = new EngineeringModule();
		 TransportModule tm = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		 Calendar start = Calendar.getInstance();
		 start.setTime(completionDate.getDate());                
		 start.set(Calendar.HOUR_OF_DAY, completionTime.getHour());
		 start.set(Calendar.MINUTE, completionTime.getMinute());
		 TransportRequest tr = new TransportRequest();
		 String currentUser = getWidgetManager().getUser().getId();
		 
		 if(userId == null || "".equals(userId)){
			 userId = evt.getRequest().getParameter("userId");
			 if(userId == null || "".equals(userId))
				 userId = currentUser;
		 }
		 
		 try{
	        tr = tm.getDriverAssignmentByAssgIdUserId(id, userId);	        	
				
	     }catch(Exception er){}
			
	     
	     if(!(eng.isValidCompletion(tr.getStartDate(), new Date(), start.getTime()))){
	   			 completionDate.setInvalid(true);
	   			 time.setInvalid(true);
	   			 setInvalid(true);  	   			 
	   			 return new Forward("backToDetail", "completeAssignment.jsp?id=" + id + "userId="+userId, false);		       
		 	}	 			
	     
		
	     return onValidate(evt);
	 }
	
	 public Forward actionPerformed(Event evt) {
        
		 if (cancelButton.getAbsoluteName().equals(findButtonClicked(evt))) {
			 return new Forward("Back");
			}
        return super.actionPerformed(evt);
	 }
	 
    public void onRequest(Event event)
    {    	
    	init();
    	id = event.getRequest().getParameter("id");
    	userId = event.getRequest().getParameter("userId");
    	populateEditForm(id, userId);
        super.onRequest(event);         
        Log.getLog(getClass()).info(id);
        
    }
    
    public void populateEditForm(String id, String manpowerId){
    	
    	TransportModule tm = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);		
		SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);		
		trequest = new TransportRequest();
		req = new SequencedHashMap();
		
		if(manpowerId == null || "".equals(manpowerId))
			manpowerId = getWidgetManager().getUser().getId();
		
        try{
        	trequest = tm.getDriverAssignmentByAssgIdUserId(id, manpowerId);	        	
			String reqUserId = trequest.getManpowerId();
			trequest.setManpowerId(service.getUser(reqUserId).getName());
        }catch(Exception er){}
        
    	manPowerSelect.setSDate(trequest.getStartDate());
		manPowerSelect.setEDate(trequest.getEndDate());
    	
    }
    
    
    public Forward onValidate(Event event)
    {
    	TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);    	
    	ManpowerAssignmentObject mObj = new ManpowerAssignmentObject();
    	EngineeringModule eng = new EngineeringModule();
    	    	
    		Calendar start = Calendar.getInstance();
	        start.setTime(completionDate.getDate());                
	        start.set(Calendar.HOUR_OF_DAY, completionTime.getHour());
	        start.set(Calendar.MINUTE, completionTime.getMinute());
	        TransportRequest tr = new TransportRequest();
	            
        	 try{
 	        	tr = TM.getDriverAssignmentByAssgIdUserId(id, userId);	        	
 				
 	        }catch(Exception er){}
    		if(!(eng.isValidCompletion(tr.getStartDate(), new Date(), start.getTime()))){
	   			 completionDate.setInvalid(true);
	   			 time.setInvalid(true);
	   			 setInvalid(true);  
	   			 
	   			 return new Forward("ErrorOnDate");
    		}
	        	
	    	try {
	    		String docId = UuidGenerator.getInstance().getUuid();
	    		mObj.setId(docId);
                mObj.setReferenceId(id);  
                mObj.setCreatedDate(new Date());
                mObj.setManpowerId(userId);
                StorageFile sf = docFileUpload.getStorageFile(event.getRequest());
                if (sf != null) {                   
                        StorageService ss = (StorageService) Application.getInstance().getService(new StorageService().getClass());
                        
                        sf.setParentDirectoryPath(DOCUMENT_PATH + docId);
                        ss.store(sf);    
                     
                        mObj.setDocuments(sf.getAbsolutePath());
                        mObj.setDocumentsName(sf.getName());                   
                        try{
                			TM.addFileAssignment(mObj);
                		}catch(Exception e){}
                }
                
    		}catch(Exception er){Log.getLog(getClass()).error("Error on uploading file :"+er);}
    	
	    	try{
	    		
	    		mObj.setCompletionDate(start.getTime());
	    		mObj.setReason((String) remarks.getValue());
	    		mObj.setStatus(SetupModule.COMPLETED_STATUS);
	    			    		
	    		TM.updateCompleteAssignment(mObj);
	    		
	    		//check for both vehicles and drivers are closed 
		    	statusCheck(tr.getRequestId(),tr, event);
	    		
	    	}catch(Exception er){
	    		Log.getLog(getClass()).error(er);
	    	}
        return new Forward("Ok");
    }
    
    
    public void statusCheck(String requestId, TransportRequest tr, Event evt){
    	String vehicleStatus = "";
    	String driverStatus = "";
    	TransportModule transModule = (TransportModule)Application.getInstance().getModule(TransportModule.class);
    	//------ VEHICLE STATUS -------
    	try {
    		Collection viewVehicles = transModule.getVehicleByRequestId(requestId);	    	
//    		Collection completedAssg = transModule.selectAssignments(tr.getId(), "E");
    		if(viewVehicles.size() > 0){
    			int countV = 0;
        		for (Iterator iterator = viewVehicles.iterator(); iterator.hasNext();) {
        			VehicleObject object = (VehicleObject) iterator.next();
    				if(object.getStatus()!= null && object.getStatus().equals("E")){
    					countV++;
    				}
    			}
        		
            	if(countV == viewVehicles.size()){
            		vehicleStatus = "C";
            	}    			
    		}
    		
		} catch (Exception e) {}
    	
		//------ DRIVER STATUS -------
		try {
			Collection viewDrivers = transModule.getDriverByRequestId(requestId);
    		int countD = 0;
    		for (Iterator iterator = viewDrivers.iterator(); iterator.hasNext();) {
				ManpowerLeaveObject object = (ManpowerLeaveObject) iterator.next();
				object.getProperty("status");
				if(object.getProperty("status")!= null && object.getProperty("status").equals("M")){
					countD++;
				}
			}
    		// check all vehicles closed
    		if(viewDrivers.size() > 0){
        		if(countD == viewDrivers.size()){
        			driverStatus = "C";
        		}    			
    		}else driverStatus = "NULL";
		} catch (Exception e) {
			// TODO: handle exception Could not find driver status
		}
		
		if(("C".equals(vehicleStatus) && "C".equals(driverStatus)) || (vehicleStatus.equals("C")&&driverStatus.equals("NULL"))){
			sendNotification(tr, evt);
			transModule.updateCloseReqStatus(requestId, SetupModule.CLOSED_STATUS);
		}
    }
    
    
    public void sendNotification(TransportRequest tr,Event evt){
    	//Send Notification
		TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
		FMSDepartmentManager deptManager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
		try {
			TransportRequest objNew = tm.selectTransportRequest(tr.getRequestId());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String sDate = sdf.format(tr.getStartDate());
			String eDate = sdf.format(tr.getEndDate());
			String subject = "Transport Request Feedback Form";
			String emailTo[] = deptManager.getEmailRequestor(tr.getRequestId());
			String requestId = tr.getRequestId();
			String requiredDate = sDate +" - "+eDate+".";
			String rate = objNew.getRate();
//			String link = "http://fms.mediaprima.com.my/ekms/fms/transport/feedbackForm.jsp?requestId="+requestId+"";
			//String serverUrl = "http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() + "/ekms/cmsadmin";
			String link = "http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() + "/ekms/fms/transport/request/feedbackForm.jsp?requestId="+requestId+"";
			String requestorName = "";
			for(String id:emailTo){
				User usrObj = security.getUsersEmail(id);
				requestorName = (String)usrObj.getProperty("firstName") +" "+ (String)usrObj.getProperty("lastName");
			}
			
			String body=
					"<table width=\"100%\" cellpadding=\"5\" cellspacing=\"0\"> " +
					"<tr><td>Dear "+requestorName+",</td></tr>" +
					"<tr><td><td></tr>" +
					"<tr><td>Your request has been completed. Request detail is shown below.<td></tr>" +
					"<tr><td><td></tr>" +
					"</table>" +
					"<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +					
					"<tr><td><b>Request ID : </b>"+requestId+"</td></tr>" +
					"<tr><td><b>Request Title : </b>Completed</td></tr>" +
					"<tr><td><b>Required Date : </b>"+requiredDate+"</td></tr>" +
					"<tr><td><b>Total amount : </b>RM "+rate+".</td></tr>" +
					"<tr><td><td></tr>" +
					"</table>" +
					"<table width=\"100%\" cellpadding=\"5\" cellspacing=\"0\">" +
			    	"<tr><td>Thank you for signing off the job done by us. As a measure of continuous improvements, " +
			    	"we wish you can feedback to us on the work quality and feel free to suggest to us on how we can improve our services to you " +
			    	"in the near future. Please click link below.</td></tr>" +
			    	"<tr><td><td></tr>" +
			    	"<tr><td><a href="+link+">"+link+"</a>.<td></tr>" +
			    	"<tr><td><td></tr>" +
			    	"<tr><td>Thank You.<td></tr>" +
			    	"</table>";

			FmsNotification notification = new FmsNotification();		
			notification.send(emailTo, subject, body);
		} catch (Exception er) {
			Log.getLog(getClass()).error("ERROR sendNotification " + er);
		}
	}
    

    public String getDefaultTemplate()
    {    	
    	return COMPLETE_TEMPLATE;
    	
    }

	public TimeField getTime() {
		return time;
	}

	public void setTime(TimeField time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Link getRequestLink() {
		return requestLink;
	}

	public void setRequestLink(Link requestLink) {
		this.requestLink = requestLink;
	}

	public Label getProgram() {
		return program;
	}

	public void setProgram(Label program) {
		this.program = program;
	}

	public Label getAssignmentDatetime() {
		return assignmentDatetime;
	}

	public void setAssignmentDatetime(Label assignmentDatetime) {
		this.assignmentDatetime = assignmentDatetime;
	}

	public DatePopupField getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(DatePopupField completionDate) {
		this.completionDate = completionDate;
	}

	public TimeField getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(TimeField completionTime) {
		this.completionTime = completionTime;
	}

	public TextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(TextBox remarks) {
		this.remarks = remarks;
	}

	public FileUpload getDocFileUpload() {
		return docFileUpload;
	}

	public void setDocFileUpload(FileUpload docFileUpload) {
		this.docFileUpload = docFileUpload;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Map getReq() {
		return req;
	}

	public void setReq(Map req) {
		this.req = req;
	}

	public Button getCompleteButton() {
		return completeButton;
	}

	public void setCompleteButton(Button completeButton) {
		this.completeButton = completeButton;
	}

	public Button getUnfulfilledButton() {
		return unfulfilledButton;
	}

	public void setUnfulfilledButton(Button unfulfilledButton) {
		this.unfulfilledButton = unfulfilledButton;
	}

	public Button getReassignButton() {
		return reassignButton;
	}

	public void setReassignButton(Button reassignButton) {
		this.reassignButton = reassignButton;
	}

	public Button getBackButton() {
		return backButton;
	}

	public void setBackButton(Button backButton) {
		this.backButton = backButton;
	}

	public ManPowerSelect getManPowerSelect() {
		return manPowerSelect;
	}

	public void setManPowerSelect(ManPowerSelect manPowerSelect) {
		this.manPowerSelect = manPowerSelect;
	}

	public boolean isAbleToAssign() {
		return ableToAssign;
	}

	public void setAbleToAssign(boolean ableToAssign) {
		this.ableToAssign = ableToAssign;
	}

	public EngineeringRequest getEngReq() {
		return engReq;
	}

	public void setEngReq(EngineeringRequest engReq) {
		this.engReq = engReq;
	}

	public boolean isEngineering() {
		return engineering;
	}

	public void setEngineering(boolean engineering) {
		this.engineering = engineering;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


}

