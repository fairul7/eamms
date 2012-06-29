package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.ekms.manpowertemp.model.ManpowerLeaveObject;
import com.tms.fms.engineering.model.AssignmentLog;
import com.tms.fms.engineering.model.AssignmentLogModule;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.Status;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleObject;
import com.tms.fms.transport.model.VehicleRequest;
import com.tms.fms.util.DateDiffUtil;


public class AssignItemForm extends Form
{

    private Button submitButton,cancelButton;    
    private Label info;
    private String vehicle_num;
    private String userId;    
    private String assgId;    
    protected Date dateF;
    protected Date dateT;
    
    private int maxVehicles;
    private int maxDrivers;
    
    protected int totalVehicles;
    protected int totalAssignedDrivers;
    
    private String type;    

	public AssignItemForm()
    {
    }
	
	public AssignItemForm(String s)
    {
		 super(s);
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
		submitButton = new Button("submitButton","Ok");		
		cancelButton = new Button("cancelButton",Application.getInstance().getMessage("fms.tran.cancel","Cancel"));
        addChild(cancelButton);
        info = new Label("info");
        
        addChild(info);
        addChild(submitButton);
        
        
    }
	 
    public void onRequest(Event event) {
    	super.onRequest(event);
    	
    	Log.getLog(getClass()).info("Info :"+vehicle_num+assgId+type+userId);
    	//operationForm();
    	doSave();
    }
    
    /*public void getMaxVehiclesDrivers(String assgId){
    	
    	Collection colTM = new ArrayList();
    	TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
    	VehicleObject vo = new VehicleObject();
    	TransportRequest TR = new TransportRequest();
    	String requestId = null;
    	
    	try{
    		Collection collass = TM.getAssignmentByAssignmentId(assgId);
    		
    		for(Iterator it = collass.iterator(); it.hasNext(); ){
    			TR = (TransportRequest) it.next();
    			dateF = TR.getStartDate();
    			dateT = TR.getEndDate();
    			requestId = TR.getRequestId();
    		}
    		    		
    	}catch(Exception er){
    		Log.getLog(getClass()).error("Error getMaxVehiclesDrivers:"+er);
    	}    	
    	
    	maxVehicles = 0;
    	maxDrivers = 0;
    	Collection vehs = new ArrayList();
    	Collection driv = new ArrayList();
    	
    	VehicleRequest vr = new VehicleRequest();
    	try{
    		vehs = TM.getVehicleDriverQuantityByType(requestId, getType(), assgId);	//select veh by type
    		for(Iterator it = vehs.iterator(); it.hasNext(); ){
    			vr = (VehicleRequest) it.next();
    			maxVehicles = vr.getQuantity();    			
    		}
    		
    		driv = TM.getVehicleDriverQuantityByType(requestId, null, assgId);
    		for(Iterator it = driv.iterator(); it.hasNext(); ){	//select all drivers
    			vr = (VehicleRequest) it.next();    			
    			maxDrivers += vr.getDriver();
    		}
    		
    	}catch(Exception er){}
    	
    }*/
    
    protected boolean allRequestAssigned(String requestId){
    	
    	boolean allAreAssigned = false;
    	TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
    	Collection getVehiclesDrivers = new ArrayList();
    	VehicleRequest vr = new VehicleRequest();
    	
    	totalVehicles = 0;    	
    	Map map = new HashMap();
    	int reqVeh = 0;
    	int reqDri = 0;
    	int assignedVeh = 0;
    	int assignedDri = 0;
    	
    	try{
    		///get request quantity 
    		map = TM.getQuantityVehiclesDrivers(requestId);
    		for (Iterator i = map.keySet().iterator(); i.hasNext();) {
    			String tmp = (String) i.next();
    			reqVeh = (Integer) map.get("qtyVeh");
    			reqDri = (Integer) map.get("qtyDri");
    		}				
    		///
    		
    		////get what already assigned
    		assignedVeh = TM.getVehicleCount(requestId);
    		assignedDri = TM.getDriverCount(requestId);    		
    		////
    		
    		if(!(reqVeh == 0 || assignedVeh == 0)){
    			
    			if(reqVeh == assignedVeh && reqDri == assignedDri)
    				allAreAssigned = true;
    		}
    			
    	}catch(Exception er){
    		Log.getLog(getClass()).error(er);
    	}
    	
    	return allAreAssigned;
    }
    
    public void doSave() {
    	
        String infos = "";
        Application app = Application.getInstance();
        TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);        
        SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
      
    	String requestId = "";
    	try{	
    		 TransportRequest tranreq = new TransportRequest();
   			 Collection coll = TM.getAssignmentByAssignmentId(assgId);
   			 
   			 for(Iterator it = coll.iterator(); it.hasNext(); ){
   				 tranreq = (TransportRequest) it.next();
   				 requestId = tranreq.getRequestId();
   				 dateF = tranreq.getStartDate();
    			 dateT = tranreq.getEndDate();   
   			 }		   			 
   		 }catch(Exception er){
   			 Log.getLog(getClass()).error("Error converting SimpleDateFormat:"+er);
   		 }
   		
