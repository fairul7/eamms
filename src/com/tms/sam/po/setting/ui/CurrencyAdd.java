package com.tms.sam.po.setting.ui;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.sam.po.setting.model.CurrencyObject;
import com.tms.sam.po.setting.model.SettingModule;

public class CurrencyAdd extends Currency{
	public void init(){
	    super.init();
		//currency.addChild(validateUniqueGroupName);
	}
	
	public Forward onValidate(Event event) {
		boolean checkingResult = false;
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit")) {
        	SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
        	if(module.isCurrencyNameExist(currency.getValue().toString(),null)){
    			currency.setInvalid(true);
    			return new Forward(FORWARD_RESULT);
    		}
           checkingResult = addCurrency();    	
        }
        if (checkingResult){
        	return new Forward(FORWARD_BACK);
        }
        else{
        	return new Forward(FORWARD_RESULT);
        }
		
	}
//	@Override
//	public Forward onSubmit(Event evt) {
//		SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
//		currency.getValue();
//		if(module.isCurrencyNameExist(currency.getValue().toString())){
//			currency.setInvalid(true);
//			this.setInvalid(true);
//			return new Forward(FORWARD_RESULT);
//		}
//		// TODO Auto-generated method stub
//		return super.onSubmit(evt);
//	}
	
	public boolean addCurrency(){
		Application app = Application.getInstance();
		SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
		CurrencyObject obj = new CurrencyObject();
		String currencyID = UuidGenerator.getInstance().getUuid();
		obj.setCurrencyID(currencyID);
		obj.setCountry(country.getValue().toString());
		obj.setCurrency(currency.getValue().toString());
		obj.setCreatedBy(app.getCurrentUser().getId());
		obj.setLastUpdatedBy(app.getCurrentUser().getId());
		
		boolean result = module.addCurrency(obj);
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
