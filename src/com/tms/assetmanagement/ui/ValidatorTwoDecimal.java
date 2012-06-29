package com.tms.assetmanagement.ui;

import java.util.Collection;
import java.util.Map;

import kacang.stdui.FormField;
import kacang.stdui.validator.Validator;

public class ValidatorTwoDecimal extends Validator{
	
	public ValidatorTwoDecimal() {
        super();
    }

    public ValidatorTwoDecimal(String name) {
        super(name);
    }

    public ValidatorTwoDecimal(String name, String text) {
        super(name);
        setText(text);
    }
	
	public boolean validate(FormField formField) {
		Object value = formField.getValue();
		
		if (value == null) 
	       return false;
		
		String strValue = (String)value; 
		if (!(strValue.indexOf(".") == -1)){
			strValue = strValue.substring(strValue.indexOf(".")+ 1, strValue.length());
			return strValue.length() < 3;		
		}
		else 
			return true;		
	}

}
