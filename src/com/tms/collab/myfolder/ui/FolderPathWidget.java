/*
 * Created on Jul 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import java.util.Properties;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.collab.myfolder.model.MyFolderException;
import com.tms.collab.myfolder.model.MyFolderModule;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FolderPathWidget extends Widget{
	
	private String mfId;
	private String path="";
	
	public void init(){
		getFolderPath();
	}

	public FolderPathWidget(){
	}
	
	public FolderPathWidget(String name){
		super(name);
	}
	
	public void onRequest(Event evt){

		Properties properties = Application.getInstance().getProperties();
		long upload = Long.parseLong(properties.getProperty("myfolder.upload.quota"));
		evt.getRequest().setAttribute("fileLimit", String.valueOf(upload));
		
		getFolderPath();
	}
	
	public void getFolderPath(){
		try{
			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			
			if(mfId == null){
				mfId = mf.getRootFolder(getWidgetManager().getUser().getId());
			}
			
			path = mf.getFolderPath(mfId);
			
		}
		catch(MyFolderException e){
			Log.getLog(getClass()).error("error in getting folder path: " + e.getMessage(), e);
		}
	}
	
	public String getDefaultTemplate(){
		return "myfolder/folderPath";
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
