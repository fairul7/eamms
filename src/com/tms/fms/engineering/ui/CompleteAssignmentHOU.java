package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.TimeField;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.PostProductionService;
import com.tms.fms.engineering.model.StudioService;
import com.tms.fms.engineering.model.TvroService;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.engineering.model.VtrService;

public class CompleteAssignmentHOU extends Form {
	
	private Label requestTitle;
	private Label requestor;
	private Label requestType;
	private Label groupAssignmentId;
	private Label requiredDate;
	private Label requiredTime;
	private Label remarks;
	private Label facility;
	private Label lbCompletionDate;
	private Label lbCompletionTime;
	private Label lbReasonUnfulfilled;
	
	protected DatePopupField completionDate;
	protected TimeField completionTime;
	
	private String groupId;
	private String requestId;
	private String closed;
	private String serviceType;
	private Date requiredFrom;
	private Date requiredTo;
	private TextBox remarksUnfulfilled;
	protected EngineeringRequest request=new EngineeringRequest();
	protected Collection childItems=new ArrayList();
	
	protected Button cancel;
	protected Button submit;
	protected Button complete;
	protected Button unfulfilled;
	protected Panel buttonPanel;
	
	private Collection files = new ArrayList();
	
	//VTR
	private Label conversion;
	private Label duration;
	private Label copies;
	private Label attachment;
	private Label description;
	
	//STUDIO
	private Label segment;
	private Label settingTime;
	private Label rehearsalTime;
	private Label vtrTime;
	
	//TVRO
	private Label tvroFeedTitle;
	private Label tvroLocation;
	private Label tvroRequiredDate;
	private Label tvroRequiredTime;
	private Label tvroTimeZone;
	private Label tvroTotalTime;
	private Label tvroRemarks;
	
	public void init() {
	}
	
	public void onRequest(Event event) {
		initForm();
		
		if (groupId != null && !"".equals("groupId")){
			populateFields();
		}
	}

	public String getDefaultTemplate() {
		return "/fms/engineering/completeAssignmentHOUtemp";
	}
	
	public void initForm() {
		setMethod("post");
		Application app = Application.getInstance();
		
		requestTitle = new Label("requestTitle");
		addChild(requestTitle);
		
		requestor = new Label("requestor");
		addChild(requestor);
		
		requestType = new Label("requestType");
		addChild(requestType);
		
		groupAssignmentId = new Label("groupAssignmentId");
		addChild(groupAssignmentId);
		
		requiredDate = new Label("requiredDate");
		addChild(requiredDate);
		
		requiredTime = new Label("requiredTime");
		addChild(requiredTime);
		
		facility = new Label("facility");
		addChild(facility);
		
		completionDate = new DatePopupField("completionDate");
		completionDate.setFormat("dd-MM-yyyy");
		completionDate.setDate(new Date());
		addChild(completionDate);
		
		completionTime = new TimeField("completionTime");
		addChild(completionTime);
		
		lbCompletionDate = new Label("lbCompletionDate");
		lbCompletionDate.setHidden(true);
		addChild(lbCompletionDate);
		
		remarksUnfulfilled = new TextBox("remarksUnfulfilled");
		remarksUnfulfilled.setRows("5");
		addChild(remarksUnfulfilled);
		
		lbReasonUnfulfilled = new Label("lbReasonUnfulfilled");
		addChild(lbReasonUnfulfilled);
				
		//VTR
		conversion = new Label("conversion");
		addChild(conversion);
		
		duration = new Label("duration");
		addChild(duration);
		
		copies = new Label("copies");
		addChild(copies);
		
		attachment = new Label("attachment");
		addChild(attachment);
		
		description = new Label("description");
		addChild(description);
		
		//STUDIO
		segment = new Label("segment");
		addChild(segment);
		
		settingTime = new Label("settingTime");
		addChild(settingTime);
		
		rehearsalTime = new Label("rehearsalTime");
		addChild(rehearsalTime);
		
		vtrTime = new Label("vtrTime");
		addChild(vtrTime);
		
		//TVRO
		tvroFeedTitle = new Label("tvroFeedTitle");
		addChild(tvroFeedTitle);
		
		tvroLocation = new Label("tvroLocation");
		addChild(tvroLocation);
		
		tvroRequiredDate = new Label("tvroRequiredDate");
		addChild(tvroRequiredDate);
		
		tvroRequiredTime = new Label("tvroRequiredTime");
		addChild(tvroRequiredTime);
		
		tvroTimeZone = new Label("tvroTimeZone");
		addChild(tvroTimeZone);
		
		tvroTotalTime = new Label("tvroTotalTime");
		addChild(tvroTotalTime);
		
		tvroRemarks = new Label("tvroRemarks");
		addChild(tvroRemarks);
		
		populateButtons();
		
	}
	
