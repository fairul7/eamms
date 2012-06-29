package com.tms.wiki.ui;

import java.util.List;
import kacang.stdui.FormField;
import kacang.stdui.SelectBox;
import kacang.stdui.validator.Validator;

public class ValidatorSelectBox extends Validator {
	String invalidKey = null;
   	
	public ValidatorSelectBox() {
		super();
	}
	
	public ValidatorSelectBox(String name) {
		super(name);
	}
	
	public ValidatorSelectBox(String name, String text) {
		super(name);
		setText(text);
	}
	
	public ValidatorSelectBox(String name, String text, String invalidKey) {
		super(name);
		setText(text);
		setInvalidKey(invalidKey);
	}
    

	public void setInvalidKey(String invalidKey) {
		this.invalidKey = invalidKey;
	}
	
	public boolean validate(FormField formField) {
		try {
            SelectBox sb = (SelectBox) formField; 
            return validateSelectBox(sb);

		} catch (Exception e) {
			return false;
		}
	}
    
    private boolean validateSelectBox(SelectBox sb){
        if (!sb.isMultiple()) {
            String str = (String) ((List) sb.getValue()).get(0);
                            
            if (invalidKey != null) {
                // invalid if 'invalidKey' is selected
                if (str.equals(invalidKey)) {
                    return false;
                }
            }
        }
        return true;
    }
}