package com.tms.fms.engineering.ui;

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

import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.engineering.model.Sequence;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.TransLogModule;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.reports.model.ReportsFmsModule;

public class RequestDetailsForm extends Form {
	
	protected String requestId;
	protected String oldRequestId;
	EngineeringRequest tempRequest;
	protected EngineeringRequest request=new EngineeringRequest();
	protected ServiceDetailsForm[] serviceForms; 
	protected Label errorLbl;
	protected Button submit;
	protected Button cancel;
	protected Button cancelRequest;
	
	protected Button hodApprove;
	protected Button hodReject;
	
	protected Button assignFC;
	
	protected Button outSource;
	protected Button checkAvailability;
	protected Button fcAccept;
	protected Button fcReject;
	protected Button fcEdit;
	protected Button fcSubmit;
	protected Button lateCompletion;
	protected Button cancellationCharges;
	protected Button viewOutsource;
	
	protected Button copyRequest;
	
	protected Button prepareAssignment;
	protected Button viewAssignment;
	protected Button houReject;
	protected Button modifyRequest;
	
	protected TextBox remarks;
	protected boolean viewMode=true;
	protected boolean fcEditMode=false;
	protected boolean isHod=false;
	protected boolean isFC=false;
	protected boolean isFCHead=false;
	protected boolean isHOU=false;
	protected boolean isOutsourced=false;
	protected boolean isModifyMode=false;
	
	protected String fcEditModeId = null;
	
	// for check availability purpose
	private Collection facilities = new ArrayList();
	private Collection serviceSCP = new ArrayList();
	private Collection servicePOST = new ArrayList();
	private Collection pds = new ArrayList();
	private Map dateSelectedMap = new SequencedHashMap();
	
	protected Double totalInternalRate = 0.0;
	protected Double totalExternalRate = 0.0;
	
	protected String systemCalculatedCharges;
	
	protected String requestTypeLabel = "";
	
	protected String editDetails = "";
	
	protected Collection servicesUnitHead = new ArrayList();
	
	private boolean showEditRequestLink = false;
	
	public RequestDetailsForm() {
	}

	public RequestDetailsForm(String s) {super(s);}

	public void init() {
		removeChildren();
		Application app=Application.getInstance();
		
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.draft"));
		cancelRequest = new Button("cancelRequest", app.getMessage("fms.facility.cancelRequest"));
		copyRequest = new Button("copyRequest", app.getMessage("fms.facility.copyRequest"));
		
		hodApprove= new Button("hodApprove",app.getMessage("fms.facility.approve"));
		hodReject= new Button("hodReject",app.getMessage("fms.facility.reject"));
		
		outSource = new Button("outSource", app.getMessage("fms.facility.outSource"));
		viewOutsource = new Button("viewOutSource", app.getMessage("fms.facility.viewOutSource"));
		checkAvailability = new Button("checkAvailability", app.getMessage("fms.facility.checkAvailability"));
		fcAccept = new Button("fcAccept", app.getMessage("fms.facility.accept"));
		fcReject = new Button("fcReject", app.getMessage("fms.facility.reject"));
		fcEdit = new Button("fcEdit", app.getMessage("fms.facility.editRequest"));
		fcSubmit = new Button("fcSubmit", app.getMessage("fms.facility.submit"));
		lateCompletion = new Button("lateCompletion", app.getMessage("fms.facility.lateCompletion"));
		cancellationCharges = new Button("cancellationCharges", app.getMessage("fms.facility.cancellationCharges"));
		
		assignFC=new Button("assignFC",app.getMessage("fms.facility.proceedRequest"));
		
		prepareAssignment=new Button("prepareAssignment",app.getMessage("fms.facility.prepareAssignment"));
		viewAssignment=new Button("viewAssignment",app.getMessage("fms.facility.viewAssignment"));
		houReject= new Button("houReject",app.getMessage("fms.facility.reject"));
		modifyRequest=new Button("modifyRequest",app.getMessage("fms.facility.modifyRequest"));
		
		remarks=new TextBox("remarks");
		remarks.setSize("40");
		remarks.setRows("5");
		remarks.setHidden(true);
		
		errorLbl = new Label("errorLbl");
		
		//Requestor
		addChild(submit);
		addChild(cancel);
		addChild(cancelRequest);
		addChild(copyRequest);
		addChild(viewOutsource);
		
		//HOD
		addChild(hodApprove);
		addChild(hodReject);
		addChild(remarks);
		
		//FCHead
		addChild(assignFC);
		
		//FC
		addChild(outSource);
		addChild(checkAvailability);
		addChild(fcAccept);
		addChild(fcReject);
		addChild(fcEdit);
		addChild(fcSubmit);
		addChild(lateCompletion);
		addChild(cancellationCharges);
		addChild(modifyRequest);
		
		//HOU
		
		addChild(prepareAssignment);
		addChild(viewAssignment);
		addChild(houReject);
		
		
		addChild(errorLbl);
	}
	
