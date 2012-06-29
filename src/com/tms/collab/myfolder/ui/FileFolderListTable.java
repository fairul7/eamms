/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
import com.tms.collab.project.Project;
import com.tms.util.FormatUtil;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileFolderListTable extends Table{
	
	public String mfId;
	private FileFolder fileFolder;

	public void init() {
        super.init();       
        setModel(new FileFolderListTableModel());
        setWidth("100%");
    }
	
	public void onRequest(Event evt) {
		try{
			setCurrentPage(1);
			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			
			if(mfId != null && !mfId.equals("")){
				SelectBox sb = (SelectBox) getModel().getFilter("folder").getWidget();
				sb.setSelectedOptions(new String[]{mfId});
			}else{
				mfId = mf.getRootFolder(getWidgetManager().getUser().getId());
				SelectBox sb = (SelectBox) getModel().getFilter("folder").getWidget();
				sb.setSelectedOptions(new String[]{mfId});
			}
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in file folder list table: " + e.getMessage(), e);
		}
	}
	
	class FileFolderListTableModel extends TableModel{
		
		private FolderSelectBox parentFolder;
		
		public FileFolderListTableModel(){
			
//			TableColumn tcFileName = new TableColumn("fileName", Application.getInstance().getMessage("mf.label.fileName","File Name"));
//			tcFileName.setUrlParam("mfId");
//			addColumn(tcFileName);
			
			TableColumn tcFileName2 = new TableColumn("mfId", Application.getInstance().getMessage("mf.label.fileName","File Name"));
			TableFormat fileNameFormat = new TableFileNameFormat("?cn=mainPg.folderTree&id=", "/myfolder/downloadFile?mfId=");
			tcFileName2.setFormat(fileNameFormat);
			addColumn(tcFileName2);
			
			TableFormat typeFormat = new TableFileTypeFormat();
			TableColumn tcFileTypeIcon = new TableColumn("fileType", "");
			tcFileTypeIcon.setFormat(typeFormat);
			addColumn(tcFileTypeIcon);
			
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
			
			TableColumn tcFileEdit = new TableColumn("mfId", "");
			tcFileEdit.setFormat(new TableFormat(){
				public String format(Object value){
					StringBuffer buf = new StringBuffer();
					buf.append("<a href='edit.jsp?id=");
					buf.append((String)value);
					buf.append("'> ");
					buf.append(Application.getInstance().getMessage("mf.label.edit","Edit"));
					buf.append("</a>");
					return buf.toString();
				}
			});
			
			
			addColumn(tcFileEdit);
			
			addFilter(new TableFilter("fileName"));
			
			addAction(new TableAction("move", Application.getInstance().getMessage("mf.label.moveFileFolder","Move File(s)"), Application.getInstance().getMessage("mf.message.moveSelectedFileFolder","Move selected file(s) to selected folder?")));
			addAction(new TableAction("delete", Application.getInstance().getMessage("mf.label.delete","Delete"), Application.getInstance().getMessage("mf.message.deleteSelectedFileFolder","All file(s) or folder(s) in this directory will be deleted. Confirm?")));
			addAction(new TableAction("upload", Application.getInstance().getMessage("mf.label.uploadFiles","Upload Files"), null));
			//addAction(new TableAction("editFolder", Application.getInstance().getMessage("mf.label.editSelectedFolder","Edit Selected Folder"), null));
			
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
						mf.deleteFilesFolders(getWidgetManager().getUser().getId(), selectedKeys);
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
		                	evt.getRequest().setAttribute("folderId", folderId);
		                	//movePhysicalFilesFolders(selectedKeys, folderId);
		                	mf.moveFilesFolders(selectedKeys, folderId);
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
//			else if("editFolder".equals(action)){
//				SelectBox sbFolder = (SelectBox) getFilter("folder").getWidget();
//                List folders = (List) sbFolder.getValue();
//                String folderId = "";
//                FileFolder f = new FileFolder();
//                
//                try{
//                	if (folders.size() > 0) {
//                        folderId = (String) folders.get(0);
//                        f = mf.loadFileFolder(folderId);
//                    }
//                	
//                	if(!"0".equals(f.getParentId())){
//        				evt.getRequest().setAttribute("folderId", folderId);
//        				return new Forward("editFolder");
//                	}else{
//                		return new Forward("invalid");
//                	}
//                }
//                catch(MyFolderException e){
//                	Log.getLog(getClass()).error("error in editing folder: " + e.getMessage(), e);
//                	return new Forward("error");
//                }
//			}
			else if("upload".equals(action)){
				
//				SelectBox sbFolder = (SelectBox) getFilter("folder").getWidget();
//                List folders = (List) sbFolder.getValue();
//                String folderId = "";
//				
//                if (folders.size() > 0) {
//                    folderId = (String) folders.get(0);
//                    evt.getRequest().setAttribute("folderId", folderId);
//                }
                
				return new Forward("upload");
			}
			else{
				return new Forward("move");
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
            TableFilter tfFolder;
            SelectBox selectFolder = new FolderSelectBox("selectFolder");
            tfFolder = new TableFilter("folder");
            tfFolder.setWidget(selectFolder);
            addFilter(tfFolder);
        }
		
		
		public int getTotalRowCount(){
			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			String fileName = (String) getFilterValue("fileName");
			
			try{
				if(mfId == null || mfId.equals("")){
					mfId = mf.getRootFolder(getWidgetManager().getUser().getId());
				}
				
				SelectBox sb = (SelectBox) getFilter("folder").getWidget();
	            List folders = (List) sb.getValue();
	            String folderId = "";
	            if (folders.size() > 0) {
	                folderId = (String) folders.get(0);
	            }
				
				return mf.getFilesFoldersCount(fileName, getWidgetManager().getUser().getId(), folderId);
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
					mfId = mf.getRootFolder(getWidgetManager().getUser().getId());
				}
				
				SelectBox sb = (SelectBox) getFilter("folder").getWidget();
	            List folders = (List) sb.getValue();
	            String folderId = "";
	            if (folders.size() > 0) {
	                folderId = (String) folders.get(0);
	            }
				
				return mf.getFilesFoldersList(fileName, folderId ,getWidgetManager().getUser().getId(), getSort(), isDesc(), getStart(), getRows());
				
			}
			catch(MyFolderException e){
				Log.getLog(getClass()).error("Error getting file folder table rows");
				return new ArrayList();
			}
		}
		

		/**
		 * @return Returns the parentFolder.
		 */
		public FolderSelectBox getParentFolder() {
			return parentFolder;
		}
		/**
		 * @param parentFolder The parentFolder to set.
		 */
		public void setParentFolder(FolderSelectBox parentFolder) {
			this.parentFolder = parentFolder;
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

	//Acquire folder name to be displayed accordingly on the title/label
 	public String getFileName() {
		MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
		String fileName = "";
        fileName = mf.getFileFolderPath(mfId, " > ");
        return fileName;
    }
}
