package com.tms.sam.po.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

import com.tms.sam.po.setting.model.ItemObject;


public class PrePurchaseDao extends DataSourceDao {

	public void init() throws DaoException {
		try {
			super.update("CREATE TABLE po_pre_purchase ("
					+ "ppID VARCHAR(100) NOT NULL, "
					+ "purchaseCode VARCHAR(225) NOT NULL, "
					+ "requesterUserID VARCHAR(100) NOT NULL, "
					+ "reason TEXT, " 
					+ "neededBy DATE, "
					+ "priority VARCHAR(35), " 
					+ "status VARCHAR(35), " 
					+ "rank int, " 
					+ "dateCreated DATETIME, "
					+ "lastUpdatedDate DATETIME, "
					+ "lastUpdatedBy  VARCHAR(100), " + "PRIMARY KEY(ppID))",
					null);
		} catch (DaoException e) {}

		try {
			super.update("CREATE INDEX requester_index ON po_pre_purchase(requesterUserID)",							null);
		} catch (DaoException e) {}

		try {
			super.update("CREATE INDEX status_index ON po_pre_purchase(status)",null);
		} catch (DaoException e) {}

		try {
			super.update("CREATE TABLE po_pre_purchase_attachment ("
							+ "attachmentID VARCHAR(100) NOT NULL, "
							+ "ppID VARCHAR(100) NOT NULL, "
							+ "userID VARCHAR(100) NOT NULL, "
							+ "newFileName VARCHAR(225), "
							+ "path VARCHAR(225) , " 
							+ "dateCreated DATETIME, "
							+ "lastUpdatedDate datetime, " 
		            		+ "lastUpdatedBy varchar(255), " 
							+ "PRIMARY KEY(attachmentID))", null);
		} catch (DaoException e) {}

		try {
			super.update("CREATE INDEX purchase_index ON po_pre_purchase_attachment(ppID)",null);
		} catch (DaoException e) {}

		try {
			super.update("CREATE INDEX userid_index ON po_pre_purchase_attachment(userID)",	null);
		} catch (DaoException e) {}

		try {
			super.update("CREATE TABLE po_pre_purchase_item ("
					+ "itemID VARCHAR(100) NOT NULL, "
					+ "ppID VARCHAR(100) NOT NULL, "
					+ "userID VARCHAR(100) NOT NULL, "
					+ "itemCode VARCHAR(225), "
					+ "qty DOUBLE, " 
					+ "suggestedVendor VARCHAR(225), " 
					+ "dateCreated DATETIME, "
					+ "lastUpdatedDate datetime, " 
            		+ "lastUpdatedBy varchar(255), " 
					+ "PRIMARY KEY(itemID))", null);
		} catch (DaoException e) {}

		try {
			super.update("CREATE INDEX ppurchase_index ON po_pre_purchase_item(ppID)",
							null);
		} catch (DaoException e) {}

		try {
			super.update(
					"CREATE INDEX usrid_index ON po_pre_purchase_item(userID)",
					null);
		} catch (DaoException e) {}
		
		try {
			super.update("CREATE TABLE po_pre_purchase_approval ("
					+ "ppaID VARCHAR(100) NOT NULL, "
					+ "ppID VARCHAR(100) NOT NULL, "
					+ "approved CHAR(1) NOT NULL, "
					+ "remarks TEXT, "
					+ "checkedBy VARCHAR(100), " 
					+ "dateChecked DATETIME, "
					+ "lastUpdatedDate datetime, " 
            		+ "lastUpdatedBy varchar(255), " 
					+ "PRIMARY KEY(ppaID))", null);
		} catch (DaoException e) {}
		
		try {
			super.update(
					"CREATE INDEX ppid_index ON po_pre_purchase_approval(ppID)",
					null);
		} catch (DaoException e) {}
		
		try {
			super.update("CREATE TABLE po_category ("
					+ "categoryID VARCHAR(100) NOT NULL, "
					+ "category VARCHAR(100) NOT NULL, "
					+ "dateCreated datetime, " 
            		+ "createdBy varchar(255), " 
					+ "lastUpdatedDate datetime, " 
            		+ "lastUpdatedBy varchar(255), " 
					+ "PRIMARY KEY(categoryID))", null);
		} catch (DaoException e) {}
		
		try {
			super.update(
					"CREATE INDEX category_index ON po_item_category(categoryID)",
					null);
		} catch (DaoException e) {}
		
		try {
			super.update("CREATE TABLE po_item ("
					+ "itemCode VARCHAR(100) NOT NULL, "
					+ "itemDesc VARCHAR(100) NOT NULL, "
					+ "categoryID VARCHAR(100) NOT NULL, "
					+ "unitOfMeasure varchar(255), " 
					+ "minQty DOUBLE, " 
					+ "approved char(1), " 
					+ "dateCreated datetime, " 
            		+ "createdBy varchar(255), " 
					+ "lastUpdatedDate datetime, " 
            		+ "lastUpdatedBy varchar(255), " 
					+ "PRIMARY KEY(itemCode))", null);
		} catch (DaoException e) {}
		
		try {
			super.update(
					"CREATE INDEX item_index ON po_item(itemID)",
					null);
		} catch (DaoException e) {}
		
		try {
			super.update("CREATE TABLE po_currency ("
					+ "currencyID VARCHAR(100) NOT NULL, "
					+ "country VARCHAR(100) NOT NULL, "
					+ "currency VARCHAR(100) NOT NULL, "
					+ "dateCreated datetime, " 
            		+ "createdBy varchar(255), " 
					+ "lastUpdatedDate datetime, " 
            		+ "lastUpdatedBy varchar(255), " 
					+ "PRIMARY KEY(currencyID))", null);
		} catch (DaoException e) {}
		
		try {
			super.update(
					"CREATE INDEX currency_index ON po_currency(currencyID)",
					null);
		} catch (DaoException e) {}
		
		try{
			super.update("CREATE TABLE po_config_detail (" +
            		"configDetailId varchar(255), " +
            		"configDetailName varchar(255), " +
            		"configDetailDescription mediumtext DEFAULT '', " +
            		"configDetailType varchar(255), " +
            		"configDetailOrder int(2) DEFAULT 1, " +
            		"dateCreated datetime, " +
            		"createdBy varchar(255), " +
            		"lastUpdatedDate datetime, " +
            		"lastUpdatedBy varchar(255), " +
                    "PRIMARY KEY(configDetailId))", null);
		} 
		catch(DaoException e){}
	}

