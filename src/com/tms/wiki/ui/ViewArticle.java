package com.tms.wiki.ui;

import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

/**
 * Created by IntelliJ IDEA. User: hima Date: Dec 14, 2006 Time: 2:10:45 PM To
 * change this template use File | Settings | File Templates.
 */
public class ViewArticle extends LightWeightWidget {
	private String articleId;

	private WikiArticle article;

	public void onRequest(Event event) {
		super.onRequest(event);
		articleId = event.getRequest().getParameter("articleId");
		
	}

	public String getDefaultTemplate() {
		return super.getDefaultTemplate();
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

	public String getTemplate() {
		return "wiki/viewArticle";
	}
}
