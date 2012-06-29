package com.tms.collab.isr.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.stdui.FileUpload;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.jobs.CloseRequestJob;
import com.tms.collab.isr.jobs.DailyDigestJob;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;

public class RequestModel extends DefaultModule {
	public static final String ISR_GROUP = "aadm_isr";
	public static final String AUTOCLOSURE_DESC = "Auto-closure of ISR requests";
	public static final String DAILY_DIGEST_DESC = "ISR Daily Digest";
	public static final String AUTO_CLOSURE_NAME = "isr.autoClosure";
	public static final String DAILY_DIGEST_NAME = "isr.dailyDigest";
	
	public void init() {
		JobSchedule schedule = new JobSchedule(AUTO_CLOSURE_NAME, JobSchedule.DAILY);
		schedule.setStartTime(0, 0, 23, 1, 5, 2006);
		schedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
        schedule.setGroup(ISR_GROUP);
        
        int dailyDigestExecutionHour = 23;
        int dailyDigestExecutionMinute = 0;
        ConfigModel model = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
		ConfigDetailObject config = new ConfigDetailObject();
		Collection cols;
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.DAILY_DIGEST_EXECUTION_HOUR, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigDetailObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					dailyDigestExecutionHour = Integer.parseInt(config.getConfigDetailName());
				}
			}
		}
		cols = model.getConfigDetailsByType(ConfigDetailObject.DAILY_DIGEST_EXECUTION_MINUTE, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigDetailObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					dailyDigestExecutionMinute = Integer.parseInt(config.getConfigDetailName());
				}
			}
		}
        
        JobSchedule schedule2 = new JobSchedule(DAILY_DIGEST_NAME, JobSchedule.DAILY);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.YEAR, 2006);
        calendar.set(Calendar.HOUR_OF_DAY, dailyDigestExecutionHour);
        calendar.set(Calendar.MINUTE, dailyDigestExecutionMinute);
        calendar.set(Calendar.SECOND, 0);
        schedule2.setStartTime(calendar.getTime());
        schedule2.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
        schedule2.setGroup(ISR_GROUP);

        JobTask task = new CloseRequestJob();
        task.setName(AUTO_CLOSURE_NAME);
        task.setGroup(ISR_GROUP);
        task.setDescription(AUTOCLOSURE_DESC);
        
        JobTask dailyDigestTask = new DailyDigestJob();
        dailyDigestTask.setName(DAILY_DIGEST_NAME);
        dailyDigestTask.setGroup(ISR_GROUP);
        dailyDigestTask.setDescription(DAILY_DIGEST_DESC);

        SchedulingService service = (SchedulingService) Application.getInstance().getService(SchedulingService.class);

        try {
        	Log.getLog(getClass()).debug("Delete existing job and re-schedule a new one.");
            service.deleteJobTask(task);
            service.scheduleJob(task, schedule);
            
            service.deleteJobTask(dailyDigestTask);
            service.scheduleJob(dailyDigestTask, schedule2);
        } 
        catch (SchedulingException e) {
            Log.getLog(getClass()).error("Error removing job: " + task.getName(), e);
        }
	}
	
	public int insertRequest(RequestObject request, FileUpload[] attachments, Event event) {
		RequestDao dao = (RequestDao)getDao();
        int errorCode = 0;
		
		try {
            errorCode = dao.insertRequest(request, attachments, event);
		}
		catch(DaoException error) {
			errorCode = 1;
			Log.getLog(getClass()).error(error, error);
		}
		
		return errorCode;
	}
	
	public int cloneRequest(RequestObject relatedRequest, RequestObject duplicatingRequest) {
		RequestDao dao = (RequestDao)getDao();
        int errorCode = 0;
		
		try {
            errorCode = dao.cloneRequest(relatedRequest, duplicatingRequest);
		}
		catch(DaoException error) {
			errorCode = 1;
			Log.getLog(getClass()).error(error, error);
		}
		
		return errorCode;
	}
	
	public boolean uploadAttachments(HttpServletRequest httpRequest, String requestId, FileUpload[] attachments, Event event) {
		RequestDao dao = (RequestDao)getDao();
        boolean isSuccess = true;
		
		try {
            ArrayList duplicatedFiles = dao.uploadAttachments(requestId, attachments, event);
            if(duplicatedFiles != null) {
            	httpRequest.setAttribute("duplicatedFiles", duplicatedFiles);
            }
		}
		catch(DaoException error) {
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
	}
	
	public boolean uploadResolutionAttachments(HttpServletRequest httpRequest, String requestId, String suggestedResolutionId, FileUpload[] attachments, Event event) {
		RequestDao dao = (RequestDao)getDao();
        boolean isSuccess = true;
		
		try {
			ArrayList duplicatedFiles = dao.uploadResolutionAttachments(requestId, suggestedResolutionId, attachments, event);
			if(duplicatedFiles != null) {
            	httpRequest.setAttribute("duplicatedFiles", duplicatedFiles);
            }
		}
		catch(DaoException error) {
			isSuccess = false;
			Log.getLog(getClass()).error(error, error);
		}
		
		return isSuccess;
	}
	
	public Collection getResolutionAttachmentByUser(String requestId, String userId) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
            return dao.getResolutionAttachmentByUser(requestId, userId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public Collection getResolutionAttachmentBySuggestedResolution(String requestId, String suggestedResolutionId) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
            return dao.getResolutionAttachmentBySuggestedResolution(requestId, suggestedResolutionId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public String getDeptCode(String userId) {
		RequestDao dao = (RequestDao)getDao();
		String deptCode = null;
		
		try {
			deptCode = dao.getDeptCode(userId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
		}
		
		return deptCode;
	}
	
	public String getDeptName(String userId) {
		RequestDao dao = (RequestDao)getDao();
		String deptName = null;
		
		try {
			deptName = dao.getDeptName(userId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
		}
		
		return deptName;
	}
	
	public RequestObject getRequest(String requestId, boolean getRemarks, boolean remarksDesc, boolean getClarification, boolean clarificationDesc) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.getRequest(requestId, getRemarks, remarksDesc, getClarification, clarificationDesc);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public Collection getRelatedDept(String delimittedRelatedRequestIds) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.getRelatedDept(delimittedRelatedRequestIds);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public Collection selectExpiredCompletedRequest(String condition) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.selectExpiredCompletedRequest(condition);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public boolean updateRequest(String requestId, String fieldName, String value) {
		RequestDao dao = (RequestDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.updateRequest(requestId, fieldName, value);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	public boolean acceptRejectRequestResolution(String requestId, boolean isAccepted) {
		RequestDao dao = (RequestDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.acceptRejectRequestResolution(requestId, isAccepted);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	public boolean completeRequestResolution(String requestId) {
		RequestDao dao = (RequestDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.completeRequestResolution(requestId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	public int updateRequestStatus(String statusFrom, String statusTo, String[] ids) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.updateRequestStatus(statusFrom, statusTo, ids);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return 0;
		}
	}
	
	public Collection getAttachments(String requestId) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.getAttachments(requestId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public boolean insertRemarks(RemarksObject remarks) {
		RequestDao dao = (RequestDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.insertRemarks(remarks);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	public boolean updateDueDate(String requestId, Date dueDate) {
		RequestDao dao = (RequestDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.updateDueDate(requestId, dueDate);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	public AttachmentObject getAttachment(String attachmentId) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.getAttachment(attachmentId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public ResolutionAttachmentObject getResolutionAttachment(String resolutionAttachmentId) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.getResolutionAttachment(resolutionAttachmentId);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public Collection getRemarks(String requestId, boolean remarksDesc) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.getRemarks(requestId, remarksDesc);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public boolean insertClarification(ClarificationObject clarification) {
		RequestDao dao = (RequestDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.insertClarification(clarification);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	public boolean updateClarificationReply(ClarificationObject clarification) {
		RequestDao dao = (RequestDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.updateClarificationReply(clarification);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	public Collection getClarification(String requestId, boolean clarificationDesc) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.getClarification(requestId, clarificationDesc);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
	public boolean insertSuggestedResolution(SuggestedResolutionObject suggestedResolution) {
		RequestDao dao = (RequestDao)getDao();
		boolean isSuccess = true;
		
		try {
			dao.insertSuggestedResolution(suggestedResolution);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			isSuccess = false;
		}
		
		return isSuccess;
	}
    
	public Collection getSuggestedResolution(String requestId, boolean suggestedResolutionDesc) {
		RequestDao dao = (RequestDao)getDao();
		
		try {
			return dao.getSuggestedResolution(requestId, suggestedResolutionDesc);
		}
		catch(DaoException error) {
			Log.getLog(getClass()).error(error, error);
			return null;
		}
	}
	
    public Collection selectRequest(String searchValue, String statusId, String deptCode, 
            String sort, boolean desc, int start, int rows) {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectRequest(searchValue, statusId, deptCode, false, sort, desc, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection selectRequest(String searchValue, String statusId, String deptCode, boolean directedFromForceClosure,
        String sort, boolean desc, int start, int rows) {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectRequest(searchValue, statusId, deptCode, directedFromForceClosure, sort, desc, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public int selectRequestCount(String searchValue, String statusId, String deptCode) {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectRequestCount(searchValue, statusId, deptCode);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return 0;
        }
    }
    
    public Collection selectAttendingRequest(String searchValue, String searchAttribute, String statusId, String priorityByAdmin, String deptCode, String requestType, String assigneeName,
            String sort, boolean desc, int start, int rows) {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectAttendingRequest(searchValue, searchAttribute, statusId, priorityByAdmin, deptCode, requestType, assigneeName, false, sort, desc, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection selectAttendingRequest(String searchValue, String searchAttribute, String statusId, String priorityByAdmin, String deptCode, String requestType, String assigneeName, boolean directedFromForceClosure,
            String sort, boolean desc, int start, int rows) {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectAttendingRequest(searchValue, searchAttribute, statusId, priorityByAdmin, deptCode, requestType, assigneeName, directedFromForceClosure, sort, desc, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public int selectAttendingRequestCount(String searchValue, String searchAttribute, String statusId, String priorityByAdmin, String deptCode, String requestType, String assigneeName) {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectAttendingRequestCount(searchValue, searchAttribute, statusId, priorityByAdmin, deptCode, requestType, assigneeName);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return 0;
        }
    }
    
    public Collection selectDailyDigestRequestByStatus(String statusId, String userId, int start, int rows) {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectDailyDigestRequestByStatus(statusId, userId, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection selectDailyDigestRequesterPendingClarification(String userId, int start, int rows) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectDailyDigestRequesterPendingClarification(userId, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection selectDailyDigestAttendingRequestByStatus(String statusId, String userId, int start, int rows) {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectDailyDigestAttendingRequestByStatus(statusId, userId, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection selectDailyDigestAttendingRequestNewAssignment(String userId, int start, int rows) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectDailyDigestAttendingRequestNewAssignment(userId, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection selectDailyDigestAttendingRequestAnsweredClarification(String userId, int start, int rows) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectDailyDigestAttendingRequestAnsweredClarification(userId, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection selectDailyDigestReminder(String userId, int start, int rows) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectDailyDigestReminder(userId, start, rows);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public boolean insertAssignment(String requestId, Map assigneeMap) {
    	RequestDao dao = (RequestDao)getDao();
    	boolean isSuccess = true;
    	
    	try {
    		dao.insertAssignment(requestId, assigneeMap, false);
    	}
    	catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            isSuccess = false;
        }
    	
    	return isSuccess;
    }
    
    /**
     * Warning: This method will delete all the assignment records automatically inserted by the system 
     * 	for dept admin, at the moment when a new request is created.
     * 	This method is written to delete previously auto assigned records for the purpose of a CR raised.
     * 	Never simply call this method without understanding of its impact.
     * @return total number of rows deleted  
     */
    public int deleteAllAutoDeptAssignment() {
    	RequestDao dao = (RequestDao)getDao();
    	
    	try {
    		return dao.deleteAllAutoDeptAssignment();
    	}
    	catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return 0;
        }
    }
    
    public boolean insertAssignmentRemarks(AssignmentRemarksObject assignmentRemarks) {
    	RequestDao dao = (RequestDao)getDao();
    	boolean isSuccess = true;
    	
    	try {
    		dao.insertAssignmentRemarks(assignmentRemarks);
    	}
    	catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            isSuccess = false;
        }
    	
    	return isSuccess;
    }
    
    public Collection getAssignmentRemarks(String requestId, boolean assignmentRemarksDesc) {
    	RequestDao dao = (RequestDao)getDao();
    	
    	try {
    		return dao.getAssignmentRemarks(requestId, assignmentRemarksDesc);
    	}
    	catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection getFirstLevelAssignees(String requestId) {
    	RequestDao dao = (RequestDao)getDao();
    	
    	try {
    		return dao.getFirstLevelAssignees(requestId);
    	}
    	catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Collection getSecondLevelAssignees(String requestId) {
    	RequestDao dao = (RequestDao)getDao();
    	
    	try {
    		return dao.getSecondLevelAssignees(requestId);
    	}
    	catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public Map getDirectAssigneeMap(String userId, String requestId) {
    	RequestDao dao = (RequestDao)getDao();
    	Map assigneeMap = new SequencedHashMap();
    	
        try {
        	assigneeMap = dao.getDirectAssigneeMap(userId, requestId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
        }
        
        return assigneeMap;
    }
    
    public Collection selectAllStatus() {
        RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.selectAllStatus();
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public boolean withdrawRequest(String[] requestIds) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.withdrawRequest(requestIds);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return false;
        }
    }
    
    public StatusObject getRequestStatus(String requestId) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.getRequestStatus(requestId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
    
    public boolean isAccessibleRequest(String requestId, HttpServletRequest request) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.isAccessibleRequest(requestId, request);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return false;
        }
    }
    
    public boolean isDownloadableAttachment(String attachmentId, HttpServletRequest request) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.isDownloadableAttachment(attachmentId, request);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return false;
        }
    }
    
    public boolean isDownloadableResolutionAttachment(String resolutionAttachmentId, HttpServletRequest request) {
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.isDownloadableResolutionAttachment(resolutionAttachmentId, request);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return false;
        }
    }
    
    //added on 9 march 2007
   /*public Collection getRecipientEmailAdd( strRequestId){
    	RequestDao dao = (RequestDao)getDao();
        
        try {
            return dao.getRecipientEmailAdd(strRequestId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
    }
   
   public Collection getRequestorDeptAdminAssigneeEmailAdd(String strRequestId){
	 	RequestDao dao = (RequestDao)getDao();	 	
	 	  try {
	 		  		
	            return dao.getRequestorDeptAdminAssigneeEmailAdd(strRequestId);
	        }
	        catch(DaoException error) {
	            Log.getLog(getClass()).error(error, error);
	            return null;
	        }
   }*/
    
    //Remarks
  public Collection getDeptAdminAssigneeUserId (String strRequestId){     
     
       Collection colDeptAdmin = getDeptAdminEmailAdd(strRequestId);
       Collection colAssignee = getAssigneeEmailAdd(strRequestId); 
       return checkDuplicatedUserId(colDeptAdmin, colAssignee);
                     
  }
  //new request
  public Collection getMutlipleDeptAdminEmailAdd(String strRequestId) {
	  RequestDao dao = (RequestDao)getDao();	 	
 	  try { 		  		
            return dao.getMutlipleDeptAdminEmailAdd(strRequestId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        } 
  }
 
  public Collection getDeptAdminEmailAdd(String strRequestId){
	  RequestDao dao = (RequestDao)getDao();	 	
 	  try { 		  		
            return dao.getDeptAdminEmailAdd(strRequestId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        } 	
    }
  
  public Collection getRequestorEmailAdd(String strRequestId){	  
	  RequestDao dao = (RequestDao)getDao();	 	
 	  try { 		  		
            return dao.getRequestorEmailAdd(strRequestId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
  }
  	  
  public Collection getAssigneeEmailAdd(String strRequestId){
	  RequestDao dao = (RequestDao)getDao();	 	
 	  try { 		  		
            return dao.getAssigneeEmailAdd(strRequestId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error(error, error);
            return null;
        }
  }
  private Collection checkDuplicatedUserId(Collection colFirst, Collection colSecond){

	  Collection colRecipient = new ArrayList();
	  if(colFirst  != null &&  colFirst.size() > 0){	
	    	for (Iterator i=colFirst.iterator(); i.hasNext();) {
	    		HashMap map = (HashMap)i.next();
	    		colRecipient.add(map);
	    	}
	  }
	  
	  if(colSecond  != null &&  colSecond.size() > 0){
	 	ArrayList list = new ArrayList();			
			 		
		 	int i=0;      		     		
		   	for (Iterator iii=colSecond.iterator(); iii.hasNext();) {
		   		boolean boolSameId = false;
		 		HashMap map1 = (HashMap)iii.next();

		 		//filter duplicate userId
		 		for (Iterator ii=colRecipient.iterator(); ii.hasNext();) {
		 			HashMap map = (HashMap)ii.next();
		 			if(map1.get("userId").toString().equals(map.get("userId").toString())){
		 			
		 				boolSameId = true;
		 				break;
		 			} 				
		 			i++;
		 		}     			
		 		i = 0;    
		 		if(!boolSameId){
		 			colRecipient.add(map1);
		 		}
		   	}
	  }
		
	  if (colRecipient != null && colRecipient.size() > 0)
		  return colRecipient;
	  return null;
  }
   
  public Collection getAssigneeUserId (String strRequestId){
	  
	 	RequestDao dao = (RequestDao)getDao();	 	
	 	  try {
	 		  		
	            return dao.getAssigneeUserId(strRequestId);
	        }
	        catch(DaoException error) {
	            Log.getLog(getClass()).error(error, error);
	            return null;
	        }
  }
	public void updateClarificationRecipientId(ClarificationObject clarification){
		RequestDao dao = (RequestDao)getDao();	 	
	 	  try {
	 		  dao.updateClarificationRecipientId(clarification);
	        }
	        catch(DaoException error) {
	            Log.getLog(getClass()).error(error, error);	          
	        }
	}
	
	public Collection getClarificationRecipientId(ClarificationObject clarification){
		RequestDao dao = (RequestDao)getDao();	 	
	 	  try {
	 		  return dao.getClarificationRecipientId(clarification);
	        }
	        catch(DaoException error) {
	            Log.getLog(getClass()).error(error, error);
	          return null;
	        }
	}
	
	public Collection getSystemAdminUserId(){
		RequestDao dao = (RequestDao)getDao();	 	
	 	  try {
	 		  return dao.getSystemAdminUserId();
	        }
	        catch(DaoException error) {
	            Log.getLog(getClass()).error(error, error);
	          return null;
	        }
	}
	
	public void updateExistingTable(){
		RequestDao dao = (RequestDao)getDao();	 	
	 	  try {
	 		 dao.updateExistingTable();
	        }
	        catch(DaoException error) {
	            Log.getLog(getClass()).error(error, error);
	        }
	}
}
