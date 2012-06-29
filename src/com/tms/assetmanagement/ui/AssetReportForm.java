package com.tms.assetmanagement.ui;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.CalendarEvent;

public class AssetReportForm extends Form {

	protected SelectBox sbxRepotType;
	protected Button btnSubmit;
	protected SelectBox sbxYear;
	protected ComboSelectBox CombosbxCategory;

	private String strReportType = "";

	public void init() {
		super.init();
		setMethod("post");
		setColumns(2);
	}
	
	public void onRequest(Event event) {
		super.onRequest(event);
		removeChildren();
		init();
		//refresh record
		MonthlyDepreciationReport mo = new MonthlyDepreciationReport();
		mo.init();
		FixedAssetListsReport fi = new FixedAssetListsReport();
		fi.init();
		if (getStrReportType() != null && !"".equals(getStrReportType())) {
			if (getStrReportType().equals("depreciationReport"))
				initDepreciationReportForm();
			else if (getStrReportType().equals("fixedAssetReport"))
				initFixedAssetReportForm();
		}
	}

	public void initDepreciationReportForm() {

		removeChildren();
		Label lblyear = new Label("lblyear", "<div align=\"right\"><b>"
				+ Application.getInstance().getMessage(
						"asset.report.reportYear", "Financial Year") + "</b></div>");
		addChild(lblyear);

		sbxYear = new SelectBox("sbxYear");
		sbxYear.setOptionMap((Map) getYear(1));
		sbxYear.setSelectedOption(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
		addChild(sbxYear);

		Label lblcategory = new Label("lblcategory", "<div align=\"right\"><b>"
				+ Application.getInstance().getMessage("asset.report.category",
						"Category") + "</b></div>");
		addChild(lblcategory);
		
		CombosbxCategory = new ComboSelectBox("CombosbxCategory");		
		addChild(CombosbxCategory);
		CombosbxCategory.init();
		CombosbxCategory.setLeftValues((Map) getCategoryType());
		
		//String test = CombosbxCategory.getValue().toString();		
		//Log.getLog(getClass()).info("Combo value:"+test);
		
		btnSubmit = new Button("btnSubmitDepreciation", "Submit");
		addChild(new Label("lbl2"));
		addChild(btnSubmit);

	}

	public void initFixedAssetReportForm() {

		removeChildren();

		Label lblyear = new Label("lblyear", "<div align=\"right\"><b>"
				+ Application.getInstance().getMessage(
						"asset.report.reportYear", "Financial Year") + "</b></div>");
		addChild(lblyear);

		sbxYear = new SelectBox("sbxYear");
		sbxYear.setOptionMap((Map) getYear(2));
		sbxYear.setSelectedOption(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
		addChild(sbxYear);

		Label lblcategory = new Label("lblcategory", "<div align=\"right\"><b>"
				+ Application.getInstance().getMessage("asset.report.category",
						"Category") + "</b></div>");
		addChild(lblcategory);
		
		CombosbxCategory = new ComboSelectBox("CombosbxCategory");	
		addChild(CombosbxCategory);
		CombosbxCategory.init();
		CombosbxCategory.setLeftValues((Map) getCategoryType());

		btnSubmit = new Button("btnSubmitAsset", "Submit");
		addChild(new Label("lbl3"));
		addChild(btnSubmit);

	}

	public Map getCategoryType() {

		Application app = Application.getInstance();
		AssetModule mod = (AssetModule) app.getModule(AssetModule.class);

		Map mapCategory = new SequencedHashMap();		

		Collection listCategory = mod.listCategoryType();

		if (listCategory != null && listCategory.size() > 0) {
			for (Iterator iterator = listCategory.iterator(); iterator
					.hasNext();) {
				Map tempMap = (Map) iterator.next();
				if (tempMap.get("categoryId") != null)
					mapCategory.put(tempMap.get("categoryId"), tempMap.get("categoryName"));
			}
			return mapCategory;
		}
		return null;
	}

	public Map getYear(int a){
		Application app = Application.getInstance();
		AssetModule mod = (AssetModule) app.getModule(AssetModule.class);
		
		int iMinYear = 0;
		int icurrentYear = Calendar.getInstance().get(Calendar.YEAR);
		int iyears = 0;
		Map mapYear = new SequencedHashMap();	
		
		switch (a){
			case 1 :
				iMinYear = mod.getMinYearA();
				break;
			case 2 :
				iMinYear = mod.getMinYearB();
				break;		
 		}
		 String key="", value="";
		if (iMinYear > 0){
		iyears = icurrentYear - iMinYear + 1;
		 for (int i = 0; i<iyears; i++){
			  key = new Integer(iMinYear + i).toString();
		 		value = key;
			 mapYear.put(key , value);}
		}
		else{
			key = new Integer(icurrentYear).toString();;
			value = key;
			mapYear.put(key , value);
		}
		return mapYear;
	}
	
	public Forward onValidate(Event event) {
		super.onValidate(event);
		
		Application app = Application.getInstance();
		AssetModule mod = (AssetModule) app.getModule(AssetModule.class);
		
		String btnClicked = findButtonClicked(event);
		if (btnClicked.endsWith("btnSubmitDepreciation")) {
	
			Map map = CombosbxCategory.getRightValues();
			String strCategoryId = "";
			Collection sortedCategory  = mod.sortCategoryType(map);
			
			if (!(sortedCategory == null || sortedCategory.isEmpty())){ 
				
				for (Iterator i = sortedCategory.iterator(); i.hasNext();)
				{
					Map mapCategory = (Map) i.next();
					if(strCategoryId.length() != 0)
						strCategoryId += ",";
					strCategoryId += mapCategory.get("categoryId");
				}
			}
			else
				return new Forward("UnselectCategory");
				
			String strYear = (String) sbxYear.getSelectedOptions().keySet().iterator().next();
			String strForwardURL = "assetMonthlyDepView.jsp?year=" + strYear + "&category=" + strCategoryId;
			strReportType = "";
			return new Forward("", strForwardURL, true);

		} else if (btnClicked.endsWith("btnSubmitAsset")) {

			Map map = CombosbxCategory.getRightValues();
			Collection sortedCategory = mod.sortCategoryType(map);
			String strCategoryId = "";
			if (!sortedCategory.isEmpty()){				
				for (Iterator i = sortedCategory.iterator(); i.hasNext();)
				{
					Map mapCategory = (Map) i.next();
					if(strCategoryId.length() != 0)
						strCategoryId += ",";
					strCategoryId += mapCategory.get("categoryId");
				}
			}else
				return new Forward("UnselectCategory");	
	
			String strYear = (String) sbxYear.getSelectedOptions().keySet().iterator().next();
			String strForwardURL = "assetFixedReport.jsp?year="+ strYear + "&category=" + strCategoryId;
			strReportType = "";
			return new Forward("", strForwardURL, true);
		}

		return null;
	}

	public String getStrReportType() {
		return strReportType;
	}

	public void setStrReportType(String strReportType) {
		this.strReportType = strReportType;
	}

}
