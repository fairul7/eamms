/*
 * Created on Jan 16, 2004
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
public class OpportunityProduct extends DefaultDataObject {
	private String productName;
    private String  opportunityID;
	private int     productSeq;
	private String  productID;
	private String  opDesc;
	private String category;
	private String categoryName;
	private double     opValue;
	private Date    modifiedDate;
	private String  modifiedBy;
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	public String getOpDesc() {
		if(opDesc == null || "".equals(opDesc))
			return " ";
		else
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
			"<Sales OpportunityProduct" +
			" opportunityID=\"" + getOpportunityID() + "\"" +
			" productSeq=\"" + getProductSeq() + "\"" +
			" productID=\"" + getProductID() + "\"" +
			" opDesc=\"" + getOpDesc() + "\"" +
			" opValue=\"" + getOpValue() + "\"" +
			" modifiedDate=\"" + getModifiedDate() + "\"" +
			" modifiedBy=\"" + getModifiedBy() + "\"" +
			" />"
		);
	}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
