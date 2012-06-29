package com.tms.wiki.ui;

import java.util.Collection;

import kacang.Application;
import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiModule;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 2:08:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecentChanges extends LightWeightWidget{
    private String articleId;
    private WikiArticle article;
    private Collection articles;

    public void onRequest(Event event) {
        super.onRequest(event);
        WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
        articles = module.getRecentChanges() ;
    }

    public WikiArticle getArticle(String articleId) {
            return article;
    }

    public String getDefaultTemplate() {
        return "wiki/recentChanges";
    }        

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

	public Collection getArticles() {
		return articles;
	}

	public void setArticles(Collection articles) {
		this.articles = articles;
	}
    
}
