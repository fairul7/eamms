package com.tms.report.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

public class ReportModule extends DefaultModule {
	
	public Collection getContentUsageReportDetails(String sectionId, String groupId, Date startDate, Date endDate ){
		ReportDao dao = (ReportDao)getDao();
		Collection col = new ArrayList();
		
		try {
			col =  dao.getContentUsageReportDetails(sectionId, groupId, startDate, endDate);
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getContentUsageReport(): ",e);
        }
		return col;
	}
	
	public Collection getContentSubmittedReportDetails(String sectionId, String groupId, Date startDate, Date endDate ){
		ReportDao dao = (ReportDao)getDao();
		Collection col = new ArrayList();
		
		try {
			col =  dao.getContentSubmittedReportDetails(sectionId, groupId, startDate, endDate);
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getContentUsageReport(): ",e);
        }
		return col;
	}
	
	public Collection getExternalSourcesReport(Date startDate, Date endDate){
		
		ReportDao dao = (ReportDao)getDao();
		Collection col = new ArrayList();
		
		try {
			col =  dao.getExternalResourcesData(startDate, endDate);
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getContentUsageReport(): ",e);
        }
		return col;
		
	}
	
	public Map getResourceUsageReport(Date startDate, Date endDate, String category, int startTime, int endTime){
		
		ReportDao dao = (ReportDao)getDao();
		Collection col = new ArrayList();
		Map reportMap = new SequencedHashMap();
		
		try {
			col =  dao.getResourceUsageData(startDate, endDate, category);
			
			Calendar calStartDate = Calendar.getInstance();
			calStartDate.setTime(startDate);
			calStartDate.set(Calendar.HOUR, startTime);
			
			Calendar calEndDate = Calendar.getInstance();
			calEndDate.setTime(endDate);
			calEndDate.set(Calendar.HOUR, endTime);
			
			Calendar tempCalStartDate = Calendar.getInstance();
			Calendar tempCalEndDate = Calendar.getInstance();
			Calendar tempCal = Calendar.getInstance();
			
			int tempStartTime = 0;
			int tempEndTime = 0;
			int tempCalStartDateDay = 0;
			int tempCalEndDateDay = 0;
			int tempCalDay = 0;
			
			StringBuffer key = new StringBuffer();
			
			for(Iterator i=col.iterator(); i.hasNext(); ){
				
				ReportObject obj = (ReportObject)i.next();
				
				tempCalStartDate.setTime(obj.getStartDate());
				tempCalEndDate.setTime(obj.getEndDate());
				
				//trim the data before selected start date
				if(tempCalStartDate.before(calStartDate)){
					tempCalStartDate = (Calendar)calStartDate.clone();
					tempCalStartDate.set(Calendar.HOUR_OF_DAY, startTime);
				}
				//trim the data after selected end date
				if(tempCalEndDate.after(calEndDate)){
					tempCalEndDate = (Calendar)calEndDate.clone();
					tempCalEndDate.set(Calendar.HOUR_OF_DAY, endTime);
				}
				
				//debug data
				tempCalStartDateDay = tempCalStartDate.get(Calendar.DAY_OF_YEAR);
				tempCalEndDateDay = tempCalEndDate.get(Calendar.DAY_OF_YEAR);
				
				if(tempCalStartDateDay == tempCalEndDateDay){ //start date and end date same
					
					tempStartTime = tempCalStartDate.get(Calendar.HOUR_OF_DAY);
					tempEndTime = tempCalEndDate.get(Calendar.HOUR_OF_DAY);
					
					for(int j=tempStartTime; j<tempEndTime; j++){
						key = new StringBuffer();
						key.append(obj.getResourceId() + "_" + j);
						
						Integer count = (Integer)reportMap.get(key.toString());
						if(count != null){
	        				int countInt = count.intValue();
	            			countInt++;
	            			count = new Integer(countInt);
	        			}else{
	        				count = new Integer(1);
	        			}
	        			
						reportMap.put(key.toString(), count);
						
					}
					
				}else{	//different start date and end date
					
					tempCal = (Calendar)tempCalStartDate.clone();
					
					tempCalDay = tempCal.get(Calendar.DAY_OF_YEAR);
					tempCalStartDateDay = tempCalStartDate.get(Calendar.DAY_OF_YEAR);
					tempCalEndDateDay = tempCalEndDate.get(Calendar.DAY_OF_YEAR);
					
					while(tempCalDay <= tempCalEndDateDay){
						
						if(tempCalDay == tempCalStartDateDay){
							
							tempStartTime = tempCalStartDate.get(Calendar.HOUR_OF_DAY);
							tempEndTime = endTime;
							
						}
						else if(tempCalDay == tempCalEndDateDay){
							
							tempStartTime = startTime;
							tempEndTime = tempCalEndDate.get(Calendar.HOUR_OF_DAY);
							
						}else{
							
							tempStartTime = startTime;
							tempEndTime = endTime;
							
						}
						
						for(int j=tempStartTime; j<tempEndTime; j++){
							key = new StringBuffer();
							key.append(obj.getResourceId() + "_" + j);
							
							Integer count = (Integer)reportMap.get(key.toString());
							if(count != null){
		        				int countInt = count.intValue();
		            			countInt++;
		            			count = new Integer(countInt);
		        			}else{
		        				count = new Integer(1);
		        			}
		        			
							reportMap.put(key.toString(), count);
							
						}
						
						tempCalDay++;
						tempCal.add(Calendar.DATE, 1);
					}
					
					
				}
				
				
			}
			
			
			
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getResourceUsageReport(): ",e);
        }
		return reportMap;
		
	}
	
