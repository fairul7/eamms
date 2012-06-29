package com.tms.assetmanagement.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import com.tms.crm.sales.model.CompanyType;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.stdui.DateField;
import kacang.util.Log;
import kacang.util.UuidGenerator;
public class AssetDao extends DataSourceDao {
	
	// Create tables
	 public void init() throws DaoException 
	  {
		 try{
			  super.update("CREATE TABLE ast_assetCategory (" 
					  		+ "categoryId VARCHAR(35) NOT NULL, "
					  		+ "categoryName varchar(100) NOT NULL, "
					  		+ "depreciation FLOAT NOT NULL, " 
					  		+ "PRIMARY KEY (categoryId))", null);
		 }catch (DaoException e) {}
		 
			 //create index
		 try{
			 super.update("CREATE INDEX categoryid_index ON ast_assetCategory(categoryId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try {
			   super.update("CREATE TABLE ast_assetItems ("
					   		 + "itemId VARCHAR(35) NOT NULL, "
					   		 + "categoryId VARCHAR(35) NOT NULL, "
					   		 + "itemName VARCHAR(100) NOT NULL, "
					   		 + "datePurchased DATE, "
					   		 + "itemQty FLOAT NOT NULL, "
					   		 + "itemUnitPrice DOUBLE NOT NULL, "
					   		 + "itemCost DOUBLE, "
					   		 + "itemDescription TEXT, "
					   		 + "dateCreated DATETIME, "
					   		 + "PRIMARY KEY (itemId))", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update("CREATE INDEX category_index ON ast_assetItems(categoryId)", null);
		 }
		 catch (DaoException e) {}

		 try{
			  super.update("CREATE INDEX itemid_index ON ast_assetItems(itemId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update( "CREATE TABLE ast_assetNotification (" +
			  				"notificationId VARCHAR(35) NOT NULL, " +
			  				"notificationTitle VARCHAR(100) NOT NULL, " +
			  				"notificationDate DATETIME DEFAULT '0000-00-00 00:00:00', " +
			  				"notificationTime DATETIME DEFAULT '0000-00-00 00:00:00', " +
			  				"notificationMsg TEXT, " +
			  				"notifyMethod CHAR(1) NOT NULL, " +
			  				"senderID VARCHAR(255) NOT NULL, " +
			  				"dateCreated DATETIME NOT NULL, " +
			  				"PRIMARY KEY (notificationId))", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update("CREATE INDEX notificationid_index ON ast_assetNotification(notificationId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update("CREATE INDEX item_index ON ast_assetNotification(itemId)", null);
		 }
		 catch (DaoException e) {}

		 try{				
			  super.update(	"CREATE TABLE ast_assetAddressee (" +
			  				"addresseeId VARCHAR(35) NOT NULL, " +
			  				"notificationId VARCHAR(35) NOT NULL, " +
			  				"recipientId VARCHAR(255) NOT NULL, " +
			  				"PRIMARY KEY (addresseeId))", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX notifiaction_index ON ast_assetAddressee(addresseeId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE TABLE ast_assetDisposal (" +
			  				"disposalId VARCHAR(35) NOT NULL, " +
			  				"itemId VARCHAR(35) NOT NULL, " +
			  				"disposalQty FLOAT, " +
			  				"disposalCost DOUBLE, " +
			  				"dateDisposal DATETIME, " +
			  				"disposalReason TEXT, " +
			  				"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
			  				"PRIMARY KEY (disposalId))", null);
		 }
		 catch (DaoException e) {}		 
		 
		 try{
			  super.update(	"CREATE INDEX disposalid_index ON ast_assetDisposal(disposalId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX item2_index ON ast_assetDisposal(itemId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE TABLE ast_fixedAssetListReport (" +
			  				"fixedAssetId VARCHAR(35) NOT NULL, " +
			  				"itemId VARCHAR(35) NOT NULL, " +
			  				"categoryId VARCHAR(35) NOT NULL, " +
			  				"monthlyDepId VARCHAR(35)NULL, " +
			  				"assetUnit FLOAT, " +
			  				"assetDate DATETIME , " +
			  				"assetYear INT, " +
			  				"endingMonth CHAR(2), " +
			  				"costBalBf DOUBLE, " +
			  				"costAdditon DOUBLE, " +
			  				"costDisposal DOUBLE, " +
			  				"costBalCf DOUBLE, " +
			  				"accumDepnBalBf DOUBLE, " +
			  				"accumDepnDCharge DOUBLE, " +
			  				"accumDepnDisposal DOUBLE, " +
			  				"accumDepnBalCf DOUBLE, " +
			  				"nbv DOUBLE, " +
			  				"PRIMARY KEY (fixedAssetId))", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX item3_index ON ast_fixedAssetListReport(itemId)", null);
		 }
		 catch (DaoException e) {}

		 try{
			  super.update(	"CREATE INDEX fixedAssetid_index ON ast_fixedAssetListReport(fixedAssetId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX disposal_index ON ast_fixedAssetListReport(disposalId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX category_index ON ast_fixedAssetListReport(categoryId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX assetDate_index ON ast_fixedAssetListReport(assetDate)", null);
		 }
		 catch (DaoException e) {}
		 
		 
		 try{
			  super.update(	"CREATE TABLE ast_monthlyDepChargesReport (" +
			  				"monthlyDepId VARCHAR(35) NOT NULL, " +
			  				"itemId VARCHAR(35) NOT NULL, " +
			  				"categoryId VARCHAR(35) NOT NULL, " +
			  				"assetUnit FLOAT NOT NULL, " +
			  				"assetDate DATETIME , " +
			  				"assetYear INT, " +
			  				"endingMonth CHAR(2), " +
			  				"totalCost DOUBLE, " +
			  				"jan DOUBLE, " +
			  				"feb DOUBLE, " +
			  				"mar DOUBLE, " +
			  				"apr DOUBLE, " +
			  				"may DOUBLE, " +
			  				"jun DOUBLE, " +
			  				"jul DOUBLE, " +
			  				"aug DOUBLE, " +
			  				"sept DOUBLE, " +
			  				"oct DOUBLE, " +
			  				"nov DOUBLE, " +
			  				"dece DOUBLE, " +
			  				"totalDepreciation DOUBLE, " +
			  				"PRIMARY KEY (monthlyDepId))", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX category2_index ON ast_monthlyDepChargesReport(categoryId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX item4_index ON ast_monthlyDepChargesReport(itemId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX monthlyid_index ON ast_monthlyDepChargesReport(monthlyDepId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX disposal2_index ON ast_monthlyDepChargesReport(disposalId)", null);
		 }
		 catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX year_index ON ast_monthlyDepChargesReport(year)", null);
		 }
		 catch (DaoException e) {}		 

		 try{
			  super.update("CREATE TABLE ast_monthlyDepDisposal (" 
					  		+ "id VARCHAR(35) NOT NULL, "
					  		+ "disposalId VARCHAR(35) NOT NULL, "
					  		+ "monthlyDepId VARCHAR(35) NOT NULL, "
					  		+ "assetYear INT, " 
					  		+ "disposalYear INT NOT NULL, " 
							+ "disposalMonth INT NOT NULL, " 
							+ "disposalQty FLOAT NOT NULL, "
					  		+ "PRIMARY KEY (Id))", null);
		 }catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX monthlyid_index ON ast_monthlyDepDisposal(monthlyDepId)", null);
		 }
		 catch (DaoException e) {}
		 

		 try{
			  super.update("CREATE TABLE ast_financialSetup (" 
					  		+ "financialId VARCHAR(35) NOT NULL, "
					  		+ "financialMonth CHAR(2) NOT NULL, "
					  		+ "currencySymbol VARCHAR(35) NOT NULL, "
					  		+ "currencyPattern VARCHAR(255) NOT NULL, "
					  		+ "PRIMARY KEY (financialId))", null);
		 }catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX financialId_index ON ast_financialSetup(financialId)", null);
		 }
		 catch (DaoException e) {}
		 

		 try{
			 super.update( "CREATE TABLE ast_saveMonthlyDepReport (" +
					    "id VARCHAR(35) NOT NULL, " +
		  				"saveMonthlyId VARCHAR(35) NOT NULL, " +
		  				"currencySymbol VARCHAR(35) NOT NULL, "+
				  		"currencyPattern VARCHAR(255) NOT NULL, "+
		  				"categoryName varchar(100) NOT NULL, " +
				  		"depreciation FLOAT NOT NULL, " +
				  		"financialYear VARCHAR(255) NOT NULL, " +
				  		"itemName varchar(100) NOT NULL," +
		  				"assetUnit FLOAT NOT NULL, " +
		  				"assetDate DATETIME , " +
		  				"assetYear INT, " +
		  				"endingMonth CHAR(2), " +
		  				"totalCost DOUBLE, " +
		  				"jan DOUBLE, " +
		  				"feb DOUBLE, " +
		  				"mar DOUBLE, " +
		  				"apr DOUBLE, " +
		  				"may DOUBLE, " +
		  				"jun DOUBLE, " +
		  				"jul DOUBLE, " +
		  				"aug DOUBLE, " +
		  				"sept DOUBLE, " +
		  				"oct DOUBLE, " +
		  				"nov DOUBLE, " +
		  				"dece DOUBLE, " +
		  				"totalDepreciation DOUBLE, " +
		  				"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
		  				"PRIMARY KEY (id))", null);
		 }catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX saveId_index ON ast_saveMonthlyDepReport(id)", null);
		 }
		 catch (DaoException e) {}
		 

		 try{
			 super.update(	"CREATE TABLE ast_saveFixedAssetReport (" +
					 	"id VARCHAR(35) NOT NULL, " +
						"currencySymbol VARCHAR(35) NOT NULL, " +
				  		"currencyPattern VARCHAR(255) NOT NULL, " +
		  				"saveAssetId VARCHAR(35) NOT NULL, " +
		  				"categoryName varchar(100) NOT NULL, " +
				  		"depreciation FLOAT NOT NULL, " +
				  		"financialYear VARCHAR(255) NOT NULL, " +
				  		"itemName varchar(100) NOT NULL," +
		  				"assetUnit FLOAT, " +
		  				"assetDate DATETIME , " +
		  				"assetYear INT, " +
		  				"endingMonth CHAR(2), " +
		  				"costBalBf DOUBLE, " +
		  				"costAdditon DOUBLE, " +
		  				"costDisposal DOUBLE, " +
		  				"costBalCf DOUBLE, " +
		  				"accumDepnBalBf DOUBLE, " +
		  				"accumDepnDCharge DOUBLE, " +
		  				"accumDepnDisposal DOUBLE, " +
		  				"accumDepnBalCf DOUBLE, " +
		  				"nbv DOUBLE, " +
		  				"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
		  				"PRIMARY KEY (id))", null);
			  
		 }catch (DaoException e) {}
		 
		 try{
			  super.update(	"CREATE INDEX saveAssetId_index ON ast_saveFixedAssetReport(id)", null);
		 }
		 catch (DaoException e) {}
		 
		 //set default Financial Setup	 	
			if(iGetSizeFinancialSetup()== 0){
				DataFinancialSetup objFinancialSetup = new DataFinancialSetup();
				UuidGenerator uuid = UuidGenerator.getInstance();
				objFinancialSetup.setFinancialId((String)uuid.getUuid());		
				objFinancialSetup.setCurrencyPattern("#,###,###,##0.00");
				objFinancialSetup.setCurrencySymbol("RM");
				objFinancialSetup.setFinancialMonth("11"); //Decemeber
				//insert record
				insertFinancialSetup(objFinancialSetup);			 
			}
		
	  }
	 
		 
//-------------- ast_assetCategory table -----------------
	    
	 public void insertCategory (DataCategory objCategories)throws DaoException {

		super.update("INSERT INTO ast_assetCategory (categoryId, categoryName, depreciation) VALUES (#categoryId#, #categoryName#, #depreciation#)", objCategories);	
	}
	 
	 public void updateCategory (DataCategory objCategories) throws DaoException {
		 
		super.update("UPDATE ast_assetCategory SET categoryName=#categoryName#, depreciation=#depreciation# WHERE categoryId=#categoryId# ", objCategories);	 
	 }

	 public void deleteCategory(String categoryid)throws DaoException{

		super.update("DELETE FROM ast_assetCategory WHERE categoryId=?", new Object[]{(String)categoryid});
	 }
	 public Collection sortCategory(String[] listCategory)throws DaoException{
	  //return a row of selected data	
		 StringBuffer sql = new StringBuffer("SELECT categoryId, categoryName, depreciation FROM ast_assetCategory WHERE 1=1 ");
		 Collection paramList = new ArrayList();
         if (listCategory != null && listCategory.length > 0) {
             sql.append(" AND categoryId IN (");
             for (int i=0; i<listCategory.length; i++) {
                 if (i > 0)
                     sql.append(",");
                 sql.append("?");
             }
             sql.append(")");
             sql.append(" ORDER BY categoryName, categoryId ASC");
             paramList.addAll(Arrays.asList(listCategory));
           
             return super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, -1);
            
         }
	  return null;
		
	 }
	 
	 public Map retrieveCategory(String categoryid) throws DaoException{
	    //return a row of selected data	    	
	    Collection result = super.select( "SELECT categoryId, categoryName, depreciation FROM ast_assetCategory WHERE categoryId=?", HashMap.class, new Object[]{(String)categoryid}, 0, 1);
		
	    Map resultMap = new HashMap();
	    	
	    if(result.size()>0){	    		
	    	resultMap = (Map)result.iterator().next();	    		
	    	return resultMap;	    		
	    }	    	
	    return null;	    	
	}
	 
	 // Search Filter
	  public Collection listingCategory(String keyword,String sort,boolean desc,int start,int rows) throws DaoException {

	        String condition = (keyword != null) ? "%" + keyword + "%" : "%";
	        String orderBy = (sort != null) ? sort : "categoryName";
	        if (desc)
	            orderBy += " DESC";
	        Object[] args = { condition };

	        return super.select("SELECT categoryId, categoryName, depreciation FROM ast_assetCategory WHERE categoryName LIKE ? ORDER BY " + orderBy, DataCategory.class, args, start, rows);
	   }
	  
	  public Collection listCategoryType() throws DaoException {
			  
		  Collection result = super.select("SELECT categoryId, categoryName, depreciation FROM ast_assetCategory ORDER BY categoryName ASC", HashMap.class, null, 0, -1);
		  
			if (result.size() > 0)		
			   return result;
			return null;
		   }
	      
	  public Collection getZeroDepRate() throws DaoException {
		  
		  Collection result = super.select("SELECT categoryId, categoryName, depreciation FROM ast_assetCategory WHERE depreciation = 0 ORDER BY categoryName DESC", DataCategory.class, null, 0, -1);
		  
		  	if (result.size() > 0)		
		  		return result;
			return null;
		   }
	      
	  //checking duplicate category name
	  public boolean isUniqueCategory(DataCategory objCategories) throws DaoException {
		
		  Collection count = super.select("SELECT COUNT(categoryId) as total FROM ast_assetCategory WHERE categoryName=?", HashMap.class, new Object[]{(String)objCategories.getCategoryName()}, 0, -1);
		 
		  int total = Integer.parseInt(((HashMap)count.iterator().next()).get("total").toString());
		  	if (total>0)
		  		return false;     
		  	return true;
	  }

//---------------------- ast_assetItems --------------------
	  
	  public void insertItems (DataItems objItems)throws DaoException {
		  
		  super.update("INSERT INTO ast_assetItems (itemId, categoryId, itemName, datePurchased, itemQty,  itemUnitPrice, itemCost, itemDescription, dateCreated) " +
		  		       "VALUES (#itemId#, #categoryId#, #itemName#, #datePurchased#, #itemQty#,  #itemUnitPrice#, #itemCost#, #itemDescription#, now())" , objItems);
	  }
	
	  public void updateItems (DataItems objItems) throws DaoException {
		  
		  super.update("UPDATE ast_assetItems SET itemId=#itemId#, categoryId=#categoryId#, itemName=#itemName#, datePurchased=#datePurchased#, " +
				       "itemQty=#itemQty#,  itemUnitPrice= #itemUnitPrice#, itemCost=#itemCost#, itemDescription=#itemDescription# WHERE itemId=#itemId# ", objItems);
	  }
		
	  public void deleteItems (String itemid)throws DaoException{
		  
		  super.update("DELETE FROM ast_assetItems WHERE itemId=?", new Object[]{(String)itemid});
	  }
	  
	  public Collection retrieveItems()throws DaoException{
			    	
	    Collection result = super.select( "SELECT category.categoryName, category.depreciation, items.itemId, items.categoryId, " +
	    		"items.itemName, items.datePurchased, items.itemQty,  items.itemUnitPrice, items.itemCost, items.itemDescription" +
	    		" FROM ast_assetCategory category INNER JOIN ast_assetItems items ON category.categoryId = items.categoryId" 
	    	, DataItemsCategory.class, null, 0, -1);
		   		    
		    if(result.size()>0)  		
				 return result;  		
		 	return null;
	 }
	  public Collection retrieveItems( String sort, boolean desc, int start, int rows)throws DaoException{	    
		    
		    String sql = "SELECT category.categoryName, category.depreciation, items.itemId, items.categoryId, " +
	    		"items.itemName, items.datePurchased, items.itemQty,  items.itemUnitPrice, items.itemCost, items.itemDescription " +
	    		"FROM ast_assetCategory category INNER JOIN ast_assetItems items ON category.categoryId = items.categoryId "; 			  
			String strdesc = desc ? " " : " DESC" ;

		  	  sql += (sort==null ? "ORDER BY category.categoryId " + strdesc :" ORDER BY "+sort+" "+ strdesc);
		  	
			    Collection result = super.select( sql, DataItemsCategory.class, null, start, rows);
			    if(result.size()>0)  		
					 return result;  		
			 	return null;
	}
	  
	  public Collection retrieveCategoryItems(String categoryid, String keyword ,  String sort, boolean desc, int start, int rows)throws DaoException{
		  String sql =  "SELECT category.categoryName, category.depreciation, items.itemId, items.categoryId, " +
	    				"items.itemName, items.datePurchased, items.itemQty,  items.itemUnitPrice, items.itemCost, items.itemDescription " +
	    				"FROM ast_assetCategory category INNER JOIN ast_assetItems items ON category.categoryId = items.categoryId WHERE 1=1 ";
		  
		  String strdesc = desc ? " " : " DESC" ;
		  
		ArrayList args = new ArrayList();
		
			if(!categoryid.equals("-1") && keyword != null && !keyword.equals("")) {
				sql+= "AND category.categoryId= ? AND items.itemName LIKE ? ";
				 args.add(categoryid);
				 args.add("%"+ keyword +"%");				
			}
			else if(!categoryid.equals("-1")){
				sql+= "AND category.categoryId= ? " ;
				 args.add(categoryid);
			}
			else if (keyword != null && !keyword.equals("")){
  			  sql += "AND items.itemName LIKE ? ";
  			 args.add("%"+ keyword +"%");
  		  }		  
  	
			  //set sort and desc
		  	  sql += (sort==null ? "ORDER BY category.categoryId " + strdesc :" ORDER BY "+sort+" "+ strdesc);
			 
	    Collection result = super.select( sql , DataItemsCategory.class, args.toArray() , start, rows);
		   		    
		    if(result.size()>0)  		
				 return result;  		
		 	return null;
	 }
	  // retrieve a single record by itemId
	  public Map retrieveItems (String itemid)throws DaoException{
			String sql= "SELECT itemId, categoryId, itemName, datePurchased, itemQty,  itemUnitPrice, itemCost, " +
					"itemDescription, dateCreated FROM ast_assetItems WHERE itemId=?";
		    Collection result = super.select(sql, HashMap.class, new Object[]{(String)itemid}, 0, 1);
		   
		    Map resultMap = new HashMap();
		    
		    if(result.size()>0){	    		
		    	resultMap = (Map)result.iterator().next();	    		
		    	return resultMap;	    		
		    }
		return null;
	 }
	
	  public Collection retrieveItems (String[] itemid)throws DaoException{
		  	String sqlCause = "";
			for(int i=0; i< itemid.length ; i++){
				if (sqlCause.length() != 0)
					sqlCause += ",";
				sqlCause += "'" + itemid[i]+ "'";
			}
		
			Collection result = super.select( "SELECT itemId, categoryId, itemName, datePurchased, itemQty,  itemUnitPrice, itemCost, itemDescription, dateCreated  FROM ast_assetItems WHERE itemId IN ("+ sqlCause + ")" , DataItems.class, null, 0, -1);
		    
		    if(result.size()>0) 			    	    		
		    	return result;	    		
		    
		return null;
	 }
	  public Map retrieveItems (String itemid , String categoryid)throws DaoException{
		  //return a row of data	    	
		    Collection result = super.select("SELECT itemId, categoryId, itemName, datePurchased, itemQty,  itemUnitPrice, itemCost, itemDescription, dateCreated  FROM ast_assetItems WHERE itemId=? AND categoryId=?", HashMap.class, new Object[]{(String)itemid, (String)categoryid}, 0, 1);
		   
		    Map resultMap = new HashMap();
		    
		    if(result.size()>0){	    		
		    	resultMap = (Map)result.iterator().next();	    		
		    	return resultMap;	    		
		    }
		return null;
	 }
	  
	  public Collection retrieveItemsByCategoryid (String categoryid)throws DaoException{
		  
		    Collection result = super.select("SELECT itemId, categoryId, itemName, datePurchased, itemQty,  itemUnitPrice, itemCost, itemDescription, dateCreated  FROM ast_assetItems WHERE categoryId=?" , DataItems.class, new Object []{(String)categoryid}, 0, -1);
			   		    
			    if(result.size()>0)  		
					 return result;  		
			 	return null;
	  }
	  
	  // Listing records with selected keyword
	  public Collection listingItems (String keyword, String categoryType, Date frmDate,Date toDate, String sort, boolean desc, int start, int rows) throws DaoException {
		  
		  String sql = "SELECT category.categoryName, items.itemId, items.categoryId, items.itemName, items.datePurchased, items.itemQty,  items.itemUnitPrice, items.itemCost, items.itemDescription " +
		  			"FROM ast_assetItems items  left JOIN ast_assetCategory category ON category.categoryId = items.categoryId "; 		  
		  ArrayList args = new ArrayList();
		  String strdesc = desc ? " " : " DESC" ;

			 // keyword is selected
		   if("".equals(categoryType) || categoryType == null || "-1".equals(categoryType) ){
					if(!"".equals(keyword) && keyword != null ){
						if( frmDate == null && toDate == null )	{						
							  sql += " WHERE items.itemName LIKE ? " ;
							args.add("%"+keyword+"%");
						}
					 }					 
			   }
		   
		  //category type is selected
		   if(!"".equals(categoryType) && categoryType != null && !"-1".equals(categoryType) ){
				if("".equals(keyword) || keyword == null ){
					if( frmDate == null && toDate == null )	{					
						  sql += " WHERE items.categoryId=? " ;	
						  args.add(categoryType);
					}
				 }					 
		   }
		
		  //keyword and Category type are selected
		   if(!"".equals(categoryType) && categoryType != null && !"-1".equals(categoryType) ){
			  if(!"".equals(keyword) && keyword != null ){
				  if( frmDate == null && toDate == null )	{				  
					  sql += " WHERE items.categoryId=? AND items.itemName LIKE ? ";
					  args.add(categoryType);
					  args.add("%"+keyword+"%");
				  }
			  }
		  }
			  
		  //category type and date are selected
		  if(!"".equals(categoryType) && categoryType != null && !"-1".equals(categoryType) ){
			  if( frmDate != null && toDate !=null ){
				  if("".equals(keyword) || keyword == null ){				  
					  sql += " WHERE items.categoryId=? AND items.datePurchased>=? AND  items.datePurchased<=? ";	
					  args.add(categoryType);
					  args.add(frmDate);
					  args.add(toDate);
				  }
			  }
		  }
				  
		  // date are selected
		  if("".equals(categoryType) || categoryType == null || "-1".equals(categoryType) ){
			  if("".equals(keyword) || keyword == null ){
				  if( frmDate !=null && toDate !=null ){				  
					  sql += " WHERE items.datePurchased>=? AND  items.datePurchased<=? ";
					  args.add(frmDate);
					  args.add(toDate);
				  }
			  }
		  }
		  
		  //keyword, category type and date are selected
		  if(!"".equals(categoryType) && categoryType != null && !"-1".equals(categoryType) ){
			  if(!"".equals(keyword) && keyword != null ){
				  if( frmDate !=null && toDate !=null ){		  
					  sql += " WHERE items.categoryId=?  AND items.itemName LIKE ?"+
					  		" AND items.datePurchased>=? AND  items.datePurchased<=? ";
					  args.add(categoryType);
					  args.add("%"+keyword+"%");
					  args.add(frmDate);
					  args.add(toDate);
				  }
			  }
		  }
	
		  //set sort and desc
		  	  sql += (sort==null ? "ORDER BY categoryName " + strdesc :" ORDER BY "+sort+" "+ strdesc);
			 
			return super.select(sql, DataItems.class , args.toArray(), start, rows);						   
	  }
		
	  //checking duplicate items
	  public boolean isUniqueItems(DataItems objItems) throws DaoException {
		
		  Collection count = super.select("SELECT COUNT(itemId) as total FROM ast_assetItems WHERE itemName=? AND categoryId=? AND datePurchased=?",
				  HashMap.class, new Object[]{(String)objItems.getItemName(),(String)objItems.getCategoryId(),(Date)objItems.getDatePurchased()}, 0, 1);
		 
		  int total = Integer.parseInt(((HashMap)count.iterator().next()).get("total").toString());
		  	if (total>0)
		  		return false;     
		  	return true;
	  }
	  
	  //Checking category type is in used or nt
	  public int countCategoryInUsed(String tableId)throws DaoException {
		  
		  Collection count = super.select("SELECT COUNT(itemId) as total FROM ast_assetItems WHERE categoryId=?",  HashMap.class, new Object[]{tableId}, 0, 1);
		
		  	HashMap map = (HashMap) count.iterator().next();

	    return Integer.parseInt(map.get("total").toString());
	  }
	  
//--------------- ast_assetDisposal -----------------

	  public void insertDisposal (DataDisposal objDisposal)throws DaoException {
		
		  super.update("INSERT INTO ast_assetDisposal (disposalId, itemId, disposalQty, disposalCost, dateDisposal,  disposalReason, dateCreated)" +
		  		       " VALUES (#disposalId#, #itemId#, #disposalQty#, #disposalCost#, #dateDisposal#, #disposalReason#, now())", objDisposal);
	  }
	 
	  public void updateDisposal (DataDisposal objDisposal)throws DaoException {
		  
		  super.update("UPDATE ast_assetDisposal SET disposalId=#disposalId#, disposalQty=#disposalQty#, disposalCost= #disposalCost#," +
		  			   " dateDisposal= #dateDisposal#, disposalReason=#disposalReason# WHERE disposalId=#disposalId# ", objDisposal);
	  }
	  
	  public void updateDisposalAndItem (DataItems objItem)throws DaoException {
		  
		  Collection result = super.select("SELECT disposalId, itemId, disposalQty, disposalCost, dateDisposal,  disposalReason, dateCreated FROM ast_assetDisposal WHERE itemId=? ", DataDisposal.class, new Object[]{objItem.getItemId().toString()}, 0, -1);
		  
		  if (result != null && result.size() > 0 ){
				for (Iterator iCount = result.iterator(); iCount.hasNext();){ 
					DataDisposal objDisposal = (DataDisposal)iCount.next();
					double dDisposalCost = objDisposal.getDisposalQty() * objItem.getItemUnitPrice();
					objDisposal.setDisposalCost(dDisposalCost);
					//update record
					updateDisposal(objDisposal);
				}
		  }
	  }
	  public void deleteDisposal (String disposalid)throws DaoException{
		  
		  super.update("DELETE FROM ast_assetDisposal WHERE disposalId=?", new Object[]{(String)disposalid});
	  }
	  
	  public void deleteDisposalbyItem (String itemId)throws DaoException{
		  
		  super.update("DELETE FROM ast_assetDisposal WHERE itemId=?", new Object[]{(String)itemId});
	  }

	    public Collection getDisposalDateByYear(String categoryid, Date dateFrm, Date dateTo)throws DaoException {

			  String sql =  "SELECT category.categoryName, items.itemName, items.datePurchased, items.itemQty, disposal.disposalId, " +
			  				"disposal.itemId, disposal.disposalQty, disposal.disposalCost, disposal.dateDisposal,  disposal.disposalReason, disposal.dateCreated " +
			  				"FROM ast_assetCategory category " +
			  				"INNER JOIN ast_assetItems items ON category.categoryId = items.categoryId " +
			  				"INNER JOIN ast_assetDisposal disposal ON items.itemId = disposal.itemId " +
			  				"WHERE category.categoryId=? AND disposal.dateDisposal>=? AND disposal.dateDisposal<=? " +
			  				"ORDER BY category.categoryId ASC";
			  
			  Object [] args = new Object[]{categoryid, dateFrm, dateTo};				 
		  
			  Collection result = super.select(sql, HashMap.class, args, 0, -1);
			  
			  if(result != null && result.size() > 0)
				  return result;			  
			  return null;
		 }

	  
	  //retrieve a single record by disposalid
	  public Map retrieveDisposal(String disposalid)throws DaoException{
	    	
		    Collection result = super.select( "SELECT p.disposalId, p.itemId, p.disposalQty, p.disposalCost, " +
		    					"p.dateDisposal,  p.disposalReason, p.dateCreated , c.categoryName , " +
		    					"i.itemName, i.categoryId" +
		    					" FROM ast_assetCategory c" +
		    					" inner JOIN ast_assetItems i ON c.categoryId = i.categoryId" +
		    					" inner JOIN ast_assetDisposal p ON i.itemId = p.itemId" + 
		    					" WHERE p.disposalId=?", HashMap.class, new Object[]{(String)disposalid}, 0, 1);
		   
		    Map resultMap = new HashMap();
		    
		    if(result.size()>0){	    		
		    	resultMap = (Map)result.iterator().next();	    		
		    	return resultMap;	    		
		    }
		return null;
		  
	  }
		 public int iGetTotalDisposalItemOnYear(String itemid, String categoryid, Date dateFrom, Date dateTo)throws DaoException {
			 int iReturn = 0;
	
			  Object [] args = null ; 
			  String sql = "SELECT SUM(disposalQty)AS total FROM ast_assetCategory category" +
				" inner JOIN ast_assetItems items ON category.categoryId = items.categoryId" +
				" inner JOIN ast_assetDisposal disposal ON items.itemId = disposal.itemId" +
				" where disposal.itemId=? and items.categoryId =? and dateDisposal>=? and dateDisposal<=?";			 
			   
			  args = new Object[]{itemid,categoryid, dateFrom, dateTo};
			  
			  Collection count = super.select(sql, HashMap.class, args, 0, 1);
			
			  if (count!=null && count.size()>0) {
			  	HashMap map = (HashMap) count.iterator().next();
			  	if (map.get("total") != null){
			  		 String strTotal = map.get("total").toString();
			  		iReturn = (int)Math.ceil(Double.parseDouble(strTotal));
			  	}
			  }
		    return iReturn;
		  }
		 
		 public int iGetDisposalUnitByItemId(String itemId)throws DaoException {
			 
			  int iReturn = 0;
			 String sql = "SELECT SUM(disposalQty)AS total FROM ast_assetDisposal disposal where itemId=? ";			 
			Collection result = super.select(sql, HashMap.class,new Object[]{(String)itemId}, 0, 1);
			
			  if (result!=null && result.size()>0) {
			  	HashMap map = (HashMap) result.iterator().next();
			  	if (map.get("total") != null){
			  		 String strTotal = map.get("total").toString();
			  		iReturn = (int)Math.ceil(Double.parseDouble(strTotal));
			  	}
			  }
		    return iReturn;
		 }
		 
		
		public Collection getDisposalYear(String itemid, Date dateFrom, Date dateTo)throws DaoException {
		
			Object [] args = null ; 
			  String sql = "SELECT p.disposalId, p.itemId, p.disposalQty, p.disposalCost, " +
				    	" p.dateDisposal,  p.disposalReason, p.dateCreated" +
				    	" FROM ast_assetCategory c" +
						" inner JOIN ast_assetItems i ON c.categoryId = i.categoryId" +
						" inner JOIN ast_assetDisposal p ON i.itemId = p.itemId" +
						" where p.itemId=? and p.dateDisposal>=? and p.dateDisposal<=?";
			   
			  args = new Object[]{itemid, dateFrom, dateTo};
			  
			  Collection result = super.select(sql, DataDisposal.class, args, 0, -1);
			
			  if (result!=null && result.size()>0) {
				  	return result;
			  }
		   return null;
		 }

	  public Collection retrieveDisposal(String itemid, String categoryid)throws DaoException{
		    	
		    Collection result = super.select( "SELECT p.disposalId, p.itemId, p.disposalQty, p.disposalCost, " +
		    				" p.dateDisposal,  p.disposalReason, p.dateCreated " +
		    				" FROM ast_assetCategory category" +
		    				" inner JOIN ast_assetItems items ON category.categoryId = items.categoryId" +
		    				" inner JOIN ast_assetDisposal p ON items.itemId = p.itemId" +
		    				" where p.itemId=? and items.categoryId =? ORDER BY dateDisposal ASC", HashMap.class, new Object[]{(String)itemid,(String)categoryid}, 0, -1);
		   
		    if (result.size()>0)
		    	return result;
		    return null;		  
	  }

	  // Listing records with selected keyword
	  public Collection listingDisposal( String keyword, String categoryType,
			  Date frmDate, Date toDate, String sort, boolean desc, int start, int rows) 
	  throws DaoException {
		  
		  String sql =  "SELECT c.categoryName, i.itemName, i.itemUnitPrice, p.disposalId, p.itemId, p.disposalQty," +
		  				" p.disposalCost, p.dateDisposal,  p.disposalReason, p.dateCreated" +
		  				" FROM ast_assetCategory c " +
		  				" RIGHT JOIN ast_assetItems i ON c.categoryId = i.categoryId" +
		  				" RIGHT JOIN ast_assetDisposal p ON i.itemId = p.itemId ";		 
			 	 		   	
		  ArrayList args = new ArrayList(); 
		  String strdesc = null;
	
		   strdesc = desc ? " " : " ASC" ;

			 // keyword is selected
		   if("".equals(categoryType) || categoryType == null || "-1".equals(categoryType) ){
					if(!"".equals(keyword) && keyword != null ){
						if( frmDate == null && toDate == null ){							
							  sql += " WHERE i.itemName LIKE ? " ;	
							  args.add("%"+keyword+"%");						
						 }
					 }					 
			   }
		   
		  //category type is selected
   			if(!"".equals(categoryType) && categoryType != null && !"-1".equals(categoryType) ){
				if("".equals(keyword) || keyword == null ){
					if( frmDate == null && toDate == null ){						
						  sql += " WHERE i.categoryId=? " ;	
						  args.add(categoryType);						
					 }
				 }					 
		   }
		
		  //keyword and Category type are selected
		  if(!"".equals(categoryType) && categoryType != null && !"-1".equals(categoryType) ){
			  if(!"".equals(keyword) && keyword != null ){
				  if( frmDate == null && toDate == null ){					  
					  sql += " WHERE i.categoryId=? AND i.itemName LIKE ?  ";			  
					  args.add(categoryType);
					  args.add("%"+keyword+"%");					 
				  }
			  }
		  }
	
		  
		  //category type and date are selected
		  if(!"".equals(categoryType) && categoryType != null && !"-1".equals(categoryType) ){
			  if( frmDate != null && toDate !=null ){
				  if("".equals(keyword) || keyword == null ){					  
					  sql += " WHERE i.categoryId=? AND p.dateDisposal>=? AND  p.dateDisposal<=? ";
					  args.add(categoryType);
					  args.add(frmDate);
					  args.add(toDate);
				  }
			  }
		  }
		
		  
		  // date are selected
		  if("".equals(categoryType) || categoryType == null || "-1".equals(categoryType) ){
			  if("".equals(keyword) || keyword == null ){
				  if( frmDate !=null && toDate !=null ){
					  
					  sql += " WHERE p.dateDisposal>=? AND  p.dateDisposal<=? ";
					  args.add(frmDate);
					  args.add(toDate);
				  }
			  }
		  }
		  
		  //keyword, category type and date are selected
		  if(!"".equals(categoryType) && categoryType != null && !"-1".equals(categoryType) ){
			  if(!"".equals(keyword) && keyword != null ){
				  if( frmDate !=null && toDate !=null ){					  
					  sql += " WHERE i.categoryId=?  AND i.itemName LIKE ? AND p.dateDisposal>=? AND  p.dateDisposal<=? ";
					  args.add(categoryType);
					  args.add("%"+keyword+"%");
					  args.add(frmDate);
					  args.add(toDate);
				  }
			  }
		  }
	
		  //set sort and desc
		  	  sql += (sort==null ? "ORDER BY itemName " + strdesc :" ORDER BY "+sort+" "+ strdesc);
		
			return super.select(sql, DataDisposal.class ,  args.toArray(), start, rows);						   
	  }

//	--------------- ast_monthlyDepChargesReport -----------------
	  
	  public void insertMonthlyDep (DataMonthlyDepReport objMonthlyDep)throws DaoException {
		  
		  super.update("INSERT INTO ast_monthlyDepChargesReport (" +
				  "monthlyDepId, itemId, categoryId, assetUnit, assetDate, assetYear, endingMonth, totalCost, " +
				  "jan, feb, mar, apr, may, jun, jul, aug, sept, oct, nov, dece, totalDepreciation) " +
				  "VALUES " +
				  "(#monthlyDepId#, #itemId#, #categoryId#, #assetUnit#,  #assetDate#, #assetYear#, #endingMonth#, #totalCost#, " +
				  "#jan#, #feb#, #mar#, #apr#, #may#, #jun#, #jul#, #aug#, #sept#, #oct#, #nov#, #dece#, " +
				  "#totalDepreciation#)", objMonthlyDep);
	  }
		
	  public void deleteMonthlyDep ()throws DaoException{
		 //Delete all records
		  super.update("DELETE FROM ast_monthlyDepChargesReport", null);
	  }
	 
	  public Collection retrieveMonthDep (String categoryid, Date frmDate, Date toDate)throws DaoException {
		  
		  Object [] args = null ;  
		  String sql = "SELECT monthlyDepId, itemId, categoryId, assetUnit, assetDate, assetYear, endingMonth, totalCost, " +
				  "jan, feb, mar, apr, may, jun, jul, aug, sept, oct, nov, dece, totalDepreciation" +
				  " FROM ast_monthlyDepChargesReport WHERE assetDate>=? AND assetDate<=? AND categoryId=?";
			  args = new Object[] {frmDate,toDate,categoryid};
	
		  Collection result = super.select(sql ,HashMap.class, args , 0 , -1);
		  
		  return result;
	  }
	  
	  public Collection retrieveMonthDep ()throws DaoException{
		  //return a row of data	    	
	    Collection result = super.select( "SELECT monthlyDepId, itemId, categoryId, assetUnit, assetDate, assetYear, " +
	    		"endingMonth, totalCost, jan, feb, mar, apr, may, jun, jul, aug, sept, oct, nov, dece, totalDepreciation" +
				" FROM ast_monthlyDepChargesReport", DataMonthlyDepReport.class, null, 0, -1);
		   		    
		    if(result.size()>0)  		
				 return result;  		
		 	return null;
	 }
	 
	  public Collection retrieveMonthDepReport (int iYear, String categoryid)throws DaoException{
		  		  
		  String sql = "SELECT monthlyDepId, itemId, categoryId, assetUnit, assetDate, assetYear, endingMonth, " +
		  		"totalCost, jan, feb, mar, apr, may, jun, jul, aug, sept, oct, nov, dece, totalDepreciation " +
		  		"FROM ast_monthlyDepChargesReport WHERE assetYear= ? AND categoryId=? " +
		  		"ORDER BY categoryId ASC ";
	    Collection result = super.select(sql, DataMonthlyDepReport.class, new Object[]{iYear,categoryid}, 0, -1);
		   		    
		    if(result.size()>0)  		
				 return result;  		
		 	return null;
	 }
	  
	  public int countMonthDepReport (int iYear, String categoryid)throws DaoException{
  		  
		  String sql = "SELECT COUNT(monthlyDepId) AS totalItem FROM ast_monthlyDepChargesReport " +
		  		       "WHERE assetYear=? AND categoryId=? ORDER BY categoryId ASC ";
		  
		  Collection count = super.select(sql, HashMap.class, new Object[]{iYear, categoryid }, 0, 1);
		  
		  if(count.size() > 0 ){
			  Map mapResult = (Map)count.iterator().next();
			  if(mapResult.get("totalItem") != null)
				  return Integer.parseInt(mapResult.get("totalItem").toString());		
		  }		  
	  return 0;	
	 }
	  public double getSumOfColumn(int iYear, String categoryid, String columnField)throws DaoException{
		  
		  String sql = "SELECT SUM(" + columnField + ") AS sumTotal FROM ast_monthlyDepChargesReport "+
			           "WHERE assetYear=? AND categoryId=?";

	    Collection result = super.select(sql, HashMap.class, new Object[]{iYear, categoryid}, 0, 1);
	    
	    Map resultMap = new HashMap();	    
		if(result.size()>0){	    		
		    resultMap = (Map)result.iterator().next();
		    if (resultMap.get("sumTotal") != null)
		    	return ((Number)resultMap.get("sumTotal")).doubleValue();	  
		} 		
		 	return 0;
	 }
	
	  
	 public int countMonthlyDepRecord()throws DaoException {
		  
		  int iReturn = 0;
		  Collection count = super.select("SELECT COUNT(monthlyDepId)AS total FROM ast_monthlyDepChargesReport", HashMap.class, null, 0, 1);
		
		  if (count!=null && count.size()>0) {
		  	HashMap map = (HashMap) count.iterator().next();
		  	iReturn = Integer.parseInt(map.get("total").toString());
		  }
	    return iReturn;
	  }
	  		  
	 public int iGetAvailableUnitFrmPreviousRecord(String itemid, int iRecordYear) throws DaoException {
		 	    
		 String sql = "SELECT assetUnit FROM ast_monthlyDepChargesReport WHERE assetYear=? AND itemId =?";
		 Collection result = super.select(sql, HashMap.class, new Object[]{iRecordYear, itemid}, 0, 1);
		 
		 if (result != null && result.size() > 0){
			  	HashMap map = (HashMap) result.iterator().next();
			  	if (map.get("assetUnit") != null){
				  	String unit = map.get("assetUnit").toString();
				  	double dUnit = Double.parseDouble(unit);
				  	int iUnit= (int) Math.ceil(dUnit);
				  	if (iUnit > 0)
				  		return iUnit;
			  	}
		 }
		 return 0;		 
	 }
	 
	 public Collection getMonthlyRecordsByYearCategory(String categoryid, int iAssetYear)throws DaoException {
		 String sql =" SELECT ca.categoryName, ca.depreciation, it.itemName, mo.monthlyDepId, mo.itemId, mo.categoryId," +
		 		" mo.assetUnit, mo.assetDate, mo.assetYear, mo.endingMonth, mo.totalCost, " +
				"mo.jan, mo.feb, mo.mar, mo.apr, mo.may, mo.jun, mo.jul, mo.aug, mo.sept, mo.oct, mo.nov, mo.dece, mo.totalDepreciation " +
				"FROM ast_monthlyDepChargesReport mo " +
		 		"INNER JOIN ast_assetItems it ON it.itemId = mo.itemId " +
		 		"INNER JOIN ast_assetCategory ca ON it.categoryId = ca.categoryId " +
		 		"WHERE mo.categoryId=? AND mo.assetYear =? ";
		 
		 Collection result = super.select(sql,  DataSaveMonthDepReport.class, new Object[]{categoryid, iAssetYear}, 0, -1);
		 if (result.size() > 0)
			 return result;
		 return null;
		 
	 }
// --------------- ast_monthlyDepDisposal ----------------	  
	  
	  public void insertMonthlyDepDisposal (DataMonthlyDepDisposal objMonthlyDepDis)throws DaoException {

		  super.update("INSERT INTO ast_monthlyDepDisposal (id, disposalId, monthlyDepId, assetYear ,disposalYear, disposalMonth, disposalQty)" +
	  		       " VALUES (#id#, #disposalId#, #monthlyDepId#, #assetYear#, #disposalYear#, #disposalMonth#, #disposalQty#)", objMonthlyDepDis);
	  }
		
	  public void deleteMonthlyDepDisposal ()throws DaoException{
		 //Delete all records
		  super.update("DELETE FROM ast_monthlyDepDisposal", null);
	  }	  

	  public int iGetTotalDisposal(String monthid, int iMonth, int iYear)throws DaoException {
			  
		int iReturn = 0;
			
		Collection count = super.select("SELECT SUM(disposalQty)AS total FROM ast_monthlyDepDisposal " +
			"WHERE monthlyDepId=? AND disposalMonth= ? AND disposalYear= ?", 
			HashMap.class, new Object[]{monthid, iMonth, iYear}, 0, 1);
		
			if (count != null && count.size()>0) {
			HashMap map = (HashMap) count.iterator().next();
			if(map.get("total") != null){
			  	String strTotal = map.get("total").toString();
		  		iReturn = (int)Math.ceil(Double.parseDouble(strTotal));
			  }
			 }			
		   return iReturn;
		}
	  public Collection retrieveItemByMonthDepid (String monthid, int iMonth, int iYear)throws DaoException {
		  String sql = "SELECT it.itemUnitPrice, ca.depreciation FROM ast_monthlyDepDisposal mo " + 
		  			"INNER JOIN ast_assetDisposal po ON mo.disposalId = po.disposalId " +
			  		"INNER JOIN ast_assetItems it ON it.itemId = po.itemId " +
			  		"INNER JOIN ast_assetCategory ca ON it.categoryId = ca.categoryId " +
			  		"WHERE mo.monthlyDepId = ? AND disposalYear=? AND disposalMonth=? ";
		  
		  Collection result = super.select(sql, DataItemsCategory.class, new Object[]{monthid, iYear, iMonth }, 0, 1);
		  if (result.size() > 0 )
			  return result;
		  return null;
		  
	  }
	  public Collection retrieveMonthDepDisposal (String monthlyDepid)throws DaoException {
		
		String sql = " SELECT id, disposalId, monthlyDepId, assetYear ,disposalYear, disposalMonth, disposalQty FROM ast_monthlyDepDisposal WHERE monthlyDepId=? ORDER BY disposalYear, disposalMonth ASC ";		  
		Collection result = super.select(sql ,DataMonthlyDepDisposal.class, new Object[]{(String)monthlyDepid} , 0 , -1);
		  
		  return result;
	}
	
	  public Collection retrieveMonthDepDisposal (String monthlyDepid, int iyear)throws DaoException {
			
			String sql = "SELECT id, disposalId, monthlyDepId, assetYear ,disposalYear, disposalMonth, disposalQty FROM ast_monthlyDepDisposal WHERE monthlyDepId=?" +
			" AND assetYear=? ORDER BY disposalYear, disposalMonth ASC";		  
			Collection result = super.select(sql ,DataMonthlyDepDisposal.class, new Object[]{monthlyDepid , iyear} , 0 , -1);			
		
			return result;
		}
	  
// ---------------------- ast_fixedAssetListReport--------------
	  
	  public void insertFixedAssetReport (DataFixedAssetReport objFixedAsset)throws DaoException {
		  
		  super.update("INSERT INTO ast_fixedAssetListReport (" +
				  "fixedAssetId, itemId, categoryId, monthlyDepId, assetUnit, assetDate, assetYear, endingMonth, costBalBf, " +
				  "costAdditon, costDisposal, costBalCf, accumDepnBalBf, accumDepnDCharge, accumDepnDisposal, " +
				  "accumDepnBalCf, nbv) " +
				  "VALUES " +
				  "(#fixedAssetId#, #itemId#, #categoryId#,#monthlyDepId#, #assetUnit#,  #assetDate#, #assetYear#, #endingMonth#, #costBalBf#, " +
				  "#costAdditon#, #costDisposal#, #costBalCf#, #accumDepnBalBf#, #accumDepnDCharge#, #accumDepnDisposal#, " +
				  "#accumDepnBalCf#, #nbv#)", objFixedAsset);
	  }	  
		
	  public void deleteFixedAssetReport ()throws DaoException{
		 //Delete all records
		  super.update("DELETE FROM ast_fixedAssetListReport", null);
	  }
	
	  public int countFixedAssetRecord()throws DaoException {
		  
		  int iReturn = 0;
		  Collection count = super.select("SELECT COUNT(fixedAssetId)AS total FROM ast_fixedAssetListReport", HashMap.class, null, 0, 1);
		
		  if (count!=null && count.size()>0) {
		  	HashMap map = (HashMap) count.iterator().next();
		  	iReturn = Integer.parseInt(map.get("total").toString());
		  }
	    return iReturn;
	  }
		 //get the disposal item on the same month of purchased Date
	public Collection getDisposalOnSameMonth()throws DaoException {
			 
		String sql = "SELECT items.itemId, items.categoryId, items.itemName, items.datePurchased, items.itemQty,  " +
				"items.itemUnitPrice, items.itemCost, items.itemDescription, items.dateCreated " +
				"FROM ast_assetItems items WHERE items.itemId NOT IN (SELECT itemId FROM ast_fixedAssetListReport)";
			 
		Collection result = super.select(sql, DataItems.class, null, 0, -1);
			 
		 if (result != null && result.size() > 0)
			 return result;
		return null;
	 }
		 
	  public  Map retrieveFixedAsset(String itemid , int iYearOfRecord)throws DaoException{
		  
		  String sql="SELECT costBalCf, accumDepnBalCf FROM ast_fixedAssetListReport WHERE itemId=? AND assetYear=?";
		  Collection result = super.select(sql, HashMap.class, new Object[]{itemid, iYearOfRecord}, 0, 1);
		   
		  Map resultMap = new HashMap();
			    
			if(result.size()>0){	    		
			    resultMap = (Map)result.iterator().next();	    		
			    	return resultMap;	  
			}
			return null;			  
	  }
	  
	  public int countFixedAsset(String categoryid, int yearTo)throws DaoException{

		  String sql= "SELECT COUNT(fixedAssetId) AS totalItem FROM ast_fixedAssetListReport " +
		  			  "WHERE categoryId=? AND assetYear=? ORDER BY categoryId ASC";
		 
		 Collection count = super.select(sql, HashMap.class, new Object[]{categoryid, yearTo}, 0, 1);
			  
			  if(count.size() > 0 ){
				  Map mapResult = (Map)count.iterator().next();
				  if(mapResult.get("totalItem") != null)
					  return Integer.parseInt(mapResult.get("totalItem").toString());		
			  }		  
		  return 0;		  
	  }

	  public  Collection retrieveFixedAssetRecord(String categoryid, int yearTo)throws DaoException{
		  		
		  String sql= "SELECT fixedAssetId, itemId, categoryId, monthlyDepId, assetUnit, assetDate, assetYear, endingMonth, costBalBf, " +
				  "costAdditon, costDisposal, costBalCf, accumDepnBalBf, accumDepnDCharge, accumDepnDisposal, " +
				  "accumDepnBalCf, nbv " +
				  "FROM ast_fixedAssetListReport "+
		  		  "WHERE categoryId=? AND assetYear=? ORDER BY categoryId ASC";
		 
		  Collection result = super.select(sql, DataFixedAssetReport.class, new Object[]{categoryid, yearTo}, 0, -1); 
			   
			if(result.size()>0)    		
			  	return result;	  
			return null;			  
	  }	
	  
	  public  Collection retrieveFixedAssetRecord(String categoryid, String itemid, int yearTo)throws DaoException{
	  		
		  String sql= "SELECT fixedAssetId, itemId, categoryId, monthlyDepId, assetUnit, assetDate, assetYear, endingMonth, costBalBf, " +
				  "costAdditon, costDisposal, costBalCf, accumDepnBalBf, accumDepnDCharge, accumDepnDisposal, " +
				  "accumDepnBalCf, nbv " +
				  "FROM ast_fixedAssetListReport " +
		  		  "WHERE categoryId=? AND itemId =? AND assetYear=? " +
		  		  " ORDER BY categoryId ASC";
		 
		  Collection result = super.select(sql, DataFixedAssetReport.class, new Object[]{categoryid, itemid,yearTo }, 0, -1); 
			   
			if(result.size()>0)	    		
			  	return result;	  
			return null;			  
	  }	
	  
	  public int retrieveMinNBV(String itemid, String categoryid)throws DaoException{
	  		
		  String sql = "SELECT MIN(nbv)as minNBV FROM ast_fixedAssetListReport WHERE itemId =? AND categoryId=?";

		  Collection result = super.select(sql, HashMap.class, new Object[]{itemid,categoryid}, 0, 1);
		  
		  if(result.size() > 0 ){
			  Map mapResult = (Map)result.iterator().next();
			  if(mapResult.get("minNBV") != null){
				  double dNBV = Double.parseDouble(mapResult.get("minNBV").toString());
				  	BigDecimal bd = new BigDecimal(dNBV);
				  	bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
				  return (int)Math.ceil(bd.doubleValue());
			  }
		  }		  
		  return 0;				  
	  }	
	
	  public int retrieveMaxYear(String itemid)throws DaoException{
	  		
		  String sql="SELECT MAX(assetYear) as maxYear FROM ast_fixedAssetListReport WHERE itemId =?";
		  
		  Collection result = super.select(sql, HashMap.class, new Object[]{itemid}, 0, 1);
		  
		  if(result.size() > 0 ){
			  Map mapResult = (Map)result.iterator().next();
			  if(mapResult.get("maxYear") != null){
				  return Integer.parseInt(mapResult.get("maxYear").toString());
			  } 
		  }		  
		  return 0;				  
	  }	
	  
	  public int retrieveMinYear(String itemid)throws DaoException{
	  		
		  String sql="SELECT MIN(assetYear) as minYear FROM ast_fixedAssetListReport WHERE itemId =?";
		  
		  Collection result = super.select(sql, HashMap.class, new Object[]{itemid}, 0, 1);
		  
		  if(result.size() > 0 ){
			  Map mapResult = (Map)result.iterator().next();
			  if(mapResult.get("minYear") != null){
				  return Integer.parseInt(mapResult.get("minYear").toString());
			  } 
		  }		  
		  return 0;				  
	  }	
	
	  public double getAssetSumOfColumn(int iToYear , String categoryid, String columnField)throws DaoException{
  		  
		  String sql = "SELECT SUM(" + columnField + ") AS sumTotal FROM ast_fixedAssetListReport "+
		  				"WHERE assetYear=? AND categoryId=?";		  
		  
	    Collection result = super.select(sql, HashMap.class, new Object[]{iToYear, categoryid}, 0, 1);
	    
	    Map resultMap = new HashMap();	    
		if(result.size()>0){	    		
		    resultMap = (Map)result.iterator().next();
		    if (resultMap.get("sumTotal") != null)
		    	return ((Number)resultMap.get("sumTotal")).doubleValue();	  
		} 		
		 	return 0;
	 }
	  
	  
	  public int getMinYear(String tableName) throws DaoException{
		  
		  String sql = "SELECT MIN(assetYear) AS minYear FROM "+ tableName ;
		  Collection result = super.select(sql, HashMap.class, null, 0, 1);
		  
		  if(result.size() > 0 ){
			  Map mapResult = (Map)result.iterator().next();
			  if(mapResult.get("minYear") != null){
				  return Integer.parseInt(mapResult.get("minYear").toString());
			  }
		  }		  
		  return 0;
	  } 
	  
	  public Collection getFixedRecordsByYearCategory(String categoryid, int iAssetYear)throws DaoException {
		String sql =" SELECT ca.categoryName, ca.depreciation, it.itemName, fi.fixedAssetId, fi.itemId, fi.categoryId, " +
				  "fi.monthlyDepId, fi.assetUnit, fi.assetDate, fi.assetYear, fi.endingMonth, fi.costBalBf, " +
				  "fi.costAdditon, fi.costDisposal, fi.costBalCf, fi.accumDepnBalBf, fi.accumDepnDCharge, fi.accumDepnDisposal, " +
				  "fi.accumDepnBalCf, fi.nbv " +
				  "FROM ast_fixedAssetListReport fi " +
			 	  "INNER JOIN ast_assetItems it ON it.itemId = fi.itemId " +
			 	  "INNER JOIN ast_assetCategory ca ON it.categoryId = ca.categoryId " +
			 	  "WHERE fi.categoryId=? AND fi.assetYear =? ";
			 
		Collection result = super.select(sql, DataSaveFixedAssetReport.class, new Object[]{categoryid, iAssetYear}, 0, -1);
		if (result.size() > 0)
			return result;
		return null;
			 
		 }

//-------------------- ast_assetNotification --------------------------
	  
	  public void insertNotification (DataNotification objNotification)throws DaoException {
	
		  super.update("INSERT INTO ast_assetNotification (" +
				  "notificationId, notificationTitle, notificationDate, notificationTime, notificationMsg, " +
				  "notifyMethod, senderID, dateCreated) " +
				  "VALUES " +
				  "(#notificationId#, #notificationTitle#, #notificationDate#, #notificationTime#, " +
				  "#notificationMsg#,  #notifyMethod#, #senderID#, now())", objNotification);
	  }
      // Search Filter

	  public Collection listingNotification(String keywordTitle, String sort, boolean desc, int start, int rows)throws DaoException{
		  String condition = (keywordTitle != null) ? "%" + keywordTitle + "%" : "%";
	      String orderBy = (sort != null) ? sort : " notificationId ";	
		   
		        if (desc)
		            orderBy += " DESC";
		        Object[] args = { condition };

	
		  String sql = "SELECT notificationId, notificationTitle, notificationDate, notificationTime, notificationMsg, " +
				  "notifyMethod, senderID, dateCreated " +
				  "FROM ast_assetNotification WHERE notificationTitle LIKE ? ORDER BY " + orderBy;
		  
		  	Collection result = super.select(sql, DataNotification.class, args, start, rows);
		  	
		  	if(result.size() > 0){
		  		return result;
		  	}
		  	return null;
	  }
	  
	  public  Collection retrieveNotification(String id)throws DaoException{
	  	
		  String sql="SELECT notificationId, notificationTitle, notificationDate, notificationTime, notificationMsg, " +
				  "notifyMethod, senderID, dateCreated FROM ast_assetNotification WHERE notificationId=?";
		  
		  Collection result = super.select(sql, DataNotification.class, new Object[]{id}, 0, -1); 
			   
			if(result.size()>0){	    		
			  	return result;	  
			}
			return null;			  
	  }	
	  
	  public int iGetTotalNotification() throws DaoException{
		  String sql = "SELECT COUNT(notificationId) AS total FROM ast_assetNotification";
		  Collection result = super.select(sql, HashMap.class, null, 0, 1);
		  if (result != null && result.size() > 0 ){
			  Map mapResult = (Map)result.iterator().next();
			  if(mapResult.get("total") != null)
				  return Integer.parseInt(mapResult.get("total").toString());	
		  }		  
		 return 0;
	  }


//-------------------- ast_assetAddressee -----------------------------
	
  public void insertAddressee (DataAddressee objAddressee)throws DaoException {		  

	  	super.update("INSERT INTO ast_assetAddressee (addresseeId, notificationId, recipientId)" +
	 		       " VALUES (#addresseeId#, #notificationId#, #recipientId#)", objAddressee);
	  }
  
  public  Collection retrieveAddressee(String id)throws DaoException{
		
	  String sql="SELECT addresseeId, notificationId, recipientId FROM ast_assetAddressee WHERE notificationId=?";
	  
	  Collection result = super.select(sql, DataAddressee.class, new Object[]{id}, 0, -1); 
		   
		if(result.size()>0){	    		
		  	return result;	  
		}
		return null;			  
  }	

//------------------------------ ast_financialSetup ------------------------------

  public void insertFinancialSetup (DataFinancialSetup objFinancialSetup) throws DaoException {	
	  	super.update("INSERT INTO ast_financialSetup (financialId, financialMonth, currencySymbol, currencyPattern)" +
	 		       " VALUES (#financialId#, #financialMonth#, #currencySymbol#, #currencyPattern#)", objFinancialSetup);
	  }
  
  public void updateFinancialSetup(DataFinancialSetup objFinancialSetup)throws DaoException {
	
	  super.update("UPDATE ast_financialSetup SET financialMonth=#financialMonth#, currencySymbol= #currencySymbol#," +
	  			   " currencyPattern= #currencyPattern# WHERE financialId=#financialId# ", objFinancialSetup);
  }
    
  public int iGetSizeFinancialSetup() throws DaoException{
	  String sql = "SELECT COUNT(financialId) AS total FROM ast_financialSetup";
	  Collection result = super.select(sql, HashMap.class, null, 0, 1);
	  if (result != null && result.size() > 0 ){
		  Map mapResult = (Map)result.iterator().next();
		  if(mapResult.get("total") != null)
			  return Integer.parseInt(mapResult.get("total").toString());	
	  }		  
	 return 0;
  }
  
  public  Collection retrieveFinancialSetup() throws  DaoException{
	 Collection result = super.select("SELECT financialId, financialMonth, currencySymbol, currencyPattern FROM ast_financialSetup",DataFinancialSetup.class, null, 0, 1 );
	 if (result.size() > 0)
		 return result;
	 return null;
  }

  //----------------------- ast_saveMonthlyDepReport ------------------------------------

  public void insertSaveMonthlyDep (DataSaveMonthDepReport objSave)throws DaoException {
	  
	  super.update("INSERT INTO ast_saveMonthlyDepReport (" +
			  "id, saveMonthlyId, currencySymbol, currencyPattern, categoryName, depreciation, financialYear, " +
			  "itemName, assetUnit, assetDate, assetYear, endingMonth, totalCost, " +
			  "jan, feb, mar, apr, may, jun, jul, aug, sept, oct, nov, dece, totalDepreciation, dateCreated) " +
			  "VALUES " +
			  "(#id#,#saveMonthlyId#,#currencySymbol#, #currencyPattern#, #categoryName#, #depreciation#, #financialYear#, #itemName#, #assetUnit#,  #assetDate#, #assetYear#, #endingMonth#, #totalCost#, " +
			  "#jan#, #feb#, #mar#, #apr#, #may#, #jun#, #jul#, #aug#, #sept#, #oct#, #nov#, #dece#, " +
			  "#totalDepreciation#, now())", objSave);
  }
	
  public void deleteSaveMonthlyDep(String strSaveId)throws DaoException{
		  super.update("DELETE FROM ast_saveMonthlyDepReport WHERE saveMonthlyId=?", new Object[]{(String)strSaveId});

	 }
  
	
  public Collection listingSaveMonthlyDep(Date frmDate, Date toDate, String sort,boolean desc,int start,int rows) throws DaoException {
	  
	  Object args = null;
	  String sql = "SELECT DISTINCT(saveMonthlyId), financialYear, dateCreated FROM ast_saveMonthlyDepReport ";
      String orderBy = (sort != null) ? sort : "dateCreated";
      if (desc)
          orderBy += " DESC";
      if(frmDate != null && toDate != null){
    	  
            Calendar calto = Calendar.getInstance();
        		calto.setTime(toDate);
                calto.set(Calendar.HOUR_OF_DAY, 23);
                calto.set(Calendar.MINUTE, 59);
                calto.set(Calendar.SECOND, 59);
    	 
    	  sql += "WHERE dateCreated>=? AND dateCreated<=?";
    	  args = new Object[]{frmDate, calto.getTime()};
      }

      return super.select( sql + " ORDER BY " + orderBy, DataSaveMonthDepReport.class, args, start, rows);
 }
  
  
  public Date getCreatedDateSaveMonthlyDep(String saveid) throws DaoException {
	  Collection result = super.select("SELECT dateCreated FROM ast_saveMonthlyDepReport WHERE saveMonthlyId =?" , HashMap.class, new Object[]{(String) saveid}, 0, 1);
	  
	   Map resultMap = new HashMap();    
	  if (result.size() > 0){
		  resultMap = (Map)result.iterator().next();
		  return ((Date)resultMap.get("dateCreated"));	 
	  }
          return null;
 }
  
  public  Collection retrieveSaveMonthlyDep(String strSaveId)throws DaoException{
  	
	  String sql="SELECT id, saveMonthlyId, currencySymbol, currencyPattern, categoryName, depreciation, financialYear, " +
			  "itemName, assetUnit, assetDate, assetYear, endingMonth, totalCost, " +
			  "jan, feb, mar, apr, may, jun, jul, aug, sept, oct, nov, dece, totalDepreciation, dateCreated" +
			  " FROM ast_saveMonthlyDepReport WHERE saveMonthlyId=? ORDER BY categoryName ASC";
	  
	  Collection result = super.select(sql, DataSaveMonthDepReport.class, new Object[]{(String) strSaveId}, 0, -1); 
		   
		if(result.size()>0){	    		
		  	return result;	  
		}
		return null;			  
  }	
  
  public double getMonthlySumOfColumn(int iYear, String categoryName, String columnField, String saveId )throws DaoException{
	  
	  String sql = "SELECT SUM(" + columnField + ") AS sumTotal FROM ast_saveMonthlyDepReport "+
		           "WHERE assetYear=? AND categoryName=? AND saveMonthlyId=?";

    Collection result = super.select(sql, HashMap.class, new Object[]{iYear, categoryName, saveId }, 0, 1);
    
    Map resultMap = new HashMap();	    
	if(result.size()>0){	    		
	    resultMap = (Map)result.iterator().next();
	    if (resultMap.get("sumTotal") != null)
	    	return ((Number)resultMap.get("sumTotal")).doubleValue();	  
	} 		
	 	return 0;
 }

  public int countSaveMonthlyDep(int iYear, String categoryName, String saveId)throws DaoException{
		  
	  String sql = "SELECT COUNT(id) AS totalItem FROM ast_saveMonthlyDepReport " +
	  		       "WHERE assetYear=? AND categoryName=? AND saveMonthlyId=?";
	  
	  Collection count = super.select(sql, HashMap.class, new Object[]{iYear, categoryName, saveId}, 0, 1);
	  
	  if(count.size() > 0 ){
		  Map mapResult = (Map)count.iterator().next();
		  if(mapResult.get("totalItem") != null)
			  return Integer.parseInt(mapResult.get("totalItem").toString());		
	  }		  
  return 0;	
 }
 
  //----------------------------------- ast_saveFixedAssetReport ------------------------------------------
  
  public void insertSaveFixedAsset(DataSaveFixedAssetReport objFixedAsset)throws DaoException {
	
	  super.update("INSERT INTO ast_saveFixedAssetReport (" +
			  "id, saveAssetId, currencySymbol, currencyPattern, categoryName, depreciation, financialYear, itemName, assetUnit, assetDate, assetYear, endingMonth, costBalBf, " +
			  "costAdditon, costDisposal, costBalCf, accumDepnBalBf, accumDepnDCharge, accumDepnDisposal, " +
			  "accumDepnBalCf, nbv , dateCreated) " +
			  "VALUES " + 
			  "(#id#,#saveAssetId#, #currencySymbol#, #currencyPattern#, #categoryName#, #depreciation#, #financialYear#, #itemName#, #assetUnit#,  #assetDate#, #assetYear#, #endingMonth#, #costBalBf#, " +
			  "#costAdditon#, #costDisposal#, #costBalCf#, #accumDepnBalBf#, #accumDepnDCharge#, #accumDepnDisposal#, " +
			  "#accumDepnBalCf#, #nbv#, now())", objFixedAsset);
  }	  
	
  public Collection listingSaveFixedAsset(Date frmDate, Date toDate, String sort,boolean desc,int start,int rows) throws DaoException {
	 
	  Object args = null;
	  String sql ="SELECT DISTINCT(saveAssetId),financialYear, dateCreated FROM ast_saveFixedAssetReport ";
      String orderBy = (sort != null) ? sort : "dateCreated";
      if (desc)
          orderBy += " DESC";
      
      if(frmDate != null && toDate != null){
    	  
          Calendar calto = Calendar.getInstance();
      		calto.setTime(toDate);
              calto.set(Calendar.HOUR_OF_DAY, 23);
              calto.set(Calendar.MINUTE, 59);
              calto.set(Calendar.SECOND, 59);
  	 
  	  sql += "WHERE dateCreated>=? AND dateCreated<=?";
  	  args = new Object[]{frmDate, calto.getTime()};
    }

      return super.select( sql + " ORDER BY " + orderBy, DataSaveFixedAssetReport.class, args, start, rows);
 }
  
  public void deleteSaveFixedAsset(String strSaveId)throws DaoException{

	  super.update("DELETE FROM ast_saveFixedAssetReport WHERE saveAssetId=?", new Object[]{(String)strSaveId});
  }
  
  public  Collection retrieveSaveFixedAsset(String strSaveId)throws DaoException{
	  	
	  String sql="SELECT id, saveAssetId, currencySymbol, currencyPattern, categoryName, depreciation, " +
	  		  "financialYear, itemName, assetUnit, assetDate, assetYear, endingMonth, costBalBf, " +
			  "costAdditon, costDisposal, costBalCf, accumDepnBalBf, accumDepnDCharge, accumDepnDisposal, " +
			  "accumDepnBalCf, nbv , dateCreated" +
			  " FROM ast_saveFixedAssetReport WHERE saveAssetId=? ORDER BY categoryName ASC";
	  
	  Collection result = super.select(sql, DataSaveFixedAssetReport.class, new Object[]{(String) strSaveId}, 0, -1); 
		   
		if(result.size()>0){	    		
		  	return result;	  
		}
		return null;			  
  }
  
  public double getFixedSumOfColumn(int iYear, String categoryName, String columnField, String saveId)throws DaoException{
	  
	  String sql = "SELECT SUM(" + columnField + ") AS sumTotal FROM ast_saveFixedAssetReport "+
		           "WHERE assetYear=? AND categoryName=? AND saveAssetId=?";

    Collection result = super.select(sql, HashMap.class, new Object[]{iYear, categoryName,saveId }, 0, 1);
    
    Map resultMap = new HashMap();	    
	if(result.size()>0){	    		
	    resultMap = (Map)result.iterator().next();
	    if (resultMap.get("sumTotal") != null)
	    	return ((Number)resultMap.get("sumTotal")).doubleValue();	  
	} 		
	 	return 0;
 }
  
  public int countSaveFixedAsset(int iYear, String categoryName,  String saveId)throws DaoException{
	  
	  String sql = "SELECT COUNT(id) AS totalItem FROM ast_saveFixedAssetReport " +
	  		       "WHERE assetYear=? AND categoryName=? AND saveAssetId=?";
	  
	  Collection count = super.select(sql, HashMap.class, new Object[]{iYear,categoryName, saveId}, 0, 1);
	  
	  if(count.size() > 0 ){
		  Map mapResult = (Map)count.iterator().next();
		  if(mapResult.get("totalItem") != null)
			  return Integer.parseInt(mapResult.get("totalItem").toString());		
	  }		  
  return 0;	
 }
  
  public Date getCreatedDateSaveFixedAsset(String saveid) throws DaoException {
	  Collection result = super.select("SELECT dateCreated FROM ast_saveFixedAssetReport WHERE saveAssetId =?" , HashMap.class, new Object[]{(String) saveid}, 0, 1);
	  
	   Map resultMap = new HashMap();    
	  if (result.size() > 0){
		  resultMap = (Map)result.iterator().next();
		  return ((Date)resultMap.get("dateCreated"));	 
	  }
          return null;
 }

}

