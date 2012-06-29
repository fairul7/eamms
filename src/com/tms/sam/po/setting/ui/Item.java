package com.tms.sam.po.setting.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.setting.model.CategoryObject;
import com.tms.sam.po.setting.model.SettingModule;

public abstract class Item extends Form{
	protected SelectBox itemCategory;
	
	protected TextField itemCode;
	protected TextField unitOfMeasure;
	protected TextField minAmount;
	protected TextField itemDesc;
	
	protected CheckBox approved;
	
	protected Button btnSubmit;
	protected Button btnCancel;
	
	protected ValidatorNotEmpty vne;
	protected ValidatorUniqueCodeName validateUniqueCodeName;
	protected Panel btnPanel;
	
	protected static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_BACK = "back";	
	public static final String FORWARD_ITEM = "item";
	public void init() {
        setMethod("POST");
        setColumns(2);
        
        Application app = Application.getInstance();
        SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
        
        vne = new ValidatorNotEmpty("vne", "");
        
        itemCategory = new SelectBox("itemCategory");
        itemCategory.addOption("", Application.getInstance().getMessage("po.label.pleaseSelect","--Please Select--"));
        Collection cat = module.getCategory();
        for( Iterator i=cat.iterator(); i.hasNext(); ){
        	CategoryObject o = (CategoryObject) i.next();
        	itemCategory.addOption(o.getCategoryID(), o.getCategory());
        }
        
        itemCategory.addChild(vne);
        itemCode = new TextField("itemCode");
        validateUniqueCodeName = new ValidatorUniqueCodeName("validateUniqueCodeName", Application.getInstance().getMessage("po.validator.countryNameNotUnique")); 
    	itemCode.addChild(validateUniqueCodeName);
        
        ValidatorIsNumeric vin = new ValidatorIsNumeric("vin", Application.getInstance().getMessage("po.message.integerOnly"));
        
        unitOfMeasure = new TextField("unitOfMeasure");
        unitOfMeasure.addChild(vne);
        minAmount = new TextField("minAmount");
        minAmount.addChild(vin);
        itemDesc = new TextField("itemDesc");
        itemDesc.addChild(vne);
        
        approved = new CheckBox("approved");
        approved.setMouseOverText(app.getMessage("po.label.approved","Approved"));
        approved.setChecked(true);
        
        btnPanel = new Panel("btnPanel");
        btnPanel.setColumns(2);
        
        btnSubmit = new Button("submit", app.getMessage("po.label.submit"));
        btnPanel.addChild(btnSubmit);
        
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("po.label.cancel"));
        btnPanel.addChild(btnCancel);
        
        
		addChild(itemCategory);
		addChild(itemCode);
		addChild(itemDesc);
        addChild(unitOfMeasure);
        addChild(minAmount);
        addChild(approved);       
      
        addChild(btnPanel);
	}
	
	public void onRequest(Event evt) {
        init();
    }
	
    @Override
    public Forward onSubmit(Event evt) {
    	Forward forward = super.onSubmit(evt);
    	if(itemCategory.getSelectedOptions().keySet().iterator().next().toString().equals("")){
    		itemCategory.setInvalid(true);
    		this.setInvalid(true);
    	}
    	return forward;
    }
    public Forward onValidate(Event event) {
        super.removeChildren();
        super.init();
        
        return new Forward(FORWARD_SUCCESS);
    }
    
    public String getTemplate() {
		return "po/addItem";
	}

	public CheckBox getApproved() {
		return approved;
	}

	public void setApproved(CheckBox approved) {
		this.approved = approved;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public SelectBox getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(SelectBox itemCategory) {
		this.itemCategory = itemCategory;
	}

	public TextField getItemCode() {
		return itemCode;
	}

	public void setItemCode(TextField itemCode) {
		this.itemCode = itemCode;
	}

	public TextField getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(TextField itemDesc) {
		this.itemDesc = itemDesc;
	}

	public TextField getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(TextField minAmount) {
		this.minAmount = minAmount;
	}

	public TextField getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(TextField unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public ValidatorNotEmpty getVne() {
		return vne;
	}

	public void setVne(ValidatorNotEmpty vne) {
		this.vne = vne;
	}

	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}
    
	protected class ValidatorUniqueCodeName extends Validator {
		private String oriValue = null;
		
		public ValidatorUniqueCodeName(String name, String text) {
	        super(name);
	        setText(text);
	    }

		public String getOriValue() {
			return oriValue;
		}

		public void setOriValue(String oriValue) {
			this.oriValue = oriValue;
		}
		
		public boolean validate(FormField formField) {
			boolean validationPass = true;
			
			Object value = formField.getValue();
	        if(value != null)
	        {
	            String strValue = null;
	            if(value instanceof Collection)
	            {
	                if(((Collection) value).size() > 0)
	                	strValue = (String) ((Collection) value).iterator().next();
	            }
	            else if(value instanceof Map)
	            {
	                if(((Map) value).keySet().size() > 0)
	                	strValue = (String) ((Map) value).keySet().iterator().next();
	            }
	            else if(value instanceof String)
	            	strValue = (String) value;
	            
	            if(strValue != null && 
	            		!"".equals(strValue)) {
	            	if(! strValue.toLowerCase().equals(oriValue)) {
	            		SettingModule model = (SettingModule)Application.getInstance().getModule(SettingModule.class);
	            		if(model.isCodeNameExist(strValue)) {
	            			validationPass = false;
	            		}
	            	}
	            }
	        }
	        
	        return validationPass;
	    }
	}
}
