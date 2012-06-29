package com.tms.wiki.ui;

import java.util.Collection;

import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class CategoryTagSearch extends LightWeightWidget{
	
	private Collection articles;
	private int pageSize;
	private int currentPage;
	private String keyword;
	private String type;	
	
	public void onRequest(Event event) {
        super.onRequest(event);
        
        String pageStr = event.getRequest().getParameter("page");
        if(pageStr!=null)
        	currentPage = Integer.parseInt(pageStr);
        else 
        	currentPage = 1;

        keyword = event.getRequest().getParameter("keyword");   
        type = event.getRequest().getParameter("type");
        WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
		if(keyword!=null && keyword.length()>0){
			if(type.equals("tag")){
				articles = module.searchArticleByTag(keyword, getStart(), 5);
			} else if(type.equals("category")){
				articles = module.searchArticleByCategory(keyword, getStart(), 5);
			}
		}		
				
    }
	
	public int getPageCount() {
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);
		WikiDao dao = (WikiDao) module.getDao();		
		int totalRows = 0;
		
		try {
			if(type.equals("tag")){
				totalRows = dao.searchArticleByTagCount(keyword);
			} else {
				totalRows = dao.searchArticleByCategoryCount(keyword);
			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
	
		int pageCount = (int) Math.ceil(((double) totalRows) / 5);
		
        return (pageCount > 0) ? pageCount : 1;		 
	}
	
	
	public String getDefaultTemplate() {
    	return "wiki/categoryTagSearch";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	


}
