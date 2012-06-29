package com.tms.sam.po.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

import com.tms.sam.po.setting.model.ItemObject;

public class SupplierDao extends DataSourceDao {
	public void init() throws DaoException {
		try{
		    super.update("CREATE TABLE po_supplier (" +
		        		"supplierID VARCHAR(100) NOT NULL, " +
		        		"id VARCHAR(255)," +
		        		"lastKnownSuppName VARCHAR(225) NOT NULL, " +
		        		"lastKnownCompany VARCHAR(255),"+
		        		"lastKnownTelephone VARCHAR(255)," +
		        		"ppID VARCHAR(100), " +
		        		"dateSent DATETIME, " +
		        		"dateReceived DATETIME, " +
		        		"dateDelivery DATETIME, " +
		        		"totalQuotation DOUBLE, " +
		        		"approved VARCHAR(10), " +
		        		"responded VARCHAR(10), " +
		        		"recommended VARCHAR(10), " +
		    		    "counting INT, " +
		    		    "currencyUsed VARCHAR(100), " +
		        		"lastUpdateBy VARCHAR(225), " +
		        		"lastUpdateDate DATETIME, " +
		        		"PRIMARY KEY(supplierID))", null);		  
		 }catch(DaoException e){}
		 
		try{
		    super.update("CREATE INDEX purchaseIndex ON po_supplier(ppID)",null);
		   }catch(DaoException e){}
		   
		try{
			super.update("CREATE TABLE po_quotation (" +
			    		 "quotationID VARCHAR(100) NOT NULL, " +
			        	 "supplierID VARCHAR(100) NOT NULL, " +
			        	 "ppID VARCHAR(100), " +
			        	 "counting INT, " +
			        	 "quotationDetails VARCHAR(225), " +
			        	 "lastUpdateBy VARCHAR(225), " +
			        	 "lastUpdateDate DATETIME, " +
			        	 "PRIMARY KEY(quotationID))", null);		  
		   }catch(DaoException e){}
			 
		try{
			super.update("CREATE INDEX supplierIndex ON po_quotation(supplierID)",null);
		   }catch(DaoException e){}
		
		try {
			 super.update("CREATE TABLE po_supplier_item (" +
			       		  "supplierID VARCHAR(100) NOT NULL, " +
					 	  "counting INT, " +
					 	  "ppID VARCHAR(100) NOT NULL, " +
			       		  "itemCode VARCHAR(100)," +
			       		  "unitPrice DOUBLE)", null);
			} catch (DaoException e) {}
			
		try{
			super.update("CREATE INDEX itemID_index ON po_supplier_item(itemID)",null);
		   }catch(DaoException e){}
		   
		try{
			super.update("CREATE INDEX supplierID_index ON po_supplier_item(supplierID)",null);
		   }catch(DaoException e){}
		   
			try {
				super.update("CREATE TABLE po_supplier_attachment ("
							+ "attachmentID VARCHAR(100) NOT NULL, "
							+ "supplierID VARCHAR(100) NOT NULL, "
							+ "ppID VARCHAR(100) NOT NULL, "
							+ "counting INT, " 
							+ "newFileName VARCHAR(225), "
							+ "path VARCHAR(225) , " 
							+ "dateCreated DATETIME, "
							+ "PRIMARY KEY(attachmentID))", null);
			} catch (DaoException e) {}
		   
		try{
		   super.update("CREATE INDEX ppIndex_index ON pp_supplier_attachment(ppID)",null);
		   }catch(DaoException e){}
		
		try{
			    super.update("CREATE TABLE po_supplier_rating (" +
			    		     "ratingID VARCHAR(100) NOT NULL, " +
			        	  	 "id VARCHAR(100) NOT NULL, " +
			        	  	 "qualitySystem VARCHAR(10)," +
			        		 "concern VARCHAR(10), " +
			        		 "history  VARCHAR(10), " +
			        		 "actual  VARCHAR(10), " +
			        		 "negotiation VARCHAR(10), " +
			        		 "technical VARCHAR(10), " +
			        		 "delivery VARCHAR(10), " +
			        		 "assistance VARCHAR(10), " +
			        		 "lastUpdateBy VARCHAR(225), " +
			        		 "lastUpdateDate DATETIME, " +
			        		 "PRIMARY KEY(ratingID))", null);		  
			 }catch(DaoException e){}
	 }
	
