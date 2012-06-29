package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorLength;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;

import com.tms.fms.facility.ui.FacilitiesPopupSelectBox;

/**
 * @author fahmi
 *
 */

@SuppressWarnings("serial")
public class RateCardCategoryForm extends Form {
    
	protected String id;   
	protected TextField categoryName;
	protected ComboSelectBox items;
	private FacilitiesPopupSelectBox fpsbFacility;
	private String facility_id[];
	
	private Label nameLbl;	
	
	protected Button submitButton;
	protected Button cancelButton;
	protected Button editButton;
	protected Button setRateButton;
	protected Button backButton;
	
	private String cancelUrl = "itemCategoryList.jsp";
	private String editUrl = "rateCardEdit.jsp?id=";
	
	private String action = "";
	
	public RateCardCategoryForm() {
    }
   
	@SuppressWarnings("unchecked")
	public void init()
	{
		String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");
		setColumns(1);
		
		categoryName = new TextField("categoryName");
		categoryName.setSize("30");     
		categoryName.setMaxlength("500");
		categoryName.addChild(new ValidatorNotEmpty("rcEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		categoryName.addChild(new ValidatorLength("cnMaxLength", "Maximum value is 500 character", 0, 500));
		addChild(categoryName);
		
//		items = new ComboSelectBox("items");
//		addChild(items);
//		items.init();
//		initItems();
		
		addChild(new Label("lb6", Application.getInstance().getMessage("fms.facility.form.selectFacilityEquipment", "Select Facility / Equipment")));
	    fpsbFacility = new FacilitiesPopupSelectBox("fpsbFacility");
	    fpsbFacility.setSortable(false);
	    addChild(fpsbFacility);
	    fpsbFacility.init();
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("fms.facility.submit", "Submit"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
        
        editButton = new Button("editButton");     
        
		addChild(submitButton);
		addChild(cancelButton);
		addChild(editButton);
	}	 
	
	public String getDefaultTemplate() {
		return "fms/ratecardcatform";
    }
	
	public void onRequest(Event evt){
		this.setInvalid(false);
		id = evt.getRequest().getParameter("id");
		
		Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
		
		init();		
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
		facility_id = fpsbFacility.getIds();
		
		rc.setIdCategory(UuidGenerator.getInstance().getUuid());
		rc.setCategoryName((String)categoryName.getValue());
		//rc.setCategoryItems((List)items.getRightSelect().getValue());
		rc.setEquipments(facility_id);
	
		if(module.isDuplicate("fms_eng_rate_card_category", "name", rc.getCategoryName(), null, null)){
			categoryName.setInvalid(true);
			return new Forward("EXISTS");
		}
		if (rc.getEquipments().length <=0){
			fpsbFacility.setInvalid(true);
			return new Forward("ITEMS_COMPULSORY");
		}
		
		module.insertRateCardCategory(rc);
		
		Forward fwd = new Forward("SAVE");
		init();
		return fwd;
	}		
	
	public void initItems() {
        Map map = getItemList();
        if (!(map.isEmpty()))
            items.setLeftValues(map);
    }

    private Map getItemList() {
    	Application application = Application.getInstance();
		SetupModule module = (SetupModule)application.getModule(SetupModule.class);
        Map map = new SequencedHashMap();
        Collection list = new ArrayList();
        
        try {
            list = module.getRateCardAllEquipment(null, null, false, 0, -1); 
            for (Iterator i = list.iterator(); i.hasNext();) {
            	RateCard rc = (RateCard) i.next();
            	map.put(rc.getIdEquipment(), rc.getEquipment());
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        return map;
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

	public String[] getFacility_id() {
		return facility_id;
	}

	public void setFacility_id(String[] facility_id) {
		this.facility_id = facility_id;
	}
	
}
