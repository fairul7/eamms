package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.stdui.RichTextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.TransLogModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.util.WidgetUtil;

public class RequestForm extends Form {
	protected String requestId;
	protected String oldRequestId;
	protected String lbRequestType;
	protected String type;
	protected TextField title;
	protected TextField clientName;
	protected RichTextBox remarks;
	protected DatePopupField requiredFrom;
	protected DatePopupField requiredTo;
	protected Radio[] requestType=new Radio[EngineeringModule.REQUEST_TYPE_MAP.size()];
	protected Radio[] programType=new Radio[EngineeringModule.PROGRAM_TYPE_MAP.size()];
	protected CheckBox[] services=new CheckBox[EngineeringModule.SERVICES_MAP.size()];
	protected Button cancel;
	protected Button submit;
	protected SingleProgramSelectBox program;
	protected Collection modServices;
	protected Collection oriServices;
	public RequestForm() {}
	
	protected EngineeringRequest request=new EngineeringRequest();
	protected ServiceDetailsForm[] serviceForms; 

	public RequestForm(String s) {super(s);}

	public void init() {
		if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {
			type = "Add";
		}
	}
	
	public String getDefaultTemplate() {
		return "/fms/engineering/requestFormTemplate";
	}

	public void onRequest(Event event) {
		initForm();
		lbRequestType = EngineeringModule.REQUEST_TYPE_INTERNAL;
		if ("Edit".equals(type) || "Modify".equals(type)) {
			populateFields();
		}else if("Add".equals(type) && requestId!=null && !requestId.equals("")){
			setOldRequestId(requestId);
			populateAddTemplateFields();
		}
	}
	
	private void populateAddTemplateFields() {
		try {
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			EngineeringRequest eRequest=module.getRequestWithService(requestId);
			title.setValue(eRequest.getTitle());
			if (eRequest.getProgram()!=null && !eRequest.getProgram().equals("")){
				program.setIds(new String[]{eRequest.getProgram()});
			}
			remarks.setValue(eRequest.getDescription());
			clientName.setValue(eRequest.getClientName());
			requiredFrom.setDate(null);
			requiredTo.setDate(null);
			populateServices(eRequest.getServices());
			WidgetUtil.setRadioValue(requestType,eRequest.getRequestType());
			WidgetUtil.setRadioValue(programType,eRequest.getProgramType());
			lbRequestType = eRequest.getRequestType();
		} catch (Exception e) {
			Log.getLog(getClass()).error("requestId = " + requestId, e);
		}
	}

