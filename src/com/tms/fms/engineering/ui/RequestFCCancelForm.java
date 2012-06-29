package com.tms.fms.engineering.ui;


import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;
import com.tms.fms.abw.model.AbwModule;
import com.tms.fms.abw.model.TransferCostCancellationObject;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.facility.model.SetupModule;


public class RequestFCCancelForm extends Form {
	protected Label idLbl;
	protected Hidden hdRequestId;
	protected Label nameLbl;
	protected String systemCalculatedCharges;
	protected TextField cancellationCharges;
	protected String requestId;   
	
	protected Hidden stat;	
	
	protected Button submitButton;
	protected Button cancelButton;
	
	private String cancelUrl = "outsourceRequestFC.jsp?requestId=";
	
	private String action = "";
	
	protected String totalFacilitiesCost;
	protected String totalManpowerCost;
	protected TextField cancellationCostManpower;
	protected Date requiredTo;
	protected String requestType;
	
	
	public RequestFCCancelForm() {

    }
   
	public void init()
	{
		setColumns(1);
		setMethod("POST");
		String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");
		
		idLbl = new Label("idLbl");
		addChild(idLbl);
		
		nameLbl = new Label("nameLbl");
		addChild(nameLbl);
		
		//systemCalculatedCharges = new Label("systemCalculatedCharges");
		//addChild(systemCalculatedCharges);
		
		cancellationCharges = new TextField("cancellationCharges");
		cancellationCharges.setSize("7");
		//cancellationCharges.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		cancellationCharges.addChild(new ValidatorIsNumeric("cancellationChargesIsNumeric", msgIsNumberic, false));
		addChild(cancellationCharges);
		
		cancellationCostManpower = new TextField("cancellationCostManpower");
		cancellationCostManpower.setSize("7");
		//cancellationCostManpower.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		cancellationCostManpower.addChild(new ValidatorIsNumeric("cancellationChargesIsNumeric", msgIsNumberic, false));
		addChild(cancellationCostManpower);
		
		hdRequestId = new Hidden("hdRequestId");
		addChild(hdRequestId);
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("fms.facility.submit", "Submit"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));        
        cancelButton.setOnClick("window.close();");
        
