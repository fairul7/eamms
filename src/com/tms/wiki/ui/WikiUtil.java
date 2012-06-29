package com.tms.wiki.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.tms.wiki.model.WikiCategory;
import com.tms.wiki.model.WikiModule;

import kacang.Application;


public class WikiUtil {
	
	public Collection getArticleCategories(String moduleId) {
		Collection col = new ArrayList();
		WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
		//col = module.getArticleCategories(moduleId);
		WikiCategory categories = module.getArticleCategoriesList(moduleId);
		
		populateFolderMapRecursion(1, categories, col);
		/*for (Iterator iter = col.iterator(); iter.hasNext();) {
			WikiCategory temp = (WikiCategory) iter.next();
			System.out.println("last"+temp.getCategory()+ " "+ temp.getParentId());
			
		}*/
		return col;
	}	
	
	
	private void populateFolderMapRecursion(int level, WikiCategory folder, Collection list){
    	Collection subFolders = folder.getChildren();
    	    	
    	if(subFolders != null){
    		for(Iterator i = subFolders.iterator(); i.hasNext(); ){
    			WikiCategory temp = (WikiCategory)i.next();
    			
                StringBuffer buffer = new StringBuffer();
                for (int j=0; j<level; j++) {
                    buffer.append("...");
                }
                if (level > 0) {
                    buffer.append("| ");
                }
                buffer.append(temp.getCategory());
                String title = buffer.toString();
                if (title.length() > 30) {
                    title = title.substring(0, 30) + "..";
                }
                temp.setCategory(title);
                list.add(temp);
                populateFolderMapRecursion(level+1, temp, list);
    		}
    	}
    	//return list;
    }

}
