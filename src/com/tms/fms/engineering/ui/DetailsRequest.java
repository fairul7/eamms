package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.engineering.model.CheckAvailabilityModule;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.engineering.model.Sequence;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;

public class DetailsRequest extends Form {
	
	public static final String TRANSPORT_STATUS = "tran";
	public static final String ENGINEERING_STATUS = "eng";
	
	
	protected String requestId;
	protected EngineeringRequest request=new EngineeringRequest();
	protected ServiceDetailsForm[] serviceForms; 
	protected Button submit;
	protected Button cancel;
	private String mode;
		
	protected TextBox remarks;
	protected boolean viewMode=true;
	protected boolean fcEditMode=false;
	protected boolean isHod=false;
	protected boolean isFC=false;
	protected boolean isFCHead=false;
	protected boolean isHOU=false;
	protected boolean isOutsourced=false;
	
	
	private Collection facilities = new ArrayList();
	private Collection serviceSCP = new ArrayList();
	private Collection servicePOST = new ArrayList();
	private Collection pds = new ArrayList();
	private Map dateSelectedMap = new SequencedHashMap();
	
	protected Double totalInternalRate = 0.0;
	protected Double totalExternalRate = 0.0;
	
	protected String requestTypeLabel = "";
	
	protected Collection servicesUnitHead = new ArrayList();
	private TransportRequest transportRequest;
	
	public DetailsRequest() {
	}

	public DetailsRequest(String s) {super(s);}

	
	//For Transport
		Collection tranRequest = new ArrayList();
		private Label requestIdL;
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
		private Label rateCard;
		
	public void init() {
		Application app=Application.getInstance();
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.draft"));
				
		remarks=new TextBox("remarks");
		remarks.setSize("40");
		remarks.setRows("5");
		remarks.setHidden(true);
		
		requestIdL = new Label("requestIdL");
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
		rateCard = new Label("rateCard");		
		addChild(requestIdL);
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
		addChild(rateCard);
		
		addChild(submit);
		addChild(cancel);	
		addChild(remarks);		
	
		
	}
	
	public void onRequest(Event arg0) {
		String userId=Application.getInstance().getCurrentUser().getId();
		isHod=EngineeringModule.isHOD(userId);
		isFCHead=EngineeringModule.isFCHead(userId);
		isFC=FacilitiesCoordinatorModule.isFC(userId);
		isHOU=UnitHeadModule.isUnitApprover(userId);
		init();
		
		if(ENGINEERING_STATUS.equals(mode)){
			populateEngineering();
		}else
			populateTransport();
		
		// for check availability purpose
		facilities = new ArrayList();
		dateSelectedMap = new SequencedHashMap();
		pds = new ArrayList();
	}

	
	
