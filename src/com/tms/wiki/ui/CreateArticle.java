package com.tms.wiki.ui;

import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiCategory;
import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiModule;
import com.tms.wiki.stdui.TagsValidator;
import com.tms.wiki.stdui.WikiEditor;
import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CreateArticle extends Form {
	
	public static final String PREVIEW_STORY ="previewStory";
    
	private TextField title;
    private WikiEditor story;    
    //private CategorySelectBox category;
    private SelectBox category;
    private SelectBox module;
    private TextField tags;    
    private Button preview;
    private Button submit;
    private Button cancel;
    private Panel panel;
    private String categoryId; 
    private String moduleId;
    private String moduleName;
    private String moduleSelected;
    private String categorySelected="-1";

    public void init() {
        super.setColumns(2);
        super.init();
        removeChildren();
        setMethod("post");
        categorySelected = "-1";
        

        addChild(new Label("l1", "Title *"));
        title = new TextField("title");
        title.addChild(new ValidatorNotEmpty("vEmpty"));
        addChild(title);

        addChild(new Label("l2", "Story *"));
        story = new WikiEditor("story");
        story.addChild(new ValidatorNotEmpty("vStory"));
        addChild(story);
        
        addChild(new Label("l3", "Module *"));
        module = new SelectBox("module");
        module.addOption("-1", "Select any module..");
        module.setSelectedOption("-1");
        module.setOnChange("populateCategories();");
        module.addChild(new ValidatorSelectBox("vModule","Select One", "-1"));
        
        addChild(module);
        
        addChild(new Label("l4", "Categories"));
        category = new SelectBox("category");        
        //category.initField();        
        category.addOption("-1", "Select any category..");    
        category.setSelectedOption("-1");
        category.addChild(new ValidatorSelectBox("vCategory","Select One","-1"));
        addChild(category);

        addChild(new Label("l5", "tags"));
        tags = new TextField("tags");
        tags.addChild(new TagsValidator("vtags"));
        addChild(tags);       
        
        preview = new Button("preview", "Preview");
        submit = new Button("submit", "Submit");
        cancel = new Button("cancel", "Cancel");
        panel = new Panel("panel");
        panel.addChild(preview);
        panel.addChild(submit);
        panel.addChild(cancel);
        addChild(new Label("l6"));
        addChild(panel);
        
    }

    public void onRequest(Event event) {
        removeChildren();
    	init();    	        
    	super.onRequest(event);   
    	populateModules();
    }
    
    public void populateModules() {
        WikiModule wm = (WikiModule)Application.getInstance().getModule(WikiModule.class);
    	WikiDao dao = (WikiDao) wm.getDao();        
        try {
            Collection col = dao.selectModules();
            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                WikiArticle wa = (WikiArticle) iterator.next();            	
                module.addOption(wa.getModuleId(), wa.getModuleName());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }    

    public Forward onSubmit(Event event) {
    	Forward fwd = super.onSubmit(event);
        String buttonName = findButtonClicked(event);
        List listModule = (List) module.getValue();
        moduleSelected = (String) listModule.get(0);
        List list = (List) category.getValue();
        categorySelected = (String) list.get(0);
        
		if (cancel.getAbsoluteName().equals(buttonName)) {
            fwd = new Forward("cancel");
        } else if(preview.getAbsoluteName().equals(buttonName)){
        	event.getRequest().getSession().setAttribute(PREVIEW_STORY,(String)story.getValue());
        	return new Forward("preview");
        }
		return fwd;
    }

    public Forward onValidate(Event event) {
        Forward fwd = null;
        fwd = super.onValidate(event);
        String buttonName = findButtonClicked(event);
        if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
            fwd = createArticle(event.getRequest());
        }
        return fwd;
    }   
    
    @Override
	public Forward onValidationFailed(Event evt) {		
        List listModule = (List) module.getValue();
        moduleSelected = (String) listModule.get(0);
        List list = (List) category.getValue();
        categorySelected = (String) list.get(0);
        
        return super.onValidationFailed(evt);
	}

	protected Forward createArticle(HttpServletRequest req) {
        Forward forward = null;
        WikiArticle article = new WikiArticle();
        article.setArticleId(UuidGenerator.getInstance().getUuid());

        article.setTitle((String) title.getValue());
        article.setStory((String) story.getValue());
        article.setTags((String) tags.getValue());
        article.setCategory(categoryId);
        article.setModule(moduleId);
        article.setName("");
        
        Date currentDate = new Date();
        article.setCreatedBy(getWidgetManager().getUser().getId());
        article.setCreatedOn(currentDate);        
        article.setModifiedBy(getWidgetManager().getUser().getId());
        article.setModifiedOn(currentDate);
        article.setRevisionId(UuidGenerator.getInstance().getUuid());
        article.setRevision(new Integer(1));
        article.setHostAddress(req.getRemoteAddr());
        List list = (List) category.getValue();
        article.setCategory((String) list.get(0));
        List listModule = (List) module.getValue();
        article.setModule((String) listModule.get(0));        
        
        try {
            Application application = Application.getInstance();
            WikiModule module = (WikiModule) application.getModule(WikiModule.class);
            module.addArticle(article);
            forward = new Forward("success");
        } catch (Exception le) {
            forward = new Forward("cancel");
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return "wiki/createArticle";
    }

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getModuleSelected() {
		return moduleSelected;
	}

	public void setModuleSelected(String moduleSelected) {
		this.moduleSelected = moduleSelected;
	}

	public String getCategorySelected() {
		return categorySelected;
	}

	public void setCategorySelected(String categorySelected) {
		this.categorySelected = categorySelected;
	} 	
}