        addChild(submitButton);
		addChild(cancelButton);	
		
	}	 
	
	public void populateForm(String id){
		Application app = Application.getInstance();
		FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)app.getModule(FacilitiesCoordinatorModule.class);
		EngineeringModule mod = (EngineeringModule)app.getModule(EngineeringModule.class);
		
		try{
			EngineeringRequest er = fcm.getRequestById(id);	
			double systemCalCharges = fcm.selectTotalSystemCalculatedCharges(er.getRequestId());
			
			double totalFacCost = mod.getTotalRateFacilities(er.getRequestId(), er.getRequestType());
			double totalMpowerCost = mod.getTotalRateManpower(er.getRequestId(), er.getRequestType());
			//requiredTo = er.getRequiredTo();
			
			idLbl.setText(er.getRequestId());
			
			//systemCalculatedCharges.setText(String.valueOf(systemCalCharges));
			systemCalculatedCharges=String.valueOf(systemCalCharges);
			
			//TODO
			totalFacilitiesCost = String.valueOf(totalFacCost);
			totalManpowerCost = String.valueOf(totalMpowerCost);
			
			nameLbl.setText(er.getTitle());			
			
			hdRequestId.setValue(er.getRequestId());
			requestType = er.getRequestType();
		} catch(Exception e){
		}
		
	}
	
	public String getDefaultTemplate() {
		return "fms/requestFCcancelform";
    }
	
	public void onRequest(Event evt){
		this.setInvalid(false);
		//requestId = evt.getRequest().getParameter("requestId");
		action = evt.getRequest().getParameter("do");		
		init();
		populateForm(getRequestId());
	}
	
	public Forward onValidate(Event evt){
		Application application = Application.getInstance();
		FacilitiesCoordinatorModule module = (FacilitiesCoordinatorModule)application.getModule(FacilitiesCoordinatorModule.class);
		EngineeringModule mod = (EngineeringModule)application.getModule(EngineeringModule.class);
		
		//if (Integer.parseInt((String)cancellationCharges.getValue())<0){
		if (Double.parseDouble((String) cancellationCharges.getValue()) < 0){
			cancellationCharges.setInvalid(true);
			return new Forward("WARNING");
		}

		//if (Integer.parseInt((String)cancellationCostManpower.getValue())<0){
		if (Double.parseDouble((String) cancellationCostManpower.getValue()) < 0){
			cancellationCostManpower.setInvalid(true);
			return new Forward("WARNING");
		}
		
		Date now = new Date();
		EngineeringRequest er = new EngineeringRequest();
		er.setCancellationCharges((String)cancellationCharges.getValue()); //treat this as facilities cancellation charge
		er.setCancellationCostManpower((String)cancellationCostManpower.getValue());
		
		er.setRequestId((String)hdRequestId.getValue());
		er.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
		er.setModifiedOn(now);
		er.setStatus(EngineeringModule.CANCELLED_STATUS);
		
		module.updateRequestFCCancel(er);
		
		//update transport request (OB Van)
		mod.updateTranportRequest(er.getRequestId());
		
		// update transfer cost with cancellation cost
		transferCostToABW(er);
		
		//Forward fwd = new Forward("continue", "requestFCcancel.jsp?requestId=" + hdRequestId.getValue(), true);
		Forward fwd = new Forward("UPDATE");
		init();
		return fwd;
	}		
	
	public void transferCostToABW(EngineeringRequest er){
		EngineeringRequest request = null;
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		AbwModule am = (AbwModule)Application.getInstance().getModule(AbwModule.class);
		
		request = module.getRequestWithService(requestId);
		if(request != null){
			String pfeCode = (String) request.getProperty("pfecode");
			// skip if program code not present #13016
			if(!(pfeCode == null || pfeCode.equals(""))) 
			{
				Calendar endDate = Calendar.getInstance();
				endDate.setTime(request.getRequiredTo());
				Calendar currentDate = Calendar.getInstance();
				
				Collection services = request.getServices();
				if(services != null){
					for (Iterator iterator = services.iterator(); iterator.hasNext();) {
						Service service=(Service)iterator.next();
						
						request.setServiceId(service.getServiceId());
						if(currentDate.after(endDate)){
							String serviceName = module.getServiceName (service.getServiceId());
							if(serviceName.equals("manpower")){
								request.setProperty("serviceName", "manpower");
								Collection serviceCol = module.getManpowerByRequestId(requestId);
								module.pushCancelCostToABW(serviceCol, request);
							}else if(serviceName.equals("scp")){
								request.setProperty("serviceName", "scp");
								Collection serviceCol = module.getScpByRequestId(requestId);
								module.pushCancelCostToABW(serviceCol, request);
							}else if(serviceName.equals("other")){
								request.setProperty("serviceName", "other");
								Collection serviceCol = module.getOtherByRequestId(requestId);
								module.pushCancelCostToABW(serviceCol, request);
							}else if(serviceName.equals("post")){
								request.setProperty("serviceName", "post");
								Collection serviceCol = module.getPostproductionByRequestId(requestId);
								module.pushCancelCostToABW(serviceCol, request);
							}else if(serviceName.equals("studio")){
								request.setProperty("serviceName", "studio");
								Collection serviceCol = module.getStudioByRequestId(requestId);
								module.pushCancelCostToABW(serviceCol, request);
							}else if(serviceName.equals("tvro")){
								request.setProperty("serviceName", "tvro");
								Collection serviceCol = module.getTvroByRequestId(requestId);
								module.pushCancelCostToABW(serviceCol, request);
							}else if(serviceName.equals("vtr")){
								request.setProperty("serviceName", "vtr");
								Collection serviceCol = module.getVtrByRequestId(requestId);
								module.pushCancelCostToABW(serviceCol, request);
							}
						}
					}
		
					// Cancellation cost Facilities
					TransferCostCancellationObject transferCostObj = new TransferCostCancellationObject();
					transferCostObj.setUniqueId(UuidGenerator.getInstance().getUuid());
					transferCostObj.setProjectCode((String) request.getProperty("pfecode"));
					transferCostObj.setRequestId(requestId);
					transferCostObj.setCost(Double.valueOf(cancellationCharges.getValue().toString()));
					transferCostObj.setType(request.getServiceId());
					transferCostObj.setCreatedDate(new Date());	
					transferCostObj.setCreatedBy(SetupModule.FMS_SYSTEM_ADMIN);
					transferCostObj.setStatus("N");
					transferCostObj.setCancellation_ind("P");				
					transferCostObj.setCancellation_remark(module.getAdditionalInfo(request.getRequestId())); 
					am.insertTransferCostReversal(transferCostObj);
					
					// Cancellation cost Manpower
					transferCostObj.setUniqueId(UuidGenerator.getInstance().getUuid());
					transferCostObj.setCost(Double.valueOf(cancellationCostManpower.getValue().toString()));
					transferCostObj.setType("4");
					am.insertTransferCostReversal(transferCostObj);
				}
			}
		}
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
	public String getCancelUrl(){
		return cancelUrl;
	}	
	public void setCancelUrl(String cancelUrl){
		this.cancelUrl=cancelUrl;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Label getNameLbl() {
		return nameLbl;
	}

	public void setNameLbl(Label nameLbl) {
		this.nameLbl = nameLbl;
	}

	public Label getIdLbl() {
		return idLbl;
	}

	public void setIdLbl(Label idLbl) {
		this.idLbl = idLbl;
	}

	public TextField getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(TextField cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public Hidden getHdRequestId() {
		return hdRequestId;
	}

	public void setHdRequestId(Hidden hdRequestId) {
		this.hdRequestId = hdRequestId;
	}

	public String getSystemCalculatedCharges() {
		return systemCalculatedCharges;
	}

	public void setSystemCalculatedCharges(String systemCalculatedCharges) {
		this.systemCalculatedCharges = systemCalculatedCharges;
	}

	public String getTotalFacilitiesCost() {
		return totalFacilitiesCost;
	}

	public void setTotalFacilitiesCost(String totalFacilitiesCost) {
		this.totalFacilitiesCost = totalFacilitiesCost;
	}

	public String getTotalManpowerCost() {
		return totalManpowerCost;
	}
	public void setTotalManpowerCost(String totalManpowerCost) {
		this.totalManpowerCost = totalManpowerCost;
	}
	public TextField getCancellationCostManpower() {
		return cancellationCostManpower;
	}

	public void setCancellationCostManpower(TextField cancellationCostManpower) {
		this.cancellationCostManpower = cancellationCostManpower;
	}

	public Date getRequiredTo() {
		return requiredTo;
	}

	public void setRequiredTo(Date requiredTo) {
		this.requiredTo = requiredTo;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}	
}
