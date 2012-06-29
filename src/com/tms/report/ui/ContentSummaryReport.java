package com.tms.report.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.report.model.ReportModule;

public class ContentSummaryReport extends Form{
	
	protected String[][] contentArr = {
			{"Section", "com.tms.cms.section.Section_"}, 
			{"Article", "com.tms.cms.article.Article_"},
			{"Document", "com.tms.cms.document.Document_"},
			{"Page", "com.tms.cms.page.Page_"}
	};
	
	protected DateField startDate;
	protected DateField endDate;
	private Button submitBtn;
	
	protected SelectBox selGroup;
	
	private Collection results;
	
	public ContentSummaryReport(){
		super();
	}
	
	public void init(){
		initForm();
		results = new ArrayList();
	}
	
	public void onRequest(Event evt){
//		initForm();
//		results = new ArrayList();
	}
	
	public void initForm(){
		startDate = new DatePopupField("startDate");
        addChild(startDate);
        endDate = new DatePopupField("endDate");
        addChild(endDate);
        
        selGroup = new SelectBox("selDivision");
        selGroup.setOptionMap(getGroupOptionsMap());
        addChild(selGroup);
        
        submitBtn = new Button("submit", "Submit");
		addChild(submitBtn);
	}
	
	public Forward onValidate(Event evt) {
		
		String button = findButtonClicked(evt);
		
		if (button.endsWith("submit")) {
			
			ReportModule rm = (ReportModule)Application.getInstance().getModule(ReportModule.class);
			results = new ArrayList();
			
			String strGroup = new String();
			strGroup = (String)selGroup.getSelectedOptions().keySet().iterator().next();
			
			results = rm.getContentSummaryReport(contentArr, startDate.getDate(), endDate.getDate(), strGroup);
			
//			results.add(rm.getContentSummaryReport("Section", "cms_content_section", startDate.getDate(), endDate.getDate()));
//			results.add(rm.getContentSummaryReport("Article", "cms_content_article", startDate.getDate(), endDate.getDate()));
//			results.add(rm.getContentSummaryReport("Document", "cms_content_document", startDate.getDate(), endDate.getDate()));
//			results.add(rm.getContentSummaryReport("Page", "cms_content_page", startDate.getDate(), endDate.getDate()));
			
		}
		
		return new Forward("success");
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
	
	public String getDefaultTemplate(){
		return "cms/admin/contentSummaryReport";
	}

	public SelectBox getSelGroup() {
		return selGroup;
	}

	public void setSelGroup(SelectBox selGroup) {
		this.selGroup = selGroup;
	}

	public DateField getEndDate() {
		return endDate;
	}

	public void setEndDate(DateField endDate) {
		this.endDate = endDate;
	}

	public Collection getResults() {
		return results;
	}

	public void setResults(Collection results) {
		this.results = results;
	}

	public DateField getStartDate() {
		return startDate;
	}

	public void setStartDate(DateField startDate) {
		this.startDate = startDate;
	}

	public Button getSubmitBtn() {
		return submitBtn;
	}

	public void setSubmitBtn(Button submitBtn) {
		this.submitBtn = submitBtn;
	}
	
	
}
