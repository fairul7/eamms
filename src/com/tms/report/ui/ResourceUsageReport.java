package com.tms.report.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.User;
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
import com.tms.collab.resourcemanager.model.ResourceManager;

public class ResourceUsageReport extends Form{

    public static final int DEFAULT_START = 8;
    public static final int DEFAULT_END = 23;

	protected SelectBox categoryList;
	protected DateField startDate;
	protected DateField endDate;
	private Button submitBtn;
	
	protected int startTime;
	protected int endTime;
	
	private Map reportMap;
	private Collection resourceCol;
	
	public ResourceUsageReport(){
		super();
	}
	
	public void init(){
		resourceCol = new ArrayList();

		Properties properties = Application.getInstance().getProperties();
        String propertyStart = properties.getProperty("resource.schedule.starttime");
        String propertyEnd = properties.getProperty("resource.schedule.endtime");
        startTime = (propertyStart == null || "".equals(propertyStart)) ? DEFAULT_START : Integer.parseInt(propertyStart);
        endTime = (propertyEnd == null || "".equals(propertyEnd)) ? DEFAULT_END : Integer.parseInt(propertyEnd);

		initForm();
		resourceCol = new ArrayList();
	}
	
	public void onRequest(Event evt){
//		initForm();
//		resourceCol = new ArrayList();
	}
	
	public void initForm(){
		
		categoryList = new SelectBox("catList");
        categoryList.addOption("-1", Application.getInstance().getMessage("resourcemanager.label.Selectacategory", "Select a category"));
        Map cmap = null;
        try {
            cmap = ((ResourceManager) Application.getInstance().getModule(ResourceManager.class)).getResourceCategories();
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        for (Iterator i = cmap.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            categoryList.addOption(key, (String) cmap.get(key));
        }
        addChild(categoryList);
		
		startDate = new DatePopupField("startDate");
        addChild(startDate);
        endDate = new DatePopupField("endDate");
        addChild(endDate);
        submitBtn = new Button("submit", "Submit");
		addChild(submitBtn);
	}
	
	public Forward onValidate(Event evt) {
		
		String button = findButtonClicked(evt);
		User user = Application.getInstance().getCurrentUser();
		
		if (button.endsWith("submit")) {
			
			ReportModule rm = (ReportModule)Application.getInstance().getModule(ReportModule.class);
			ResourceManager rm2 = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
			
			try{
				String selectedCategoryId = new String();
				Map selectedCat = categoryList.getSelectedOptions();

		        if (selectedCat.size() > 0 && !selectedCat.containsKey("-1")) {
		            selectedCategoryId = (String) selectedCat.keySet().iterator().next();
		        }
				
				resourceCol = new ArrayList();
				resourceCol = rm2.getResource(null, selectedCategoryId, new String[]{user.getId()}, null, true, true, true, null, false, 0, -1);
				
				reportMap = new SequencedHashMap();
				reportMap = rm.getResourceUsageReport(startDate.getDate(), endDate.getDate(), selectedCategoryId, startTime, endTime);
				
			}
			catch(DaoException e){
				
			}
			
			
			
			
		}
		
		return new Forward("success");
	}
	
	public String getDefaultTemplate(){
		return "resourcemanager/resourceUsageReport";
	}

	public DateField getEndDate() {
		return endDate;
	}

	public void setEndDate(DateField endDate) {
		this.endDate = endDate;
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

	public Map getReportMap() {
		return reportMap;
	}

	public void setReportMap(Map reportMap) {
		this.reportMap = reportMap;
	}

	public Collection getResourceCol() {
		return resourceCol;
	}

	public void setResourceCol(Collection resourceCol) {
		this.resourceCol = resourceCol;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public SelectBox getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(SelectBox categoryList) {
		this.categoryList = categoryList;
	}
	
}