	 public Collection getSupplier(String ppID, int index, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
			
			String orderBy = (sort != null) ? sort : "supplierID";
			if (desc)
	            orderBy += " DESC";
			
			Object[] args = {ppID,index};
			

	        int totalRating =0;
			Collection request = null;
			request = super.select("SELECT distinct spp.supplierID, spp.id, " +
					  "spp.lastKnownSuppName, spp.lastKnownCompany,sppItem.counting, " +
					  " spp.ppID, spp.dateSent, spp.dateReceived,  " +
					  " spp.totalQuotation, spp.responded, spp.recommended, " + 
					  "spp.lastUpdateBy, spp.lastUpdateDate " +
					  "FROM po_supplier spp, po_supplier_item sppItem" +
					  " WHERE sppItem.ppID=?" +
					  "AND sppItem.counting=? AND spp.ppID=sppItem.ppID " +
					  "AND sppItem.supplierID = spp.supplierID " 
		              , SupplierObject.class,args, start, rows);
			
			Collection rating = null;
			
			for(Iterator itr=request.iterator();itr.hasNext();){
				SupplierObject obj = (SupplierObject)itr.next();
				rating = super.select("SELECT qualitySystem, concern, " +
				         "history, actual, negotiation, technical, delivery, " +
				         "assistance  " +
						 "FROM po_supplier_rating " +
						 " WHERE id=?" 
			             , RatingObject.class,new Object[]{obj.getId()}, 0, 1);
				for(Iterator i=rating.iterator();i.hasNext();){
					RatingObject rate = (RatingObject)i.next();
					int quality = Integer.parseInt(rate.getQualitySystem());
					int concern = Integer.parseInt(rate.getConcern());
					int history = Integer.parseInt(rate.getHistory());
					int actual = Integer.parseInt(rate.getActual());
					int negotiation = Integer.parseInt(rate.getNegotiation());
					int technical = Integer.parseInt(rate.getTechnical());
					int delivery = Integer.parseInt(rate.getDelivery());
					int assistance = Integer.parseInt(rate.getAssistance());
					totalRating = (quality + concern + history + actual + negotiation + technical + delivery + assistance)/8;
					obj.setTotalRating(totalRating);
				}
			}
				
			return request;
	  }
	 
	  public int countListing(String ppID) throws DaoException, DataObjectNotFoundException{
			
			int total = 0;	
			
			Object[] args = {ppID};
			Collection countrequest = null;
			
	        
	        countrequest = super.select("SELECT COUNT(*) AS total " + 
					       "FROM po_supplier " +
					       "WHERE ppID=? "
		                   , HashMap.class,args, 0, 1);
	        
	        if(countrequest != null) {
	        	for(Iterator i= countrequest.iterator(); i.hasNext(); ) {
	        		HashMap map = (HashMap) i.next();
	        		total = Integer.parseInt(map.get("total").toString());
	        	}
	        }
	        
	        return total;
	 }
	  
	  public void addSupplier(SupplierObject pr) throws DaoException {
		  
		Collection request = null;
		  
		request = super.select("SELECT supplierID, counting " +
		  		  "FROM po_supplier " +
				  "WHERE ppID=? ORDER BY counting DESC"
		  		  , SupplierObject.class,new Object[]{pr.getPpID()}, 0,1);
		if(pr.getCounting()== 0){
			if(request.size()!=0){
				for(Iterator i = request.iterator();i.hasNext();)
				{
					SupplierObject obj = (SupplierObject)i.next();
					pr.setCounting(obj.getCounting()+1);
				}
			}else{
				pr.setCounting(1);
			}
		}
			super.update("INSERT INTO po_supplier ("+ 
			         "counting, supplierID, id, lastKnownSuppName, " +
			         "lastKnownCompany, lastKnownTelephone, ppID, " +
			         "dateSent, dateReceived, dateDelivery, totalQuotation, approved, " +
			         "responded, recommended, lastUpdateBy, lastUpdateDate) VALUES " + 
			         "(#counting#, #supplierID#, #id#, #lastKnownSuppName#, #lastKnownCompany#," +
			         "#lastKnownTelephone#, #ppID#, #dateSent#, " +
			         "#dateReceived#, #dateDelivery#, #totalQuotation#, #approved#, #responded#, " +
			         "#recommended#, #lastUpdateBy#, now())",pr);
	
		   super.update("INSERT INTO po_quotation ("+ 
			         "quotationID, supplierID, quotationDetails, ppID, counting, " +
			         "lastUpdateBy, lastUpdateDate) VALUES " + 
			         "(#quotationID#, #supplierID#, #quotationDetails#, #ppID#, #counting#, " +
			         "#lastUpdateBy#, now())",pr);
		   
		   for(Iterator i=pr.getItemID().iterator();i.hasNext();){
			   String id = (String) i.next();
			   pr.setItem(id);
			   super.update("INSERT INTO po_supplier_item ("+ 
				         "counting, supplierID, itemCode, ppID ) VALUES " + 
				         "(#counting#,#supplierID#, #item#, #ppID# )",pr);
		   }
	 }
	
	
     public void insertAttachment(AttachmentObject obj) throws DaoException {
    	String fileName = "";
    	fileName = obj.getNewFileName();
    	Collection file =  null;
	
	
		file = super.select("SELECT attachmentID "+ 
			   "FROM po_supplier_attachment " +
			   "WHERE newFileName=?",
		       HashMap.class,new Object[] {fileName}, 0, 1);
		
		if(file.size()== 0){
			super.update("INSERT INTO po_supplier_attachment ("+ 
				         "attachmentID, supplierID, ppID, counting, " +
				         "newFileName, path, dateCreated) VALUES " + 
				         "(#attachmentID#, #supplierID#, #ppID#, #counting#, " +
				         "#newFileName#, #path#, now())",obj);
		}
	 }
    
    
	 public void recommendSupplier(SupplierObject obj) throws DaoException, DataObjectNotFoundException{
		 
			super.update("UPDATE po_supplier SET recommended=#recommended# WHERE supplierID=#supplierID#", obj);
	 }
	 
