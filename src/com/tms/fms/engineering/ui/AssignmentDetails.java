package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;

public class AssignmentDetails extends Form {	
	private String id;
	
	private Label assignmentId;
	private Label requestTitle;
	private Label program;
	private Label requiredDate;
	private Label requiredTime;
	private Label manpower;
	
	protected Button submit;
	protected Button cancel;
	
	public void init() {
	}
	
	public void onRequest(Event event) {
		initForm();
		populateFields();
	}

	public void initForm() {
		setMethod("post");
				
		assignmentId = new Label("assignmentId");
		addChild(assignmentId);
		
		requestTitle = new Label("requestTitle");
		addChild(requestTitle);
		
		program = new Label("program");
		addChild(program);
		
		requiredDate = new Label("requiredDate");
		addChild(requiredDate);
		
		requiredTime =new Label("requiredTime");
		addChild(requiredTime);
		
		manpower = new Label("manpower");
		addChild(manpower);
		
		Application app=Application.getInstance();
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		addChild(submit);
		addChild(cancel);
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		EngineeringRequest eRequest = module.getAssignment(id);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String reqDate = sdf.format(eRequest.getRequiredFrom()) + " - " + sdf.format(eRequest.getRequiredTo());
		
		assignmentId.setText(eRequest.getAssignmentCode());
		requestTitle.setText(eRequest.getTitle());
		program.setText(eRequest.getProgramName());
		requiredDate.setText(reqDate);
		requiredTime.setText(eRequest.getFromTime() + " - " + eRequest.getToTime());
		manpower.setText(eRequest.getCompetencyName());
		
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/assignmentdetailstemp";
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

	
}