	public void populateButtons() {
		Application app = Application.getInstance();
		complete = new Button("complete", app.getMessage("fms.facility.completeAssignment"));
		complete.setOnClick("populateClientName('complete');");
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		unfulfilled = new Button("unfulfilled", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		cancel.setOnClick("window.close();");
		addChild(complete);
		addChild(submit);
		addChild(unfulfilled);
		addChild(cancel);
	}
	
	private void populateFields(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat sdfAll = new SimpleDateFormat("dd MMM yyyy HH:mm");
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		
		UnitHeadDao uDao = (UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
		
		if (groupId != null && !"".equals(groupId)){
			//childItems = uDao.getChildFacilityAssignments(groupIds);
			childItems = uDao.getChildFacilityAssignmentsByGroupIdMap(groupId);
		}		
		
		if (childItems!=null && childItems.size() >0){
			int count=0;
			//for (Iterator i = childItems.iterator();i.hasNext();){
				Assignment asg = (Assignment) childItems.iterator().next();
								
				if (asg.getRequestId() != null) {
					requestId = asg.getRequestId();
				}				
				
				if (asg.getServiceType() != null) {
					requestType.setText((String) EngineeringModule.SERVICES_MAP.get(asg.getServiceType()));
				}
				
				if (asg.getRequiredFrom() != null && asg.getRequiredTo() != null){
					requiredDate.setText(sdf.format(asg.getRequiredFrom()) + " - " + sdf.format(asg.getRequiredTo()));
					// set date required - for checking
					requiredFrom = asg.getRequiredFrom();
					requiredTo = asg.getRequiredTo();
				}
				
				if (asg.getFromTime() != null && asg.getToTime() != null){
					requiredTime.setText(asg.getFromTime() + " - " + asg.getToTime());
					
					requiredFrom.setHours(Integer.parseInt(asg.getFromTime().substring(0, 2)));
					requiredFrom.setMinutes(Integer.parseInt(asg.getFromTime().substring(3)));
					
					requiredTo.setHours(Integer.parseInt(asg.getToTime().substring(0, 2)));
					requiredTo.setMinutes(Integer.parseInt(asg.getToTime().substring(3)));
				}
				
				if (asg.getCheckedInBy() != null && asg.getCheckedInDate() != null && 
						EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKIN.equals(asg.getStatus())){
					completionDate.setHidden(true);
					completionTime.setHidden(true);
					complete.setHidden(true);
					lbCompletionDate.setText(sdfAll.format(asg.getCheckedInDate()));
					lbCompletionDate.setHidden(false);
					closed = "Y";
					
				} else if (EngineeringModule.ASSIGNMENT_FACILITY_STATUS_UNFULFILLED.equals(asg.getStatus())) {
					lbReasonUnfulfilled.setText(asg.getReasonUnfulfilled());					
					closed = "U";
				} else {
					closed = "N";
				}
				
				facility.setText(module.getRequestedItems(groupId));
				
				if (ServiceDetailsForm.SERVICE_VTR.equals(asg.getServiceType())){
					serviceType = "vtr";
					VtrService s=module.getVtrService(asg.getServiceId());
					
					files = module.getFiles(asg.getServiceId());
					conversion.setText(s.getConversionFromLabel() + " - " + s.getConversionToLabel());
					duration.setText(s.getDuration());
					copies.setText(Integer.toString(s.getNoOfCopies()));
					description.setText(s.getRemarks());
					description.setEscapeXml(false);
					
					String content="<table class='borderTable' width='80%'>";
					
					//Iterate files
					if (files.size() > 0) {
						Iterator itr = files.iterator();
						while (itr.hasNext()){
							VtrService file = (VtrService)itr.next();
							content+="<tr><td><a onClick=\"javascript:window.open('/storage" + file.getFilePath() + "', 'openAttachment', 'height=350,width=250,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');return false;\">" + file.getFileName() +"</a></td></tr>";
						}
					}
					
					content+="</table><br>";
					attachment.setText(content);
					attachment.setEscapeXml(false);
					
				} else if (ServiceDetailsForm.SERVICE_STUDIO.equals(asg.getServiceType())){
					serviceType = "studio";
					StudioService s=module.getStudioService(asg.getServiceId());

					segment.setText(s.getSegment());
					
					String strSettingTime = s.getSettingFrom() + " - " + s.getSettingTo();
					settingTime.setText(strSettingTime);
					
					String strRehearsalTime = s.getRehearsalFrom() + " - " + s.getRehearsalTo();
					rehearsalTime.setText(strRehearsalTime);
					
					String strVtrTime = s.getVtrFrom() + " - " + s.getVtrTo();
					vtrTime.setText(strVtrTime);
					
				} else if (ServiceDetailsForm.SERVICE_TVRO.equals(asg.getServiceType())){
					serviceType = "tvro";
					TvroService s=module.getTvroService(asg.getServiceId());
					
					tvroFeedTitle.setText(s.getFeedTitle());
					tvroLocation.setText(s.getLocation());
					//tvroRequiredDate.setText(sdf.format(s.getRequiredDate()) + " - " + sdf.format(s.getRequiredDateTo()));
					
					String strRequiredTime = s.getFromTime() + " - " + s.getToTime();
					
					//tvroRequiredTime.setText(strRequiredTime);
					tvroTimeZone.setText((String) module.TIMEZONES.get(s.getTimezone()));
					tvroTotalTime.setText(s.getTotalTimeReq()+" "+s.getTimeMeasureLabel() );
					tvroRemarks.setText(s.getRemarks());
					
				} else if (ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(asg.getServiceType())){
					PostProductionService pps=module.getPostProductionService(asg.getServiceId());
				}
			//}
		}
		
		request=module.getRequestWithService(requestId);		
		requestTitle.setText(request.getTitle());
		requestor.setText(request.getCreatedUserName());
		groupAssignmentId.setText(groupId);
		
		
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
	    	return new Forward("CANCEL");
	    } else if (buttonName != null && unfulfilled.getAbsoluteName().equals(buttonName)) {
	    	return result;
	    } else if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
	    	return result;
	    }
	    return result;
	}
	
