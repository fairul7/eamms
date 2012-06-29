package com.tms.fms.engineering.ui;

import java.util.Iterator;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

public class SubmitRequestForm extends Form {
	private Radio[] radioRequest = new Radio[EngineeringModule.SUBMIT_REQUEST_MAP.size()];
	private RequestPopupSelect request;
	private Button btnContinue;
	private String requestId;
	
	public void init() {
		Application app = Application.getInstance();
		
		int i=0;
		for(Iterator itr=EngineeringModule.SUBMIT_REQUEST_MAP.keySet().iterator();itr.hasNext();i++){
			String key=(String)itr.next();
			radioRequest[i]=new Radio("submitType_"+key);
			radioRequest[i].setText((String)EngineeringModule.SUBMIT_REQUEST_MAP.get(key));
			radioRequest[i].setValue(key);
			if(EngineeringModule.SUBMIT_NEW_REQUEST.equals(key)){
				radioRequest[i].setChecked(true);
			}
			radioRequest[i].setOnClick("populateSubmit('"+key+"');");
			radioRequest[i].setGroupName("requestType");
			addChild(radioRequest[i]);
		}
		
		request = new RequestPopupSelect("request");
		request.init();
		addChild(request);
		
		btnContinue = new Button("btnContinue", app.getMessage("fms.request.label.continue"));
		addChild(btnContinue);
	}
	
	public Forward onValidate(Event evt) {
		String buttonName = findButtonClicked(evt);
		if(buttonName != null && btnContinue.getAbsoluteName().equals(buttonName)){
			String checkedRadio="";
			requestId = "";
			for(int i=0; i<EngineeringModule.SUBMIT_REQUEST_MAP.size(); i++){
				if(radioRequest[i].isChecked()){
					checkedRadio = radioRequest[i].getName();
				}
			}
			if(checkedRadio != null && !checkedRadio.equals("")){
				if(!checkedRadio.equals("submitType_"+EngineeringModule.SUBMIT_NEW_REQUEST)){
					if (WidgetUtil.getSbValue(request)!=null && !WidgetUtil.getSbValue(request).equals("")){
						String optionValue = WidgetUtil.getSbValue(request);
						String[] tempValue = optionValue.split("-");
						requestId = tempValue[0].trim();
						return new Forward("continue");
					}else{
						return new Forward("error");
					}
				}else{
					return new Forward("continueNewRequest");
				}
			}
		}
		return null;
	}
	
	public void onRequest(Event evt) {
		init();
	}
	
	public String getDefaultTemplate() {
		return "/fms/engineering/submitNewRequestTemplate";
	}

	public Radio[] getRadioRequest() {
		return radioRequest;
	}

	public void setRadioRequest(Radio[] radioRequest) {
		this.radioRequest = radioRequest;
	}
	
	public RequestPopupSelect getRequest() {
		return request;
	}

	public void setRequest(RequestPopupSelect request) {
		this.request = request;
	}

	public Button getBtnContinue() {
		return btnContinue;
	}

	public void setBtnContinue(Button btnContinue) {
		this.btnContinue = btnContinue;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
