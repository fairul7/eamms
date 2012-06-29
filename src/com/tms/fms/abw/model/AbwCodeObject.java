package com.tms.fms.abw.model;

import kacang.model.DefaultDataObject;

public class AbwCodeObject extends DefaultDataObject {
	protected String abw_code;
	protected String description;
	
	public String getAbw_code() {
		return abw_code;
	}
	public void setAbw_code(String abw_code) {
		this.abw_code = abw_code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
