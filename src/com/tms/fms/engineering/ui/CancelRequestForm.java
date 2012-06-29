package com.tms.fms.engineering.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.OtherService;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.transport.model.FmsNotification;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.widgets.BoldLabel;

public class CancelRequestForm extends Form {
	protected SelectBox reason;
	protected Button cancel;
	protected Button submit;
	protected Panel buttonPanel;
	protected Label lbTitle;
	protected Label lbProgram;
	protected Label lbSubmittedBy;
	protected Label lbReason;
	protected Label title;
	protected Label program;
	protected Label submittedBy;
	protected String requestId;
	protected EngineeringRequest request;
	protected EngineeringRequest manpowerAssign;
	
	public void onRequest(Event event) {
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		
		request=module.getRequest(requestId);
		initForm();
	}
	
	public void populateButtons() {
		Application app = Application.getInstance();
		buttonPanel = new Panel("panel");
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		//cancel.setOnClick("window.close();");
		buttonPanel.addChild(submit);
		buttonPanel.addChild(cancel);
		addChild(new Label(("tupuku")));
		addChild(buttonPanel);
	}

	public void initForm() {
		setMethod("post");
		setColumns(2);
		Application app = Application.getInstance();
		
		lbTitle = new BoldLabel("lbTitle");
		lbTitle.setAlign("right");
		lbTitle.setText(app.getMessage("fms.request.label.requestTitle"));
		addChild(lbTitle);
		
		title = new Label("title");
		title.setText(request.getTitle());
		addChild(title);
		
		if (EngineeringModule.REQUEST_TYPE_INTERNAL.equals(request.getRequestType())) {
			lbProgram = new BoldLabel("lbProgram");
			lbProgram.setAlign("right");
			lbProgram.setText(app.getMessage("fms.request.label.program"));
			addChild(lbProgram);
			
			program =new Label("program");
			program.setText(request.getProgramName());
			addChild(program);
		} else if (EngineeringModule.REQUEST_TYPE_EXTERNAL.equals(request.getRequestType())){
			lbProgram = new BoldLabel("lbProgram");
			lbProgram.setAlign("right");
			lbProgram.setText(app.getMessage("fms.request.label.clientName"));
			addChild(lbProgram);
			
			program =new Label("program");
			program.setText(request.getClientName());
			addChild(program);
		}
		
		lbSubmittedBy = new BoldLabel("lbSubmittedBy");
		lbSubmittedBy.setAlign("right");
		lbSubmittedBy.setText(app.getMessage("fms.facility.label.submittedBy"));
		addChild(lbSubmittedBy);
		
		submittedBy = new Label("submittedBy");
		submittedBy.setText(request.getCreatedUserName()+" ["+DateUtil.formatDate(SetupModule.DATE_FORMAT, request.getSubmittedDate())+"]");
		addChild(submittedBy);
		
		lbReason = new BoldLabel("lbReason");
		lbReason.setAlign("right");
		lbReason.setText(app.getMessage("fms.facility.label.cancellationReasons"));
		addChild(lbReason);
		
		reason = new SelectBox("reason");
		reason.setOptionMap(EngineeringModule.CANCELLATION_REASONS_MAP);
		addChild(reason);
		
		populateButtons();
		
	}
	
	public Forward actionPerformed(Event evt) {
		
		if (cancel.getAbsoluteName().equals(findButtonClicked(evt))) {
			return new Forward("CANCEL");
		}
		
		return super.actionPerformed(evt);
	}
	
	
	public Forward onValidate(Event event) {
		try {
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			module.cancelRequest(requestId, WidgetUtil.getSbSelectionText(reason), request.getStatus());	
			
			if (EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()) 
					|| EngineeringModule.APPLIED_CANCELLATION.equals(request.getStatus()) 
					|| EngineeringModule.FC_ASSIGNED_STATUS.equals(request.getStatus())
					|| EngineeringModule.OUTSOURCED_STATUS.equals(request.getStatus())){
				if(module.isOutsource(requestId)){
					return new Forward("CANCELLED");
				}
				else{
					module.sendCancellationEmailToManpower(requestId, request.getTitle(), WidgetUtil.getSbSelectionText(reason));
					return new Forward("APPLIED-CANCELLATION");
				}	
				
			} else {
				return new Forward("CANCELLED");
			}			
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.toString()); 
			return new Forward("FAILED");
		} 
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
}
