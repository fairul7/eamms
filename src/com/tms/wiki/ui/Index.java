package com.tms.wiki.ui;

import java.util.Collection;

import com.tms.wiki.model.WikiModule;

import kacang.Application;
import kacang.ui.LightWeightWidget;
import kacang.ui.Event;

public class Index extends LightWeightWidget{

	private Collection articles;
	private String key;
	
    public void onRequest(Event event) {
        
		WikiModule module = (WikiModule)Application.getInstance().getModule(WikiModule.class);	
		key = getKey();
		if(key==null)
			key = event.getRequest().getParameter("alphabet");
		articles = module.getIndexArticles(key);
	}   

    public String getDefaultTemplate() {
		return "wiki/index";
	}

	public Collection getArticles() {
		return articles;
	}

	public void setArticles(Collection articles) {
		this.articles = articles;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
	
	

}