	public void insertRequest(PurchaseRequestObject pr) throws DaoException {

		Collection attachment = new ArrayList();
		attachment = pr.getAttachmentObject();

		Collection item = new ArrayList();
		item = pr.getPurchaseItemsObject();

		insertPurchaseRequest(pr);
		super.update("DELETE FROM po_pre_purchase_attachment WHERE ppID=?",  new Object []{pr.getPpID()});
		super.update("DELETE FROM po_pre_purchase_item WHERE ppID=?", new Object []{pr.getPpID()});
		for (Iterator i = attachment.iterator(); i.hasNext();) {
			AttachmentObject object = new AttachmentObject();
			object = (AttachmentObject) i.next();
			// System.out.println("AA"+object.getPath());
			insertAttachment(object);
		}

		for (Iterator i = item.iterator(); i.hasNext();) {
			PurchaseItemObject object = new PurchaseItemObject();
			object = (PurchaseItemObject) i.next();
			// System.out.println("AA"+object.getPath());
			insertItems(object);
		}

	}

	public void insertPurchaseRequest(PurchaseRequestObject pr)
			throws DaoException {
		try {
			
				Collection request = super.select("SELECT ppID " +
						  "FROM po_pre_purchase " +
						  "WHERE ppID=?", PurchaseRequestObject.class,new Object[]{pr.getPpID()}, 0, 1);
				
				if(request.size()!=0){
					super.update("UPDATE po_pre_purchase " +
							"SET priority=#priority#, reason=#reason#, status=#status#, " +
							"rank=#rank#, neededby=#neededBy#, " +
							"lastUpdatedDate=#lastUpdatedDate#, lastUpdatedBy=#lastUpdatedBy# " +
							"WHERE ppID=#ppID#", pr);  
				}else{
					super.update("INSERT INTO po_pre_purchase (" + 
						     "ppID, purchaseCode, requesterUserID, " +
						     "priority, reason, neededBy, " +
						     "status, rank, dateCreated, lastUpdatedDate, " +
						     "lastUpdatedBy) VALUES " + 
						     "(#ppID#,#purchaseCode#, #requesterUserID#, " +
						     "#priority#, #reason#, #neededBy#, " +
						     "#status#, #rank#, now(), now(), #lastUpdatedBy#)",pr);
				}
		
			
		} catch (DaoException e) {}
	}

	public void insertAttachment(AttachmentObject obj) throws DaoException {
		try {
			
			super.update("INSERT INTO po_pre_purchase_attachment ("+ 
					     "attachmentID, ppID, userID, newFileName, " +
					     "path, dateCreated) VALUES " + 
					     "(#attachmentID#, #ppID#, #userID#, " +
					     "#newFileName#, #path#, now())",obj);
		} catch (DaoException e) {}
	}

