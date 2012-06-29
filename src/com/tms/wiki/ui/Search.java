package com.tms.wiki.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiModule;

public class Search extends LightWeightWidget{
	private Collection articles;
	private int pageSize;
	private int currentPage;
	private String keyword;
		
	public void onRequest(Event event) {
        super.onRequest(event);
        String pageStr = event.getRequest().getParameter("page");
        if(pageStr!=null)
        	currentPage = Integer.parseInt(pageStr);
        else 
        	currentPage = 1;

        keyword = event.getRequest().getParameter("keyword");        
        WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
		if(keyword!=null && keyword.length()>0)
			articles = module.search(keyword, getStart(), 5);	
		
    }
	
	public int getPageCount() {
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);
				
		int totalRows = module.searchCount(keyword);
		int pageCount = (int) Math.ceil(((double) totalRows) / 5);
		
        return (pageCount > 0) ? pageCount : 1;		 
	}
	
	
	public String getDefaultTemplate() {
    	return "wiki/search";
    }

	public Collection getArticles() {
		return articles;
	}

	public void setArticles(Collection articles) {
		this.articles = articles;
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


