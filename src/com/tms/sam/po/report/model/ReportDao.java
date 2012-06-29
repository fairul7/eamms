package com.tms.sam.po.report.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

import com.tms.sam.po.model.RatingObject;

public class ReportDao extends DataSourceDao{
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
	 
	 public int countSuppliersRating(Collection ids) throws DaoException, DataObjectNotFoundException{
		 Collection countrequest = null;
			int total =0;
			Collection paramList = new ArrayList();
			StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS total " + 
					       "FROM po_supplier_rating suppr, dir_biz_contact dir " +
					       "WHERE suppr.id=dir.id ");
			
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
	        countrequest = super.select(sql.toString(), RatingObject.class,paramList.toArray(), 0, 1);
	        if(countrequest != null) {
	        	for(Iterator i= countrequest.iterator(); i.hasNext(); ) {
	        		RatingObject map = (RatingObject) i.next();
	        		total = Integer.parseInt(map.getProperty("total").toString());
	        	}
	        }
	        
	        return total;
	 }
	 
	 
	public Collection getStatusListing(String fromDate, String toDate, String status, String reqDept, 
				String sort, boolean desc, int start, int row) throws DaoException{
			Collection result = new ArrayList();
			
			StringBuffer sql = new StringBuffer("SELECT p.status, deptC.associativityId, " +
					"concat(country.shortDesc, '-', dept.shortDesc) AS deptDesc, " +
					"count(ppID) AS totalRequest " +
					"FROM po_pre_purchase p, org_chart_hierachy h, " +
					"org_chart_department dept, org_chart_country country, org_chart_dept_country deptC " +
					"WHERE h.userId = p.requesterUserID " +
					"AND dept.code = deptC.deptCode " +
					"AND country.code = deptC.countryCode " +
					"AND h.deptCode =  deptC.deptCode " +
					"AND h.countryCode =  deptC.countryCode");
			
			if(fromDate != null && fromDate.trim().length() > 0){
				sql.append(" AND p.dateCreated >= '" + fromDate + " 00:00:00' ");
			}
			
			if(toDate != null && toDate.trim().length() > 0){
				sql.append(" AND p.dateCreated <= '" + toDate + " 23:59:00' ");
			}
			
			if(status != null && status.trim().length() > 0){
				sql.append(" AND p.status = '" + status + "' ");
			}
			
			if(reqDept != null && reqDept.trim().length() > 0){
				sql.append(" AND deptC.associativityId = '" + reqDept + "' ");
			}

			
			sql.append(" GROUP BY deptDesc, p.status ");
			
			if(sort != null && sort.trim().length() > 0){
				sql.append(" ORDER BY " + sort);
			}else{
				sql.append(" ORDER BY deptDesc ");
			}
			
			if(desc)
				sql.append(" DESC");
			
			result = super.select(sql.toString(), StatusReportObject.class, null, start, row);
			
			return result;
		}
}
