package com.tms.sam.po.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

public class PurchaseOrderDao extends DataSourceDao{
	public void init() throws DaoException {
		try {
			super.update("CREATE TABLE po_purchase_order ("
					+ "poID VARCHAR(100) NOT NULL, "
					+ "ppID VARCHAR(100) NOT NULL, "
					+ "poCode VARCHAR(100) NOT NULL, "
					+ "supplierID VARCHAR(100) NOT NULL, "
					+ "counting INT, "
					+ "deliveryOrderNo VARCHAR(35), "
					+ "invoiceNo VARCHAR(35), " 
					+ "invoiceDate DATE, "
					+ "terms INT, " 
					+ "paid CHAR(1), " 
					+ "referenceNo VARCHAR(35), "
					+ "beneficiary VARCHAR(35), " 
					+ "typeOfPayment VARCHAR(35), "
					+ "datePaid DATETIME, "
					+ "amountPaid DOUBLE, "
					+ "dateDelivered DATETIME, " 
					+ "dateCreated DATETIME, "
					+ "createdBy  VARCHAR(100), "
					+ "lastUpdatedDate DATETIME, "
					+ "lastUpdatedBy  VARCHAR(100), " 
					+ "PRIMARY KEY(poID))",
					null);
		} catch (DaoException e) {}

		try {
			super.update("CREATE INDEX purchase_order_index ON po_purchase_order(poID)", null);
		} catch (DaoException e) {}

	}

	public boolean insertPurchaseOrder(PurchaseOrderObject pr, PurchaseRequestObject prObj)
	throws DaoException ,DataObjectNotFoundException{
		try {
			
			PurchaseOrderObject po = getPurchaseOrder(pr.getPpID(), pr.getSupplierID(), pr.getCount());
			
			if(po==null){
				super.update("INSERT INTO po_purchase_order (" + 
					     "poID, ppID, poCode, supplierID, counting, " +
					     "deliveryOrderNo, invoiceNo, invoiceDate, " +
					     "terms, dateDelivered, dateCreated, createdBy, lastUpdatedDate," +
					     "lastUpdatedBy) VALUES " + 
					     "(#poID#,#ppID#, #poCode#, #supplierID#, #count#, " +
					     "#deliveryOrderNo#, #invoiceNo#, #invoiceDate#, #terms#," +
					     "#dateDelivered#, #dateCreated#, #createdBy#, #lastUpdatedDate#, #lastUpdatedBy#)",pr);
				
				Collection request = super.select("SELECT COUNT(counting) AS count " +
						  "FROM po_supplier " +
						  "WHERE ppID=? AND approved='Yes' " 
						  , PurchaseOrderObject.class,new Object[]{pr.getPpID()}, 0, -1);
				
				PurchaseOrderObject poo=null;
				int count=0;
				if (request.size() == 1) {
					Iterator iterator = request.iterator();
					poo = (PurchaseOrderObject) iterator.next();
					count = Integer.parseInt(poo.getProperty("count").toString());
					
				}
				
				request = super.select("SELECT poID " + 
						  "FROM po_purchase_order " +
						  "WHERE ppID=? " 
						  , PurchaseOrderObject.class,new Object[]{pr.getPpID()}, 0, -1);
				
				if(request.size()==count){
					super.update("UPDATE po_pre_purchase SET status=#status#, rank=#rank# WHERE ppID=#ppID#", prObj);
					return true;
				}else{
					return false;
				}
				
			}else{
				super.update("UPDATE po_purchase_order SET " +
						"deliveryOrderNo=#deliveryOrderNo#, " +
						"invoiceNo=#invoiceNo#, " +
						"invoiceDate=#invoiceDate#, " +
						"dateDelivered=#dateDelivered#, " +
						"terms=#terms# " +
						"WHERE ppID=#ppID#", pr);
			}
			
			
		} catch (DaoException e) {}
		
		return false;
	}
	
	public boolean checkRecord(String ppID)
	throws DaoException {
		try {
			Collection check = super.select("SELECT status FROM po_pre_purchase " +
					"WHERE status='Approved by BO' " +
					"AND ppID=?" ,
				          		HashMap.class,new Object[]{ppID}, 0, 1);
			
			if(check!=null){
				if(check.size()!=0){
					return true;
				}else{
					return false;
				}
			}
		} catch (DaoException e) {}
		
		return false;
	}
	
