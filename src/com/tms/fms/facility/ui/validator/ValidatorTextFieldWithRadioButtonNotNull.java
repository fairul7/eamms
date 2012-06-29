package com.tms.fms.facility.ui.validator;

import java.util.Map;

import kacang.stdui.FormField;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.validator.Validator;

public class ValidatorTextFieldWithRadioButtonNotNull extends Validator {
	private Radio rdButton;
	
    public ValidatorTextFieldWithRadioButtonNotNull() {
    }

    public ValidatorTextFieldWithRadioButtonNotNull(String name) {
        super(name);
    }
    
    public ValidatorTextFieldWithRadioButtonNotNull(String name, Radio rdButton, String text){
    	super(name);
    	this.rdButton = rdButton;
    	setText(text);
    }

    public boolean validate(FormField formField) {
    	String value = formField.getValue().toString();
        if(rdButton.isChecked() && ("".equals(value.trim()))){
        	return false;
        }else{
        	return true;
        }
    }
}
