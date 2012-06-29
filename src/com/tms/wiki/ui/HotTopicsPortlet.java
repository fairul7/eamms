package com.tms.wiki.ui;

import java.util.Collection;

import com.tms.wiki.model.WikiModule;

import kacang.Application;
import kacang.ui.LightWeightWidget;
import kacang.ui.Event;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 2:38:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class HotTopicsPortlet extends LightWeightWidget{
	
	public Collection articles;

    public void onRequest(Event event) {
        super.onRequest(event);
        WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);		
		
		articles = module.getHotTopics();
    }

    public String getDefaultTemplate() {
		return "wiki/hotTopicsPortlet";
	}

	public Collection getArticles() {
		return articles;
	}

	public void setArticles(Collection articles) {
		this.articles = articles;
	}    
    
}
