package com.tms.sam.po.setting.ui;

import java.util.Date;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.setting.model.CurrencyObject;
import com.tms.sam.po.setting.model.SettingModule;

public class CurrencyEdit extends Currency{
	private String currencyID;
	
	public void init(){
		super.init();
	}

	public void onRequest(Event evt) {
        init();
        
        SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
        CurrencyObject obj = module.getCurrency(currencyID);
        country.setValue(obj.getCountry());
        currency.setValue(obj.getCurrency());
        
    }
	
	public Forward onValidate(Event event) {
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit")) {
           SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
           if(module.isCurrencyNameExist(currency.getValue().toString(), currencyID)){
    			currency.setInvalid(true); 
    			return new Forward(FORWARD_RESULT);
           }
           editCurrency(); 
           return new Forward(FORWARD_BACK);
        }

		return new Forward(FORWARD_BACK);
	}

	public void editCurrency(){
		Application app = Application.getInstance();
		Date now = new Date();
		SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
		CurrencyObject obj = new CurrencyObject();
		obj.setCurrencyID(currencyID);
		obj.setCountry(country.getValue().toString());
		obj.setCurrency(currency.getValue().toString());
		obj.setLastUpdatedBy(app.getCurrentUser().getId());
		obj.setLastUpdatedDate(now);
		module.editCurrency(obj);
	}
	public String getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(String currencyID) {
		this.currencyID = currencyID;
	}
	
}