	public void initForm() {
		setMethod("post");
		Application app = Application.getInstance();
		

		title = new TextField("title");
		title.setSize("50");
		title.setMaxlength("255");
		title.addChild(new ValidatorNotEmpty("nameNotEmpty", ""));
		
		clientName = new TextField("clientName");
		clientName.setSize("50");
		clientName.setMaxlength("255");

		remarks = new RichTextBox("remarks");
		//remarks.setSize("160");
		remarks.setCols("50");

		requiredFrom =new DatePopupField("requiredFrom");
		requiredFrom.setFormat("dd-MM-yyyy");
    	requiredFrom.setDate(new Date());
    	
		requiredTo =new DatePopupField("requiredTo");
		requiredTo.setFormat("dd-MM-yyyy");
    	requiredTo.setDate(new Date());
    	
		int i=0;
		for(Iterator itr=EngineeringModule.REQUEST_TYPE_MAP.keySet().iterator();itr.hasNext();i++){
			String key=(String)itr.next();
			requestType[i]=new Radio("requestType_"+key);
			requestType[i].setText((String)EngineeringModule.REQUEST_TYPE_MAP.get(key));
			requestType[i].setValue(key);
			if(EngineeringModule.REQUEST_TYPE_INTERNAL.equals(key)){
				requestType[i].setChecked(true);
			}
			requestType[i].setOnClick("populateClientName('"+key+"');");
			requestType[i].setGroupName("requestType");
			addChild(requestType[i]);
		}
		
		i=0;
		for(Iterator itr=EngineeringModule.PROGRAM_TYPE_MAP.keySet().iterator();itr.hasNext();i++){
			String key=(String)itr.next();
			programType[i]=new Radio("programType_"+key);
			programType[i].setText((String)EngineeringModule.PROGRAM_TYPE_MAP.get(key));
			programType[i].setValue(key);
			if(EngineeringModule.PROGRAM_TYPE_LIVE.equals(key)){
				programType[i].setChecked(true);
			}
			programType[i].setGroupName("programType");
			addChild(programType[i]);
		}
		
		program = new SingleProgramSelectBox("program");
		program.init();
		
		
		i=0;
		for(Iterator itr=EngineeringModule.SERVICES_MAP.keySet().iterator();itr.hasNext();i++){
			String key=(String)itr.next();
			services[i]=new CheckBox("services_"+key);
			services[i].setText((String)EngineeringModule.SERVICES_MAP.get(key));
			services[i].setValue(key);
			services[i].setGroupName("services");
			addChild(services[i]);
		}
		
		// not allow to checked these 2 at the same time
		services[0].setOnClick("check_cb_valid()"); // SCP/MCP/OB/SNG/VSAT
		services[4].setOnClick("check_cb_valid()"); // Studio
		
		submit = new Button("submit", app.getMessage("fms.request.label.continue"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		
		addChild(title);
		addChild(clientName);
		addChild(program);
		addChild(remarks);
		addChild(requiredFrom);
		addChild(requiredTo);
		addChild(submit);
		addChild(cancel);
		
	}

	public void populateFields() {
		try {
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			EngineeringRequest eRequest=module.getRequestWithService(requestId);
			modServices= new ArrayList();
			oriServices= new ArrayList();
			
			for(Iterator itrServ = eRequest.getServices().iterator();itrServ.hasNext();){
				Service service=(Service)itrServ.next();
				oriServices.add(service);
			}
			title.setValue(eRequest.getTitle());
			if (eRequest.getProgram()!=null && !eRequest.getProgram().equals("")){
				program.setIds(new String[]{eRequest.getProgram()});
			}
			remarks.setValue(eRequest.getDescription());
			requiredFrom.setDate(eRequest.getRequiredFrom());
			requiredTo.setDate(eRequest.getRequiredTo());
			clientName.setValue(eRequest.getClientName());
			populateServices(eRequest.getServices());
			//String[]servc = new String[eRequest.getServices().size()];
			String servcs = "";
			
			for (Iterator iterator = eRequest.getServices().iterator(); iterator.hasNext();) {
				Service service=(Service)iterator.next();
				servcs = servcs+ (String)EngineeringModule.SERVICES_MAP.get(service.getServiceId())+"#";
			}
			if(type.equals("Modify"))
				submit.setOnClick("javascript: return get_check_value('"+servcs+"');");	
			
			WidgetUtil.setRadioValue(requestType,eRequest.getRequestType());
			WidgetUtil.setRadioValue(programType,eRequest.getProgramType());
			lbRequestType = eRequest.getRequestType();
		} catch (Exception e) {
			Log.getLog(getClass()).error("requestId = " + requestId, e);
		}
	}

	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			init();
			return new Forward(Form.CANCEL_FORM_ACTION, "requestListing.jsp", true);
		}
		else {return result;}
	}

	private Collection getSelectedServices(){
		Collection col=WidgetUtil.getCheckBoxValue(services);
		Collection finalCol=new ArrayList();
		for(Iterator itr = col.iterator();itr.hasNext();){
			String serviceId=(String)itr.next();
			Service service=new Service();
			service.setServiceId(serviceId);
			finalCol.add(service);
		}
		return finalCol;
	}
	