   		if(!(vehicle_num == null || "".equals(vehicle_num))){       	 
	   		 
        	try{
        		VehicleObject vo = TM.getVehicle(vehicle_num);
        		setType(vo.getCategory_name());
        	}catch(Exception er){
        		Log.getLog(getClass()).error(er);
        	}        	   		 
	   		//getMaxVehiclesDrivers(assgId);
	   		try{
		    	maxVehicles = 0;		    	
		    	Collection vehs = new ArrayList();		    	
		    	
		    	VehicleRequest vr = new VehicleRequest();
		    	try{
		    		vehs = TM.getVehicleDriverQuantityByType(requestId, getType(), assgId);	//select veh by type
		    		for(Iterator it = vehs.iterator(); it.hasNext(); ){
		    			vr = (VehicleRequest) it.next();
		    			maxVehicles = maxVehicles + vr.getQuantity();    			
		    		}
		    		
		    	}catch(Exception er){}
	   		}catch(Exception er){}
	   		// 
   		
	   			Collection cc = new ArrayList();
	        	int vehqty = 100;
	        	try{
	        		cc = TM.getVehicleRequestId(requestId, getType(), assgId);
	        		
	        	} catch(Exception er){
	        		Log.getLog(getClass()).error(er);
	        	}        	
	        	
	        	Collection colV = new ArrayList();        
	        	try{
	        		colV = TM.getVehicleAssigmentByVehicleNum(vehicle_num, dateF, dateT);
	        	} catch(Exception er){        		
	        	}
	        	
	        	if(colV.size() <= 0){		//check existing assigned vehicle        		
	        		////
	        		
	        		if(cc.size() < maxVehicles){
	        		////
			        	VehicleObject vo = new VehicleObject();
			        	vo.setId(assgId);
			        	vo.setVehicle_num(vehicle_num);			        	 
			        	vo.setRequestId(requestId);      	
			        	vo.setCreatedby(getWidgetManager().getUser().getId());
			        	vo.setFlagId(UuidGenerator.getInstance().getUuid());
			        	try{        		
			        		TM.insertVehicleAssigment(vo);
			        		int statusTrail = TM.countRequestStatus(requestId, SetupModule.ASSIGNED_STATUS);
			        		if (statusTrail <= 0){
			        			TM.insertRequestStatus(requestId, SetupModule.ASSIGNED_STATUS, "");
			        		} else {
			        			Collection colStatusTrail = TM.getStatusTrail(assgId);
			        			String reason = "";
			        			for (Iterator itr = colStatusTrail.iterator(); itr.hasNext();){
			        				Status statusObj = (Status)itr.next();
			        				reason = statusObj.getReason();
			        			}
			        			TM.updateRequestStatus(requestId, SetupModule.ASSIGNED_STATUS, reason);
			        		}
			        		
			        		// to check whether all assignment already assigned, if so, update status to Assigned
			        		boolean allAssigned = allRequestAssigned(requestId);
		        			Log.getLog(getClass()).info(allAssigned);
		        			if(allAssigned == true){
		        	    		TM.updateStatus(SetupModule.ASSIGNED_STATUS, null, requestId, getWidgetManager().getUser().getId(),new Date());
		        			}
	
			        		infos += "<br/>"+Application.getInstance().getMessage("fms.tran.youHaveAssignedSuccessful")+vehicle_num;
			        	} catch(Exception er) {
			        		Log.getLog(getClass()).error("Error when assigning vehicle: "+er);
			        	}
	        		} else {
	        		
	        			infos += "<br/>"+Application.getInstance().getMessage("fms.tran.reachedLimitVehicles")+maxVehicles;
	        		}        		
	        	} else {
	        		
	        		infos += "<br/>"+Application.getInstance().getMessage("fms.tran.vehicleAlreadyAssigned");
	        	}        	
	        	vehicle_num = null;
	    }
        
        totalAssignedDrivers = 0;
        
