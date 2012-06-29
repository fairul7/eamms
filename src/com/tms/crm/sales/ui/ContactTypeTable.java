/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ContactTypeTable extends Table {
	private String linkUrl;
	private TableColumn tc_ContactTypeName;
	
	
	/* Step 1: Initialization */
	public void init() {
		setModel(new ContactTypeTableModel());
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
		tc_ContactTypeName.setUrl(linkUrl); // Set the link
	}
	
	
	/* Step 3: Table display and processing */
	public class ContactTypeTableModel extends TableModel {
	
		public ContactTypeTableModel() {
			tc_ContactTypeName = new TableColumn("contactTypeName", Application.getInstance().getMessage("sfa.label.contactType","Contact Type"));
			tc_ContactTypeName.setUrl(linkUrl);
			tc_ContactTypeName.setUrlParam("contactTypeID");
			addColumn(tc_ContactTypeName);
			
			TableColumn tc_isArchived = new TableColumn("isArchived", Application.getInstance().getMessage("sfa.label.archived","Archived"));
			tc_isArchived.setFormat(new TableStringFormat(DisplayConstants.getYesNoMap()));
			addColumn(tc_isArchived);
			
            addAction(new TableAction("delete",Application.getInstance().getMessage("sfa.message.delete","Delete"),Application.getInstance().getMessage("sfa.message.deleteMessage","Are you sure you want to delete?")));


		}
		
		public Collection getTableRows() {
			Application application  = Application.getInstance();
			ContactTypeModule module = (ContactTypeModule) application.getModule(ContactTypeModule.class);
			return module.listContactTypes(getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			Application application  = Application.getInstance();
			ContactTypeModule module = (ContactTypeModule) application.getModule(ContactTypeModule.class);
			return module.countContactTypes();
		}
		
		public String getTableRowKey() {
			return "contactTypeID";
		}

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
                ContactTypeModule ctm = (ContactTypeModule) Application.getInstance().getModule(ContactTypeModule.class);
                OpportunityContactModule ocm = (OpportunityContactModule) Application.getInstance().getModule(OpportunityContactModule.class);
                boolean type=false;
                for (int i = 0; i < selectedKeys.length; i++) {
                	int count=ocm.countContactByContactType(selectedKeys[i]);
                	if(count>0){
                		type=true;
                	}
                }
               if(!type){
                for (int i = 0; i < selectedKeys.length; i++) {
                    String selectedKey = selectedKeys[i];
                    ctm.deleteContactType(selectedKey);
                }if(selectedKeys.length>0)
                return new Forward("delete");
                }
               else{
            	return new Forward("notdelete"); 	
                }
            }
            return super.processAction(evt, action, selectedKeys);    //To change body of overridden methods use File | Settings | File Templates.
        }


	}
	
	public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}
}
