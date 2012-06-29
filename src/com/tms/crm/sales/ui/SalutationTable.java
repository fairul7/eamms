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
public class SalutationTable extends Table {
	private String linkUrl;
	private TableColumn tc_SalutationText;
	
	
	/* Step 1: Initialization */
	public void init() {
		setModel(new SalutationTableModel());
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
		tc_SalutationText.setUrl(linkUrl); // Set the link
	}
	
	
	/* Step 3: Table display and processing */
	public class SalutationTableModel extends TableModel {
	
		public SalutationTableModel() {
			tc_SalutationText = new TableColumn("salutationText", Application.getInstance().getMessage("sfa.label.salutation","Salutation"));
			tc_SalutationText.setUrl(linkUrl);
			tc_SalutationText.setUrlParam("salutationID");
			addColumn(tc_SalutationText);
			
			TableColumn tc_isArchived = new TableColumn("isArchived", Application.getInstance().getMessage("sfa.label.archived","Archived"));
			tc_isArchived.setFormat(new TableStringFormat(DisplayConstants.getYesNoMap()));
			addColumn(tc_isArchived);

/*
            addAction(new TableAction("delete","Delete","Are you sure you want to delete?"));
*/

		}
		
		public Collection getTableRows() {
			Application application = Application.getInstance();
			SalutationModule module = (SalutationModule) application.getModule(SalutationModule.class);
			return module.listSalutations(getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			Application application = Application.getInstance();
			SalutationModule module = (SalutationModule) application.getModule(SalutationModule.class);
			return module.countSalutations();
		}
		
		public String getTableRowKey() {
			return "salutationID";
		}

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
                SalutationModule sm = (SalutationModule) Application.getInstance().getModule(SalutationModule.class);
                for (int i = 0; i < selectedKeys.length; i++) {
                    String selectedKey = selectedKeys[i];
                    sm.deleteSalutation(selectedKey);
                }

            }
            return super.processAction(evt, action, selectedKeys);    //To change body of overridden methods use File | Settings | File Templates.
        }

	}
	
	public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}
}
