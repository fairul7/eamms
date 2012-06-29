package com.tms.fms.widgets;

import kacang.stdui.*;

public class BoldLabel extends Label {
	public BoldLabel() {
		super();
	}
	
	public BoldLabel(String name) {
		super(name);
	}
	
	public BoldLabel(String name, String text) {
		super(name, text);
	}
	
	public String getDefaultTemplate() {
		return "fms/boldlabel";
	}
}