	public Forward onValidate(Event event) {
		Application app = Application.getInstance();
		EngineeringModule module = (EngineeringModule)app.getModule(EngineeringModule.class);
		EngineeringRequest er = new EngineeringRequest();
		String buttonName = findButtonClicked(event);
		
		if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
			Date today = new Date();
			Date cDate = completionDate.getDate();
			cDate.setHours(completionTime.getHour());
			cDate.setMinutes(completionTime.getMinute());
			
			if (!module.isValidCompletion(requiredFrom, today, cDate)){
				closed = "C";
				event.getRequest().setAttribute("groupId", groupId);
				return new Forward("INVALID-COMPLETION-DATE");
			}
			
			er.setGroupId(groupId);
			er.setCheckedInDate(cDate);
			module.completeEquipmentAssignment(er);
			//module.updateRequestStatus(requestId);
			
			return new Forward("COMPLETE");
			
		} else if (buttonName != null && unfulfilled.getAbsoluteName().equals(buttonName)) {
			
			er.setReasonUnfulfilled((String)remarksUnfulfilled.getValue());
			
			if (er.getReasonUnfulfilled() == null || "".equals(er.getReasonUnfulfilled())){
				closed = "F";
				remarksUnfulfilled.setInvalid(true);
				event.getRequest().setAttribute("groupId", groupId);
				return new Forward("INVALID-REMARKS");
			}
			
			er.setGroupId(groupId);
			module.unfulfilledEquipmentAssignment(er);
			module.updateRequestStatus(requestId);
			
			return new Forward("UNFULFILLED");
			
		} else if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			return new Forward("CANCEL");
			
		} else {
			return new Forward("FAILED");
		}
	}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Label getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(Label requestTitle) {
		this.requestTitle = requestTitle;
	}

	public Label getRequestor() {
		return requestor;
	}

	public void setRequestor(Label requestor) {
		this.requestor = requestor;
	}

	public Label getRequestType() {
		return requestType;
	}

	public void setRequestType(Label requestType) {
		this.requestType = requestType;
	}

	public Label getGroupAssignmentId() {
		return groupAssignmentId;
	}

	public void setGroupAssignmentId(Label groupAssignmentId) {
		this.groupAssignmentId = groupAssignmentId;
	}

	public Label getRequiredDate() {
		return requiredDate;
	}

	public void setRequiredDate(Label requiredDate) {
		this.requiredDate = requiredDate;
	}

	public Label getRequiredTime() {
		return requiredTime;
	}

	public void setRequiredTime(Label requiredTime) {
		this.requiredTime = requiredTime;
	}

	public Label getRemarks() {
		return remarks;
	}

	public void setRemarks(Label remarks) {
		this.remarks = remarks;
	}

	public Label getFacility() {
		return facility;
	}

	public void setFacility(Label facility) {
		this.facility = facility;
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

	public Button getComplete() {
		return complete;
	}

	public void setComplete(Button complete) {
		this.complete = complete;
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

	public Label getLbCompletionDate() {
		return lbCompletionDate;
	}

	public void setLbCompletionDate(Label lbCompletionDate) {
		this.lbCompletionDate = lbCompletionDate;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public Label getLbCompletionTime() {
		return lbCompletionTime;
	}

	public void setLbCompletionTime(Label lbCompletionTime) {
		this.lbCompletionTime = lbCompletionTime;
	}

	public Date getRequiredFrom() {
		return requiredFrom;
	}

	public void setRequiredFrom(Date requiredFrom) {
		this.requiredFrom = requiredFrom;
	}

	public Date getRequiredTo() {
		return requiredTo;
	}

	public void setRequiredTo(Date requiredTo) {
		this.requiredTo = requiredTo;
	}

	public Collection getFiles() {
		return files;
	}

	public void setFiles(Collection files) {
		this.files = files;
	}

	public Label getConversion() {
		return conversion;
	}

	public void setConversion(Label conversion) {
		this.conversion = conversion;
	}

	public Label getDuration() {
		return duration;
	}

	public void setDuration(Label duration) {
		this.duration = duration;
	}

	public Label getCopies() {
		return copies;
	}

	public void setCopies(Label copies) {
		this.copies = copies;
	}

	public Label getAttachment() {
		return attachment;
	}

	public void setAttachment(Label attachment) {
		this.attachment = attachment;
	}

	public Label getDescription() {
		return description;
	}

	public void setDescription(Label description) {
		this.description = description;
	}

	public Label getSegment() {
		return segment;
	}

	public void setSegment(Label segment) {
		this.segment = segment;
	}

	public Label getSettingTime() {
		return settingTime;
	}

	public void setSettingTime(Label settingTime) {
		this.settingTime = settingTime;
	}

	public Label getRehearsalTime() {
		return rehearsalTime;
	}

	public void setRehearsalTime(Label rehearsalTime) {
		this.rehearsalTime = rehearsalTime;
	}

	public Label getVtrTime() {
		return vtrTime;
	}

	public void setVtrTime(Label vtrTime) {
		this.vtrTime = vtrTime;
	}

	public Label getTvroFeedTitle() {
		return tvroFeedTitle;
	}

	public void setTvroFeedTitle(Label tvroFeedTitle) {
		this.tvroFeedTitle = tvroFeedTitle;
	}

	public Label getTvroLocation() {
		return tvroLocation;
	}

	public void setTvroLocation(Label tvroLocation) {
		this.tvroLocation = tvroLocation;
	}

	public Label getTvroRequiredDate() {
		return tvroRequiredDate;
	}

	public void setTvroRequiredDate(Label tvroRequiredDate) {
		this.tvroRequiredDate = tvroRequiredDate;
	}

	public Label getTvroRequiredTime() {
		return tvroRequiredTime;
	}

	public void setTvroRequiredTime(Label tvroRequiredTime) {
		this.tvroRequiredTime = tvroRequiredTime;
	}

	public Label getTvroTimeZone() {
		return tvroTimeZone;
	}

	public void setTvroTimeZone(Label tvroTimeZone) {
		this.tvroTimeZone = tvroTimeZone;
	}

	public Label getTvroTotalTime() {
		return tvroTotalTime;
	}

	public void setTvroTotalTime(Label tvroTotalTime) {
		this.tvroTotalTime = tvroTotalTime;
	}

	public Label getTvroRemarks() {
		return tvroRemarks;
	}

	public void setTvroRemarks(Label tvroRemarks) {
		this.tvroRemarks = tvroRemarks;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Button getUnfulfilled() {
		return unfulfilled;
	}

	public void setUnfulfilled(Button unfulfilled) {
		this.unfulfilled = unfulfilled;
	}

	public TextBox getRemarksUnfulfilled() {
		return remarksUnfulfilled;
	}

	public void setRemarksUnfulfilled(TextBox remarksUnfulfilled) {
		this.remarksUnfulfilled = remarksUnfulfilled;
	}

	public Label getLbReasonUnfulfilled() {
		return lbReasonUnfulfilled;
	}

	public void setLbReasonUnfulfilled(Label lbReasonUnfulfilled) {
		this.lbReasonUnfulfilled = lbReasonUnfulfilled;
	}

}
