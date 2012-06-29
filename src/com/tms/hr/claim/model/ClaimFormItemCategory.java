/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.model;

import kacang.model.DefaultDataObject;

import java.util.Date;

public class ClaimFormItemCategory extends DefaultDataObject
{
	private String id;
	private String code;
	private String name;
	private String description;
	private String userEdit;
	private Date timeEdit;
	private String state;
	private String status;

    // add for type & dependencies
    private String type;
    private String[] dependencies;

	public ClaimFormItemCategory()
	{
		id="x";
		code= "x";
		name="x";
		description="x";
		userEdit="x";
		timeEdit= new Date();
		state="x";
		status = "act";
	}

	public String getId()
	{ return id;}
	public void setId(String id)
	{ this.id = id ;}

	public String getCode()
	{ return code ;}
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

	public String getStatusImage()
	{
		if(this.status.equals("act"))
		{ return "<img src=\"/common/table/booleantrue.gif\">"; }	
		else
		{ return "<img src=\"/ekms/images/blank.gif\">"; }	
	}

	public String toString()
	{
		return (
			"<Claim ClaimFormItemCategory" +
			" id=\""+getId()+"\""+
			" code=\""+getCode()+"\""+
			" name=\""+getName()+"\""+
			" description=\""+getDescription()+"\""+
			" userEdit =\""+getUserEdit()+"\""+
			" timeEdit=\""+getTimeEdit().toString()+"\""+
			" state =\""+getState()+"\""+
			" status=\""+getStatus()+"\""+
			" />"
					);
	}

    public void setType(String type) {
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    public String[] getDependencies() {
        return dependencies;
    }
}


