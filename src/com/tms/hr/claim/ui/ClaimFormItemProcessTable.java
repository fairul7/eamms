/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormItemModule;
import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;

public class ClaimFormItemProcessTable extends Table  
{

	protected String formId ;

	public void setFormId(String formId)
	{ this.formId = formId;}
	public String getFormId()
	{ return this.formId;}


	public void init() 
	{
        setMethod("POST");
		setModel(new ClaimFormItemTableModel());
	}

	public class ClaimFormItemTableModel extends TableModel 
	{
		public ClaimFormItemTableModel() 
		{
			addColumn(new TableColumn("timeFrom", "Date"));
			addColumn(new TableColumn("remarks", "Purpose"));
			addColumn(new TableColumn("categoryName", "Category"));
			addColumn(new TableColumn("standardTypeName", "Type"));
			addColumn(new TableColumn("projectName", "Project"));
			addColumn(new TableColumn("timeFrom", "Date"));
			addColumn(new TableColumn("currency", "Currency"));
			addColumn(new TableColumn("amount", "Amount"));
			addColumn(new TableColumn("description", "Description"));

			addAction(new TableAction("delete", "Delete", "Delete Selected Items?"));
		}

		public Collection getTableRows() 
		{

			Application application = Application.getInstance();
			ClaimFormItemModule module = (ClaimFormItemModule)
					application.getModule(ClaimFormItemModule.class);
			return module.selectObjects("formId",formId.toString(), getStart(), getRows());
		}

		public int getTotalRowCount() 
		{
            Application application = Application.getInstance();
            ClaimFormItemModule module = (ClaimFormItemModule)	
						application.getModule(ClaimFormItemModule.class);
            return module.countObjects("act");
		}

		public String getTableRowKey() 
		{ return "id"; }

		public Forward processAction(Event evt, String action, String[] selectedKeys) 
		{
			if ("delete".equals(action)) 
			{
				Application application = Application.getInstance();
				ClaimFormItemModule module = (ClaimFormItemModule)application.getModule(ClaimFormItemModule.class);
        		for (int i=0; i<selectedKeys.length; i++) 
				{
        			module.deleteObject(selectedKeys[i]);
         	}
			}
			return null;
		}

    }

}