	 public void responded(SupplierObject obj) throws DaoException, DataObjectNotFoundException{
		   
			super.update("UPDATE po_supplier SET responded=#responded# WHERE supplierID=#supplierID#", obj);
	 }
	 
	 public void deleteSupplier(String ppID, String id, int index) throws DaoException, DataObjectNotFoundException{
			
		    super.update("DELETE FROM po_supplier WHERE supplierID=? AND ppID=? AND counting=?"
		    		, new Object[]{ id, ppID, index});
		    super.update("DELETE FROM po_supplier_attachment WHERE supplierID=? AND ppID=? AND counting=?"
		    		, new Object[]{ id, ppID, index});
		    super.update("DELETE FROM po_quotation WHERE supplierID=? AND ppID=? AND counting=?"
		    		, new Object[]{ id, ppID, index});
		    super.update("DELETE FROM po_supplier_item WHERE supplierID=? AND ppID=? AND counting=?"
		    		, new Object[]{ id, ppID, index});
			SupplierObject obj = new SupplierObject();
			obj.setPpID(ppID);
			obj.setSupplierID(id);
			obj.setCounting(index);
		    if(index==1){
		    	
		    	super.update("UPDATE po_supplier SET counting=counting-1 " +
				         "WHERE ppID=#ppID# AND counting<>#counting#", obj);
		    	super.update("UPDATE po_supplier_attachment SET counting=counting-1 " +
		    			  "WHERE ppID=#ppID# AND counting<>#counting#", obj);
		    	super.update("UPDATE po_quotation SET counting=counting-1 " +
		    			  "WHERE ppID=#ppID# AND counting<>#counting#", obj);
		    	super.update("UPDATE po_supplier_item SET counting=counting-1 " +
		    			  "WHERE ppID=#ppID# AND counting<>#counting#", obj);
		    	
		    }else if(index>1){
		    	
		    	super.update("UPDATE po_supplier SET counting=counting-1 " +
				         "WHERE ppID=#ppID# AND counting<>'1'", obj);
		    	super.update("UPDATE po_supplier_attachment SET counting=counting-1 " +
		    			  "WHERE ppID=#ppID# AND counting<>'1'", obj);
		    	super.update("UPDATE po_quotation SET counting=counting-1 " +
		    			  "WHERE ppID=#ppID# AND counting<>'1'", obj);
		    	super.update("UPDATE po_supplier_item SET counting=counting-1 " +
		    			  "WHERE ppID=#ppID# AND counting<>'1'", obj);
		    }
	 }
	 
	 public SupplierObject singleRequest(String supplierID, int count, String ppID) throws DaoException, DataObjectNotFoundException{
			Object[] suppID = new Object[] {
					supplierID, ppID, count
			}; 
			
			Collection oneRequest =  null;
			SupplierObject po = null;
		   
		    oneRequest = super.select("SELECT supplierID, id, lastKnownSuppName, " +
		    		     "lastKnownCompany, lastKnownTelephone, ppID, dateDelivery, " +
					     "dateSent, dateReceived, totalQuotation, responded, currencyUsed, " +
					     "recommended, lastUpdateBy, lastUpdateDate " +
					     "FROM po_supplier supp, po_currency cu " +
					     "WHERE supplierID=? AND ppID=? AND counting=?",
					     SupplierObject.class,suppID, 0, 1);
		 
		    if (oneRequest.size() == 0) {
	            throw new DataObjectNotFoundException();
			}
			else {
				Collection quotation = null;
				Collection items = null;
				for(Iterator i=oneRequest.iterator(); i.hasNext();){
					po = (SupplierObject)i.next();
					quotation = super.select("SELECT quotationDetails " + 
							"FROM po_quotation " +
							"WHERE supplierID=? AND ppID=? AND counting=?",
							HashMap.class,suppID, 0, 1);
					for(Iterator itr= quotation.iterator(); itr.hasNext(); ) {
		        		HashMap map = (HashMap) itr.next();
		        		po.setQuotationDetails((String) map.get("quotationDetails"));
		        	}
					
					items = super.select("SELECT unitPrice,itemCode " + 
							"FROM po_supplier_item " +
							"WHERE supplierID=? AND ppID=? AND counting=?",
							ItemObject.class,suppID, 0, -1);
					po.setItemID(items);
					
					quotation = super.select("SELECT currency " + 
							"FROM po_currency " +
							"WHERE currencyID=?",
							HashMap.class,new Object[]{po.getCurrencyUsed()}, 0, 1);
					for(Iterator itr= quotation.iterator(); itr.hasNext(); ) {
		        		HashMap map = (HashMap) itr.next();
		        		po.setCurrency((String) map.get("currency"));
		        	}
					
				}
				
			}
	  	     return po;
	 }
	 