        if(!(userId == null || "".equals(userId))){   
        	
	    	maxDrivers = 0;	    	
	    	Collection driv = new ArrayList();
	    	VehicleRequest vr = new VehicleRequest();
        	try{	    		
	    		driv = TM.getVehicleDriverQuantityByType(requestId, null, assgId);
	    		for(Iterator it = driv.iterator(); it.hasNext(); ){	//select all drivers
	    			vr = (VehicleRequest) it.next();    			
	    			maxDrivers += vr.getDriver();
	    		}
	    		
	    	}catch(Exception er){}
	    	
        	////
        	Collection cc = new ArrayList();
        	try{
        		cc = TM.selectAssigmentByRequestId(requestId, assgId);
        	} catch (Exception er) {
        		
        	}
        	////
        	Collection colA = new ArrayList();        
        	try{
        		colA = TM.getDriversAssignmentByUserId(userId, dateF, dateT);
        	}catch(Exception er){        		
        	}
        	
        	totalAssignedDrivers = cc.size();
        	
        	if (colA.size() <= 0) {
	        	//selectAssigmentByDriver
        		if(totalAssignedDrivers < maxDrivers){
		        	ManpowerLeaveObject ML = new ManpowerLeaveObject();
		        	ML.setAssignmentId(assgId);
		        	ML.setManpowerId(userId);
		        	ML.setRequestId(requestId);		
		        	ML.setFlagId(UuidGenerator.getInstance().getUuid());
		        	try{
		        		TM.insertDriverAssigment(ML);
		        		int statusTrail = TM.countRequestStatus(ML.getRequestId(), SetupModule.ASSIGNED_STATUS);
		        		if (statusTrail <= 0){
		        			TM.insertRequestStatus(ML.getRequestId(), SetupModule.ASSIGNED_STATUS, "");
		        		} else {
		        			Collection colStatusTrail = TM.getStatusTrail(ML.getRequestId());
		        			String reason = "";
		        			for (Iterator itr = colStatusTrail.iterator(); itr.hasNext();){
		        				Status statusObj = (Status)itr.next();
		        				reason = statusObj.getReason();
		        			}
		        			TM.updateRequestStatus(ML.getRequestId(), SetupModule.ASSIGNED_STATUS, reason);
		        		}
		        		
		        		if(allRequestAssigned(requestId)){
	        				TM.updateStatus(SetupModule.ASSIGNED_STATUS, null, requestId, getWidgetManager().getUser().getId(),new Date());
	        			}
	        	    		
		        		String nameOfUser="";
		        		try{nameOfUser = security.getUser(userId).getName();}catch(SecurityException se){}
		        		
		        		// Log Setting
		            	AssignmentLogModule mod = (AssignmentLogModule) app.getModule(AssignmentLogModule.class);
		            	AssignmentLog log = new AssignmentLog();
		            	log.setLogId(UuidGenerator.getInstance().getUuid());
		            	log.setAssignmentId(assgId);
		            	log.setUserId(userId);
		            	log.setAssignBy(app.getCurrentUser().getId());
		            	log.setAssignDate(new Date());
		            	if(mod.searchDriverLog(assgId)) {
		            		log.setOldUserId(mod.getDriver(assgId));
		            		mod.updateDriverLog(log);
		            	}else {
		            		mod.addLog(log);
		            	}
		            	
		        		infos += "<br/>"+Application.getInstance().getMessage("fms.tran.youHaveAssignedDriverSuccessful")+nameOfUser;
		        	}catch(Exception er){
		        		Log.getLog(getClass()).error("Error when assigning driver: "+er);
		        	}
        		} else {

        			infos += "<br/>"+Application.getInstance().getMessage("fms.tran.reachedLimitDrivers")+maxDrivers;
        		}
        		
        	} else {

        		infos += "<br/>"+Application.getInstance().getMessage("fms.tran.driverAlreadyAssigned");
        	}
        	
        	userId = null;
        }
            
        /*if(allRequestAssigned(requestId)){}
			TM.updateStatus(SetupModule.ASSIGNED_STATUS, null, requestId, getWidgetManager().getUser().getId(),new Date());*/
			
        info.setText(infos);
    }

    public Forward onValidate(Event event)
    {
    	TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);        
        SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        
    	
    		
    	super.onValidate(event);
        return new Forward("Ok");
    }

    public long dateDiff(Date start, Date end){
    	
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));
		
		return diff;
	}

    protected Date getDateToEndTime(Date xDate){
    	
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(xDate);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59); 
        xDate = endTime.getTime();
        
        return xDate;      
    }
    
    protected Date getDateFromStartTime(Date xDate){
    	
        Calendar start = Calendar.getInstance();
        start.setTime(xDate);
        start.set(Calendar.HOUR_OF_DAY, 00);
        start.set(Calendar.MINUTE, 00);
        start.set(Calendar.SECOND, 00);
        xDate = start.getTime();
        
        return xDate;      
    }
    
    public String getDefaultTemplate()
    {
        return "fms/assignform";
    }
   
	public String getVehicle_num() {
		return vehicle_num;
	}

	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDateF() {
		return dateF;
	}

	public void setDateF(Date dateF) {
		this.dateF = dateF;
	}

	public Date getDateT() {
		return dateT;
	}

	public void setDateT(Date dateT) {
		this.dateT = dateT;
	}

	public Label getInfo() {
		return info;
	}

	public void setInfo(Label info) {
		this.info = info;
	}
	
	public int getMaxVehicles() {
		return maxVehicles;
	}

	public void setMaxVehicles(int maxVehicles) {
		this.maxVehicles = maxVehicles;
	}

	public int getMaxDrivers() {
		return maxDrivers;
	}

	public void setMaxDrivers(int maxDrivers) {
		this.maxDrivers = maxDrivers;
	}
	
	public String getAssgId() {
		return assgId;
	}

	public void setAssgId(String assgId) {
		this.assgId = assgId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	


}

