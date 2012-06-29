package com.tms.wiki.ui;

import java.util.Collection;

import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

public class ViewArticleComments extends LightWeightWidget{
	private Collection comments;
	private String articleId;
	private String message;	
		
	public void onRequest(Event event) {		
		super.onRequest(event);
		articleId = event.getRequest().getParameter("articleId");
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);
		String commentId = event.getRequest().getParameter("commentId");
		try {
			if (commentId!=null){
				module.deleteComment(commentId);
				message = "Comment deleted";
			}
		} catch (WikiException e) {
			Log.getLog(getClass()).error(e);
		}	
		comments = module.getComments(articleId);
		 
        
        	
	}	

	public String getDefaultTemplate() {
		return "wiki/viewArticleComments";
	}


	public Collection getComments() {
		return comments;
	}

	public void setComments(Collection comments) {
		this.comments = comments;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	
}
