package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.ekms.manpowertemp.model.ManpowerLeaveObject;
import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.AssignmentObject;
import com.tms.fms.transport.model.FmsNotification;
import com.tms.fms.transport.model.RateCardObject;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportDao;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleObject;

public class AssignmentForm extends Form {
	
	private String id;
	private String status; 
	private String source;
	
	protected TimeField time;	
	protected DatePopupField endDate;
	private TextBox remarks;

	private TextField speedoMeter;
	private TextField endSpeedoMeter;
	private SelectBox petrolCard;
	private Collection assignmentDetails;
	private TransportRequest TR;
	private AssignmentObject AO;
	
	private Label dateRequired;
	private Label requestBy;
	private Label driver;
	private Label timeRequired;
	
	private Button submitButton;
	private Button updateButton, cancelButton, unfulfilledButton, backButton;
	
	public static final String CHECKOUT_STATUS 		= "c";
	public static final String NEW_STATUS 			= "n";
	public static final String CLOSE_STATUS 		= "f";
	
	private String checkIn;
	private String checkOutTime;
	private String link;
	//private StorageService storage;
	private String vehicleNo;
	private String flagId; 	
	
	public AssignmentForm() {
    }

    public AssignmentForm(String s) {
        super(s);
    }

	public void init() {
		removeChildren();
		time = new TimeField("time");
        time.setTemplate("calendar/timefield");
        
       /* endTime = new TimeField("endTime");
        endTime.setTemplate("calendar/timefield");*/
         
		speedoMeter = new TextField("speedoMeter");		
		speedoMeter.setSize("30");
		
		endSpeedoMeter = new TextField("endSpeedoMeter");		
		endSpeedoMeter.setSize("30");
		
		petrolCard = new SelectBox("petrolCard");
		
		updateButton = new Button("updateButton");
		updateButton.setText(Application.getInstance().getMessage("fms.tran.update", "Update"));
		
		
		submitButton = new Button("submitButton");
		submitButton.setText(Application.getInstance().getMessage("fms.tran.update", "Update"));
		
		
		cancelButton = new Button("cancelButton");
		cancelButton.setText(Application.getInstance().getMessage("fms.tran.cancel", "Cancel"));
		
		backButton = new Button("backButton");
		backButton.setText(Application.getInstance().getMessage("fms.tran.back", "backButton"));
		
		
		
		unfulfilledButton = new Button("unfulfilledButton");
		unfulfilledButton.setText(Application.getInstance().getMessage("fms.label.notFulfilled", "Not Fulfilled"));
		
		dateRequired = new Label("dateRequired");
		requestBy = new Label("requestBy");		
		driver = new Label("driver");
		timeRequired = new Label("timeRequired");

		endDate = new DatePopupField("endDate");
	    endDate.setDate(new Date());
	    endDate.setTemplate("extDatePopupField");
	    endDate.setOptional(true);
	    endDate.setSize("10");
	    endDate.setMaxlength("10");	    
	    
	    remarks = new TextBox("remarks");
	    
		addChild(time);
		addChild(speedoMeter);
		addChild(endSpeedoMeter);
		addChild(updateButton);
		addChild(cancelButton);
		addChild(unfulfilledButton);
		addChild(backButton);
		addChild(endDate);
		addChild(remarks);
		
		addChild(dateRequired);
		addChild(requestBy);
		addChild(driver);
		addChild(timeRequired);
		addChild(submitButton);
		
		Collection colPetrol = new ArrayList();
		
		////
		TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
		try {
			colPetrol = dao.selectSetupObject("fms_tran_petrolcard", null, "-1", null, false, 0, -1);
			
	    } catch (DaoException e) {
	        Log.getLog(getClass()).error(e.toString());	        
	    }
	    
	    petrolCard.addOption("-", "-- Select Card --");
	    
	    for(Iterator it = colPetrol.iterator(); it.hasNext(); ){
	    	SetupObject so = (SetupObject) it.next();
	    	petrolCard.addOption(so.getName(), so.getName());	    	    	
	    }
	    addChild(petrolCard);
		////
	}
	
	public Forward actionPerformed(Event evt) {    
	    if(cancelButton.getAbsoluteName().equals(findButtonClicked(evt)) ||
	    		backButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
	    	 return new Forward("Back");
		}	    
	    	    
	    return super.actionPerformed(evt);
    }
	
