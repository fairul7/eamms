package com.tms.fms.widgets;

import kacang.stdui.TextField;

public class ExtendedTextField extends TextField{
	private String onKeyUp;
	private String onChange;
	
	public ExtendedTextField(String name){
		super(name);
	}
	
	public ExtendedTextField(String name, String value){
		super(name, value);
	}
	
	public String getDefaultTemplate(){
		return "extendedtextfield";
	}

	public String getOnKeyUp() {
		return onKeyUp;
	}

	public void setOnKeyUp(String onKeyUp) {
		this.onKeyUp = onKeyUp;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}
	
}
