/*
 * Created on Jun 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UpdateFileAccessWidget extends Widget{
	
	private String mfId;
	private String path;
	
	public void onRequest(Event evt){
		
		try{
    		MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
    		
    		path = "/storage" + updateFileAccess(evt);
    		
    		mf.logAction(Application.getInstance().getCurrentUser().getId(), mfId, "download");
    		
			evt.getRequest().setAttribute("path", path);
		}
		catch(Exception e){
			Log.getLog(getClass()).error("Unable to retrieve quota status: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve quota status: " + e.toString());
		}
	}
	
	public String updateFileAccess(Event evt){
		
		try{
			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			
			mf.updateFileAccessCount(mfId, getWidgetManager().getUser().getId());
			FileFolder f = mf.loadFileFolder(mfId);
			return f.getFilePath();
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in updateting file access: " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
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
