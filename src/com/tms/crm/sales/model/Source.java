/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Source extends DefaultDataObject {
	private String  sourceID;
	private String  sourceText;
	private boolean isArchived;
	
	
	public String getSourceID() {
		return sourceID;
	}
	
	public String getSourceText() {
		return sourceText;
	}
	
	public String getIsArchived() {
		if (isArchived) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public void setSourceID(String string) {
		sourceID = string;
	}
	
	public void setSourceText(String string) {
		sourceText = string;
		if (sourceText != null) {
			sourceText = sourceText.trim();
		}
	}
	
	public void setIsArchived(String string) {
		if (string.equals("1")) {
			isArchived = true;
		} else {
			isArchived = false;
		}
	}
	
	public String toString() {
		return(
			"<Sales Source" +
			" sourceID=\"" + getSourceID() + "\"" +
			" sourceText=\"" + getSourceText() + "\"" +
			" isArchived=\"" + getIsArchived() + "\"" +
			" />"
		);
	}
}