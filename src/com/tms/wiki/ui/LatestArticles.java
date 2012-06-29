package com.tms.wiki.ui;

import java.util.Collection;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import com.tms.wiki.model.WikiModule;

public class LatestArticles extends LightWeightWidget{
public Collection articles;
	
    public void onRequest(Event event) {
        super.onRequest(event);
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);		
		
		articles = module.getArticles();
	}   

    public String getDefaultTemplate() {
		return "wiki/latestArticles";
	}

	public Collection getArticles() {
		return articles;
	}

	public void setArticles(Collection articles) {
		this.articles = articles;
	}    

}
