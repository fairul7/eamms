package com.tms.collab.isr.ui;

import java.util.List;

import kacang.stdui.FormField;
import kacang.stdui.validator.Validator;

public class ValidatorNotEmptySelectBox extends Validator {
	private String emptyValue = "";
	
	public ValidatorNotEmptySelectBox(String name, String text){
        super(name);
        setText(text);
    }
	
	public ValidatorNotEmptySelectBox(String name, String text, String emptyValue){
        super(name);
        setText(text);
        if(emptyValue != null)
        	this.emptyValue = emptyValue;
    }

    public String getEmptyValue() {
		return emptyValue;
	}


	public void setEmptyValue(String emptyValue) {
		if(emptyValue != null)
			this.emptyValue = emptyValue;
	}

	public boolean validate(FormField formField) {
		List list = (List) formField.getValue();
		if(list != null) {
	        String strValue = (String) list.get(0);
	        if(emptyValue.equals(strValue)){
	            return false;
	        }
		}
		return true;
    }
}
