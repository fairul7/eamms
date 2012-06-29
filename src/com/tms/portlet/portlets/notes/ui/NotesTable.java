package com.tms.portlet.portlets.notes.ui;

import com.tms.portlet.portlets.notes.model.NotesException;
import com.tms.portlet.portlets.notes.model.NotesModule;
import com.tms.util.FormatUtil;

import java.util.Collection;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;


public class NotesTable extends Table {
	
	public void init() {
		setModel(new NotesTableModel());
	}

	public String getDefaultTemplate() {	
		 return super.getDefaultTemplate();				 
	}
	
	
	public class NotesTableModel extends TableModel {
		
		
		
		public NotesTableModel() {
		  
		  
		  TableColumn contentColumn = new TableColumn("content", "Content");
		  contentColumn.setSortable(false);
		  contentColumn.setUrlParam("notesId");		  
		  //contentColumn.setFormat(new TableStringFormat(0,500));
		  
		 
		 
		  TableColumn lastModifiedColumn = new TableColumn("lastModified", "Last Modified");
		  lastModifiedColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
		  
		  
		  
		  addColumn(contentColumn);		  		  
		  addColumn(lastModifiedColumn);
		  
			
		  addFilter(new TableFilter("content"));
		  
		  addAction(new TableAction("delete", "Delete", "Click on OK to confirm"));
		}
		
		public Collection getTableRows() {
			String content = (String)getFilterValue("content");			
			try {
				Application application = Application.getInstance();
				NotesModule module = (NotesModule) application.getModule(NotesModule.class);
				boolean isDesc = (getSort() == null) ? true : isDesc(); 								
				return module.findNotes(content, getSort(), isDesc , getStart(), getRows());
			}
			catch (NotesException e) {
				throw new RuntimeException(e.toString());
			}
						
			
		}
		
		public int getTotalRowCount() {
			
			try {
				Application application = Application.getInstance();
				NotesModule module = (NotesModule)application.getModule(NotesModule.class);
				return module.countNotes();
			}
			catch (NotesException e) {
				throw new RuntimeException(e.toString());
			}			
			
		}
		
		public String getTableRowKey() {
			return "notesId";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			
			
			if ("delete".equals(action)) {												
				Application application = Application.getInstance();
				NotesModule module = (NotesModule)application.getModule(NotesModule.class);
				for (int i=0; i<selectedKeys.length; i++) 
					module.deleteNotes(selectedKeys[i]);									 
			}
			return null;
		}
				
	}	
		

}
