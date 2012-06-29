package com.tms.assetmanagement.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

import com.tms.crm.sales.model.CompanyType;
import com.tms.crm.sales.model.CompanyTypeDao;
import com.tms.crm.sales.model.CompanyTypeException;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class AssetModule extends DefaultModule {
	 
	 public void init()
	  {	  }
	 
	//-------------------------- Asset Category  ------------------------------------
	 public void insertCategory(DataCategory objcategories){
		 
		 AssetDao dao = (AssetDao)getDao();
		 try {
			dao.insertCategory(objcategories);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Cannot insert into ast_assetCategory table" + e);
		}
	 }

	 public void updateCategory(DataCategory objcategories)
	 {
		 AssetDao dao = (AssetDao)getDao();
		 try{
			 dao.updateCategory(objcategories);
			 
		 }catch(DaoException e) {
				Log.getLog(getClass()).error("Cannot update into ast_assetCategory table" + e);
		 }
	 }

	 public void deleteCategory(String categoryid){
		 
		 AssetDao dao = (AssetDao)getDao();
		 try{		 
			dao.deleteCategory(categoryid);
				 
		 }catch(DaoException e){
			Log.getLog(getClass()).error("Cannot delete from ast_assetCategory table" + e);
		}
	 }
	 
	 public Map retrieveCategory (String categoryid){
		 
		 AssetDao dao = (AssetDao)getDao();
		 try{		 
			return dao.retrieveCategory(categoryid);
				 
		 }catch(DaoException e){
			Log.getLog(getClass()).error("Cannot retrieve from ast_assetCategory table" + e);			
			 return null;
		}	
		 
	 }
	 
	 public Collection listingCategory(String keyword,String sort,boolean desc,int start,int rows){
		 
		 AssetDao dao = (AssetDao)getDao();
		 try {
	      		return dao.listingCategory(keyword,sort,desc,start,rows);
	      	}
	      	catch (DaoException e){
	      		Log.getLog(getClass()).error("Cannot retrieve from ast_assetCategory table" + e);
	      		return null;
	      	}
	 }
	 
	 public Collection listCategoryType (){
		 AssetDao dao = (AssetDao)getDao();
		 try {
	      		return dao.listCategoryType();
	      	}
	      	catch (DaoException e){
	      		Log.getLog(getClass()).error("Cannot retrieve from ast_assetCategory table" + e);
	      		return null;
	      	}
	 }
	 public Collection sortCategoryType(Map mapCategory){
		 if(mapCategory != null){
			String[] listCategoryId = new String[mapCategory.size()];
			int ii =0;
			for (Iterator i = mapCategory.keySet().iterator(); i.hasNext();)
			{	
				listCategoryId[ii] =(String) i.next();
				ii++;
			}	
			 AssetDao dao = (AssetDao)getDao();
			 try {
		      		return dao.sortCategory(listCategoryId);
		      	}
		      	catch (DaoException e){
		      		Log.getLog(getClass()).error("Cannot retrieve from ast_assetCategory table" + e);
		      		return null;
		      	}
		 } 		 
		return null;
		
	 }
	 public Collection getZeroDepRate(){
		 AssetDao dao = (AssetDao)getDao();
		 try {
	      		return dao.getZeroDepRate();
	      	}
	      	catch (DaoException e){
	      		Log.getLog(getClass()).error("Cannot retrieve from ast_assetCategory table" + e);
	      		return null;
	      	}
	 }
	 
	   public boolean isUniqueCategory(DataCategory objcategories) {
	      
			 AssetDao dao = (AssetDao)getDao();
	        try {
	            return dao.isUniqueCategory(objcategories);
	        } catch (DaoException e) {
	        	Log.getLog(getClass()).error("Error verifying uniqueness of category " + e);
	           return false;
	        }

	    }
	    
//----------------------- Asset Items ---------------------------------
	   
	   public void insertItems(DataItems objItems){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{
				dao.insertItems(objItems);
					 
			 }catch(DaoException e) {
					Log.getLog(getClass()).error("Cannot insert into ast_assetItems table" + e);
			}
	   }
	   public Collection retrieveItems (String[] itemid){

			 AssetDao dao = (AssetDao)getDao();
			 try{
				return dao.retrieveItems (itemid);
					 
			 }catch(DaoException e) {
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetItems table" + e);
				return null;
			}
	   }
	   
	   public void updateItems(DataItems objItems){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{
				dao.updateItems(objItems);
				dao.updateDisposalAndItem(objItems);
					 
			 }catch(DaoException e) {
					Log.getLog(getClass()).error("Cannot update into ast_assetItems table" + e);
			}		   
	   }
	   
	   public void deleteItems(String itemid){
		   
		   AssetDao dao = (AssetDao)getDao();
			 try{
				dao.deleteItems(itemid);
				dao.deleteDisposalbyItem(itemid);
					 
			 }catch(DaoException e) {
					Log.getLog(getClass()).error("Cannot delete from ast_assetItems table" + e);
			}
	   }
	   
	   public Collection retrieveItems(){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveItems();
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetItems table" + e);			
				 return null;
			}
	   }
	   
	   public Collection retrieveItems( String sort, boolean desc, int start, int rows){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveItems(sort, desc, start, rows);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetItems table" + e);			
				 return null;
			}
	   }
			
	   public Collection retrieveCategoryItems(String categoryid, String keyword, String sort, boolean desc, int start, int rows){

			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveCategoryItems(categoryid, keyword, sort, desc, start, rows);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetItems table" + e);			
				 return null;
			}  
	   }
	   
	   public Map retrieveItems(String itemid){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveItems(itemid);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetItems table" + e);			
				 return null;
			}
	   }
	
	   public Map retrieveItems(String itemid, String categoryid){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveItems(itemid, categoryid);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetItems table" + e);			
				 return null;
			}
	   }
	   
	   public Collection retrieveItemsByCategoryid (String categoryid){
		   
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveItemsByCategoryid(categoryid);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetItems table" + e);			
				 return null;
			}
	   }
	
	   public Collection listingItems (String keyword, String categoryType, Date frmDate, Date toDate, String sort, boolean desc, int start, int rows){
		   
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.listingItems(keyword, categoryType, frmDate, toDate, sort, desc, start, rows);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetItems table" + e);			
				 return null;
			}
	   }
		
	   public boolean isUniqueItems (DataItems objItems){
		   
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.isUniqueItems(objItems);
				
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Error verifying uniqueness of item " + e);			
				 return false;
			}
	   }
	
	   public int countCategoryInUsed (String tableId){
			   
			   AssetDao dao = (AssetDao)getDao();
			   try {
				return dao.countCategoryInUsed(tableId);
				
			} catch (DaoException e) {
				Log.getLog(getClass()).error("Error finding item " + e);	
				return 0;
			}
	   }
	   
