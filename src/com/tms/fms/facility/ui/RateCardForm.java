package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.RichTextBox;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorNumberInRange;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.util.WidgetUtil;

/**
 * @author fahmi
 *
 */

@SuppressWarnings("serial")
public class RateCardForm extends Form {
    
	protected String id;   
	protected TextField rateCardName;
	protected SelectBox serviceType;
	protected SelectBox requestType;
	protected DatePopupField dpfEffectiveDate;
	protected TextField internalRate;
	protected TextField externalRate;	
	protected RichTextBox description;
	protected RichTextBox remarksRequestor;
	protected CheckBox transportRequest;
	private Panel pnType;
	protected Radio trYes;
	protected Radio trNo;
	private SelectBox category;
	private String needCategory;
	
	private Label nameLbl;	
	private Label errorLbl;
	private Label serviceLbl;
	private Label effectiveDateLbl;
	private Label internalRateLbl;
	private Label externalRateLbl;
	private Label descriptionLbl;
	private Label remarksLbl;
	private Label statusLbl;
	private Label transportRequestLbl;
	
	protected Button submitButton;
	protected Button cancelButton;
	protected Button editButton;
	protected Button setRateButton;
	protected Button backButton;
	
	private String cancelUrl = "rateCardList.jsp";
	private String editUrl = "rateCardEdit.jsp?id=";
	private String setRateUrl = "rateCardSetRate.jsp?id=";
	
	private String action = "";
	
	private Collection facilities = new ArrayList();
	private Collection manpower = new ArrayList();
	
	protected SelectBox abwCode;
	private Label abwCodeLbl;
	

	public RateCardForm() {
    }
   
