package com.tms.fms.engineering.ui;

import java.util.Iterator;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.stdui.RichTextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

public class editRequestForm extends Form{
	protected String requestId;
	protected String lbRequestType;
	protected String type;
	protected TextField title;
	protected TextField clientName;
	protected RichTextBox remarks;
	protected Radio[] requestType=new Radio[EngineeringModule.REQUEST_TYPE_MAP.size()];
	protected Radio[] programType=new Radio[EngineeringModule.PROGRAM_TYPE_MAP.size()];
	protected SingleProgramSelectBox program;
	
	protected Button cancel;
	protected Button submit;
	
	private void initForm() {
		setMethod("post");
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		EngineeringRequest eRequest=module.getRequestWithService(requestId);
		Application app = Application.getInstance();

		title = new TextField("title");
		title.setSize("50");
		title.setMaxlength("255");
		title.setValue(eRequest.getTitle());
		title.addChild(new ValidatorNotEmpty("nameNotEmpty", ""));
		
		clientName = new TextField("clientName");
		clientName.setSize("50");
		clientName.setMaxlength("255");
		clientName.setValue(eRequest.getClientName());

		remarks = new RichTextBox("remarks");
		remarks.setCols("50");
		remarks.setValue(eRequest.getDescription());
		
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
		WidgetUtil.setRadioValue(requestType,eRequest.getRequestType());
		lbRequestType = eRequest.getRequestType();
		
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
		
		WidgetUtil.setRadioValue(programType,eRequest.getProgramType());
		
		program = new SingleProgramSelectBox("program");
		program.init();
		
		if (eRequest.getProgram()!=null && !eRequest.getProgram().equals("")){
			program.setIds(new String[]{eRequest.getProgram()});
		}
		submit = new Button("submit", app.getMessage("fms.facility.update"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		
		addChild(title);
		addChild(clientName);
		addChild(program);
		addChild(remarks);
		addChild(submit);
		addChild(cancel);
	}
	
	public void onRequest(Event evt) {
		super.onRequest(evt);
		
		initForm();
	}
	
	public Forward onValidate(Event evt) {
		String buttonName = findButtonClicked(evt);
		if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			EngineeringRequest eRequest = module.getRequestWithService(requestId);
			Forward fwd=new Forward("");
			boolean valid=true;
			
			eRequest.setTitle((String)title.getValue());
			eRequest.setClientName((String)clientName.getValue());
			eRequest.setDescription((String)remarks.getValue());
			eRequest.setProgram(WidgetUtil.getSbValue(program));
			eRequest.setRequestType(WidgetUtil.getRadioValue(requestType));
			eRequest.setProgramType(WidgetUtil.getRadioValue(programType));
			eRequest.setStatus(EngineeringModule.DRAFT_STATUS);
			
			if((eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_INTERNAL)) && 
					(eRequest.getProgram()==null||eRequest.getProgram().trim().length()==0)){
				program.setInvalid(true);
				valid=false;
			}
			
			if(valid==false){
				this.setInvalid(true);
				return fwd;
			}
			
			if ((eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_INTERNAL)) || 
					(eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM))){
				eRequest.setClientName("");
			}
			
			if ((eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)) || 
					(eRequest.getRequestType().equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM))){
				eRequest.setProgram("");
			}
			eRequest.setRequestId(requestId);
			module.editRequest(eRequest);
			return new Forward("edited");
		}else if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			return new Forward("cancel");
		}
		return null;
	}
	
	public String getDefaultTemplate() {
		return "/fms/engineering/editFormTemplate";
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getLbRequestType() {
		return lbRequestType;
	}

	public void setLbRequestType(String lbRequestType) {
		this.lbRequestType = lbRequestType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TextField getTitle() {
		return title;
	}

	public void setTitle(TextField title) {
		this.title = title;
	}

	public TextField getClientName() {
		return clientName;
	}

	public void setClientName(TextField clientName) {
		this.clientName = clientName;
	}

	public RichTextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(RichTextBox remarks) {
		this.remarks = remarks;
	}

	public Radio[] getRequestType() {
		return requestType;
	}

	public void setRequestType(Radio[] requestType) {
		this.requestType = requestType;
	}

	public Radio[] getProgramType() {
		return programType;
	}

	public void setProgramType(Radio[] programType) {
		this.programType = programType;
	}

	public SingleProgramSelectBox getProgram() {
		return program;
	}

	public void setProgram(SingleProgramSelectBox program) {
		this.program = program;
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
}