	 public Collection getAttachment(String supplierID, int index, String ppID) throws DaoException, DataObjectNotFoundException{
			Object[] suppID = new Object[] {
					supplierID, index, ppID
			}; 
			Collection attachment = null;
			attachment = super.select("SELECT attachmentID, newFileName " +
					     "FROM po_supplier_attachment " + 
					     "WHERE supplierID=? AND counting=? AND ppID=?", 
					     HashMap.class,suppID, 0, -1);
			return attachment;
	 }
	 
	 public String getPurchaseID(String supplierID)throws DaoException, DataObjectNotFoundException{
			Object[] suppID = new Object[] {
					supplierID
			}; 
			
			Collection purchaseID =  null;
			String pID="";
		
			purchaseID = super.select("SELECT ppID "+ 
					"FROM po_supplier " +
					"WHERE supplierID=?",
			        HashMap.class,suppID, 0, 1);
		    if (purchaseID.size() != 0) {
	          		
				for(Iterator i=purchaseID.iterator(); i.hasNext();){
					HashMap map = (HashMap) i.next();
					pID = (String) map.get("ppID");
				}
			}
		    return pID;
	 }
	
     public void updateSupplierInfo(SupplierObject obj) throws DaoException, DataObjectNotFoundException{
    	Collection attachment = new ArrayList();
    	Collection items = new ArrayList();
    	items = obj.getItemID();
		attachment = obj.getAttachmentObject();
		
		super.update("UPDATE po_supplier SET dateReceived=#dateReceived#, " +
			         "totalQuotation=#totalQuotation#, dateDelivery=#dateDelivery#," +
			         "dateSent=#dateSent#, responded=#responded#, recommended=#recommended#, " +
			         "lastUpdateBy=#lastUpdateBy#, lastUpdateDate=#lastUpdateDate#, currencyUsed=#currencyUsed# " +
			         "WHERE supplierID=#supplierID# AND ppID=#ppID# AND counting=#counting#", obj);
		
		super.update("UPDATE po_quotation SET quotationDetails=#quotationDetails#, " +
		         "lastUpdateBy=#lastUpdateBy#, lastUpdateDate=#lastUpdateDate# " +
		         "WHERE supplierID=#supplierID# AND ppID=#ppID# AND counting=#counting#", obj);
		
		for (Iterator i = attachment.iterator(); i.hasNext();) {
			AttachmentObject object = new AttachmentObject();
			object = (AttachmentObject) i.next();
			// System.out.println("AA"+object.getPath());
			insertAttachment(object);
		}
		
		for (Iterator i = items.iterator(); i.hasNext();) {
			ItemObject object = new ItemObject();
			object = (ItemObject) i.next();
			// System.out.println("AA"+object.getPath());
			super.update("UPDATE po_supplier_item SET unitPrice=#unitPrice# " +
			         "WHERE supplierID=#supplierID# AND ppID=#ppID# AND counting=#counting# and itemCode=#itemCode#", object);
		}
	 }
    
