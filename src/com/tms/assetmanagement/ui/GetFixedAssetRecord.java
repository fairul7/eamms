package com.tms.assetmanagement.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.AssetSetupException;
import com.tms.assetmanagement.model.DataCategory;
import com.tms.assetmanagement.model.DataDisposal;
import com.tms.assetmanagement.model.DataFinancialSetup;
import com.tms.assetmanagement.model.DataFixedAssetReport;
import com.tms.assetmanagement.model.DataItems;
import com.tms.assetmanagement.model.DataItemsCategory;
import com.tms.assetmanagement.model.DataMonthlyDepDisposal;
import com.tms.assetmanagement.model.DataMonthlyDepReport;

import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class GetFixedAssetRecord extends Form {
	
	protected int iTotalUnits = 0;
	public void init() {
		super.init();
		setMethod("post"); 
		
		deleteExistingRecord();
		generateNewRecord();
	}
	
	public void deleteExistingRecord(){
		 
		 Application app = Application.getInstance(); 
		 AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		 
		 int iTotalRecord = mod.countFixedAssRecord();
		 if( iTotalRecord > 0) //delete all records
			 mod.deleteFixedAsset();
	}
	
	public void generateNewRecord(){	   
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		//retrieve financial month from financial setup
		DataFinancialSetup obj = null;
		Collection colFinancialDetail = mod.retrieveFinancialSetup();
		if (colFinancialDetail != null && colFinancialDetail.size() > 0){
			for (Iterator iterator = colFinancialDetail.iterator(); iterator.hasNext();) {
				obj = (DataFinancialSetup)iterator.next();
			}
		} else {
			throw new AssetSetupException();
		}
			
		//Note: Don't change the sequnce of getting FixedAsset record in this function
		Collection colDep= (Collection)retrieveMonthlyDepReport();
		if (colDep != null && colDep.size() > 0){
		  for (Iterator iterator = colDep.iterator(); iterator.hasNext();){
			  
			  DataMonthlyDepReport objMonthlyDep = (DataMonthlyDepReport)iterator.next();
			  setFixedAssetObj(objMonthlyDep , obj.getFinancialMonth());				  
		  }
		}		

	//checking for available asset
		Collection colItemid = mod.retrieveItems();
		if (colItemid != null && colItemid.size() > 0){
			for (Iterator iCount = colItemid.iterator(); iCount.hasNext();){	
				  DataItemsCategory objItem = (DataItemsCategory)iCount.next();
				 
				  String strTotalUnit = new Float(objItem.getItemQty()).toString(); 
				  int iTotalUnit = (int)Math.ceil(Double.parseDouble(strTotalUnit));
				  int iFirstRecordYear = mod.retrieveMinYear(objItem.getItemId());
				  int iLastRecordYear = mod.retrieveMaxYear(objItem.getItemId());
				  
				  //set  Opening Year and Closing year
				  int iEndingMonth = Integer.parseInt(obj.getFinancialMonth()) + 1;
				  int iStartingMonth = iEndingMonth + 1;
				  int iDayOfMonth = 0;
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					
				  Date dateFirstRecordYear = null; 
				  Date dateLastRecordYear = null;
										
					try {
						if (iStartingMonth > 12){//Financial Year ends on the month of December 							
						//	iStartingMonth = 1;
							dateFirstRecordYear = sdf.parse(new Integer(iFirstRecordYear).toString()+"/1/1");
							iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(iLastRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
							dateLastRecordYear = sdf.parse(new Integer(iLastRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" + new Integer(iDayOfMonth).toString());
							}		
						else{
							dateFirstRecordYear = sdf.parse(new Integer(iFirstRecordYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1");
							iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(iLastRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
						    dateLastRecordYear = sdf.parse(new Integer(iLastRecordYear + 1).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" + new Integer(iDayOfMonth).toString());
						}
					}
					 catch (ParseException e) {
							Log.getLog(getClass()).error("Error Converting String to Date! " + e);	
					}
					 
				  int iDisposalUnit =  mod.iGetTotalDisposalItemOnYear(objItem.getItemId(), objItem.getCategoryId(), dateFirstRecordYear, dateLastRecordYear);
				  int iAvailableUnit =  iTotalUnit - iDisposalUnit;
				  int iNBV = mod.retrieveMinNBV(objItem.getItemId(), objItem.getCategoryId());
				  
				  if (iNBV == iAvailableUnit && iAvailableUnit > 0){ // if nbv value same with number of available unit-> asset still exist in the record
					  Collection colAsset = mod.retrieveFixedAssetRecord(objItem.getCategoryId(), objItem.getItemId(), iLastRecordYear);
					  if(colAsset != null && colAsset.size() > 0){
					  for (Iterator iNext = colAsset.iterator(); iNext.hasNext();){
						  
							DataFixedAssetReport objAsset = (DataFixedAssetReport)iNext.next();
							setFixedAssetObject(objAsset, iAvailableUnit, obj.getFinancialMonth());				  
						  }
					 }
				  }
			}
		}
		
		//Checking for zero depreciation rate
		Collection colCategoryDep = mod.getZeroDepRate();
		if(colCategoryDep != null && colCategoryDep.size() > 0){
			for (Iterator iNext = colCategoryDep.iterator(); iNext.hasNext();){
				  
				DataCategory objCategory = (DataCategory)iNext.next();
				Collection colItem = mod.retrieveItemsByCategoryid((String)objCategory.getCategoryId());
				
				if(colItem != null && colItem.size() > 0){
					for (Iterator iCount = colItem.iterator(); iCount.hasNext();){
						
						DataItems objItems = (DataItems)iCount.next();
						setFixedAssetObjInZeroDep(objItems,  obj.getFinancialMonth());
					}
				}			
			  }			
		}
		
		// get disposal item on the same month of purchased Date
		Collection coltemDetails = mod.getDisposalOnSameMonth();
		if(coltemDetails != null && coltemDetails.size() > 0){
			for (Iterator iNext = coltemDetails.iterator(); iNext.hasNext();){
				DataItems objItem = (DataItems)iNext.next();
				setAssetObjForSameDisposalMonth(objItem);
			}
		}		
   }
   

	public void setAssetObjForSameDisposalMonth(DataItems objItems){
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		DataFixedAssetReport objFixedAsset = new DataFixedAssetReport();
		
		UuidGenerator uuid = UuidGenerator.getInstance();
		objFixedAsset.setFixedAssetId(uuid.getUuid());
		objFixedAsset.setItemId((String)objItems.getItemId());
		objFixedAsset.setCategoryId((String)objItems.getCategoryId());
		objFixedAsset.setMonthlyDepId(""); // no monthly Depreciation(id) is generated
		objFixedAsset.setAssetDate((Date)objItems.getDatePurchased());
		objFixedAsset.setAssetUnit(0);
		
		//set record of year
		String strPurchasedYear = new SimpleDateFormat("yyyy").format(objItems.getDatePurchased());
		int iPurchasedYear = Integer.parseInt(strPurchasedYear);
		objFixedAsset.setAssetYear(iPurchasedYear);
		
		//set Cost
		objFixedAsset.setCostBalBf(0);
		objFixedAsset.setCostAdditon(objItems.getItemCost());
		objFixedAsset.setCostDisposal(objItems.getItemCost());
		objFixedAsset.setCostBalCf(0);		
	
		//set Accumalate Depreciation
		objFixedAsset.setAccumDepnBalBf(0);
		objFixedAsset.setAccumDepnDCharge(0);
		objFixedAsset.setAccumDepnDisposal(0);
		objFixedAsset.setAccumDepnBalCf(0);
		objFixedAsset.setNbv(0);
		
		//Save record
		mod.insertFixedAsset(objFixedAsset);	
	}
	
	public void setFixedAssetObjInZeroDep(DataItems objItems, String StrEndingMonth){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);		
		
		//Note: Assume that each Item has only 1 quantity, such as Land
		
		DataFixedAssetReport objFixedAsset = new DataFixedAssetReport();
		int iDisposalYear= 0;
				
		String strPurchasedYear = new SimpleDateFormat("yyyy").format(objItems.getDatePurchased());
		int iPurchasedYear = Integer.parseInt(strPurchasedYear);
		int iCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
		int iNumbYear = iCurrentYear - iPurchasedYear + 1 ;
		float fAvailableUnit = (float)objItems.getItemQty();
		for(int i=0; i<iNumbYear; i++){
			
		UuidGenerator uuid = UuidGenerator.getInstance();
		objFixedAsset.setFixedAssetId(uuid.getUuid());
		objFixedAsset.setItemId((String)objItems.getItemId());
		objFixedAsset.setCategoryId((String)objItems.getCategoryId());
		objFixedAsset.setMonthlyDepId(""); // no monthly Depreciation(id) is generated
		objFixedAsset.setAssetDate((Date)objItems.getDatePurchased());		
		objFixedAsset.setEndingMonth(StrEndingMonth);
		
		//set record of year
		objFixedAsset.setAssetYear(iPurchasedYear + i);	
		int iContinueRecordYear = iPurchasedYear + i;		
		
		//set  Opening Year and Closing year for next record
		int iEndingMonth = Integer.parseInt(StrEndingMonth) + 1;
		int iStartingMonth = iEndingMonth + 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		int iDayOfMonth = 0;
		Date dateNextRecordYear = null; 
		Date dateContinueRecordYear = null;
								
			try {
				if (iStartingMonth > 12){//Financial Year ends on the month of December 							
					//iStartingMonth = 1;
					dateNextRecordYear = sdf.parse(new Integer(iContinueRecordYear).toString()+"/1/1");
					iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(iContinueRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
					dateContinueRecordYear = sdf.parse(new Integer(iContinueRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" + new Integer(iDayOfMonth).toString());
					}		
				else{
					dateNextRecordYear = sdf.parse(new Integer(iContinueRecordYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1");
					iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(iContinueRecordYear + 1).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
					dateContinueRecordYear = sdf.parse(new Integer(iContinueRecordYear + 1).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" +  new Integer(iDayOfMonth).toString());
				}
			}
			 catch (ParseException e) {
					Log.getLog(getClass()).error("Error Converting String to Date! " + e);	
			}

		int iDisposalQty = mod.iGetTotalDisposalItemOnYear(objItems.getItemId(), objItems.getCategoryId(), dateNextRecordYear, dateContinueRecordYear);
		if(iDisposalQty  > 0){
			Collection colDisposalYear = mod.getDisposalYear(objItems.getItemId(), dateNextRecordYear, dateContinueRecordYear);
			
			if(colDisposalYear != null && colDisposalYear.size() > 0){
				for (Iterator iCount = colDisposalYear.iterator(); iCount.hasNext();){
					DataDisposal objDisposal = (DataDisposal)iCount.next();
					iDisposalYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(objDisposal.getDateDisposal()));
				}
			}
		}
		int iCase = 0 ;
		//first record and no disposal
		if(i == 0 && iDisposalYear == 0) 
			iCase = 1;
		//first record and has disposal on same record's year
		else if (i == 0 && iDisposalYear > 0) 
			iCase = 2; 
		//nt first recrod and no disposal
		else if (i > 0 && iDisposalYear == 0)
			iCase = 3;
		 //nt first record and has disposal on same record's year
		else if ( i > 0 && iDisposalYear > 0 )
				//&& iDisposalYear == iPurchasedYear + i)
			iCase = 4;
		//nt first record and has disposal on other record's year
		else //default icase = 1;
			iCase = 1;
		
		switch (iCase){
			case 0 :
				break;
			case 1:
				//set Cost
				objFixedAsset.setCostBalBf(0);
				objFixedAsset.setCostAdditon(objItems.getItemCost());
				objFixedAsset.setCostDisposal(0);
				objFixedAsset.setCostBalCf(objItems.getItemCost());
									
				//set NBV
				objFixedAsset.setNbv(objItems.getItemCost());					
				break;
				
			case 2:
				//set Cost
				objFixedAsset.setCostBalBf(0);
				objFixedAsset.setCostAdditon(objItems.getItemCost());
				objFixedAsset.setCostDisposal(objItems.getItemCost());
				objFixedAsset.setCostBalCf(0);				
				//set NBV
				objFixedAsset.setNbv(0);
				
				//update available unit 
				fAvailableUnit = 0 ;
				
				break;
				
			case 3:
				//set Cost
				objFixedAsset.setCostBalBf(objItems.getItemCost());
				objFixedAsset.setCostAdditon(0);
				objFixedAsset.setCostDisposal(0);
				objFixedAsset.setCostBalCf(objItems.getItemCost());
											
				//set NBV
				objFixedAsset.setNbv(objItems.getItemCost());			
				break;
				
			case 4:
				//set Cost
				objFixedAsset.setCostBalBf(objItems.getItemCost());
				objFixedAsset.setCostAdditon(0);
				objFixedAsset.setCostDisposal(objItems.getItemCost());
				objFixedAsset.setCostBalCf(0);		
								
				//set NBV
				objFixedAsset.setNbv(0);	
				
				//update available unit 
				fAvailableUnit = 0 ;
				break;				
				
			default :
					break;
		}
		
		//set asset unit
		objFixedAsset.setAssetUnit(fAvailableUnit);
		//set Accumalate Depreciation
		objFixedAsset.setAccumDepnBalBf(0);
		objFixedAsset.setAccumDepnDCharge(0);
		objFixedAsset.setAccumDepnDisposal(0);
		objFixedAsset.setAccumDepnBalCf(0);
		
		//Save record
		mod.insertFixedAsset(objFixedAsset);
		
		if(iCase == 2 || iCase == 4)
			break; // Exit 'for' loop
		}
		
		}
	
	public void setFixedAssetObject(DataFixedAssetReport objAsset, int iAvailableUnit , String StrEndingMonth){
			
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	
	
		int iLastRecordYear = (int)objAsset.getAssetYear();
		int iCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
		int iNumbYear = iCurrentYear - iLastRecordYear ;
		int iEndingMonth = Integer.parseInt(StrEndingMonth) + 1;
		int iStartingMonth = iEndingMonth + 1;
		int iNewAvailableUnit = iAvailableUnit;
		if (iStartingMonth == 13 )
			iNumbYear --; //avoid to generate over year's record
		
		for(int i=0; i<iNumbYear; i++){

		DataFixedAssetReport objFixedAsset = new DataFixedAssetReport();
		
		UuidGenerator uuid = UuidGenerator.getInstance();
		objFixedAsset.setFixedAssetId(uuid.getUuid());
		objFixedAsset.setItemId((String)objAsset.getItemId());
		objFixedAsset.setCategoryId((String)objAsset.getCategoryId());
		objFixedAsset.setMonthlyDepId("");
		objFixedAsset.setAssetDate((Date)objAsset.getAssetDate());	
		objFixedAsset.setAssetUnit((float)iAvailableUnit);	
		objFixedAsset.setEndingMonth(StrEndingMonth);
		
		//set Opening Year and Closing year for next record
		int iNextRecordYear = iLastRecordYear + 1 + i ;
		int iContinueRecordYear = iLastRecordYear + i + 1;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		int iDayOfMonth = 0;
		Date dateNextRecordYear = null; 
		Date dateContinueRecordYear = null;
								
			try {
				if (iStartingMonth > 12){//Financial Year ends on the month of December 							
					//iStartingMonth = 1;
					dateNextRecordYear = sdf.parse(new Integer(iNextRecordYear).toString()+"/1/1");
					iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(iNextRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
					dateContinueRecordYear = sdf.parse(new Integer(iNextRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" + new Integer(iDayOfMonth).toString());
					}		
				else{
					iContinueRecordYear ++;
					dateNextRecordYear = sdf.parse(new Integer(iNextRecordYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1");
					iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(iContinueRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
					dateContinueRecordYear = sdf.parse(new Integer(iContinueRecordYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" + new Integer(iDayOfMonth).toString());				
				}
			}
			 catch (ParseException e) {
					Log.getLog(getClass()).error("Error Converting String to Date! " + e);	
			}
		
				
		//set record of year
		objFixedAsset.setAssetYear(iLastRecordYear + i + 1);	
		int iDisposalYear = 0 ;
		int iCase=0;
		int iDisposalQty = mod.iGetTotalDisposalItemOnYear(objAsset.getItemId(),objAsset.getCategoryId(), dateNextRecordYear, dateContinueRecordYear);

		iNewAvailableUnit -= iDisposalQty;

		
		if(iDisposalQty == 0 && iNewAvailableUnit > 0) // no disposal
			iCase = 1;
		else if(iDisposalQty > 0 && iNewAvailableUnit  == 0) //has dispose all units
			iCase = 2;
		else if (iDisposalQty > 0 && iNewAvailableUnit > 0)//	has disposal and has unit(s)
			iCase = 3;	
		else //default iCase = 1;
			iCase = 1;
		
		switch(iCase){
			case 0: break;
			case 1:				
			
				objFixedAsset.setCostBalBf(objAsset.getCostBalCf());
				objFixedAsset.setCostAdditon(0);
				objFixedAsset.setCostDisposal(0);
				objFixedAsset.setCostBalCf(objAsset.getCostBalCf());			
				objFixedAsset.setAccumDepnBalBf(objAsset.getAccumDepnBalCf());
				objFixedAsset.setAccumDepnDCharge(0);
				objFixedAsset.setAccumDepnDisposal(0);
				objFixedAsset.setAccumDepnBalCf(objAsset.getAccumDepnBalCf());
				objFixedAsset.setNbv(objAsset.getNbv());
			
				//Save record
				mod.insertFixedAsset(objFixedAsset);	
				break;
				
			case 2: 				
	
				objFixedAsset.setCostBalBf(objAsset.getCostBalBf());
				objFixedAsset.setCostAdditon(0);
				objFixedAsset.setCostDisposal(objAsset.getCostBalBf());
				objFixedAsset.setCostBalCf(0);				
				objFixedAsset.setAccumDepnBalBf(objAsset.getAccumDepnBalCf());
				objFixedAsset.setAccumDepnDCharge(0);
				objFixedAsset.setAccumDepnDisposal(objAsset.getAccumDepnBalCf());
				objFixedAsset.setAccumDepnBalCf(0);
				objFixedAsset.setNbv(0);
				
				//Update the available unit		
				objFixedAsset.setAssetUnit(0);
				
				//Save record
				mod.insertFixedAsset(objFixedAsset);	
				break;
			
			case 3:		//has disposal and has unit(s) and on the correct record's year
	
				objFixedAsset.setCostBalBf(objAsset.getCostBalCf());
				objFixedAsset.setCostAdditon(0);
				double dCostDisposal = objAsset.getCostBalCf()/iAvailableUnit * iDisposalQty;
				objFixedAsset.setCostDisposal(dCostDisposal);
				double dCostBalCf = objAsset.getCostBalCf() - dCostDisposal;
				objFixedAsset.setCostBalCf(dCostBalCf);
									
				double dAccDisposalCost , dDepnBalCf;

				objFixedAsset.setAccumDepnBalBf(objAsset.getAccumDepnBalCf());
				objFixedAsset.setAccumDepnDCharge(0);
				dAccDisposalCost = (objAsset.getAccumDepnBalCf())/iAvailableUnit *  iDisposalQty;
				objFixedAsset.setAccumDepnDisposal(dAccDisposalCost);
				dDepnBalCf = objAsset.getAccumDepnBalCf()- dAccDisposalCost;
				objFixedAsset.setAccumDepnBalCf(dDepnBalCf);		
				objFixedAsset.setNbv(dCostBalCf - dDepnBalCf);
			
				iAvailableUnit -= iDisposalQty;
				
				//Update the available unit		
				objFixedAsset.setAssetUnit((float)iAvailableUnit);
				
				//Save record
				mod.insertFixedAsset(objFixedAsset);	
				
				//Update object			
				int iRecordYear = iNextRecordYear;
				Collection col = mod.retrieveFixedAssetRecord(objAsset.getCategoryId(),objAsset.getItemId(),iRecordYear);
				if (col != null && col.size() > 0){
					for (Iterator iCount = col.iterator(); iCount.hasNext();){						
						DataFixedAssetReport newFixedAssetObject = (DataFixedAssetReport)iCount.next();
						objAsset = newFixedAssetObject;//Update object	
					}				
				}
							
				break;
		}		
		if(iCase == 2)
			break; // Exit 'for' loop
		
		}
	}
	
	public Collection retrieveMonthlyDepReport(){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
				 
			Collection colTempData = mod.retrieveMonthDep();
			if (colTempData != null && colTempData.size() > 0)
				return colTempData;
		return null;
	}
	
	public void setFixedAssetObj(DataMonthlyDepReport objMonthlyDep, String StrEndingMonth){
		  
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		DataFixedAssetReport objFixedAsset = new DataFixedAssetReport();
		UuidGenerator uuid = UuidGenerator.getInstance();
		objFixedAsset.setFixedAssetId(uuid.getUuid());
		objFixedAsset.setItemId((String)objMonthlyDep.getItemId());
		objFixedAsset.setCategoryId((String)objMonthlyDep.getCategoryId());
		objFixedAsset.setMonthlyDepId((String)objMonthlyDep.getMonthlyDepId());
		objFixedAsset.setAssetDate((Date)objMonthlyDep.getAssetDate());
		objFixedAsset.setAssetUnit((float)objMonthlyDep.getAssetUnit());
		objFixedAsset.setAssetYear((int)objMonthlyDep.getAssetYear());
		objFixedAsset.setEndingMonth(objMonthlyDep.getEndingMonth());
		
		//set Cost
		double[] dCost = costColumn(objMonthlyDep,StrEndingMonth );
		objFixedAsset.setCostBalBf(dCost[0]);
		objFixedAsset.setCostAdditon(dCost[1]);
		objFixedAsset.setCostDisposal(dCost[2]);
		objFixedAsset.setCostBalCf(dCost[3]);
		
		//set Accumalate Depreciation
		double[] dAccumDepn = accumDepnColumn(objMonthlyDep, objFixedAsset, StrEndingMonth);
		objFixedAsset.setAccumDepnBalBf(dAccumDepn[0]);
		objFixedAsset.setAccumDepnDCharge(dAccumDepn[1]);
		objFixedAsset.setAccumDepnDisposal(dAccumDepn[2]);
		objFixedAsset.setAccumDepnBalCf(dAccumDepn[3]);
						
		//set NBV
		objFixedAsset.setNbv(dCost[3] - dAccumDepn[3]);
	
		//Save record
		mod.insertFixedAsset(objFixedAsset);		
	}
	
	
	/* Notes:
	  dSetCost[0] = BalB/f
	  dSetCost[1] = Cost Addition
	  dSetCost[2] = Cost Disposal
	  dSetCost[3] = Cost BalC/f
	  */
	public double[] costColumn(DataMonthlyDepReport objMonthlyDep, String strEndingMonth){
				  
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		double[] dSetCost = new double[4];
		Map mapAsset;
		
		//initialize array		
		for (int i = 0  ; i < dSetCost.length; i++ ){
			dSetCost[i] = 0;
		}	
		
		String strPurchasedMonth = new SimpleDateFormat("MM").format((Date)objMonthlyDep.getAssetDate());
		String strPurchasedYear = new SimpleDateFormat("yyyy").format((Date)objMonthlyDep.getAssetDate());
		int iPurchasedMonth = Integer.parseInt(strPurchasedMonth);
		int iPurchasedYear = Integer.parseInt(strPurchasedYear);
		int iEndingMonth = Integer.parseInt(strEndingMonth)+ 1;
		int iStartingMonth = iEndingMonth + 1;
	    int iRecordYear = (int)objMonthlyDep.getAssetYear();
	    if (iPurchasedMonth < iStartingMonth && iStartingMonth < 13){
			 if (iRecordYear < iPurchasedYear)
				 iPurchasedYear = iRecordYear;
			 else if (iRecordYear == iPurchasedYear)
				 iPurchasedYear ++;					    
	    }

	    int iFirstRecordYear = iRecordYear - iPurchasedYear + 1 ; // 1=First Record
	   
	    //get Cost Addition
	    if (iFirstRecordYear == 1){
	    	Map tempMap =  mod.retrieveItems(objMonthlyDep.getItemId(), objMonthlyDep.getCategoryId());
	    	if (tempMap != null && tempMap.get("itemCost") != null){
	    		dSetCost[1] = Double.parseDouble(tempMap.get("itemCost").toString());
	    		iTotalUnits = (int)Math.ceil(Double.parseDouble(tempMap.get("itemQty").toString()));	    		
	       	}
	    }
	    else { //Bal bring forward to next year record
	    	 mapAsset = mod.retrieveFixedAsset(objMonthlyDep.getItemId().toString(), (int)objMonthlyDep.getAssetYear()-1);
	    	 if(mapAsset != null)
	    		 dSetCost[0] = Double.parseDouble(mapAsset.get("costBalCf").toString());
	    }
	    
	    // get Cost Disposal 
	   double dDisposalCost = getDisposalCost(objMonthlyDep);
	   if(dDisposalCost > 0)
		   dSetCost[2] = dDisposalCost;
	    
	   //get Cost Balc/f
	   dSetCost[3] = dSetCost[0]+ dSetCost[1]- dSetCost[2];
	
		return dSetCost;
	}
	
	public double getDisposalCost (DataMonthlyDepReport objMonthlyDep){		
		  
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		 double dUnitPrice = 0, dtotalDisposalCost = 0;
		 String strQty;
		 int iDisposalQty = 0;
		 
		Collection colMonDisposal = getDisposalItemDetail(objMonthlyDep);
		if(colMonDisposal != null && colMonDisposal.size() > 0){

			//get unit Price
			Map mapItem = mod.retrieveItems(objMonthlyDep.getItemId().toString());
			
			if(mapItem != null)
				dUnitPrice = Double.parseDouble(mapItem.get("itemUnitPrice").toString());
			
			//get sum of disposalQty at same year	
			for(Iterator iterator = colMonDisposal.iterator(); iterator.hasNext();){
				DataMonthlyDepDisposal objMonDep = (DataMonthlyDepDisposal)iterator.next();;
					strQty = new Float(objMonDep.getDisposalQty()).toString();
					iDisposalQty += (int)Math.ceil(Double.parseDouble(strQty));								
			}
			
			dtotalDisposalCost = iDisposalQty * dUnitPrice;
		}//else -> no disposal item found
		
		return dtotalDisposalCost;		
	}
	
	/* Notes:
	  dSetAccumDepn[0] = Accum Depn BalB/f
	  dSetAccumDepn[1] = Accum Depn D.charge
	  dSetAccumDepn[2] = Accum Depn Disposal
	  dSetAccumDepn[3] = Accum Depn BalC/f
	  */
	public double[] accumDepnColumn (DataMonthlyDepReport objMonthlyDep , DataFixedAssetReport objFixesAsset ,String strEndingMonth){	
		  
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		double[] dSetAccumDepn = new double[4];
		
		//initialize array		
		for (int i = 0 ; i < dSetAccumDepn.length; i++ )
			dSetAccumDepn[i] = 0;

		String strPurchasedMonth = new SimpleDateFormat("MM").format((Date)objMonthlyDep.getAssetDate());
		String strPurchasedYear = new SimpleDateFormat("yyyy").format((Date)objMonthlyDep.getAssetDate());
		int iPurchasedMonth = Integer.parseInt(strPurchasedMonth);
		int iPurchasedYear = Integer.parseInt(strPurchasedYear);
		int iEndingMonth = Integer.parseInt(strEndingMonth)+ 1;
		int iStartingMonth = iEndingMonth + 1;
	    int iRecordYear = (int)objMonthlyDep.getAssetYear();
	    if (iPurchasedMonth < iStartingMonth && iStartingMonth < 13){
			 if (iRecordYear < iPurchasedYear)
				 iPurchasedYear = iRecordYear;
			 else if (iRecordYear == iPurchasedYear)
				 iPurchasedYear ++;					    
	    }
	    int iFirstRecordYear = iRecordYear - iPurchasedYear + 1 ; // 1=First Record
	    Map mapAsset;
	
	    if (iFirstRecordYear != 1){	 
	     //Bal bring forward to next year record
	    	 mapAsset = mod.retrieveFixedAsset(objMonthlyDep.getItemId().toString(), (int)objMonthlyDep.getAssetYear()-1);
	    	 if(mapAsset != null)
	    		 dSetAccumDepn[0] = Double.parseDouble(mapAsset.get("accumDepnBalCf").toString());
	    }
		//D.Charge
		 dSetAccumDepn[1]= (double)objMonthlyDep.getTotalDepreciation();
		 
		//disposal
		 dSetAccumDepn[2] = getAccumDepnDisposalCost(objMonthlyDep, dSetAccumDepn[0], iPurchasedYear, objFixesAsset , strEndingMonth);
	
		 //get Accum Depn Bal c/f
		 dSetAccumDepn[3] = dSetAccumDepn[0] + dSetAccumDepn[1] - dSetAccumDepn[2];
		 
		 return dSetAccumDepn;
	}
	
	public Collection getDisposalItemDetail(DataMonthlyDepReport objMonthlyDep){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		Collection col = mod.retrieveMonthDepDisposal(objMonthlyDep.getMonthlyDepId().toString(), objMonthlyDep.getAssetYear());
		Collection colNewResult = new ArrayList();
		String strDisposalUnit;
		int iTotalDisposalQty = 0, iDisposalUnit = 0, iPreviousMonth = 0, iCurrentMonth = 0, iPreviousYear = 0, iCurrentYear = 0;
		int i = 0 ;
		
		if (col != null && col.size() > 0){
			//Sum up the disposal Qty on the same month & year
			for (Iterator iCount = col.iterator(); iCount.hasNext();){ 
				DataMonthlyDepDisposal objDisposal = (DataMonthlyDepDisposal)iCount.next();
				
				//Update the month and year
				iCurrentMonth = objDisposal.getDisposalMonth();
				iCurrentYear = objDisposal.getDisposalYear();
				
				if (i == 0){	
					
					iTotalDisposalQty = mod.iGetTotalDisposal(objDisposal.getMonthlyDepId(), objDisposal.getDisposalMonth(), objDisposal.getDisposalYear());
				
					strDisposalUnit = new Float(objDisposal.getDisposalQty()).toString();
					iDisposalUnit = (int)Math.ceil(Double.parseDouble(strDisposalUnit));
					if (iTotalDisposalQty > iDisposalUnit){
						DataMonthlyDepDisposal objMonthDisposal = new DataMonthlyDepDisposal();
						objMonthDisposal = objDisposal;
						objMonthDisposal.setDisposalQty(Float.parseFloat(new Integer(iTotalDisposalQty).toString()));
						colNewResult.add(objMonthDisposal);
						
					}else if(iTotalDisposalQty == iDisposalUnit){
						colNewResult.add(objDisposal);
					}
												
				}
				else if (iPreviousYear == iCurrentYear && iPreviousMonth != iCurrentMonth ){
					
					iTotalDisposalQty = mod.iGetTotalDisposal(objDisposal.getMonthlyDepId(), objDisposal.getDisposalMonth(), objDisposal.getDisposalYear());
					
					strDisposalUnit = new Float(objDisposal.getDisposalQty()).toString();
					iDisposalUnit = (int)Math.ceil(Double.parseDouble(strDisposalUnit));
					
					if (iTotalDisposalQty > iDisposalUnit){
						DataMonthlyDepDisposal objMonthDisposal = new DataMonthlyDepDisposal();
						objMonthDisposal = objDisposal;
						objMonthDisposal.setDisposalQty(Float.parseFloat(new Integer(iTotalDisposalQty).toString()));
					
						colNewResult.add(objMonthDisposal);
						
					}else if(iTotalDisposalQty == iDisposalUnit){
						colNewResult.add(objDisposal);
					}
				}
				
				iPreviousMonth = iCurrentMonth;
				iPreviousYear = iCurrentYear;
				i++;
				
			}
		}			
						
		if(colNewResult.size() >  0)
			return colNewResult;
		return null;
	}
	
	public double getAccumDepnDisposalCost(DataMonthlyDepReport objMonthlyDep , double dAccumDepnBalBf, int iPurchasedYear, 
			DataFixedAssetReport objFixesAsset, String strEndingMonth){	
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		double dAccumDisposal = 0 ;
		double dtotalDep;			
		String strDisposalUnit;
		int iTotalDisposalUnit = 0, iAvailableUnit = 0, iDisposalUnit = 0, iStart = 1, iDisposalMonth;
		DataMonthlyDepDisposal obj = null;
		
		double [] dMonthlyDepreciation = new double [13];
		//get monhtlyDepreciation
		dMonthlyDepreciation[1] = objMonthlyDep.getJan();
		dMonthlyDepreciation[2] = objMonthlyDep.getFeb();
		dMonthlyDepreciation[3] = objMonthlyDep.getMar();
		dMonthlyDepreciation[4] = objMonthlyDep.getApr();
		dMonthlyDepreciation[5] = objMonthlyDep.getMay();
		dMonthlyDepreciation[6] = objMonthlyDep.getJun();
		dMonthlyDepreciation[7] = objMonthlyDep.getJul();
		dMonthlyDepreciation[8] = objMonthlyDep.getAug();
		dMonthlyDepreciation[9] = objMonthlyDep.getSept();
		dMonthlyDepreciation[10] = objMonthlyDep.getOct();
		dMonthlyDepreciation[11] = objMonthlyDep.getNov();
		dMonthlyDepreciation[12] = objMonthlyDep.getDece();		
		
		int iEndingMonth = Integer.parseInt(strEndingMonth) + 1;
		int iStartingMonth = iEndingMonth + 1;		
		if (iStartingMonth > 12)
			iStartingMonth = 1;
		
		//Check for disposal item		 
		Collection colAccDisposal = getDisposalItemDetail(objMonthlyDep);
		if(colAccDisposal != null && colAccDisposal.size() > 0){
			
			  for (Iterator iterator = colAccDisposal.iterator(); iterator.hasNext();){
				  obj = (DataMonthlyDepDisposal)iterator.next();				  	
				  iDisposalMonth = obj.getDisposalMonth()+ 1;
				  dtotalDep = 0;
				  
				  if (iStartingMonth == 1){ // financial Year falls on December -> Start adding from Jan -> Dec					  
					  for (int i = iStart; i < iDisposalMonth ; i++){
						  dtotalDep += dMonthlyDepreciation[i];					                                         
					  }
				  }else if(iDisposalMonth >  iStartingMonth){// if disposal month=oct & starting Month=Sept -> adding from Oct - Dec
					  for (int i = iStartingMonth; i < iDisposalMonth; i++){
						  dtotalDep += dMonthlyDepreciation[i];	
					  }
				  	}else if (iDisposalMonth == iStartingMonth){
				  	  for (int i = 1; i < 13; i++){
						  dtotalDep += dMonthlyDepreciation[i];	
					  }
				  		}	else {// if disposal month=May & starting Month=Sept -> adding from Sept -> Dec -> May
						  //get sum from current year record
						  for (int i = iStartingMonth; i < 13; i++){
							  dtotalDep += dMonthlyDepreciation[i];					                                         
						  }
						  //get sum from next year record
						  for (int i = 1; i < iDisposalMonth ; i++){
							  dtotalDep += dMonthlyDepreciation[i];					                                         
						  }
				  }
				 int iFirstRecordYear = mod.retrieveMinYear(objMonthlyDep.getItemId());
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				  int iDayOfMonth = 0 ;
				  if (iFirstRecordYear == 0){
					  iFirstRecordYear = iPurchasedYear;
				  }
				  Date dateFirstRecordYear = null; 
				  Date dateDisposalMonth = null;
			/*				
				try {				
						 	dateFirstRecordYear = sdf.parse(new Integer(iFirstRecordYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1");
						 	iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(obj.getDisposalYear()).toString()+"/"+ new Integer(iDisposalMonth-1).toString()+"/1"));
							dateDisposalMonth = sdf.parse(new Integer(obj.getDisposalYear()).toString()+"/"+ new Integer(iDisposalMonth-1).toString()+"/"+ new Integer(iDayOfMonth).toString());
						}
					catch (ParseException e) {
							Log.getLog(getClass()).error("Error Converting String to Date! " + e);	
					}*/
						
					try {				
						 	dateFirstRecordYear = sdf.parse(new Integer(iFirstRecordYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1");
						 	iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(obj.getDisposalYear()).toString()+"/"+ new Integer(iDisposalMonth-1).toString()+"/1"));
							dateDisposalMonth = sdf.parse(new Integer(obj.getDisposalYear()).toString()+"/"+ new Integer(iDisposalMonth-1).toString()+"/"+ new Integer(iDayOfMonth).toString());
						}
					catch (ParseException e) {
							Log.getLog(getClass()).error("Error Converting String to Date! " + e);	
					}
							 						  
				  strDisposalUnit = new Float(obj.getDisposalQty()).toString();
				  iDisposalUnit = (int)Math.ceil(Double.parseDouble(strDisposalUnit));
				  iAvailableUnit = iTotalUnits - mod.iGetTotalDisposalItemOnYear(objMonthlyDep.getItemId(), objMonthlyDep.getCategoryId(), dateFirstRecordYear, dateDisposalMonth);				 
				  
				  if (iAvailableUnit == 0) {
					  dAccumDisposal = (double)objMonthlyDep.getTotalDepreciation();
					  dAccumDisposal += dAccumDepnBalBf;					
					  return dAccumDisposal;
				  }
				  //total unit on year = available unit on year + disposal unit on year  
				  else if (iAvailableUnit > 0){
					  dtotalDep = (dtotalDep / (iAvailableUnit + iDisposalUnit)) * iDisposalUnit;	
					  dAccumDisposal += dtotalDep;	
				 }
				  
				  iTotalDisposalUnit += iDisposalUnit;	
				  
				  //rearrange the monthly depreciation charges	
					if (iStartingMonth == 1){ // financial Year falls on December -> Start recalculate  from Jan -> Dec
							  
						 for (int a = iStart; a <iDisposalMonth; a++){
							if (iDisposalMonth < 13)
								dMonthlyDepreciation[a] =  dMonthlyDepreciation[iDisposalMonth]; 
						 }
					}else if(iDisposalMonth > iStartingMonth){// if disposal month=oct & starting Month=Sept ->recalculate from Oct - Dec
						for (int a = iStartingMonth; a <iDisposalMonth; a++)
							if(iDisposalMonth > 12)
								dMonthlyDepreciation[a] =  dMonthlyDepreciation[12];
							else
								dMonthlyDepreciation[a] =  dMonthlyDepreciation[iDisposalMonth];			
						}
		
					else {// if disposal month=May & starting Month=Sept ->recalculate  from Sept -> Dec -> May
						for (int a = iStartingMonth; a <iDisposalMonth; a++)
							 dMonthlyDepreciation[a] = dMonthlyDepreciation[iDisposalMonth];
						//next year record
						for (int a = iStart; a < iDisposalMonth ; a++){
							dMonthlyDepreciation[a] =  dMonthlyDepreciation[iDisposalMonth]; 				                                         
							}
					}						
			  }
		
			  dAccumDepnBalBf = ( dAccumDepnBalBf / (iAvailableUnit + iTotalDisposalUnit)) * iTotalDisposalUnit;
			  dAccumDisposal += dAccumDepnBalBf;
		}		
		 
		return dAccumDisposal;
	}
	    
   public void onRequest(Event event) {
		super.onRequest(event);
   }
   public Forward onValidate(Event event) {
	   super.onValidate(event);
	   return null;
   }	

}