    public void onRequest(Event event) {    	    	    	
    	//status = event.getRequest().getParameter("status");
    	
    	init();
    	if(flagId != null) {
    		status = getStatusByFlag(flagId);
    		populateForm(flagId);
    		if(!("".equals(status) || null == status)){
    			if("n".equals(status)){			
    				speedoMeter.addChild(new ValidatorIsNumeric("speedoMeterIsNumberic", Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field")));			
    	        }else{	
    				endSpeedoMeter.addChild(new ValidatorIsNumeric("endSpeedoMeterIsNumberic", Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field")));			
    	        }
    		}
    	}
    	
        super.onRequest(event);              
    }
    
    public void populateForm(String flagId) {    	
    	checkIn = " - ";
    	checkOutTime = " - "; 
    	TR = new TransportRequest();
    	
    	SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
        TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
        
        String pathfile = Application.getInstance().getProperty("check.list");
        //storage = (StorageService)Application.getInstance().getService(StorageService.class);   		
    	link = pathfile;            	
        
        try{
        	TR = TM.getTransportByFlagId(flagId);
        	id = TR.getId();
        	//requestId = TR.getRequestId();
        	vehicleNo = TR.getVehicle_num();
        	AO = TM.getAssignmentObject(id, vehicleNo);
        }catch(Exception er){
        	Log.getLog(getClass()).error(er);
        }
        
        if(AO != null){
	        try {
	        	SimpleDateFormat stf = new SimpleDateFormat("h:mm a");
	        	checkIn = (stf.format(AO.getCheckin_date()));
	        	checkOutTime = stf.format(AO.getCheckout_date());
	        } catch(Exception er) {
	        	
	        }
        }
        
        String deptname = "";		
		FMSDepartmentManager FDM = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);		
		try{			
			FMSDepartment FD = FDM.getselectFMSDepartment(TR.getDepartment());
			deptname = FD.getName();
			TR.setDepartment(deptname);
		} catch (Exception e){
		}
				
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");        
        String date = sdf.format(TR.getStartDate()) +" - "+ sdf.format(TR.getEndDate());
        dateRequired.setText(date);
        
        try{
        	requestBy.setText(security.getUser(TR.getRequestBy()).getName());
        }catch(Exception er){}
        
        //driver
        String drivers = "";
        ManpowerLeaveObject ML = new ManpowerLeaveObject();        
        try{
        	Collection col = TM.getDriverByAssgId(id);
        	for(Iterator it = col.iterator(); it.hasNext(); ){
        		ML = (ManpowerLeaveObject) it.next();
        		//drivers += ML.getManpowerName()+",";
        		if ("".equals(drivers)) {
        			drivers += ML.getManpowerName();
        		} else {
        			drivers += ", " + ML.getManpowerName();
        		}
        	}
        	driver.setText(drivers);
        }catch(Exception er){}
        
        SimpleDateFormat stf = new SimpleDateFormat("h:mm a");
        timeRequired.setText(stf.format(TR.getStartDate()) +"-"+ stf.format(TR.getEndDate()));
        	
    }  
    
    public Forward onSubmit(Event evt) {
    	Forward fwd = super.onSubmit(evt);
    	
    	if ("c".equals(status)) {
    		if (AO != null) {
                Calendar end = Calendar.getInstance();           
                end.setTime(endDate.getDate());
                end.set(Calendar.HOUR_OF_DAY, time.getHour());
                end.set(Calendar.MINUTE, time.getMinute());
                Date checkInDate = end.getTime();
    			
    			if (AO.getCheckout_date().after(checkInDate)) {
    				endDate.setInvalid(true);
    				time.setInvalid(true);
    				setInvalid(true);
    			}
    		}
    	}
    	
    	return fwd;
    }
    
    public Forward onValidate(Event event) {
    	TransportModule transModule = (TransportModule)Application.getInstance().getModule(TransportModule.class);
    	AssignmentObject AO = new AssignmentObject();
    	
    	if("n".equals(status)){
	    	Calendar today = Calendar.getInstance(); 			
	    	today.setTime(DateUtil.getToday());
			today.set(Calendar.HOUR_OF_DAY,time.getHour());
			today.set(Calendar.MINUTE,time.getMinute());
			AO.setAssgId(id);
	    	AO.setCheckout_date(today.getTime());
	    	AO.setCreatedBy(getWidgetManager().getUser().getId());
	    	
	    	AO.setCreatedDate(new Date());
	    	AO.setMeterStart(Long.parseLong((String) speedoMeter.getValue()));
	    	AO.setPetrolCard((String) petrolCard.getSelectedOptions().keySet().iterator().next());
	    	AO.setStatus(SetupModule.CHECKOUT_STATUS);
	    	AO.setVehicle_num(vehicleNo);
    	
	    	try{
	    		transModule.insertAssignmentDetails(AO);
	    	}catch(Exception er){
	    		Log.getLog(getClass()).error(er);
	    	}
	    	
	    	return new Forward("backToDetail", "assignment.jsp?id=" + id + "&status=c&source=" + source, true);
    	}
    	
    	if("c".equals(status)){
	    	    		
    		Collection col = new ArrayList();
    		try{
    			col = transModule.getAssignmentDetailRequestId(id);
    			for(Iterator it = col.iterator(); it.hasNext(); ){
    				AO = (AssignmentObject) it.next();
    			}
    		}catch(Exception er){}    		
    		
    		long meter = 0;
    		//
            Calendar end = Calendar.getInstance();           
            end.setTime(endDate.getDate());
            end.set(Calendar.HOUR_OF_DAY, time.getHour());
            end.set(Calendar.MINUTE, time.getMinute());            
    		//
            meter = Long.parseLong((String) endSpeedoMeter.getValue());            
            if (meter < AO.getMeterStart()) {
            	endSpeedoMeter.setInvalid(true);
            	return new Forward("invalid-end-meter");
            }
            AO.setCheckin_date(end.getTime());
	    	AO.setUpdatedBy(getWidgetManager().getUser().getId());	    	
	    	AO.setUpdatedDate(new Date());
	    	AO.setMeterEnd(meter);	    	
	    	AO.setStatus(SetupModule.CLOSED_STATUS);
	    	AO.setRemarks((String)remarks.getValue());
	    	AO.setVehicle_num(vehicleNo);
            
	    	//update rate card calculation 	    	
	    	VehicleObject vehicle = transModule.selectVehicleByNo(vehicleNo);
	    	if(vehicle != null){
		    	if("M".equals(vehicle.getType())){
		    		try {
			    		TransportRequest tr = transModule.selectTransportRequest(AO.getRequestId());			
				    	String rateVehicle = getRateFacility(AO.getRequestId(), AO.getMeterStart(), AO.getMeterEnd(), AO.getCheckin_date(), tr.getRateVehicle(), vehicle.getCategory_name());	
				    	transModule.updateVehicleRate(AO.getRequestId(), rateVehicle);
			    	} catch (DaoException e) {
			    		Log.getLog(getClass()).error(e.toString(), e);
					}
		    	}
	    	}
	    	
	    	try{
	    		transModule.updateAssignmentDetails(AO); 
	    	}catch(Exception er){
	    		Log.getLog(getClass()).error(er);
	    	}
	    	
	    	// check for both vehicles and drivers are closed 
	    	AssignmentForm.statusCheck(AO.getRequestId(), TR.getStartDate(), TR.getEndDate(), event);

	    	return new Forward("backToDetail", "assignment.jsp?id=" + id + "&status=f&source=" + source, true);
    	}
    	    	
        return new Forward("Back");
    }

    public static void statusCheck(String requestId, Date startDate, Date endDate, Event evt) {
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
			sendNotification(requestId, startDate, endDate, evt);
			transModule.updateCloseReqStatus(requestId, SetupModule.CLOSED_STATUS);
		}
    }
     
    private String getRateFacility(String id, double meterStart, double meterEnd, Date checkInDate, String strOldRate, String category){
    			
		RateCardObject rateObj = new RateCardObject();
		TransportModule transModule = (TransportModule) Application.getInstance().getModule(TransportModule.class);		
				
		double mileage = 0;
		double meter = 0;
		double amount = 0.0;				
		meter = meterEnd - meterStart;	
		
		try {						
			rateObj = transModule.selectRateCardByNameDate(category, checkInDate);
			if (!(null == rateObj)) {
				amount = Double.valueOf(rateObj.getAmount());					
			}								
			mileage = meter * amount / 1000;
			if(!(strOldRate == null || "".equals(strOldRate))){
				double oldRate = Double.valueOf(strOldRate);
				mileage = mileage + oldRate;					
			}
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
				
		return String.valueOf(mileage);
	
    }
    
    private static void sendNotification(String requestId, Date startDate, Date endDate, Event evt) {
    	//Send Notification
		TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
		FMSDepartmentManager deptManager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
		try {
			TransportRequest objNew = tm.selectTransportRequest(requestId);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String sDate = sdf.format(startDate);
			String eDate = sdf.format(endDate);
			String subject = "Transport Request Feedback Form";
			String emailTo[] = deptManager.getEmailRequestor(requestId);
			String requiredDate = sDate +" - "+eDate+".";
			String rate = objNew.getRate();
//			String link = "http://fms.mediaprima.com.my/ekms/fms/transport/feedbackForm.jsp?requestId="+requestId+"";
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
			Log.getLog(AssignmentForm.class).error("ERROR sendNotification " + er, er);
		}
	}

    
    public String getDefaultTemplate() {
    	if(CHECKOUT_STATUS.equals(status))
    		return "fms/assignmentcheckout";
    	
    	if(CLOSE_STATUS.equals(status))
    		return "fms/assignmentclose";
    	
        return "fms/assignmentform";
    }
    
    
    public String getStatusByFlag(String flagId) {
    	String vehicleNo = null;
    	String status = null;
    	Collection c = new ArrayList();
    	
    	AssignmentObject AO = new AssignmentObject();
    	
    	TransportModule transModule = (TransportModule) Application.getInstance().getModule(TransportModule.class);
    	TransportRequest tr = new TransportRequest();
    	
    	if(flagId != null){
	    	try{	
	    		tr = transModule.getTransportByFlagId(flagId);
	    		if(!(tr == null)){
	    			vehicleNo = tr.getVehicle_num();
	    		}
	    				
	    		c = transModule.getAssignmentDetailFlagIdVehicleNo(flagId, vehicleNo);
	    		if(c.size() > 0){
	    			for(Iterator it = c.iterator(); it.hasNext(); ){
	    				AO = (AssignmentObject) it.next();
	    				status = AO.getStatus();			
	    			}	
	    			
	    			if(SetupModule.CHECKOUT_STATUS.equals(status)){
	    				status = "c";
	    			} else if (SetupModule.CLOSED_STATUS.equals(status) 
	    					|| SetupModule.UNFULFILLED_STATUS.equals(status)
	    					|| SetupModule.OUTSOURCED_STATUS.equals(status)){
	    				status = "f";
	    			}
	    		} else {
	    			status = "n";
	    		}
	    		
	    	}catch(Exception e){
	    	}
    	}
    	
    	return status;
    }

	public TimeField getTime() {
		return time;
	}

	public void setTime(TimeField time) {
		this.time = time;
	}

	public TextField getSpeedoMeter() {
		return speedoMeter;
	}

	public void setSpeedoMeter(TextField speedoMeter) {
		this.speedoMeter = speedoMeter;
	}

	public SelectBox getPetrolCard() {
		return petrolCard;
	}

	public void setPetrolCard(SelectBox petrolCard) {
		this.petrolCard = petrolCard;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Collection getAssignmentDetails() {
		return assignmentDetails;
	}

	public void setAssignmentDetails(Collection assignmentDetails) {
		this.assignmentDetails = assignmentDetails;
	}

	public Button getUpdateButton() {
		return updateButton;
	}

	public void setUpdateButton(Button updateButton) {
		this.updateButton = updateButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public TransportRequest getTR() {
		return TR;
	}

	public void setTR(TransportRequest tr) {
		TR = tr;
	}

	public Label getDateRequired() {
		return dateRequired;
	}

	public void setDateRequired(Label dateRequired) {
		this.dateRequired = dateRequired;
	}

	public Label getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(Label requestBy) {
		this.requestBy = requestBy;
	}

	public Label getDriver() {
		return driver;
	}

	public void setDriver(Label driver) {
		this.driver = driver;
	}

	public Label getTimeRequired() {
		return timeRequired;
	}

	public void setTimeRequired(Label timeRequired) {
		this.timeRequired = timeRequired;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Button getUnfulfilledButton() {
		return unfulfilledButton;
	}

	public void setUnfulfilledButton(Button unfulfilledButton) {
		this.unfulfilledButton = unfulfilledButton;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AssignmentObject getAO() {
		return AO;
	}

	public Button getBackButton() {
		return backButton;
	}

	public void setBackButton(Button backButton) {
		this.backButton = backButton;
	}

	public void setAO(AssignmentObject ao) {
		AO = ao;
	}

	public String getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}

	public TextField getEndSpeedoMeter() {
		return endSpeedoMeter;
	}

	public void setEndSpeedoMeter(TextField endSpeedoMeter) {
		this.endSpeedoMeter = endSpeedoMeter;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public DatePopupField getEndDate() {
		return endDate;
	}

	public void setEndDate(DatePopupField endDate) {
		this.endDate = endDate;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

	public TextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(TextBox remarks) {
		this.remarks = remarks;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}

