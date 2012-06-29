package com.tms.fms.abw.ui;


import java.util.Collection;
import java.util.Iterator;

import com.tms.fms.abw.model.AbwModule;


import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;

public class AbwTableContent extends Table {
	private TextField keyword;
	private String tableName;
	public void onRequest(Event evt) {
		setModel(new AbwTableContentTableModel());
		setPageSize(20);
	}
	
	
	
public String getTableName() {
		return tableName;
	}



	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



private class AbwTableContentTableModel extends TableModel{
		
		public AbwTableContentTableModel(){
			
			AbwModule abw = (AbwModule)Application.getInstance().getModule(AbwModule.class);
			Collection col = abw.columnListing(getTableName());			
			for(Iterator iter=col.iterator(); iter.hasNext();){
				DefaultDataObject  obj = (DefaultDataObject)iter.next();
				TableColumn tblCol = new TableColumn(""+ obj.getProperty("column_name"), ""+obj.getProperty("column_name"));
				//tblCol.setSortable(false);
				
				addColumn(tblCol);
			}
			
			
			TableFilter keywordFilter = new TableFilter("keywordFilter");
	    	keyword = new TextField("keyword");
	    	keyword.setSize("20");
	    	keywordFilter.setWidget(keyword);
	    	addFilter(keywordFilter);
						
		}

		
		public Collection getTableRows() {
			
			AbwModule module = (AbwModule) Application.getInstance().getModule(AbwModule.class);
			
			return module.abwTableListing((String) keyword.getValue(),getTableName(),getSort(),isDesc(),getStart(), getRows());
			
		}

		
		public int getTotalRowCount() {
			AbwModule module = (AbwModule) Application.getInstance().getModule(AbwModule.class);
			
			return module.abwTableCount((String) keyword.getValue(),getTableName());
		}
		
				
	}
	
}