     public void saveRating(RatingObject obj) throws DaoException, DataObjectNotFoundException{
    	String id = obj.getId();
    	Collection rating =  null;
	
		rating = super.select("SELECT id "+ 
				"FROM po_supplier_rating " +
				"WHERE id=?",
		        HashMap.class,new Object[] {id}, 0, 1);
		
		if(rating.size()== 0){
			super.update("INSERT INTO po_supplier_rating ("+ 
				         "ratingID, id, qualitySystem, concern, " +
				         "history, actual, negotiation, technical, delivery, " +
				         "assistance, lastUpdateBy, lastUpdateDate) VALUES " + 
				         "(#ratingID#, #id#, #qualitySystem#, #concern#, " +
				         "#history#, #actual#, #negotiation#, #technical#, #delivery#, " +
				         "#assistance#, #lastUpdateBy#, now())",obj);
		}else{
			super.update("UPDATE po_supplier_rating " +
			            "SET ratingID=#ratingID#, " +
			            "qualitySystem=#qualitySystem#, concern=#concern#, " +
			            "history=#history#, actual=#actual#, negotiation=#negotiation#, " +
			            "technical=#technical#, delivery=#delivery#, " +
			            "assistance=#assistance#, lastUpdateBy=#lastUpdateBy#, lastUpdateDate=#lastUpdateDate# " +
			            "WHERE id=#id#",obj);
			        
		//	super.update("UPDATE po_supplier_rating " +
		//			     "SET dateReceived=#dateReceived#, minBudget=#minBudget# " +
		//			     "WHERE supplierID=#supplierID#", obj);
		}
     }
    
    
	 public RatingObject getRating(String supplierID) throws DaoException, DataObjectNotFoundException{
			Object[] suppID = new Object[] {
					supplierID
			}; 
			
			Collection rating =  null;
			RatingObject ro = null;
		   
			rating = super.select("SELECT ratingID, id, " +
					"qualitySystem, concern, history, " +
					"actual, negotiation, technical, " +
					"delivery, assistance, lastUpdateBy, lastUpdateDate " +
					"FROM po_supplier_rating " +
					"WHERE id=?",
					RatingObject.class,suppID, 0, 1);
		 
		    if (rating.size() != 0) {
				for(Iterator i=rating.iterator(); i.hasNext();){
					ro = (RatingObject)i.next();
				}
			}
	  	     return ro;
	  }
	 
	  public Collection viewQuatation(String ppID, int index, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
			
			
			
			String orderBy = (sort != null) ? sort : "supplierID";
			if (desc)
	            orderBy += " DESC";
			
			Object[] args = {ppID, index};
			
	        
	        
			Collection request = null;
			request = super.select("SELECT supplierID, lastKnownSuppName, totalQuotation, currency AS currencyUsed," +
					  "lastKnownCompany, " +
					  "ppID, dateSent, dateReceived, " +
					  "totalQuotation, approved, responded, recommended " +
					  "FROM po_supplier supp, po_currency cu " +
					  "WHERE ppID=? AND cu.currencyID = supp.currencyUsed AND counting=? " +
		              "ORDER BY " + orderBy, SupplierObject.class,args, start, rows);
				
			return request;
	  }
	 
	  public int countSupplier(String ppID, String name, String searchCol) throws DaoException, DataObjectNotFoundException{
			String condition = (name != null) ? "%" + name + "%" : "%";
			int total = 0;	
			
			Object[] args = {ppID, condition };
			Collection countrequest = null;
			String columnName = "lastKnownSuppName";
			
			if(! "".equals(searchCol)) {
				if("totalQuotation".equals(searchCol)) {
					columnName = "totalQuotation";
				}
				else if("recommended".equals(searchCol)) {
					columnName = "recommended";
				}
			}
	        
	        countrequest = super.select("SELECT COUNT(*) AS total " + 
					       "FROM po_supplier " +
					       "WHERE ppID=? AND " +
		                   columnName + " LIKE ? "
		                   , HashMap.class,args, 0, 1);
	        
	        if(countrequest != null) {
	        	for(Iterator i= countrequest.iterator(); i.hasNext(); ) {
	        		HashMap map = (HashMap) i.next();
	        		total = Integer.parseInt(map.get("total").toString());
	        	}
	        }
	        
	        return total;
		}
	 
	 public void approvedSupplier(SupplierObject obj) throws DaoException, DataObjectNotFoundException{
		 super.update("UPDATE po_supplier SET approved=#approved# " +
		 		"WHERE supplierID=#supplierID# " +
		 		"AND counting=#counting# " +
		 		"AND ppID=#ppID#", obj);
	 }
	 
	 public void disapprove(SupplierObject obj) throws DaoException, DataObjectNotFoundException{
		 super.update("UPDATE po_supplier SET approved=#approved# " +
		 		"WHERE ppID=#ppID# " +
		 		"AND counting=#counting#", obj);
	 }
	 
	 public Collection getSupplierID(String ppID, String approved)throws DaoException, DataObjectNotFoundException{
		    Object[] args = {ppID};
			Collection sID =  null;
		
			sID = super.select("SELECT supplierID, counting "+ 
					"FROM po_supplier " +
					"WHERE ppID=? ",
			        SupplierObject.class,args, 0, -1);
		   
		    return sID;
	 } 
	 
	 public Collection checkAddedSupplier(String ppID) throws DaoException ,DataObjectNotFoundException{
			
			Object[] args = {ppID};
			
			Collection request = null;
			request = super.select("SELECT supplierID, " +
					  "lastKnownSuppName, lastKnownCompany, " +
					  "ppID, dateSent, dateReceived, " +
					  "totalQuotation, approved, responded, recommended, " +
					  "lastUpdateBy, lastUpdateDate " + 
					  "FROM po_supplier " +
					  "WHERE ppID=? " ,
		              SupplierObject.class,args, 0,-1);
					
			return request;
	  }
	
