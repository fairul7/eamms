/*
 * Created on Jun 29, 2005
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
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import com.tms.collab.myfolder.model.MyFolderException;
import com.tms.collab.myfolder.model.MyFolderModule;
import com.tms.util.FormatUtil;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SharedFileListTable extends Table{
	
	public String mfId;
	
	public void init() {
        super.init();
        setModel(new SharedFileListTableModel());
        setWidth("100%");
    }
	
	class SharedFileListTableModel extends TableModel{
		
		public SharedFileListTableModel(){
			
			String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
			
			TableColumn tcFileName = new TableColumn("fileName", Application.getInstance().getMessage("mf.label.fileName","File Name"));
			tcFileName.setUrlParam("mfId");
			tcFileName.setUrl(contextPath + "/myfolder/downloadFile");
			
			addColumn(tcFileName);
			
			TableColumn tcFileType = new TableColumn("fileType", Application.getInstance().getMessage("mf.label.fileType","File Type"));
            addColumn(tcFileType);
            
			TableColumn tcFileSize = new TableColumn("fileSize", Application.getInstance().getMessage("mf.label.fileSize", "File Size"));
			
			tcFileSize.setFormat(new TableFormat() {
                public String format(Object value) {
                    String result;
                    double size;
                    try {
                    	size = Double.parseDouble(value.toString());
                    }
                    catch (Exception e) {
                        return value.toString();
                    }
                    if (size >= 1024) {
                        double db = (((double)size)/1024);
                        result = new DecimalFormat("0.00").format(db) + " MB";
                    }
                    else {
                        result = size + " KB";
                    }
                    return result;
                }
            });
			
			addColumn(tcFileSize);
			
            TableColumn tcFileDescription = new TableColumn("fileDescription", Application.getInstance().getMessage("mf.label.fileDescription", "File Description"));
			addColumn(tcFileDescription);
			
			TableColumn tcFileLastModified = new TableColumn("lastModifiedDate", Application.getInstance().getMessage("mf.label.lastModified", "Last Modified"));
			tcFileLastModified.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
			addColumn(tcFileLastModified);
			
			addFilter(new TableFilter("fileName"));
		}
		

		public String getTableRowKey(){
			return "mfId";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			return new Forward("");
		}
		
		public int getTotalRowCount(){
			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			String fileName = (String) getFilterValue("fileName");
			
			try{
				if(mfId == null || mfId.equals("")){
					mfId = "-1";
				}
				
				return mf.getSharedFilesCount(fileName, getWidgetManager().getUser().getId(), mfId);
			}
			catch(MyFolderException e){
				Log.getLog(getClass()).error("Error getting file folder table rows count");
				return 0;
			}
		}
		
		public Collection getTableRows(){

			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			String fileName = (String) getFilterValue("fileName");
			
			try{
				//get the root folder when page first load
				if(mfId == null || mfId.equals("")){
					mfId = "-1";
				}
				
				return mf.getSharedFilesList(fileName, mfId ,getWidgetManager().getUser().getId(), getSort(), isDesc(), getStart(), getRows());
				
			}
			catch(MyFolderException e){
				Log.getLog(getClass()).error("Error getting file folder table rows");
				return new ArrayList();
			}
		}
		
	}
	

	/**
	 * @return Returns the mfId.
	 */
	public String getMfId() {
		return mfId;
	}
	/**
	 * @param mfId The mfId to set.
	 */
	public void setMfId(String mfId) {
		this.mfId = mfId;
	}
}
