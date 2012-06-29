package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.RichTextBox;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.axis.collections.SequencedHashMap;

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
public class RateCardEditForm extends Form {
	protected String id;   
	protected String statusValue;
	
	protected TextField rateCardName;
	protected SelectBox serviceType;
	protected SelectBox requestType;
	protected DatePopupField dpfEffectiveDate;
	protected TextField internalRate;
	protected TextField externalRate;	
	protected RichTextBox description;
	protected RichTextBox remarksRequestor;
	protected CheckBox transportRequest;
	protected Hidden stat;	
	protected TextField[] equipmentQty;
	protected TextField[] manpowerQty;
	private Panel pnType;
	protected Radio trYes;
	protected Radio trNo;
	private SelectBox category;
	private String needCategory;
	
	protected String idEquipment;
	protected String idManpower;
	
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

	private Collection facilities = new ArrayList();
	private Collection manpower = new ArrayList();
	
	protected SelectBox abwCode;
	
	
	public RateCardEditForm() {

    }
   
	public void init()
	{
		setColumns(1);
		
		rateCardName = new TextField("rateCardName");		
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
		
		description = new RichTextBox("description");
		description.setCols("8");
		description.setRows("10"); 	        
		addChild(description);
		
		remarksRequestor = new RichTextBox("remarksRequestor");
		remarksRequestor.setCols("8");
		remarksRequestor.setRows("10");
		addChild(remarksRequestor);
		
		pnType = new Panel("pnType");
		trYes = new Radio("trYes", Application.getInstance().getMessage("fms.tran.setup.transportRequestYes", "Yes"));
		trNo = new Radio("trNo", Application.getInstance().getMessage("fms.tran.setup.transportRequestNo", "No"));
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
		
		stat = new Hidden("stat");
		addChild(stat);
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("fms.facility.done", "Done"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));        
        
        addChild(submitButton);
		addChild(cancelButton);	
		
