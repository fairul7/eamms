package com.tms.collab.myfolder.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.util.Log;

public class DeletedUserFolderSelectBox extends SelectBox{
	

	private boolean parentSelection;
	private Map folderMap;
	private String folderIdForParentSelection;
	//private Collection folders;
	
	public DeletedUserFolderSelectBox() {
    }

    public DeletedUserFolderSelectBox(String name) {
        super(name);
    }

    public void init() {
        initField();
    }

    public void onRequest(Event evt) {
        initField();
    }
    
    
    
    public void initField(){
    	
    	try{
    		folderMap = new SequencedHashMap();
    		Application app = Application.getInstance();
            
            //if (isParentSelection()) {
                folderMap.put("0", Application.getInstance().getMessage("messaging.label.folder","--- Folder ---"));
            //}
            
            MyFolderModule mf = (MyFolderModule)app.getModule(MyFolderModule.class);
            FileFolder rootFolder = mf.formDeletedUserFolderTree();
            
            populateFolderMap(rootFolder);
            
            this.setOptionMap(folderMap);
    	}
    	catch(Exception e){
    		Log.getLog(getClass()).error("error in init folder select box field: " + e.getMessage(), e);
    	}
    }
    

    private void populateFolderMap(FileFolder rootFolder) {
        
    	//add the root into the map
    	//folderMap.put(rootFolder.getId(), rootFolder.getFileName());
    	
    	populateFolderMapRecur(0, rootFolder);
    }
    
    private void populateFolderMapRecur(int level, FileFolder folder){
    	
    	Collection subFolders = folder.getSubFolders();
    	
    	if(subFolders != null){
    		for(Iterator i = subFolders.iterator(); i.hasNext(); ){
    			FileFolder f = (FileFolder)i.next();
                
                StringBuffer buffer = new StringBuffer();
                for (int j=0; j<level; j++) {
                    buffer.append("...");
                }
                if (level > 0) {
                    buffer.append("| ");
                }
                buffer.append(f.getFileName());
                String title = buffer.toString();
                if (title.length() > 30) {
                    title = title.substring(0, 30) + "..";
                }
                
                folderMap.put(f.getMfId(), title);
                populateFolderMapRecur(level+1, f);
    		}
    	}
    	
    }
    
    public String getSelectedFolderId() {
        Map selected = getSelectedOptions();
        Map options = getOptionMap();
        String key = null;
        String folderId = null;

        if (selected != null && selected.size() > 0) {
            key = selected.keySet().iterator().next().toString();
        }
        else {
            if (options != null && options.size() > 0) {
                key = options.keySet().iterator().next().toString();
            }
        }
        if (key != null) {
            try {
                Application app = Application.getInstance();
                MyFolderModule mm = (MyFolderModule)app.getModule(MyFolderModule.class);
                FileFolder f = mm.loadFileFolder(key);
                folderId = f.getId();
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Unable to retrieve folder: " + key, e);
            }
        }
        return (folderId != null) ? folderId : "";
    }
    
    
	/**
	 * @return Returns the folderIdForParentSelection.
	 */
	public String getFolderIdForParentSelection() {
		return folderIdForParentSelection;
	}
	/**
	 * @param folderIdForParentSelection The folderIdForParentSelection to set.
	 */
	public void setFolderIdForParentSelection(String folderIdForParentSelection) {
		this.folderIdForParentSelection = folderIdForParentSelection;
	}
	/**
	 * @return Returns the parentSelection.
	 */
	public boolean isParentSelection() {
		return parentSelection;
	}
	/**
	 * @param parentSelection The parentSelection to set.
	 */
	public void setParentSelection(boolean parentSelection) {
		this.parentSelection = parentSelection;
	}

}
