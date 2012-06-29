/*
 * Created on Sep 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.misc;

import java.util.*;
import kacang.stdui.*;
import kacang.ui.Widget;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.SequencedHashMap;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MyUtil {
	/**
	 * Input: a Collection of HashMap
	 */
	public static int getRecordCount(Collection col) {
		Iterator iterator = col.iterator();
		HashMap hm = (HashMap) iterator.next();
		return (((Integer) hm.get("recordCount")).intValue());
	}
	
	public static String getSingleValue_SelectBox(SelectBox selectBox) {
		String str = "";
		List selectBoxValue = (List) selectBox.getValue();
		if ((selectBoxValue).size() != 0) {
			str = (String) (selectBoxValue).get(0);
		}
		return (str);	
	}
	
	/**
	 * Populates a select box with the data supplied from a Collection 
	 * dataCol = a Collection of HashMaps
	 */
	public static void populate_SelectBox(SelectBox selectBox, Collection dataCol, String keyColumn, String valueColumn) {
		Iterator iterator = dataCol.iterator();
		while (iterator.hasNext()) {
			HashMap hm    = (HashMap) iterator.next();
			String  key   = (String) hm.get(keyColumn);
			String  value = (String) hm.get(valueColumn);
			selectBox.addOption(key, value);
		}
	}
	
	public static FormField getFormField(Form form, String fieldName) {
		FormField formField = (FormField) form.getChild(fieldName);
		return (formField);
	}
	
	public static Widget getTableFilterWidget(TableModel model, String filterName) {
		Object[] filterArray = model.getFilterList().toArray();
		
		Widget aWidget = null;
		for (int i=0; i<filterArray.length; i++) {
			if (((TableFilter) filterArray[i]).getProperty().equals(filterName)) {
				aWidget = ((TableFilter) filterArray[i]).getWidget();
				break;
			}
		}
		
		return (aWidget);
	}
	
	public static Map arrayToMap(String[] keys, String[] values) {
		Map map = new HashMap();
		
		for (int i=0; i<keys.length; i++) {
			map.put(keys[i], values[i]);
		}
		return (map);
	}
	
	public static Map arrayToMap(Integer[] keys, String[] values) {
		Map map = new HashMap();
		
		for (int i=0; i<keys.length; i++) {
			map.put(keys[i], values[i]);
		}
		return (map);
	}
	
	/**
	 * Input: a collection of HashMaps
	 * Output: a Map
	 */
	public static Map collectionToMap(Collection colHashMaps, String keyColumn, String valueColumn) {
		Map map = new SequencedHashMap();
		
		Iterator iterator = colHashMaps.iterator();
		while (iterator.hasNext()) {
			HashMap hm    = (HashMap) iterator.next();
			String  key   = (String) hm.get(keyColumn);
			String  value = (String) hm.get(valueColumn);
			map.put(key, value);
		}
		
		return (map);
	}
	
	public static String escapeSingleQuotes(String inStr) {
		if (inStr != null) {
			//return (inStr.replaceAll("\'", "\'\'"));
            return StringUtils.replace(inStr, "\'", "\'\'");
		} else {
			return (null);
		}
	}
	
	public static boolean isValidChoice(String choice, String[] inArr) {
		if (inArr != null && choice != null) {
			for (int i=0; i<inArr.length; i++) {
				if (choice.equals(inArr[i])) {
					return true;
				}
			}
		}
		return false;
	}
}