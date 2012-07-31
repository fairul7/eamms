package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
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
import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.AssignmentLog;
import com.tms.fms.engineering.model.AssignmentLogModule;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;


public class DriverAssignmentForm extends Form
{
	
	public static final String VIEW_MODE = "view";
	public static final String EDIT_MODE = "edit";
	public static final String COMPLETE_MODE = "complete";
	
	public static final String VIEW_TEMPLATE = "fms/viewAssignment";
	public static final String EDIT_TEMPLATE = "fms/editAssignment";
	public static final String COMPLETE_TEMPLATE = "fms/completeAssignment";
	public static final String DOCUMENT_PATH = "/Transport/Assignment/";
	
	public static final String VIEW_DUTY = "fms/dutyView";
	public static final String ENG_DUTY = "fms/dutyEng";
				
	private String id;
	private String requestId;
	private String mode;	
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
	private EngineeringRequest eRequest;
	
	private boolean ableToAssign;
	private boolean engineering;
	private String userId;
	
	
	//ENG
	private Label lbl_requestId;
	private Label assignmentId;
	private Label requestTitle;
	
	private Label description;
	private Label assignTo;
	private Label lblstatus;
	private Label requiredDateFrom;
	private Label requiredDateTo;
	private Label requiredTimeFrom;
	private Label requiredTimeTo;
	private Label manpower;
	private Label lblremarks;
	private Label lblAssignmentLog;
	
	private Label reasonUnfulfilled;
	private Label lbAttachmentList;
	
	private String assignmentStatus = "";
	private String cancelUrl 				= "myEngineeringAssignment.jsp";
	private String completeAssignmentUrl 	= "completeMyAssignment.jsp?id=";
	private String unfulfilledAssignmentUrl = "unfulfillMyAssignment.jsp?id=";
	private Label lblcompletionDate;
	

	public TransportRequest getTrequest() {
		return trequest;
	}

	public void setTrequest(TransportRequest trequest) {
		this.trequest = trequest;
	}

	public DriverAssignmentForm()
    {
    }

    public DriverAssignmentForm(String s)
    {
        super(s);
    }

	public void init()
	{
		removeChildren();
		setEngineering(false);
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
		lbl_requestId = new Label("lbl_requestId");
		addChild(lbl_requestId);
		
		assignmentId = new Label("assignmentId");
		addChild(assignmentId);
		
		requestTitle = new Label("requestTitle");
		addChild(requestTitle);
		
		program = new Label("program");
		addChild(program);
		
		description = new Label("description");
		addChild(description);
		
		assignTo = new Label("assignTo");
		addChild(assignTo);
		
		lblstatus = new Label("lblstatus");
		addChild(lblstatus);
		
		lblremarks = new Label("lblremarks");
		addChild(lblremarks);
		
		lbAttachmentList = new Label("lbAttachmentList");
		addChild(lbAttachmentList);
		
		requiredDateFrom = new Label("requiredDateFrom");
		addChild(requiredDateFrom);
		
		requiredDateTo = new Label("requiredDateTo");
		addChild(requiredDateTo);
		
		requiredTimeFrom = new Label("requiredTimeFrom");
		addChild(requiredTimeFrom);
		
		requiredTimeTo = new Label("requiredTimeTo");
		addChild(requiredTimeTo);
		
		manpower = new Label("manpower");
		addChild(manpower);
		
		lblcompletionDate = new Label("lblcompletionDate");
		addChild(lblcompletionDate);
		
		lblAssignmentLog = new Label("lblAssignmentLog");
		addChild(lblAssignmentLog);
		
	}
	
 	
	public Forward actionPerformed(Event evt)
    {    
	    if(cancelButton.getAbsoluteName().equals(findButtonClicked(evt)) || backButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
	    	 return new Forward("Back");
		}
	    
	    if(reassignButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
	    	 return new Forward("Reassign");
		}
	    
	    if(completeButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
	    	 return new Forward("Completed");
		}
	    
	    
	    
	    
	    
	    return onValidate(evt);
    }
	
