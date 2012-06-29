package com.tms.assetmanagement.model;

import kacang.model.DefaultDataObject;

public class DataSaveFixedAssetReport extends DefaultDataObject {
	
		private String id;
		private String saveAssetId;
		private String currencySymbol;
		private String currencyPattern;
		private String categoryName;
		private String itemName;
		private float depreciation;
		private String financialYear;
		private float assetUnit;
		private java.util.Date assetDate;
		private int assetYear;
		private String endingMonth;
		private double costBalBf;
		private double costAdditon;
		private double costDisposal; 
		private double costBalCf;
		private double accumDepnBalBf;
		private double accumDepnDCharge; 
		private double accumDepnDisposal;
		private double accumDepnBalCf; 
		private double nbv;
		
		// ------ gettters and setters ---
		public double getAccumDepnBalBf() {
			return accumDepnBalBf;
		}
		public void setAccumDepnBalBf(double accumDepnBalBf) {
			this.accumDepnBalBf = accumDepnBalBf;
		}
		public double getAccumDepnBalCf() {
			return accumDepnBalCf;
		}
		public void setAccumDepnBalCf(double accumDepnBalCf) {
			this.accumDepnBalCf = accumDepnBalCf;
		}
		public double getAccumDepnDCharge() {
			return accumDepnDCharge;
		}
		public void setAccumDepnDCharge(double accumDepnDCharge) {
			this.accumDepnDCharge = accumDepnDCharge;
		}
		public double getAccumDepnDisposal() {
			return accumDepnDisposal;
		}
		public void setAccumDepnDisposal(double accumDepnDisposal) {
			this.accumDepnDisposal = accumDepnDisposal;
		}
		public java.util.Date getAssetDate() {
			return assetDate;
		}
		public void setAssetDate(java.util.Date assetDate) {
			this.assetDate = assetDate;
		}
		public float getAssetUnit() {
			return assetUnit;
		}
		public void setAssetUnit(float assetUnit) {
			this.assetUnit = assetUnit;
		}
		public int getAssetYear() {
			return assetYear;
		}
		public void setAssetYear(int assetYear) {
			this.assetYear = assetYear;
		}
		public String getCategoryName() {
			return categoryName;
		}
		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}
		public double getCostAdditon() {
			return costAdditon;
		}
		public void setCostAdditon(double costAdditon) {
			this.costAdditon = costAdditon;
		}
		public double getCostBalBf() {
			return costBalBf;
		}
		public void setCostBalBf(double costBalBf) {
			this.costBalBf = costBalBf;
		}
		public double getCostBalCf() {
			return costBalCf;
		}
		public void setCostBalCf(double costBalCf) {
			this.costBalCf = costBalCf;
		}
		public double getCostDisposal() {
			return costDisposal;
		}
		public void setCostDisposal(double costDisposal) {
			this.costDisposal = costDisposal;
		}
		public float getDepreciation() {
			return depreciation;
		}
		public void setDepreciation(float depreciation) {
			this.depreciation = depreciation;
		}
		public String getEndingMonth() {
			return endingMonth;
		}
		public void setEndingMonth(String endingMonth) {
			this.endingMonth = endingMonth;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		public double getNbv() {
			return nbv;
		}
		public void setNbv(double nbv) {
			this.nbv = nbv;
		}
		public String getSaveAssetId() {
			return saveAssetId;
		}
		public void setSaveAssetId(String saveAssetId) {
			this.saveAssetId = saveAssetId;
		}
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
		public String getFinancialYear() {
			return financialYear;
		}
		public void setFinancialYear(String financialYear) {
			this.financialYear = financialYear;
		}
		
		

}
