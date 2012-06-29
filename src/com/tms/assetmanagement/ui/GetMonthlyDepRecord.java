package com.tms.assetmanagement.ui;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.AssetSetupException;
import com.tms.assetmanagement.model.DataDisposal;
import com.tms.assetmanagement.model.DataFinancialSetup;
import com.tms.assetmanagement.model.DataItemsCategory;
import com.tms.assetmanagement.model.DataMonthlyDepDisposal;
import com.tms.assetmanagement.model.DataMonthlyDepReport;

public class GetMonthlyDepRecord extends Form{
	
	private int iTotalMonthDep = 0;
	
	public void init() {
		super.init();
		setMethod("post"); 
		
		deleteExistingRecord();
		generateNewRecord();
	}
	public void deleteExistingRecord(){
		 
		 Application app = Application.getInstance(); 
		 AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		 
		 int iTotalRecord = mod.countMonthlyDepRecord();
		 if( iTotalRecord > 0) //delete all records
			 mod.deleteMonthlyDep();
	}
	
	public void generateNewRecord(){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		 
		float fNumberOfDepMonth = 0;
		int iNumberOfDepMonth = 0;
		DataFinancialSetup obj = null;
		
		//retrieve ending month from financial setup
		Collection colFinancialDetail = mod.retrieveFinancialSetup();
		if (colFinancialDetail != null && colFinancialDetail.size() > 0){
			for (Iterator iterator = colFinancialDetail.iterator(); iterator.hasNext();) {
				obj = (DataFinancialSetup)iterator.next();
			}
		} else{
			throw new AssetSetupException();
		}
					
		// retrieve all assets
		Collection colItems = (Collection)retrieveItemDetails();
		if (colItems != null && colItems.size() > 0 ){		
		  for (Iterator iterator = colItems.iterator(); iterator.hasNext();){
			  
			  DataItemsCategory objItemCategoryTbl = (DataItemsCategory)iterator.next();
			
			  //get current year and purchased year, purchased Month
			  Date datePurchased = (Date)objItemCategoryTbl.getDatePurchased();
			  String strPurchasedYear = new SimpleDateFormat("yyyy").format(datePurchased);
			  
			  //Note: Depreciation rate is 0, no monthly depreciation is needed to be genearated
			  if (objItemCategoryTbl.getDepreciation() > 0){
				  //get the depreciation rate in month(s)
				  fNumberOfDepMonth = (100/objItemCategoryTbl.getDepreciation())* 12; 			
				  BigDecimal bd = new BigDecimal(fNumberOfDepMonth);
				  iNumberOfDepMonth = bd.setScale(2,BigDecimal.ROUND_UP).intValue();
				  
				  int iPurchasedYear  = Integer.parseInt(strPurchasedYear);
				  int icurrentYear = Calendar.getInstance().get(Calendar.YEAR);
				  int iNumbYear = (icurrentYear - iPurchasedYear) + 1 ; //Avoid getting Zero year
		 
			 for (int i = 0 ; i < iNumbYear ; i++){
				  
				  if (i == 0){ //first record
					  iTotalMonthDep = 0;   //start new record
					  setMonthlyDepObj(objItemCategoryTbl, iPurchasedYear+i ,iNumberOfDepMonth, obj.getFinancialMonth());
				  }
				  else if (iTotalMonthDep < iNumberOfDepMonth ){
					  setMonthlyDepObj(objItemCategoryTbl, iPurchasedYear+i , iNumberOfDepMonth, obj.getFinancialMonth());
				  }
				  else  if (iTotalMonthDep >= iNumberOfDepMonth )
					  break;
			  }			  
		  }
		 }
		}//No items found
	}	

    
	public Collection retrieveItemDetails(){
		
		 Application app = Application.getInstance(); 
		 AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		 
		 Collection colTempData = mod.retrieveItems();
		 if (colTempData != null && colTempData.size() > 0 )
			 return colTempData;
		return null;
	}

