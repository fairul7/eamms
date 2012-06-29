package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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

import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.RateCardObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleRequest;

public class TranAssignmentForm extends Form {
    
	protected String id;
	
	private TextField type;
	private Radio program;
	private Radio nonProgram;
	private ButtonGroup programUnit;	
	
	private TextBox purpose;
	private TextBox remarks;
	protected Button submitButton, cancelButton, draftButton, deleteDraftButton;    
	
	protected Button cancelReqButton;
	private VehicleSelect vehicleSelect;
	private ManPowerSelect manPowerSelect;	
	private TransportVehicle transportVehicle;
	
	protected Button backToDetailButton;
	
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
    
	public TranAssignmentForm() {
    }
   
	public void init()	{	
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
        
        submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("com.tms.fms.transport.submitForApproval", "Submit"));
                
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("calendar.label.cancel", "Cancel"));
        
        draftButton = new Button("draftButton");
        draftButton.setText(Application.getInstance().getMessage("com.tms.fms.transport.saveAsDraft", "Save"));
        
        deleteDraftButton = new Button("deleteDraftButton");
        deleteDraftButton.setText(Application.getInstance().getMessage("com.tms.fms.transport.deleteDraftButton", "Save"));
        
        backToDetailButton = new Button("backToDetailButton");
        backToDetailButton.setText(Application.getInstance().getMessage("com.tms.fms.transport.backToDetailsButton", "Back To Details"));
        
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
		addChild(submitButton);		
		addChild(cancelButton);
		addChild(draftButton);
		
		addChild(transportVehicle);
		addChild(cancelReqButton);
		
		addChild(deleteDraftButton);		
		addChild(backToDetailButton);
		
		addChild(rateCard);		
		
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
		return "fms/transport/tranAssignment";
    }
		
	public void onRequest(Event event){		
		super.onRequest(event);
		String view = event.getRequest().getParameter("view");
		
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		
		//id = event.getRequest().getParameter("requestId");
		act = event.getRequest().getParameter("do");
		idVehicle = event.getRequest().getParameter("vid");
		idDriver = event.getRequest().getParameter("did");
		
		if(id != null)
			populateFormSubmit(id);
				
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
		 if(backToDetailButton.getAbsoluteName().equals(findButtonClicked(evt))){
	    	 evt.getRequest().setAttribute("id", id);
	    	 return new Forward("backToDetail", "detailsRequest.jsp?id=" + id + "&view=submit", true);
		}		 
		return null;
	}	 
	 
	public void populateFormSubmit(String id){
		
		SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		tranreq = new TransportRequest();
		SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		request = null;
		String status = null;
		
		try{
			String approvedDate = " - ";
			
			tranreq = TM.selectTransportRequest(id);
			if(tranreq != null){
				titleL.setText(tranreq.getRequestTitle());
				typeL.setText(tranreq.getRequestType());
				
				try{
			        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			        startDateL.setText(sdf.format(tranreq.getStartDate()));
			        endDateL.setText(sdf.format(tranreq.getEndDate()));
			        
			        SimpleDateFormat stf = new SimpleDateFormat("k:mm");
			        startTimeL.setText(stf.format(tranreq.getStartDate()));
			        endTimeL.setText(stf.format(tranreq.getEndDate()));
			        
			        if(tranreq.getApprovedDate() != null)
			        	approvedDate = sdf.format(tranreq.getApprovedDate());
			        
		        }catch(Exception er){
		        	Log.getLog(getClass()).error("SimpleDateFormat error converting:"+er);
		        }
				
				destinationL.setText(tranreq.getDestination());
				purposeL.setText(tranreq.getPurpose());
				remarksL.setText(tranreq.getRemarks());
				
				statusL.setText(TM.selectStatus(tranreq.getStatus()));								
				programL.setText(SM.selectProgName(tranreq.getProgram()));
				request = TM.getVehicles(id);
								
				String requestName = security.getUser(tranreq.getRequestBy()).getName();
				String approvedName = security.getUser(tranreq.getApprovedBy()).getName();
				String requestDate = "";
				try{
					 SimpleDateFormat sdf =  new SimpleDateFormat("dd-MM-yyyy");
					 requestDate  = sdf.format(tranreq.getRequestDate());
				 }catch(Exception e){
					 Log.getLog(getClass()).error("SimpleDateFormat error :"+e);
				 }				
				
				requestBy.setText(requestName);
				approvedBy.setText(approvedName);
				
				rateCard.setText("RM " + tranreq.getRate());
			}
			
			//show assigned vehicles n drivers
    		try{
    			viewVehicles = TM.getVehicleByRequestId(id);    			
    			viewDrivers = TM.getDriverByRequestId(id);    			
    			
    		}catch(Exception er){
    			
    		}
    		
    		
    		//show requested vehicles n drivers
    		int veh = 0;
    		int dri = 0;
    		
    		for(Iterator it=request.iterator(); it.hasNext(); ){
    			VehicleRequest vr = (VehicleRequest) it.next();
    			veh += vr.getQuantity();
    			dri += vr.getDriver();
    		}
    		
    		allItems.setText(veh+" Car,"+ dri+" Driver");
    		
    		
    		
		}catch(Exception er){
			Log.getLog(getClass()).error("We got the prob:"+er);
		}
			   
	}
	
		
	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
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

	public Button getDraftButton() {
		return draftButton;
	}

	public void setDraftButton(Button draftButton) {
		this.draftButton = draftButton;
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

	public Button getDeleteDraftButton() {
		return deleteDraftButton;
	}

	public void setDeleteDraftButton(Button deleteDraftButton) {
		this.deleteDraftButton = deleteDraftButton;
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
	
	public Button getBackToDetailButton() {
		return backToDetailButton;
	}

	public void setBackToDetailButton(Button backToDetailButton) {
		this.backToDetailButton = backToDetailButton;
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

	public TransportRequest getTranreq() {
		return tranreq;
	}

	public void setTranreq(TransportRequest tranreq) {
		this.tranreq = tranreq;
	}
	
	
}



