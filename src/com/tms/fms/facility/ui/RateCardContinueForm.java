package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;

/**
 * @author fahmi
 *
 */

@SuppressWarnings("serial")
public class RateCardContinueForm extends Form {
    
	protected String id;   
	protected TextField[] equipmentQty;
	protected TextField[] manpowerQty;
	
	protected String idEquipment;
	protected String idManpower;
	
	private Label nameLbl;	
	private Label serviceLbl;
	private Label effectiveDateLbl;
	private Label internalRateLbl;
	private Label externalRateLbl;
	private Label descriptionLbl;
	private Label remarksLbl;
	private Label statusLbl;
	
	protected Button submitButton;
	protected Button cancelButton;
	protected Button doneButton;
	protected Button addFacilitesButton;
	protected Button addManpowerButton;
		
	private String cancelUrl = "rateCardList.jsp";
	private String doneUrl = "rateCardList.jsp";
	private String setRateUrl = "rateCardSetRate.jsp?id=";
	
	private String action = "";

	private Collection facilities = new ArrayList();
	private Collection manpower = new ArrayList();
	
	private Label abwCodeLbl;
	
	public RateCardContinueForm() {
    }
   
	@SuppressWarnings("unchecked")
	public void init()
	{
		setColumns(1);
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		setMethod("POST");	
	    
		nameLbl = new Label("namelbl");
		addChild(nameLbl);

		serviceLbl = new Label("servicelbl");
		addChild(serviceLbl);		

		effectiveDateLbl = new Label("effectiveDateLbl");
		addChild(effectiveDateLbl);
		
		internalRateLbl = new Label("internalRateLbl");
		addChild(internalRateLbl);
				
		externalRateLbl = new Label("externalRateLbl");
		addChild(externalRateLbl);
		
		descriptionLbl = new Label("descriptionLbl");
		addChild(descriptionLbl);		

		remarksLbl = new Label("remarksLbl");
		addChild(remarksLbl);		

		statusLbl = new Label("statusLbl");
		addChild(statusLbl);			
		
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
        
        doneButton = new Button("doneButton",Application.getInstance().getMessage("fms.facility.done", "Done"));
        
		addChild(cancelButton);
		addChild(doneButton);
		
		abwCodeLbl = new Label("abwCodeLbl");
		addChild(abwCodeLbl);	
	}	 
	
	public void populateForm(String id){
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try{
			RateCard rc = module.getRateCard(id);
			
			nameLbl.setText(rc.getName());			
			
			serviceLbl.setText(Application.getInstance().getMessage(rc.getServiceType()));			
			
			effectiveDateLbl.setText(DateUtil.formatDate(SetupModule.DATE_FORMAT, rc.getEffectiveDate()));			
			
			internalRateLbl.setText(rc.getInternalRate());
			
			externalRateLbl.setText(rc.getExternalRate());			
			
			descriptionLbl.setText(rc.getDescription());
			
			remarksLbl.setText(rc.getRemarksRequest());
			
			statusLbl.setText(rc.getStatus());
			
			abwCodeLbl.setText(rc.getAbwCodeDesc());			
			
		} catch(Exception e){			
		}		   
	}
	
	public String getDefaultTemplate() {
		if (getId() != null){
			return "fms/ratecardcontview";
		} else {
			return "fms/ratecardform";
		}
    }
	
	public void onRequest(Event evt){
		this.setInvalid(false);
		
		id = evt.getRequest().getParameter("id");
		action = evt.getRequest().getParameter("do");		
		idEquipment = evt.getRequest().getParameter("idEquipment");
		idManpower = evt.getRequest().getParameter("idManpower");
		
		Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
				
		if (getId() != null){
			if (getAction() != null){
				if (getAction().equals("deleteEquip") && getIdEquipment() != null){
					module.deleteRateCardEquipment(getId(), getIdEquipment());
				} else if (getAction().equals("deleteManpower") && getIdManpower() != null){
					module.deleteRateCardManpower(getId(), getIdManpower());
				}				
			}
			
			facilities = module.getRateCardEquipment(id, "");
			manpower = module.getRateCardManpower(id, "");
			
			populateEquipmentQty();
			populateManpowerQty();
			populateForm(getId());
		} else {
			init();
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

	public Forward onSubmit(Event evt) {
		Forward result = super.onSubmit(evt);
		
		String buttonName = findButtonClicked(evt);
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
		boolean validRateCard = module.isValidRateCard(getId());
		if (!validRateCard) {
			fwd = new Forward("invalidRateCard");
		}
		
		init();
		return fwd;
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
	public String getDoneUrl() {
		return doneUrl;
	}
	public void setDoneUrl(String doneUrl) {
		this.doneUrl = doneUrl;
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

	public Button getDoneButton() {
		return doneButton;
	}

	public void setDoneButton(Button doneButton) {
		this.doneButton = doneButton;
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
	
}
