package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.TimeField;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;

public class UnfulfilledAssignmentForm extends Form {	
	private String id;
	
	protected DatePopupField completionDate;
	protected TimeField completionTime;
	protected TextBox remarks;
	protected FileUpload attachment;
	
	protected Panel attachmentPanel;
	
	private Label assignmentId;
	private Label requestTitle;
	private Label program;
	private Label description;
	private Label assignTo;
	private Label requiredDateFrom;
	private Label requiredDateTo;
	private Label requiredTimeFrom;
	private Label requiredTimeTo;
	private Label manpower;

	protected Button submit;
	protected Button cancel;

	private String cancelUrl 				= "myAssignmentDetails.jsp?id=";
	private String completeAssignmentUrl 	= "completeMyAssignment.jsp?id=";
	private String unfulfilledAssignmentUrl = "unfulfillMyAssignment.jsp?id=";
	
	public void onRequest(Event event) {
		init();
		populateFields();
	}

	public void init() {
		Application app=Application.getInstance();
		setMethod("post");
				
		assignmentId = new Label("assignmentId");
		addChild(assignmentId);
		
		requestTitle = new Label("requestTitle");
		addChild(requestTitle);
		
		program = new Label("program");
		addChild(program);
		
		requiredDateFrom = new Label("requiredDateFrom");
		addChild(requiredDateFrom);
		
		requiredDateTo = new Label("requiredDateTo");
		addChild(requiredDateTo);
		
		requiredTimeFrom = new Label("requiredTimeFrom");
		addChild(requiredTimeFrom);
		
		requiredTimeTo = new Label("requiredTimeTo");
		addChild(requiredTimeTo);
		
		remarks = new TextBox("remarks");
		remarks.setRows("4");
		remarks.setCols("50");
		addChild(remarks);
		
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		
		addChild(submit);
		addChild(cancel);
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		EngineeringRequest eRequest = module.getAssignment(id);
		
		if (eRequest != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			String reqDate = sdf.format(eRequest.getRequiredFrom()) + " - " + sdf.format(eRequest.getRequiredTo());
			
			assignmentId.setText(eRequest.getAssignmentCode());
			requestTitle.setText(eRequest.getTitle());
			program.setText(eRequest.getProgramName());		
			requiredDateFrom.setText(sdf.format(eRequest.getRequiredFrom()));
			requiredDateTo.setText(sdf.format(eRequest.getRequiredTo()));
			requiredTimeFrom.setText(eRequest.getFromTime());
			requiredTimeTo.setText(eRequest.getToTime());
		}
	}
	
	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);
		
	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);

	    //if the cancel button was pressed
	    if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	      	return new Forward(Form.CANCEL_FORM_ACTION, getCancelUrl() + getId(), true);
	    } else {
	    	return result;
	    }
	}
	
	@Override
	public Forward onValidate(Event evt) {
		Application application = Application.getInstance();
		EngineeringModule module = (EngineeringModule)application.getModule(EngineeringModule.class);
		EngineeringRequest er = new EngineeringRequest();
		
		er.setAssignmentId(id);		
		er.setDescription((String)remarks.getValue());
		
		module.unfulfillAssignment(er);
		
		return new Forward("UNFULFILLED");		
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/unfulfilledassignmenttemp";
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

	public String getCompleteAssignmentUrl() {
		return completeAssignmentUrl;
	}

	public void setCompleteAssignmentUrl(String completeAssignmentUrl) {
		this.completeAssignmentUrl = completeAssignmentUrl;
	}

	public String getUnfulfilledAssignmentUrl() {
		return unfulfilledAssignmentUrl;
	}

	public void setUnfulfilledAssignmentUrl(String unfulfilledAssignmentUrl) {
		this.unfulfilledAssignmentUrl = unfulfilledAssignmentUrl;
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
	

	public TextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(TextBox remarks) {
		this.remarks = remarks;
	}

	public FileUpload getAttachment() {
		return attachment;
	}

	public void setAttachment(FileUpload attachment) {
		this.attachment = attachment;
	}

	public Panel getAttachmentPanel() {
		return attachmentPanel;
	}

	public void setAttachmentPanel(Panel attachmentPanel) {
		this.attachmentPanel = attachmentPanel;
	}
	
}
