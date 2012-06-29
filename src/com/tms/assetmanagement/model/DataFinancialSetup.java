package com.tms.assetmanagement.model;

import kacang.model.DefaultDataObject;

public class DataFinancialSetup extends DefaultDataObject {

	private String financialId;
	private String financialMonth;
	private String currencySymbol;
	private String currencyPattern;
	
	//----- getters and setters ----------
	public String getCurrencyPattern() {
		return currencyPattern;
	}
	public void setCurrencyPattern(String currencyPattern) {
		this.currencyPattern = currencyPattern;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getFinancialId() {
		return financialId;
	}
	public void setFinancialId(String financialId) {
		this.financialId = financialId;
	}
	public String getFinancialMonth() {
		return financialMonth;
	}
	public void setFinancialMonth(String financialMonth) {
		this.financialMonth = financialMonth;
	}


}
