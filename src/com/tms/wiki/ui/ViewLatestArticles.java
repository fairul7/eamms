package com.tms.wiki.ui;

import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 2:08:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewLatestArticles extends LightWeightWidget {     
    public Collection articles;

    public void onRequest(Event event) {
        super.onRequest(event);
        articles = listArticles();
    }

    public Collection listArticles() {
        return null;
    }

    public String getDefaultTemplate() {
        return "wiki/viewLatestArticles";
    }

    public Collection getArticles() {
        return articles;
    }

    public void setArticles(Collection articles) {
        this.articles = articles;
    }
}
