/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.model;

import kacang.model.DefaultDataObject;

import java.math.BigDecimal;
import java.util.Date;

public class ClaimStandardType extends DefaultDataObject
{
	private String claimStandardTypeID;
	private String category;
	private String code;
	private String name;
	private String description;
	private String currency;
	private BigDecimal amount;
	private String userEdit;
	private Date timeEdit;
	private String state;
	private String status;

	public ClaimStandardType()
	{
		category="x";
		code="x";
		name="x";
		description="x";
		currency = "MYR";
		amount = new BigDecimal("0.00");
		userEdit = "";
		timeEdit = new Date();
		state = "act";
		status = "act";
	}

	public String getId()
	{ return getClaimStandardTypeID();}
	public void setId(String id)
	{ setClaimStandardTypeID(id);}

	public String getClaimStandardTypeID() 
	{ return claimStandardTypeID;}
	public void setClaimStandardTypeID(String claimStandardTypeID)
	{ this.claimStandardTypeID = claimStandardTypeID;}

	public String getCategory() 
	{ return category;}
	public void setCategory(String category)
	{ this.category = category;}

	public String getCode() 
	{ return code;}
	public void setCode(String code)
	{ this.code = code;}

	public String getName()
	{ return name;}
	public void setName(String name)
	{ this.name = name;}

	public String getDescription()
	{ return description;}
	public void setDescription(String description)
	{ this.description = description;}
	
	public String getCurrency()
	{ return currency;}
	public void setCurrency(String currency)
	{ this.currency = currency;}


	public BigDecimal getAmount()
	{ return amount;}
	public void setAmount(BigDecimal amount)
	{ this.amount = amount;}

   public String getAmountStr()
   { return "<div align='right'>"+amount.setScale(2).toString()+"</div>";}

	public String getUserEdit()
	{ return userEdit;}
	public void setUserEdit(String userEdit)
	{ this.userEdit = userEdit;}

	public Date getTimeEdit()
	{ return timeEdit;}
	public void setTimeEdit(Date timeEdit)
	{ this.timeEdit = timeEdit;}
   public String getTimeEditStr()
   { return DateFormat.format(this.timeEdit,"yyyy-MM-dd");}

	public String getState()
	{ return state;}
	public void setState(String state)
	{ this.state = state;}

	public String getStatus()
	{ return status;}
	public void setStatus(String status)
	{ this.status = status;}

	public String toString()
	{
		return (
			"<Claim ClaimStandardType" +
			" claimStandardTypeID=\""+getClaimStandardTypeID()+"\""+
			" category=\""+getCategory()+"\""+
			" code=\""+getCode()+"\""+
			" name=\""+getName()+"\""+
			" description=\""+getDescription()+"\""+
			" currency=\""+getCurrency()+"\""+
			" amount=\""+getAmount()+"\""+
			" userEdit=\""+getUserEdit()+"\""+
			" timeEdit=\""+getTimeEdit()+"\""+
			" state=\""+getState()+"\""+
			" status=\""+getStatus()+"\""+
			" />"
					);
	}


}


