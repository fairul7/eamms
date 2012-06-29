/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimStandardTypeModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;

public class ClaimStandardTypeTable extends Table 
{
	private String linkUrl;
	private TableColumn tc_Category;
	private TableColumn tc_Code;
	private TableColumn tc_Name;
	private TableColumn tc_Description;
	private TableColumn tc_Amount;
	private TableColumn tc_UserEdit;
	private TableColumn tc_TimeEdit;
	private TableColumn tc_State;
	
	
	/* Step 1: Initialization */
	public void init() 
	{
        setMethod("POST");
		setModel(new ClaimStandardTypeTableModel());
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
		tc_Name.setUrl(linkUrl); // Set the link
	}
	
	
	/* Step 3: Table display and processing */
	public class ClaimStandardTypeTableModel extends TableModel 
	{
	
		public ClaimStandardTypeTableModel() 
		{
//			tc_Category= new TableColumn("category", "Category");
//			addColumn(tc_Category);
//			tc_Code = new TableColumn("code", "Code");
			//tc_Code.setUrl("standardtype.jsp?cn=jsp_standardtype_edit.form1&type=View&fwdPage=standardTypeEdit");
//			tc_Code.setUrl("test_standardtype.jsp?type=View&fwdPage=standardTypeEdit");
//			tc_Code.setUrlParam("claimStandardTypeID");
//			addColumn(tc_Code);
			tc_Name = new TableColumn("name", "Name");
			tc_Name.setUrl("config_standard_type_edit.jsp");
			tc_Name.setUrlParam("claimStandardTypeID");
//			tc_Name.setUrl(linkUrl);
//			tc_Name.setUrlParam("claimStandardTypeID");
			addColumn(tc_Name);
			tc_Description = new TableColumn("description", "Description");
//			tc_Description.setUrl(linkUrl);
//			tc_Description.setUrlParam("claimStandardTypeID");
			addColumn(tc_Description);

//			tc_Currency = new TableColumn("currency","Currency");
//			addColumn(tc_Currency);

			tc_Amount = new TableColumn("amountStr","Amount");
			addColumn(tc_Amount);

			tc_TimeEdit = new TableColumn("timeEditStr", "Date Edit");
			addColumn(tc_TimeEdit);			

			addAction(new TableAction("delete", "Delete", "Delete selected Items?"));

            addFilter(new TableFilter("keyword"));

        }
		
		public Collection getTableRows() 
		{
			Application application  = Application.getInstance();

            String keyword = (String) getFilterValue("keyword");

            ClaimStandardTypeModule module = (ClaimStandardTypeModule)
					application.getModule(ClaimStandardTypeModule.class);
            if (getSort()!=null&&getSort().equals("amountStr"))
                setSort("amount");
            else if (getSort()!=null&&getSort().equals("timeEditStr"))
                setSort("timeEdit");

	   	    return module.selectObjects(keyword,"status","act",getStart(), getRows(), getSort(),isDesc());// 0,10 );
		}
		
		public int getTotalRowCount() 
		{
			Application application  = Application.getInstance();

            String keyword = (String) getFilterValue("keyword");

            ClaimStandardTypeModule module = (ClaimStandardTypeModule)
							application.getModule(ClaimStandardTypeModule.class);
			return module.countObjects(keyword);
		}
		
		public String getTableRowKey() {
			//return "claimStandardTypeID";
			return "id";
		}

		public Forward processAction(Event evt, String action, 
				String[] selectedKeys) 
		{
			Log.getLog(this.getClass()).debug("Inside processsssssssAction");
			if ("delete".equals(action)) 
			{
				Log.getLog(this.getClass()).debug("Inside delllllllettte");
				Application application = Application.getInstance();
				ClaimStandardTypeModule module = (ClaimStandardTypeModule)
					application.getModule(ClaimStandardTypeModule.class);
				for (int i=0; i<selectedKeys.length; i++) 
				{
					Log.getLog(this.getClass()).debug("Deletinggggg " + selectedKeys[i]);
					module.deleteObject(selectedKeys[i]);
				}
			}	/// end if 
			return null;
		}

	} /// end of Table Model

}