	public void onRequest(Event arg0) {
		SetupModule module = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		
		// consistency check on FC edit mode
		if (fcEditMode && !requestId.equals(fcEditModeId)) {
			Log.getLog(getClass()).warn("Inconsistent FC edit mode requestId=" + requestId + " fcEditModeId=" + fcEditModeId);
			fcEditMode = false;
			fcEditModeId = null;
		}
		
		String userId=Application.getInstance().getCurrentUser().getId();
		isHod=EngineeringModule.isHOD(userId);
		isFCHead=EngineeringModule.isFCHead(userId);
		isFC=FacilitiesCoordinatorModule.isFC(userId);
		isHOU=UnitHeadModule.isUnitApprover(userId);
		init();
		systemCalculatedCharges="";
		populateRequest();
		populateButtons();
		//getState();
		
		// for check availability purpose
		facilities = new ArrayList();
		dateSelectedMap = new SequencedHashMap();
		pds = new ArrayList();
		
		// check rate cards
		errorLbl.setText("");
		if (isFC) {
			boolean validRateCard = module.requestHasValidRateCards(requestId);
			if (!validRateCard) {
				errorLbl.setText("<br>" + Application.getInstance().getMessage("fms.facility.msg.rateCardInvalid"));
			}
		}
	}

	private void populateButtons(){
		
		UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
		UnitHeadModule uModule=(UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
		remarks.setHidden(true);
		submit.setHidden(true);
		cancel.setHidden(true);
		cancelRequest.setHidden(true);
		copyRequest.setHidden(true);		
		assignFC.setHidden(true);
		
		//viewOutsource.setHidden(true);
		outSource.setHidden(true);
		checkAvailability.setHidden(true);
		fcAccept.setHidden(true);
		fcReject.setHidden(true);
		fcEdit.setHidden(true);
		fcSubmit.setHidden(true);
		lateCompletion.setHidden(true);
		cancellationCharges.setHidden(true);
		modifyRequest.setHidden(true);
		
		viewAssignment.setHidden(true);
		prepareAssignment.setHidden(true);
		houReject.setHidden(true);
		
		showEditRequestLink = false;
		
		if(request.isViewMode()){
			if (request.isCurrentUserRequest() && (EngineeringModule.CANCELLED_STATUS.equals(request.getStatus()) || 
					EngineeringModule.REJECTED_STATUS.equals(request.getStatus()))){
				copyRequest.setHidden(false);
			}
			if(request.isCurrentUserRequest() && (!EngineeringModule.APPLIED_CANCELLATION.equals(request.getStatus()) && 
					!EngineeringModule.REJECTED_STATUS.equals(request.getStatus()) &&
					!EngineeringModule.CANCELLED_STATUS.equals(request.getStatus()) &&
					!EngineeringModule.FULFILLED_STATUS.equals(request.getStatus()) &&
					!EngineeringModule.LATE_STATUS.equals(request.getStatus()) &&
					!EngineeringModule.CLOSED_STATUS.equals(request.getStatus()) //&& 
					//!EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus())
				)) {
				// && (EngineeringModule.PENDING_STATUS.equals(request.getStatus()))){
				//checkAvailability.setHidden(false);
				//viewAssignment.setHidden(false);
				cancelRequest.setHidden(false);
			}
			if(isHod && (EngineeringModule.PENDING_STATUS.equals(request.getStatus()))){
				hodApprove.setHidden(false);
				hodReject.setHidden(false);
				remarks.setHidden(false);
			}else{
				hodApprove.setHidden(true);
				hodReject.setHidden(true);
			}
			if(isFCHead && (EngineeringModule.PROCESS_STATUS.equals(request.getStatus()))){
				assignFC.setHidden(false);
			}else{
				assignFC.setHidden(true);
			}
			if(isFC && (EngineeringModule.FC_ASSIGNED_STATUS.equals(request.getStatus()))){
				outSource.setHidden(false);
				checkAvailability.setHidden(false);
				fcAccept.setHidden(false);
				fcReject.setHidden(false);
				if (fcEditMode) {
					fcEdit.setHidden(true);
					fcSubmit.setHidden(false);
					fcAccept.setHidden(true);
					fcReject.setHidden(true);
				} else {
					fcEdit.setHidden(false);
					fcSubmit.setHidden(true);
					showEditRequestLink = true;
				}
			}
			if (isFC && (EngineeringModule.APPLIED_CANCELLATION.equals(request.getStatus()))){
				cancellationCharges.setHidden(false);
			}
			if (isFC && (EngineeringModule.LATE_STATUS.equals(request.getStatus())) && (request.getLateCharges()==null)){
				lateCompletion.setHidden(false);
			}
			if(isHOU && (EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()))){
				
				if(uModule.isAssignmentPrepared(requestId)){
					viewAssignment.setHidden(false);
					prepareAssignment.setHidden(true);
					houReject.setHidden(true);
					checkAvailability.setHidden(false);
					
					if(isFCHead){
						if(fcEditMode){
							submit.setHidden(false);
							cancel.setHidden(false);
							checkAvailability.setHidden(true);
							viewAssignment.setHidden(true);
						}
						else{
							cancelRequest.setHidden(false);						
							boolean newAssignment = false;				
							try{
								newAssignment = uDao.checkNewAssignments(requestId);
							}catch(Exception e){
								Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
							}
							if(newAssignment){
								if(request.getRequiredFrom().after(new Date())) {
									modifyRequest.setHidden(false);				
								}
								if(request.getRequiredTo().after(new Date())) {
									showEditRequestLink = true;
								}
							}
						}
					}
				}else{
					viewAssignment.setHidden(true);
					prepareAssignment.setHidden(false);
					houReject.setHidden(false);
				}
				
				//checkAvailability.setHidden(false);
			}else if(isFCHead && (EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()))){///
				
				if(fcEditMode){
					submit.setHidden(false);
					cancel.setHidden(true);
				}
				else{
					checkAvailability.setHidden(false);
					if(uModule.isAssignmentPrepared(requestId)){
						viewAssignment.setHidden(false);
						prepareAssignment.setHidden(true);
					}
					else{
						viewAssignment.setHidden(true);
						prepareAssignment.setHidden(false);
					}
					
					cancelRequest.setHidden(false);
					
					boolean newAssignment = false;				
					try{
						newAssignment = uDao.checkNewAssignments(requestId);
					}catch(Exception e){
						Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
					}
					if(newAssignment){
						if(request.getRequiredFrom().after(new Date())) {
							modifyRequest.setHidden(false);			
						}
						if(request.getRequiredTo().after(new Date())) {
							showEditRequestLink = true;
						}
					}
				}
			}
			
		}else{
			submit.setHidden(false);
			cancel.setHidden(false);
			cancelRequest.setHidden(true);
			hodApprove.setHidden(true);
			hodReject.setHidden(true);
			
		}
	}
	
	private void populateRequest(){
		setMethod("post");
		EngineeringModule module= (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		request=module.getRequestWithService(requestId);
		
		viewMode=request.isViewMode();
		if(isFC && 
				(EngineeringModule.FC_ASSIGNED_STATUS.equals(request.getStatus()) 
						|| EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()))){
			editDetails = "[" + Application.getInstance().getMessage("fms.facility.editRequest") + "]";
			
			if (fcEditMode){
				viewMode = false;
			}
		}
		if(EngineeringModule.FULFILLED_STATUS.equals(request.getStatus())
				|| EngineeringModule.LATE_STATUS.equals(request.getStatus())){
			FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
			if(fcm.selectTotalSystemCalculatedCharges(requestId)>0)
				systemCalculatedCharges = String.valueOf(fcm.selectTotalSystemCalculatedCharges(requestId));
			else 
				systemCalculatedCharges="";
		}
			
		requestTypeLabel = (String)EngineeringModule.REQUEST_TYPE_MAP.get(request.getRequestType());
		totalInternalRate = 0.0;
		totalExternalRate = 0.0;
		//if (isFC || isFCHead || isHod){
			totalInternalRate = module.getTotalRate(requestId, "I");
			totalExternalRate = module.getTotalRate(requestId, "E");
		//}
		// get service id that belongs to unit head / unit approver
		//Collection servs = uHModule.getUnitServicesApprover(requestId, userId);
		
		Collection services = request.getServices();
		serviceForms=new ServiceDetailsForm[services.size()];
		int i=0;
		for(Iterator itr=services.iterator();itr.hasNext();i++){
			Service service=(Service)itr.next();
			
			if(service != null){
				serviceForms[i]=new ServiceDetailsForm("serviceDetails"+i);
				serviceForms[i].setService(service);
				serviceForms[i].setServiceId(service.getServiceId());
				serviceForms[i].setViewMode(viewMode);
				serviceForms[i].setRequestId(requestId);				


				if(isFCHead && (EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus())) && fcEditMode){
					serviceForms[i].setModifyMode(true);
				}
			}
			addChild(serviceForms[i]);
		}
		
		FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
		
		try {
			int x = 0;
			x = fcm.getOutsourceCount("", "-1", requestId, "clientName", true, 0, -1);
			if (x > 0) {
				viewOutsource.setHidden(false);
				isOutsourced = true;
			} else {
				viewOutsource.setHidden(true);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			return new Forward(Form.CANCEL_FORM_ACTION, "requestListing.jsp", true);
		} else if (buttonName != null && outSource.getAbsoluteName().equals(buttonName)) {
			return new Forward("", "outsourceRequestFC.jsp?requestId="+requestId, true);
		} else if (isFCHead && buttonName != null && viewAssignment.getAbsoluteName().equals(buttonName)) {
			UnitHeadModule module=(UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
			if(!module.isAssignmentPrepared(requestId)){
				module.prepareAssignment(requestId);
			}
			return new Forward("", "requestAssignmentDetails.jsp?requestId="+requestId, true);
		} else if (!isFCHead && buttonName != null && viewAssignment.getAbsoluteName().equals(buttonName)) {
			return new Forward("", "requestAssignmentDetails.jsp?requestId="+requestId, true);
		} 
		else if (buttonName != null && cancelRequest.getAbsoluteName().equals(buttonName)) {
			return new Forward("cancelRequest");
		} else if (buttonName != null && assignFC.getAbsoluteName().equals(buttonName)) {
			return new Forward("assignFC");
		} else if (buttonName != null && (fcReject.getAbsoluteName().equals(buttonName) || houReject.getAbsoluteName().equals(buttonName) )) {
			return new Forward("rejectFC");
		} else if (buttonName != null && lateCompletion.getAbsoluteName().equals(buttonName)) {
			return new Forward("lateFC");
		} else if (buttonName != null && cancellationCharges.getAbsoluteName().equals(buttonName)) {
			return new Forward("cancelFC");
		} else if (buttonName != null && checkAvailability.getAbsoluteName().equals(buttonName)) {
			//return new Forward("checkAvailability");
			return new Forward("", "checkAvailabilityFCfacility.jsp?requestId="+requestId, true);
		} else if (buttonName != null && fcEdit.getAbsoluteName().equals(buttonName)) {
			fcEditMode = true;
			fcEditModeId = requestId;
			
			// change status to "Modified"
			EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
			module.pendingModifyRequest(requestId);
			
			return new Forward("","requestDetails.jsp?requestId="+requestId, true);
		} else if (buttonName != null && fcSubmit.getAbsoluteName().equals(buttonName)) {
			fcEditMode = false;
			fcEditModeId = null;
			
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			EngineeringDao dao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
			
			module.updateTotalAmountRequest(requestId, totalInternalRate, totalExternalRate);
			
			// remove pending modification additionalInfo
			try {
				if (dao.isPendingModification(requestId)) {
					module.updateRequestStatus(requestId, EngineeringModule.MODIFY_STATUS, "", "");
				}
			} catch (DaoException e) {
				Log.getLog(getClass()).error("requestId = " + requestId, e);
			}
			
			// check services required date & availability		
			Collection services=request.getServices();
			if (services!=null && services.size()>0) {
				String checkService = module.checkServicesRequiredDate(requestId, request.getRequiredFrom(), request.getRequiredTo(), services);
				if (EngineeringModule.INVALID_SERVICE_DATE.equals(checkService)) {
					module.pendingModifyRequest(requestId);
					return new Forward("invalidServiceDate");
				} else if (EngineeringModule.INVALID_SERVICE_ITEM.equals(checkService)) {
					module.pendingModifyRequest(requestId);
					return new Forward("invalidServiceItem");
				} 			
			} else {
				return new Forward("serviceEmpty");
			}
			
			return new Forward("","requestDetails.jsp?requestId="+requestId, true);
		} else if (buttonName != null && viewOutsource.getAbsoluteName().equals(buttonName)) {
			return new Forward("viewOutsource");
		}else if (buttonName != null && modifyRequest.getAbsoluteName().equals(buttonName)) {
			//return new Forward("checkAvailability");
			return new Forward("", "requestModify.jsp?requestId="+requestId, true);
		} 
		else {return result;}
		
		
	}
	@Override
	public Forward onValidate(Event arg0) {
		Application application = Application.getInstance();
		FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
		
		String buttonName = findButtonClicked(arg0);
		if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
			EngineeringModule module	= (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			ReportsFmsModule reportmod = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			UnitHeadModule uHead=(UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
			
			// check services required date & availability		
				Collection services=request.getServices();
				if (services!=null && services.size()>0) {
					String checkService = module.checkServicesRequiredDate(requestId, request.getRequiredFrom(), request.getRequiredTo(), services);
					if (EngineeringModule.INVALID_SERVICE_DATE.equals(checkService)) {
						return new Forward("invalidServiceDate");
					} else if (EngineeringModule.INVALID_SERVICE_ITEM.equals(checkService)) {
						return new Forward("invalidServiceItem");
					} else if (EngineeringModule.SERVICE_UNAVAILABLE.equals(checkService)) {
						if(!EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus())){
							// if status is Assignment, remove checking for conflict service
							arg0.getRequest().setAttribute("id", requestId);
							return new Forward("serviceNotAvailable");
						}
					}				
				} else {
					return new Forward("serviceEmpty");
				}
						
			module.updateTotalAmountRequest(requestId, totalInternalRate, totalExternalRate);
			
			// after modified, remove old request and generate new
			if (isModifyMode) {
				if(uHead.isAssignmentPrepared(requestId)){			
					try{
						uHead.prepareModifiedAssignment(requestId);
						module.deleteReport(requestId);
						reportmod.generateReport(requestId);
					}catch(Exception e){
						Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
					}
				} else {
					module.deleteBooking(requestId);
					module.insertBooking(requestId);
					module.deleteReport(requestId);
					reportmod.generateReport(requestId);
				}
			}			
			
			if (EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus())) {
				module.submitModifyRequest(requestId);
			} else {
				module.submitRequest(requestId);
			}
			
			return new Forward("SUBMITTED");
		} else if (buttonName != null && hodApprove.getAbsoluteName().equals(buttonName)) {
			EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			
			module.approveRequest(requestId, getState(), (String)remarks.getValue());
			return new Forward("APPROVED");
		} else if (buttonName != null && hodReject.getAbsoluteName().equals(buttonName)) {
			EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			String remarkValue=(String)remarks.getValue();
				if(remarkValue==null || remarkValue.trim().length()==0){
					remarks.setInvalid(true);
					this.setInvalid(true);
				}else{
					module.rejectRequest(requestId,remarkValue);
					return new Forward("REJECTED");
				}
		} else if (buttonName != null && fcAccept.getAbsoluteName().equals(buttonName)) {
						
			EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			ReportsFmsModule report=(ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			SetupModule setupModule = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			
			EngineeringRequest eRequest= module.getRequestWithService(requestId);
			try {
				int x = 0;
				x = fcm.getOutsourceCount("", "-1", requestId, "clientName", true, 0, -1);
				if (x > 0) {
					isOutsourced = true;
				} else {
					isOutsourced = false;
				}
			} catch (DaoException e) {
				Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
			}
			
			// check status
			String reqStatus = eRequest.getStatus();
			if (reqStatus != null && reqStatus.equals(EngineeringModule.ASSIGNMENT_STATUS)) {
				TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
				transLog.error(requestId, "DUPLICATE_ACCEPT", "status=" + reqStatus);
				return new Forward("DUPLICATE_ACCEPT");
			}
			
			// flag to allow invalid rate cards
			String allowInvalid = application.getProperty(EngineeringModule.ALLOW_ACCEPT_WITH_INVALID_RATE_CARDS);
			boolean allowAccept = false;
			if (allowInvalid != null && allowInvalid.equals("1")) {
				allowAccept = true;
			}
			
			// check for valid rate cards
			if (!setupModule.requestHasValidRateCards(requestId)) {
				if (allowAccept) {
					Log.getLog(getClass()).warn("Accept with invalid rate cards, requestId = " + requestId);
				} else {
					return new Forward("INVALID_RATE_CARD");
				}
			}
			
			// check services required date & availability		
			Collection services=request.getServices();
			if (services!=null && services.size()>0) {
				String checkService = module.checkServicesRequiredDate(requestId, request.getRequiredFrom(), request.getRequiredTo(), services);
				if (EngineeringModule.INVALID_SERVICE_DATE.equals(checkService)) {
					return new Forward("invalidServiceDate");
				} else if (EngineeringModule.INVALID_SERVICE_ITEM.equals(checkService)) {
					return new Forward("invalidServiceItem");
				} 				
			} else {
				return new Forward("serviceEmpty");
			}
			module.updateTotalAmountRequest(requestId, totalInternalRate, totalExternalRate);
			module.acceptRequest(requestId, isOutsourced);			
			
			// create transport request if any
			module.createTransportRequest(eRequest);
			
			//generate report for resource listing
			report.generateReport(requestId);
			
			return new Forward("ACCEPTED");
		} else if (buttonName != null && copyRequest.getAbsoluteName().equals(buttonName)){
			EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			
			EngineeringRequest eRequest= module.getRequestWithService(requestId);
			eRequest.setRequestId(new Sequence(Sequence.TYPE_ENGINEERING).genarteCode());
			
			module.copyRequest(eRequest, requestId);
			requestId = eRequest.getRequestId();
			
			return new Forward("COPY");
		}  else if (buttonName != null && prepareAssignment.getAbsoluteName().equals(buttonName)) {
			UnitHeadModule module=(UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
			if(!module.isAssignmentPrepared(requestId)){
				module.prepareAssignment(requestId);
			}
			return new Forward("ASSIGNMENT_PREPARED");
		}
		
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
		return "fms/engineering/requestDetailsFormTemplate";
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

	public Button getOutSource() {
		return outSource;
	}

	public void setOutSource(Button outSource) {
		this.outSource = outSource;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public Button getCancelRequest() {
		return cancelRequest;
	}

	public void setCancelRequest(Button cancelRequest) {
		this.cancelRequest = cancelRequest;
	}

	public Button getHodApprove() {
		return hodApprove;
	}

	public void setHodApprove(Button hodApprove) {
		this.hodApprove = hodApprove;
	}

	public Button getHodReject() {
		return hodReject;
	}

	public void setHodReject(Button hodReject) {
		this.hodReject = hodReject;
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

	public Button getAssignFC() {
		return assignFC;
	}

	public void setAssignFC(Button assignFC) {
		this.assignFC = assignFC;
	}

	public Button getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(Button cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public Button getCheckAvailability() {
		return checkAvailability;
	}

	public void setCheckAvailability(Button checkAvailability) {
		this.checkAvailability = checkAvailability;
	}

	public Button getFcAccept() {
		return fcAccept;
	}

	public void setFcAccept(Button fcAccept) {
		this.fcAccept = fcAccept;
	}

	public Button getFcReject() {
		return fcReject;
	}

	public void setFcReject(Button fcReject) {
		this.fcReject = fcReject;
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

	public Button getLateCompletion() {
		return lateCompletion;
	}

	public void setLateCompletion(Button lateCompletion) {
		this.lateCompletion = lateCompletion;
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
	
	public String getEditDetails() {
		return editDetails;
	}

	public void setEditDetails(String editDetails) {
		this.editDetails = editDetails;
	}

	public String getRequestTypeLabel() {
		return requestTypeLabel;
	}

	public void setRequestTypeLabel(String requestTypeLabel) {
		this.requestTypeLabel = requestTypeLabel;
	}

	public Button getHouReject() {
		return houReject;
	}

	public void setHouReject(Button houReject) {
		this.houReject = houReject;
	}

	public Button getPrepareAssignment() {
		return prepareAssignment;
	}

	public void setPrepareAssignment(Button prepareAssignment) {
		this.prepareAssignment = prepareAssignment;
	}

	public boolean getIsHOU() {
		return isHOU;
	}

	public void setHOU(boolean isHOU) {
		this.isHOU = isHOU;
	}

	public Button getCopyRequest() {
		return copyRequest;
	}

	public void setCopyRequest(Button copyRequest) {
		this.copyRequest = copyRequest;
	}

	public Button getViewAssignment() {
		return viewAssignment;
	}

	public void setViewAssignment(Button viewAssignment) {
		this.viewAssignment = viewAssignment;
	}

	public Button getFcEdit() {
		return fcEdit;
	}

	public void setFcEdit(Button fcEdit) {
		this.fcEdit = fcEdit;
	}

	public Button getFcSubmit() {
		return fcSubmit;
	}

	public void setFcSubmit(Button fcSubmit) {
		this.fcSubmit = fcSubmit;
	}

	public boolean isFcEditMode() {
		return fcEditMode;
	}

	public void setFcEditMode(boolean fcEditMode) {
		this.fcEditMode = fcEditMode;
		
		if (fcEditMode) {
			Log.getLog(getClass()).info("FC edit mode set: requestId=" + requestId);
			fcEditModeId = requestId;
		}
	}

	public Button getViewOutsource() {
		return viewOutsource;
	}

	public void setViewOutsource(Button viewOutsource) {
		this.viewOutsource = viewOutsource;
	}

	public Button getModifyRequest() {
		return modifyRequest;
	}

	public void setModifyRequest(Button modifyRequest) {
		this.modifyRequest = modifyRequest;
	}

	public String getSystemCalculatedCharges() {
		return systemCalculatedCharges;
	}

	public void setSystemCalculatedCharges(String systemCalculatedCharges) {
		this.systemCalculatedCharges = systemCalculatedCharges;
	}

	public String getOldRequestId() {
		return oldRequestId;
	}

	public void setOldRequestId(String oldRequestId) {
		this.oldRequestId = oldRequestId;
	}
	
	public void setModifyMode(boolean isModifyMode) {
		this.isModifyMode = isModifyMode;
	}
	
	public boolean isShowEditRequestLink() {
		return showEditRequestLink;
	}
}
