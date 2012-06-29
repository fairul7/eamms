/*
 * Created on Dec 17, 2003
 * Created by Paul Pak
 */
package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.stdui.validator.*;

/**
 * Code snippet:<br>
 * <blockquote><pre>
 * public void init() {
 *     setColumns(2);
 *     
 *     addChild(new Label("lb1", "Age:"));
 *     tf_Age = new TextField("tf_Age");
 *     vNum   = new ValidatorIsNumeric("vNum", Application.getInstance().getMessage("sfa.label.agemustbenumeric","Age must be numeric"));
 *     vMsg   = new ValidatorMessage("vMsg");
 *     tf_Age.addChild(vNum);
 *     tf_Age.addChild(vMsg);
 *     addChild(tf_Age);
 *     
 *     submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
 *     addChild(submit);
 * }
 * 
 * public Forward onSubmit(Event evt) {
 *     super.onSubmit(evt);
 * 
 *     int age = Integer.parseInt((String) tf_Age.getValue());
 *     if (age < 21) {
 *         vMsg.showError(Application.getInstance().getMessage("sfa.label.youmustbe21orabovetovote","You must be 21 or above to vote"));
 *         this.setInvalid(true); // invalidate the form
 *     }
 *     
 *     return null;
 * }</pre></blockquote>
 */
public class ValidatorMessage extends Validator {
	FormField formField = null;
	
	public ValidatorMessage(String name) {
		super.setName(name);
	}
	
	public void showError(String text) {
		showError(text, true);
	}
	
	public void showError(String text, boolean setFieldInvalid) {
		setText(text);
		setInvalid(true);
		if (setFieldInvalid && formField != null) {
			formField.setInvalid(true);
		}
	}
	
	public boolean validate(FormField formField) {
		this.formField = formField;
 		return(true);
	}
}
