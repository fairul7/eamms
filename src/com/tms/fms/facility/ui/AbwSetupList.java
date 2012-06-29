package com.tms.fms.facility.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.facility.model.SetupModule;

public class AbwSetupList extends Table
{
	public AbwSetupList(){}
	
	public AbwSetupList(String name)
	{
		super(name);
	}
	
	 public void init() 
	 {
	     //setPageSize(20);
	     setWidth("100%");
		 setModel(new AbwSetupListModel());
	 }
	 
	public void onRequest(Event evt) 
	{
		init();
	}
	
	private class AbwSetupListModel extends TableModel
	{
		public AbwSetupListModel()
		{
			TableColumn abw_code = new TableColumn("abw_code", Application.getInstance().getMessage("fms.setup.table.abwCode"));
			abw_code.setUrlParam("id");
			addColumn(abw_code);
			
			TableColumn description = new TableColumn("description", Application.getInstance().getMessage("fms.setup.table.desc"));
			addColumn(description);
			
			TableAction del = new TableAction("del", Application.getInstance().getMessage("fms.setup.btnDelAbw", "Delete Selected ABW Code"));
			addAction(del);
			
			addFilter(new TableFilter("keyword"));
		}
		
		public Collection getTableRows() 
		{
			SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			String keyword = (String)getFilterValue("keyword");
			
			Collection result = sm.getAbwCodes(keyword, isDesc(), getSort(), getStart(), getRows());
			return result;
		}
		
		public int getTotalRowCount() 
		{
			SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			String keyword = (String)getFilterValue("keyword");
			
			int total = sm.getAbwCodesCount(keyword);
			return total;
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) 
		{
			if(action.equals("del"))
			{
				if(selectedKeys == null || selectedKeys.length == 0)
				{
					return new Forward("noRecordSelelcted");
				}
				
				SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
				for(int i = 0; i < selectedKeys.length; i++)
				{
					String abwCode = selectedKeys[i];
					if(sm.isAbwCodeInUse(abwCode))
					{
						return new Forward("abwCodeInUse");
					}
					sm.deleteAbwCode(abwCode);
				}
				
				return new Forward("deleteSuccessfully");
			}
			
			return super.processAction(evt, action, selectedKeys);
		}
		
		public String getTableRowKey() 
		{
		    return "id";
		}
	}
	
	public String getDefaultTemplate()
	{
		return "fms/facility/abwSetupListTemplate";
	}
}
