package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.tms.fms.engineering.model.Sequence;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.FmsNotification;
import com.tms.fms.transport.model.OutsourceObject;
import com.tms.fms.transport.model.RateCardObject;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleRequest;
import com.tms.fms.util.DateDiffUtil;


public class AdminForm extends Form {
    
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
	
	protected Button approveButton, rejectButton, backToListButton, submitOutsource, backToDetailButton;
	protected Button rejectReqButton, outsourceButton, editButton, backToListButton2;
	protected Button viewAvailabilityButton;
	protected Button createAssignmentButton;
	
	private boolean isPending = false;
	private boolean isSubmit = false;
	private boolean isAssign = false;
	private boolean isOutsource = false;
	private boolean isView = false;
	private boolean assignmentIsCreated = false;
	
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
	
	//outsource form
	private Label timeO;
	private Label allItems;
	private OutsourcePanelSelect outsourcePanelSelect;		
	private TextField outsourcepanel;
	private TextField quotationNo;
	private TextField quotationPrice;
	private TextField invoiceO;
	private TextField invoicePrice;
	private TextBox remarksO;
	
	// for deletion
	private String act = "";
	private String idVehicle = "";
	private String idDriver = "";
	
	private TransportRequest transportRequest;
	
	private Label rateCard;
    
	public AdminForm() {

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
        
        submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("com.tms.fms.transport.submitForApproval", "Submit"));
        
        submitOutsource = new Button("submitOutsource");
        submitOutsource.setText(Application.getInstance().getMessage("fms.tran.submit", "Submit"));
        
        approveButton = new Button("approveButton");
        approveButton.setText(Application.getInstance().getMessage("userRequest.label.approve", "approve"));
        
        viewAvailabilityButton = new Button("viewAvailabilityButton");
        viewAvailabilityButton.setText(Application.getInstance().getMessage("fms.tran.viewAvailability", "View"));
        
        createAssignmentButton = new Button("createAssignmentButton");
        createAssignmentButton.setText(Application.getInstance().getMessage("fms.label.createAssignment", "View"));
        
        rejectButton = new Button("rejectButton");
        rejectButton.setText(Application.getInstance().getMessage("userRequest.label.reject", "reject"));
       
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
        
        outsourcePanelSelect = new OutsourcePanelSelect("outsourcePanelSelect");
        outsourcePanelSelect.setSortable(false);
        
        manPowerSelect = new ManPowerSelect("manPowerSelect");
        manPowerSelect.setSortable(false);
        
        transportVehicle = new TransportVehicle("transportVehicle");
        transportVehicle.init();
        
        cancelReqButton = new Button("cancelReqButton");
        cancelReqButton.setText(Application.getInstance().getMessage("fms.tran.cancelRequest", "Submit"));
        
        backToListButton = new Button("backToListButton");
        backToListButton.setText(Application.getInstance().getMessage("fms.tran.backToList", "Submit"));
               
        backToListButton2 = new Button("backToListButton2");
        backToListButton2.setText(Application.getInstance().getMessage("fms.tran.backToList", "Submit"));
        
        outsourceButton = new Button("outsourceButton");
        outsourceButton.setText(Application.getInstance().getMessage("fms.tran.outsource", "Outsource"));
        
        editButton = new Button("editButton");
        editButton.setText(Application.getInstance().getMessage("fms.tran.edit", "Edit"));
        
        rejectReqButton = new Button("rejectReqButton");
        rejectReqButton.setText(Application.getInstance().getMessage("fms.tran.rejectRequest", "Reject"));
        
        rateCard = new Label("rateCard");
       
		addChild(type);
		addChild(program);
		addChild(nonProgram);
		addChild(programUnit);
		
		
		addChild(purpose);
		addChild(remarks);
		addChild(submitButton);
		addChild(submitOutsource);
		addChild(cancelButton);
		addChild(draftButton);
		
		addChild(transportVehicle);
		addChild(cancelReqButton);
		addChild(backToListButton);
		addChild(deleteDraftButton);
		addChild(approveButton);
		addChild(rejectButton);
		addChild(viewAvailabilityButton);
		addChild(createAssignmentButton);
		addChild(backToDetailButton);
		
