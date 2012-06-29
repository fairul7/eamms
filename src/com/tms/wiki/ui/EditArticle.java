package com.tms.wiki.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.hr.employee.model.EmployeeException;
import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;
import com.tms.wiki.stdui.TagsValidator;
import com.tms.wiki.stdui.WikiEditor;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 2:11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditArticle extends Form {
	
	public static final String PREVIEW_STORY ="previewStory";
	
    private String articleId;
    private WikiEditor article;
    private TextField editSummary;
    private TextField tags;
    private CategorySelectBox category;
    private Button preview;
    private Button submit;
    private Button cancel;
    private Panel panel;
    
    private Number revision;   
    

    public void init() {
        super.init();
        removeChildren();

        article = new WikiEditor("article");
        article.addChild(new ValidatorNotEmpty("vEmpty"));
        addChild(article);

        editSummary = new TextField("editSummary");
        editSummary.setSize("50");        
        addChild(editSummary);

        category = new CategorySelectBox("category");        
        addChild(category);

        tags = new TextField("tags");
        tags.addChild(new TagsValidator("vTags"));
        addChild(tags);

        preview = new Button("preview", "Preview");
        submit = new Button("submit", "Submit");
        cancel = new Button("cancel", "Cancel");
        panel = new Panel("panel");
        panel.addChild(preview);
        panel.addChild(submit);
        panel.addChild(cancel);
        addChild(new Label("l5"));
        addChild(panel);
        setMethod("post");
    }

    public void onRequest(Event event) {
        super.onRequest(event);
        init();
        /*articleId = event.getRequest().getParameter("articleId");*/
        if(articleId!=null) {
            populateArticle();
        }       
        
    }    
    
    public void populateArticle(){
        WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
        WikiDao dao = (WikiDao) module.getDao();
        try {
			WikiArticle wiki = module.viewArticle(articleId);
			if(wiki!=null){
				article.setValue(wiki.getStory());
				category.setCategoryId(wiki.getCategoryId());
				category.initField();
				category.setSelectedOption(wiki.getCategoryId());
				tags.setValue(wiki.getTags());
				revision = wiki.getRevision();
				
			}
		} catch (WikiException e) {			
			e.printStackTrace();
		}     
    }

    public Forward onSubmit(Event event) {
    	Forward fwd = super.onSubmit(event);
        String buttonName = findButtonClicked(event);
		if (cancel.getAbsoluteName().equals(buttonName)) {
            fwd = new Forward("cancel");
        } else if(preview.getAbsoluteName().equals(buttonName)){
        	event.getRequest().getSession().setAttribute(PREVIEW_STORY,(String)article.getValue());
        	return new Forward("preview");
        }
		return fwd;
    }

    public Forward onValidate(Event event) {
        Forward fwd = null;
        fwd = super.onValidate(event);
        String buttonName = findButtonClicked(event);
        if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
            fwd = editArticle(event.getRequest());
        } 
        return fwd;
    }
    
    protected Forward editArticle(HttpServletRequest req) {
        Forward forward = null;
        WikiArticle wa = new WikiArticle();
        wa.setArticleId(articleId);        
       
        List list = (List) category.getValue();
        wa.setCategory((String) list.get(0));
        wa.setTags((String)tags.getValue());  
        wa.setStory((String)article.getValue());
        wa.setModifiedBy(getWidgetManager().getUser().getId());
        wa.setModifiedOn(new Date());
        wa.setRevision(new Integer(revision.intValue()+1));
        wa.setRevisionId(UuidGenerator.getInstance().getUuid());
        wa.setEditSummary((String)editSummary.getValue());
        wa.setHostAddress(req.getRemoteAddr());

        try {
            Application application = Application.getInstance();
            WikiModule module = (WikiModule) application.getModule(WikiModule.class);
            module.editArticle(wa);
            forward = new Forward("success");
        } catch (Exception le) {
            forward = new Forward("Cancel");
        }
        return forward;
    }   
    
   
	public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articlId) {
        this.articleId = articlId;
     }

    public String getDefaultTemplate() {
        return "wiki/editArticle";
    }
}
