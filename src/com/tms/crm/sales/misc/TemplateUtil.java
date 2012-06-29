/*
 * Created on Jan 27, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.misc;

import java.util.*;
import javax.servlet.jsp.PageContext;
import kacang.stdui.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TemplateUtil {
	/** For Table template */
	public static Collection getTableActions(PageContext pageContext) {
		TableModel tableModel = (TableModel) pageContext.getAttribute("model");
		return tableModel.getActionList();
	}
	
	/**
	 * For Table template
	 * Sets actionList_Top & actionList_Bottom to the PageContext
	 */
	public static void splitTableActions(PageContext pageContext) {
		Vector vecTop    = new Vector();
		Vector vecBottom = new Vector();
		
		Collection tableActions = getTableActions(pageContext);
		if (tableActions != null) {
			Iterator iterator = tableActions.iterator();
			while (iterator.hasNext()) {
				TableAction tableAction = (TableAction) iterator.next();
				if (isTopAction(tableAction)) {
					vecTop.add(tableAction);
				} else {
					vecBottom.add(tableAction);
				}
			}
		}
		
		pageContext.setAttribute("actionList_Top", vecTop);
		pageContext.setAttribute("actionList_Bottom", vecBottom);
	}
	
	/** For Table template */
	public static boolean isTopAction(TableAction aTableAction) {
		if (!aTableAction.getAction().endsWith("_bottom")) {
			return true;
		} else {
			return false;
		}
	}
}