    public void onRequest(Event event)
    {    	
    	
    	id = event.getRequest().getParameter("id");
    	setEngineering(false);	
    	status = "new";
    	String manpowerId = null;
    	manpowerId = event.getRequest().getParameter("userId");
    	String optionalAssignmentId = event.getRequest().getParameter("assignmentId");
    	
    	if(!(manpowerId == null)) {
    		dutyView(id, manpowerId, optionalAssignmentId);
    	} else {
	    	init();
	    	if(VIEW_MODE.equals(mode))
	    		//populateView(id);
	    		populateForm(id);
	    	
	    	if(EDIT_MODE.equals(mode))
	    		populateEditForm(id);
	    	
	    	if(COMPLETE_MODE.equals(mode))
	    		populateEditForm(id);
    	}
    		
        super.onRequest(event);         
        
    }
    
    public void populateEditForm(String id){
    	
    	TransportModule tm = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);		
		trequest = new TransportRequest();
		req = new SequencedHashMap();
				
		String manpowerId = getWidgetManager().getUser().getId();
		String assignmentId="";
		try{
			assignmentId = tm.getDriverAssignmentIdByRequestId(id, manpowerId);
        	trequest = tm.getDriverAssignmentByAssgIdUserId(assignmentId, manpowerId);	        	
			String reqUserId = trequest.getManpowerId();
			trequest.setManpowerId(service.getUser(reqUserId).getName());
        }catch(Exception er){}
        
        
        
    	manPowerSelect.setSDate(trequest.getStartDate());
		manPowerSelect.setEDate(trequest.getEndDate());
    	
    }
    
    public void populateView(String id){
		
    	
		TransportModule tm = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);		
		trequest = new TransportRequest();
		req = new SequencedHashMap();
		
		String requestId = null;        
        TransportRequest TRQ = new TransportRequest();
        
        String userId = getWidgetManager().getUser().getId();
		String assignmentId="";
		try{
			assignmentId = tm.getDriverAssignmentIdByRequestId(id, userId);			
			
			TRQ = tm.getTransportByAssignment(assignmentId);
        	requestId = TRQ.getRequestId();
        	
        	
        	//
			trequest = tm.getDriverAssignment(requestId, getWidgetManager().getUser().getId());
			String manpowerId = trequest.getManpowerId();
			trequest.setManpowerId(service.getUser(manpowerId).getName());
			
			//get drivers
	        String drivers = "";
	        ManpowerLeaveObject ML = new ManpowerLeaveObject();        
	        try{
	        	Collection col = tm.getDriverByRequestId(requestId);
	        	for(Iterator it = col.iterator(); it.hasNext(); ){
	        		ML = (ManpowerLeaveObject) it.next();
	        		drivers += ML.getManpowerName()+",";
	        	}
	        	manpower.setText(drivers);
	        }catch(Exception er){}
	        
			
		}catch(Exception er){
			Log.getLog(getClass()).error("We got the prob:"+er);
		}
		
		setAbleToAssign(false);