/*	   public Collection listItemCategory (String categoryid){
		   
		   AssetDao dao = (AssetDao)getDao();
		   try {
			return dao.listItemAccordingCategory(categoryid);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error finding item(s) for selected category!  " + e);	
			return null;
		} 
	   }
	 */
		
//	 ----------------------- Asset Disposal Item(s) ---------------------------------
	   
	   public void insertDisposal (DataDisposal objDisposal){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{
				dao.insertDisposal(objDisposal);
					 
			 }catch(DaoException e) {
					Log.getLog(getClass()).error("Cannot insert into ast_assetDisposal table" + e);
			}
	   }
	   
	   public void updateDisposal(DataDisposal objDisposal){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{
				dao.updateDisposal(objDisposal);
					 
			 }catch(DaoException e) {
					Log.getLog(getClass()).error("Cannot update into ast_assetDisposal table" + e);
			}		   
	   }
	   
	   public void deleteDisposal(String disposalid){
		   
		   AssetDao dao = (AssetDao)getDao();
			 try{
				dao.deleteDisposal(disposalid);
					 
			 }catch(DaoException e) {
					Log.getLog(getClass()).error("Cannot delete from ast_assetDisposal table" + e);
			}
	   }
	   
	   public Map retrieveDisposal(String disposalid){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveDisposal(disposalid);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetDisposal table" + e);			
				 return null;
			}
	   }
	   
	   /*public Collection getDisposalYear(String itemid, String categoryid, Date fromYear, Date toYear){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.getDisposalYear(itemid, categoryid, fromYear, toYear);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetDisposal table" + e);			
				 return null;
			}
	   }
	   */

	   public Collection getDisposalYear(String itemid, Date dateFrom, Date dateTo) {
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.getDisposalYear(itemid, dateFrom, dateTo);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetDisposal table" + e);			
				 return null;
			}
	   }
	    	

		 public int iGetDisposalUnitByItemId(String itemId){
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.iGetDisposalUnitByItemId(itemId);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetDisposal table" + e);			
				 return 0;
			}
		 }
	   public int iGetTotalDisposalItemOnYear(String itemid, String categoryid, Date dateFrom, Date dateTo){
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.iGetTotalDisposalItemOnYear(itemid, categoryid, dateFrom, dateTo);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetDisposal table" + e);			
				 return 0;
			}
	   }
	   public Collection retrieveDisposal(String itemid, String categoryid){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveDisposal(itemid,categoryid);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetDisposal table" + e);			
				 return null;
			}
	   }	   
	  
	   public Collection getDisposalDateByYear(String categoryid, Date dateFrm, Date dateTo){
		   
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.getDisposalDateByYear(categoryid, dateFrm, dateTo);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetDisposal table" + e);			
				 return null;
			}  
	   }

	
	   public Collection listingDisposal(String keyword, String categoryType, Date frmDate, Date toDate, String sort, boolean desc, int start, int rows){
		   
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.listingDisposal(keyword, categoryType, frmDate, toDate, sort, desc, start, rows);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetDisposal table" + e);			
				 return null;
			}
	   }
	   
	   
	   //---------------------Monthly Depreciation Charges Report -----------------------------
	   
	   public int countMonthlyDepRecord(){
		   
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.countMonthlyDepRecord();
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepChargesReport table" + e);			
				 return 0;
			}
	   }
	  public double getSumOfColumn(int iYear, String categoryid, String columnField){
		  
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.getSumOfColumn(iYear, categoryid, columnField);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot get Sum values from ast_monthlyDepChargesReport table" + e);			
				 return 0;
			}  
	  }
	  
	  public Collection retrieveMonthDepReport(int iYear, String categoryid){
		 
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveMonthDepReport(iYear, categoryid);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepChargesReport table" + e);			
				 return null;
			}  
	  }
	  
	  public int countMonthDepReport (int iYear, String categoryid){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.countMonthDepReport(iYear, categoryid);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepChargesReport table" + e);			
				 return 0;
			}  
	  }
	  
	  public int getMinYearA() {
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.getMinYear("ast_monthlyDepChargesReport");
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepChargesReport table" + e);		
				return 0;
			}
	  }
	  
	  public void deleteMonthlyDep(){
		  
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.deleteMonthlyDep();
				 dao.deleteMonthlyDepDisposal();
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot delete from ast_monthlyDepChargesReport table" + e);			
			}
	  }
	  
	  public void insertMonthlyDep (DataMonthlyDepReport objMonDep){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.insertMonthlyDep(objMonDep);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot inset into ast_monthlyDepChargesReport table" + e);			
			}
	  }
	  
	  public Collection retrieveMonthDep (){

		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveMonthDep();
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepChargesReport table" + e);		
				return null;
			}
	  }
	  
	  public int iGetAvailableUnitFrmPreviousRecord(String itemid, int iRecordYear){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 				 
				return dao.iGetAvailableUnitFrmPreviousRecord(itemid, iRecordYear);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepChargesReport table" + e);		
				return 0;
			}
	  }
	  
	  public Collection getMonthlyRecordsByYearCategory(String categoryid, int iAssetYear){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 				 
				return dao.getMonthlyRecordsByYearCategory(categoryid, iAssetYear);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepChargesReport table" + e);		
				return null;
			}
	}
	  
	  // ----------- Monthly Depreciation Disposal ID table ---------------------------------
	  
	  public void insertMonthlyDepDisposal (DataMonthlyDepDisposal objMonthlyDepDis){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.insertMonthlyDepDisposal(objMonthlyDepDis);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot inset into ast_monthlyDepDisposal table" + e);			
			}
	  }
	  
	  public Collection retrieveMonthDepDisposal (String strMonthDepid){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveMonthDepDisposal(strMonthDepid);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepDisposal table" + e);		
				return null;
			}
	  }
	 public int iGetTotalDisposal(String monthid, int iMonth, int iYear){
//  public int iGetTotalDisposalItemOnYear(String monthid, int iFromYear, int iToYear){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.iGetTotalDisposal(monthid, iMonth, iYear);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepDisposal table" + e);		
				return 0;
			}
	  }
	  
	  public Collection retrieveItemByMonthDepid (String monthid, int iMonth, int iYear){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveItemByMonthDepid(monthid, iMonth, iYear);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepDisposal table" + e);		
				return null;
			}
	  }
	  public Collection retrieveMonthDepDisposal (String strMonthDepid, int iyear){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveMonthDepDisposal(strMonthDepid, iyear);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_monthlyDepDisposal table" + e);		
				return null;
			}
	  }
	  
	  //------------------------Fixed Asset Lists Report table --------------------------------
	  
	  public void insertFixedAsset (DataFixedAssetReport objFixedAsset){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.insertFixedAssetReport(objFixedAsset);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot inset into ast_fixedAssetListReport table" + e);		
			}		  
	  }	  
	  
	  public int getMinYearB() {
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.getMinYear("ast_fixedAssetListReport");
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);		
				return 0;
			}
	  }
	  public Collection getDisposalOnSameMonth(){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.getDisposalOnSameMonth();
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);			
				 return null;
			}
	  }
	  public void deleteFixedAsset(){
		  
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.deleteFixedAssetReport();				
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot delete from ast_fixedAssetListReport table" + e);			
			}
	  }
	  
	  public int countFixedAssRecord(){
		   
		   AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.countFixedAssetRecord();
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);			
				 return 0;
			}
	   }
	  public  Map retrieveFixedAsset(String itemid , int iYearOfRecord){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveFixedAsset(itemid, iYearOfRecord);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);		
				return null;
			}
	  }
	  
	  public int retrieveMinNBV(String itemid, String categoryid){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveMinNBV(itemid, categoryid);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);		
				return 0;
			}	
		  
	  }
	  
	  public int retrieveMaxYear(String itemid){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveMaxYear(itemid);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);		
				return 0;
			}
	  }
	  
	  
	  public int retrieveMinYear(String itemid){
			  
			AssetDao dao = (AssetDao)getDao();
			 try{		 
					return dao.retrieveMinYear(itemid);
			}catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);		
				return 0;
			}
	  }	
		
	  public Collection retrieveFixedAssetRecord(String categoryid, String itemid, int year){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveFixedAssetRecord(categoryid, itemid, year);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);		
				return null;
			}	
	  }
	  public  Collection retrieveFixedAssetRecord(String categoryid, int idateTo){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveFixedAssetRecord(categoryid, idateTo);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);		
				return null;
			}	
	  }
	  public int countFixedAsset(String categoryid, int yearTo){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.countFixedAsset(categoryid, yearTo);
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);		
				return 0;
			}	
	  }
	 public double getAssetSumOfColumn( int iToYear, String categoryid, String columnField){
		 
		 AssetDao dao = (AssetDao)getDao();
		 try{		 
			return dao.getAssetSumOfColumn(iToYear, categoryid, columnField);
				 
		 }catch(DaoException e){
			Log.getLog(getClass()).error("Cannot get Sum values from ast_fixedAssetListReport table" + e);			
			 return 0;
		}  
	 }
	 public Collection getFixedRecordsByYearCategory(String categoryid, int iAssetYear){
		 
		 AssetDao dao = (AssetDao)getDao();
		 try{		 
			return dao.getFixedRecordsByYearCategory(categoryid, iAssetYear);
				 
		 }catch(DaoException e){
			Log.getLog(getClass()).error("Cannot retrieve from ast_fixedAssetListReport table" + e);			
			 return null;
		}  
		 
	 }
	  //------------------------Notification table --------------------------------
	 
	  public void insertNotification(DataNotification objnotification){		  
			 
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				dao.insertNotification(objnotification);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot insert into ast_assetNotification table" + e);			
			}  		  
	  }
	  
	  public Collection listingNotification(String keywordTitle, String sort, boolean desc, int start, int rows){
		 
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.listingNotification(keywordTitle, sort, desc, start, rows);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetNotification table" + e);			
				return null;
			 }  	
	  }
	  public  Collection retrieveNotification(String id){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveNotification(id);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_assetNotification table" + e);			
				return null;
			 }  	
	  }
	 
	  public  int iGetTotalNotification(){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.iGetTotalNotification();
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot get total record from ast_assetNotification table" + e);			
				return 0;
			 }  	
	  }
	  //------------------------Addressee table --------------------------------
	  
	  public void insertAddressee (DataAddressee objAddressee){
		  
			 AssetDao dao = (AssetDao)getDao();
			 try{		 
				dao.insertAddressee(objAddressee);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot insert into ast_assetAddressee table" + e);			
			}
	  }
	  
	  public  Collection retrieveAddressee(String id){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveAddressee(id);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retriev from ast_assetAddressee table" + e);			
				return null;
			 }  	
	  }
	  
	  public double roundUpTo2DecimalPoints(double dvalue){
		  
		  BigDecimal bd = new BigDecimal(dvalue);
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		 return  bd.doubleValue();
	  }	  
	  
