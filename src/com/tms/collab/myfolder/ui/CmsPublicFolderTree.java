package com.tms.collab.myfolder.ui;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

import kacang.Application;
import kacang.stdui.Tree;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class CmsPublicFolderTree extends Tree{
	
	public CmsPublicFolderTree() {
    }

    public CmsPublicFolderTree(String name) {
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
	    	MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
	    	FileFolder rootFolder = mf.formCmsPublicFolderTree();
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
		return new Forward("folderNavi");
	}
	
}
