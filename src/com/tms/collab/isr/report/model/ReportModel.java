package com.tms.collab.isr.report.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.lang.StringUtils;

public class ReportModel extends DefaultModule{
	
	public static final String LESS_THAN = "lt";
	public static final String LESS_THAN_SYMB = "<";
	public static final String GREATER_THAN = "gt";
	public static final String GREATER_THAN_SYMB = ">=";
	
	public Collection getTimeOfResolveReqListing(Date fromDate, Date toDate, String condition, String assocId, String statusId){
		Collection result = new ArrayList();
		
		try{
			ReportDao dao = (ReportDao)getDao();
			result = dao.selectTimeOfResolveReportDetailListing(fromDate, toDate, formCondition(condition), assocId, statusId);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getTimeOfResolveReqListing: " + e.getMessage());
		}
		
		return result;
	}
	
	public int getTimeOfResolveReqListingCount(Date fromDate, Date toDate, String condition, String assocId, String statusId){
		int result = 0;
		
		try{
			ReportDao dao = (ReportDao)getDao();
			result = dao.selectTimeOfResolveReportDetailListingCount(fromDate, toDate, formCondition(condition), assocId, statusId);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getTimeOfResolveReqListingCount: " + e.getMessage());
		}
		
		return result;
	}
	
	public String formCondition(String condition){
		String temp = new String();
		if(StringUtils.contains(condition, LESS_THAN))
			temp = LESS_THAN_SYMB + condition.substring(2, condition.length());
		else if(StringUtils.contains(condition, GREATER_THAN))
			temp = GREATER_THAN_SYMB + condition.substring(2, condition.length());
		
		return temp;
	}
	
	public Collection getStaffReportDetailListing(Date fromDate, Date toDate, String status, String assignee){
		Collection result = new ArrayList();
		
		try{
			ReportDao dao = (ReportDao)getDao();
			result = dao.selectStaffReportDetailListing(fromDate, toDate, status, assignee);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityReportDetailListing: " + e.getMessage());
		}
		
		return result;
	}
	
	public int getStaffReportDetailListingCount(Date fromDate, Date toDate, String status, String assignee){
		int result = 0;
		
		try{
			ReportDao dao = (ReportDao)getDao();
			result = dao.selectStaffReportDetailListingCount(fromDate, toDate, status, assignee);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityReportDetailListingCount: " + e.getMessage());
		}
		
		return result;
	}
	
	public Collection getPriorityReportDetailListing(Date fromDate, Date toDate, String status, String reqDept, String recDept){
		Collection result = new ArrayList();
		
		try{
			ReportDao dao = (ReportDao)getDao();
			result = dao.selectPriorityReportDetailListing(fromDate, toDate, status, reqDept, recDept);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityReportDetailListing: " + e.getMessage());
		}
		
		return result;
	}
	
	public int getPriorityReportDetailListingCount(Date fromDate, Date toDate, String status, String reqDept, String recDept){
		int result = 0;
		
		try{
			ReportDao dao = (ReportDao)getDao();
			result = dao.selectPriorityReportDetailListingCount(fromDate, toDate, status, reqDept, recDept);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityReportDetailListingCount: " + e.getMessage());
		}
		
		return result;
	}
	
	public Collection getStatusReportDetailListing(Date fromDate, Date toDate, String status, String reqDept, String recDept){
		Collection result = new ArrayList();
		
		try{
			ReportDao dao = (ReportDao)getDao();
			result = dao.selectStatusReportDetailListing(fromDate, toDate, status, reqDept, recDept);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getStatusReportDetailListing: " + e.getMessage());
		}
		
		return result;
	}
	
	public int getStatusReportDetailListingCount(Date fromDate, Date toDate, String status, String reqDept, String recDept){
		int result = 0;
		
		try{
			ReportDao dao = (ReportDao)getDao();
			result = dao.selectStatusReportDetailListingCount(fromDate, toDate, status, reqDept, recDept);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getStatusReportDetailListingCount: " + e.getMessage());
		}
		
		return result;
	}
	
