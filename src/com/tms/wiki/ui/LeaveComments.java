package com.tms.wiki.ui;

import javax.servlet.http.HttpServletRequest;

import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiModule;
import com.tms.wiki.stdui.WikiEditor;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

public class LeaveComments extends Form{
	
	public static final String PREVIEW_STORY ="previewStory";
	
	private String articleId;
	
	private TextField name;
	private TextField email;
	private TextBox comments;
	private Button submit;
	private Button cancel;
	private Panel panel;
	
	public LeaveComments() {
		super();
	}

	public LeaveComments(String s) {
		super(s);
	}	
	
	public void onRequest(Event evt){
		init();
	}

	public void init(){
		super.init();
        setColumns(2);
        removeChildren();
        
        setWidth("100%");
        setMethod("post");
        
        addChild(new Label("l1", "Name"));
        name = new TextField("name");       
        name.addChild(new ValidatorNotEmpty("vEmpty","Can not be empty"));
        addChild(name);
                
        addChild(new Label("l2", "Email"));
        email = new TextField("email");
        email.addChild(new ValidatorEmail("vEmail","Not a valid email"));
        addChild(email);
        
        addChild(new Label("l3", "comments"));
        comments = new TextBox("comments");
        comments.addChild(new ValidatorNotEmpty("vEmtpy"));
        addChild(comments);
        
        submit = new Button("submit", "Submit");
        cancel = new Button("cancel", "Close");
        panel = new Panel("panel");
        panel.addChild(submit);
        panel.addChild(cancel);
        addChild(new Label("l5"));
        addChild(panel);
	}
		
		
	public Forward onSubmit(Event event) {
        Forward fwd = super.onSubmit(event);
        String buttonName = findButtonClicked(event);
		if (cancel.getAbsoluteName().equals(buttonName)) {
            fwd = new Forward("cancel");
        } 
		return fwd;
    }
		
	public Forward onValidate(Event event) {
		Forward forward = null;
		super.onValidate(event);
		String buttonName = findButtonClicked(event);
		if (submit.getAbsoluteName().equals(buttonName)) {
            forward = leaveComments(event.getRequest());
        } else if (cancel.getAbsoluteName().equals(buttonName)) {
            forward = new Forward("cancel");
        } 
        return forward;
    }
	
	public Forward leaveComments(HttpServletRequest req){
		Forward forward = null;
		WikiArticle wa = new WikiArticle();
		wa.setArticleId(articleId);
		wa.setComments((String)comments.getValue());
		wa.setCommentId(UuidGenerator.getInstance().getUuid());
		wa.setName((String)name.getValue());
		wa.setEmail((String)email.getValue());
		
		try {
            Application application = Application.getInstance();
            WikiModule module = (WikiModule) application.getModule(WikiModule.class);
            module.addComments(wa);
            forward = new Forward("success");
        } catch (Exception le) {
            forward = new Forward("Cancel");
        }
        return forward;
    }

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
	
	
}
