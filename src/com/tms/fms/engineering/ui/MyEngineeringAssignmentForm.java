package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;

public class MyEngineeringAssignmentForm extends Form {	
	private String id;
	
	private String requestId;
	private Label assignmentId;
	private Label requestTitle;
	private Label program;
	private Label description;
	private Label assignTo;
	private Label status;
	private Label requiredDateFrom;
	private Label requiredDateTo;
	private Label requiredTimeFrom;
	private Label requiredTimeTo;
	private Label manpower;
	private Label remarks;
	private Label reasonUnfulfilled;
	private Label lbAttachmentList;
	private Label completionDate;
	
	protected Button completeAssignment;
	protected Button unfulfilledAssignment;
	protected Button submit;
	protected Button cancel;
	
	private Collection files = new ArrayList();
	private String assignmentStatus = "";
	private String cancelUrl 				= "myEngineeringAssignment.jsp";
	private String completeAssignmentUrl 	= "completeMyAssignment.jsp?id=";
	private String unfulfilledAssignmentUrl = "unfulfillMyAssignment.jsp?id=";
	
	public void onRequest(Event event) {
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		files = null;
		if (id!=null) {
			files = module.getManpowerFiles(id);
		}
		init();
		populateFields();
	}

	public void init() {
		setMethod("post");
				
		assignmentId = new Label("assignmentId");
		addChild(assignmentId);
		
		requestTitle = new Label("requestTitle");
		addChild(requestTitle);
		
		program = new Label("program");
		addChild(program);
		
		description = new Label("description");
		addChild(description);
		
		assignTo = new Label("assignTo");
		addChild(assignTo);
		
		status = new Label("status");
		addChild(status);
		
		requiredDateFrom = new Label("requiredDateFrom");
		addChild(requiredDateFrom);
		
		requiredDateTo = new Label("requiredDateTo");
		addChild(requiredDateTo);
		
		requiredTimeFrom = new Label("requiredTimeFrom");
		addChild(requiredTimeFrom);
		
		requiredTimeTo = new Label("requiredTimeTo");
		addChild(requiredTimeTo);
		
		manpower = new Label("manpower");
		addChild(manpower);
		
		completionDate = new Label("completionDate");
		addChild(completionDate);
		
		remarks = new Label("remarks");
		addChild(remarks);
		
		lbAttachmentList= new Label("attachmentList");
		String content="<table class='borderTable' width='60%'>";
		
		//Iterate files
		if (files.size() > 0) {
			Iterator itr = files.iterator();
			while (itr.hasNext()){
				EngineeringRequest file = (EngineeringRequest)itr.next();
				content += "<tr><td><a onClick=\"javascript:window.open('/storage" + file.getFilePath() + "', 'openAttachment', 'height=350,width=250,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');return false;\">" + file.getFileName() +"</a></td>";
				//content += "<td><a href='?id="+ id + "&amp;do=delete&amp;idfile=" + file.getFileId() + "'>Remove</a><br /></td>";
				content += "</tr>";
			}
		}
		
		content+="</table>";
		lbAttachmentList.setText(content);
		lbAttachmentList.setEscapeXml(false);
		addChild(lbAttachmentList);
		
		reasonUnfulfilled = new Label("reasonUnfulfilled");
		addChild(reasonUnfulfilled);
		
		Application app=Application.getInstance();
		completeAssignment = new Button("completeAssignment", app.getMessage("fms.label.completeAssignment"));
		unfulfilledAssignment = new Button("unfulfilledAssignment", app.getMessage("fms.label.unfulfilledAssignment"));
		cancel = new Button("cancel", app.getMessage("fms.facility.backToListing"));
		addChild(completeAssignment);
		addChild(unfulfilledAssignment);
		addChild(cancel);
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		EngineeringRequest eRequest = module.getAssignment(getId());
		
		if (eRequest != null ) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			SimpleDateFormat sdfcomplete = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
			String reqDate = sdf.format(eRequest.getRequiredFrom()) + " - " + sdf.format(eRequest.getRequiredTo());
			
			assignmentId.setText(eRequest.getAssignmentCode());
			requestTitle.setText(eRequest.getTitle());
			program.setText(eRequest.getProgramName());
			description.setText(eRequest.getDescription());
			status.setText((String) EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.get(eRequest.getStatus()));
			assignTo.setText(eRequest.getManpowerName());
			requiredDateFrom.setText(sdf.format(eRequest.getRequiredFrom()));
			requiredDateTo.setText(sdf.format(eRequest.getRequiredTo()));
			requiredTimeFrom.setText(eRequest.getFromTime());
			requiredTimeTo.setText(eRequest.getToTime());
			manpower.setText(eRequest.getCompetencyName());
			
			setRequestId(eRequest.getRequestId());
			
			if (EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED.equals(eRequest.getStatus())){
				assignmentStatus = EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED;
				completionDate.setText(sdfcomplete.format(eRequest.getCompletionDate()));
				remarks.setText(eRequest.getRemarks());
				reasonUnfulfilled.setText("");
			}
			
			if (EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED.equals(eRequest.getStatus())){
				assignmentStatus = EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED;
				remarks.setText("");
				reasonUnfulfilled.setText(eRequest.getReasonUnfulfilled());
			}
			
			if (EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED.equals(eRequest.getStatus())
					|| EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED.equals(eRequest.getStatus())){
				completeAssignment.setHidden(true);
				unfulfilledAssignment.setHidden(true);
				
			}
		}
	}
	
	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);

	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);

	    //if the cancel button was pressed
	    if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	      	return new Forward(Form.CANCEL_FORM_ACTION, getCancelUrl(), true);
	    } else if (buttonName != null && completeAssignment.getAbsoluteName().equals(buttonName)) {
	    	return new Forward("Complete", getCompleteAssignmentUrl() + getId() , true);
		} else if (buttonName != null && unfulfilledAssignment.getAbsoluteName().equals(buttonName)) {
	    	return new Forward("Unfulfilled", getUnfulfilledAssignmentUrl() + getId() , true);
		} else {
	    	return result;
	    }	    
	}
	
	@Override
	public Forward onValidate(Event evt) {
		return super.onValidate(evt);
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/myassignmentdetailstemp";
	}

	public Label getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Label assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Label getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(Label requestTitle) {
		this.requestTitle = requestTitle;
	}

	public Label getProgram() {
		return program;
	}

	public void setProgram(Label program) {
		this.program = program;
	}

	public Label getRequiredDateFrom() {
		return requiredDateFrom;
	}

	public void setRequiredDateFrom(Label requiredDateFrom) {
		this.requiredDateFrom = requiredDateFrom;
	}

	public Label getRequiredDateTo() {
		return requiredDateTo;
	}

	public void setRequiredDateTo(Label requiredDateTo) {
		this.requiredDateTo = requiredDateTo;
	}

	public Label getRequiredTimeFrom() {
		return requiredTimeFrom;
	}

	public void setRequiredTimeFrom(Label requiredTimeFrom) {
		this.requiredTimeFrom = requiredTimeFrom;
	}

	public Label getRequiredTimeTo() {
		return requiredTimeTo;
	}

	public void setRequiredTimeTo(Label requiredTimeTo) {
		this.requiredTimeTo = requiredTimeTo;
	}

	public Label getManpower() {
		return manpower;
	}

	public void setManpower(Label manpower) {
		this.manpower = manpower;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Label getDescription() {
		return description;
	}

	public void setDescription(Label description) {
		this.description = description;
	}

	public Label getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Label assignTo) {
		this.assignTo = assignTo;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public Button getCompleteAssignment() {
		return completeAssignment;
	}

	public void setCompleteAssignment(Button completeAssignment) {
		this.completeAssignment = completeAssignment;
	}

	public String getCompleteAssignmentUrl() {
		return completeAssignmentUrl;
	}

	public void setCompleteAssignmentUrl(String completeAssignmentUrl) {
		this.completeAssignmentUrl = completeAssignmentUrl;
	}

	public Button getUnfulfilledAssignment() {
		return unfulfilledAssignment;
	}

	public void setUnfulfilledAssignment(Button unfulfilledAssignment) {
		this.unfulfilledAssignment = unfulfilledAssignment;
	}

	public String getUnfulfilledAssignmentUrl() {
		return unfulfilledAssignmentUrl;
	}

	public void setUnfulfilledAssignmentUrl(String unfulfilledAssignmentUrl) {
		this.unfulfilledAssignmentUrl = unfulfilledAssignmentUrl;
	}

	public Label getStatus() {
		return status;
	}

	public void setStatus(Label status) {
		this.status = status;
	}

	public Label getRemarks() {
		return remarks;
	}

	public void setRemarks(Label remarks) {
		this.remarks = remarks;
	}

	public Label getReasonUnfulfilled() {
		return reasonUnfulfilled;
	}

	public void setReasonUnfulfilled(Label reasonUnfulfilled) {
		this.reasonUnfulfilled = reasonUnfulfilled;
	}

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}

	public Label getLbAttachmentList() {
		return lbAttachmentList;
	}

	public void setLbAttachmentList(Label lbAttachmentList) {
		this.lbAttachmentList = lbAttachmentList;
	}

	public Collection getFiles() {
		return files;
	}

	public void setFiles(Collection files) {
		this.files = files;
	}

	public Label getCompletionDate() {
		return completionDate;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setCompletionDate(Label completionDate) {
		this.completionDate = completionDate;
	}
	
	
}
