package com.tms.hr.recruit.ui;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.RichTextBox;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class SetupMessageBody extends Form{
	public static final String FORWARD_UPDATED = "updated";
	public static final String FORWARD_ERROR = "error"; 
	
	protected TextBox tbMessageBody;
	protected TextBox tbEmployeeOppor;
	protected Button btnSubmit; 
	private boolean hasData;

	public void init(){
		initForm();
	}
	 
	public void initForm(){
		setColumns(2);
    	setMethod("POST");
    	
    	Application app = Application.getInstance();
    	
    	tbMessageBody = new RichTextBox("tbMessageBody");
    	//tbMessageBody.addChild(new ValidatorNotEmpty("tbMessageBodyVNE", app.getMessage("recruit.general.warn.empty")));

    	tbMessageBody.setRows("40");
    	tbMessageBody.setCols("20");
    	Label lblMessageBody = new Label("lblMessageBody","<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tbMessageBody") + "</span>");
    	lblMessageBody.setAlign("right");
    	addChild(lblMessageBody);
    	addChild(tbMessageBody);
    	
    	/*Label lblspace = new Label("lblspace","");
    	addChild(lblspace);*/
    	
    	tbEmployeeOppor = new RichTextBox("tbEmployeeOppor");
    	tbEmployeeOppor.setRows("40");
    	tbEmployeeOppor.setCols("20");
    	Label lblEmployeeOppor = new Label("lblEmployeeOppor","<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.employeeOppor") + "</span>");
    	lblEmployeeOppor.setAlign("right");
    	addChild(lblEmployeeOppor);
    	addChild(tbEmployeeOppor);
    	
    	Label lblspace1 = new Label("lblspace1","");
    	addChild(lblspace1);
    	
    	btnSubmit = new Button("btnSubmit", app.getMessage("recruit.general.label.submit","Submit"));
    	addChild(btnSubmit);
	}
	
	public void onRequest(Event evt){
		populateForm();
	}
	
	public void populateForm(){
		Application app = Application.getInstance();
		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
		
		try{
			VacancyObj smbObj = rm.loadMessagebody();
			
			if(smbObj.getMessageBody()!=null && !smbObj.getMessageBody().equals("")){
				tbMessageBody.setValue(smbObj.getMessageBody());
				tbEmployeeOppor.setValue(smbObj.getEmployeeOppor());
				hasData=true;
			}	
			else{
				tbMessageBody.setValue("sample");
				tbEmployeeOppor.setValue("");
				hasData=false;
			}
		}catch(DataObjectNotFoundException e){
			Log.getLog(getClass()).error("Module Message Body not found" + e);
			init();
		}
	}
	
	public Forward onValidate(Event evt){
		String action = findButtonClicked(evt);
		if(action.equals(btnSubmit.getAbsoluteName())){
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			
			//auditObj
			VacancyObj auditObj = new VacancyObj();
			String actionTaken="";
			
			VacancyObj smbObj = new VacancyObj();
			smbObj.setMessageBody((String)tbMessageBody.getValue());
			if(tbEmployeeOppor.getValue()!=null){
				smbObj.setEmployeeOppor((String)tbEmployeeOppor.getValue());
			}else{
				smbObj.setEmployeeOppor("");
			}
			if(hasData){
				rm.updateSetupMessageBody(smbObj);
				//audit
				actionTaken="Update Message Body & Employment Opportunities Message";
				auditObj.setAndInsertAudit("", "", actionTaken);
			}else{
				rm.insertSetupMessageBody(smbObj);
				//audit
				actionTaken="Insert Message Body & Employment Opportunities Message";
				auditObj.setAndInsertAudit("", "", actionTaken);
			}
			
		 	return new Forward(FORWARD_UPDATED);
		}else
			return new Forward(FORWARD_ERROR);	
	}
		
	public boolean isHasData() {
		return hasData;
	}

	public void setHasData(boolean hasData) {
		this.hasData = hasData;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public TextBox getTbEmployeeOppor() {
		return tbEmployeeOppor;
	}

	public void setTbEmployeeOppor(TextBox tbEmployeeOppor) {
		this.tbEmployeeOppor = tbEmployeeOppor;
	}

	public TextBox getTbMessageBody() {
		return tbMessageBody;
	}

	public void setTbMessageBody(TextBox tbMessageBody) {
		this.tbMessageBody = tbMessageBody;
	}
	
	
}

