	public void insertItems(PurchaseItemObject obj) throws DaoException {
		try {
			
			
			super.update("INSERT INTO po_pre_purchase_item (" + 
					     "itemID, ppID, userID, itemCode, qty, " +
					     "suggestedVendor, dateCreated) VALUES " + 
					     "(#itemID#, #ppID#, #userID#, #itemCode#, #qty#, " +
					     "#suggestedVendor#, now())",obj);
		} catch (DaoException e) {}
	}
	
	public Collection getPurchaseCode() throws DaoException, DataObjectNotFoundException{
		
		Collection purchaseCode = null;
		purchaseCode = super.select("SELECT purchaseCode " + 
				  "FROM po_pre_purchase " +
				  "ORDER BY dateCreated DESC",
		          HashMap.class,null, 0, 1);

	   return purchaseCode;
	}
	
	
	public Collection getRequestItems(String name, String searchCol, String status, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		
		String condition = (name != null) ? "%" + name + "%" : "%";
		String orderBy = (sort != null) ? sort : "rank";
		if (desc)
            orderBy += " DESC";
		
		
        String columnName = "purchaseCode";

        ArrayList param = new ArrayList();
		Collection request = null;
		StringBuffer sql = new StringBuffer("SELECT ppID, purchaseCode, requesterUserID, " +
				  "reason, neededBy, concat(ppID, '_', purchaseCode, '_', status) as combinedData, " +
				  "status, rank, dateCreated, lastUpdatedDate, lastUpdatedBy " + 
				  "FROM po_pre_purchase WHERE requesterUserID=? AND " +
	              columnName + " LIKE ? ");
		
		param.add(Application.getInstance().getCurrentUser().getId());
		param.add(condition);
		if(!status.equals("")){
			sql.append("AND status=?");
			param.add(status);
		}
		
		sql.append("ORDER BY " + orderBy);
		request = super.select( sql.toString(), PurchaseRequestObject.class,param.toArray(), start, rows);
		
		
		if (request.size() != 0) {
         
			Collection items = null;
			for(Iterator i=request.iterator(); i.hasNext();){
				PurchaseRequestObject po = (PurchaseRequestObject)i.next();
				items = super.select("SELECT i.itemID, i.ppID, i.userID, i.itemCode, " +
						"i.suggestedVendor, i.dateCreated, o.itemDesc " + 
						"FROM po_pre_purchase_item i, po_item o " +
						"WHERE i.ppID=? AND o.itemCode = i.itemCode ",
				        PurchaseItemObject.class,new Object[]{po.getPpID()}, 0, -1);
				
				// po.setDelimitedItems("dfd");
				//po.setPurchaseItemsObject(items);
				String item = "";
				for(Iterator itr=items.iterator(); itr.hasNext();){
					
					PurchaseItemObject pi = (PurchaseItemObject)itr.next();		
					
					if(item.length() > 0){
						item += ", ";
					}
						
					item += pi.getItemDesc();
					po.setDelimitedItems(item);
				}
			}
		}
		 
		return request;
	}
	
