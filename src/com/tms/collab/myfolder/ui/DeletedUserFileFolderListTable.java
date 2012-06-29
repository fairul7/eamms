package com.tms.collab.myfolder.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderException;
import com.tms.collab.myfolder.model.MyFolderModule;
import com.tms.util.FormatUtil;

public class DeletedUserFileFolderListTable extends Table{

	public String mfId;
	
	public void init() {
        super.init();
        setModel(new FileFolderListTableModel());
        setWidth("100%");
    }
	
	public void onRequest(Event evt){
		try{
			if(mfId != null){
				SelectBox sb = (SelectBox) getModel().getFilter("folder").getWidget();
				sb.setSelectedOptions(new String[]{mfId});
			}
			else{
				SelectBox sb = (SelectBox) getModel().getFilter("folder").getWidget();
				sb.setSelectedOptions(new String[]{"0"});
			}
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in file folder list table: " + e.getMessage(), e);
		}
	}
	
	class FileFolderListTableModel extends TableModel{
		
		public FileFolderListTableModel(){
			
			TableColumn tcFileName = new TableColumn("fileName", Application.getInstance().getMessage("mf.label.fileName","File Name"));
			tcFileName.setUrlParam("mfId");
			addColumn(tcFileName);
			
			TableColumn tcFileType = new TableColumn("fileType", Application.getInstance().getMessage("mf.label.fileType","File Type"));
            addColumn(tcFileType);
            
            TableColumn tcFileAccess = new TableColumn("fileAccess", Application.getInstance().getMessage("mf.label.fileAccess","File Access"));
            addColumn(tcFileAccess);
            
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
			
			TableColumn tcFileLastModified = new TableColumn("lastModifiedDate", Application.getInstance().getMessage("mf.label.lastModified", "Last Modified"));
			tcFileLastModified.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
			addColumn(tcFileLastModified);
			
			addFilter(new TableFilter("fileName"));
			
			addAction(new TableAction("move", Application.getInstance().getMessage("mf.label.moveFileFolder","Move File(s)"), Application.getInstance().getMessage("mf.label.moveSelectedFileFolder","Move selected file(s) to selected folder?")));
			addAction(new TableAction("delete", Application.getInstance().getMessage("mf.label.delete","Delete"), Application.getInstance().getMessage("mf.message.confirm","Confirm?")));
			
			addAction(new TableAction("editFolder", Application.getInstance().getMessage("mf.label.editSelectedFolder","Edit Selected Folder"), null));
			
			setFolderFilter();
		}
		
		public String getTableRowKey(){
			return "mfId";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			
			if("delete".equals(action)){
				if(selectedKeys != null || selectedKeys.length > 0){
					try{
						deletePhysicalFilesFolders(selectedKeys);
						mf.deleteFilesFolders(null, selectedKeys);
						return new Forward("delete");
					}
					catch(Exception e){
						Log.getLog(getClass()).error("error in file folder list table process Action: " + e.getMessage(), e);
						return new Forward("error");
					}
				}
				else{
					return new Forward("none");
				}
			}
			else if("move".equals(action)){
				if(selectedKeys != null || selectedKeys.length >0){
					
					SelectBox sbFolder = (SelectBox) getFilter("folder").getWidget();
	                List folders = (List) sbFolder.getValue();
	                String folderId = "";
	                if (folders.size() > 0) {
	                    folderId = (String) folders.get(0);
	                    
		                try{
		                	if(!"0".equals(folderId)){
		                		evt.getRequest().setAttribute("folderId", folderId);
			                	//movePhysicalFilesFolders(selectedKeys, folderId);
			                	mf.moveFilesFolders(selectedKeys, folderId);
		                	}
		                }
		                catch(Exception e){
		                	Log.getLog(getClass()).error("error in moving files or folders: " + e.getMessage(), e);
		                	return new Forward("error");
		                }
	                    
	                }
	                
					return new Forward("move");
				}else{
					return new Forward("none");
				}
			}
			else if("editFolder".equals(action)){
				SelectBox sbFolder = (SelectBox) getFilter("folder").getWidget();
                List folders = (List) sbFolder.getValue();
                String folderId = "";
                FileFolder f = new FileFolder();
                
                try{
                	if (folders.size() > 0) {
                        folderId = (String) folders.get(0);
                        f = mf.loadFileFolder(folderId);
                    }
                	
                	if(!"0".equals(f.getParentId()) && !"0".equals(folderId) ){
        				evt.getRequest().setAttribute("folderId", folderId);
        				return new Forward("editFolder");
                	}else{
                		return new Forward("invalid");
                	}
                }
                catch(MyFolderException e){
                	Log.getLog(getClass()).error("error in editing folder: " + e.getMessage(), e);
                	return new Forward("error");
                }
			}
			else{
				return null;
			}
		}
		