		addChild(backToListButton2);
		addChild(outsourceButton);
		addChild(editButton);
		addChild(rejectReqButton);
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
		
		
		timeO = new Label("timeO");
		quotationNo = new TextField("quotationNo");
		quotationNo.setSize("30");     
		
		quotationPrice = new TextField("quotationPrice");
		quotationPrice.setSize("30"); 
		
		invoiceO = new TextField("invoiceO");
		invoiceO.setSize("30"); 
				
		remarksO = new TextBox("remarksO");
		remarksO.setMaxlength("10");
		remarksO.setRows("4");
		
		invoicePrice = new TextField("invoicePrice");
		invoicePrice.setSize("30");
		
		outsourcepanel = new TextField("outsourcepanel");
		outsourcepanel.setSize("30");
		
		
		addChild(timeO);
		addChild(quotationNo);
		addChild(quotationPrice);
		addChild(quotationPrice);
		addChild(remarksO);
		addChild(invoicePrice);
		addChild(invoiceO);
		addChild(outsourcepanel);
		addChild(outsourcePanelSelect);
		outsourcePanelSelect.init();
	}	 
	
	
	public String getDefaultTemplate() {    
		
		
		if(isSubmit)
			return "fms/detailsRequest";
		
		if(isAssign)
			return "fms/assignRequest";		
		
		if(isOutsource)
			return "fms/outsource";
			
		if(isView)
			return "fms/detailsView";
		
		return "fms/viewAllRequest";
			
			
    }
	
	SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
        
		
	public Forward actionPerformed(Event evt)
    {    
	    if(backToListButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
	    	 return new Forward("Back");
		}
	    
	    if(backToDetailButton.getAbsoluteName().equals(findButtonClicked(evt))){
	    	 evt.getRequest().setAttribute("id", id);
	    	 return new Forward("backToDetail", "detailsRequest.jsp?id=" + id + "&view=submit", true);
		}
	    
	    if(cancelButton.getAbsoluteName().equals(findButtonClicked(evt))){
	    	
	    	return  new Forward("Cancel");
	    }
	    
	    if(editButton.getAbsoluteName().equals(findButtonClicked(evt))){
	    	
	    	return  new Forward("Edit");
	    }
	
	    if(deleteDraftButton.getAbsoluteName().equals(findButtonClicked(evt))){
	    		
	    	evt.getRequest().setAttribute("id", id);
	    	return  new Forward("Deleted");
	    }
	    
	    if(submitButton.getAbsoluteName().equals(findButtonClicked(evt)))
	    	submitStatus = SetupModule.PENDING_STATUS;
	    
	    if(draftButton.getAbsoluteName().equals(findButtonClicked(evt)))
	    	submitStatus = SetupModule.DRAFT_STATUS;
	    	    
	    return super.actionPerformed(evt);
    }
	
	public void resetStatus(){
		isPending = false;
		isSubmit = false;
		isAssign = false;
		isOutsource = false;
	}
		
	
	public Forward onValidate(Event evt) {
								
		TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		String userId = getWidgetManager().getUser().getId();
		String reason = null;
		setOutsource(false);
		
		
		
		
		reason = (String) remarks.getValue();
		if(id == null)
			id = evt.getRequest().getParameter("id");
		
				
		if(approveButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
			try{
				
				tm.approveStatus(SetupModule.PROCESS_STATUS, reason, userId, getState(), id);
				tm.insertRequestStatus(id, SetupModule.PROCESS_STATUS, reason);
				
				//send notification
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String subject = Application.getInstance().getMessage("fms.notification.tran.approved.subject");
				String wordings = Application.getInstance().getMessage("fms.notification.tran.approved.body");
				String sDate = sdf.format(transportRequest.getStartDate());
				String eDate = sdf.format(transportRequest.getEndDate());
				this.sendNotificationToRequestors(transportRequest.getRequestTitle(), sDate, eDate, subject, wordings, "");
				
				
			}catch(Exception e){
				Log.getLog(getClass()).error(e);
			}			
	    	 return new Forward("Approve");
		}
		
		if(rejectButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
			try{
				tm.updateStatus(SetupModule.REJECTED_STATUS, reason, id, getWidgetManager().getUser().getId(),new Date());
				tm.insertRequestStatus(id, SetupModule.REJECTED_STATUS, reason);
				
				//send notification
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String subject = Application.getInstance().getMessage("fms.notification.tran.rejected.subject");
				String wordings = Application.getInstance().getMessage("fms.notification.tran.rejected.body");
				String sDate = sdf.format(transportRequest.getStartDate());
				String eDate = sdf.format(transportRequest.getEndDate());
				this.sendNotificationToRequestors(transportRequest.getRequestTitle(), sDate, eDate, subject, wordings, reason);
				
				
			}catch(Exception e){
				Log.getLog(getClass()).error(e);
			}			
	    	 return new Forward("Reject");
		}
		
		if(viewAvailabilityButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
			vehicles = vehicleSelect.getIds();
			drivers = manPowerSelect.getIds();
			Date startDate = new Date();
			Date endDate = new Date();
			//id = 
			
			transportRequest = new TransportRequest();
			try{
				transportRequest = tm.selectTransportRequest(id);
				startDate = transportRequest.getStartDate();
				endDate = transportRequest.getEndDate();
			
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
	    		
	    		request.setAttribute("requestId", id);
	    		evt.setRequest(request);
	    		
	    	
	    		return new Forward("vehicles");
			}		
		}
		
		if(outsourceButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
			try{
			evt.getResponse().sendRedirect("outsource.jsp?id="+id+"&view=outsource");
			}catch(Exception er){}
						
			
		}
		
		if(submitOutsource.getAbsoluteName().equals(findButtonClicked(evt))){
			
			OutsourceObject oo = new OutsourceObject();
			if(!(id == null) || "".equals(id)){
				
			
				
				transportRequest = new TransportRequest();
				
				try{				
					transportRequest = tm.selectTransportRequest(id);
				}catch(Exception er){				
				}
				
				if(outsourcePanelSelect.getIds().length == 0 ){
					outsourcePanelSelect.setInvalid(true);
					setInvalid(true);	
					try{
						evt.getResponse().sendRedirect("outsource.jsp?id="+id+"&view=outsource");
					}catch(Exception er){}
				
					return null;
				}
				
				double quoPrice=0;
				double invPrice=0;
				try{
					quoPrice = Double.parseDouble((String) quotationPrice.getValue());
				}catch(Exception e){
					quoPrice =0;
				}
				try{
					invPrice = Double.parseDouble((String) invoicePrice.getValue());
				}catch(Exception e){
					invPrice =0;
				}
				oo.setRequestId(id);				
				oo.setId(UuidGenerator.getInstance().getUuid());				
				oo.setStartDate(transportRequest.getStartDate());
				oo.setEndDate(transportRequest.getEndDate());
				oo.setCreatedBy(getWidgetManager().getUser().getId());
				oo.setSetup_id(outsourcePanelSelect.getIds());
				oo.setQuotationNo((String) quotationNo.getValue());
				oo.setQuotationPrice(quoPrice);			
				oo.setInvoiceNo((String)invoiceO.getValue());
				oo.setInvoicePrice(invPrice);
				oo.setRemark((String) remarksO.getValue());
				reason = "Outsourced";
				
				try{
					tm.insertOutsource(oo);
					tm.updateStatus(SetupModule.OUTSOURCED_STATUS, reason, id, getWidgetManager().getUser().getId(),new Date());
					
					sendNotificationToRequestor(id, oo);
				}catch(Exception er){Log.getLog(getClass()).error("error on insertOutsource :"+er);}
				
				return new Forward("Outsource");
			}					
		}
		
		
		if(createAssignmentButton.getAbsoluteName().equals(findButtonClicked(evt))){
			
			String bb = "";
			String assId = "";
			TransportRequest tReq = new TransportRequest();
			try{
				tReq = tm.getTransportRequest(id);
				bb = tReq.getBlockBooking();
			}catch(Exception er){}
			
			
			//set assignment endDate to same day but folow request endDate time
			Date sDate = getDateFromStartTime(tReq.getStartDate());
			Date eDate = getDateToEndTime(tReq.getEndDate());
			long diffdate = dateDiff4Assignment(sDate, eDate);        
			Date start = tReq.getStartDate();        				
			Date tmpEnd = tReq.getEndDate();
			Date end = new Date();
			
			Calendar cL = Calendar.getInstance();			
			cL.setTime(tmpEnd);
			int hr = cL.get(Calendar.HOUR_OF_DAY);
			int min= cL.get(Calendar.MINUTE);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			
			cal.set(Calendar.HOUR_OF_DAY, hr);
			cal.set(Calendar.MINUTE, min);
			end = cal.getTime();
			
			if("1".equals(bb)){  				//if block booking is true
    			for(int i = 0; i <= diffdate; i++){
    				assId = new Sequence(Sequence.TYPE_TRANSPORT_ASSIGNMENT).genarteCode();	
    				TransportRequest tr = new TransportRequest();
		        	tr.setId(assId);
		        	
		        	long sChecked = start.getTime() + (DateDiffUtil.MS_IN_A_DAY * i);
					Date dateStart = new Date(sChecked);	
					
					long eChecked = end.getTime() + (DateDiffUtil.MS_IN_A_DAY * i);
					Date dateEnd = new Date(eChecked);
					
					tr.setStartDate(dateStart);
					tr.setEndDate(dateEnd);
					tr.setRequestId(id);
					tr.setStatus(SetupModule.NEW_STATUS);					
		        	
					try{        		
		        		tm.insertTranAssigment(tr);
		        		
		        	} catch(Exception er) {
		        		Log.getLog(getClass()).error("Error when assigning vehicle: "+er);
		        	}
    			}
			}else{
				
				assId = new Sequence(Sequence.TYPE_TRANSPORT_ASSIGNMENT).genarteCode();	
				TransportRequest tr = new TransportRequest();
	        	tr.setId(assId);
				tr.setStartDate(start);
				tr.setEndDate(tmpEnd);
				tr.setRequestId(id);
				tr.setStatus(SetupModule.NEW_STATUS);			       	
				try{        		
	        		tm.insertTranAssigment(tr);
	        		
	        	} catch(Exception er) {
	        		Log.getLog(getClass()).error("Error when assigning vehicle: "+er);
	        	}
	        	
	    	}
			
			try{
			evt.getResponse().sendRedirect("transportAssignment.jsp?requestId="+id);
			}catch(Exception er){}
						
			
		}
		

		return null;
	}
		

	protected void sendNotificationToRequestors(String title, String sDate, String eDate, String subject, String wordings, String remarks){
		
		//Send Notification
		
		TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		
		try {
						
			
			String requiredDate = sDate +" - "+eDate;
						
			tm.sendNotificationToRequestors(id, title, requiredDate, subject, wordings, remarks);
			
		} catch (Exception er) {
			Log.getLog(getClass())
					.error("ERROR sendNotification " + er);
		}
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
	
	public void onRequest(Event event){
		
		setSubmit(false);
		super.onRequest(event);
		String view = event.getRequest().getParameter("view");
				
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		
		id = event.getRequest().getParameter("id");
		act = event.getRequest().getParameter("do");
		idVehicle = event.getRequest().getParameter("vid");
		idDriver = event.getRequest().getParameter("did");
		
		
		if("submit".equals(view)){
			setSubmit(true);
			
			if (getId() != null){
				if (getAct() != null){
					if (getAct().equals("deleteVehicle") && getIdVehicle() != null){
						TM.deleteAssignedVehicle(getId(), getIdVehicle());
					} else if (getAct().equals("deleteDriver") && getIdDriver() != null){
						TM.deleteAssignedDriver(getId(), getIdDriver());
					}
				}
			}
			
			populateFormSubmit(id);
		}
		
		if("assign".equals(view)){
			setAssign(true);
			//setSubmit(true);
			populateFormAssign(id);
		}
		
		if("pending".equals(view)){
			
			populateFormPending(id);
		}
		
		if("outsource".equals(view)){
			setOutsource(true);
			populateFormOutsource(id);
		}
		

		if("view".equals(view)){
			setView(true);
			
			if (getId() != null){
				if (getAct() != null){
					if (getAct().equals("deleteVehicle") && getIdVehicle() != null){
						TM.deleteAssignedVehicle(getId(), getIdVehicle());
					} else if (getAct().equals("deleteDriver") && getIdDriver() != null){
						TM.deleteAssignedDriver(getId(), getIdDriver());
					}
				}
			}
			
			populateFormSubmit(id);
		}
		
		event.getRequest().getSession().setAttribute("requestId",id );
		event.getRequest().getSession().setAttribute("action", act );
		
		
		//show/hide create assignment button
		assignmentIsCreated = false;
		Collection colass = new ArrayList();
		try{
			colass = TM.getAssignmentByRequestId(id);
		}catch(Exception er){}
		
		if(colass.size() > 0){
			assignmentIsCreated = true;
		}
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
	
	protected void sendNotificationToRequestor(String requestId, OutsourceObject outObj){
		
		String wordings = Application.getInstance().getMessage("fms.label.requestOutsourced");
		String subject = wordings.replace("{REQUESTID}", requestId);
		Application app = Application.getInstance();
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
				
		String outsourceItem = "";
		String setupId = "";
		for(int i=0; i<outObj.getSetup_id().length; i++){
			setupId = (outObj.getSetup_id()[i]);
			SetupObject o = (SetupObject)TM.getSetupObject("fms_tran_outsourcepanel", setupId);
			outsourceItem += o.getName()+", ";
		}
		
		String body = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">" +
        "</head><body>" +
        "<style>" +
        ".contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}" +
        "</style>" +
        "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">" +
        "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">" +
        "<b><U>" +
        app.getMessage("fms.outsource.outsourceDetails") +
        "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>" +
        "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>" +
        "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">" +
        "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
        
        "<b>" + app.getMessage("fms.outsource.outsourcePanel") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + outsourceItem + "</td></tr>" +
        "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
        
        "<b>" + app.getMessage("fms.outsource.quotationNo") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + outObj.getQuotationNo() + "</td></tr>" +
        "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
        
        "<b>" + app.getMessage("fms.outsource.quotationPrice") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + outObj.getQuotationPrice() + "</td></tr>" +
        "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
        
        "<b>" + app.getMessage("fms.outsource.invoiceNo") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + outObj.getInvoiceNo() + "</td></tr>" +
        "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
        
        "<b>" + app.getMessage("fms.outsource.invoicePrice") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + outObj.getInvoicePrice() + "</td></tr>" +
        "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
        
        "<b>"  + app.getMessage("fms.tran.remarks") +"</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + outObj.getRemark() + "</td></tr>" +
        "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +            
        "</td></tr></table></td></tr><tr><td colspan=\"2\" valign=\"TOP\"  class=\"contentBgColorMail\">&nbsp;</td>" +
        "</tr></table><p>&nbsp; </p></body></html>";
		
		FmsNotification notification = new FmsNotification();		
		FMSDepartmentManager deptManager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);			
				
		
		try{			
			String emailTo[] = deptManager.getEmailRequestor(requestId);			
			notification.send(emailTo, subject, body);
			
		}catch(Exception er){
			Log.getLog(getClass()).error("TransportModule: ERROR sendNotification "+er);
		}					
	}
	
	public void populateFormPending(String id){
		
		
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		transportRequest = new TransportRequest();
		SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		request = null;
		
		try{
			
			transportRequest = TM.selectTransportRequest(id);
			if(transportRequest != null){
				titleL.setText(transportRequest.getRequestTitle());
				typeL.setText(transportRequest.getRequestType());
				
				try{
			        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			        startDateL.setText(sdf.format(transportRequest.getStartDate()));
			        endDateL.setText(sdf.format(transportRequest.getEndDate()));
			        
			        SimpleDateFormat stf = new SimpleDateFormat("k:mm");
			        startTimeL.setText(stf.format(transportRequest.getStartDate()));
			        endTimeL.setText(stf.format(transportRequest.getEndDate()));
			        
		        }catch(Exception er){
		        	Log.getLog(getClass()).error("SimpleDateFormat error converting:"+er);
		        }
				
				destinationL.setText(transportRequest.getDestination());
				purposeL.setText(transportRequest.getPurpose());
				remarksL.setText(transportRequest.getRemarks());
				
				statusL.setText(TM.selectStatus(transportRequest.getStatus()));
								
				programL.setText(SM.selectProgName(transportRequest.getProgram()));			
				
				request = TM.getVehicles(id, true);
				
				String rate = transportRequest.getRate();
				if(null == rate || "".equals(rate))
					rate = "0";				
				rateCard.setText("RM " + rate);
			}
    		
			
		}catch(Exception er){
			Log.getLog(getClass()).error("We got the prob:"+er);
		}
			   
	}
	
	private String getState(){
		try{
			Date reqDate=transportRequest.getStartDate();
			Date current=new Date();
			//from=DateUtil.dateAdd(current, Calendar.DATE, 7);
			long diff=dateDiff(current,reqDate);
			if(diff<24){
				return SetupModule.ADHOC_STATUS;
			}else if( diff <168){
				return SetupModule.LATE_STATUS;
			}else {
				return SetupModule.NORMAL_STATUS;
			}
		}catch(Exception e){}
		return SetupModule.NORMAL_STATUS;
	}
	
	 public long dateDiff(Date start, Date end){
		 
			long diff = Math.round((end.getTime() - start.getTime()) / ( 60 * 60 * 1000));			
			return diff;
		}
	
	 public long dateDiff4Assignment(Date start, Date end){
		 
			long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));			
			return diff;
		}
	
	public void populateFormSubmit(String id){
		
		SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		transportRequest = new TransportRequest();
		SetupModule SM = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		request = null;
		
		try{
			transportRequest = TM.selectTransportRequest(id);
			if(transportRequest != null){
				titleL.setText(transportRequest.getRequestTitle());
				typeL.setText(transportRequest.getRequestType());
				
				try{
			        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			        startDateL.setText(sdf.format(transportRequest.getStartDate()));
			        endDateL.setText(sdf.format(transportRequest.getEndDate()));
			        
			        SimpleDateFormat stf = new SimpleDateFormat("k:mm");
			        startTimeL.setText(stf.format(transportRequest.getStartDate()));
			        endTimeL.setText(stf.format(transportRequest.getEndDate()));
			        
		        }catch(Exception er){
		        	Log.getLog(getClass()).error("SimpleDateFormat error converting:"+er);
		        }
				
				destinationL.setText(transportRequest.getDestination());
				purposeL.setText(transportRequest.getPurpose());
				remarksL.setText(transportRequest.getRemarks());
				
				statusL.setText(TM.selectStatus(transportRequest.getStatus()));								
				programL.setText(SM.selectProgName(transportRequest.getProgram()));
				request = TM.getVehicles(id, true);
								
				String requestName = security.getUser(transportRequest.getRequestBy()).getName();
				String approvedName = security.getUser(transportRequest.getApprovedBy()).getName();
				
				requestBy.setText(requestName);// + "["+ requestDate +"]");				
				approvedBy.setText(approvedName);// + "["+ approvedDate +"]");
								
				rateCard.setText("RM " + transportRequest.getRate());
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
	
	
	public void populateFormAssign(String id){
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		transportRequest = new TransportRequest();
		request = null;
		
		String time = "";
		try{
			
			transportRequest = TM.selectTransportRequest(id);
			if(transportRequest != null){
								
				try{
			        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			        startDateL.setText(sdf.format(transportRequest.getStartDate()));
			        endDateL.setText(sdf.format(transportRequest.getEndDate()));
			        
			        SimpleDateFormat stf = new SimpleDateFormat("hh:mm");
			        time = stf.format(transportRequest.getStartDate())+" - "+ stf.format(transportRequest.getEndDate());
			        startTimeL.setText(time);
			        
			        
		    		manPowerSelect.setSDate(transportRequest.getStartDate());
		    		manPowerSelect.setEDate(transportRequest.getEndDate());
		    		
			        
		        }catch(Exception er){
		        	Log.getLog(getClass()).error("SimpleDateFormat error converting:"+er);
		        }
		        
			}
    		
			
		}catch(Exception er){
			Log.getLog(getClass()).error("We got the prob:"+er);
		}
			   
	}
	
	
	public void populateFormOutsource(String id){
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		transportRequest = new TransportRequest();
		
		try{
			
			transportRequest = TM.selectTransportRequest(id);
			if(transportRequest != null){
				titleL.setText(transportRequest.getRequestTitle());
				typeL.setText(transportRequest.getRequestType());
				
				try{
			        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			        startDateL.setText(sdf.format(transportRequest.getStartDate()));
			        endDateL.setText(sdf.format(transportRequest.getEndDate()));
			        
			        SimpleDateFormat stf = new SimpleDateFormat("h:mm a");
			        startTimeL.setText(stf.format(transportRequest.getStartDate()));
			        endTimeL.setText(stf.format(transportRequest.getEndDate()));
			        
		        }catch(Exception er){
		        	Log.getLog(getClass()).error("SimpleDateFormat error converting:"+er);
		        }
				
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
    		
    		request = TM.getVehicles(id);
    		for(Iterator it=request.iterator(); it.hasNext(); ){
    			VehicleRequest vr = (VehicleRequest) it.next();
    			veh += vr.getQuantity();
    			dri += vr.getDriver();
    		}
    		
    		allItems.setText(veh+" Car,"+ dri+" Driver");
    		
    		timeO.setText(startTimeL.getText()+ "-" +endTimeL.getText());			
			
		}catch(Exception er){
			Log.getLog(getClass()).error("We got the prob:"+er);
		}
			   
	}
	
	public void populate(String id){
		init();
		setId(id);
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		transportRequest = new TransportRequest();
		request = null;
			
		try{
			transportRequest = TM.selectTransportRequest(id);			
		
			
			if(!("-".equals(transportRequest.getProgram()))){
				program.setChecked(true);
				nonProgram.setChecked(false);
				String[] progs = new String[1];
				progs[0] = transportRequest.getProgram();
				//programSelect.setIds(progs);
			}
			
			
			
			purpose.setValue(transportRequest.getPurpose());
			remarks.setValue(transportRequest.getRemarks());
			
			statusL.setText(TM.selectStatus(transportRequest.getStatus()));
			
			
			
		}catch(Exception e){Log.getLog(getClass()).error("Error on populateFormEdit:"+e);}
				
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

	public Button getBackToListButton() {
		return backToListButton;
	}

	public void setBackToListButton(Button backToListButton) {
		this.backToListButton = backToListButton;
	}

	public Button getDeleteDraftButton() {
		return deleteDraftButton;
	}

	public void setDeleteDraftButton(Button deleteDraftButton) {
		this.deleteDraftButton = deleteDraftButton;
	}

	
	public Button getApproveButton() {
		return approveButton;
	}

	public void setApproveButton(Button approveButton) {
		this.approveButton = approveButton;
	}

	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public Button getRejectButton() {
		return rejectButton;
	}

	public void setRejectButton(Button rejectButton) {
		this.rejectButton = rejectButton;
	}

	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}

	public boolean isSubmit() {
		return isSubmit;
	}

	public void setSubmit(boolean isSubmit) {
		this.isSubmit = isSubmit;
	}

	public Button getRejectReqButton() {
		return rejectReqButton;
	}

	public void setRejectReqButton(Button rejectReqButton) {
		this.rejectReqButton = rejectReqButton;
	}

	public Button getOutsourceButton() {
		return outsourceButton;
	}

	public void setOutsourceButton(Button outsourceButton) {
		this.outsourceButton = outsourceButton;
	}

	public Button getEditButton() {
		return editButton;
	}

	public void setEditButton(Button editButton) {
		this.editButton = editButton;
	}

	public Button getBackToListButton2() {
		return backToListButton2;
	}

	public void setBackToListButton2(Button backToListButton2) {
		this.backToListButton2 = backToListButton2;
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
	
	public boolean isAssign() {
		return isAssign;
	}

	public void setAssign(boolean isAssign) {
		this.isAssign = isAssign;
	}

	public ManPowerSelect getManPowerSelect() {
		return manPowerSelect;
	}

	public void setManPowerSelect(ManPowerSelect manPowerSelect) {
		this.manPowerSelect = manPowerSelect;
	}

	public Button getViewAvailabilityButton() {
		return viewAvailabilityButton;
	}

	public void setViewAvailabilityButton(Button viewAvailabilityButton) {
		this.viewAvailabilityButton = viewAvailabilityButton;
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

	public boolean isOutsource() {
		return isOutsource;
	}

	public void setOutsource(boolean isOutsource) {
		this.isOutsource = isOutsource;
	}

	public Label getAllItems() {
		return allItems;
	}

	public void setAllItems(Label allItems) {
		this.allItems = allItems;
	}

	public Label getTimeO() {
		return timeO;
	}

	public void setTimeO(Label timeO) {
		this.timeO = timeO;
	}

	public TextField getQuotationNo() {
		return quotationNo;
	}

	public void setQuotationNo(TextField quotationNo) {
		this.quotationNo = quotationNo;
	}

	public TextField getQuotationPrice() {
		return quotationPrice;
	}

	public void setQuotationPrice(TextField quotationPrice) {
		this.quotationPrice = quotationPrice;
	}

	public TextField getInvoiceO() {
		return invoiceO;
	}

	public void setInvoiceO(TextField invoiceO) {
		this.invoiceO = invoiceO;
	}

	public TextBox getRemarksO() {
		return remarksO;
	}

	public void setRemarksO(TextBox remarksO) {
		this.remarksO = remarksO;
	}

	public TextField getInvoicePrice() {
		return invoicePrice;
	}

	public void setInvoicePrice(TextField invoicePrice) {
		this.invoicePrice = invoicePrice;
	}

	public TextField getOutsourcepanel() {
		return outsourcepanel;
	}

	public void setOutsourcepanel(TextField outsourcepanel) {
		this.outsourcepanel = outsourcepanel;
	}

	public Button getSubmitOutsource() {
		return submitOutsource;
	}

	public void setSubmitOutsource(Button submitOutsource) {
		this.submitOutsource = submitOutsource;
	}
	
	public Button getBackToDetailButton() {
		return backToDetailButton;
	}

	public void setBackToDetailButton(Button backToDetailButton) {
		this.backToDetailButton = backToDetailButton;
	}

	public OutsourcePanelSelect getOutsourcePanelSelect() {
		return outsourcePanelSelect;
	}

	public void setOutsourcePanelSelect(OutsourcePanelSelect outsourcePanelSelect) {
		this.outsourcePanelSelect = outsourcePanelSelect;
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

	public Button getCreateAssignmentButton() {
		return createAssignmentButton;
	}

	public boolean isAssignmentIsCreated() {
		return assignmentIsCreated;
	}

	public void setAssignmentIsCreated(boolean assignmentIsCreated) {
		this.assignmentIsCreated = assignmentIsCreated;
	}

	public void setCreateAssignmentButton(Button createAssignmentButton) {
		this.createAssignmentButton = createAssignmentButton;
	}

	public boolean isView() {
		return isView;
	}

	public void setView(boolean isView) {
		this.isView = isView;
	}

	public TransportRequest getTransportRequest() {
		return transportRequest;
	}

	public void setTransportRequest(TransportRequest transportRequest) {
		this.transportRequest = transportRequest;
	}
	
	
	
}



