package com.tms.wiki.ui;

import java.util.Collection;


import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.util.Log;

public class ArticleHistory extends LightWeightWidget {
    private String articleId;    
    private WikiArticle article;
    public Collection articleHistory;
    private String message;
    private int pageSize;
	private int currentPage;
	    
    public void onRequest(Event event) {
    	String pageStr = event.getRequest().getParameter("page");
        if(pageStr!=null)
        	currentPage = Integer.parseInt(pageStr);
        else 
        	currentPage = 1;
        
        articleId = event.getRequest().getParameter("articleId");        
        WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);
        String revisionId = event.getRequest().getParameter("revisionId"); 
        
        try {
			if (revisionId!=null){
				module.rollBack(revisionId);
				message = "Article changed to selected version ";
			}
		} catch (WikiException e) {
			Log.getLog(getClass()).error(e);
		}
        articleHistory = module.articleHistory(articleId, getStart(), 10);
        
    }
    
    public int getPageCount() {
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);
		WikiDao dao = (WikiDao) module.getDao();
		try {
			int totalRows = dao.selectVersionCount(articleId);
			int pageCount = (int) Math.ceil(((double) totalRows) / 10);
			
            return (pageCount > 0) ? pageCount : 1;

		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString());
			return 1;
		}
	}    
   
    public String getDefaultTemplate() {
    	return "wiki/history";
    }

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
	public WikiArticle getArticle() {
        Application application = Application.getInstance();
        WikiModule module = (WikiModule) application.getModule(WikiModule.class);
        try {
            article = module.viewArticle(articleId);
        } catch (WikiException e) {
            e.printStackTrace();
        }
        return article;
    }    

	public void setArticle(WikiArticle article) {
		this.article = article;
	}


	public Collection getArticleHistory() {
		return articleHistory;
	}

	public void setArticleHistory(Collection articleHistory) {
		this.articleHistory = articleHistory;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getStart() {
        return (getCurrentPage() - 1) * 10;
    }

	public int getNextIndex() {
        if (isLastPage()) {
            return -1;
        } else {
            return getCurrentPage() * 10;
        }
    }

   
    public int getPreviousIndex() {
        if (isFirstPage()) {
            return -1;
        } else {
            return (getCurrentPage() - 2) * 10;
        }
    }

    
    public boolean isFirstPage() {
        return (getCurrentPage() == 1);
    }

   
    public boolean isLastPage() {
        return (getCurrentPage() == getPageCount());
    }

}

	