	@SuppressWarnings("unchecked")
	public void init()
	{
		String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");
		setColumns(1);
		
		rateCardName = new TextField("rateCardName");
		rateCardName.setSize("30");     
		rateCardName.addChild(new ValidatorNotEmpty("rcEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		addChild(rateCardName);
		
		serviceType = new SelectBox("serviceType");		
		int i=0;
		for(Iterator itr=EngineeringModule.SERVICES_MAP.keySet().iterator();itr.hasNext();i++){
			String key=(String)itr.next();
			serviceType.addOption(key, (String)EngineeringModule.SERVICES_MAP.get(key));
		}		
		addChild(serviceType);
			
		requestType = new SelectBox("requestType");
		addChild(requestType);
		
		dpfEffectiveDate = new DatePopupField("dpfEffectiveDate");
    	dpfEffectiveDate.setFormat("dd-MM-yyyy");
    	dpfEffectiveDate.setDate(new Date());
    	dpfEffectiveDate.addChild(new ValidatorNotEmpty("edEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
  	    Calendar year = Calendar.getInstance();
	    dpfEffectiveDate.addChild(new ValidatorNumberInRange("dpfEffectiveDateIsInRange", Application.getInstance().getMessage("fms.tran.msg.invalidYear", "Invalid Year"), 1000, year.get(Calendar.YEAR)+1));
		addChild(dpfEffectiveDate);
		
		internalRate = new TextField("internalRate");
		internalRate.setSize("15");     
		internalRate.addChild(new ValidatorNotEmpty("irEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		internalRate.addChild(new ValidatorIsNumeric("internalRateIsNumberic", msgIsNumberic, false));
		addChild(internalRate);
		
		externalRate = new TextField("externalRate");
		externalRate.setSize("15");     
		externalRate.addChild(new ValidatorNotEmpty("erEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		externalRate.addChild(new ValidatorIsNumeric("externalRateIsNumberic", msgIsNumberic, false));
		addChild(externalRate);
		
		description = new RichTextBox("description");
		description.setCols("8");
		description.setRows("10");        
		addChild(description);
		
		remarksRequestor = new RichTextBox("remarksRequestor");
		remarksRequestor.setCols("8");
		remarksRequestor.setRows("10");
		addChild(remarksRequestor);
	    
//	    transportRequest = new CheckBox("transportRequest", Application.getInstance().getMessage("fms.tran.setup.transportRequestYes", "Transport Request"));
//	    transportRequest.setChecked(false);
//	    transportRequest.setOnClick("showHiddenCategory();");
//	    addChild(transportRequest);
	    
	    pnType = new Panel("pnType");
		trYes = new Radio("trYes", Application.getInstance().getMessage("fms.tran.setup.transportRequestYes", "Yes"));
		trNo = new Radio("trNo", Application.getInstance().getMessage("fms.tran.setup.transportRequestNo", "No"), true);
		trYes.setGroupName("typeGroup");
		trYes.setOnClick("showHiddenCategory('Y')");
		trNo.setGroupName("typeGroup");
		trNo.setOnClick("showHiddenCategory('N')");
		pnType.addChild(trYes);
		pnType.addChild(trNo);
		addChild(pnType);
		
		Collection collcat = new ArrayList();
		category = new SelectBox("category");
		TransportModule tran = (TransportModule) Application.getInstance().getModule(TransportModule.class);        		
		collcat = tran.selectSetupObject("fms_tran_category",null,"-1",null,false,0,-1);
		category.addOption("-1", "--- NONE ---");
		for(Iterator it = collcat.iterator(); it.hasNext(); ){
			SetupObject so = (SetupObject) it.next();
			String id = so.getSetup_id();
			String name = so.getName();
			category.addOption(id, name);
		}
		addChild(category);
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("fms.facility.continue", "Continue"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
        
        editButton = new Button("editButton");
        setRateButton = new Button("setRateButton");       
        
		addChild(submitButton);
		addChild(cancelButton);
		addChild(editButton);
		addChild(setRateButton);
		
		abwCode = new SelectBox("abwCode");		
		int j=0;
		SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		SequencedHashMap abwCodeMap = sm.getAbwCode();
		if(abwCodeMap != null)
		{
			for(Iterator itr=abwCodeMap.keySet().iterator();itr.hasNext();j++){
				String key=(String)itr.next();
				abwCode.addOption(key, key+" - "+(String)abwCodeMap.get(key));
			}		
		}
		addChild(abwCode);
	}	 
	
	public void populateForm(String id){
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try{
			RateCard rc = module.getRateCard(id);
			nameLbl = new Label("namelbl");
			nameLbl.setText(rc.getName());
			addChild(nameLbl);
			
			boolean validRateCard = module.isValidRateCard(rc.getId());
			errorLbl = new Label("errorLbl");
			if (!validRateCard) {
				errorLbl.setText(Application.getInstance().getMessage("fms.facility.msg.rateCardInvalid"));
			} else {
				errorLbl.setText("");
			}
			addChild(errorLbl);
			
			serviceLbl = new Label("servicelbl");
			serviceLbl.setText(Application.getInstance().getMessage(rc.getServiceType()));
			addChild(serviceLbl);
			
			effectiveDateLbl = new Label("effectiveDateLbl");
			effectiveDateLbl.setText(DateUtil.formatDate(SetupModule.DATE_FORMAT, rc.getEffectiveDate()));
			addChild(effectiveDateLbl);
			
			internalRateLbl = new Label("internalRateLbl");
			internalRateLbl.setText(rc.getInternalRate());
			addChild(internalRateLbl);
			
			externalRateLbl = new Label("externalRateLbl");
			externalRateLbl.setText(rc.getExternalRate());
			addChild(externalRateLbl);
			
			descriptionLbl = new Label("descriptionLbl");
			descriptionLbl.setText(rc.getDescription());
			addChild(descriptionLbl);
			
			remarksLbl = new Label("remarksLbl");
			remarksLbl.setText(rc.getRemarksRequest());
			addChild(remarksLbl);
			
			statusLbl = new Label("statusLbl");
			statusLbl.setText(rc.getStatus().equals("1")?"Active":"Inactive");
			addChild(statusLbl);
			
			transportRequestLbl = new Label("transportRequestLbl");
			transportRequestLbl.setText(((rc.getTransportRequest()==null)||rc.getTransportRequest().equals("0"))?
					Application.getInstance().getMessage("fms.tran.setup.transportRequestNo", "No"):Application.getInstance().getMessage("fms.tran.setup.transportRequestYes", "Yes"));
			addChild(transportRequestLbl);
			
			editButton.setText(Application.getInstance().getMessage("fms.facility.edit", "Edit"));
			setRateButton.setText(Application.getInstance().getMessage("fms.facility.setRate", "Set Rate"));		    
			
			abwCodeLbl = new Label("abwCodeLbl");
			if(rc.getAbwCode() == null){
				rc.setAbwCode("");
			}
			if(rc.getAbwCodeDesc() == null){
				rc.setAbwCodeDesc("");
			}
			abwCodeLbl.setText(rc.getAbwCode()+" - "+rc.getAbwCodeDesc());
			addChild(abwCodeLbl);
		} catch(Exception e){			
		}		   
	}
	
	public String getDefaultTemplate() {
		if (getId() != null){
			return "fms/ratecardformview";
		} else {
			return "fms/ratecardform";
		}
    }
	
	public void onRequest(Event evt){
		this.setInvalid(false);
		id = evt.getRequest().getParameter("id");
	
		Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
		facilities = module.getRateCardEquipmentForChecking(id);
		manpower = module.getRateCardManpowerForChecking(id);
		needCategory = "N";
		
		if (getId() != null){		
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
	    } else if (buttonName != null && editButton.getAbsoluteName().equals(buttonName)) {
	    	return new Forward("Edit", getEditUrl() + getId() , true);
		} else if (buttonName != null && setRateButton.getAbsoluteName().equals(buttonName)){
			return new Forward("SetRate", getSetRateUrl() + getId(), true);
		} else {
	    	return result;
	    }	    
	}
	
	public Forward onValidate(Event evt){
		Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
		RateCard rc = new RateCard();
		
		rc.setId(UuidGenerator.getInstance().getUuid());
		rc.setName((String)rateCardName.getValue());
		rc.setServiceTypeId(WidgetUtil.getSbValue(serviceType));
		rc.setEffectiveDate(dpfEffectiveDate.getDate());
		rc.setInternalRate((String)internalRate.getValue());
		rc.setExternalRate((String)externalRate.getValue());
		rc.setDescription((String)description.getValue());
		rc.setRemarksRequest((String)remarksRequestor.getValue());
		rc.setStatus("0"); // hardcode status to inactive
		rc.setAbwCode(WidgetUtil.getSbValue(abwCode));
		
		if(module.isDuplicate("fms_rate_card", "name", rc.getName(), null, null)){
			rateCardName.setInvalid(true);
			return new Forward("EXISTS");
		}
		
		if (trYes.isChecked()){
			rc.setTransportRequest("1");
			if ("-1".equals(WidgetUtil.getSbValue(category))){
				
				// 'needCategory' variable is needed to initiate 'javascript' on 'ratecardform.jsp'
				needCategory = "Y";
				category.setInvalid(true);
				return new Forward("CATEGORY_COMPULSORY");
			} else {
				rc.setVehicleCategoryId(WidgetUtil.getSbValue(category));
			}
		} else {
			rc.setTransportRequest("0");
			rc.setVehicleCategoryId("");
		}
				
		module.insertRateCard(rc);
		
		Forward fwd = new Forward("continue", "rateCardContinueSetup.jsp?id=" + rc.getId(), true);
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
	public RichTextBox getDescription() {
		return description;
	}
	public void setDescription(RichTextBox description) {
		this.description = description;
	}
	public RichTextBox getRemarksRequestor() {
		return remarksRequestor;
	}
	public void setRemarksRequestor(RichTextBox remarksRequestor) {
		this.remarksRequestor = remarksRequestor;
	}
	
	public CheckBox getTransportRequest() {
		return transportRequest;
	}

	public void setTransportRequest(CheckBox transportRequest) {
		this.transportRequest = transportRequest;
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

	public Collection getFacilities() {
		return facilities;
	}

	public void setFacilities(Collection facilities) {
		this.facilities = facilities;
	}

	public Collection getManpower() {
		return manpower;
	}

	public void setManpower(Collection manpower) {
		this.manpower = manpower;
	}

	public Label getStatusLbl() {
		return statusLbl;
	}

	public void setStatusLbl(Label statusLbl) {
		this.statusLbl = statusLbl;
	}

	public Label getNameLbl() {
		return nameLbl;
	}

	public void setNameLbl(Label nameLbl) {
		this.nameLbl = nameLbl;
	}

	public Label getServiceLbl() {
		return serviceLbl;
	}

	public void setServiceLbl(Label serviceLbl) {
		this.serviceLbl = serviceLbl;
	}

	public Label getEffectiveDateLbl() {
		return effectiveDateLbl;
	}

	public void setEffectiveDateLbl(Label effectiveDateLbl) {
		this.effectiveDateLbl = effectiveDateLbl;
	}

	public Label getInternalRateLbl() {
		return internalRateLbl;
	}

	public void setInternalRateLbl(Label internalRateLbl) {
		this.internalRateLbl = internalRateLbl;
	}

	public Label getExternalRateLbl() {
		return externalRateLbl;
	}

	public void setExternalRateLbl(Label externalRateLbl) {
		this.externalRateLbl = externalRateLbl;
	}

	public Label getDescriptionLbl() {
		return descriptionLbl;
	}

	public void setDescriptionLbl(Label descriptionLbl) {
		this.descriptionLbl = descriptionLbl;
	}

	public Label getRemarksLbl() {
		return remarksLbl;
	}

	public void setRemarksLbl(Label remarksLbl) {
		this.remarksLbl = remarksLbl;
	}

	public Label getTransportRequestLbl() {
		return transportRequestLbl;
	}

	public void setTransportRequestLbl(Label transportRequestLbl) {
		this.transportRequestLbl = transportRequestLbl;
	}

	public Panel getPnType() {
		return pnType;
	}

	public void setPnType(Panel pnType) {
		this.pnType = pnType;
	}

	public SelectBox getCategory() {
		return category;
	}

	public void setCategory(SelectBox category) {
		this.category = category;
	}

	public String getNeedCategory() {
		return needCategory;
	}

	public void setNeedCategory(String needCategory) {
		this.needCategory = needCategory;
	}

	public Label getAbwCodeLbl() {
		return abwCodeLbl;
	}

	public void setAbwCodeLbl(Label abwCodeLbl) {
		this.abwCodeLbl = abwCodeLbl;
	}
	
	
}
