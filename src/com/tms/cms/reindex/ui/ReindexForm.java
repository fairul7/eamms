package com.tms.cms.reindex.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.reindex.model.ReindexContentObject;
import com.tms.cms.section.Section;

public class ReindexForm extends Form{
	
	private ReindexContentObject root;
	private String[] contentObjectClasses = new String[] {
	        Section.class.getName()
	    };
	private int contentCount = 0;
	private ArrayList selectedContent;
	
	private CheckBox[] chkContent;
	private Button submitBtn;
	
	public ReindexForm(){
		super();
	}
	
	public void init(){
		
	}
	
	public void onRequest(Event evt){
		initForm();
		submitBtn = new Button("submit", "Reindex");
		addChild(submitBtn);
		
	}
	
	public void initForm(){
		contentCount = 0;
		selectedContent = new ArrayList();
		
		Application app = Application.getInstance();
        ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
        try {
            // get current user
            User user = getWidgetManager().getUser();
            
            // get content tree root
            ContentObject root = contentManager.viewTreeWithOrphans(ContentManager.CONTENT_TREE_ROOT_ID, 
            		getContentObjectClasses(), false, ContentManager.USE_CASE_PREVIEW, user);
            
            contentObjectConversion(root);
            
            
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
		
	}
	
	public void contentObjectConversion(ContentObject root){
		 
		ReindexContentObject rObj = new ReindexContentObject(); 
		rObj = populateObject(root);
		rObj.setChildren(contentObjectConversionRecursive(root.getChildren()));
		
		this.root = new ReindexContentObject();
		this.root = rObj;
	}
	
	public Collection contentObjectConversionRecursive(Collection children){
		Collection col = new ArrayList();
		Iterator i = children.iterator();
		
		while(i.hasNext()){
			ContentObject obj = (ContentObject)i.next();
			ReindexContentObject rObj = new ReindexContentObject();  
			rObj = populateObject(obj);
			
			if(obj.getChildren().size() > 0){
				rObj.setChildren(contentObjectConversionRecursive(obj.getChildren()));
			}
			
			col.add(rObj);
		}
		
		return col;
	}
	
	public ReindexContentObject populateObject(ContentObject obj){
		++contentCount;
		ReindexContentObject r = new ReindexContentObject();
		r.setId(obj.getId());
		r.setName(obj.getName());
		r.setClassName(obj.getClassName());
		
		r.setApprovalDate(obj.getApprovalDate());
		r.setApprovalUser(obj.getApprovalUser());
		r.setApprovalUserId(obj.getApprovalUserId());
		r.setAuthor(obj.getAuthor());
		r.setVersion(obj.getVersion());
		r.setTemplate(obj.getTemplate());
		r.setSummary(obj.getSummary());
		r.setCheckedOut(obj.isCheckedOut());
		r.setCheckOutDate(obj.getCheckOutDate());
		r.setCheckOutUser(obj.getCheckOutUser());
		r.setCheckOutUserId(obj.getCheckOutUserId());
		r.setComments(obj.getComments());
		
		String checkboxName = new String("cbContent");
		checkboxName = checkboxName + contentCount;
		CheckBox tempChkBx = new CheckBox("cbContent" + contentCount);
		tempChkBx.setValue(obj.getId().toString());
		r.setChkBx(tempChkBx);
		addChild(r.getChkBx());
		
		return r;
	}
	
	public Forward onValidate(Event evt) {
		
		String button = findButtonClicked(evt);
		
		if (button.endsWith("submit")) {
			browseTree();
			
			if(selectedContent != null && selectedContent.size() > 0){
				
				for(int i=0; i<selectedContent.size(); i++){
					try{
						ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
					    cm.reindexTree(selectedContent.get(i).toString(), getWidgetManager().getUser());
					}
					catch(ContentException e){
						Log.getLog(getClass()).error("Error reindex key - " + selectedContent.get(i) + ": " + e.getMessage());
						return new Forward("error");
					}
					
					try{
						ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
						cp.reindexTree(selectedContent.get(i).toString(), getWidgetManager().getUser());
					}
					catch(ContentException e){
						Log.getLog(getClass()).error("Error reindex key - " + selectedContent.get(i) + ": " + e.getMessage());
						return new Forward("error");
					}
				}
				
				initForm();
				return new Forward("success");
			}else{
				return new Forward("nonselect");
			}
		}
		
		return new Forward("error");
	}
	
	public void browseTree(){
		if(root.getChkBx().isChecked()){
			selectedContent.add(root.getId());
		}else{
			Iterator i = root.getChildren().iterator();
			
			while(i.hasNext()){
				ReindexContentObject obj = (ReindexContentObject)i.next();
				
				if(obj.getChildren() != null && obj.getChildren().size() > 0){
					browseTreeRecur(obj.getChildren());
				}
				
				if(obj.getChkBx().isChecked()){
					selectedContent.add(obj.getId());
				}
			}
		}
	}
	
	public void browseTreeRecur(Collection children){
		Iterator i = children.iterator();
		
		while(i.hasNext()){
			
			ReindexContentObject obj = (ReindexContentObject)i.next();
			
			if(obj.getChildren() != null && obj.getChildren().size() > 0){
				browseTreeRecur(obj.getChildren());
			}
			
			if(obj.getChkBx().isChecked()){
				selectedContent.add(obj.getId());
			}
		}
	}
	
	public String getDefaultTemplate(){
		return "cms/admin/reindexContentTree";
	}
	
	public CheckBox[] getChkContent() {
		return chkContent;
	}

	public void setChkContent(CheckBox[] chkContent) {
		this.chkContent = chkContent;
	}

	public ReindexContentObject getRoot() {
		return root;
	}

	public void setRoot(ReindexContentObject root) {
		this.root = root;
	}

	public String[] getContentObjectClasses() {
		return contentObjectClasses;
	}

	public void setContentObjectClasses(String[] contentObjectClasses) {
		this.contentObjectClasses = contentObjectClasses;
	}

	public Button getSubmitBtn() {
		return submitBtn;
	}

	public void setSubmitBtn(Button submitBtn) {
		this.submitBtn = submitBtn;
	}

}
