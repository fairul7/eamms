package com.tms.sam.po.setting.ui;

import java.util.Collection;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.setting.model.SettingModule;

public abstract class ItemCategory extends Form{
	protected TextField category;
	
	protected Button btnSubmit;
	protected Button btnCancel;
	
	protected Panel btnPanel;
	
	protected ValidatorNotEmpty vne;
	protected ValidatorUniqueGroupName validateUniqueGroupName;
	
	protected static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_BACK = "back";	
	public static final String FORWARD_CATEGORY = "category";
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		removeChildren();
		setColumns(2);
		
		Application application = Application.getInstance();
		
		vne = new ValidatorNotEmpty("vne", "");
		validateUniqueGroupName = new ValidatorUniqueGroupName("validateUniqueGroupName", application.getMessage("po.validator.countryNameNotUnique"));
		
		category = new TextField("category");
		
		category.addChild(vne);
		
        btnPanel = new Panel("btnPanel");
        btnPanel.setColumns(2);
		btnSubmit = new Button("submit", application.getMessage("po.label.submit"));
	    btnPanel.addChild(btnSubmit);
	        
	    btnCancel = new Button(Form.CANCEL_FORM_ACTION, application.getMessage("po.label.cancel"));
	    btnPanel.addChild(btnCancel);
	    
		addChild(category);
		addChild(btnPanel);
		
	}
	
    public void onRequest(Event evt) {
        init();
    }
    
    public Forward onValidate(Event event) {
        super.removeChildren();
        super.init();
        
        return new Forward(FORWARD_SUCCESS);
    }
	
	public String getTemplate() {
		return "po/addCategory";
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public TextField getCategory() {
		return category;
	}

	public void setCategory(TextField category) {
		this.category = category;
	}

	public ValidatorNotEmpty getVne() {
		return vne;
	}

	public void setVne(ValidatorNotEmpty vne) {
		this.vne = vne;
	}
	
	protected class ValidatorUniqueGroupName extends Validator {
		private String oriValue = null;
		
		public ValidatorUniqueGroupName(String name, String text) {
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
	            		SettingModule module = (SettingModule)Application.getInstance().getModule(SettingModule.class);
	            		if(module.isCategoryExist(strValue)) {
	            			validationPass = false;
	            		}
	            	}
	            }
	        }
	        
	        return validationPass;
	    }
	}
}