	//new staff report
	public Collection getStaffReportListing(String fromDate, String toDate, String associativityId, String sort, boolean desc, int start, int row){
		Collection result = new ArrayList();
		
		try{
			ReportDao dao = (ReportDao)getDao();
			
			Collection staffList = dao.selectDepartmentStaff(associativityId);
			String[] staffStrAry = new String[staffList.size()];
			Map staffMap = new HashMap();
			
			if(staffList != null && staffList.size() > 0){
				int counter = 0;
				for(Iterator i=staffList.iterator(); i.hasNext(); ){
					ReportObject ro = (ReportObject)i.next();
					staffStrAry[counter] = ro.getUserId();
					staffMap.put(ro.getUserId(), ro.getStaffName());
					counter++;
				}
				
				result = dao.selectStaffReportListing(fromDate, toDate, staffStrAry, sort, desc, start, row);
				
				for(Iterator i=result.iterator(); i.hasNext(); ){
					ReportObject ro = (ReportObject)i.next();
					
					if(staffMap.get(ro.getUserId())!=null){
						staffMap.remove(ro.getUserId());
					}
				}
				
				for(Iterator i=staffMap.keySet().iterator(); i.hasNext(); ){
					String userId = (String)i.next();
					ReportObject ro = new ReportObject(); 
					ro.setStaffName((String)staffMap.get(userId));
					ro.setNoOfReqNew("0");
					ro.setReportLinkNew(userId+"*1");
					
					ro.setNoOfReqInProgress("0");
					ro.setReportLinkInProgress(userId+"*3");
					
					ro.setNoOfReqCompleted("0");
					ro.setReportLinkCompleted(userId+"*4");
					
					ro.setNoOfReqClose("0");
					ro.setReportLinkClose(userId+"*5");
					
					ro.setNoOfReqReopen("0");
					ro.setReportLinkReopen(userId+"*2");
					
					ro.setNoOfReq("0");
					ro.setReportLink(userId);
					
					result.add(ro);
				}
				
			}
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getStaffReportListing: " + e.getMessage());
		}
		
		return result;
	}
	
	public int getStaffReportListingCount(String fromDate, String toDate, String associativityId){
		Collection result = new ArrayList();
		
		try{
			ReportDao dao = (ReportDao)getDao();
			
			Collection staffList = dao.selectDepartmentStaff(associativityId);
			String[] staffStrAry = new String[staffList.size()];
			Map staffMap = new HashMap();
			
			if(staffList != null && staffList.size() > 0){
				int counter = 0;
				for(Iterator i=staffList.iterator(); i.hasNext(); ){
					ReportObject ro = (ReportObject)i.next();
					staffStrAry[counter] = ro.getUserId();
					staffMap.put(ro.getUserId(), ro.getStaffName());
					counter++;
				}
				
				result = dao.selectStaffReportListing(fromDate, toDate, staffStrAry, null, false, 0, -1);
				
				for(Iterator i=result.iterator(); i.hasNext(); ){
					ReportObject ro = (ReportObject)i.next();
					
					if(staffMap.get(ro.getUserId())!=null){
						staffMap.remove(ro.getUserId());
					}
				}
				
				return staffList.size()+staffMap.keySet().size();
			}
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getStaffReportListingCount: " + e.getMessage());
		}
		
		
		return 0;
	}
	
	//old staff report
	public Collection getStaffReportListing(String fromDate, String toDate, String status, String associativityId, String sort, boolean desc, int start, int row){
		Collection result = new ArrayList();
		
		try{
			ReportDao dao = (ReportDao)getDao();
			
			Collection staffList = dao.selectDepartmentStaff(associativityId);
			String[] staffStrAry = new String[staffList.size()];
			
			if(staffList != null && staffList.size() > 0){
				int counter = 0;
				for(Iterator i=staffList.iterator(); i.hasNext(); ){
					ReportObject ro = (ReportObject)i.next();
					staffStrAry[counter] = ro.getUserId();
					counter++;
				}
				
				result = dao.selectStaffReportingListing(fromDate, toDate, status, staffStrAry, sort, desc, start, row);
				
				//format the result
				Iterator i=result.iterator();
				String tempStr = new String(); 
				if(i.hasNext()){
					ReportObject tempRo = (ReportObject)i.next();
					tempStr = tempRo.getStaffName();
				}
				
				while(i.hasNext()){
					ReportObject ro = (ReportObject)i.next();
					if(ro.getStaffName().equals(tempStr)){
						ro.setStaffName("");
					}else{
						tempStr = ro.getStaffName();
					}
				}
				
			}
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityNoOfReq: " + e.getMessage());
		}
		
		return result;
	}
	
