/*
 * Created on Jan 19, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.Date;
import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityProductArchive extends DefaultDataObject {
	private String  opportunityID;
	private int     archiveSet;
	private Date    archivedOn;
	
	
	public int getArchiveSet() {
		return archiveSet;
	}
	
	public Date getArchivedOn() {
		return archivedOn;
	}
	
	public String getOpportunityID() {
		return opportunityID;
	}
	
	public void setArchiveSet(int i) {
		archiveSet = i;
	}
	
	public void setArchivedOn(Date date) {
		archivedOn = date;
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public String toString() {
		return(
			"<Sales OpportunityProductArchive" +
			" opportunityID=\"" + getOpportunityID() + "\"" +
			" archiveSet=\"" + getArchiveSet() + "\"" +
			" archivedOn=\"" + getArchivedOn() + "\"" +
			" />"
		);
	}
}