	public Collection getContentSummaryReport(String[][] contentArr, Date startDate, Date endDate, String groupId){
		
		ReportDao dao = (ReportDao)getDao();
		Collection rawResult = new ArrayList();
		Collection result = new ArrayList();
		Map contentMap = new SequencedHashMap();
		ReportContentObject tempObj = new ReportContentObject();
		
		for(int i=0; i<contentArr.length; i++){
			ReportContentObject obj = new ReportContentObject();
			obj.setContentName(contentArr[i][0]);
			contentMap.put(contentArr[i][1], obj);
		}
		
		try {
			rawResult = dao.getContentSummaryDate(contentArr, startDate, endDate, groupId);
			String contentType = new String();
			int index = 0;
			
			for(Iterator i=rawResult.iterator(); i.hasNext(); ){
				HashMap row = (HashMap)i.next();
				
				index = row.get("id").toString().indexOf("_");
				contentType = row.get("id").toString().substring(0, index+1);
				
				tempObj = (ReportContentObject)contentMap.get(contentType);
				
				if(tempObj != null){
					
					if(row.get("event").toString().equals("Submit")){
						tempObj.setSubmittedCount(tempObj.getSubmittedCount()+1);
						
					}else if(row.get("event").toString().equals("Publish")){
						tempObj.setPublishCount(tempObj.getPublishCount()+1);
						
					}else if(row.get("event").toString().equals("Approve")){
						tempObj.setApprovedBySupervisorCount(tempObj.getApprovedBySupervisorCount()+1);
                        //Removing kz specific checks
						/*if(row.get("message") != null && row.get("message").toString().length() > 0){
							if(row.get("message").toString().indexOf("ASA-") >= 0){
								tempObj.setApprovedBySupervisorCount(tempObj.getApprovedBySupervisorCount()+1);
							}else if(row.get("message").toString().indexOf("AEA-") >= 0){
								tempObj.setApprovedByEditorCount(tempObj.getApprovedByEditorCount()+1);
							}else if(row.get("message").toString().indexOf("ACA-") >= 0){
								tempObj.setApprovedByComplianceCount(tempObj.getApprovedByComplianceCount()+1);
							}
						}*/
						
					}
					
					contentMap.put(contentType, tempObj);
					
				}
				
			}
			
			int mapsize = contentMap.size();

			Iterator keyValuePairs1 = contentMap.entrySet().iterator();
			for (int i = 0; i < mapsize; i++)
			{
				  Map.Entry entry = (Map.Entry) keyValuePairs1.next();
				  result.add(entry.getValue());
			}
			
			return result;
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getContentSummaryReport(): ",e);
            return null;
        }
		
	}
	
	public ReportContentObject getContentSummaryReport(String contentName, String contentPrefix, Date startDate, Date endDate){
		
		ReportDao dao = (ReportDao)getDao();
		
		try {
			return  dao.getContentSummaryData(contentName, contentPrefix, startDate, endDate);
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getContentSummaryReport(): ",e);
            return null;
        }
		
	}
	
	public Collection getContentUsageReport(String parentName, String group, String parentId, Date startDate, Date endDate){
		
		ReportDao dao = (ReportDao)getDao();
		Collection col = new ArrayList();
		
		try {
			col =  dao.getContentUsageData(parentName, group, parentId, startDate, endDate);
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getContentUsageReport(): ",e);
        }
		return col;
		
	}
	
