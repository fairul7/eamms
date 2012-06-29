package com.tms.fms.facility.ui;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;

/**
 * @author fahmi
 *
 */

@SuppressWarnings("serial")
public class RateCardCategoryEditForm extends Form {
    
	protected String id;   
	protected TextField categoryName;
	protected ComboSelectBox items;
	private FacilitiesPopupSelectBox fpsbFacility;
	
	private Label nameLbl;
	private Label errorLbl;
	
	protected Button submitButton;
	protected Button cancelButton;
	protected Button editButton;
	protected Button setRateButton;
	protected Button backButton;
	
	private String cancelUrl = "itemCategoryList.jsp";
	private String editUrl = "rateCardEdit.jsp?id=";
	
	private String action = "";
	
	public RateCardCategoryEditForm() {
    }
   
	@SuppressWarnings("unchecked")
	public void init()
	{
		setMethod("POST");
		setColumns(1);
		
//		items = new ComboSelectBox("items");
//		addChild(items);
//		items.init();
		
		fpsbFacility = new FacilitiesPopupSelectBox("fpsbFacility");
		fpsbFacility.init();
		addChild(fpsbFacility);
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("fms.facility.submit", "Submit"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
        
        editButton = new Button("editButton");     
        
		addChild(submitButton);
		addChild(cancelButton);
		addChild(editButton);
	}	 
	
	public void populateForm(String id){
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try{
			RateCard rc = module.getRateCardCategory(id);
			nameLbl = new Label("nameLbl");
			nameLbl.setText(rc.getCategoryName());
			addChild(nameLbl);
			
			boolean validCategory = module.isValidRateCardCategory(id);
			errorLbl = new Label("errorLbl");
			if (!validCategory) {
				errorLbl.setText(Application.getInstance().getMessage("fms.facility.msg.rateCardCategoryInvalid"));
			} else {
				errorLbl.setText("");
			}
			addChild(errorLbl);
		
			String[] item = new String[rc.getEquipments().length];
			item = rc.getEquipments();
			fpsbFacility.setIds(item);
			
			editButton.setText(Application.getInstance().getMessage("fms.facility.edit", "Edit"));			
		} catch(Exception e){			
		}		   
	}
	
	public String getDefaultTemplate() {
		if (getId() != null){
			return "fms/ratecardcatformedit";
		} else {
			return "fms/ratecardcatform";
		}
    }
	
	public void onRequest(Event evt){
		this.setInvalid(false);
		id = evt.getRequest().getParameter("id");
		
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
		} else {
	    	return result;
	    }	    
	}
	
	public Forward onValidate(Event evt){
		Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
		RateCard rc = new RateCard();
		
		rc.setIdCategory(getId());
		//rc.setCategoryItems((List)items.getRightSelect().getValue());
		rc.setEquipments(fpsbFacility.getIds());
		
		//if (rc.getCategoryItems().size() <=0){
		if (rc.getEquipments().length <=0){
			//items.setInvalid(true);
			fpsbFacility.setInvalid(true);
			return new Forward("ITEMS_COMPULSORY");
		}
		
		module.updateRateCardCategory(rc);
		
		Forward fwd = new Forward("SAVE");
		init();
		return fwd;
	}		
	
    public void initItemsEdit(String categoryId) {
    	Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
        Map leftMap = new SequencedHashMap();
        Map rightMap = new SequencedHashMap();
        Collection leftList = new ArrayList();
        Collection rightList = new ArrayList();
    	
        try {
        	
        	rightList = module.getRateCardCategoryItems(categoryId, "");
        	for (Iterator y = rightList.iterator(); y.hasNext();){
        		RateCard rcR = (RateCard) y.next();
        		rightMap.put(rcR.getIdEquipment(), rcR.getEquipment());
        		
            	leftList = module.getFacility(rcR.getIdEquipment(), "!=");
            	for (Iterator x = leftList.iterator(); x.hasNext();){
            		RateCard rcL = (RateCard) x.next();
            		leftMap.put(rcL.getIdEquipment(), rcL.getEquipment());
            	}
            	
        	}
        	if (!(leftMap.isEmpty())){
        		items.setLeftValues(leftMap);
        	}
        	
        	if (!(rightMap.isEmpty())){
        		items.setRightValues(rightMap);
        	}
        } catch (Exception e){
        	Log.getLog(getClass()).error(e.toString(), e);
        }

    }
	
	public Label getNameLbl() {
		return nameLbl;
	}

	public void setNameLbl(Label nameLbl) {
		this.nameLbl = nameLbl;
	}

	public TextField getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(TextField categoryName) {
		this.categoryName = categoryName;
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

	public ComboSelectBox getItems() {
		return items;
	}

	public void setItems(ComboSelectBox items) {
		this.items = items;
	}

	public FacilitiesPopupSelectBox getFpsbFacility() {
		return fpsbFacility;
	}

	public void setFpsbFacility(FacilitiesPopupSelectBox fpsbFacility) {
		this.fpsbFacility = fpsbFacility;
	}
	
}
