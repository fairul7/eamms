package com.tms.fms.widgets;

import kacang.stdui.SelectBox;

public class ExtendedSelectBox extends SelectBox{
	private String onKeyUp;
	
	public ExtendedSelectBox(String name){
		super(name);
	}
	
	public String getDefaultTemplate(){
		return "extselectbox";
	}

	public String getOnKeyUp() {
		return onKeyUp;
	}

	public void setOnKeyUp(String onKeyUp) {
		this.onKeyUp = onKeyUp;
	}
	
}
