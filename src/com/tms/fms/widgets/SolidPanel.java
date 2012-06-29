package com.tms.fms.widgets;

import kacang.ui.*;

public class SolidPanel extends Widget {
	private String title;
	
	public SolidPanel() {
	}
	
	public SolidPanel(String name, String title) {
		super(name);
		setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDefaultTemplate() {
		return "fms/solidPanel";
	}
}
