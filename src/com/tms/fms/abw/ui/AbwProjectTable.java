package com.tms.fms.abw.ui;


import java.util.Collection;

import com.tms.fms.abw.model.AbwModule;


import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;

public class AbwProjectTable extends Table {
	public void onRequest(Event evt) {
		setModel(new AbwProjectTableModel());
		setPageSize(20);
	}
	
private class AbwProjectTableModel extends TableModel{
		
		public AbwProjectTableModel(){
			TableColumn col = new TableColumn("namax", "Table Listing");
			col.setUrlParam("namax");
			addColumn(col);
			
						
		}

		
		public Collection getTableRows() {
			
			AbwModule module = (AbwModule) Application.getInstance().getModule(AbwModule.class);
			
			return module.abw_projectListing(getSort(),isDesc(),getStart(), getRows());
			
		}

		
		public int getTotalRowCount() {
			AbwModule module = (AbwModule) Application.getInstance().getModule(AbwModule.class);
			
			return module.abw_projectCount();
		}
		
		
	}
	
}

