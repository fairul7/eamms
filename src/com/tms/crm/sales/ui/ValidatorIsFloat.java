/*
 * Created on Apr 5, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import kacang.stdui.validator.Validator;
import kacang.stdui.FormField;
import org.apache.commons.lang.StringUtils;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ValidatorIsFloat extends Validator {
	public ValidatorIsFloat() {
		super();
	}
	
	public ValidatorIsFloat(String name) {
		super(name);
	}
	
	public ValidatorIsFloat(String name, String text) {
		super(name);
		setText(text);
	}
	
	public boolean validate(FormField formField) {
		try {
            String sDouble = (String)formField.getValue();

            sDouble = StringUtils.replace(sDouble, ",", "");
            //sDouble.replaceAll("," , "");
			Double.parseDouble(sDouble);
			formField.setValue(sDouble);
            return true;
		} catch (Exception e) {
			return false;
		}
	}
}