package com.tms.wiki.ui;


import java.util.ArrayList;
import java.util.Collection;

import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

public class AdvanceSearch extends LightWeightWidget{
	
	private Collection articles;	
	private int pageSize;
	private int currentPage;
	private String keyword;
	private boolean categories = false, tagsCollection = false , titleCollection = false, articleCollection=false ;	
	public void onRequest(Event event) {
        super.onRequest(event);
        keyword = event.getRequest().getParameter("keyword");
        String category = event.getRequest().getParameter("category");
        String tags = event.getRequest().getParameter("tags");
        String title = event.getRequest().getParameter("title");
        String article = event.getRequest().getParameter("article");
        
        articles = new ArrayList(); 
        
        String pageStr = event.getRequest().getParameter("page");
        if(pageStr!=null)
        	currentPage = Integer.parseInt(pageStr);
        else 
        	currentPage = 1;
              
        WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
				
		
		if(category!=null ){
			categories = true;
		}
		if(tags!=null){
			tagsCollection = true;
		}
		if(title!=null){
			titleCollection  = true;
		}
		if(article!=null){
			articleCollection = true;
		}
		if(keyword!=null  && (categories || tagsCollection || titleCollection || articleCollection)){
			articles = module.advanceSearch(keyword, categories, tagsCollection, titleCollection, articleCollection, 0, 5);
		}		
		
    }
	
	public int getPageCount() {
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);
				
		int totalRows = module.advanceSearchCount(keyword, categories, tagsCollection, titleCollection, articleCollection);
		int pageCount = (int) Math.ceil(((double) totalRows) / 5);
		
        return (pageCount > 0) ? pageCount : 1;		 
	}
	
	
	public String getDefaultTemplate() {
    	return "wiki/advanceSearchResults";
    }

	public Collection getArticles() {
		return articles;
	}

	public void setArticles(Collection articles) {
		this.articles = articles;
	}	
	
		
	public int getSearchCount() {
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);
		WikiDao dao = (WikiDao) module.getDao();
		try {
			int totalRows = dao.selectMyArticlesCount(keyword);
			int pageCount = (int) Math.ceil(((double) totalRows) / 5);
			
            return (pageCount > 0) ? pageCount : 1;

		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString());
			return 1;
		}
	}	
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getStart() {
        return (getCurrentPage() - 1) * 5;
    }

	public int getNextIndex() {
        if (isLastPage()) {
            return -1;
        } else {
            return getCurrentPage() * 5;
        }
    }
   
    public int getPreviousIndex() {
        if (isFirstPage()) {
            return -1;
        } else {
            return (getCurrentPage() - 2) * 5;
        }
    }
    
    public boolean isFirstPage() {
        return (getCurrentPage() == 1);
    }

   
    public boolean isLastPage() {
        return (getCurrentPage() == getPageCount());
    }

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}	
    
}