		public void deletePhysicalFilesFolders(String[] selectedKeys){
			
			try{
				if(selectedKeys != null || selectedKeys.length > 0){
					
					for(int i=0; i<selectedKeys.length; i++){
						deletePhysicalFilesFoldersRecur(selectedKeys[i]);
					}
				}
			}
			catch(Exception e){
				Log.getLog(getClass()).error("error in delete physical file folder: " + e.getMessage(), e);
			}
		}
		
		public void deletePhysicalFilesFoldersRecur(String mfId){
			
			StorageService ss;
			StorageFile file;
			
			Application application = Application.getInstance();
			
			try{	
				MyFolderModule mf = (MyFolderModule)application.getModule(MyFolderModule.class);
				ss = (StorageService)application.getService(StorageService.class);
				
				FileFolder temp = new FileFolder();
				FileFolder parent = new FileFolder();
				parent = mf.loadFileFolder(mfId);
				
				Collection childFilesFolders = mf.getChildFilesFolders(mfId);
				
				for(Iterator iterator = childFilesFolders.iterator(); iterator.hasNext();){
					temp = new FileFolder();
					temp = (FileFolder)iterator.next();
					deletePhysicalFilesFoldersRecur(temp.getId());
				}
				
				mf.logAction(getWidgetManager().getUser().getId(), mfId, "delete");
				
				if(!parent.isFolder()){
					if(parent.getFilePath() != null || "".equals(parent.getFilePath())){
						file = new StorageFile(parent.getFilePath());
						ss.delete(file);
					}else{
						Log.getLog(getClass()).error("!!!!!!!!!!!!! invalid file path ");
						return;
					}
				}
			}
			catch(Exception e){
				Log.getLog(getClass()).error("error in delete physical file folder: " + e.getMessage(), e);
			}
		}
		
		
		public void movePhysicalFilesFolders(String[] selectedKeys, String newParentId){
			StorageService ss;
			StorageFile source;
			StorageDirectory dest;
			
			Application application = Application.getInstance();
			FileFolder selected;
			FileFolder parent;
			
			try{
				if(selectedKeys != null || selectedKeys.length > 0){

					MyFolderModule mf = (MyFolderModule)application.getModule(MyFolderModule.class);
					ss = (StorageService)application.getService(StorageService.class);
					
					parent = new FileFolder();
					parent = mf.loadFileFolder(newParentId);
					dest = new StorageDirectory(parent.getFilePath());
					
					for(int i=0; i<selectedKeys.length; i++){
						selected = new FileFolder();
						selected = mf.loadFileFolder(selectedKeys[i]);
						source = new StorageFile(selected.getFilePath());
						
						ss.move(source, dest);
					}
				}
			}
			catch(Exception e){
				Log.getLog(getClass()).error("error in moving physical file folder: " + e.getMessage(), e);
			}
		}
		
		private void setFolderFilter(){
			SelectBox selectFolder = new DeletedUserFolderSelectBox("selectFolder");
			
			TableFilter tfFolder;
            tfFolder = new TableFilter("folder");
            tfFolder.setWidget(selectFolder);
            addFilter(tfFolder);
        }
		
		
		public int getTotalRowCount(){
			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			String fileName = (String) getFilterValue("fileName");
			
			try{
				SelectBox sb = (SelectBox) getFilter("folder").getWidget();
	            List folders = (List) sb.getValue();
	            String folderId = "";
	            if (folders.size() > 0) {
	                folderId = (String) folders.get(0);
	            }
				
				return mf.getDeletedUserFilesFoldersCount(fileName, folderId);
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
				SelectBox sb = (SelectBox) getFilter("folder").getWidget();
	            List folders = (List) sb.getValue();
	            String folderId = "";
	            if (folders.size() > 0) {
	                folderId = (String) folders.get(0);
	            }
				
				return mf.getDeletedUserFilesFoldersList(fileName, folderId, getSort(), isDesc(), getStart(), getRows());
			}
			catch(Exception e){
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
