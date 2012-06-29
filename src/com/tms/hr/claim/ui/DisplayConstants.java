/*
 * Created on Feb 11, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.hr.claim.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DisplayConstants {
	public static String DATE_FORMAT = "dd MMM yyyy";
	
	
	public static Map getYesNoMap() {
		Map map = new HashMap();
		
		map.put("1", "Yes");
		map.put("0", "No");
		
		return map;
	}
}