	 public Collection checkAddedItem(String ppID) throws DaoException ,DataObjectNotFoundException{
			
			Object[] args = {ppID};
			
			Collection request = null;
			request = super.select("SELECT distinct itemCode " + 
					  "FROM po_supplier_item " +
					  "WHERE ppID=? " ,
		              HashMap.class,args, 0,-1);
					
			return request;
	  }
	 
	  public boolean getApprovedSupplier(String ppID) throws DaoException ,DataObjectNotFoundException{
		
		Object[] args = {ppID};
		
		Collection request = null;
		Collection countTable = super.select("SELECT counting " + 
				  "FROM po_supplier " +
				  "WHERE ppID=? ORDER BY counting desc" ,
	              SupplierObject.class,args, 0,1);
		int count = 0;
		request = super.select("SELECT supplierID, lastKnownSuppName, lastKnownCompany, " +
				  "ppID, dateSent, dateReceived, " +
				  "totalQuotation, approved, responded, " +
				  "recommended, lastUpdateBy, lastUpdateDate " + 
				  "FROM po_supplier " +
				  "WHERE ppID=?  AND approved in ('Yes')" ,
	              SupplierObject.class,args, 0,-1);
	
		for(Iterator i=countTable.iterator();i.hasNext();){
			SupplierObject obj = (SupplierObject)i.next();
			count = obj.getCounting();
		}
		if(request.size()==count){
			return true;
		}else{
			return false;
		}
		
     }
	  
	 public AttachmentObject downloadFile(String attachmentID)throws DaoException, DataObjectNotFoundException{
			Object[] purchaseID = new Object[] {
					attachmentID
			}; 
			AttachmentObject obj = null;
			Collection attachment = null;
			attachment = super.select("SELECT attachmentID, ppID, " +
					     "newFileName, path, dateCreated " +
					     "FROM po_supplier_attachment " + 
					     "WHERE attachmentID=?", 
					     AttachmentObject.class,purchaseID, 0, 1);
			
			for(Iterator i=attachment.iterator(); i.hasNext();){
				obj = (AttachmentObject)i.next();
			}
				
				
			return obj;
	}
	  
	public Collection getSupplierItem(String ppID) throws DaoException, DataObjectNotFoundException{
			Object[] id = new Object[] {
					ppID
			}; 
			Collection attachment = null;
			attachment = super.select("SELECT supp.supplierID, item.itemID " +
					     "FROM po_supplier supp, po_supplier_item item " + 
					     "WHERE ppID=?", 
					     HashMap.class,id, 0, -1);
			return attachment;
	}
	
	public String countTable(String ppID) throws DaoException, DataObjectNotFoundException{
		Object[] id = new Object[] {
				ppID
		}; 
		
		Collection count = null;
		count = super.select("SELECT counting " +
			     "FROM po_supplier " + 
			     "WHERE ppID=? ORDER BY counting DESC", 
			     HashMap.class,id, 0,1);
	
		String no="";
		for(Iterator i=count.iterator();i.hasNext();){
			HashMap map = (HashMap)i.next();
			no = map.get("counting").toString();
		}
		return no;
	}
	
	public Collection getSupplierItems(String supplierID, String ppID, int count) throws DaoException, DataObjectNotFoundException{
		Collection items= null;
		items = super.select("SELECT i.itemDesc, i.unitOfMeasure, i.itemCode, item.unitPrice,ppi.qty AS minQty, " +
						"item.supplierID " + 
						"FROM po_supplier_item item, po_item i, po_pre_purchase_item ppi " +
						"WHERE item.supplierID=? AND item.ppID=? AND item.counting=? " +
						"AND ppi.ppID=item.ppID " +
						"AND i.itemCode = item.itemCode " +
						"AND i.itemCode = ppi.itemCode",
				        ItemObject.class,new Object[]{supplierID,ppID, count}, 0, -1);
		return items;
	}
	
	public Collection getItems(String ppID, int index) throws DaoException, DataObjectNotFoundException{
		Collection items= null;
		items = super.select("SELECT itemCode " + 
						"FROM po_supplier_item " +
						"WHERE ppID=? AND counting=?" ,
				        HashMap.class,new Object[]{ppID,index}, 0, -1);
		
		Collection itemId = new ArrayList();
		for(Iterator i= items.iterator();i.hasNext();){
			HashMap map = (HashMap)i.next();
			String item = map.get("itemCode").toString();
			itemId.add(item);
		}
		return itemId;
	}
	
