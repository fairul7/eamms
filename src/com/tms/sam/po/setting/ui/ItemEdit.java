package com.tms.sam.po.setting.ui;

import java.util.Date;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.setting.model.ItemObject;
import com.tms.sam.po.setting.model.SettingModule;

public class ItemEdit extends Item{
	private String code;
	public void init(){
		super.init();
	}
	
	public void onRequest(Event evt) {
        init();
        
        SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
        ItemObject obj = module.getOneItem(code);
     
        itemDesc.setValue(obj.getItemDesc());
        itemCategory.setSelectedOption(obj.getCategoryID());
        minAmount.setValue(obj.getMinQty());
        unitOfMeasure.setValue(obj.getUnitOfMeasure());
        if(obj.isApproved()){
        	approved.setChecked(true);
        }else{
        	approved.setChecked(false);
        }
        
      
        
    }
	
	public Forward onValidate(Event event) {
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit")) {
            // process upload
           editCategory(); 
           return new Forward(FORWARD_BACK);
            
        }else if(button.endsWith("cancel")){
        	return new Forward(FORWARD_BACK);
        }

		return new Forward(FORWARD_BACK);
	}

	public void editCategory(){
		Application app = Application.getInstance();
		Date now = new Date();
		SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
		ItemObject obj = new ItemObject();
		obj.setItemCode(code);
		obj.setCategoryID(itemCategory.getSelectedOptions().keySet().iterator().next().toString());
		obj.setItemDesc(itemDesc.getValue().toString());
		obj.setMinQty(Double.parseDouble(minAmount.getValue().toString()));
		obj.setUnitOfMeasure(unitOfMeasure.getValue().toString());
		if(approved.isChecked()){
			obj.setApproved(true);
		}else{
			obj.setApproved(false);
		}
		
		obj.setLastUpdatedBy(app.getCurrentUser().getId());
		obj.setLastUpdatedDate(now);
		module.editItem(obj);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTemplate() {
			return "po/editItem";
	}
	

}