	private void populateEngineering(){
		
		setMethod("post");
		EngineeringModule module= (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		UnitHeadModule uHModule = (UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
		String userId=Application.getInstance().getCurrentUser().getId();
		
		request=module.getRequestWithService(requestId);
		request.setCreatedUserName(request.getCreatedUserName());
		request.setApprovedBy(request.getApproverUserName());
		
		viewMode=request.isViewMode();
		if(isFC && (EngineeringModule.FC_ASSIGNED_STATUS.equals(request.getStatus()))){
			if (fcEditMode){
				viewMode = false;
			}
		}
		requestTypeLabel = (String)EngineeringModule.REQUEST_TYPE_MAP.get(request.getRequestType());
		totalInternalRate = 0.0;
		totalExternalRate = 0.0;
	
		totalInternalRate = module.getTotalRate(requestId, "I");
		totalExternalRate = module.getTotalRate(requestId, "E");
		
		
		Collection services=request.getServices();
		serviceForms=new ServiceDetailsForm[services.size()];
		int i=0;
		for(Iterator itr=services.iterator();itr.hasNext();i++){
			Service service=(Service)itr.next();
				serviceForms[i]=new ServiceDetailsForm("serviceDetails"+i);
				serviceForms[i].setService(service);
				serviceForms[i].setServiceId(service.getServiceId());
				serviceForms[i].setViewMode(true);
				serviceForms[i].setRequestId(requestId);				
				serviceForms[i].setRemoveLink(true);
						
				addChild(serviceForms[i]);
			
		}
		
		FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
		
		try {
			int x = 0;
			x = fcm.getOutsourceCount("", "-1", requestId, "clientName", true, 0, -1);
			if (x > 0) {
				
				isOutsourced = true;
			} 
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	public void populateTransport() {

		TransportModule TM = (TransportModule) Application.getInstance()
				.getModule(TransportModule.class);
		transportRequest = new TransportRequest();
		SetupModule SM = (SetupModule) Application.getInstance().getModule(
				SetupModule.class);
		tranRequest = null;

		try {

			transportRequest = TM.selectTransportRequest(requestId);
			if (transportRequest != null) {
				requestIdL.setText(requestId);
				titleL.setText(transportRequest.getRequestTitle());
				typeL.setText(transportRequest.getRequestType());

				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					startDateL.setText(sdf.format(transportRequest.getStartDate()));
					endDateL.setText(sdf.format(transportRequest.getEndDate()));

					SimpleDateFormat stf = new SimpleDateFormat("k:mm");
					startTimeL.setText(stf.format(transportRequest.getStartDate()));
					endTimeL.setText(stf.format(transportRequest.getEndDate()));

				} catch (Exception er) {
					Log.getLog(getClass()).error(
							"SimpleDateFormat error converting:" + er);
				}

				destinationL.setText(transportRequest.getDestination());
				purposeL.setText(transportRequest.getPurpose());
				remarksL.setText(transportRequest.getRemarks());

				statusL.setText(TM.selectStatus(transportRequest.getStatus()));

				programL.setText(SM.selectProgName(transportRequest.getProgram()));

				tranRequest = TM.getVehicles(requestId);
				String rate = transportRequest.getRate();
				if(null == rate || "".equals(rate))
					rate = "0";				
				rateCard.setText("RM " + rate);
			}

		} catch (Exception er) {
			Log.getLog(getClass()).error("We got the prob:" + er);
		}

		
	}
	
	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			return new Forward(Form.CANCEL_FORM_ACTION, "requestListing.jsp", true);
		
		} else {return result;}
	}
	@Override
	public Forward onValidate(Event arg0) {
				
		return new Forward("");
	}
	
	private String getState(){
		try{
			Date from=request.getRequiredFrom();
			Date current=new Date();
			//from=DateUtil.dateAdd(current, Calendar.DATE, 7);
			long diff=dateDiff(from,current);
			if(diff<24){
				return EngineeringModule.STATE_ADHOC;
			}else if( diff <168){
				return EngineeringModule.STATE_LATE;
			}else {
				return EngineeringModule.STATE_NORMAL;
			}
		}catch(Exception e){}
		return EngineeringModule.STATE_NORMAL;
	}
	
	public long dateDiff(Date start, Date end){
		long diff = Math.round((start.getTime() - end.getTime()) / ( 60 * 60 * 1000));
		return diff;
	}
	
	
	public String getDefaultTemplate() {
		if(ENGINEERING_STATUS.equals(mode)){
			return "fms/engineering/detailsRequest";
		}else
			return "fms/transport/detailsRequest";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public EngineeringRequest getRequest() {
		return request;
	}

	public void setRequest(EngineeringRequest request) {
		this.request = request;
	}

	public ServiceDetailsForm[] getServiceForms() {
		return serviceForms;
	}

	public void setServiceForms(ServiceDetailsForm[] serviceForms) {
		this.serviceForms = serviceForms;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}


	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean getIsHod() {
		return isHod;
	}

	public TextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(TextBox remarks) {
		this.remarks = remarks;
	}

	

	public boolean getIsFC() {
		return isFC;
	}

	public void setFC(boolean isFC) {
		this.isFC = isFC;
	}

	public boolean getIsFCHead() {
		return isFCHead;
	}

	public void setFCHead(boolean isFCHead) {
		this.isFCHead = isFCHead;
	}

	

	public void setHod(boolean isHod) {
		this.isHod = isHod;
	}

	
	// for check availability purpose
	public Collection getFacilities() {
		return facilities;
	}

	public void setFacilities(Collection facilities) {
		this.facilities = facilities;
	}

	public boolean isOutsourced() {
		return isOutsourced;
	}

	public void setOutsourced(boolean isOutsourced) {
		this.isOutsourced = isOutsourced;
	}

	public Collection getServicesUnitHead() {
		return servicesUnitHead;
	}

	public void setServicesUnitHead(Collection servicesUnitHead) {
		this.servicesUnitHead = servicesUnitHead;
	}
	
	public Label getRequestIdL() {
		return requestIdL;
	}

	public void setRequestIdL(Label requestIdL) {
		this.requestIdL = requestIdL;
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

	public Label getProgramL() {
		return programL;
	}

	public void setProgramL(Label programL) {
		this.programL = programL;
	}

	public Label getRateCard() {
		return rateCard;
	}

	public void setRateCard(Label rateCard) {
		this.rateCard = rateCard;
	}

	public Collection getPds() {
		return pds;
	}

	public void setPds(Collection pds) {
		this.pds = pds;
	}

	public Map getDateSelectedMap() {
		return dateSelectedMap;
	}

	public void setDateSelectedMap(Map dateSelectedMap) {
		this.dateSelectedMap = dateSelectedMap;
	}

	public Collection getServiceSCP() {
		return serviceSCP;
	}

	public void setServiceSCP(Collection serviceSCP) {
		this.serviceSCP = serviceSCP;
	}

	public Collection getServicePOST() {
		return servicePOST;
	}

	public void setServicePOST(Collection servicePOST) {
		this.servicePOST = servicePOST;
	}

	public Double getTotalInternalRate() {
		return totalInternalRate;
	}

	public void setTotalInternalRate(Double totalInternalRate) {
		this.totalInternalRate = totalInternalRate;
	}

	public Double getTotalExternalRate() {
		return totalExternalRate;
	}

	public void setTotalExternalRate(Double totalExternalRate) {
		this.totalExternalRate = totalExternalRate;
	}

	public String getRequestTypeLabel() {
		return requestTypeLabel;
	}

	public void setRequestTypeLabel(String requestTypeLabel) {
		this.requestTypeLabel = requestTypeLabel;
	}

	public boolean getIsHOU() {
		return isHOU;
	}

	public void setHOU(boolean isHOU) {
		this.isHOU = isHOU;
	}

	
	public boolean isFcEditMode() {
		return fcEditMode;
	}

	public Collection getTranRequest() {
		return tranRequest;
	}

	public void setTranRequest(Collection tranRequest) {
		this.tranRequest = tranRequest;
	}

	public void setFcEditMode(boolean fcEditMode) {
		this.fcEditMode = fcEditMode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public TransportRequest getTransportRequest() {
		return transportRequest;
	}

	public void setTransportRequest(TransportRequest transportRequest) {
		this.transportRequest = transportRequest;
	}

	
		
}
