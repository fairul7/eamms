/*
 * Created on Jun 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Tree;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SharedFolderTree extends Tree{
	
	private String[] selectedUsers;
	
	public SharedFolderTree() {
    }

    public SharedFolderTree(String name) {
        super(name);
    }

    public String getDisplayId() {
        return "mfId";
    }

    public String getDisplayProperty() {
        return "fileName";
    }
    
    public String getChildrenProperty() {
        return "subFolders";
    }
    
    public void onRequest(Event evt){
    	
    	try{
    		
	    	User user = getWidgetManager().getUser();
	    	MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
	    	FileFolder rootFolder = mf.formSharedFolderTree(null, user.getId());
	    	
	    	setModel(rootFolder);
	    	String selectedId = getSelectedId();
	    	if(selectedId == null){
	    		setSelectedId("0");
	    	}
    	}
    	catch(Exception e){
    		Log.getLog(getClass()).error("Unable to retrieve folder tree: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve folder tree: " + e.toString());
    	}
    }
    
    
    public Forward actionPerformed(Event evt){
		setSelectedId(evt.getParameter("id"));
		
		//new code
		try{
    		
	    	User user = getWidgetManager().getUser();
	    	MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
	    	FileFolder rootFolder = mf.formSharedFolderTree(null, user.getId());
	    	
	    	setModel(rootFolder);
	    	String selectedId = getSelectedId();
	    	if(selectedId == null){
	    		setSelectedId("0");
	    	}
	    	
	    	return new Forward("folderNavi");
    	}
    	catch(Exception e){
    		Log.getLog(getClass()).error("Unable to retrieve folder tree: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve folder tree: " + e.toString());
    	}
    	
	}
    
	/**
	 * @return Returns the selectedUsers.
	 */
	public String[] getSelectedUsers() {
		return selectedUsers;
	}
	/**
	 * @param selectedUsers The selectedUsers to set.
	 */
	public void setSelectedUsers(String[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
}