//      get HOD permission 
		String manpowerId = getWidgetManager().getUser().getId();
        Application app = Application.getInstance();
        String department = null;
        boolean transApproval = false;
        FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
    	try{
    		department = FRM.getUserDepartment(manpowerId);
    	}catch(Exception e){
    		Log.getLog(getClass()).error(e.toString(), e);
    	}
    	FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);	
    	//String transportdept = Application.getInstance().getProperty("TransportDepartment");
    	String transportdept = Application.getInstance().getProperty("ManagementService");
    	
    	//dept must from transport dept
    	try{
    	if(transportdept.equals(department)){
    		transApproval = FDM.userIsHOD(manpowerId, department);	    
    	    
    	    setAbleToAssign(transApproval);    	
    	    
    	}
    	}catch(Exception er){}
			   
	}
    
    private void populateFields(String assid){
    	
    	
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		eRequest = module.getAssignment(assid);
		
		if (eRequest != null ) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			SimpleDateFormat sdfcomplete = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
			//String reqDate = sdf.format(eRequest.getRequiredFrom()) + " - " + sdf.format(eRequest.getRequiredTo());
			lbl_requestId.setText(eRequest.getRequestId());
			assignmentId.setText(eRequest.getAssignmentCode());
			requestTitle.setText(eRequest.getTitle());
			program.setText(eRequest.getProgramName());
			description.setText(eRequest.getDescription());
			lblstatus.setText((String) EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.get(eRequest.getStatus()));
			//assignTo.setText(eRequest.getManpowerName());
			requiredDateFrom.setText(sdf.format(eRequest.getRequiredFrom()));
			requiredDateTo.setText(sdf.format(eRequest.getRequiredTo()));
			requiredTimeFrom.setText(eRequest.getFromTime());
			requiredTimeTo.setText(eRequest.getToTime());
			manpower.setText(eRequest.getCompetencyName());
			setRequestId(eRequest.getRequestId());
			
			if (EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED.equals(eRequest.getStatus())){
				assignmentStatus = EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED;
				lblcompletionDate.setText(sdfcomplete.format(eRequest.getCompletionDate()));
				lblremarks.setText(eRequest.getRemarks());
				reasonUnfulfilled.setText("");
			}
			
			if (EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED.equals(eRequest.getStatus())){
				assignmentStatus = EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED;
				remarks.setValue("");
				reasonUnfulfilled.setText(eRequest.getReasonUnfulfilled());
			}			
			
		}
	}
    
    private void dutyView(String id, String manpowerId, String optionalAssignmentId) {
    	mode = "duty";
    	
		TransportModule tm = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);		
		SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);		
		trequest = new TransportRequest();
		req = new SequencedHashMap();
		
		Application app = Application.getInstance();
		String department = null;
        boolean transApproval = false;
        FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
        //String transportdept = Application.getInstance().getProperty("TransportDepartment");//get transport dept
        String transportdept = Application.getInstance().getProperty("ManagementService");//get transport dept
        
        AssignmentLogModule mod = (AssignmentLogModule) app.getModule(AssignmentLogModule.class);
    	try{
    		department = FRM.getUserDepartment(manpowerId);
    	}catch(Exception e){
    		Log.getLog(getClass()).error(e.toString(), e);
    	}
		
    	if(transportdept.equals(department)){
    		String tempAssignmentId="";
			try{
					tempAssignmentId = tm.getDriverAssignmentIdByRequestId(id, manpowerId, optionalAssignmentId);
					trequest = tm.getDriverAssignmentByAssgIdUserId(tempAssignmentId, manpowerId);				
				
					String reqUserId = trequest.getManpowerId();
					trequest.setManpowerId(service.getUser(reqUserId).getName());
					
					
					//driver
			        String drivers = "";
			        ManpowerLeaveObject ML = new ManpowerLeaveObject();        
			       
			        //Collection col = tm.getDriverByRequestId(id);
			        Collection col = tm.getDriverByAssgId(tempAssignmentId);
			        
			        	for(Iterator it = col.iterator(); it.hasNext(); ){
			        		ML = (ManpowerLeaveObject) it.next();
			        		drivers += ML.getManpowerName()+",<br/>";
			        	}
					
			        	manpower.setText(drivers);
				
			}catch(Exception er){
				Log.getLog(getClass()).error("We got the prob:"+er);
			}
			
			setAbleToAssign(false);
			
			//get HOD permission     
	    	FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);
	    	
	    	try{
	    	if(transportdept.equals(department)){
	    		transApproval = FDM.userIsHOD(manpowerId, department);	        	    
	    	    setAbleToAssign(transApproval);        	    
	    	}
	    	
	    	}catch(Exception er){}
	    	 //Log Properties
	        String assignmentLabel = "";
	      
	        try{
	        	Collection colAssignment = mod.getLog(tempAssignmentId);
	        	assignmentLabel += "<table cellspacing=\"1\" cellpadding=\"2\">";
	        	for(Iterator iter = colAssignment.iterator(); iter.hasNext(); ){
	        		AssignmentLog log = (AssignmentLog) iter.next();
	        		if (log!=null){
	        			if(iter.hasNext()) {
	        				if(log.getOldUserId()!=null && !log.getUserId().equals("")){
	        					assignmentLabel+= "<tr><td><b>Reassigned from</b> "+service.getUser(log.getOldUserId()).getName()+" <b>to</b> "+ service.getUser(log.getUserId()).getName() +" on " +log.getStrAssignDate()+ " By " +service.getUser(log.getAssignBy()).getName()+"</td></tr>";
	        				}else{
	        					assignmentLabel+= "<tr><td><b>Assigned to</b> "+service.getUser(log.getUserId()).getName()+" on " +log.getStrAssignDate()+ " By " +service.getUser(log.getAssignBy()).getName()+"</td></tr>";
	        				}
	        			}else {
	        				assignmentLabel+= "<tr><td><b>Assigned to</b> "+service.getUser(log.getUserId()).getName()+" on " +log.getStrAssignDate()+ " By " +service.getUser(log.getAssignBy()).getName()+"</td></tr>";
	        			}
	        		}
	        	}
	        	assignmentLabel+="</table>";
	        	lblAssignmentLog.setText(assignmentLabel);
	        }catch(Exception e){
	        	
	        }
    	}else{
    		EngineeringModule eM = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
    		engReq = new EngineeringRequest();
    		
    		String assignmentId="";
    		try{
    			assignmentId = SM.selectAssignmentIdByBookingIdUserId(id, manpowerId, optionalAssignmentId);	
    			if (assignmentId != null && !assignmentId.equals("-")) {
    				eRequest = eM.getAssignment(assignmentId);
    			}
    			setEngineering(true);
    			
    			
    			//driver
    			SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
    	        String drivers = "";
    	        String name = "";
    	        HashMap map = new HashMap();        
    	        try{
    	        	Collection col = sm.getDutyRosterAssignUser(id, eRequest.getRequiredFrom());
    	        	for(Iterator it = col.iterator(); it.hasNext(); ){
    	        		map = (HashMap) it.next();
    	        		drivers = (String)map.get("manpowerId");
    	        		if(drivers != null)
    	        			if (it.hasNext()){
    	        				name += service.getUser(drivers).getName()+",<br/>";
    	        			} else{
    	        				name += service.getUser(drivers).getName()+"<br/>";
    	        			}
    	        			
    	        		
    	        	}
    	        	assignTo.setText(name);
    	        }catch(Exception er){}
    	        
    	        //Log Properties
    	        String assignmentLabel = "";
    	      
    	        try{
    	        	Collection colAssignment = mod.getLog(assignmentId);
    	        	assignmentLabel += "<table cellspacing=\"1\" cellpadding=\"2\">";
    	        	for(Iterator iter = colAssignment.iterator(); iter.hasNext(); ){
    	        		AssignmentLog log = (AssignmentLog) iter.next();
    	        		if (log!=null){
    	        			if(iter.hasNext()) {
    	        				assignmentLabel+= "<tr><td><b>Reassigned to</b> "+service.getUser(log.getUserId()).getName()+" on " +log.getStrAssignDate()+ " By " +service.getUser(log.getAssignBy()).getName()+"</td></tr>";
    	        			}else {
    	        				assignmentLabel+= "<tr><td><b>Assigned to</b> "+service.getUser(log.getUserId()).getName()+" on " +log.getStrAssignDate()+ " By " +service.getUser(log.getAssignBy()).getName()+"</td></tr>";
    	        			}
    	        		}
    	        	}
    	        	assignmentLabel+="</table>";
    	        	lblAssignmentLog.setText(assignmentLabel);
    	        }catch(Exception e){
    	        	
    	        }
    	        
    			//setEngineering(true);
    			//populateView(id);
    			 //TODO cari assignment id unik, parameter bookingid n userid
    			if(null != assignmentId)
    				populateFields(assignmentId);
    			
    		}catch(Exception er){}
    		
    	}	   
	}
    
    public void populateForm(String id) {
    	Application app = Application.getInstance();
    	SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);		
        TransportModule tm = (TransportModule)Application.getInstance().getModule(TransportModule.class);
        
        userId = getWidgetManager().getUser().getId();
        String assignmentId="";
		try{
			assignmentId = tm.getDriverAssignmentIdByRequestId(id, userId);
        	trequest = tm.getDriverAssignmentByAssgIdUserId(assignmentId, userId);	
        	if(trequest.getStatus() != null){
        		status = trequest.getStatus();
        	}
        	
        	this.setRequestId(trequest.getRequestId());
        	
			String reqUserId = trequest.getManpowerId();
			trequest.setManpowerId(service.getUser(reqUserId).getName());
        }catch(Exception er){}
        
        
        String deptname = "";		
		FMSDepartmentManager FDM = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);		
		try{			
			FMSDepartment FD = FDM.getselectFMSDepartment(trequest.getDepartment());
			deptname = FD.getName();
			trequest.setDepartment(deptname);
		}catch(Exception e){
			
		}
		
        
        //driver
        String drivers = "";
        ManpowerLeaveObject ML = new ManpowerLeaveObject();        
        try{
        	//Collection col = tm.getDriverByRequestId(requestId);
        	Collection col = tm.getDriverByAssgId(id);
        	for(Iterator it = col.iterator(); it.hasNext(); ){
        		ML = (ManpowerLeaveObject) it.next();
        		drivers += ML.getManpowerName()+",";
        	}
        	manpower.setText(drivers);
        }catch(Exception er){}
        


        setAbleToAssign(false);
