package com.tms.fms.facility.ui;

import java.util.Date;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;

/**
 * @author fahmi
 *
 */
public class RateCardSetRateForm extends Form {
    
	protected String id;   
	protected TextField rateCardName;
	protected SelectBox serviceType;
	protected SelectBox requestType;
	protected DatePopupField dpfEffectiveDate;
	protected TextField internalRate;
	protected TextField externalRate;	
	protected TextBox description;
	protected TextBox remarksRequestor;
	protected CheckBox status;
	protected Hidden rateCardId;
	
	private Label nameLbl;	
	
	protected Button submitButton;
	protected Button cancelButton;
	protected Button editButton;
	protected Button setRateButton;
	protected Button backButton;
	
	private String cancelUrl = "rateCardList.jsp";
	private String editUrl = "rateCardEdit.jsp?id=";
	private String setRateUrl = "rateCardSetRate.jsp?id=";
	
	private String action = "";

	public RateCardSetRateForm() {

    }
   
	public void init()
	{
		setColumns(1);
		String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");
		
		dpfEffectiveDate = new DatePopupField("dpfEffectiveDate");
    	dpfEffectiveDate.setFormat("dd-MM-yyyy");
    	dpfEffectiveDate.setDate(new Date());
    	dpfEffectiveDate.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		addChild(dpfEffectiveDate);
		
		internalRate = new TextField("internalRate");
		internalRate.setSize("15");     
		internalRate.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		internalRate.addChild(new ValidatorIsNumeric("internalRateIsNumberic", msgIsNumberic, false));
		addChild(internalRate);
		
		externalRate = new TextField("externalRate");
		externalRate.setSize("15");
		externalRate.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		externalRate.addChild(new ValidatorIsNumeric("externalRateIsNumberic", msgIsNumberic, false));
		addChild(externalRate);		
			
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("fms.facility.submit", "Submit"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
        
        rateCardId = new Hidden("id");
        addChild(rateCardId);
		
		addChild(submitButton);
		addChild(cancelButton);	
	}	 
	
	public void populateForm(String id){
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try{
			RateCard rc = module.getRateCard(id);
			nameLbl = new Label("namelbl");
			nameLbl.setText(rc.getName());
			addChild(nameLbl);			
			
			rateCardId.setValue(rc.getId());
		} catch(Exception e){
			
		}		   
	}
	
	public String getDefaultTemplate() {
		return "fms/ratecardsetrateform";
    }
	
	public void onRequest(Event evt){
		this.setInvalid(false);
		id = evt.getRequest().getParameter("id");
		
		if (getId() != null){		
			//Log.getLog(getClass()).info(getId() + " xxxx ");
			populateForm(getId());
		} else {
			init();
		}
	}

	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);

	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);

	    //if the cancel button was pressed
	    if (buttonName != null && cancelButton.getAbsoluteName().equals(buttonName)) {
	    	init();
	      	return new Forward(Form.CANCEL_FORM_ACTION, getCancelUrl(), true);
	    } else {
	    	return result;
	    }	    
	}
	
	public Forward onValidate(Event evt){
		Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
		RateCard rc = new RateCard();
		
		rc.setId(getId());
		rc.setEffectiveDate(dpfEffectiveDate.getDate());
		rc.setInternalRate((String)internalRate.getValue());
		rc.setExternalRate((String)externalRate.getValue());
		
		module.insertRateCardDetail(rc);
		
		{
			//push to ABW #13014
			RateCard rcObj = module.getRateCard(rc.getId());
			if(rcObj.getStatus() != null && rcObj.getStatus().equals("1"))
			{
				module.pushToAbwServer(rcObj);
			}
			else
			{
				//#13014 send email for inactive updates
				DefaultDataObject obj = new DefaultDataObject();
				obj.setId(rc.getId());
				obj.setProperty("abwCode", rcObj.getAbwCode());
				obj.setProperty("rate_card_name", rcObj.getName());
				
				module.insertRateCardEmailNotification(obj);
			}
		}
		
		Forward fwd = new Forward("continue", getSetRateUrl() + getId(), true);
		init();
		return fwd;
	}		
	
	public TextField getRateCardName() {
		return rateCardName;
	}

	public void setRateCardName(TextField rateCardName) {
		this.rateCardName = rateCardName;
	}

	public SelectBox getRequestType() {
		return requestType;
	}

	public void setRequestType(SelectBox requestType) {
		this.requestType = requestType;
	}

	public DatePopupField getDpfEffectiveDate() {
		return dpfEffectiveDate;
	}

	public void setDpfEffectiveDate(DatePopupField dpfEffectiveDate) {
		this.dpfEffectiveDate = dpfEffectiveDate;
	}

	public TextField getInternalRate() {
		return internalRate;
	}

	public void setInternalRate(TextField internalRate) {
		this.internalRate = internalRate;
	}

	public TextField getExternalRate() {
		return externalRate;
	}

	public void setExternalRate(TextField externalRate) {
		this.externalRate = externalRate;
	}

	public TextBox getDescription() {
		return description;
	}

	public void setDescription(TextBox description) {
		this.description = description;
	}

	public TextBox getRemarksRequestor() {
		return remarksRequestor;
	}

	public void setRemarksRequestor(TextBox remarksRequestor) {
		this.remarksRequestor = remarksRequestor;
	}

	public CheckBox getStatus() {
		return status;
	}

	public void setStatus(CheckBox status) {
		this.status = status;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}
	public String getCancelUrl(){
		return cancelUrl;
	}	
	public void setCancelUrl(String cancelUrl){
		this.cancelUrl=cancelUrl;
	}
	public String getEditUrl() {
		return editUrl;
	}
	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}
	public String getSetRateUrl() {
		return setRateUrl;
	}
	public void setSetRateUrl(String setRateUrl) {
		this.setRateUrl = setRateUrl;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Hidden getRateCardId() {
		return rateCardId;
	}

	public void setRateCardId(Hidden rateCardId) {
		this.rateCardId = rateCardId;
	}
	
}
