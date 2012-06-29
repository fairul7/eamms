/*
 * Created on Jun 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Tree;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FolderTree extends Tree{
	
	
	public FolderTree() {
    }

    public FolderTree(String name) {
        super(name);
    }

    public String getDisplayId() {
        return "id";
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
    		FileFolder rootFolder = mf.getFolderTree(user.getId(), user.getUsername());
    		setModel(rootFolder);
    		
    		String selectedId = getSelectedId();
    		if(selectedId == null){
    			setSelectedId(rootFolder.getId());
    		}
    	}
    	catch(Exception e){
    		Log.getLog(getClass()).error("Unable to retrieve folder tree: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve folder tree: " + e.toString());
    	}
    }
    
    
    public Forward actionPerformed(Event evt){
    	setSelectedId(evt.getParameter("id"));
		return new Forward("folderNavi");
	}
    
}
