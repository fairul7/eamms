/*
 * Created on Jun 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.myfolder.model.MyFolderModule;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QuotaTable extends Table{
	
	public QuotaTable(){
		super();
	}
	
	public QuotaTable(String name){
		super(name);
	}
	
	public void init() {
        setModel(new QuotaTableModel());
    }
	
	
	
	class QuotaTableModel extends TableModel{
		
		public QuotaTableModel(){
			Application app = Application.getInstance();
			
			TableColumn nameCol = new TableColumn("groupName", app.getMessage("security.label.group"));
            addColumn(nameCol);

            TableColumn quotaCol = new TableColumn("folderQuota", app.getMessage("messaging.label.quota"));
            quotaCol.setFormat(new TableFormat() {
                public String format(Object value) {
                    String result;
                    long quota;
                    try {
                        quota = Long.parseLong(value.toString());
                    }
                    catch (Exception e) {
                        return value.toString();
                    }
                    if (quota >= 1024) {
                        double db = (((double)quota)/1024);
                        result = new DecimalFormat("0.00").format(db) + " MB";
                    }
                    else {
                        result = quota + " KB";
                    }
                    return result;
                }
            });
            addColumn(quotaCol);

            addAction(new TableAction("delete", app.getMessage("general.label.delete", "Delete"), app.getMessage("messaging.label.confirmDelete", "Confirm Deletion?"))); 
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                try {
                	MyFolderModule mf = (MyFolderModule) Application.getInstance().getModule(MyFolderModule.class);
                	mf.deleteGroupQuota(selectedKeys);
                	
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error deleting principal quota", e);
                    return new Forward("error");
                }
                return new Forward("delete");
            }
            else {
                return null;
            }
        }
		
		public Collection getTableRows(){
			try{
				MyFolderModule mf = (MyFolderModule) Application.getInstance().getModule(MyFolderModule.class);
				Collection list = mf.getGroupQuotaList(null, getSort(), isDesc(), getStart(), getRows());
				return list;
			}
			catch(Exception e){
				Log.getLog(getClass()).error("Error retrieving quota list", e);
				return new ArrayList();
			}
		}
		
		public int getTotalRowCount(){
			try{
				MyFolderModule mf = (MyFolderModule) Application.getInstance().getModule(MyFolderModule.class);
				int count = mf.getGroupQuotaCount(null);
				return count;
			}
			catch(Exception e){
				Log.getLog(getClass()).error("Error retrieving group count", e);
				return 0;
			}
		}
		
		public String getTableRowKey() {
	        return "id";
	    }
		
	}

}
