package com.tms.assetmanagement.ui;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.AssetSetupException;
import com.tms.assetmanagement.model.DataFinancialSetup;
import com.tms.assetmanagement.model.DataSaveMonthDepReport;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;

public class MonthlyDepreciationReport extends Form {

	private String year;
	private String category;

	//company name and logo path
	private String companyName;
	private String companyLogo;

	private String showPrintViewDisposal="false";
	private Button btnSave;
	private String strSaveId ="";
	private String recordYear="";


	public String getDefaultTemplate(){

		return "assetmanagement/tempMonthlyDepReport";
	}

	public void init(){

		super.init();
		setMethod("post");

		//generating monthly depreciation charges Report
		GetMonthlyDepRecord obj = new GetMonthlyDepRecord();
		try {
			obj.init();
		} catch (AssetSetupException e) {}

		GetFixedAssetRecord obj1 = new GetFixedAssetRecord();
		try {
			obj1.init();
		} catch (AssetSetupException e) {}

		//grab company name and log
		SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		try {
			companyName = setup.get("siteName");
		} catch (SetupException e) {

			//company name is not entered at the setup page
		}

		try {
			companyLogo =  "/ekms/" + setup.get("siteLogo");
		} catch (SetupException e) {

			//company name is not entered at the setup page
		}

		btnSave = new Button("btnSave", Application.getInstance().getMessage("asset.label.btnSave", "Save") );	
		addChild(btnSave);

		//reset
		strSaveId ="";
	}


	public void onRequest(Event event) {
		super.onRequest(event);	
		init();
	}

	public Forward onValidate(Event event) {
		super.onValidate(event);

		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);

		String strCategoryId;
		StringTokenizer tokenizer = new StringTokenizer(category, ",");

		if (btnSave.getAbsoluteName().equals(findButtonClicked(event))){	

			//retrieve financial month from financial setup
			DataFinancialSetup obj = null;
			Collection colFinancialDetail = mod.retrieveFinancialSetup();
			if (colFinancialDetail != null && colFinancialDetail.size() > 0){
				for (Iterator iterator = colFinancialDetail.iterator(); iterator.hasNext();) {
					obj = (DataFinancialSetup)iterator.next();
				}
			}			

			if (strSaveId.equals("")){ //Avoid inserting data for twice
				UuidGenerator uuid = UuidGenerator.getInstance();

				strSaveId = (String)uuid.getUuid();
				while(tokenizer.hasMoreTokens())
				{
					strCategoryId = tokenizer.nextToken();  
					Collection colMonthlyReport = mod.getMonthlyRecordsByYearCategory(strCategoryId, Integer.parseInt(year));
					if (colMonthlyReport != null && colMonthlyReport.size() > 0){
						for (Iterator iterator = colMonthlyReport.iterator(); iterator.hasNext();) {
							DataSaveMonthDepReport objOldReport = (DataSaveMonthDepReport)iterator.next();
							insertSaveMonthDepReport(objOldReport, strSaveId ,obj);						
						}							
					}					
				}	
			}
			else
				return new Forward("Duplicated");
		}
		return null;
	}

	public void insertSaveMonthDepReport(DataSaveMonthDepReport objOldReport , String strSaveId , DataFinancialSetup objSetup){

		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);

		DataSaveMonthDepReport objSaveReport = new DataSaveMonthDepReport();
		UuidGenerator uuid = UuidGenerator.getInstance();

		objSaveReport.setId((String)uuid.getUuid());
		objSaveReport.setSaveMonthlyId(strSaveId);
		objSaveReport.setCurrencyPattern(objSetup.getCurrencyPattern());
		objSaveReport.setCurrencySymbol(objSetup.getCurrencySymbol());
		objSaveReport.setCategoryName(objOldReport.getCategoryName());
		objSaveReport.setDepreciation(objOldReport.getDepreciation());	
		objSaveReport.setFinancialYear(recordYear);
		objSaveReport.setItemName(objOldReport.getItemName());
		objSaveReport.setAssetDate(objOldReport.getAssetDate());
		objSaveReport.setAssetUnit(objOldReport.getAssetUnit());
		objSaveReport.setTotalCost(objOldReport.getTotalCost());
		objSaveReport.setAssetYear(objOldReport.getAssetYear());		
		objSaveReport.setEndingMonth(objOldReport.getEndingMonth());
		objSaveReport.setJan(objOldReport.getJan());
		objSaveReport.setFeb(objOldReport.getFeb());
		objSaveReport.setMar(objOldReport.getMar());
		objSaveReport.setApr(objOldReport.getApr());
		objSaveReport.setMay(objOldReport.getMay());
		objSaveReport.setJun(objOldReport.getJun());
		objSaveReport.setJul(objOldReport.getJul());
		objSaveReport.setAug(objOldReport.getAug());
		objSaveReport.setSept(objOldReport.getSept());
		objSaveReport.setOct(objOldReport.getOct());
		objSaveReport.setNov(objOldReport.getNov());		
		objSaveReport.setDece(objOldReport.getDece());
		objSaveReport.setTotalDepreciation(objOldReport.getTotalDepreciation());

		//insert
		mod.insertSaveMonthlyDep(objSaveReport);		
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRecordYear() {
		return recordYear;
	}

	public void setRecordYear(String recordYear) {
		this.recordYear = recordYear;
	}

	//grab company name and logo
	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public String getShowPrintViewDisposal() {
		return showPrintViewDisposal;
	}

	public void setShowPrintViewDisposal(String showPrintViewDisposal) {
		this.showPrintViewDisposal = showPrintViewDisposal;
	}

}