	public int countRequest(String name, String searchCol) throws DaoException, DataObjectNotFoundException{
		String condition = (name != null) ? "%" + name + "%" : "%";
		int total = 0;	
		Object[] args = {Application.getInstance().getCurrentUser().getId(),  condition };
		Collection countrequest = null;
        String columnName = "purchaseCode";
        
        if(! "".equals(searchCol)) {
            if("status".equalsIgnoreCase(searchCol)) {
                columnName = "status";
            }
            else if("dateCreated".equals(searchCol)) {
                columnName = "dateCreated";
            }
        }
        
        countrequest = super.select("SELECT COUNT(*) AS total " + 
				       "FROM po_pre_purchase " +
				       "WHERE requesterUserID=? AND " +
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
	
	public void deleteRequest(String id) throws DaoException, DataObjectNotFoundException{
		Object[] args = new String[] {
		            id
		        };
		PurchaseRequestObject po = new PurchaseRequestObject();
		po.setStatus("Withdrawn");
		po.setRank(4);
		po.setPpID(id);
		super.update("UPDATE po_pre_purchase SET status=#status#, rank=#rank# WHERE ppID=#ppID#", po);  
		
	}
	
	public PurchaseRequestObject singleRequest(String ppID) throws DaoException, DataObjectNotFoundException{
		Object[] purchaseID = new Object[] {
				ppID
		}; 
		
		Collection oneRequest =  null;
		PurchaseRequestObject po = null;
	    oneRequest = super.select("SELECT ppID, purchaseCode, requesterUserID, " +
				     "reason, neededBy, priority, " +
				     "status, rank, dateCreated, lastUpdatedDate, lastUpdatedBy " + 
				     "FROM po_pre_purchase " +
				     "WHERE ppID=?",
				     PurchaseRequestObject.class,purchaseID, 0, 1);
	 
	    if (oneRequest.size() == 0) {
            throw new DataObjectNotFoundException();
		}
		else {
			Collection attachment = null;
			Collection items = null;
			Collection userName = null;
			for(Iterator i=oneRequest.iterator(); i.hasNext();){
				po = (PurchaseRequestObject)i.next();
				items = super.select("SELECT item.itemID, ppID, userID, " +
						"i.itemDesc, i.unitOfMeasure, qty, " +
						"item.itemCode, suggestedVendor, item.dateCreated " + 
						"FROM po_pre_purchase_item item, po_item i " +
						"WHERE ppID=? AND item.itemCode=i.itemCode ",
				        PurchaseItemObject.class,new Object[]{po.getPpID()}, 0, -1);
				
				attachment = super.select("SELECT attachmentID, ppID, userID, " +
						"newFileName, path " +
						"FROM po_pre_purchase_attachment " +
						"WHERE ppID=? ",
				        AttachmentObject.class,new Object[]{po.getPpID()}, 0, -1);
				
				userName = super.select("SELECT firstName, lastName FROM security_user WHERE id=? " ,
			               HashMap.class,new Object[]{po.getRequesterUserID()}, 0, 1);
				
				String fName="";
				String lName="";
				for(Iterator itr= userName.iterator(); itr.hasNext(); ) {
	        		HashMap map = (HashMap) itr.next();
	        		fName =(String) map.get("firstName");
	        		lName =(String) map.get("lastName");
	        		po.setRequester(fName + " " + lName);
	        	}
				if(attachment!=null && attachment.size()!=0){
					po.setAttachmentObject(attachment);
				}
				po.setPurchaseItemsObject(items);	
			}
		}
  	     return po;
	}
	 
	public Collection getListingItem(String name, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		
			String condition = (name != null) ? "%" + name + "%" : "%";
			
			String orderBy = (sort != null) ? sort : "rank";
			if (desc)
	            orderBy += " DESC";
			
			Object[] args = {condition};
			
	        String columnName = "purchaseCode";
	        
	        if(! "".equals(searchCol)) {
	          if("dateCreated".equals(searchCol)) {
	                columnName = "dateCreated";
	            }
	        }
	        
			Collection request = null;
			
			request = super.select("SELECT ppID, purchaseCode, requesterUserID, concat(firstname, ' ', lastname) as requester, " +
					 "reason, neededBy, " +
					 "status, rank, dateCreated, lastUpdatedDate, lastUpdatedBy " + 
					  "FROM po_pre_purchase po , security_user u " +
					  "WHERE status <>'New' AND status <>'Rejected by HOD' AND status <> 'Draft' AND " +
		              columnName + " LIKE ? AND u.id=po.requesterUserID " +
		              "ORDER BY " + orderBy, PurchaseRequestObject.class,args, start, rows);
			
			
			if (request.size() != 0) {
	          	Collection items = null;
				Collection userName = null;
				for(Iterator i=request.iterator(); i.hasNext();){
					PurchaseRequestObject po = (PurchaseRequestObject)i.next();
					items = super.select("SELECT i.itemID, i.ppID, i.userID, i.itemCode, " +
						"i.suggestedVendor, i.dateCreated, o.itemDesc " + 
						"FROM po_pre_purchase_item i, po_item o " +
						"WHERE i.ppID=? AND o.itemCode = i.itemCode ",
					        PurchaseItemObject.class,new Object[]{po.getPpID()}, 0, -1);
					
					po.setPurchaseItemsObject(items);
					String item = "";
					for(Iterator itr=items.iterator(); itr.hasNext();){
						
						PurchaseItemObject pi = (PurchaseItemObject)itr.next();		
						
						if(item.length() > 0){
							item += ", ";
						}
							
						item += pi.getItemDesc();
						po.setDelimitedItems(item);
					}
				}
			}
						
			return request;
	}
	 
	public int countListingRequest(String name, String searchCol) throws DaoException, DataObjectNotFoundException{
			String condition = (name != null) ? "%" + name + "%" : "%";
			int total = 0;	
			
			Object[] args = {condition };
			Collection countrequest = null;
	        String columnName = "purchaseCode";
	        
	        if(! "".equals(searchCol)) {
	             if("dateCreated".equals(searchCol)) {
	                columnName = "dateCreated";
	            }
	        }
	        
	        countrequest = super.select("SELECT COUNT(*) AS total " + 
					       "FROM po_pre_purchase " +
					       "WHERE status <>'New' AND status <>'Rejected by HOD' AND " +
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
			
	public void saveDecision(PrePurchaseApprovalObject obj,PurchaseRequestObject po)throws DaoException {
			super.update("INSERT INTO po_pre_purchase_approval ("+ 
				     "ppaID, ppID, approved, remarks, checkedBy, dateChecked) VALUES " + 
				     "(#ppaID#, #ppID#, #approved#, #remarks#, #checkedBy#, now())",obj);
			super.update("UPDATE po_pre_purchase SET status=#status#, rank=#rank# WHERE ppID=#ppID#", po);
			
	}
		
	public String getReason(String ppID, String status)throws DaoException, DataObjectNotFoundException{
			Object[] purchaseID = new Object[] {
					ppID
			}; 
			
			Collection reason =  null;
			String reasons="";
			if(status.equals("Approved by HOD") || status.equals("Rejected by HOD") ){
				reason = super.select("SELECT remarks "+ 
						 "FROM po_pre_purchase_approval " +
						 "WHERE ppID=?",
				         HashMap.class,purchaseID, 0, 1);
			}else if (status.equals("Approved by BO")|| status.equals("Rejected by BO") || status.equals("Re-Quote")){
				reason = super.select("SELECT remarks "+ 
						 "FROM po_budget_approval " +
						 "WHERE ppID=?",
				         HashMap.class,purchaseID, 0, 1);
			}
			
		    if (reason.size() != 0) {
				for(Iterator i=reason.iterator(); i.hasNext();){
					HashMap map = (HashMap) i.next();
					reasons = (String) map.get("remarks");
				}
			}
		    return reasons;
	}
		
	public Collection getAttachment(String ppID) throws DaoException, DataObjectNotFoundException{
			Object[] purchaseID = new Object[] {
					ppID
			}; 
			Collection attachment = null;
			attachment = super.select("SELECT attachmentID, newFileName " +
					     "FROM po_pre_purchase_attachment " + 
					     "WHERE ppID=?", 
					     HashMap.class,purchaseID, 0, -1);
			return attachment;
	}
		
	public AttachmentObject downloadFile(String attachmentID)throws DaoException, DataObjectNotFoundException{
			Object[] purchaseID = new Object[] {
					attachmentID
			}; 
			AttachmentObject obj = null;
			Collection attachment = null;
			attachment = super.select("SELECT attachmentID, ppID, " +
					     "userID, newFileName, path, dateCreated " +
					     "FROM po_pre_purchase_attachment " + 
					     "WHERE attachmentID=?", 
					     AttachmentObject.class,purchaseID, 0, 1);
			
			for(Iterator i=attachment.iterator(); i.hasNext();){
				obj = (AttachmentObject)i.next();
			}
				
				
			return obj;
	}
		
	public Collection getQuotedItem(String name, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
				
				String condition = (name != null) ? "%" + name + "%" : "%";
				String status = "Quoted";
				
				String orderBy = (sort != null) ? sort : "rank";
				if (desc)
		            orderBy += "DESC";
				
				Object[] args = {status, condition};
				
		        String columnName = "purchaseCode";
		        
		        if(! "".equals(searchCol)) {
		          if("dateCreated".equals(searchCol)) {
		                columnName = "dateCreated";
		            }
		        }
		        
				Collection request = null;

				request = super.select("SELECT ppID, purchaseCode, requesterUserID, " +
						  "reason, neededBy, " +
						  "status, rank, dateCreated, lastUpdatedDate, lastUpdatedBy " + 
						  "FROM po_pre_purchase " +
						  "WHERE status=? AND " +
			              columnName + " LIKE ? " +
			              "ORDER BY " + orderBy, PurchaseRequestObject.class,args, start, rows);
				
				
				if (request.size() != 0) {
		          	Collection items = null;
					Collection userName = null;
					for(Iterator i=request.iterator(); i.hasNext();){
						PurchaseRequestObject po = (PurchaseRequestObject)i.next();
						items = super.select("SELECT itemID, ppID, userID, " +
								"itemCode, suggestedVendor, dateCreated " + 
								"FROM po_pre_purchase_item " +
								"WHERE ppID=?",
						        PurchaseItemObject.class,new Object[]{po.getPpID()}, 0, -1);
						userName = super.select("SELECT firstName, lastName FROM security_user WHERE id=? " ,
					               HashMap.class,new Object[]{po.getRequesterUserID()}, 0, 1);
						
						String fName="";
						String lName="";
						for(Iterator itr= userName.iterator(); itr.hasNext(); ) {
			        		HashMap map = (HashMap) itr.next();
			        		fName =(String) map.get("firstName");
			        		lName =(String) map.get("lastName");
			        		po.setRequester(fName + " " + lName);
			        	}
						po.setPurchaseItemsObject(items);
						String item = "";
						for(Iterator itr=items.iterator(); itr.hasNext();){
							
							PurchaseItemObject pi = (PurchaseItemObject)itr.next();		
							
							if(item.length() > 0){
								item += ", ";
							}
								
							item += pi.getItemCode();
							po.setDelimitedItems(item);
						}
					}
				}
							
				return request;
	}
		 
	public void changeDecision(PurchaseRequestObject po)throws DaoException {
		super.update("UPDATE po_pre_purchase SET status=#status#, rank=#rank# WHERE ppID=#ppID#", po);
	}
	
	public Collection getRequesterID(String ppID)throws DaoException, DataObjectNotFoundException{
		Object[] purchaseID = new Object[] {
				ppID
		}; 
		
		Collection id =  null;
		
	
		id = super.select("SELECT requesterUserID "+ 
			 "FROM po_pre_purchase " +
			 "WHERE ppID=?",
		     HashMap.class,purchaseID, 0, -1);

		 if (id.size() == 0) {
	            throw new DataObjectNotFoundException();
	      } else {
	            return id;
	      }
	}
	
    public String getPCode(String ppID) throws DaoException, DataObjectNotFoundException{
	   Object[] purchaseID = new Object[] {
				ppID
		}; 
		
		String purchaseCode = "";
		Collection pCode = null;
		pCode = super.select("SELECT purchaseCode "+ 
				"FROM po_pre_purchase " +
				"WHERE ppID=?",
		        HashMap.class,purchaseID, 0, 1);
		
		 if (pCode.size() != 0) {
       		
				for(Iterator i=pCode.iterator(); i.hasNext();){
					HashMap map = (HashMap) i.next();
					purchaseCode = (String) map.get("purchaseCode");
				}
					
		 }
		 
	   return purchaseCode;
	
	}
   
    public Collection getHODListing(String name, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
	    Application app = Application.getInstance();
		String currentUser;
		currentUser = app.getCurrentUser().getId();
		
		String condition = (name != null) ? "%" + name + "%" : "%";
		
	
		String orderBy = (sort != null) ? sort : "rank";
		if (desc)
           orderBy += " DESC";
				
       String columnName = "purchaseCode";
       
       if(! "".equals(searchCol)) {
         if("dateCreated".equals(searchCol)) {
               columnName = "dateCreated";
           }
       }
       
		Collection request = null;
		Object[] args = {currentUser, condition};
		 
	
		request = super.select("SELECT ppID, purchaseCode, requesterUserID,  concat(firstname, ' ', lastname) as requester, " +
				  "reason, neededBy, " +
				  "status, rank, dateCreated, lastUpdatedDate, lastUpdatedBy " + 
				  "FROM po_pre_purchase po, org_chart_subordinates org, security_user u  " +
				  "WHERE status<>'Draft' AND requesterUserID=subordinatesId AND org.id=? AND " +
	              columnName + " LIKE ? AND u.id=po.requesterUserID " +
	              "ORDER BY " + orderBy, PurchaseRequestObject.class, args, start, rows);
		
		
		if (request.size() != 0) {
         	Collection items = null;
			Collection userName = null;
			for(Iterator i=request.iterator(); i.hasNext();){
				PurchaseRequestObject po = (PurchaseRequestObject)i.next();
				items = super.select("SELECT i.itemID, i.ppID, i.userID, i.itemCode, " +
						"i.suggestedVendor, i.dateCreated, o.itemDesc " + 
						"FROM po_pre_purchase_item i, po_item o " +
						"WHERE i.ppID=? AND o.itemCode = i.itemCode ",
				        PurchaseItemObject.class,new Object[]{po.getPpID()}, 0, -1);
				userName = super.select("SELECT firstName, lastName FROM security_user WHERE id=? " ,
			               HashMap.class,new Object[]{po.getRequesterUserID()}, 0, 1);
				
				String fName="";
				String lName="";
				for(Iterator itr= userName.iterator(); itr.hasNext(); ) {
	        		HashMap map = (HashMap) itr.next();
	        		fName =(String) map.get("firstName");
	        		lName =(String) map.get("lastName");
	        		po.setRequester(fName + " " + lName);
	        	}
				po.setPurchaseItemsObject(items);
				String item = "";
				for(Iterator itr=items.iterator(); itr.hasNext();){
					
					PurchaseItemObject pi = (PurchaseItemObject)itr.next();		
					
					if(item.length() > 0){
						item += ", ";
					}
						
					item += pi.getItemDesc();
					po.setDelimitedItems(item);
				}
			}
		}
					
		return request;
    }
   
    public int countHODListing(String name, String searchCol, String user) throws DaoException, DataObjectNotFoundException{
	    Application app = Application.getInstance();
		String currentUser;
		currentUser = app.getCurrentUser().getId();
		
		String condition = (name != null) ? "%" + name + "%" : "%";
						
        String columnName = "purchaseCode";
      
        if(! "".equals(searchCol)) {
        	if("dateCreated".equals(searchCol)) {
              columnName = "dateCreated";
        	}
        }
		Object[] args = {condition};
			   
		int total = 0;	
			
		Collection countrequest = null;
	      
        countrequest = super.select("SELECT COUNT(*) AS total " + 
				       "FROM po_pre_purchase po, org_chart_subordinates org " +
				       "WHERE requesterUserID=subordinatesId AND id=? AND " +
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
    
    public Collection getRequestListing(String name, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		
		String condition = (name != null) ? "%" + name + "%" : "%";
		
		String orderBy = (sort != null) ? sort : "rank";
		if (desc)
           orderBy += " DESC";
		
		Object[] args = {condition};
		
        String columnName = "purchaseCode";
     
		Collection request = null;
		
		request = super.select("SELECT ppID, purchaseCode, requesterUserID, concat(firstname, ' ', lastname) as requester," +
				 "reason, neededBy, " +
				 "status, rank, dateCreated, lastUpdatedDate, lastUpdatedBy " + 
				  "FROM po_pre_purchase po, security_user u " +
				  "WHERE status <>'New' AND status <>'Rejected by HOD' AND " +
				  "status <>'Approved by HOD' AND status<>'Draft' AND " +
	              columnName + " LIKE ? AND u.id=po.requesterUserID " +
	              "ORDER BY " + orderBy, PurchaseRequestObject.class,args, start, rows);
		
		
		if (request.size() != 0) {
         	Collection items = null;
			Collection userName = null;
			for(Iterator i=request.iterator(); i.hasNext();){
				PurchaseRequestObject po = (PurchaseRequestObject)i.next();
				items = super.select("SELECT itemID, ppID, userID, " +
						"itemDesc, suggestedVendor, ppi.dateCreated " + 
						"FROM po_pre_purchase_item ppi, po_item i " +
						"WHERE i.itemCode=ppi.itemCode AND ppID=?",
				        PurchaseItemObject.class,new Object[]{po.getPpID()}, 0, -1);
				
				po.setPurchaseItemsObject(items);
				String item = "";
				for(Iterator itr=items.iterator(); itr.hasNext();){
					
					PurchaseItemObject pi = (PurchaseItemObject)itr.next();		
					
					if(item.length() > 0){
						item += ", ";
					}
						
					item += pi.getItemDesc();
					po.setDelimitedItems(item);
				}
			}
		}
					
		return request;
	}
   
    public int countRequestListing(String name, String searchCol) throws DaoException, DataObjectNotFoundException{
		String condition = (name != null) ? "%" + name + "%" : "%";
		int total = 0;	
		
		Object[] args = {condition };
		Collection countrequest = null;
       String columnName = "purchaseCode";
       
       if(! "".equals(searchCol)) {
            if("dateCreated".equals(searchCol)) {
               columnName = "dateCreated";
           }
       }
       
       countrequest = super.select("SELECT COUNT(*) AS total " + 
				      "FROM po_pre_purchase " +
				      "WHERE status <>'New' AND status <>'Rejected by HOD' AND " +
					  "status <>'Approved by HOD' AND " +
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
    
	 public String getStatus(String ppID) throws DaoException, DataObjectNotFoundException {
		  String status = "";
		  Collection ppStatus = null;
		  Object[] args = new Object[] {
				  ppID
		        };
		 
		  ppStatus = super.select("SELECT status FROM po_pre_purchase WHERE ppID=?" ,
	                 HashMap.class,args, 0, -1);
		  
		  if (ppStatus.size() != 0) {
	       		
				for(Iterator i=ppStatus.iterator(); i.hasNext();){
					HashMap map = (HashMap) i.next();
					status = (String) map.get("status");
				}
				
		 }
		return status;
	 }
	 
	 public Collection getHOD(String currentUserId) throws DaoException, DataObjectNotFoundException {
		  Object[] args = new Object[] {
				  currentUserId
		        };
		  Collection hod = null;
		 
		  hod = super.select("SELECT sub.id AS userId " +
		  		"FROM org_chart_subordinates sub, org_chart_hierachy hie " +
		  		"WHERE sub.subordinatesId=? AND hie.hod='1' AND hie.userId=sub.id " ,
	               HashMap.class,args, 0, -1);
		  
		  if (hod.size()!= 0) {
	            return hod;
	      }
		  
		  return null;
	 }
	 
		public Collection getSupplierItems(String ppID) throws DaoException, DataObjectNotFoundException{
			Collection counts = null;
			Collection addItems=null;
			counts = super.select("SELECT counting " +
				     "FROM po_supplier " + 
				     "WHERE ppID=? ORDER BY counting DESC", 
				     HashMap.class,new Object[]{ppID}, 0,1);
		
			String no="";
			for(Iterator i=counts.iterator();i.hasNext();){
				HashMap map = (HashMap)i.next();
				no = map.get("counting").toString();
			}
			if(no!=""){
				addItems=new ArrayList();
				int count = Integer.parseInt(no);
				Collection items= null;
				for(int i=1;i<=count;i++){
					
					items = super.select("SELECT DISTINCT spp.ppID, " +
							"i.itemDesc, i.unitOfMeasure, i.itemCode, ppi.qty AS minQty, " +
							"spp.counting " + 
							"FROM po_supplier spp, po_supplier_item item, po_item i, po_pre_purchase_item ppi " +
							"WHERE spp.ppID=? AND spp.supplierID=item.supplierID " +
							"AND spp.counting=? AND item.itemCode = i.itemCode " +
							"AND ppi.itemCode = i.itemCode AND spp.ppID= ppi.ppID ",
					        ItemObject.class,new Object[]{ppID,i}, 0, -1);
					
					for(Iterator j = items.iterator();j.hasNext();){
						ItemObject obj = (ItemObject)j.next();
						addItems.add(obj);
					}
				}
			}
			
				
	  	     return addItems;
		}
		
		public Collection getPurchaseOrderCode() throws DaoException, DataObjectNotFoundException{
			
			Collection purchaseCode = null;
			purchaseCode = super.select("SELECT poCode " + 
					  "FROM po_purchase_order " +
					  "ORDER BY dateCreated DESC",
			          HashMap.class,null, 0, 1);

		   return purchaseCode;
		}
		
		 public int countRequest(String status) throws DaoException, DataObjectNotFoundException{
			   Collection countrequest= null;
		       String id = Application.getInstance().getCurrentUser().getId();
		       if(status.equals("po")){
		    	   countrequest = super.select("SELECT COUNT(*) AS total " + 
							  "FROM po_pre_purchase po " +
							  "WHERE ( status='Approved By HOD' OR status='Re-Quote')"
				                  , HashMap.class,null, 0, -1);
		    	   
		       }else if(status.equals("New")){
		    	   countrequest = super.select("SELECT COUNT(*) AS total " + 
							  "FROM po_pre_purchase po, org_chart_subordinates org " +
							  "WHERE requesterUserID=subordinatesId AND id=? AND status=?"
				                  , HashMap.class,new Object[]{id, status}, 0, -1);
		    	   
		       }else if(status.equals("Quoted")){
		    	   countrequest = super.select("SELECT COUNT(*) AS total " + 
							  "FROM po_pre_purchase po " +
							  "WHERE status=? "
				                  , HashMap.class,new Object[]{status}, 0, -1);
		    	   
		       }
		       
		       int total=0;
		       if(countrequest != null) {
		       	for(Iterator i= countrequest.iterator(); i.hasNext(); ) {
		       		HashMap map = (HashMap) i.next();
		       		total = Integer.parseInt(map.get("total").toString());
		       	}
		       }
		       
		       return total;
		 }
		 
			public Collection getItems(String ppID) throws DaoException, DataObjectNotFoundException{
				
				Collection itemCode = new ArrayList();
				Collection items = super.select("SELECT itemCode " + 
						  "FROM po_pre_purchase_item " +
						  "WHERE ppID=?",
				          HashMap.class,new Object[]{ppID}, 0, -1);
				for(Iterator i=items.iterator();i.hasNext();){
					HashMap map = (HashMap)i.next();
					itemCode.add(map.get("itemCode"));
				}

			   return itemCode;
			}
			
		
			public void resubmitRequest(String id) throws DaoException, DataObjectNotFoundException{
				Object[] args = new String[] {
				            id
				        };
				PurchaseRequestObject po = new PurchaseRequestObject();
				po.setStatus("Resubmitted");
				po.setRank(3);
				po.setPpID(id);
				super.update("UPDATE po_pre_purchase SET status=#status#, rank=#rank# WHERE ppID=#ppID#", po);  
				
			}
}
