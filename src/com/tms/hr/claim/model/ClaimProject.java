/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.model;

import kacang.model.DefaultDataObject;

public class ClaimProject extends DefaultDataObject
{
	private String id;
	private String fkPcc;
	private String name;
	private String description;
	private String state;
	private String status;

	public ClaimProject()
	{
		id="x";
		fkPcc = "x";
		name="x";
		description="x";
		status = "act";
	}

	public String getId()
	{ return id;}
	public void setId(String id)
	{ this.id = id ;}

	public String getFkPcc()
	{ return fkPcc;}
	public void setFkPcc(String fkPcc)
	{ this.fkPcc = fkPcc;}

	public String getName()
	{ return name;}
	public void setName(String name)
	{ this.name = name;}

	public String getDescription()
	{ return description;}
	public void setDescription(String description)
	{ this.description = description;}

	public String getStatusImage()
	{
      if(this.status.equals("act"))
      { return "<div align=\"center\"><img src=\"/common/table/booleantrue.gif\"></div>"; }
      else
      { return "<div align=\"center\"><img src=\"/ekms/images/blank.gif\"></div>"; }
	}
	
	public String getStatus()
	{ return status;}
	public void setStatus(String status)
	{ this.status = status;}

	public String toString()
	{
		return (
			"<Claim ClaimProject" +
			" id=\""+getId()+"\""+
			" fkPcc=\""+getFkPcc()+"\""+
			" name=\""+getName()+"\""+
			" description=\""+getDescription()+"\""+
			" status=\""+getStatus()+"\""+
			" />"
					);
	}


}


