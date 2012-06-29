package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.FmsNotification;
import com.tms.fms.transport.model.OutsourceObject;
import com.tms.fms.transport.model.RateCardObject;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleRequest;


public class AssignAssignmentForm extends Form {
    
	protected String id;
	private String requestId;
	private TextField type;
	private Radio program;
	private Radio nonProgram;
	private ButtonGroup programUnit;	
	
	private TextBox purpose;
	private TextBox remarks;
	protected Button viewAvailabilityButton, backToDetailButton, backToListButton;    
	
	protected Button cancelReqButton;
	private VehicleSelect vehicleSelect;
	private ManPowerSelect manPowerSelect;
	
	private TransportVehicle transportVehicle;
	
	private Label titleL;
	private Label typeL;
	private Label startDateL;
	private Label endDateL;
	private Label startTimeL;
	private Label endTimeL;
	private Label destinationL;
	private Label purposeL;
	private Label remarksL;
	private Label statusL;
	private Label programL;
	private Collection request;
	private Label requestBy;
	private Label approvedBy;
	
	
	private String submitStatus;
    
	private Collection viewVehicles;
	private Collection viewDrivers;
	
	private String[] vehicles;
	private String[] drivers;
	private Label allItems;
	
		
	// for deletion
	private String act = "";
	private String idVehicle = "";
	private String idDriver = "";
	
	private TransportRequest tranreq;
	
	private Label rateCard;
    
	public AssignAssignmentForm() {

    }
   
	public void init()
	{
	
		type = new TextField("type");
		type.setSize("30");     
		//type.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		
		program = new Radio("program", "Program");
		program.setOnClick("hasProgram();");
		
		nonProgram = new Radio("nonProgram", "Non Program");
		nonProgram.setOnClick("hasProgram();");
		nonProgram.setChecked(true);
		
		programUnit = new ButtonGroup("programUnit", new Radio[]{program, nonProgram});

	
        purpose = new TextBox("purpose");
        purpose.setMaxlength("10");
        purpose.setRows("4");
        
        remarks = new TextBox("remarks");
        remarks.setMaxlength("10");
        remarks.setRows("4");
        
        
        backToDetailButton = new Button("backToDetailButton");
        backToDetailButton.setText(Application.getInstance().getMessage("com.tms.fms.transport.backToDetailsButton", "Back To Details"));
        
        backToListButton = new Button("backToListButton");
        backToListButton.setText(Application.getInstance().getMessage("fms.tran.backToList", "Submit"));
               
        viewAvailabilityButton = new Button("viewAvailabilityButton");
        viewAvailabilityButton.setText(Application.getInstance().getMessage("fms.tran.viewAvailability", "View"));
        
        vehicleSelect = new VehicleSelect("vehicleSelect");
        vehicleSelect.setSortable(false);
        
        manPowerSelect = new ManPowerSelect("manPowerSelect");
        manPowerSelect.setSortable(false);
        
        transportVehicle = new TransportVehicle("transportVehicle");
        transportVehicle.init();
        
        cancelReqButton = new Button("cancelReqButton");
        cancelReqButton.setText(Application.getInstance().getMessage("fms.tran.cancelRequest", "Submit"));
               
        rateCard = new Label("rateCard");
       
		addChild(type);
		addChild(program);
		addChild(nonProgram);
		addChild(programUnit);
		addChild(purpose);
		addChild(remarks);
		addChild(transportVehicle);
		addChild(cancelReqButton);
		addChild(viewAvailabilityButton);
		addChild(rateCard);	
		addChild(backToDetailButton);
		addChild(backToListButton);
		
		
		
		titleL = new Label("titleL");
		typeL = new Label("typeL");
		startDateL = new Label("startDateL");
		endDateL = new Label("endDateL");
		startTimeL = new Label("startTimeL");
		endTimeL = new Label("endTimeL");
		destinationL = new Label("destinationL");
		purposeL = new Label("purposeL");
		remarksL = new Label("remarksL");
		statusL = new Label("statusL");
		programL = new Label("programL");
		requestBy = new Label("requestBy");
		approvedBy = new Label("approvedBy");
		allItems = new Label("allItems");
		
		
		addChild(titleL);
		addChild(typeL);
		addChild(startDateL);
		addChild(endDateL);
		addChild(startTimeL);
		addChild(endTimeL);
		addChild(destinationL);
		addChild(purposeL);
		addChild(remarksL);
		addChild(statusL);
		addChild(programL);
		addChild(requestBy);
		addChild(approvedBy);
		addChild(allItems);
		addChild(vehicleSelect);
		vehicleSelect.init();
		
		addChild(manPowerSelect);
		manPowerSelect.init();
		
	}	 
	
	
	public String getDefaultTemplate() {    

		return "fms/assignRequest";		
    }
	
	
	