	public Collection getApprovedSupplier(String ppID, int index, String tableName, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		
		
		
		String approved = "";
		String orderBy = (sort != null) ? sort : "supplierID";
		if (desc)
            orderBy += " DESC";
		if(tableName.equals("approvedSupplier")){
			approved = "Yes";
		}else if(tableName.equals("rejectedSupplier")){
			approved = "No";
		}
		Object[] args = {ppID,index, approved};
		
       
		Collection request = null;
		request = super.select("SELECT distinct spp.supplierID, " +
				  "spp.lastKnownSuppName, spp.lastKnownCompany,  spp.lastKnownTelephone, sppItem.counting, " +
				  " spp.ppID, spp.dateSent, spp.dateReceived,  " +
				  " spp.totalQuotation, spp.responded, spp.recommended, " + 
				  "spp.lastUpdateBy, spp.lastUpdateDate, cu.currency AS currencyUsed " +
				  "FROM po_supplier spp, po_supplier_item sppItem, po_currency cu" +
				  " WHERE sppItem.ppID=? " +
				  "AND cu.currencyID = spp.currencyUsed " +
				  "AND sppItem.counting=? AND spp.ppID=sppItem.ppID " +
				  "AND sppItem.supplierID = spp.supplierID AND spp.approved=? " 
	              , SupplierObject.class,args, start, rows);
		
		return request;
	}
	
	 public int countAprrovedSupplierListing(String ppID, String name, String searchCol) throws DaoException, DataObjectNotFoundException{
			String condition = (name != null) ? "%" + name + "%" : "%";
			int total = 0;	
			
			Object[] args = {ppID, condition };
			Collection countrequest = null;
			String columnName = "lastKnownSuppName";
		        
		    if(! "".equals(searchCol)) {
		    	if("responded".equals(searchCol)) {
		                    columnName = "responded";
		        }
		        else if("recommended".equals(searchCol)) {
		                    columnName = "recommended";
		        }
		    }
	        
	        countrequest = super.select("SELECT COUNT(*) AS total " + 
					       "FROM po_supplier " +
					       "WHERE ppID=? AND approved='YES' AND " +
		                   columnName + " LIKE ? "
		                   , HashMap.class,args, 0, 1);
	        
	        if(countrequest != null) {
	        	for(Iterator i= countrequest.iterator(); i.hasNext(); ) {
	        		HashMap map = (HashMap) i.next();
	        		total = Integer.parseInt(map.get("total").toString());
	        	}
	        }
	        
	        return total;
	 }
	  
	  public boolean checkInsertedData(String ppID) throws DaoException {
		  
			Collection request = null;
			  
			request = super.select("SELECT unitPrice, responded " +
			  		  "FROM po_supplier_item i, po_supplier s " +
					  "WHERE i.ppID=? " +
					  "AND (unitPrice is NULL OR unitPrice='0' OR s.responded='No') " +
					  "AND s.ppID=i.ppID "
			  		  , SupplierObject.class,new Object[]{ppID}, 0,-1);
			
			if(request!=null){
				if(request.size()==0){
					return true;
				}else{
					return false;
				}
			}
			return false;
	}
	  
	public Collection supplierInfo(String ppID) throws DaoException, DataObjectNotFoundException{
			Object[] suppID = new Object[] {
					ppID
			}; 
			
			Collection oneRequest =  null;
			SupplierObject po = null;
			Collection items = null;
		    oneRequest = super.select("SELECT distinct supp.lastKnownCompany, " +
		    			 "supp.supplierID, supp.lastKnownSuppName, " +
		    		     "supp.lastKnownTelephone, supp.ppID, supp.dateDelivery, quotationDetails, " +
					     "supp.counting, purchaseCode, " +
					     "supp.recommended, supp.lastUpdateBy, supp.lastUpdateDate " +
					     "FROM po_supplier supp, po_quotation quote, po_pre_purchase po " +
					     "WHERE supp.ppID=? " +
					     "AND quote.ppID=supp.ppID " +
					     "AND quote.supplierID = supp.supplierID " +
					     "AND quote.counting=supp.counting " +
					     "AND po.ppID = supp.ppID",
					     SupplierObject.class,suppID, 0, -1);
		 
		    for(Iterator i=oneRequest.iterator();i.hasNext();){
		    	SupplierObject obj = (SupplierObject)i.next();
		    	items = super.select("SELECT i.itemCode, i.itemDesc, ppItem.qty, i.unitOfMeasure " +
						     "FROM po_item i, po_pre_purchase_item ppItem, po_supplier_item sItem " +
						     "WHERE sItem.ppID=? " +
						     "AND sItem.supplierID=? " +
						     "AND sItem.counting=? " +
						     "AND ppItem.itemCode=i.itemCode " +
						     "AND sItem.itemCode=ppItem.itemCode AND ppItem.ppID=sItem.ppID", 
						     HashMap.class,new Object[]{obj.getPpID(),obj.getSupplierID(),obj.getCounting()}, 0, -1);
		    	obj.setItemID(items);
		    }
		    
		return oneRequest;
	}
	