//	------------------------------ ast_financialSetup ------------------------------

	  public void insertFinancialSetup (DataFinancialSetup objFinancialSetup){	
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.insertFinancialSetup(objFinancialSetup);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot insert into ast_financialSetup table" + e);			
			 }  
		}
	  
	  public void updateFinancialSetup(DataFinancialSetup objFinancialSetup){
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.updateFinancialSetup(objFinancialSetup);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot update into ast_financialSetup table" + e);			
			 }  
	  }
	    
	  public int iGetSizeFinancialSetup(){
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.iGetSizeFinancialSetup();
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_financialSetup table" + e);			
				return 0;
			 }  	
	  }
	  	  
	 public Collection retrieveFinancialSetup() {
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveFinancialSetup();
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_financialSetup table" + e);			
				return null;
			 }  	
	 }
	 
	 //----------------------- ast_saveMonthlyDepReport ------------------------------------
	 
	  public void insertSaveMonthlyDep (DataSaveMonthDepReport objSave){
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				dao.insertSaveMonthlyDep(objSave);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot insert into ast_saveMonthlyDepReport table" + e);			
			 }  		  
	  }
	  
	  public void deleteSaveMonthlyDep(String strSaveId){
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				dao.deleteSaveMonthlyDep(strSaveId);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot delete from ast_saveMonthlyDepReport table" + e);			
			 }  
	  }
	  
	  public Collection listingSaveMonthlyDep(Date frmDate, Date toDate,String sort,boolean desc,int start,int rows) {
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.listingSaveMonthlyDep(frmDate, toDate,sort, desc, start, rows);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_saveMonthlyDepReport table" + e);	
				return null;
			 }
	  }

	  public  Collection retrieveSaveMonthlyDep(String strSaveId){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.retrieveSaveMonthlyDep(strSaveId);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_saveMonthlyDepReport table" + e);	
				return null;
			 }  
	  }
	  
	  public double getMonthlySumOfColumn(int iYear, String categoryName, String columnField , String saveId ){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.getMonthlySumOfColumn(iYear, categoryName, columnField ,saveId );
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot get sum from ast_saveMonthlyDepReport table" + e);	
				return 0;
			 }
		  
	  }
	  
	  public int countSaveMonthlyDep(int iYear, String categoryName, String saveId ){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.countSaveMonthlyDep(iYear, categoryName, saveId );
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_saveMonthlyDepReport table" + e);	
				return 0;
			 }
		  
	  }
		  
		public Date getCreatedDateSaveMonthlyDep(String saveid) {
			  
			 AssetDao dao = (AssetDao)getDao();
				try{		 
					return dao.getCreatedDateSaveMonthlyDep(saveid);
					
				 }catch(DaoException e){
					Log.getLog(getClass()).error("Cannot retrieve from ast_saveMonthlyDepReport table" + e);	
					return null;
				 }	
		 }
