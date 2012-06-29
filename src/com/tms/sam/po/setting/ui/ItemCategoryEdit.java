package com.tms.sam.po.setting.ui;

import java.util.Date;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.setting.model.CategoryObject;
import com.tms.sam.po.setting.model.SettingModule;

public class ItemCategoryEdit extends ItemCategory{
	private String categoryID;
	
	public void init(){
		super.init();
	}
	
	public void onRequest(Event evt) {
        init();
        
        SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
        CategoryObject obj = module.getOneCategory(categoryID);
        category.setValue(obj.getCategory());
      
        
    }
	
	public Forward onValidate(Event event) {
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit")) {
            // process upload
           editCategory(); 
           return new Forward(FORWARD_BACK);
            
        }

		return new Forward(FORWARD_BACK);
	}

	public void editCategory(){
		Application app = Application.getInstance();
		Date now = new Date();
		SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
		CategoryObject obj = new CategoryObject();
		obj.setCategoryID(categoryID);
		obj.setCategory(category.getValue().toString());
		obj.setLastUpdatedBy(app.getCurrentUser().getId());
		obj.setLastUpdatedDate(now);
		module.editCategory(obj);
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	
}