	private void populateServices(Collection servicesCol){
		for(Iterator itrServ = servicesCol.iterator();itrServ.hasNext();){
			Service service=(Service)itrServ.next();
			for(int i=0;i<EngineeringModule.SERVICES_MAP.size();i++){
				if(services[i].getValue().equals(service.getServiceId())){
					services[i].setChecked(true);
				}
			}
		}
	}
	
	public Forward onValidate(Event event) {
		
		EngineeringRequest eRequest = new EngineeringRequest();
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		lbRequestType = WidgetUtil.getRadioValue(requestType);
		eRequest.setTitle((String)title.getValue());
		eRequest.setClientName((String)clientName.getValue());
		
		//eRequest.setDescription((String)remarks.getValue()); 
		/*
		 * IE has set <body> in first place.
		 * If has body in db field, will effect report in Excel.
		 * Has to take out <body> to fix report.
		 */
		String remarksDesc = (String)remarks.getValue();
		if(remarksDesc.startsWith("<body>")){
			remarksDesc = remarks.getValue().toString().replaceFirst("<body>", "");
		}else if(remarksDesc.startsWith("<body />")){
			remarksDesc = remarks.getValue().toString().replaceFirst("<body />", "");
		}
		eRequest.setDescription(remarksDesc);
		
		eRequest.setProgram(WidgetUtil.getSbValue(program));
		eRequest.setRequiredFrom(requiredFrom.getDate());
		eRequest.setRequiredTo(requiredTo.getDate());
		eRequest.setRequestType(WidgetUtil.getRadioValue(requestType));
		eRequest.setProgramType(WidgetUtil.getRadioValue(programType));
		eRequest.setStatus(EngineeringModule.DRAFT_STATUS);
		eRequest.setServices(getSelectedServices());
		
		if(type.equals("Modify")){
			boolean exist = false;
			for(Iterator itrServ = oriServices.iterator();itrServ.hasNext();){
				exist=false;
				Service originalService=(Service)itrServ.next();
				for(Iterator itrServ2 = getSelectedServices().iterator();itrServ2.hasNext();){
					Service modifiedService=(Service)itrServ2.next();
					if(originalService.getServiceId().equals(modifiedService.getServiceId())){
						exist=true;
					}
				}
				if(!exist)
					modServices.add(originalService.getServiceId());
			}
		}
		
//		for (var o=0; o < currentTagTokens.length -1; o++){
//			exist=false;
//			for (var p=0; p < c_value.length; p++){
//				if(currentTagTokens[o]==c_value[p]){
//					exist=true;
//				}
//			}
//			if(!exist){
//				modifiedServ = modifiedServ +"- "+currentTagTokens[o]+"\n";
//				//alert(currentTagTokens[o]);//exist=true;
//				//arrModifiedServ[counter]=currentTagTokens[o];
//			}
//				
//		}
		boolean valid=true;
		Forward fwd=new Forward("");
		if(eRequest.getServices()==null || !(eRequest.getServices().size()>0)){
			valid=false;
			fwd=new Forward("NoServices");
		}
		
		if(requiredFrom.getDate().after(requiredTo.getDate())){
			requiredTo.setInvalid(true);
			valid=false;
		}
		
		if((eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_INTERNAL)) && 
				(eRequest.getProgram()==null||eRequest.getProgram().trim().length()==0)){
			program.setInvalid(true);
			valid=false;
		}
		
		if(valid==false){
			this.setInvalid(true);
			return fwd;
		}
		
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
		requiredFrom.getDate().setSeconds(1);
		requiredTo.getDate().setSeconds(1);
		
		if (requiredFrom.getDate().before(now) && !requiredFrom.getDate().equals(now)){
			requiredFrom.setInvalid(true);
			return new Forward("INVALID-DATE-FROM");
		}
		
		if (requiredTo.getDate().before(now) && !requiredTo.getDate().equals(now)){
 			requiredTo.setInvalid(true);
			return new Forward("INVALID-DATE-TO");
		}	
		
		requiredFrom.getDate().setSeconds(0);
		requiredTo.getDate().setSeconds(0);
		
