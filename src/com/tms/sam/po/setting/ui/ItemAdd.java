package com.tms.sam.po.setting.ui;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.setting.model.ItemObject;
import com.tms.sam.po.setting.model.SettingModule;

public class ItemAdd extends Item{
	public void init(){
		super.init();
		itemCode.addChild(vne);
	}
	
	public Forward onValidate(Event event) {
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit")) {
            // process upload
            addItem(); 
            return new Forward(FORWARD_ITEM);
        }
        else{
        	return new Forward(FORWARD_BACK);
        }
		
	}
	
	
	public Forward onSubmit(Event evt) {
		Forward forward= super.onSubmit(evt);
		
		String button = findButtonClicked(evt);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit")) {

    		String selectedCategoryId=itemCategory.getSelectedOptions().keySet().iterator().next().toString();
    		if(selectedCategoryId == null || selectedCategoryId.equals("")){
    			itemCategory.setInvalid(true);
    			setInvalid(true);
    		}
        }
		
		return forward;
	}
	
	public Forward addItem(){
		Application app = Application.getInstance();
		SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
		ItemObject obj = new ItemObject();
		obj.setItemCode(itemCode.getValue().toString());
		obj.setItemDesc(itemDesc.getValue().toString());
		obj.setCategoryID(itemCategory.getSelectedOptions().keySet().iterator().next().toString());
		if(approved.isChecked()){
			obj.setApproved(true);
		}else{
			obj.setApproved(false);
		}
		obj.setUnitOfMeasure(unitOfMeasure.getValue().toString());
		obj.setMinQty(Double.parseDouble(minAmount.getValue().toString()));
		obj.setCreatedBy(app.getCurrentUser().getId());
		obj.setLastUpdatedBy(app.getCurrentUser().getId());
		
	
		module.addItem(obj);
		super.removeChildren();
	    super.init();
		return new Forward(FORWARD_BACK);
      
	}
}