	public void onRequest(Event event){
		
		
		super.onRequest(event);
		String view = event.getRequest().getParameter("view");
		
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		
		id = event.getRequest().getParameter("id");
		act = event.getRequest().getParameter("do");
		idVehicle = event.getRequest().getParameter("vid");
		idDriver = event.getRequest().getParameter("did");
		
		populateFormAssign(id);
	
	}
	
	//calculate rate for transport 
	protected double getTotalRate(String id, Date effDate){
			
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		RateCardObject rate = null;
		double total = 0.0;
		double vehicle = 0.0;
		double totalVehicle = 0.0;
		
		double driver = 0.0;
		double totalDriver = 0.0;
		
		VehicleRequest vr = new VehicleRequest();
		
		try {
			request = TM.getVehicles(id);	//get how many vehicle & driver
			for(Iterator it = request.iterator(); it.hasNext(); ){
				 vr = (VehicleRequest) it.next();
				 
				 //rate vehicle
				 rate = new RateCardObject();
				 rate = TM.selectRateCardByNameDate(vr.getName(), effDate);	
				 if(!(null == rate)){
					 vehicle = Double.valueOf(rate.getAmount());
					 vehicle = vehicle * vr.getQuantity();
					 totalVehicle += vehicle;
				 }
				 
				//rate driver
				 rate = new RateCardObject();
				 rate = TM.selectRateCardByNameDate("Driver", effDate);
				 if(!(null == rate)){
					 driver = Double.valueOf(rate.getAmount());
					 driver = driver * vr.getDriver();
					 totalDriver += driver;
				 }
				 
			}
			total = totalVehicle + totalDriver;
			
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		if( !(null == rate)){
			 
			
		}
				
		
		return total;
	}
	
		
	private String getState(){
		
		SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		
		try{
			Date from=tranreq.getStartDate();
			Date current=new Date();
			//from=DateUtil.dateAdd(current, Calendar.DATE, 7);
			long diff=dateDiff(from,current);
			if(diff<24){
				return sm.ADHOC_STATUS;
			}else if( diff <168){
				return sm.LATE_STATUS;
			}else {
				return sm.NORMAL_STATUS;
			}
		}catch(Exception e){}
		return sm.NORMAL_STATUS;
	}
	
	public long dateDiff(Date start, Date end){
		long diff = Math.round((start.getTime() - end.getTime()) / ( 60 * 60 * 1000));
		return diff;
	}
	
	public Forward onValidate(Event evt) {
			
		TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		if(viewAvailabilityButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
			vehicles = vehicleSelect.getIds();
			drivers = manPowerSelect.getIds();
			Date startDate = new Date();
			Date endDate = new Date();
			//id = 
			
			tranreq = new TransportRequest();
			 
			try{
				Collection coll = tm.getAssignmentByAssignmentId(id);
				 for(Iterator it = coll.iterator(); it.hasNext(); ){
					 tranreq = (TransportRequest) it.next();
				 }
				
				startDate = tranreq.getStartDate();
				endDate = tranreq.getEndDate();
			
				//start day from 12.00am - 11.59pm
		    	Calendar start = Calendar.getInstance();
		        start.setTime(startDate);                
		        start.set(Calendar.HOUR_OF_DAY, 00);
		        start.set(Calendar.MINUTE, 00);
		        start.set(Calendar.SECOND, 00);   
		        
		        Calendar end = Calendar.getInstance();
		        end.setTime(endDate);                
		        end.set(Calendar.HOUR_OF_DAY, 23);
		        end.set(Calendar.MINUTE, 59);
		        end.set(Calendar.SECOND, 59);      
		                      
		        startDate = start.getTime();
		        endDate = end.getTime();    	
		    	//
			}catch(Exception e){}
			
			
			if(vehicles.length == 0){
				return new Forward("vehicles_empty");
			}else{
				HttpServletRequest request = evt.getRequest();
	    		request.setAttribute("selectedKeys", vehicles);
	    		evt.setRequest(request);	    		
	    		
	    		request.setAttribute("selectedUser", drivers);
	    		evt.setRequest(request);
	    		
	    		request.setAttribute("startDate", startDate);
	    		evt.setRequest(request);
	    		
	    		request.setAttribute("endDate", endDate);
	    		evt.setRequest(request);
	    		
	    		request.setAttribute("assgId", id);
	    		evt.setRequest(request);
	    		
	    	
	    		return new Forward("vehicles");
			}		
		}
		
		if(backToListButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
	    	 return new Forward("Back");
		}
	    
	    if(backToDetailButton.getAbsoluteName().equals(findButtonClicked(evt))){	    
	    	 if(requestId != null)
	    		 return new Forward("backToDetail", "transportAssignment.jsp?requestId=" + requestId, true);
		}
		 
		return null;
	 }
	 
	
	public void populateFormAssign(String id){
		
		SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		tranreq = new TransportRequest();
		SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		request = null;
		String requestId = null;
		
		String time = "";
		////
		try{		
			 
			 Collection coll = TM.getAssignmentByAssignmentId(id);
			 for(Iterator it = coll.iterator(); it.hasNext(); ){
				 tranreq = (TransportRequest) it.next();
			 }
			 
			 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			 SimpleDateFormat stf = new SimpleDateFormat("k:mm");
			 startDateL.setText(sdf.format(tranreq.getStartDate()));
			 endDateL.setText(sdf.format(tranreq.getEndDate()));
			 
			 time = stf.format(tranreq.getStartDate())+" - "+stf.format(tranreq.getEndDate());
			 startTimeL.setText(time);
			 
			 requestId = tranreq.getRequestId();
			 request = TM.getVehicles(requestId);
		    
		 }catch(Exception er){
			 Log.getLog(getClass()).error("Error converting SimpleDateFormat:"+er);
		 }
		////
			 	
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TextField getType() {
		return type;
	}

	public void setType(TextField type) {
		this.type = type;
	}

	public Radio getProgram() {
		return program;
	}

	public void setProgram(Radio program) {
		this.program = program;
	}

	public Radio getNonProgram() {
		return nonProgram;
	}

	public void setNonProgram(Radio nonProgram) {
		this.nonProgram = nonProgram;
	}

	public ButtonGroup getProgramUnit() {
		return programUnit;
	}

	public void setProgramUnit(ButtonGroup programUnit) {
		this.programUnit = programUnit;
	}
	
	public TextBox getPurpose() {
		return purpose;
	}

	public void setPurpose(TextBox purpose) {
		this.purpose = purpose;
	}

	public TextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(TextBox remarks) {
		this.remarks = remarks;
	}
	
	public VehicleSelect getVehicleSelect() {
		return vehicleSelect;
	}

	public void setVehicleSelect(VehicleSelect vehicleSelect) {
		this.vehicleSelect = vehicleSelect;
	}

	public TransportVehicle getTransportVehicle() {
		return transportVehicle;
	}

	public void setTransportVehicle(TransportVehicle transportVehicle) {
		this.transportVehicle = transportVehicle;
	}


	public Label getTitleL() {
		return titleL;
	}

	public void setTitleL(Label titleL) {
		this.titleL = titleL;
	}

	public Label getTypeL() {
		return typeL;
	}

	public void setTypeL(Label typeL) {
		this.typeL = typeL;
	}

	public Label getStartDateL() {
		return startDateL;
	}

	public void setStartDateL(Label startDateL) {
		this.startDateL = startDateL;
	}

	public Label getEndDateL() {
		return endDateL;
	}

	public void setEndDateL(Label endDateL) {
		this.endDateL = endDateL;
	}

	public Label getStartTimeL() {
		return startTimeL;
	}

	public void setStartTimeL(Label startTimeL) {
		this.startTimeL = startTimeL;
	}

	public Label getEndTimeL() {
		return endTimeL;
	}

	public void setEndTimeL(Label endTimeL) {
		this.endTimeL = endTimeL;
	}

	public Label getDestinationL() {
		return destinationL;
	}

	public void setDestinationL(Label destinationL) {
		this.destinationL = destinationL;
	}

	public Label getPurposeL() {
		return purposeL;
	}

	public void setPurposeL(Label purposeL) {
		this.purposeL = purposeL;
	}

	public Label getRemarksL() {
		return remarksL;
	}

	public void setRemarksL(Label remarksL) {
		this.remarksL = remarksL;
	}

	public Label getStatusL() {
		return statusL;
	}

	public void setStatusL(Label statusL) {
		this.statusL = statusL;
	}

	public Collection getRequest() {
		return request;
	}

	public void setRequest(Collection request) {
		this.request = request;
	}

	public Label getProgramL() {
		return programL;
	}

	public void setProgramL(Label programL) {
		this.programL = programL;
	}

	public Button getCancelReqButton() {
		return cancelReqButton;
	}

	public void setCancelReqButton(Button cancelReqButton) {
		this.cancelReqButton = cancelReqButton;
	}
	
	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public Label getRequestBy() {
		return requestBy;
	}

	public void setSubmittedBy(Label requestBy) {
		this.requestBy = requestBy;
	}

	public Label getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Label approvedBy) {
		this.approvedBy = approvedBy;
	}

	public ManPowerSelect getManPowerSelect() {
		return manPowerSelect;
	}

	public void setManPowerSelect(ManPowerSelect manPowerSelect) {
		this.manPowerSelect = manPowerSelect;
	}

	
	public Collection getViewVehicles() {
		return viewVehicles;
	}

	public void setViewVehicles(Collection viewVehicles) {
		this.viewVehicles = viewVehicles;
	}

	public Collection getViewDrivers() {
		return viewDrivers;
	}

	public void setViewDrivers(Collection viewDrivers) {
		this.viewDrivers = viewDrivers;
	}

	public Label getAllItems() {
		return allItems;
	}

	public void setAllItems(Label allItems) {
		this.allItems = allItems;
	}
	
	public Button getViewAvailabilityButton() {
		return viewAvailabilityButton;
	}

	public void setViewAvailabilityButton(Button viewAvailabilityButton) {
		this.viewAvailabilityButton = viewAvailabilityButton;
	}
	
	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getIdVehicle() {
		return idVehicle;
	}

	public void setIdVehicle(String idVehicle) {
		this.idVehicle = idVehicle;
	}

	public String getIdDriver() {
		return idDriver;
	}

	public void setIdDriver(String idDriver) {
		this.idDriver = idDriver;
	}

	public Label getRateCard() {
		return rateCard;
	}

	public void setRateCard(Label rateCard) {
		this.rateCard = rateCard;
	}

	public Button getBackToDetailButton() {
		return backToDetailButton;
	}

	public void setBackToDetailButton(Button backToDetailButton) {
		this.backToDetailButton = backToDetailButton;
	}

	public Button getBackToListButton() {
		return backToListButton;
	}

	public void setBackToListButton(Button backToListButton) {
		this.backToListButton = backToListButton;
	}

	public TransportRequest getTranreq() {
		return tranreq;
	}

	public void setTranreq(TransportRequest tranreq) {
		this.tranreq = tranreq;
	}
	
	
}



