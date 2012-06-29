package com.tms.wiki.ui;

import com.tms.hr.employee.model.EmployeeException;
import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiConverter;
import com.tms.wiki.model.WikiModule;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 2:10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShowPreview extends LightWeightWidget {
    
    private String previewStory;

    public void onRequest(Event event) { 	  	
    	
        super.onRequest(event);
        String story = (String) event.getRequest().getSession().getAttribute(CreateArticle.PREVIEW_STORY); 
        previewStory = WikiConverter.convertToHtml(story);
    }    
    
    public String getTemplate() {
        return "wiki/showPreview";
    }

	public String getPreviewStory() {
		return previewStory;
	}

	public void setPreviewStory(String previewStory) {
		this.previewStory = previewStory;
	}
}