		abwCode = new SelectBox("abwCode");		
		int j=0;
		SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		SequencedHashMap abwCodeMap = sm.getAbwCode();
		if(abwCodeMap != null){
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
			
			serviceType.addSelectedOption(rc.getServiceTypeId());
			
			description.setValue(rc.getDescription());
			
			remarksRequestor.setValue(rc.getRemarksRequest());						
			
			//transportRequest.setChecked("1".equals(rc.getTransportRequest()));
			if ("1".equals(rc.getTransportRequest())){
				trYes.setChecked(true);
				category.addSelectedOption(rc.getVehicleCategoryId());
				needCategory = "Y";
			} else {
				trNo.setChecked(true);
			}
			
			statusValue = rc.getStatus();
			stat.setValue(statusValue);		
			
			abwCode.addSelectedOption(rc.getAbwCode());
			
		} catch(Exception e){
		}
		
	}
	
	public String getDefaultTemplate() {
		if (getId() != null){
			return "fms/ratecardformedit";
		} else {
			return "fms/ratecardform";
		}
    }
	
	public void onRequest(Event evt){
		
		id = evt.getRequest().getParameter("id");
		action = evt.getRequest().getParameter("do");		
		idEquipment = evt.getRequest().getParameter("idEquipment");
		idManpower = evt.getRequest().getParameter("idManpower");
		needCategory = "N";
		
		Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
		
		init();
		if (getId() != null){
			if (getAction() != null){
				if (getAction().equals("deleteEquip") && getIdEquipment() != null){
					module.deleteRateCardEquipment(getId(), getIdEquipment());
				} else if (getAction().equals("deleteManpower") && getIdManpower() != null){
					module.deleteRateCardManpower(getId(), getIdManpower());
				}				
			}
			facilities = module.getRateCardEquipmentForChecking(id);
			manpower = module.getRateCardManpowerForChecking(id);
			populateEquipmentQty();
			populateManpowerQty();
			populateForm(getId());
		} else {
			//init();
		}
	}

	private void populateEquipmentQty(){
		equipmentQty = new TextField[facilities.size()];
		int count = 0;
		
		for(Iterator itr=facilities.iterator();itr.hasNext();count++){
			RateCard rc = (RateCard)itr.next();
			equipmentQty[count] = new TextField("tfEquipmentQty"+count);
			equipmentQty[count].setSize("6");
			equipmentQty[count].setValue(rc.getEquipmentQty());
			equipmentQty[count].setAttribute("idEquipment", rc.getIdEquipment());
			addChild(equipmentQty[count]);
		}
	}
	
	private void populateManpowerQty(){
		manpowerQty = new TextField[manpower.size()];
		int count = 0;
		
		for(Iterator itr=manpower.iterator();itr.hasNext();count++){
			RateCard rc = (RateCard)itr.next();
			manpowerQty[count] = new TextField("tfManpowerQty"+count);
			manpowerQty[count].setSize("6");
			manpowerQty[count].setValue(rc.getManpowerQty());
			manpowerQty[count].setAttribute("idManpower", rc.getIdManpower());
			addChild(manpowerQty[count]);
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
		rc.setName((String)rateCardName.getValue());
		rc.setServiceTypeId((String) serviceType.getSelectedOptions().keySet().iterator().next());
		rc.setDescription((String)description.getValue());
		rc.setRemarksRequest((String)remarksRequestor.getValue());
		rc.setAbwCode((String) abwCode.getSelectedOptions().keySet().iterator().next());
		
		if (trYes.isChecked()){
			rc.setTransportRequest("1");
			if ("-1".equals(WidgetUtil.getSbValue(category))){
				
				// 'needCategory' variable is needed to initiate 'javascript' on 'ratecardeditform.jsp'
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
		
		for(int count=0;count<equipmentQty.length;count++){
			String idEquipment = (String)equipmentQty[count].getAttribute("idEquipment");
			String quantity = (String)equipmentQty[count].getValue();
			
			module.updateRateCard(idEquipment, quantity);
		}
		
		for(int countManpower=0;countManpower<manpowerQty.length;countManpower++){
			String idManpower = (String)manpowerQty[countManpower].getAttribute("idManpower");
			String qtyManpower = (String)manpowerQty[countManpower].getValue();
			
			module.updateRateCardManpower(idManpower, qtyManpower);
		}
		
		Forward fwd = new Forward("continue");
		
		// validate rate card
		boolean validRateCard = module.isValidRateCard(rc.getId());
		if (!validRateCard) {
			statusValue = "0";
			fwd = new Forward("invalidRateCard");
		}
		rc.setStatus(statusValue);
		
		module.updateRateCard(rc);
		if(statusValue.equals("1"))
		{
			rc.setName(nameLbl.getText());
			module.pushToAbwServer(rc);
		}
		else
		{
			//#13014 send email for inactive updates
			DefaultDataObject obj = new DefaultDataObject();
			obj.setId(rc.getId());
			obj.setProperty("abwCode", rc.getAbwCode());
			obj.setProperty("rate_card_name", nameLbl.getText());
			
			module.insertRateCardEmailNotification(obj);
		}
		
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

	public void setDescription(RichTextBox description) {
		this.description = description;
	}

	public RichTextBox getRemarksRequestor() {
		return remarksRequestor;
	}

	public void setRemarksRequestor(RichTextBox remarksRequestor) {
		this.remarksRequestor = remarksRequestor;
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

	public String getIdEquipment() {
		return idEquipment;
	}

	public void setIdEquipment(String idEquipment) {
		this.idEquipment = idEquipment;
	}

	public String getIdManpower() {
		return idManpower;
	}

	public void setIdManpower(String idManpower) {
		this.idManpower = idManpower;
	}

	public TextField[] getEquipmentQty() {
		return equipmentQty;
	}

	public void setEquipmentQty(TextField[] equipmentQty) {
		this.equipmentQty = equipmentQty;
	}

	public TextField[] getManpowerQty() {
		return manpowerQty;
	}

	public void setManpowerQty(TextField[] manpowerQty) {
		this.manpowerQty = manpowerQty;
	}

	public CheckBox getTransportRequest() {
		return transportRequest;
	}

	public void setTransportRequest(CheckBox transportRequest) {
		this.transportRequest = transportRequest;
	}

	public SelectBox getServiceType() {
		return serviceType;
	}

	public void setServiceType(SelectBox serviceType) {
		this.serviceType = serviceType;
	}

	public Label getNameLbl() {
		return nameLbl;
	}

	public void setNameLbl(Label nameLbl) {
		this.nameLbl = nameLbl;
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

	public SelectBox getAbwCode() {
		return abwCode;
	}

	public void setAbwCode(SelectBox abwCode) {
		this.abwCode = abwCode;
	}

}
