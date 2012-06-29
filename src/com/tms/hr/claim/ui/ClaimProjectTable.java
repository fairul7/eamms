/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimProjectModule;
import com.tms.hr.claim.model.ClaimFormItemModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;

public class ClaimProjectTable extends Table  
{

	public void init() 
	{
        setMethod("POST");
		setModel(new ClaimProjectTableModel());
	}

	public class ClaimProjectTableModel extends TableModel 
	{
		public ClaimProjectTableModel() 
		{
			TableColumn tcName = new TableColumn("name",Application.getInstance().getMessage("claims.label.projectName","Project Name"));
            tcName.setUrl("config_project_edit.jsp");
            tcName.setUrlParam("id");
            addColumn(tcName);
            //addColumn(new TableColumn("name", "Project Name"));

			addColumn(new TableColumn("description", Application.getInstance().getMessage("claims.category.description","Description")));
			addColumn(new TableColumn("statusImage", Application.getInstance().getMessage("claims.label.active","Active")));

//			TableColumn editCol = new TableColumn(null, "", false);
//			editCol.setLabel("Edit");
//			editCol.setUrl("config_project_edit.jsp");
//			editCol.setUrlParam("id");
//			addColumn(editCol);

//			addFilter(new TableFilter("name"));

			addAction(new TableAction("delete", Application.getInstance().getMessage("claims.label.delete","Delete"), 
					Application.getInstance().getMessage("claims.message.deleteSelected","Delete Selected Items?")));

            addFilter(new TableFilter("keyword"));
        }

		public Collection getTableRows() 
		{

			Application application = Application.getInstance();
            String keyword = (String) getFilterValue("keyword");
            ClaimProjectModule module = (ClaimProjectModule)
					application.getModule(ClaimProjectModule.class);
            if (getSort()!=null && getSort().equals("statusImage")) {
                setSort("status");
            }
			return module.selectObjects(keyword, null,null, getStart(), getRows(),getSort(),isDesc());
		}

		public int getTotalRowCount() 
		{
            Application application = Application.getInstance();
            String keyword = (String) getFilterValue("keyword");
            ClaimProjectModule module = (ClaimProjectModule)	
						application.getModule(ClaimProjectModule.class);
            return module.countObjects(keyword,"act");
		}

		public String getTableRowKey() 
		{ return "id"; }

		public Forward processAction(Event evt, String action, String[] selectedKeys) 
		{
			if ("delete".equals(action)) 
			{
				Application application = Application.getInstance();
				ClaimProjectModule module = (ClaimProjectModule)application.getModule(ClaimProjectModule.class);
                ClaimFormItemModule itemModule = (ClaimFormItemModule)application.getModule(ClaimFormItemModule.class);
                boolean projectInUse=false;
        		for (int i=0; i<selectedKeys.length; i++)
				{
        			int iProjectInUse=itemModule.countObjects("projectId",selectedKeys[i]);
                    if (iProjectInUse>0) {
                        projectInUse = true;
                        break;
                    }
         	    }
                if(projectInUse)
                    return new Forward("projectInUse");
                else
                    for (int i=0; i<selectedKeys.length; i++)
                    {
                        module.deleteObject(selectedKeys[i]);
                     }

			}
			return null;
		}

    }

}