	public String getInfo(String type) throws DaoException, DataObjectNotFoundException{
		
		if(type.equals("logo")){
			Collection results = super.select("SELECT value FROM setup WHERE property='siteLogo' ", HashMap.class, null, 0, 1);
			if (results.size() == 1) {
				Iterator iterator = results.iterator();
				HashMap map = (HashMap) iterator.next();
				String logo = map.get("value").toString();
				return logo;
			}
		}else {
			Collection results = super.select("SELECT configDetailName " +
					"FROM po_config_detail " +
					"WHERE configDetailType=? ", HashMap.class, new Object[]{type}, 0, 1);
			if (results.size() == 1) {
				Iterator iterator = results.iterator();
				HashMap map = (HashMap) iterator.next();
				String name = map.get("configDetailName").toString();
				return name;
			}
		}
		
	 return null;
	
	}
	
	public Collection getRequester(String ppID) throws DaoException, DataObjectNotFoundException{
		Object[] suppID = new Object[] {
				ppID
		}; 
		
		Collection oneRequest =  null;
	    oneRequest = super.select("SELECT s.firstname, s.lastname, s.telMobile " +
				     "FROM po_pre_purchase po, security_user s " +
				     "WHERE po.ppID=? AND po.requesterUserID=s.id ",
				     HashMap.class,suppID, 0, 1);
	    
	    return oneRequest;
	}
	
	 public boolean isSupplierNameExist(String ppID, String company, String tel) throws DaoException {
			boolean isSupplierNameExist = false;
			
			Collection col = super.select("SELECT lastKnownCompany,lastKnownTelephone " +
			  		  "FROM po_supplier " +
					  "WHERE ppID=? " +
					  "AND lastKnownCompany=? " +
					  "AND lastKnownTelephone=? "
			  		  , SupplierObject.class,
			  		  new Object[]{ppID, company, tel}, 0,-1);
		
			if(col != null) {
				Iterator i = col.iterator();
				if(i.hasNext()){
					isSupplierNameExist = true;
				}
			}
			
			return isSupplierNameExist;
	 }
	 
	 public Collection getSuppliersRating(Collection ids, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
			Collection rating = null;
			int totalRating =0;
			Collection paramList = new ArrayList();
			StringBuffer sql = new StringBuffer("SELECT suppr.id AS id, firstname, company, " +
						 "qualitySystem, concern, " +
				         "history, actual, negotiation, technical, delivery, " +
				         "assistance  " +
						 "FROM po_supplier_rating suppr, dir_biz_contact dir " +
						 "WHERE suppr.id=dir.id ");
			
			
			
			String orderBy = (sort != null) ? sort : "suppr.id ";
			if(ids.size()!=0){
				sql.append("AND suppr.id IN (");
				String [] idArray = (String [])ids.toArray();
				
				for(int i=0; i<idArray.length; i++){
		    		
	    			if(i > 0){
	        			sql.append(",");
	        			
	        		}
	        		sql.append("?");
	        		
	        		paramList.add(idArray[i]);
		    	}
				
				sql.append(")");
			}
			
	    	
	    	
	    	
	    	sql.append(" ORDER BY " + orderBy);
	        if (desc) {
	        	sql.append(" DESC");
	        }
	        
	    	rating = super.select(sql.toString(), RatingObject.class,paramList.toArray(), start, rows);
	    	
				for(Iterator i=rating.iterator();i.hasNext();){
					RatingObject rate = (RatingObject)i.next();
					int quality = Integer.parseInt(rate.getQualitySystem());
					int concern = Integer.parseInt(rate.getConcern());
					int history = Integer.parseInt(rate.getHistory());
					int actual = Integer.parseInt(rate.getActual());
					int negotiation = Integer.parseInt(rate.getNegotiation());
					int technical = Integer.parseInt(rate.getTechnical());
					int delivery = Integer.parseInt(rate.getDelivery());
					int assistance = Integer.parseInt(rate.getAssistance());
					totalRating = (quality + concern + history + actual + negotiation + technical + delivery + assistance)/8;
					rate.setTotalRating(totalRating);
				}
		
		    
		    return rating;
		}
	 
	 
	
	 
	 public SupplierObject getSupplier(String id) throws DaoException, DataObjectNotFoundException{
			Object[] suppID = new Object[] {
					id
			}; 
			
			Collection oneRequest =  null;
			SupplierObject po = null;
		   
		    oneRequest = super.select("SELECT supplierID, id, lastKnownSuppName, " +
		    		     "lastKnownCompany, lastKnownTelephone, ppID, dateDelivery, " +
					     "dateSent, dateReceived, totalQuotation, responded, " +
					     "recommended, lastUpdateBy, lastUpdateDate " +
					     "FROM po_supplier " +
					     "WHERE id=?",
					     SupplierObject.class,suppID, 0, 1);
		 
		    if (oneRequest.size() == 1) {
				Iterator iterator = oneRequest.iterator();
				po = (SupplierObject) iterator.next();
			}
	  	     return po;
	 }
}