		if ((eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_INTERNAL)) || 
				(eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM))){
			eRequest.setClientName("");
		} 
		
		if ((eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)) || 
				(eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM))){
			eRequest.setProgram("");
		}
		
		if ("Add".equals(type)) {
			try {
				module.insertRequest(eRequest);
				if(getOldRequestId()!=null && !getOldRequestId().equals("")){
					copyRequest(eRequest.getRequestId());
					return new Forward("ADDED","requestDetails.jsp?requestId="+eRequest.getRequestId(),true);
				}else{
					return new Forward("ADDED","requestDetails.jsp?requestId="+eRequest.getRequestId(),true);
				}
			}catch (Exception e) {
				Log.getLog(getClass()).error("requestId = " + requestId, e);
				return new Forward("FAILED");
			} 
		}
		if ("Edit".equals(type)) {
			try {
				eRequest.setRequestId(requestId);
				module.updateRequest(eRequest);
				return new Forward("ADDED","requestDetails.jsp?requestId="+eRequest.getRequestId(),true);
			}catch (Exception e) {
				Log.getLog(getClass()).error("requestId = " + requestId, e);
				return new Forward("FAILED");
			} 
		}
		if ("Modify".equals(type)) {
			try {
				EngineeringDao eDao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
				TransportModule transport = (TransportModule) Application.getInstance().getModule(TransportModule.class);
				
				// delete for OB Van
				transport.deleteOBVanRecords(requestId);
				
				if (modServices != null && modServices.size() > 0) {
					// get assignments that are affected
					Collection assignmentIds = module.selectAssignmentByRequestIdAndServiceType(requestId, modServices);
					
					for (Iterator iterator = assignmentIds.iterator(); iterator.hasNext();) {
						EngineeringRequest ass = (EngineeringRequest) iterator.next(); 
						EngineeringRequest assignment = module.selectParticularAssignment(ass.getAssignmentId(), ass.getAssignmentType()); 
						String serviceId = eDao.selectServiceId(ass.getAssignmentId());
						
						if (!ass.getServiceType().equals("4") && !ass.getServiceType().equals("6")) {
							// delete assignment
							eDao.deleteAssignmentByAssignmentId(ass.getAssignmentId());
							
							String assRateCardId = eDao.getRateCardByAssignmentId(ass.getAssignmentId());
							if (!eDao.isServiceExist(serviceId)) {
								if (!eDao.isServiceTypeExist(requestId, ass.getServiceType())) {
									// logging
									TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
									transLog.info(eRequest.getRequestId(), "REQUEST_SERVICE_CANCEL", "serviceId=" + ass.getServiceType());
									
									eDao.deleteServiceFromRequestServices(requestId, ass.getServiceType());
								}
								  
							  	String statlabel = "";
							  	if (ass.getStatusLabel() != null) {
							  		statlabel = ass.getStatusLabel();
							  	} else {
							  		statlabel = ass.getStatus();
							  	}
							  	
								if (statlabel.equals("N")) {
									eDao.deleteManpowerAssignment(ass.getAssignmentId());
								}
						  	}
						 	
							if (!eDao.isRateCardExistForRequest(requestId, assRateCardId)) {
								eDao.deleteRateCardByReqIdFromRequestUnit(requestId, assRateCardId); 
							}
						} else {
							if (assignment != null) {
								eDao.deleteAssignmentByAssignmentId(ass.getAssignmentId());
								
								String assRateCardId = eDao.getRateCardByAssignmentId(ass.getAssignmentId());
								if (!eDao.isServiceExist(serviceId)) {
									if(!eDao.isServiceTypeExist(requestId, ass.getServiceType())) {
										eDao.deleteServiceFromRequestServices(requestId, ass.getServiceType());
									}
								}
								if(!eDao.isRateCardExistForRequest(requestId, assRateCardId)) {
									eDao.deleteRateCardByReqIdFromRequestUnit(requestId, assRateCardId); 
								}
								
								eDao.deleteManpowerAssignment(ass.getAssignmentId());
							}
						}
					}
					
					// delete individual service type for the request
					for (Iterator iterator = modServices.iterator(); iterator.hasNext();) {
						String serviceType = (String) iterator.next();
						eDao.deleteRequestFromServicesTypeTable(requestId, serviceType);
					}
				}
				eRequest.setRequestId(requestId);
				module.updateRequest(eRequest);
				
				// update status trail
				module.pendingModifyRequest(requestId);
				
				return new Forward("ADDED","requestServiceModify.jsp?requestId="+requestId,true);
			}catch (Exception e) {
				Log.getLog(getClass()).error("requestId = " + requestId, e);
				return new Forward("FAILED");
			} 
		}
		
		return new Forward("ADDED");
	}
	
	private void copyRequest(String requestId) {
		Application app = Application.getInstance();
		
		EngineeringModule module = (EngineeringModule) app.getModule(EngineeringModule.class);
		
		EngineeringRequest tempRequest = null;
		if(oldRequestId!= null && !oldRequestId.equals("")){
			tempRequest = module.getRequestWithService(oldRequestId);
		}
		
		request=module.getRequestWithService(requestId);
		request.setCreatedUserName(request.getCreatedUserName());
		request.setApprovedBy(request.getApproverUserName());
		
		Collection services = request.getServices();
		serviceForms=new ServiceDetailsForm[services.size()];
		
		for (Iterator itr=services.iterator();itr.hasNext();) {
			Service service=(Service)itr.next();
			if(tempRequest!=null){
				Collection tempServices = tempRequest.getServices();
				for(Iterator iterate = tempServices.iterator(); iterate.hasNext(); ){
					Service tempService = (Service) iterate.next();
					if(service.getServiceId().equals(tempService.getServiceId())){
						module.copyServicesForTemplate(request, oldRequestId, service.getServiceId());
					}
				}
			}
		}
		
	}

	/**
	 * @return the cancel
	 */
	public Button getCancel() {
		return cancel;
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	/**
	 * @return the clientName
	 */
	public TextField getClientName() {
		return clientName;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(TextField clientName) {
		this.clientName = clientName;
	}

	/**
	 * @return the programType
	 */
	public Radio[] getProgramType() {
		return programType;
	}

	/**
	 * @param programType the programType to set
	 */
	public void setProgramType(Radio[] programType) {
		this.programType = programType;
	}

	public RichTextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(RichTextBox remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the requestType
	 */
	public Radio[] getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(Radio[] requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the requiredFrom
	 */
	public DatePopupField getRequiredFrom() {
		return requiredFrom;
	}

	/**
	 * @param requiredFrom the requiredFrom to set
	 */
	public void setRequiredFrom(DatePopupField requiredFrom) {
		this.requiredFrom = requiredFrom;
	}

	/**
	 * @return the requiredTo
	 */
	public DatePopupField getRequiredTo() {
		return requiredTo;
	}

	/**
	 * @param requiredTo the requiredTo to set
	 */
	public void setRequiredTo(DatePopupField requiredTo) {
		this.requiredTo = requiredTo;
	}

	/**
	 * @return the services
	 */
	public CheckBox[] getServices() {
		return services;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(CheckBox[] services) {
		this.services = services;
	}

	/**
	 * @return the submit
	 */
	public Button getSubmit() {
		return submit;
	}

	/**
	 * @param submit the submit to set
	 */
	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	/**
	 * @return the title
	 */
	public TextField getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(TextField title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the program
	 */
	public SingleProgramSelectBox getProgram() {
		return program;
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(SingleProgramSelectBox program) {
		this.program = program;
	}

	public String getLbRequestType() {
		return lbRequestType;
	}

	public void setLbRequestType(String lbRequestType) {
		this.lbRequestType = lbRequestType;
	}

	public String getOldRequestId() {
		return oldRequestId;
	}

	public void setOldRequestId(String oldRequestId) {
		this.oldRequestId = oldRequestId;
	}
}
