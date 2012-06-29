/*
 * Created on Jan 2, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
import kacang.stdui.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TableLimitStringFormat implements TableFormat {
	Map valueMap;
	int maxLength;
	String defaultValue = "";
	
	/**
	 * Similar functionality to TableStringFormat
	 */
	public TableLimitStringFormat(Map valueMap, int maxLength) {
		this(maxLength);
		this.valueMap = valueMap;
	}
	
	/**
	 * Limits the length of the string based on maxLength.
	 */
	public TableLimitStringFormat(int maxLength) {
		if (maxLength < 5) {
			maxLength = 5;
		}
		this.maxLength = maxLength;
	}
	
	public String format(Object value) {
		String str;
		
		if (valueMap != null) {
			str = (String) valueMap.get(value);
		} else {
			str = value.toString();
		}
		
		if (str == null) {
			str = defaultValue;
		}
		
		return (str.length() > maxLength ? str.substring(0, maxLength-3) + ".." : str);
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
