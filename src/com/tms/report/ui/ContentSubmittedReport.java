package com.tms.report.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.report.model.ReportModule;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.reindex.model.ReindexContentObject;
import com.tms.cms.section.Section;

public class ContentSubmittedReport extends Form{
	
	protected DatePopupField startDate;
	protected DatePopupField endDate;
	protected SelectBox selGroup;
	
	private ReindexContentObject root;
	private String[] contentObjectClasses = new String[] {
	        Section.class.getName()
	    };
	private int contentCount = 0;
	private ArrayList selectedContent;
	
	private CheckBox[] chkContent;
	private Button submitBtn;
	
	private Collection results;
	
	public ContentSubmittedReport(){
		super();
	}
	
	public void init(){
		initForm();
		setMethod("POST");
		results = new ArrayList();
	}
	
	public void onRequest(Event evt){
//		initForm();
//		results = new ArrayList();
		selectedContent = new ArrayList();
	}
	
	public void initForm(){
		contentCount = 0;
		selectedContent = new ArrayList();
		
		Application app = Application.getInstance();
        ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
        
        startDate = new DatePopupField("startDate");
        startDate.setOptional(true);
        addChild(startDate);
        endDate = new DatePopupField("endDate");
        endDate.setOptional(true);
        addChild(endDate);
        selGroup = new SelectBox("selDivision");
        selGroup.setOptionMap(getGroupOptionsMap());
        addChild(selGroup);
        submitBtn = new Button("submit", "Submit");
		addChild(submitBtn);
        
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
	
	public Map  getGroupOptionsMap(){
		
		Application app = Application.getInstance();
        SecurityService security = (SecurityService)app.getService(SecurityService.class);
		
		Map groupMap = new SequencedHashMap();
        try {
            DaoQuery q = new DaoQuery();
            q.addProperty(new OperatorEquals("active", "1", DaoOperator.OPERATOR_AND));
            Collection list = security.getGroups(q, 0, -1, "groupName", false);
            groupMap.put("", "--- "+app.getMessage("security.label.filterGroup"+" ---", "Filter Group"));
            for (Iterator i = list.iterator(); i.hasNext();) {
                Group group = (Group) i.next();
                groupMap.put(group.getId(), group.getName());
            }
        }
        catch (Exception e) {
            Log.getLog(ContentSubmittedReport.class).error("Error loading groups: " + e.toString(), e);
        }
        return groupMap;
		
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
		
//		r.setApprovalDate(obj.getApprovalDate());
//		r.setApprovalUser(obj.getApprovalUser());
//		r.setApprovalUserId(obj.getApprovalUserId());
//		r.setAuthor(obj.getAuthor());
//		r.setVersion(obj.getVersion());
//		r.setTemplate(obj.getTemplate());
//		r.setSummary(obj.getSummary());
//		r.setCheckedOut(obj.isCheckedOut());
//		r.setCheckOutDate(obj.getCheckOutDate());
//		r.setCheckOutUser(obj.getCheckOutUser());
//		r.setCheckOutUserId(obj.getCheckOutUserId());
//		r.setComments(obj.getComments());
		
		String checkboxName = new String("cbContent");
		checkboxName = checkboxName + contentCount;
		CheckBox tempChkBx = new CheckBox("cbContent" + contentCount);
		tempChkBx.setOnClick("return checkChildren('" + obj.getId() + "', this)");
		tempChkBx.setValue(obj.getId().toString());
		r.setChkBx(tempChkBx);
		addChild(r.getChkBx());
		
		return r;
	}
	
	public Forward onValidate(Event evt) {
		
		String button = findButtonClicked(evt);
		
		if (button.endsWith("submit")) {
			browseTree();
			
			ReportModule rm = (ReportModule)Application.getInstance().getModule(ReportModule.class);
			
			results = new ArrayList();
			
			if(selectedContent != null && selectedContent.size() > 0){
				String strGroup = new String();
				
				strGroup = (String)selGroup.getSelectedOptions().keySet().iterator().next();
				Collection tempResult = new ArrayList();
				
				for(int i=0; i<selectedContent.size(); i++){
					ReindexContentObject parent = (ReindexContentObject)selectedContent.get(i);
					
					try{
						tempResult = rm.getContentSubmittedReport(parent.getName(), strGroup, parent.getId(), 
								startDate.getDate()!=null?startDate.getDate():null, endDate.getDate()!=null?endDate.getDate():null);
						
						results.addAll(tempResult);
					}
					catch(Exception e){
						Log.getLog(getClass()).error("error in contentSubmittedReport(onValidate): " + e.getMessage());
					}
					
				}
				
				selectedContent = new ArrayList();
				//initForm();
				return new Forward("success");
			}else{
				return new Forward("nonselect");
			}
		}
		
		return new Forward("error");
	}
	
	public void browseTree(){
		if(root.getChkBx().isChecked()){
			selectedContent.add(root);
		}
		
		Iterator i = root.getChildren().iterator();
			
		while(i.hasNext()){
			ReindexContentObject obj = (ReindexContentObject)i.next();
			
			if(obj.getChildren() != null && obj.getChildren().size() > 0){
				browseTreeRecur(obj.getChildren());
			}
			
			if(obj.getChkBx().isChecked()){
				selectedContent.add(obj);
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
				selectedContent.add(obj);
			}
		}
	}
	
	public String getDefaultTemplate(){
		return "cms/admin/contentSubmittedReport";
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

	public DatePopupField getEndDate() {
		return endDate;
	}

	public void setEndDate(DatePopupField endDate) {
		this.endDate = endDate;
	}

	public SelectBox getSelGroup() {
		return selGroup;
	}

	public void setSelGroup(SelectBox selGroup) {
		this.selGroup = selGroup;
	}

	public DatePopupField getStartDate() {
		return startDate;
	}

	public void setStartDate(DatePopupField startDate) {
		this.startDate = startDate;
	}

	public Collection getResults() {
		return results;
	}

	public void setResults(Collection results) {
		this.results = results;
	}
	

}