	public Collection getRequestListing(String name, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException ,DataObjectNotFoundException{
		
		String condition = (name != null) ? "%" + name + "%" : "%";
		
		String orderBy = (sort != null) ? sort : "poCode";
		if (desc)
           orderBy += " DESC";
		
		Object[] args = {condition};
		
        String columnName = "poCode";
       
		Collection request = null;
		
		request = super.select("SELECT o.poID, o.ppID, o.poCode, ppr.requesterUserID, " +
				  "o.dateCreated, ppr.status " + 
				  "FROM po_pre_purchase ppr, po_purchase_order o " +
				  "WHERE (ppr.status='PO Issued' OR ppr.status='Closed') " +
				  "AND ppr.ppID=o.ppID " +
				  "AND " + columnName + " LIKE ? " +
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
	
	public int countRequestListing(String name, String searchCol) throws DaoException, DataObjectNotFoundException{
		String condition = (name != null) ? "%" + name + "%" : "%";
		int total = 0;	
		
		Object[] args = {condition };
		Collection countrequest = null;
        String columnName = "poCode";
     
       countrequest = super.select("SELECT COUNT(*) AS total " + 
    		   			"FROM po_pre_purchase ppr, po_purchase_order o " +
    		   			"WHERE (ppr.status='PO Issued' OR ppr.status='Closed') " +
    		   			"AND ppr.ppID=o.ppID " +
    		   			"AND " + columnName + " LIKE ? " 
	                    , HashMap.class,args, 0, 1);
       
       if(countrequest != null) {
       	for(Iterator i= countrequest.iterator(); i.hasNext(); ) {
       		HashMap map = (HashMap) i.next();
       		total = Integer.parseInt(map.get("total").toString());
       	}
       }
       
       return total;
	}
	
	public PurchaseOrderObject getPurchaseOrder(String ppID, String supplierId, int count) throws DaoException ,DataObjectNotFoundException{
		Collection request;
		
		request = super.select("SELECT deliveryOrderNo, invoiceNo, invoiceDate, terms, " +
				  "dateDelivered " + 
				  "FROM po_purchase_order " +
				  "WHERE ppID=? AND supplierID=? AND counting=?" 
				  , PurchaseOrderObject.class,new Object[]{ppID, supplierId, count}, 0, 1);
		
		PurchaseOrderObject po = null;
		if (request.size() == 1) {
			Iterator iterator = request.iterator();
			po = (PurchaseOrderObject) iterator.next();
		}
		
		return po;
	}
	
	public void insertClosedPurchaseOrder(PurchaseOrderObject pr, PurchaseRequestObject prObj)
	throws DaoException ,DataObjectNotFoundException{
		try {
			super.update("UPDATE po_purchase_order SET " +
						"referenceNo=#referenceNo#, " +
						"beneficiary=#beneficiary#, " +
						"typeOfPayment=#typeOfPayment#, " +
						"datePaid=#datePaid#, " +
						"paid=#paid#, " +
						"amountPaid=#amount# " +
						"WHERE ppID=#ppID# " +
						"AND supplierID=#supplierID# " +
						"AND counting=#count#", pr);
			
			Collection request = super.select("SELECT COUNT(counting) AS count " +
					  "FROM po_supplier " +
					  "WHERE ppID=? AND approved='Yes' " 
					  , PurchaseOrderObject.class,new Object[]{pr.getPpID()}, 0, -1);
			
			PurchaseOrderObject poo=null;
			int count=0;
			if (request.size() == 1) {
				Iterator iterator = request.iterator();
				poo = (PurchaseOrderObject) iterator.next();
				count = Integer.parseInt(poo.getProperty("count").toString());
				
			}
			
			request = super.select("SELECT referenceNo, beneficiary, typeOfPayment, datePaid, " +
					  "amountPaid AS amount, paid " + 
					  "FROM po_purchase_order " +
					  "WHERE ppID=? AND referenceNo is not null" 
					  , PurchaseOrderObject.class,new Object[]{pr.getPpID()}, 0, -1);
			
			if(request.size()==count){
				super.update("UPDATE po_pre_purchase SET status=#status#, rank=#rank# WHERE ppID=#ppID#", prObj);
			}
	
		} catch (DaoException e) {}
	}
	
	public PurchaseOrderObject getClosedPurchaseOrder(String ppID, String supplierId, int count) throws DaoException ,DataObjectNotFoundException{
		Collection request;
		
		request = super.select("SELECT referenceNo, beneficiary, typeOfPayment, datePaid, " +
				  "amountPaid AS amount, paid " + 
				  "FROM po_purchase_order " +
				  "WHERE ppID=? AND supplierID=? AND counting=? AND referenceNo is not null" 
				  , PurchaseOrderObject.class,new Object[]{ppID, supplierId, count}, 0, 1);
		
		PurchaseOrderObject po = null;
		if (request.size() == 1) {
			Iterator iterator = request.iterator();
			po = (PurchaseOrderObject) iterator.next();
		}
		
		return po;
	}
}