	public int getStaffReportListingCount(String fromDate, String toDate, String status, String associativityId){
		Collection result = new ArrayList();
		
		try{
			ReportDao dao = (ReportDao)getDao();
			
			Collection staffList = dao.selectDepartmentStaff(associativityId);
			String[] staffStrAry = new String[staffList.size()];
			
			if(staffList != null && staffList.size() > 0){
				int counter = 0;
				for(Iterator i=staffList.iterator(); i.hasNext(); ){
					ReportObject ro = (ReportObject)i.next();
					staffStrAry[counter] = ro.getUserId();
					counter++;
				}
				
				result = dao.selectStaffReportingListing(fromDate, toDate, status, staffStrAry, null, false, 0, -1);
			}
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityNoOfReq: " + e.getMessage());
		}
		
		
		return result.size();
	}
	
	public Collection getTimeOfResolveReqCol(Date fromDate, Date toDate, String requestToDept, String statusId){
		Collection result = new ArrayList();
		Collection temp1 = new ArrayList();
		Collection temp2 = new ArrayList();
		Collection temp3 = new ArrayList();
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			String condition = new String(LESS_THAN_SYMB + " 1 ");
			temp1 = dao.selectTimeOfResolve(fromDate, toDate, condition, requestToDept, statusId);
			
			for(Iterator i=temp1.iterator(); i.hasNext(); ){
				ReportObject ro = (ReportObject)i.next(); 
				ro.setType("Less Than 1 Day");
				ro.setReportLink(LESS_THAN+"1");
				if(ro.getNoOfReq() == null)
					ro.setNoOfReq("0");
				result.add(ro);
			}
			
			condition = new String(LESS_THAN_SYMB+" 7 ");
			temp2 = dao.selectTimeOfResolve(fromDate, toDate, condition, requestToDept, statusId);
			
			for(Iterator i=temp2.iterator(); i.hasNext(); ){
				ReportObject ro = (ReportObject)i.next(); 
				ro.setType("Less Than 7 Day");
				ro.setReportLink(LESS_THAN+"7");
				if(ro.getNoOfReq() == null)
					ro.setNoOfReq("0");
				result.add(ro);
			}
			
			condition = new String(GREATER_THAN_SYMB+" 7 ");
			temp3 = dao.selectTimeOfResolve(fromDate, toDate, condition, requestToDept, statusId);
			
			for(Iterator i=temp3.iterator(); i.hasNext(); ){
				ReportObject ro = (ReportObject)i.next(); 
				ro.setType("Greater than and equal to 7 days");
				ro.setReportLink(GREATER_THAN+"7");
				if(ro.getNoOfReq() == null)
					ro.setNoOfReq("0");
				result.add(ro);
			}
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityNoOfReq: " + e.getMessage());
		}
		
