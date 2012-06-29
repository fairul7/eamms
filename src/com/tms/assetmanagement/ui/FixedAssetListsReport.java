package com.tms.assetmanagement.ui;

import java.util.Collection;
import java.util.Iterator;
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
import com.tms.assetmanagement.model.DataSaveFixedAssetReport;
import com.tms.assetmanagement.model.DataSaveMonthDepReport;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;

public class FixedAssetListsReport extends Form {

	protected String year;
	protected String category;

	//company name and logo path
	protected String companyName;
	protected String companyLogo;

	protected String recordYear="";
	protected Button btnSave;
	protected String strSaveId ="";
	protected String showPrintViewDisposal="false";

	public String getDefaultTemplate(){

		return "assetmanagement/tempFixedAssetReport";
	}

	public void init(){			
		super.init();		

		//grab company name and log
		SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		try {
			companyName = setup.get("siteName");
		} catch (SetupException e) {

			//company name is not entered at the setup page
		}

		try {			
			companyLogo = "/ekms/" + setup.get("siteLogo");

		} catch (SetupException e) {

			//company name is not entered at the setup page
		}

		//generating monthly depreciation charges Report
		GetMonthlyDepRecord obj = new GetMonthlyDepRecord();

		try {
			obj.init();
		} catch (AssetSetupException e) {
			// do something

		}
		GetFixedAssetRecord obj1 = new GetFixedAssetRecord();
		try {
			obj1.init();
		} catch (AssetSetupException e) {
			// do something
		}

		btnSave = new Button("btnSave", Application.getInstance().getMessage("asset.label.btnSave", "Save") );	
		addChild(btnSave);

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
					Collection colMonthlyReport = mod.getFixedRecordsByYearCategory(strCategoryId, Integer.parseInt(year));
					if (colMonthlyReport != null && colMonthlyReport.size() > 0){
						for (Iterator iterator = colMonthlyReport.iterator(); iterator.hasNext();) {
							DataSaveFixedAssetReport objOldReport = (DataSaveFixedAssetReport)iterator.next();
							insertSaveFixedAssetReport(objOldReport, strSaveId , obj);
						}				
					}
				}	
			}
			else
				return new Forward("Duplicated");
		}
		return null;

	}	

	public void insertSaveFixedAssetReport(DataSaveFixedAssetReport objOldReport , String strSaveId  , DataFinancialSetup objSetup){

		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);

		DataSaveFixedAssetReport objSaveReport = new DataSaveFixedAssetReport();
		UuidGenerator uuid = UuidGenerator.getInstance();

		objSaveReport.setId((String)uuid.getUuid());
		objSaveReport.setCurrencyPattern(objSetup.getCurrencyPattern());
		objSaveReport.setCurrencySymbol(objSetup.getCurrencySymbol());
		objSaveReport.setSaveAssetId(strSaveId);
		objSaveReport.setCategoryName(objOldReport.getCategoryName());
		objSaveReport.setDepreciation(objOldReport.getDepreciation());
		objSaveReport.setFinancialYear(recordYear);
		objSaveReport.setItemName(objOldReport.getItemName());
		objSaveReport.setAssetDate(objOldReport.getAssetDate());
		objSaveReport.setAssetUnit(objOldReport.getAssetUnit());	
		objSaveReport.setAssetYear(objOldReport.getAssetYear());		
		if(objOldReport.getEndingMonth()!=null)
			objSaveReport.setEndingMonth(objOldReport.getEndingMonth());
		else
			objSaveReport.setEndingMonth(objSetup.getFinancialMonth());
		objSaveReport.setCostBalBf(objOldReport.getCostBalBf());
		objSaveReport.setCostAdditon(objOldReport.getCostAdditon());
		objSaveReport.setCostDisposal(objOldReport.getCostDisposal());
		objSaveReport.setCostBalCf(objOldReport.getCostBalCf());
		objSaveReport.setAccumDepnBalBf(objOldReport.getAccumDepnBalBf());
		objSaveReport.setAccumDepnDCharge(objOldReport.getAccumDepnDCharge());
		objSaveReport.setAccumDepnDisposal(objOldReport.getAccumDepnDisposal());
		objSaveReport.setAccumDepnBalCf(objOldReport.getAccumDepnBalCf());
		objSaveReport.setNbv(objOldReport.getNbv());

		//insert
		mod.insertSaveFixedAsset(objSaveReport);
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

	public String getRecordYear() {
		return recordYear;
	}

	public void setRecordYear(String recordYear) {
		this.recordYear = recordYear;
	}




}