	public Collection getContentSubmittedReport(String parentName, String group, String parentId, Date startDate, Date endDate){
		
		ReportDao dao = (ReportDao)getDao();
		Collection col = new ArrayList();
		
		try {
			col =  dao.getContentSubmittedData(parentName, group, parentId, startDate, endDate);
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getContentSubmittedReport(): ",e);
        }
		return col;
	}
	
	public String[] getUserDivision(){
		ReportDao dao = (ReportDao)getDao();
		Collection col = new ArrayList();
		String[] division = null;
		try {
			col = dao.getUserDivision();
			division = new String[col.size()];
			int counter = 0;
			
			for(Iterator i=col.iterator(); i.hasNext(); ){
				HashMap hmap = (HashMap)i.next();
                String strDivision = (String)hmap.get("division");
                
                division[counter] = strDivision;
                counter++;
			}
			
		}catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getUserDivision(): ",e);
        }
		
		return division;
	}
	
    public ReportObject[] getReportUserMap(Date startDate, Date endDate) {
        ReportDao dao = (ReportDao)getDao();
        ReportObject[] objects=null;
        //Map map = null;
        try {
            Collection col = dao.getReportUserList(startDate,endDate);
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            if (col!=null && col.size()>0) {
                objects = new ReportObject[col.size()];
                int iCur = 0;
               // map = new SequencedHashMap();
                for (Iterator i=col.iterator();i.hasNext();) {
                    HashMap hmap = (HashMap)i.next();
                    String sUserId = (String)hmap.get("userId");
                    int iTotal = dao.getTotalLoginPerUser(startDate,endDate,sUserId);
                    User user = null;

                    try {
                        user = service.getUser(sUserId);
                    }
                    catch(Exception e) {

                    }
                    objects[iCur] = new ReportObject();
                    if (user==null) {
                        user = new User();
                        user.setId(sUserId);
                        user.setUsername("-");
                    }
                    objects[iCur].setUser(user);
                    objects[iCur].setTotalLogin(iTotal);
                    iCur++;
                    //map.put("userId",sUserId);
                    //map.put(sUserId,iTotal+"");
                }
            }
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getReportUserMap(): ",e);
        }


        return objects;
    }

    public ReportObject[] getReportModuleMap(Date startDate, Date endDate) {
        ReportDao dao = (ReportDao)getDao();
        //Map map = null;
        ReportObject[] objects=null;
        try {
            Collection col = dao.getReportModuleList(startDate,endDate);
            if (col!=null && col.size()>0) {
                //map = new SequencedHashMap();
                objects = new ReportObject[col.size()];
                int iCur=0;
                for (Iterator i=col.iterator();i.hasNext();) {
                    HashMap hmap = (HashMap)i.next();
                    String module = (String)hmap.get("module");
                    //map.put("module",module);
                    int iUniqueCount = dao.getTotalUniqueCountPerModule(startDate,endDate,module);
                    //map.put("u"+module,iUniqueCount+"");
                    int iTotalCount = dao.getTotalCountPerModule(startDate,endDate,module);
                    //map.put("t"+module,iTotalCount+"");

                    objects[iCur]=new ReportObject();
                    objects[iCur].setModule(module);
                    objects[iCur].setModuleName(Application.getInstance().getMessage(module));
                    objects[iCur].setUniqueCount(iUniqueCount);
                    objects[iCur].setTotalCount(iTotalCount);
                    iCur++;
                }
            }

        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getReportModuleMap(): ",e);
        }
        return objects;
    }

    public Collection getGroupsByUser(String userId) {
        ReportDao dao = (ReportDao)getDao();
        Collection col = null;
        try {
            col = dao.selectGroupsByUser(userId);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("error in getGroupsByUser():",e);
        }
        return col;
    }

    public Collection getModulesByGroup(String groupId) {
        ReportDao dao = (ReportDao)getDao();
        Collection col = null;
        try {
            col = dao.selectDistinctModules(groupId);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("error in getModulesByGroup():",e);
        }
        return col;
    }

    public Collection getPermissionByGroupAndModule(String groupId, String moduleId) {
        ReportDao dao = (ReportDao)getDao();
        Collection col = null;
        try {
            col = dao.selectPermissionForGroupAndModule(groupId,moduleId);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("error in getPermissionForGroupAndModule():",e);
        }
        return col;
    }
}