	  public void setMonthlyDepObj(DataItemsCategory objItemCategoryTbl , int iRecordYear, int iTotalDepYearInMonth , String strEndingMonth){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
	
		DataMonthlyDepReport objMonthlyDep = new DataMonthlyDepReport();
			
		UuidGenerator uuid = UuidGenerator.getInstance();
		objMonthlyDep.setMonthlyDepId(uuid.getUuid());	
		objMonthlyDep.setCategoryId((String)objItemCategoryTbl.getCategoryId());
		objMonthlyDep.setItemId((String)objItemCategoryTbl.getItemId());
		
		//get opening year and closing year 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		int iEndingMonth = Integer.parseInt(strEndingMonth) + 1;
		int iStartingMonth = iEndingMonth + 1;
		int iOpeningYear = iRecordYear;
		int iClosingYear = iOpeningYear;
		int iDayOfMonth = 0;
		Date dateOpeningYear = null;
		Date dateClosingYear = null;		

		try {
			if (iStartingMonth > 12){//Financial Year ends on the month of December -> eg.Jan 2000 - Dec 2000
				iStartingMonth = 1;				
				dateOpeningYear =  sdf.parse(new Integer(iOpeningYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1" );
				iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
				dateClosingYear = sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" + new Integer(iDayOfMonth).toString());
				}
			else {// Else ending Month is fall on any month except Dec, -> eg: 5/2000 -> 4/2001 (12 months)
				iClosingYear = iOpeningYear + 1;
				dateOpeningYear =  sdf.parse(new Integer(iOpeningYear).toString()+"/"+ new Integer(iStartingMonth).toString()+ "/1");
				iDayOfMonth = mod.getDaysInMonth(sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+ "/1"));
				dateClosingYear = sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+ "/" + new Integer(iDayOfMonth).toString());	
			}
		}		
		 catch (ParseException e) {
			Log.getLog(getClass()).error("Error Converting String to Date! " + e);	
		}
		
		//determine record of year
		String strPurchasedMonth = new SimpleDateFormat("MM").format(objItemCategoryTbl.getDatePurchased());
		String strPurchasedYear = new SimpleDateFormat("yyyy").format(objItemCategoryTbl.getDatePurchased());
		int iFirstPurchasedYear = Integer.parseInt(strPurchasedYear); 	
		int iPurchasedMonth = Integer.parseInt(strPurchasedMonth);
		if (iPurchasedMonth < iStartingMonth){			
			try {
				//Update Opening Year and Closing Year and RecordYear			
					iClosingYear = iOpeningYear;
					iOpeningYear -= 1;	
					iRecordYear -= 1;	
					iFirstPurchasedYear -= 1;
					dateOpeningYear =  sdf.parse(new Integer(iOpeningYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1" );
					iDayOfMonth = mod.getDaysInMonth(sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
					dateClosingYear = sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" +  new Integer(iDayOfMonth).toString());
				}
			catch (ParseException e) {
					Log.getLog(getClass()).error("Error Converting String to Date! " + e);	
				}
		}

		objMonthlyDep.setAssetDate((Date)objItemCategoryTbl.getDatePurchased());
		int iDiposalUnit = 0;

		Date dateFirstRecordYear = null;
		Date dateNextRecordYear = null;
	
		try{
			iDayOfMonth =mod.getDaysInMonth(sdf.parse(new Integer(iFirstPurchasedYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1"));
			dateFirstRecordYear = sdf.parse(new Integer(iFirstPurchasedYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/"+ new Integer(iDayOfMonth).toString());
			iDayOfMonth =mod.getDaysInMonth(sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
			dateNextRecordYear = sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" + new Integer(iDayOfMonth).toString());
		}
		catch (ParseException e) {
			Log.getLog(getClass()).error("Error Converting String to Date! " + e);	
		}
		
		String strTotalUnit = new Float(objItemCategoryTbl.getItemQty()).toString(); 
		int iTotalUnit = (int)Math.ceil(Double.parseDouble(strTotalUnit));
		iDiposalUnit = mod.iGetTotalDisposalItemOnYear(objItemCategoryTbl.getItemId(), objItemCategoryTbl.getCategoryId(), dateFirstRecordYear , dateNextRecordYear);
		
		int iAvailableUnit = iTotalUnit - iDiposalUnit;
		
		objMonthlyDep.setAssetUnit((float)iAvailableUnit);
		double iAvailableCost = (objItemCategoryTbl.getItemCost()/iTotalUnit)* iAvailableUnit;
		objMonthlyDep.setTotalCost((double)iAvailableCost);	
				
		objMonthlyDep.setAssetYear(iRecordYear);
		objMonthlyDep.setEndingMonth(strEndingMonth);
		
		//check disposal for each item 
		String disposalFoundid = retrieveDisposalitem(objItemCategoryTbl.getItemId().toString(),
				objMonthlyDep, dateOpeningYear, dateClosingYear);
		
		double[] dMonthlyDep = getMonthlyDepreciation(objItemCategoryTbl, disposalFoundid, iTotalDepYearInMonth,
				iRecordYear,strEndingMonth);

		objMonthlyDep.setJan(dMonthlyDep[1]);
		objMonthlyDep.setFeb(dMonthlyDep[2]);
		objMonthlyDep.setMar(dMonthlyDep[3]);
		objMonthlyDep.setApr(dMonthlyDep[4]);
		objMonthlyDep.setMay(dMonthlyDep[5]);
		objMonthlyDep.setJun(dMonthlyDep[6]);
		objMonthlyDep.setJul(dMonthlyDep[7]);
		objMonthlyDep.setAug(dMonthlyDep[8]);
		objMonthlyDep.setSept(dMonthlyDep[9]);
		objMonthlyDep.setOct(dMonthlyDep[10]);
		objMonthlyDep.setNov(dMonthlyDep[11]);		
		objMonthlyDep.setDece(dMonthlyDep[12]);
		objMonthlyDep.setTotalDepreciation(dMonthlyDep[13]);
			
		//don't insert record when the record year(07/08) is above the current record year(06/07)
		if (iStartingMonth != 1){//if starting month falls on Jan -> generate a year record frm Jan 2000 - Dec 2000
			//  int icurrentYear = Calendar.getInstance().get(Calendar.YEAR);
			//if (!(icurrentYear - iRecordYear == 0) && dMonthlyDep[13] > 0){
				mod.insertMonthlyDep(objMonthlyDep);
			//}
		}else if (dMonthlyDep[13] > 0 ){//is disposal all and it is new reocrd-> don't insert record	
			mod.insertMonthlyDep(objMonthlyDep);
		}		
	  }
	
	public String retrieveDisposalitem(String itemid, DataMonthlyDepReport objMonthlyReport, Date dateOpeningYear, Date dateClosingYear){
				
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
			
		DataMonthlyDepDisposal objDepDisposal = new DataMonthlyDepDisposal();				
				 
		String strDisposalFound = "";

		Collection colDisposallist = mod.getDisposalYear(itemid, dateOpeningYear, dateClosingYear);
		if (colDisposallist != null && colDisposallist.size() > 0){					
			for(Iterator iterator = colDisposallist.iterator(); iterator.hasNext();){
				DataDisposal objDisposal = (DataDisposal) iterator.next();					
			
					String strDisposalYear = new SimpleDateFormat("yyyy").format(objDisposal.getDateDisposal());
					int iDisposalYear = Integer.parseInt(strDisposalYear);
						
					String strDisposalMonth = new SimpleDateFormat("MM").format(objDisposal.getDateDisposal());
					int iDisposalMonth = Integer.parseInt(strDisposalMonth);
									
					UuidGenerator uuid = UuidGenerator.getInstance();
					objDepDisposal.setId(uuid.getUuid());
					objDepDisposal.setAssetYear(objMonthlyReport.getAssetYear());
					objDepDisposal.setDisposalId(objDisposal.getDisposalId());
					objDepDisposal.setMonthlyDepId(objMonthlyReport.getMonthlyDepId().toString());
					objDepDisposal.setDisposalYear(iDisposalYear);
					objDepDisposal.setDisposalMonth(iDisposalMonth);
					objDepDisposal.setDisposalQty(objDisposal.getDisposalQty());
							
					//insert 
					mod.insertMonthlyDepDisposal(objDepDisposal);
					strDisposalFound = objMonthlyReport.getMonthlyDepId().toString();					
				}	
		}
		return strDisposalFound;
	}

	public int iConditionDep (String monthlyDepid, int iTotalDepYearInMonth, int iTotalMonthDep){
		
		int iBal = iTotalDepYearInMonth - iTotalMonthDep; 
	
		//is first year's record and no disposal item id found
		if (iTotalMonthDep == 0 && monthlyDepid.equals("") )
			return 1;
		//is first year's record and has disposal item id 
		else if(iTotalMonthDep == 0 && !monthlyDepid.equals("") && monthlyDepid != null)
			return 2;	
		//other year's record and no disposal item id found
		else if(iBal > 12 &&  monthlyDepid.equals(""))
			return 3;
		//other year's record and has disposal item id 
		else if(iBal > 12 && !monthlyDepid.equals("") && monthlyDepid != null)
			return 4;	
		// last depreciation year's record and no disposal item id
		else if(iBal <= 12 && monthlyDepid.equals(""))
			return 5;
		// last depreciation year's record and has disposal item id
		else if(iBal <= 12 && !monthlyDepid.equals("") && monthlyDepid != null)
			return 6;
			
		return 0;
	}
	
	public double[] getMonthlyDepreciation(DataItemsCategory objcategoryItem, String MonthlyDepid, int iTotalDepYearInMonth,
		int iYearRecord, String strEndingMonth){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		int[] iMonthDisposal, iDisposalQty;
		Collection colDisposal;
		int iMonthly =0, iTotalQty, iAvaiUnit, iPurchasedMonth;
		String strPurchasedMonth, strTotalQty, itemID;
		double dMonthlyDep, dUnitPrice, dDepreciation, dGetMonthlyDep, dTotalDep = 0;
		
		// double [1 ->12] is monthly depreciation, double[13] is total monthly depreciation 
		double[] dMonthlyDepArray = new double [14];
		for (int i = 0; i < dMonthlyDepArray.length; i ++) 
			dMonthlyDepArray[i]= 0;
	
		//get Ending Month of financial year
		int iEndingMonth =  Integer.parseInt(strEndingMonth) + 1 ;
		int iStartingMonth = iEndingMonth + 1;
		if (iEndingMonth > 11 )
			iStartingMonth = 13; //Start from Jan -Dec -> 12 months
		
		switch(iConditionDep(MonthlyDepid, iTotalDepYearInMonth, iTotalMonthDep)){
			case 0 : break;
			case 1 : 	//is first year's record and no disposal item id found
				
				strPurchasedMonth = new SimpleDateFormat("MM").format((Date)objcategoryItem.getDatePurchased());
				iPurchasedMonth = Integer.parseInt(strPurchasedMonth);
				dMonthlyDep =(objcategoryItem.getItemCost() * objcategoryItem.getDepreciation()/100)/12;
			
				//initialize first year's record
				if (iPurchasedMonth < iStartingMonth){ // eg:purchased Month= March & iStartingMonth =Aug, insert data till Julai 
					iTotalMonthDep = iStartingMonth - iPurchasedMonth;
					for (int i = iPurchasedMonth; i<iStartingMonth; i++ ){
						dMonthlyDepArray[i] = dMonthlyDep;					
					}
				}
				else{	//eg: purchased Month = Sept &	iStartingMonth = Aug, insert data utill December	
					iTotalMonthDep = 13 - iPurchasedMonth;
					for (int i = iPurchasedMonth; i<13; i++ ){ //eg: 9/2000-> 12/2000
						dMonthlyDepArray[i] = dMonthlyDep;					
					}					
						//next year's record eg: 1/2001 -> 8/2001
						for(int j=1; j< iStartingMonth; j++){
							dMonthlyDepArray[j] = dMonthlyDep;
							iTotalMonthDep++;
					}
				}
							
				break;
				
			case 2 : 	//is first year's record and has disposal item id 				
						
				strPurchasedMonth = new SimpleDateFormat("MM").format((Date)objcategoryItem.getDatePurchased());
				iPurchasedMonth = Integer.parseInt(strPurchasedMonth);
				dMonthlyDep =(objcategoryItem.getItemCost() * objcategoryItem.getDepreciation()/100)/12;
				iTotalMonthDep = iStartingMonth - iPurchasedMonth;
				//initialize first year's record
				if (iPurchasedMonth < iStartingMonth){ // eg: purchased Month= March & iStartingMonth =Aug, insert data till Julai 
					for (int i = iPurchasedMonth; i<iStartingMonth; i++ ){
						dMonthlyDepArray[i] = dMonthlyDep;					
					}
				}
				else{	//eg: purchased Month = Sept &	iStartingMonth = Aug, insert data till December		
					for (int i = iPurchasedMonth; i<13; i++ ){ //eg: 9/2000-> 12/2000
						dMonthlyDepArray[i] = dMonthlyDep;					
					}					
						//next year's record eg: 9/2000 -> 8/2001
						for(int j=1; j< iStartingMonth; j++){
							dMonthlyDepArray[j] = dMonthlyDep;
							iTotalMonthDep++;
					}
				}
						
				//check disposal item(s)
				colDisposal = mod.retrieveMonthDepDisposal(MonthlyDepid.toString());
				if ( colDisposal != null && colDisposal.size() > 0){
					iMonthDisposal = new int[colDisposal.size()];
					iDisposalQty = new int[colDisposal.size()];
					
					  for (Iterator iterator = colDisposal.iterator(); iterator.hasNext();){
						  DataMonthlyDepDisposal objDisposal = (DataMonthlyDepDisposal)iterator.next();
						  iMonthDisposal [iMonthly] = objDisposal.getDisposalMonth();
						  String strDisposalQty = new Float(objDisposal.getDisposalQty()).toString();
						  iDisposalQty [iMonthly] =(int)Math.ceil(Double.parseDouble(strDisposalQty));
						  iMonthly ++;
					  }					 					
						  					
				 //Check Available Unit
				  strTotalQty = new Float(objcategoryItem.getItemQty()).toString();
				  iTotalQty = (int)Math.ceil(Double.parseDouble(strTotalQty));
				  iAvaiUnit = iTotalQty;
				  
				  for (int ii=0; ii<iMonthly ; ii++ ){ 				
					iAvaiUnit -= iDisposalQty[ii];
		
					if (iAvaiUnit > 0){									
						dUnitPrice = (double)objcategoryItem.getItemUnitPrice();
						dDepreciation = Double.parseDouble(new Float(objcategoryItem.getDepreciation()).toString());
						dGetMonthlyDep = dUnitPrice*(dDepreciation/100)* iAvaiUnit/12;
						if (iPurchasedMonth < iStartingMonth){
							for (int i = iMonthDisposal[ii]+1; i < iStartingMonth ; i++)							
								 dMonthlyDepArray[i]= dGetMonthlyDep;
						}
						else{
							 if (iMonthDisposal[ii]+1 == iStartingMonth) // Avoid back to last year record
								  break;
							
							if (iMonthDisposal[ii]+1 > iStartingMonth){ // Update current year and next year record
								for (int i = iMonthDisposal[ii]+1; i < 13 ; i++)							
									 dMonthlyDepArray[i]= dGetMonthlyDep;
								//next year record
								for(int j=1; j< iStartingMonth; j++)
									dMonthlyDepArray[j] = dGetMonthlyDep;
								}
							else{ //Update only next year record
								//next year record
								for(int j=iMonthDisposal[ii]+1; j< iStartingMonth; j++)
									dMonthlyDepArray[j] = dGetMonthlyDep;	
							}
						}
								  							  
					 }else if(iAvaiUnit == 0){ // no available unit left behind
							 //Checking if item has been disposed on the same purchased Date's month ->don't generate monthly depreciation
						if(iPurchasedMonth == iMonthDisposal[0]) {
							for (int i = 0; i < 13; i++)							
								dMonthlyDepArray[i]= 0;
								break;
						}
						else { // dispose on different month
							for (int i = iMonthDisposal[ii]+1; i < 13; i++)							
									dMonthlyDepArray[i]= 0;
								  }						
						}							 				  
				  }
				}
				 break;
				  
			case 3 : //other year's record and no disposal item id found
			
				  itemID = objcategoryItem.getItemId().toString();
				  dUnitPrice = (double)objcategoryItem.getItemUnitPrice();
				  dDepreciation = Double.parseDouble(new Float(objcategoryItem.getDepreciation()).toString());
				
				  iAvaiUnit = mod.iGetAvailableUnitFrmPreviousRecord(itemID,iYearRecord - 1);
			 
				 dGetMonthlyDep = dUnitPrice*(dDepreciation/100)* iAvaiUnit/12;	
				 iTotalMonthDep += 12; 
				 
				 //initialize month depreciation into array
					for (int i = 0; i<13; i++ ){
						dMonthlyDepArray[i] = dGetMonthlyDep;
					}				
				break;
				
			case 4 :  //other year's record and has disposal item id 
				
				 itemID = objcategoryItem.getItemId().toString();
				 dUnitPrice = (double)objcategoryItem.getItemUnitPrice();
				 dDepreciation = Double.parseDouble(new Float(objcategoryItem.getDepreciation()).toString());
				 iTotalMonthDep += 12; 
			
				 iAvaiUnit = mod.iGetAvailableUnitFrmPreviousRecord(itemID,iYearRecord - 1);				 
				 dGetMonthlyDep = dUnitPrice*(dDepreciation/100)* iAvaiUnit/12;	
				 
				//initialize month depreciation into array
				for (int i = 0; i<13; i++ ){
					dMonthlyDepArray[i] = dGetMonthlyDep;
				}
				
				//check disposal item(s)			
				colDisposal = mod.retrieveMonthDepDisposal(MonthlyDepid.toString());
				if ( colDisposal != null && colDisposal.size() > 0){
					iMonthDisposal = new int[colDisposal.size()];
					iDisposalQty = new int[colDisposal.size()];
								
					  for (Iterator iterator = colDisposal.iterator(); iterator.hasNext();){
						  DataMonthlyDepDisposal objDisposal = (DataMonthlyDepDisposal)iterator.next();
						  //get month and Qty disposal item
						  iMonthDisposal [iMonthly] = objDisposal.getDisposalMonth();
						  String strDisposalQty = new Float(objDisposal.getDisposalQty()).toString();
						  iDisposalQty [iMonthly] =(int)Math.ceil(Double.parseDouble(strDisposalQty));
						  iMonthly ++;
					  }					 
						
					  //Check Available Unit on year	
				  for (int ii=0; ii<iMonthly ; ii++ ){ 
					  iAvaiUnit -= iDisposalQty[ii];
					  if (iAvaiUnit > 0){					
						  dGetMonthlyDep = dUnitPrice*(dDepreciation/100)* iAvaiUnit/12;
						  if (iMonthDisposal[ii]+1 == iStartingMonth) // Avoid back to last year record
							  break;
						
						  if (iMonthDisposal[ii]+1 > iStartingMonth){ // Update current year and next year record
									for (int i = iMonthDisposal[ii]+1; i < 13 ; i++)							
										 dMonthlyDepArray[i]= dGetMonthlyDep;
									//next year record
									for(int j=1; j< iStartingMonth; j++)
										dMonthlyDepArray[j] = dGetMonthlyDep;
						} else{ //Update only next year record
									//next year record
								for(int j=iMonthDisposal[ii]+1; j< iStartingMonth; j++)
									dMonthlyDepArray[j] = dGetMonthlyDep;	
							}	
					
					  }else if(iAvaiUnit == 0){ // no available unit left
						  for (int i = iMonthDisposal[ii]+1; i < 13; i++)							
							  dMonthlyDepArray[i]= 0;							  
					  }					 
				  }
				}
				
				break;
				
			case 5 : // last depreciation year's record and no disposal item id
			
				 itemID = objcategoryItem.getItemId().toString();
				 dUnitPrice = (double)objcategoryItem.getItemUnitPrice();
				 dDepreciation = Double.parseDouble(new Float(objcategoryItem.getDepreciation()).toString());				
			
				 iAvaiUnit = mod.iGetAvailableUnitFrmPreviousRecord(itemID,iYearRecord - 1);
				 dGetMonthlyDep = dUnitPrice*(dDepreciation/100)* iAvaiUnit/12;				
			
				  //initialize month depreciation into array
				  if (iStartingMonth > 12){ //record started on Jan - Dec
					  for (int i = 0; i< iTotalDepYearInMonth - iTotalMonthDep + 1 ; i++ ){
						  dMonthlyDepArray[i] = dGetMonthlyDep;
						  
						if (i == iTotalDepYearInMonth - iTotalMonthDep)					
							  dMonthlyDepArray[i]= dGetMonthlyDep - iAvaiUnit;
					  }					  
				  }
				  else{  //record started on eg:Jun 2005 - Mei 2006
					  int iRecordNumb = iTotalDepYearInMonth - iTotalMonthDep;
					  int a = 0;
					  if (iRecordNumb > (13 - iStartingMonth)){
						  //previous year's record
						  for (int j = iStartingMonth; j< 13; j++ ){
							  dMonthlyDepArray[j] = dGetMonthlyDep;
							  a++;						 
						  }					  
						  //next year's record 
						  for (int i = 0; i< (iRecordNumb - a) + 1; i++  ){
							  dMonthlyDepArray[i] = dGetMonthlyDep;
							  if (i == iRecordNumb - a)					
								  dMonthlyDepArray[i]= dGetMonthlyDep - iAvaiUnit;
						  }							
				  }
				  else{
					  int i = iStartingMonth;
					  //previous year's record
					  for (int j = 0; j< iRecordNumb; j++ ){
						  dMonthlyDepArray[i] = dGetMonthlyDep;
						  
						  if (j+1 == iRecordNumb)			
							  dMonthlyDepArray[i]= dGetMonthlyDep - iAvaiUnit;					
						  i++;
					  }
				  }
				 }
					
				 iTotalMonthDep += iTotalDepYearInMonth - iTotalMonthDep;
				 
				break;
				
			case 6 : 	// last depreciation year's record and has disposal item_id
		
				  itemID = objcategoryItem.getItemId().toString();
				  dUnitPrice = (double)objcategoryItem.getItemUnitPrice();
				  dDepreciation = Double.parseDouble(new Float(objcategoryItem.getDepreciation()).toString());				
			
				  iAvaiUnit = mod.iGetAvailableUnitFrmPreviousRecord(itemID,iYearRecord - 1);
				  dGetMonthlyDep = dUnitPrice*(dDepreciation/100)* iAvaiUnit/12;
				  
				  //initialize month depreciation into array
				 
				  if (iStartingMonth > 12){ //record started on Jan - Dec
					  for (int i = 0; i< iTotalDepYearInMonth - iTotalMonthDep + 1 ; i++ ){
						  dMonthlyDepArray[i] = dGetMonthlyDep;
						  
						if (i == iTotalDepYearInMonth - iTotalMonthDep)					
							dMonthlyDepArray[i]= dGetMonthlyDep - iAvaiUnit;
					  }					  				  
				  }
				  else{  //record started on eg:Jun 2005 - Mei 2006
					  int iRecordNumb = iTotalDepYearInMonth - iTotalMonthDep;				
					  int a = 0;
					  if (iRecordNumb > (13 - iStartingMonth)){
						  	//previous record
					  	for (int j = iStartingMonth; j< 13; j++ ){
						  dMonthlyDepArray[j] = dGetMonthlyDep;
						  a++;							  
					  	}	//next year's record			  
						for (int i = 0; i< (iRecordNumb - a) + 1; i++  ){
							dMonthlyDepArray[i] = dGetMonthlyDep;
							if (i == iRecordNumb - a)			
								dMonthlyDepArray[i]= dGetMonthlyDep - iAvaiUnit;	
						}					 
					  }
					  else{
						  int i = iStartingMonth;
						  //previous year's record
						  for (int j = 0; j< iRecordNumb; j++ ){
							  dMonthlyDepArray[i] = dGetMonthlyDep;
							  
							  if (j == iTotalDepYearInMonth - iTotalMonthDep)			
							  dMonthlyDepArray[i]= dGetMonthlyDep - iAvaiUnit;					
							  i++;
						  }					  
					  }
				}				  
				
				//check disposal item(s)				
				colDisposal = mod.retrieveMonthDepDisposal(MonthlyDepid.toString());
				if ( colDisposal != null && colDisposal.size() > 0){
					iMonthDisposal = new int[colDisposal.size()];
					iDisposalQty = new int[colDisposal.size()];
								
					  for (Iterator iterator = colDisposal.iterator(); iterator.hasNext();){
						  DataMonthlyDepDisposal objDisposal = (DataMonthlyDepDisposal)iterator.next();
						  //get month and Qty disposal item
						  iMonthDisposal [iMonthly] = objDisposal.getDisposalMonth();
						  String strDisposalQty = new Float(objDisposal.getDisposalQty()).toString();
						  iDisposalQty [iMonthly] =(int)Math.ceil(Double.parseDouble(strDisposalQty));
						  iMonthly ++;
					  }					 
	
					  //Check Available Unit		
				  for (int ii=0; ii<iMonthly ; ii++ ){ 
					  
					  //check if disposal month is within the monthly depreciation's Month, if not, break;
					  if (dMonthlyDepArray[iMonthDisposal[ii]+1] <= 0)					
						  break;  
						  
					  iAvaiUnit -= iDisposalQty[ii];
					  if (iAvaiUnit > 0){
						  int iStartDiposalMonth = iMonthDisposal[ii]+1;
						  if(iStartDiposalMonth > 12)
							  iStartDiposalMonth = 1;
						  dGetMonthlyDep = dUnitPrice*(dDepreciation/100)* iAvaiUnit/12;
						  
						  if (iStartDiposalMonth == iStartingMonth) // Avoid back to last year record
							  break;
						  
						  int iRecordNumb = iTotalDepYearInMonth - iTotalMonthDep;
						  if (iRecordNumb > (13 - iStartingMonth)){
							  
							  if (iStartDiposalMonth > iStartingMonth){ // Update current year and next year record
								  int a = 0;
								  for (int i = iStartDiposalMonth; i <13; i++){							
										 dMonthlyDepArray[i]= dGetMonthlyDep;
										 a++;
								  }
									//next year record
									for(int j=1; j< (iRecordNumb - a) + 1; j++){
										dMonthlyDepArray[j] = dGetMonthlyDep;
									  
										if (j+1 == iRecordNumb - a)				
											dMonthlyDepArray[j]= dGetMonthlyDep - iAvaiUnit;
									}
									
								}
								else{ //Update only next year record
									int iRecord =iRecordNumb - (13- iStartingMonth) - iStartDiposalMonth + 1;
									//next year record
									for(int j=iStartDiposalMonth; j< iStartDiposalMonth + iRecord ; j++){
										dMonthlyDepArray[j] = dGetMonthlyDep;	
										
									  if (j+1 == iStartDiposalMonth + iRecord)					
										 dMonthlyDepArray[j]= dGetMonthlyDep - iAvaiUnit;
									}
								}
						  }
						  else
						  {
							for (int i = 0; i <iRecordNumb-(iStartDiposalMonth - iStartingMonth); i++){							
								 dMonthlyDepArray[iStartDiposalMonth]= dGetMonthlyDep;
								
								  if (i+1 == iRecordNumb-(iStartDiposalMonth - iStartingMonth))			
									  dMonthlyDepArray[iStartDiposalMonth]= dGetMonthlyDep - iAvaiUnit;	
								  iStartDiposalMonth++;
							}
						  }
						 
						 						  
					  }else if(iAvaiUnit == 0 ||iMonthDisposal[ii] > iTotalDepYearInMonth - iTotalMonthDep ){ // no available unit left
						  for (int i = iMonthDisposal[ii]+1; i < 13; i++)							
							  dMonthlyDepArray[i]= 0;
					  }
				  }
				}
				 iTotalMonthDep += iTotalDepYearInMonth - iTotalMonthDep;
				 break;
				 
			 default:
				 break;
		}
		
		//Calculate Total Depreciation
		  for (int i = 0; i < 13; i++)			  
			  dTotalDep +=  dMonthlyDepArray[i+1];		  
		  dMonthlyDepArray[13] = dTotalDep;
		
		return dMonthlyDepArray;
	}

	public void onRequest(Event event) {
		super.onRequest(event);
	}
	public Forward onValidate(Event event) {
		super.onValidate(event);
		return null;
	}
	

}