		return result;
	}
	
	public Collection getDepartmentNoOfReq(Date fromDate, Date toDate){
		
		Collection result = new ArrayList();
		Collection reqDeptCol = new ArrayList();
		Collection recDeptCol = new ArrayList();
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			reqDeptCol = dao.selectReqDeptNoOfReq(fromDate, toDate);
			
			recDeptCol = dao.selectRecDeptNoOfReq(fromDate, toDate);
			
			Map recDeptMap = new SequencedHashMap();
			for(Iterator j=recDeptCol.iterator(); j.hasNext(); ){
				ReportObject ro = (ReportObject) j.next();
				recDeptMap.put(ro.getDept(), ro);
			}
			
			for(Iterator k=reqDeptCol.iterator(); k.hasNext(); ){
				ReportObject ro = (ReportObject)k.next();
				
				ReportObject temp = (ReportObject) recDeptMap.get(ro.getDept());
				if(temp != null){
					ro.setRecDeptNoOfReq(temp.getRecDeptNoOfReq());
					recDeptMap.remove(ro.getDept());
				}else{
					ro.setRecDeptNoOfReq("0");
				}
				
				result.add(ro);
			}
			
			if(recDeptMap.size() > 0){
				
				for(Iterator l=recDeptMap.values().iterator(); l.hasNext(); ){
					ReportObject temp = (ReportObject)l.next(); 
					temp.setReqDeptNoOfReq("0");
					result.add(temp);
				}
			}
			
			for(Iterator i=result.iterator(); i.hasNext(); ){
				
				ReportObject temp = (ReportObject)i.next();
				if(temp.getDept() == null || temp.getDept().equals("")){
					temp.setDept("*");
				}
				
			}
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getDepartmentNoOfReq: " + e.getMessage());
		}
		
		return result;
	}
	
	public Collection getStatusNoOfReq(Date fromDate, Date toDate){
		
		Collection result = new ArrayList();
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			result = dao.selectStatusNoOfReq(fromDate, toDate);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityNoOfReq: " + e.getMessage());
		}
		
		return result;
		
	}
	
	public Collection getRequestTypeNoOfReq(Date fromDate, Date toDate){
		
		Collection result = new ArrayList();
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			result = dao.selectRequestTypeNoOfReq(fromDate, toDate);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getRequestTypeNoOfReq: " + e.getMessage());
		}
		
		return result;
		
	}
	
	public Collection getPriorityNoOfReq(Date fromDate, Date toDate){
		
		Collection result = new ArrayList();
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			result = dao.selectPriorityNoOfReq(fromDate, toDate);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityNoOfReq: " + e.getMessage());
		}
		
		return result;
		
	}
	
	public int getCommonStatsPriority(Date fromDate, Date toDate, String status, String priority){
		
		int result=0;
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			result = dao.getCommonStatsPriority(fromDate, toDate, status, priority);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getCommonStatsPriority: " + e.getMessage());
		}
		
		return result;
		
	}
	
	public Collection getPriorityReportListing(String fromDate, String toDate, String priority, String reqDept, String recDept, 
			String sort, boolean desc, int start, int row){
		Collection result = new ArrayList();
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			result = dao.selectPriorityReportListing(fromDate, toDate, priority, reqDept, recDept, sort, desc, start, row);
			
			for(Iterator i=result.iterator(); i.hasNext(); ){
				ReportObject obj = (ReportObject)i.next();
				if(obj.getRecDept() == null || obj.getRecDept().equals("")){
					obj.setRecDept("*");
				}
				if(obj.getReqDept() == null || obj.getReqDept().equals("")){
					obj.setReqDept("*");
				}
			}
			
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getPriorityReportListing: " + e.getMessage());
		}
		
		return result;
	}
	
	public int getPriorityReportListingCount(String fromDate, String toDate, String priority, String reqDept, String recDept){
		int result = 0;
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			result = dao.selectPriorityReportListing(fromDate, toDate, priority, reqDept, recDept, null, false, 0, -1).size();
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in Priority: " + e.getMessage());
		}
		
		return result;
	}
	
	public Collection getStatusReportListing(String fromDate, String toDate, String status, String reqDept, String recDept, 
			String sort, boolean desc, int start, int row){
		Collection result = new ArrayList();
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			result = dao.selectStatusReportListing(fromDate, toDate, status, reqDept, recDept, sort, desc, start, row);
			
			for(Iterator i=result.iterator(); i.hasNext(); ){
				ReportObject obj = (ReportObject)i.next();
				if(obj.getRecDept() == null || obj.getRecDept().equals("")){
					obj.setRecDept("*");
				}
				if(obj.getReqDept() == null || obj.getReqDept().equals("")){
					obj.setReqDept("*");
				}
			}
			
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getStatusReportListing: " + e.getMessage());
		}
		
		return result;
	}
	
	public int getStatusReportListingCount(String fromDate, String toDate, String status, String reqDept, String recDept){
		int result = 0;
		
		ReportDao dao = (ReportDao)getDao();
		
		try{
			result = dao.selectStatusReportListing(fromDate, toDate, status, reqDept, recDept, null, false, 0, -1).size();
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getStatusReportListingCount: " + e.getMessage());
		}
		
		return result;
	}

}