//	----------------------------------- ast_saveFixedAssetReport ------------------------------------------
	  
	  public void insertSaveFixedAsset(DataSaveFixedAssetReport objFixedAsset){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.insertSaveFixedAsset(objFixedAsset);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot insert into ast_saveFixedAssetReport table" + e);	
			 }  
	  }
	  
	  public void deleteSaveFixedAsset(String strSaveId){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 dao.deleteSaveFixedAsset(strSaveId);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot delete from ast_saveFixedAssetReport table" + e);	
			 }
	  }
	  
	  public Collection listingSaveFixedAsset(Date frmDate, Date toDate, String sort,boolean desc,int start,int rows){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				return dao.listingSaveFixedAsset(frmDate, toDate, sort, desc, start, rows);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_saveFixedAssetReport table" + e);	
				return null;
			 }
	  }
	  
	  public Collection retrieveSaveFixedAsset(String strSaveId){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.retrieveSaveFixedAsset(strSaveId);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot get sum  from ast_saveFixedAssetReport table" + e);	
				return null;
			 }
		  
	  }
	  
	  public double getFixedSumOfColumn(int iYear, String categoryName, String columnField, String saveId ){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.getFixedSumOfColumn(iYear, categoryName, columnField, saveId);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_saveFixedAssetReport table" + e);	
				return 0;
			 }
	  }
		  
	  public int countSaveFixedAsset(int iYear, String categoryName, String saveId ){
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.countSaveFixedAsset(iYear, categoryName, saveId);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_saveFixedAssetReport table" + e);	
				return 0;
			 }		  
	  }
	  
	 public Date getCreatedDateSaveFixedAsset(String saveid) {
		  
		  AssetDao dao = (AssetDao)getDao();
			 try{		 
				 return dao.getCreatedDateSaveFixedAsset(saveid);
					 
			 }catch(DaoException e){
				Log.getLog(getClass()).error("Cannot retrieve from ast_saveFixedAssetReport table" + e);	
				return null;
			 }	
	 }
	 //----------------- Get Days in Month -----------------------
	 
	    public static int getDaysInMonth(Date date){
	    	GregorianCalendar gc = new GregorianCalendar();
	    	gc.setTime(date);
	        return gc.getActualMaximum(Calendar.DAY_OF_MONTH);
	    }

}
