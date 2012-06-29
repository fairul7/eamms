/*
 * Created on Apr 23, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GroupForm extends Form {
	private ComboSelectBox csb_Members; 	
	
	public void init() {
	}
	
	public void initForm() {
		removeChildren();
		setMethod("POST");
		
		Application application = Application.getInstance();
		SalesGroupModule sgm = (SalesGroupModule) application.getModule(SalesGroupModule.class);
		
		Hashtable ht = sgm.getSalesGroupsMap();
		csb_Members = new ComboSelectBox("csb_Members");
		
		addChild(csb_Members);
		csb_Members.init();
		csb_Members.setLeftValues((Map) ht.get("nonSalesGroup"));
		csb_Members.setRightValues((Map) ht.get("salesGroup"));
		
		Button btn_submit = new Button("btn_submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
		addChild(btn_submit);
	}
	
	public void onRequest(Event evt) {
		initForm();
	}
	
	public Forward onValidate(Event evt) {
		Application application = Application.getInstance();
		SalesGroupModule module = (SalesGroupModule) application.getModule(SalesGroupModule.class);
		
		Map map = csb_Members.getRightSelect().getOptionMap();
		module.updateSalesGroup(map);
		return new Forward("salesGroupsUpdated");
	}
}
