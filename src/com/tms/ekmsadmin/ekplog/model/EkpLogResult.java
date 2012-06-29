package com.tms.ekmsadmin.ekplog.model;

import java.util.Collection;

import kacang.model.DefaultDataObject;

public class EkpLogResult extends DefaultDataObject {
	private Collection logObjects;
	private int totalResult = 0;
	
	public Collection getLogObjects() {
		return logObjects;
	}
	public void setLogObjects(Collection logObjects) {
		this.logObjects = logObjects;
	}
	public int getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
}
