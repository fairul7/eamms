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
public class OpportunityProductArchiveItem extends DefaultDataObject {
	private String  opportunityID;
	private int     productSeq;
	private String  productID;
	private String  opDesc;
	private double     opValue;
	private Date    modifiedDate;
	private String  modifiedBy;
	private int     archiveSet;
	
	public OpportunityProductArchiveItem() {
	}
	
	public OpportunityProductArchiveItem(OpportunityProduct op, int newArchiveSet) {
		opportunityID = op.getOpportunityID();
		productSeq    = op.getProductSeq();
		productID     = op.getProductID();
		opDesc        = op.getOpDesc();
		opValue       = op.getOpValue();
		modifiedDate  = op.getModifiedDate();
		modifiedBy    = op.getModifiedBy();
		archiveSet    = newArchiveSet;
	}
	
	public int getArchiveSet() {
		return archiveSet;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	public String getOpDesc() {
		return opDesc;
	}
	
	public String getOpportunityID() {
		return opportunityID;
	}
	
	public double getOpValue() {
		return opValue;
	}
	
	public String getProductID() {
		return productID;
	}
	
	public int getProductSeq() {
		return productSeq;
	}
	
	public void setArchiveSet(int num) {
		archiveSet = num;
	}
		
	public void setModifiedBy(String string) {
		modifiedBy = string;
	}
	
	public void setModifiedDate(Date date) {
		modifiedDate = date;
	}
	
	public void setOpDesc(String string) {
		opDesc = string;
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public void setOpValue(double num) {
		opValue = num;
	}
	
	public void setProductID(String string) {
		productID = string;
	}
	
	public void setProductSeq(int num) {
		productSeq = num;
	}
	
	public String toString() {
		return(
			"<Sales OpportunityProductArchiveItem" +
			" opportunityID=\"" + getOpportunityID() + "\"" +
			" productSeq=\"" + getProductSeq() + "\"" +
			" productID=\"" + getProductID() + "\"" +
			" opDesc=\"" + getOpDesc() + "\"" +
			" opValue=\"" + getOpValue() + "\"" +
			" modifiedDate=\"" + getModifiedDate() + "\"" +
			" modifiedBy=\"" + getModifiedBy() + "\"" +
			" archiveSet=\"" + getArchiveSet() + "\"" +
			" />"
		);
	}
}
