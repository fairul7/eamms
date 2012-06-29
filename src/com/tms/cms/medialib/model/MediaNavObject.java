package com.tms.cms.medialib.model;

import kacang.model.DefaultDataObject;

public class MediaNavObject extends DefaultDataObject {
	private String nextId = "";
	private String prevId = "";
	
	public String getNextId() {
		return nextId;
	}
	public void setNextId(String nextId) {
		this.nextId = nextId;
	}
	public String getPrevId() {
		return prevId;
	}
	public void setPrevId(String prevId) {
		this.prevId = prevId;
	}
	
}
