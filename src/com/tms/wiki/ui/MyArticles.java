package com.tms.wiki.ui;

import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiModule;

public class MyArticles extends LightWeightWidget{
	private Collection myArticles;
	private int pageSize;
	private int currentPage;
	public String userId;
	
    public void onRequest(Event event) {
        super.onRequest(event);
        String pageStr = event.getRequest().getParameter("page");
        if(pageStr!=null)
        	currentPage = Integer.parseInt(pageStr);
        else 
        	currentPage = 1;

        SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
        User user = security.getCurrentUser(event.getRequest()); 
        userId = user.getId();
        WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);		
		
		myArticles = module.getMyArticles(userId, getStart(), 5);
		
	}   

    public String getDefaultTemplate() {
		return "wiki/myArticles";
	}

	public Collection getMyArticles() {
		return myArticles;
	}

	public void setMyArticles(Collection myArticles) {
		this.myArticles = myArticles;
	}
	
	public int getPageCount() {
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);
		WikiDao dao = (WikiDao) module.getDao();
		try {
			int totalRows = dao.selectMyArticlesCount(userId);
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

}
