/*
 * Created on Feb 11, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import kacang.Application;

import java.util.*;

import com.tms.util.FormatUtil;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DisplayConstants {
	public static String DATE_FORMAT = FormatUtil.getInstance().getLongDateFormat();
	
	
	public static Map getYesNoMap() {
		Map map = new HashMap();
		
		map.put("1", Application.getInstance().getMessage("sfa.label.yes","Yes"));
		map.put("0", Application.getInstance().getMessage("sfa.label.no","No"));
		
		return map;
	}
}
