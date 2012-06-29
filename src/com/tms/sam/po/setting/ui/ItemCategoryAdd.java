package com.tms.sam.po.setting.ui;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.sam.po.setting.model.CategoryObject;
import com.tms.sam.po.setting.model.SettingModule;

public class ItemCategoryAdd extends ItemCategory{
	public void init(){
		
		super.init();
		category.addChild(validateUniqueGroupName);
	}
	
	public Forward onValidate(Event event){
		boolean checkingResult = false;
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
		
        if (button.endsWith("submit")) {
            // process upload
          checkingResult = addCategory();    	
        }
        if (checkingResult){
        	return new Forward(FORWARD_BACK);
        }
        else{
            return new Forward(FORWARD_CATEGORY);
        }
	}
	public boolean addCategory(){
		Application app = Application.getInstance();
		SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
		CategoryObject obj = new CategoryObject();
		String categoryID = UuidGenerator.getInstance().getUuid();
		obj.setCategoryID(categoryID);
		obj.setCategory(category.getValue().toString());
		obj.setCreatedBy(app.getCurrentUser().getId());
		obj.setLastUpdatedBy(app.getCurrentUser().getId());
		
		
		boolean result = module.addCategory(obj);;
		super.removeChildren();
        super.init();
		if(result){
			return true;
		}
		else{
			return false;
		}
	}
}
