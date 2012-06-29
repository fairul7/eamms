/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.model;

import kacang.model.DefaultDataObject;

public class ClaimConfig extends DefaultDataObject
{
	private String id;
	private String namespace;
	private String category;
	private String property1;
	private String property2;
	private String property3;
	private String property4;
	private String property5;

	public ClaimConfig()
	{
		id = "x";
		namespace = "x";
		category = "x";
		property1 = "x";
		property2 = "x";
		property3 = "x";
		property4 = "x";
		property5 = "x";
	}

	public String getId()
	{ return id;}
	public void setId(String id)
	{ this.id = id ;}

	public String getNamespace()
	{ return namespace;}
	public void setNamespace(String namespace)
	{ this.namespace = namespace;}

	public String getCategory()
	{ return category;}
	public void setCategory(String category)
	{ this.category = category;}

	public String getProperty1()
	{ return property1;}
	public void setProperty1(String property1)
	{ this.property1 = property1;}
	
   public String getProperty2()
   { return property2;}
   public void setProperty2(String property2)
   { this.property2 = property2;}

   public String getProperty3()
   { return property3;}
   public void setProperty3(String property3)
   { this.property3 = property3;}

   public String getProperty4()
   { return property4;}
   public void setProperty4(String property4)
   { this.property4 = property4;}

   public String getProperty5()
   { return property5;}
   public void setProperty5(String property5)
   { this.property5 = property5;}

	public String toString()
	{
		return (
			"<Claim ClaimConfig" +
			" id=\""+getId()+"\""+
			" namespace=\""+getNamespace()+"\""+
			" />"
					);
	}

}


