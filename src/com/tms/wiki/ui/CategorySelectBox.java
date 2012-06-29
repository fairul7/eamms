package com.tms.wiki.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.wiki.model.WikiCategory;
import com.tms.wiki.model.WikiModule;

import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.stdui.*;
                         
/**
 * User: hima
 * Date: Dec 15, 2006
 * Time: 3:34:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategorySelectBox extends SelectBox{ 
	private Map folderMap;
	private String categoryId;

    public CategorySelectBox() {
        super();
    }

    public CategorySelectBox(String s) {
        super(s);
    }
    
    public CategorySelectBox(String s,String categoryId) {
        super(s);
        this.categoryId = categoryId;
    }

     public void onRequest(Event evt) {
        super.onRequest(evt);
    }

    public void initField() {
        folderMap = new SequencedHashMap();
        WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
        WikiCategory wc = module.getFolderTree(categoryId);
        populateFolderMap(wc);
        
        setOptionMap(folderMap);
    }

     private void populateFolderMap(WikiCategory root) {
    	folderMap.put(root.getCategoryId(), root.getCategory());
    	populateFolderMapRecursion(1, root);
    }

    private void populateFolderMapRecursion(int level, WikiCategory folder){
    	Collection subFolders = folder.getChildren();

    	if(subFolders != null){
    		for(Iterator i = subFolders.iterator(); i.hasNext(); ){
    			WikiCategory f = (WikiCategory)i.next();

                StringBuffer buffer = new StringBuffer();
                for (int j=0; j<level; j++) {
                    buffer.append("...");
                }
                if (level > 0) {
                    buffer.append("| ");
                }
                buffer.append(f.getCategory());
                String title = buffer.toString();
                if (title.length() > 30) {
                    title = title.substring(0, 30) + "..";
                }

                folderMap.put(f.getCategoryId(), title);
                populateFolderMapRecursion(level+1, f);
    		}
    	}
    }

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
    
    
}
