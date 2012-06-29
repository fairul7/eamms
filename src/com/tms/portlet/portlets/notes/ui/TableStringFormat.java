
package com.tms.portlet.portlets.notes.ui;

import kacang.stdui.*;

public class TableStringFormat implements TableFormat  {
	int endIndex;
	int beginIndex;
	
	public TableStringFormat(int beginIndex, int endIndex) {
		this.endIndex = endIndex;
		this.beginIndex = beginIndex;		
	}
	
	public String format(Object value) {
		if (value == null)
		  return "";
		if (!(value instanceof String))
		  return value.toString();
		  
		String str = value.toString();
				
		if (str.length() < endIndex) 
			return str;				  
		else 						
			return str.substring(beginIndex,endIndex);		  		 		
		  	
	 
	}

}
