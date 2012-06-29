package com.tms.collab.formwizard.ui;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kacang.stdui.TableFormat;
import org.apache.commons.lang.StringUtils;


public class TableReplaceStringFormat implements TableFormat {
	Map valueMap;
	
	public TableReplaceStringFormat(Map valueMap) {
		this.valueMap = valueMap;
	}
	
	public String format(Object value) {		 
		String str = String.valueOf(value);
		Set oldKeySet = valueMap.keySet();
		String oldKey;
		if (str != null) {
			for (Iterator it = oldKeySet.iterator(); it.hasNext();) {
				oldKey = String.valueOf(it.next());
				if (str.equals(oldKey)) {
					str = String.valueOf(valueMap.get(oldKey));	
				}
			}
		}
        if (str != null) {
            str = StringUtils.replace(str, "\r", "<br>");
        }
		return str;
	
	}

}
