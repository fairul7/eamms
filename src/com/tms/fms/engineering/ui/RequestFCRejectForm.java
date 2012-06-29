package com.tms.fms.engineering.ui;

import org.apache.axis.collections.SequencedHashMap;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.widgets.BoldLabel;

/**
 * 
 * @author fahmi
 *
 */
public class RequestFCRejectForm extends Form {
	protected SelectBox reason;
	protected Button cancel;
	protected Button submit;
	protected Panel buttonPanel;
	protected Label lbTitle;
	protected Label lbProgram;
	protected Label lbSubmittedBy;
	protected Label lbReason;
	protected Label lbOtherReason;
	protected Label title;
	protected Label program;
	protected Label submittedBy;
	protected String requestId;
	protected TextBox otherReason;
	protected EngineeringRequest request;
	
	public void onRequest(Event event) {
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		request = module.getRequest(requestId);
		initForm();
	}
	
	public void populateButtons() {
		Application app = Application.getInstance();
		buttonPanel = new Panel("panel");
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		cancel.setOnClick("window.close();");
		
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
		lbReason.setText(app.getMessage("fms.facility.label.rejectReasons"));
		addChild(lbReason);
		
		reason = new SelectBox("reason");
		SequencedHashMap map=new SequencedHashMap();
		map.put("-1", app.getMessage("fms.request.label.selectReason"));
		map.putAll(FacilitiesCoordinatorModule.REJECT_REASONS_MAP);
		reason.setOptionMap(map);
		addChild(reason);

		lbOtherReason = new BoldLabel("lbOtherReason");
		lbOtherReason.setAlign("right");
		lbOtherReason.setText(app.getMessage("fms.facility.label.otherRejectReason"));
		addChild(lbOtherReason);
		
		otherReason = new TextBox("otherReason");
		otherReason.setSize("100");
		addChild(otherReason);
		
		populateButtons();
		
	}
	
	
	public Forward onValidate(Event event) {
		try {
			//FacilitiesCoordinatorModule module = (FacilitiesCoordinatorModule)Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
			
			Application application = Application.getInstance();
			FacilitiesCoordinatorModule module = (FacilitiesCoordinatorModule)application.getModule(FacilitiesCoordinatorModule.class);
			EngineeringModule mod = (EngineeringModule)application.getModule(EngineeringModule.class);
			
			String strReason = "";
			
			if(!"-1".equals(WidgetUtil.getSbValue(reason))){
				strReason = WidgetUtil.getSbSelectionText(reason);
			}
			
			module.rejectRequest(requestId, strReason, (String)otherReason.getValue());		
			//update transport request (OB Van)
			mod.updateTranportRequest(requestId);
			
			return new Forward("UPDATE");
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