//      get HOD permission 
		
        //Application app = Application.getInstance();
        String department = null;
        boolean transApproval = false;
        FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
    	try{
    		department = FRM.getUserDepartment(userId);
    	}catch(Exception e){
    		Log.getLog(getClass()).error(e.toString(), e);
    	}
    		
    	//String transportdept = Application.getInstance().getProperty("TransportDepartment");
    	String transportdept = app.getProperty("ManagementService");
    	
    	//dept must from transport dept
    	try{
    	if(transportdept.equals(department)){
    		transApproval = FDM.userIsHOD(userId, department);	    
    	    
    	    setAbleToAssign(transApproval);    	
    	    
    	}
    	}catch(Exception er){}
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
	        
	        if(COMPLETE_MODE.equals(mode)){
	        	String assignmentId="";
	     		try{
	     			assignmentId = TM.getDriverAssignmentIdByRequestId(id, getWidgetManager().getUser().getId());
	 	        	tr = TM.getDriverAssignmentByAssgIdUserId(assignmentId, getWidgetManager().getUser().getId());	        	
	 				
	 	        }catch(Exception er){}
	    		if(!(eng.isValidCompletion(tr.getStartDate(), new Date(), start.getTime()))){
	   			 completionDate.setInvalid(true);
	   			 time.setInvalid(true);
	   			 setInvalid(true);  
	   			 
	   			 return null;
	   		 	}
	       	    	
	    	try {
                StorageFile sf = docFileUpload.getStorageFile(event.getRequest());
                if (sf != null) {                   
                        StorageService ss = (StorageService) Application.getInstance().getService(new StorageService().getClass());
                        String docId = UuidGenerator.getInstance().getUuid();
                        sf.setParentDirectoryPath(DOCUMENT_PATH + docId);
                        ss.store(sf);    
                        mObj.setDocuments(sf.getAbsolutePath());
                        mObj.setDocumentsName(sf.getName());
                        mObj.setId(docId);
                        mObj.setReferenceId(id);
                        mObj.setManpowerId(getWidgetManager().getUser().getId());
                        mObj.setCreatedDate(new Date());
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
	    	}catch(Exception er){
	    		Log.getLog(getClass()).error(er);
	    	}
	        ////
	    
	        		
    	}
    	
    	if(EDIT_MODE.equals(mode)){
    		
    		if(!(0 == manPowerSelect.getIds().length)){
    			try{
	    			String manpowerId = trequest.getManpowerId();
    				String newUserId = manPowerSelect.getIds()[0];
	    			
	    			TM.updateDrivers(newUserId, id ,manpowerId);
    			}catch(Exception er){Log.getLog(getClass()).error(er);}
    		}    		
    	}
    	
    	    	    	
        return new Forward("Ok");
    }

    public String getDefaultTemplate()
    {
    	if(VIEW_MODE.equals(mode))
    		return VIEW_TEMPLATE;
    	
    	
    	if(EDIT_MODE.equals(mode))
    		return EDIT_TEMPLATE;
    	if(COMPLETE_MODE.equals(mode))
    		return COMPLETE_TEMPLATE;
    	if(engineering)
    		return ENG_DUTY;
    	
    	return VIEW_DUTY;
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

	public Label getLblcompletionDate() {
		return lblcompletionDate;
	}

	public void setLblcompletionDate(Label lblcompletionDate) {
		this.lblcompletionDate = lblcompletionDate;
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
	
	public Label getLbl_requestId() {
		return lbl_requestId;
	}

	public void setLbl_requestId(Label lbl_requestId) {
		this.lbl_requestId = lbl_requestId;
	}

	public Label getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Label assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Label getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(Label requestTitle) {
		this.requestTitle = requestTitle;
	}

	public Label getDescription() {
		return description;
	}

	public void setDescription(Label description) {
		this.description = description;
	}

	public Label getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Label assignTo) {
		this.assignTo = assignTo;
	}

	public Label getLblstatus() {
		return lblstatus;
	}

	public void setLblstatus(Label lblstatus) {
		this.lblstatus = lblstatus;
	}

	public Label getRequiredDateFrom() {
		return requiredDateFrom;
	}

	public void setRequiredDateFrom(Label requiredDateFrom) {
		this.requiredDateFrom = requiredDateFrom;
	}

	public Label getRequiredDateTo() {
		return requiredDateTo;
	}

	public void setRequiredDateTo(Label requiredDateTo) {
		this.requiredDateTo = requiredDateTo;
	}

	public Label getRequiredTimeFrom() {
		return requiredTimeFrom;
	}

	public void setRequiredTimeFrom(Label requiredTimeFrom) {
		this.requiredTimeFrom = requiredTimeFrom;
	}

	public Label getRequiredTimeTo() {
		return requiredTimeTo;
	}

	public void setRequiredTimeTo(Label requiredTimeTo) {
		this.requiredTimeTo = requiredTimeTo;
	}

	public Label getManpower() {
		return manpower;
	}

	public void setManpower(Label manpower) {
		this.manpower = manpower;
	}

	public Label getReasonUnfulfilled() {
		return reasonUnfulfilled;
	}

	public void setReasonUnfulfilled(Label reasonUnfulfilled) {
		this.reasonUnfulfilled = reasonUnfulfilled;
	}

	public Label getLbAttachmentList() {
		return lbAttachmentList;
	}

	public void setLbAttachmentList(Label lbAttachmentList) {
		this.lbAttachmentList = lbAttachmentList;
	}

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getCompleteAssignmentUrl() {
		return completeAssignmentUrl;
	}

	public void setCompleteAssignmentUrl(String completeAssignmentUrl) {
		this.completeAssignmentUrl = completeAssignmentUrl;
	}

	public String getUnfulfilledAssignmentUrl() {
		return unfulfilledAssignmentUrl;
	}

	public void setUnfulfilledAssignmentUrl(String unfulfilledAssignmentUrl) {
		this.unfulfilledAssignmentUrl = unfulfilledAssignmentUrl;
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

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

	public Label getLblremarks() {
		return lblremarks;
	}

	public void setLblremarks(Label lblremarks) {
		this.lblremarks = lblremarks;
	}

	public Label getLblAssignmentLog() {
		return lblAssignmentLog;
	}

	public void setLblAssignmentLog(Label lblAssignmentLog) {
		this.lblAssignmentLog = lblAssignmentLog;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}

