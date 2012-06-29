package com.tms.collab.isr.model;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.FileUpload;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.util.Transaction;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.collab.isr.util.HtmlFormatting;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class RequestDao extends DataSourceDao {
	public void init() {
		try {
			super.update("CREATE TABLE isr_request (" +
					"requestId varchar(255) NOT NULL," +
					"requestFromDept varchar(255)," +
					"requestToDept varchar(255)," +
					"requestSubject varchar(100)," +
					"requestDescription mediumtext," +
					"requestPriority varchar(50) DEFAULT ''," +
					"requestPriorityByAdmin varchar(50) DEFAULT ''," +
					"requestStatus varchar(2)," +
					"requestType varchar(50) DEFAULT '" + Application.getInstance().getMessage("isr.label.requestTypeDefaultOption") + "'," +
					"requestResolution mediumtext," +
					"requestResolutionAccepted varchar(1) DEFAULT '0'," +
					"requestResolutionDate datetime," +
					"dueDate datetime," +
					"dateCreated datetime," +
					"createdBy varchar(255)," +
					"lastUpdatedDate datetime," +
					"lastUpdatedBy varchar(255)," +
					"PRIMARY KEY(requestId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		try {
			super.update("ALTER TABLE isr_request " +
					"ADD dueDate datetime ",null);
		}
		catch(DaoException e) {
			// do nothing
		}
		try {
			super.update("ALTER TABLE isr_request " +
					"ADD requestType varchar(50) DEFAULT '" + Application.getInstance().getMessage("isr.label.requestTypeDefaultOption") + "'", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("ALTER TABLE isr_request " +
					"ADD relatedRequests text DEFAULT ''", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("ALTER TABLE isr_request " +
					"ADD createdById varchar(255)", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("CREATE TABLE isr_attachment (" +
					"attachmentId varchar(255) NOT NULL," +
					"requestId varchar(255) NOT NULL," +
					"fileName varchar(255)," +
					"oriFileName varchar(255)," +
					"dateCreated datetime," +
					"createdBy varchar(255)," +
					"PRIMARY KEY(attachmentId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("CREATE TABLE isr_resolution_attachment (" +
					"resolutionAttachmentId varchar(255) NOT NULL," +
					"requestId varchar(255) NOT NULL," +
					"suggestedResolutionId varchar(255)," +
					"fileName varchar(255)," +
					"oriFileName varchar(255)," +
					"dateCreated datetime," +
					"createdBy varchar(255)," +
					"PRIMARY KEY(resolutionAttachmentId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("CREATE TABLE isr_remarks (" +
					"remarksId varchar(255) NOT NULL," +
					"requestId varchar(255) NOT NULL," +
					"remarks mediumtext," +
					"dateCreated datetime," +
					"createdBy varchar(255)," +
					"PRIMARY KEY(remarksId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("CREATE TABLE isr_clarification (" +
					"clarificationId varchar(255) NOT NULL," +
					"requestId varchar(255) NOT NULL," +
					"clarificationQuestion mediumtext," +
					"clarificationAnswer mediumtext," +
					"dateCreated datetime," +
					"createdBy varchar(255)," +
					"lastUpdatedDate datetime," +
					"lastUpdatedBy varchar(255)," +
					"PRIMARY KEY(clarificationId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("ALTER TABLE isr_clarification " +
					"ADD createdById varchar(255) ",null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("CREATE TABLE isr_assignment (" +
					"assignmentId varchar(255) NOT NULL, " +
					"requestId varchar(255) NOT NULL, " +
					"assigneeUserId varchar(255) NOT NULL, " +
					"adminType varchar(100), " +
					"active char(1), " +
					"dateCreated datetime, " +
					"createdBy varchar(255), " +
					"PRIMARY KEY(assignmentId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("CREATE TABLE isr_assignment_remarks (" +
					"assignmentRemarksId varchar(255) NOT NULL, " +
					"requestId varchar(255) NOT NULL, " +
					"assignmentRemarks mediumtext, " +
					"dateCreated datetime, " +
					"createdBy varchar(255), " +
					"PRIMARY KEY(assignmentRemarksId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		try {
			super.update("CREATE TABLE isr_suggested_resolution (" +
					"suggestedResolutionId varchar(255) NOT NULL, " +
					"requestId varchar(255) NOT NULL, " +
					"resolution mediumtext, " +
					"dateCreated datetime, " +
					"createdBy varchar(255), " +
					"PRIMARY KEY(suggestedResolutionId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
		
		//Update Existing Table
		try {
			updateExistingTable();
		} catch (DaoException e) {
			
		}
	}
	
	/**
	 * Create new request
	 * @param request The object of type isr.model.RequestObject
	 * @param attachments Array of attachments, some or all could be empty value
	 * @param event
	 * @return Returns 0 if no error; 1 if exception caught while creating new request; 2 if exception caught while uploading attachment 
	 * @throws DaoException
	 */
	public int insertRequest(RequestObject request, FileUpload[] attachments, Event event) throws DaoException {
		Application application = Application.getInstance();
        StringBuffer sql = new StringBuffer();
		int errorCode = 0; // 0 if no error; 
							//1 if exception caught while creating new request; 
							//2 if exception caught while uploading attachment
		
		sql.append("INSERT INTO isr_request (" +
				"requestId, requestFromDept, requestToDept, " +
				"requestSubject, requestDescription, requestPriority, requestPriorityByAdmin, " +
				"requestStatus, requestType, requestResolution, requestResolutionAccepted, " +
				"dateCreated, createdBy, createdById, lastUpdatedDate, lastUpdatedBy, dueDate) VALUES (" +
				"#requestId#, #requestFromDept#, #requestToDept#, " +
				"#requestSubject#, #requestDescription#, #requestPriority#, #requestPriorityByAdmin#, " +
				"#requestStatus#, #requestType#, #requestResolution#, #requestResolutionAccepted#, " +
				"now(), #createdBy#, #createdById#, now(), #lastUpdatedBy#, #dueDate#)");
		
		User user = application.getCurrentUser();
        String userId = user.getId();
        String userName = user.getName();
        if("".equals(request.getCreatedById())) {
        	request.setCreatedById(userId);
        }
        if("".equals(request.getCreatedBy())) {
        	request.setCreatedBy(userName);
        }
        if("".equals(request.getLastUpdatedBy())) {
        	request.setLastUpdatedBy(userName);
        }
        if("".equals(request.getRequestStatus())) {
        	request.setRequestStatus(StatusObject.STATUS_ID_NEW);
        }
        
        Map attendingDepts = request.getRequestMultipleToDept();
        ArrayList relatedRequests = new ArrayList();
        int count=0;
        String newRequestId ="";
        for(Iterator itr=attendingDepts.keySet().iterator(); itr.hasNext(); count++) {
        	if("".equals(newRequestId)){
        		newRequestId=getNewRequestId();
        	}else{
        		String check=newRequestId.substring(newRequestId.length()-2,newRequestId.length());
        		try{
        	        if(Integer.parseInt(check)>0)
        	        	newRequestId+="a";
        			}catch (Exception e) {
        				if("z".equals(check.substring(check.length()-1,check.length()))){
        					newRequestId+="a";
        				}else{
        	           char checkers=check.charAt(check.length()-1);
        	            int alphabet=checkers+1;        	            
        	            newRequestId=newRequestId.substring(0,newRequestId.length()-1)+new Character((char)alphabet).toString();
        				}
        	        }   		
        	}
            if(newRequestId != null) {
    	        request.setRequestId(newRequestId);
    	        request.setRequestToDept(itr.next().toString());
    	        try {
    	        	super.update(sql.toString(), request);
    	        	relatedRequests.add(request.getRequestId());
    	        	
    	        	/*// Add relevant dept admin into assignment table as well
    	        	String allAttendingDeptStaff = "";
    	        	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	        	Collection departmentCountryAssociativityCol = orgChartHandler.selectDepartmentCountryAssociativity(request.getRequestToDept(), null, null, null, false, 0, -1);
    	        	if(departmentCountryAssociativityCol != null) {
    	        		if(departmentCountryAssociativityCol.size() > 0) {
    	        			DepartmentCountryAssociativityObject departmentCountryAssociativityObject = (DepartmentCountryAssociativityObject) departmentCountryAssociativityCol.iterator().next();
    	        			Collection staffHierachyCols = orgChartHandler.findAllHierachy(null, 0, -1, null, false, true, 
    	        					departmentCountryAssociativityObject.getDeptCode(), departmentCountryAssociativityObject.getCountryCode(), null);
    	        			if(staffHierachyCols != null) {
    	        				if(staffHierachyCols.size() > 0) {
    	        					int count = 0;
    	        					for(Iterator itr=staffHierachyCols.iterator(); itr.hasNext(); count++) {
    	        						StaffHierachy staffHierachy = (StaffHierachy) itr.next();
    	        						if(count == 0)
    	        							allAttendingDeptStaff += "('" + staffHierachy.getUserId() + "'";
    	        						if(count != 0)
    	        							allAttendingDeptStaff += ",'" + staffHierachy.getUserId() + "'";
    	        					}
    	        				}
    	        			}
    	        		}
    	        	}
    	        	
    	        	if(allAttendingDeptStaff != null && !"".equals(allAttendingDeptStaff)) {
    	        		allAttendingDeptStaff += ")";
    	        		Collection assignedDeptAdmin = super.select("select distinct groupUser.userId userId " +
    	        				"from isr_group_user groupUser, isr_group isrgroup " +
    	        				"where isrgroup.role = ? " +
    	        				"and groupUser.groupId = isrgroup.id " +
    	        				"and groupUser.userId in " + allAttendingDeptStaff, HashMap.class, new Object[] {ISRGroup.ROLE_DEPT_ADMIN}, 0, -1);
    	        		if(assignedDeptAdmin != null) {
    	        			if(assignedDeptAdmin.size() > 0) {
    	        				Map assignedDeptAdminMap = new SequencedHashMap();
    	        				for(Iterator itr=assignedDeptAdmin.iterator(); itr.hasNext(); ) {
    	        					HashMap map = (HashMap) itr.next();
    	        					assignedDeptAdminMap.put(map.get("userId").toString(), map.get("userId").toString());
    	        				}
    	        				insertAssignment(request.getRequestId(), assignedDeptAdminMap, true);
    	        			}
    	        		}
    	        	}*/
    	        }
    	        catch(DaoException error) {
    	        	Log.getLog(getClass()).error(error, error);
    	        	return 1;
    	        }
    	        
    	        LogModel logModel = (LogModel) Application.getInstance().getModule(LogModel.class);
    	        LogObject log = new LogObject();
    	        log.setRequestId(request.getRequestId());
    	        log.setLogAction(LogObject.LOG_ACTION_TYPE_NEW);
    	        log.setLogDescription(LogObject.LOG_DESC_NEW_REQUEST);
    	        logModel.insertLog(log);
    	        
    	        try {
    	        	AttachmentObject attachment = new AttachmentObject();
    	        	attachment.setRequestId(request.getRequestId());
    	        	attachment.setCreatedBy(userId);
    	        	
                    StorageService storage = (StorageService) application.getService(StorageService.class);
                    if(attachments !=null){
    	        	for(int i=0; i<attachments.length; i++) {
    	        		StorageFile sf = attachments[i].getStorageFile(event.getRequest());
                        if(sf != null) {
                        	attachment.setOriFileName(sf.getName());
                        	sf.setName(formatAttachmentFileName(sf.getName()));
                            StorageFile file = new StorageFile("/" + AttachmentObject.STORAGE_ROOT + "/" + request.getRequestId(), sf);
                            storage.store(file);
                            attachment.setFileName(sf.getName());
                            
                            String newId = UuidGenerator.getInstance().getUuid();
                            attachment.setAttachmentId(newId);
        	        		attachment.setRequestId(request.getRequestId());
        	        		insertAttachment(attachment);
                        }
    	        	}
    	        }
    	        }
    	        catch(Exception error) {
    	        	Log.getLog(getClass()).error(error, error);
    	        	return 2;
    	        }
            }
            else {
            	Log.getLog(getClass()).error("Exception caught while composing new requestId");
            	return 1;
            }
        }
        
        if(attendingDepts.size() > 1)
        	insertRelatedRequests(relatedRequests);
        
        return errorCode;
	}
	
	public int cloneRequest(RequestObject relatedRequest, RequestObject duplicatingRequest) throws DaoException {
		Application application = Application.getInstance();
        StringBuffer sql = new StringBuffer();
		int errorCode = 0; // 0 if no error; 
							//1 if exception caught while creating new request; 
							//2 if exception caught while uploading attachment
		
		sql.append("INSERT INTO isr_request (" +
				"requestId, requestFromDept, requestToDept, " +
				"requestSubject, requestDescription, requestPriority, requestPriorityByAdmin, " +
				"requestStatus, requestType, requestResolution, requestResolutionAccepted, " +
				"dateCreated, createdBy, lastUpdatedDate, lastUpdatedBy,dueDate) VALUES (" +
				"#requestId#, #requestFromDept#, #requestToDept#, " +
				"#requestSubject#, #requestDescription#, #requestPriority#, #requestPriorityByAdmin#, " +
				"#requestStatus#, #requestType#, #requestResolution#, #requestResolutionAccepted#, " +
				"now(), #createdBy#, now(), #lastUpdatedBy#,#dueDate#)");
		
		User user = application.getCurrentUser();
        String userId = user.getId();
        String userName = user.getName();
        if("".equals(duplicatingRequest.getCreatedBy())) {
        	duplicatingRequest.setCreatedBy(userName);
        }
        if("".equals(duplicatingRequest.getLastUpdatedBy())) {
        	duplicatingRequest.setLastUpdatedBy(userName);
        }
        if("".equals(duplicatingRequest.getRequestStatus())) {
        	duplicatingRequest.setRequestStatus(StatusObject.STATUS_ID_NEW);
        }
        
        // The newly added attending depts
        Map attendingDepts = duplicatingRequest.getRequestMultipleToDept();
        
        // Trace a list of inter-related requests, including the existing relationship
        ArrayList relatedRequests = new ArrayList();
        relatedRequests.add(relatedRequest.getRequestId());
        String delimittedRelatedRequestIds = relatedRequest.getRelatedRequests();
        if(delimittedRelatedRequestIds != null && !"".equals(delimittedRelatedRequestIds)) {
	        // Currently, the relatedRequest has only 1 related request
			if(delimittedRelatedRequestIds.indexOf(",") == -1) {
				relatedRequests.add(delimittedRelatedRequestIds);
			}
			// Currently, the relatedRequest has more than 1 related request
			else {
				StringTokenizer stringTokenizer = new StringTokenizer(delimittedRelatedRequestIds, ",");
				while(stringTokenizer.hasMoreElements()) {
					relatedRequests.add(stringTokenizer.nextElement().toString());
				}
			}
        }
		
        int count=0;
        for(Iterator itr=attendingDepts.keySet().iterator(); itr.hasNext(); count++) {
    			String newRequestId="";
    			for(int j=0; j<relatedRequests.size(); j++) {
    				if(newRequestId.length()<relatedRequests.get(j).toString().length())
    				{
    					newRequestId=relatedRequests.get(j).toString();
    				}else if(newRequestId.length()==relatedRequests.get(j).toString().length()){
    					int newIdChecker=newRequestId.charAt(newRequestId.length()-1);
    					int relatedChecker=relatedRequests.get(j).toString().charAt(relatedRequests.get(j).toString().length()-1);
    					if(newIdChecker<relatedChecker){
    						newRequestId=relatedRequests.get(j).toString();
    					}
    				}  				
    			}
    			try{
    				String check=newRequestId.substring(newRequestId.length()-2,newRequestId.length());
        	        if(Integer.parseInt(check)>0){
        	        	if(relatedRequest.getRequestId().length()==newRequestId.length()){
        	        		newRequestId=relatedRequest.getRequestId()+"a";
        	        	}else{
        	        	newRequestId+="a";
        	        	}
        	        }
        			}catch (Exception e) {
    			if("z".equals(newRequestId.substring(newRequestId.length()-1,newRequestId.length()))){
    				newRequestId+="a";
				}else{
	           char checkers=newRequestId.charAt(newRequestId.length()-1);
	            int alphabet=checkers+1;        	            
	            newRequestId=newRequestId.substring(0,newRequestId.length()-1)+new Character((char)alphabet).toString();
				}
        		}
    			
            if(newRequestId != null) {
    	        duplicatingRequest.setRequestId(newRequestId);
    	        duplicatingRequest.setRequestToDept(itr.next().toString());
    	        try {
    	        	super.update(sql.toString(), duplicatingRequest);
    	        	relatedRequests.add(duplicatingRequest.getRequestId());
    	        }
    	        catch(DaoException error) {
    	        	Log.getLog(getClass()).error(error, error);
    	        	return 1;
    	        }
    	        
    	        LogModel logModel = (LogModel) Application.getInstance().getModule(LogModel.class);
    	        LogObject log = new LogObject();
    	        log.setRequestId(duplicatingRequest.getRequestId());
    	        log.setLogAction(LogObject.LOG_ACTION_TYPE_NEW);
    	        log.setLogDescription(LogObject.LOG_DESC_NEW_REQUEST);
    	        logModel.insertLog(log);
    	        
    	        // Now, copy the existing attachments from relatedRequest
    	        try {
    	        	AttachmentObject attachment = new AttachmentObject();
    	        	attachment.setRequestId(duplicatingRequest.getRequestId());
    	        	attachment.setCreatedBy(userId);
    	        	
    	        	Collection existingAttachments = relatedRequest.getAttachments();
                    StorageService storage = (StorageService) application.getService(StorageService.class);
                    StorageFile tempSf;
                    StorageFile newSf;
                    for(Iterator attachmentItr=existingAttachments.iterator(); attachmentItr.hasNext();) {
                    	AttachmentObject tempAttachment = (AttachmentObject) attachmentItr.next();
                    	tempSf = new StorageFile("/" + AttachmentObject.STORAGE_ROOT + "/" + relatedRequest.getRequestId() + "/" + tempAttachment.getFileName());
                    	newSf = new StorageFile("/" + AttachmentObject.STORAGE_ROOT + "/" + duplicatingRequest.getRequestId(), tempSf);
                    	storage.store(newSf);
                    	
                    	attachment.setOriFileName(tempAttachment.getOriFileName());
                    	attachment.setFileName(newSf.getName());
                    	String newId = UuidGenerator.getInstance().getUuid();
                        attachment.setAttachmentId(newId);
                        insertAttachment(attachment);
                    }
    	        }
    	        catch(Exception error) {
    	        	Log.getLog(getClass()).error(error, error);
    	        	return 2;
    	        }
            }
            else {
            	Log.getLog(getClass()).error("Exception caught while composing new requestId");
            	return 1;
            }
        }
        
        insertRelatedRequests(relatedRequests);
        
        return errorCode;
	}
	
	private String formatAttachmentFileName(String oriFileName) {
		String newFileName = "";
		
		DateFormat dmyDateFormat = new SimpleDateFormat("yyyyMMddkkmmss");
		String fileNameWithoutExt = oriFileName.substring(0, oriFileName.lastIndexOf("."));
		String fileExt = oriFileName.substring(oriFileName.lastIndexOf("."), oriFileName.length());
		
		newFileName = fileNameWithoutExt + " " + dmyDateFormat.format(new Date()) + fileExt;
		
		return newFileName;
	}
    
    public void insertAttachment(AttachmentObject attachment) throws DaoException {
        User user = Application.getInstance().getCurrentUser();
        String userId = user.getId();
        if("".equals(attachment.getCreatedBy())) {
            attachment.setCreatedBy(userId);
        }
        if("".equals(attachment.getAttachmentId())) {
            String newId = UuidGenerator.getInstance().getUuid();
            attachment.setAttachmentId(newId);
        }
        
        super.update("INSERT INTO isr_attachment " +
                "(attachmentId, requestId, fileName, oriFileName, dateCreated, createdBy) " +
                "VALUES (#attachmentId#, #requestId#, #fileName#, #oriFileName#, now(), #createdBy#)", attachment);
    }
    
    public ArrayList uploadAttachments(String requestId, FileUpload[] attachments, Event event) throws DaoException {
    	ArrayList duplicatedFiles = null;
    	
    	try {
    		Application application = Application.getInstance();
    		String userId = application.getCurrentUser().getId();
        	AttachmentObject attachment = new AttachmentObject();
        	attachment.setRequestId(requestId);
        	attachment.setCreatedBy(userId);
        	
            StorageService storage = (StorageService) application.getService(StorageService.class);
            
        	for(int i=0; i<attachments.length; i++) {
        		StorageFile sf = attachments[i].getStorageFile(event.getRequest());
                if(sf != null) {
                	if(!isDuplicatedAttachment(requestId, sf.getName())) {
	                	attachment.setOriFileName(sf.getName());
	                	sf.setName(formatAttachmentFileName(sf.getName()));
	                    StorageFile file = new StorageFile("/" + AttachmentObject.STORAGE_ROOT + "/" + requestId, sf);
	                    storage.store(file);
	                    
	                    String newId = UuidGenerator.getInstance().getUuid();
	                    attachment.setAttachmentId(newId);
	                    attachment.setFileName(sf.getName());
	                    insertAttachment(attachment);
                	}
                	else {
                		if(duplicatedFiles == null) {
                			duplicatedFiles = new ArrayList();
                		}
                		duplicatedFiles.add(sf.getName());
                	}
                }
        	}
        }
        catch(Exception error) {
        	throw new DaoException(error);
        }
        
        return duplicatedFiles;
    }
    
    private boolean isDuplicatedAttachment(String requestId, String oriFilename) throws DaoException {
    	boolean isDuplicatedAttachment = false;
    	
    	Collection col = super.select("SELECT attachmentId FROM isr_attachment " +
    			"WHERE requestId = ? AND oriFileName = ?", HashMap.class, new Object[] {requestId, oriFilename}, 0, 1);
    	if(col != null) {
    		if(col.size() > 0) {
    			isDuplicatedAttachment = true;
    		}
    	}
    	
    	return isDuplicatedAttachment;
    }
    
    private boolean insertRelatedRequests(ArrayList multipleRequest) {
    	boolean isSuccess = true;
    	
    	if(multipleRequest != null) {
	    	try {
	    		for(int i=0; i<multipleRequest.size(); i++) {
	    			String relatedDeptString = "";
	    			for(int j=0; j<multipleRequest.size(); j++) {
	    				if(i != j) {
	    					if(relatedDeptString.length() != 0)
	    						relatedDeptString += ",";
	    					relatedDeptString += multipleRequest.get(j).toString();
	    				}
	    			}
	    			
	    			super.update("UPDATE isr_request " +
	    					"SET relatedRequests = ? WHERE requestId = ? ", new Object[] {relatedDeptString, multipleRequest.get(i).toString()});
	    		}
	    	}
	    	catch(DaoException error) {
	    		isSuccess = false;
	    		Log.getLog(getClass()).error(error, error);
	    	}
    	}
    	
    	return isSuccess;
    }
    
    public void insertResolutionAttachment(ResolutionAttachmentObject attachment) throws DaoException {
        User user = Application.getInstance().getCurrentUser();
        String userId = user.getId();
        if("".equals(attachment.getCreatedBy())) {
            attachment.setCreatedBy(userId);
        }
        if("".equals(attachment.getResolutionAttachmentId())) {
            String newId = UuidGenerator.getInstance().getUuid();
            attachment.setResolutionAttachmentId(newId);
        }
        
        super.update("INSERT INTO isr_resolution_attachment " +
                "(resolutionAttachmentId, requestId, suggestedResolutionId, fileName, oriFileName, dateCreated, createdBy) " +
                "VALUES (#resolutionAttachmentId#, #requestId#, #suggestedResolutionId#, #fileName#, #oriFileName#, now(), #createdBy#)", attachment);
    }
    
    public ArrayList uploadResolutionAttachments(String requestId, String suggestedResolutionId, FileUpload[] attachments, Event event) throws DaoException {
    	ArrayList duplicatedFiles = null;
    	
    	try {
    		Application application = Application.getInstance();
    		String userId = application.getCurrentUser().getId();
        	ResolutionAttachmentObject attachment = new ResolutionAttachmentObject();
        	attachment.setRequestId(requestId);
        	if(suggestedResolutionId != null &&
        			!"".equals(suggestedResolutionId)) {
        		attachment.setSuggestedResolutionId(suggestedResolutionId);
        	}
        	attachment.setCreatedBy(userId);
        	
            StorageService storage = (StorageService) application.getService(StorageService.class);
            
        	for(int i=0; i<attachments.length; i++) {
        		StorageFile sf = attachments[i].getStorageFile(event.getRequest());
                if(sf != null) {
                	if(!isDuplicatedResolutionAttachment(requestId, sf.getName())) {
	                	attachment.setOriFileName(sf.getName());
	                	sf.setName(formatAttachmentFileName(sf.getName()));
	                    StorageFile file = new StorageFile("/" + ResolutionAttachmentObject.STORAGE_ROOT + "/" + requestId + "/" + ResolutionAttachmentObject.SUB_FOLDER + "/", sf);
	                    storage.store(file);
	                    
	                    String newId = UuidGenerator.getInstance().getUuid();
	                    attachment.setResolutionAttachmentId(newId);
	                    attachment.setFileName(sf.getName());
	                    insertResolutionAttachment(attachment);
                	}
                	else {
                    	if(duplicatedFiles == null) {
                    		duplicatedFiles = new ArrayList();
                    	}
                    	duplicatedFiles.add(sf.getName());
                    }
                }
        	}
        }
        catch(Exception error) {
        	throw new DaoException(error);
        }
        
        return duplicatedFiles;
    }
	
    private boolean isDuplicatedResolutionAttachment(String requestId, String oriFilename) throws DaoException {
    	boolean isDuplicatedResolutionAttachment = false;
    	
    	Collection col = super.select("SELECT resolutionAttachmentId FROM isr_resolution_attachment " +
    			"WHERE requestId = ? AND oriFileName = ?", HashMap.class, new Object[] {requestId, oriFilename}, 0, 1);
    	if(col != null) {
    		if(col.size() > 0) {
    			isDuplicatedResolutionAttachment = true;
    		}
    	}
    	
    	return isDuplicatedResolutionAttachment;
    }
    
	private String getNewRequestId() throws DaoException {
		String deptCode = null;
		String yearCode = null;
		String runningAlphabet = null;
		String runningNumber = null;
		
		String newRequestId = null;
		String latestRequestId = null;
		Date maxDate = null;
		String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		String userId = Application.getInstance().getCurrentUser().getId();
		deptCode = getDeptCode(userId);
		
		DateFormat yyDateFormat = new SimpleDateFormat("yy");
		Date now = new Date();
		yearCode = yyDateFormat.format(now);
		
		Collection maxDateSelect = super.select("SELECT requestId " +
				"FROM isr_request " +
				"WHERE requestId like '" + deptCode + "%' ORDER BY requestId DESC", HashMap.class, null, 0, 1);
		if(maxDateSelect != null) {
			if(maxDateSelect.size() != 0) {
				HashMap map = (HashMap) maxDateSelect.iterator().next();
				latestRequestId = map.get("requestId").toString();
			}
		}
		if(latestRequestId != null) {
			String check=latestRequestId.substring(latestRequestId.length()-2,latestRequestId.length());
			try{
				int testId=Integer.parseInt(check);			
			}catch(Exception e){
				boolean oneAlphabet=false;
				try{
					int testId=Integer.parseInt(check.substring(0,check.length()-1));	
					oneAlphabet=true;
				}catch(Exception ex){
					
				}
				if(oneAlphabet)
					latestRequestId=latestRequestId.substring(0,latestRequestId.length()-1);
				else
					latestRequestId=latestRequestId.substring(0,latestRequestId.length()-2);
					
			}
		
			int dateSequenceBeginsAt = latestRequestId.length() - 8;
			String currentDateSequence = latestRequestId.substring(dateSequenceBeginsAt, dateSequenceBeginsAt + 2);
			
			if(!currentDateSequence.equals(yearCode)) {
				runningAlphabet = "A";
				runningNumber = "00001";
			}
			else {
				int sequencialSequenceBeginsAt = latestRequestId.length() - 6;
				String currentAlphabetSequence = latestRequestId.substring(sequencialSequenceBeginsAt, sequencialSequenceBeginsAt + 1);
				long currentNumericalSequence = Long.parseLong(latestRequestId.substring(sequencialSequenceBeginsAt + 1, latestRequestId.length()));
				
				if(currentNumericalSequence == 99999) {
					int currentAlphabetSequenceIndex = alphabets.indexOf(currentAlphabetSequence);
					runningAlphabet = alphabets.substring(currentAlphabetSequenceIndex + 1, currentAlphabetSequenceIndex + 2);
					runningNumber = "00001";
				}
				else {
					runningAlphabet = currentAlphabetSequence;
					NumberFormat zeroFillingFormat = new DecimalFormat("00000");
					
					runningNumber = zeroFillingFormat.format(currentNumericalSequence + 1);
				}
			}
		}
		else {
			runningAlphabet = "A";
			runningNumber = "00001";
		}
		
		if(deptCode != null &&
				yearCode != null &&
				runningAlphabet != null &&
				runningNumber != null) {
			newRequestId = deptCode + yearCode + runningAlphabet + runningNumber;
		}
		
		return newRequestId;
	}
	
	public String getDeptCode(String userId) throws DaoException {
		String deptCode = null;
		
		Collection deptCodeSelect = super.select("SELECT deptCode FROM org_chart_hierachy " +
				"WHERE userId = ?", HashMap.class, new Object[] {userId}, 0, 1);
		if(deptCodeSelect != null) {
			if(deptCodeSelect.size() > 0) {
				HashMap map = (HashMap) deptCodeSelect.iterator().next();
				deptCode = map.get("deptCode").toString();
			}
		}
		
		return deptCode;
	}
	
	public String getDeptName(String userId) throws DaoException {
		String deptName = null;
		
		Collection deptNameSelect = super.select("SELECT concat(country.shortDesc, ' - ', dept.shortDesc) deptName " +
				"FROM org_chart_hierachy hierachy, org_chart_department dept, org_chart_country country " +
				"WHERE hierachy.userId = ? " +
				"AND hierachy.deptCode = dept.code " +
				"AND hierachy.countryCode = country.code ", HashMap.class, new Object[] {userId}, 0, 1);
		if(deptNameSelect != null) {
			if(deptNameSelect.size() > 0) {
				HashMap map = (HashMap) deptNameSelect.iterator().next();
				deptName = map.get("deptName").toString();
			}
		}
		
		return deptName;
	}
    
	public RequestObject getRequest(String requestId, boolean getRemarks, boolean remarksDesc, boolean getClarification, boolean clarificationDesc) throws DaoException {
		RequestObject requestObject = null;
		
		if(requestId != null && 
				!"".equals(requestId)) {
			StringBuffer selectRequest = new StringBuffer("");
			selectRequest.append("SELECT requestId, request.dueDate, " +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "relatedRequests, " +
	                "requestResolution, requestResolutionAccepted, requestResolutionDate, " +
	                "dateCreated, createdBy, " +
	                "lastUpdatedDate, lastUpdatedBy " +
	                "FROM isr_request request, " +
	                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
	                "org_chart_department fromDept, org_chart_department toDept, " +
	                "org_chart_country fromCountry, org_chart_country toCountry, " +
	                "isr_status status " +
	                "WHERE requestFromDept = fromDeptMapping.associativityId " +
	                "AND fromDeptMapping.deptCode = fromDept.code " +
	                "AND fromDeptMapping.countryCode = fromCountry.code " +
	                "AND requestToDept = toDeptMapping.associativityId " +
	                "AND toDeptMapping.deptCode = toDept.code " +
	                "AND toDeptMapping.countryCode = toCountry.code " +
	                "AND requestStatus = status.statusId " +
	                "AND requestId = ?");
			
			Collection requestCol = super.select(selectRequest.toString(), RequestObject.class, new Object[] {requestId}, 0, 1);
			if(requestCol != null) {
				if(requestCol.size() > 0) {
					requestObject = (RequestObject) requestCol.iterator().next();
					requestObject.setAttachments(getAttachments(requestObject.getRequestId()));
					requestObject.setResolutionAttachments(getConsolidatedResolutionAttachment(requestObject.getRequestId()));
					if(getRemarks)
						requestObject.setRemarks(getRemarks(requestObject.getRequestId(), remarksDesc));
					if(getClarification)
						requestObject.setClarification(getClarification(requestObject.getRequestId(), clarificationDesc));
					
					requestObject.setRelatedRequestsCol(populateRelatedRequests(requestObject.getRelatedRequests()));
				}
			}
		}
		
		return requestObject;
	}
	
	public Collection populateRelatedRequests(String delimittedRelatedRequestIds) throws DaoException {
		Collection relatedRequestsCol = null;
		
		if(delimittedRelatedRequestIds != null && !"".equals(delimittedRelatedRequestIds)) {
			String sqlInClauseIds = "";
			// This request has only 1 related request
			if(delimittedRelatedRequestIds.indexOf(",") == -1) {
				sqlInClauseIds = "'" + delimittedRelatedRequestIds + "'";
			}
			// This request has only more than 1 related request
			else {
				StringTokenizer stringTokenizer = new StringTokenizer(delimittedRelatedRequestIds, ",");
				while(stringTokenizer.hasMoreElements()) {
					if(sqlInClauseIds.length() > 0)
						sqlInClauseIds += ",";
					sqlInClauseIds += "'" + stringTokenizer.nextElement().toString() + "'";
				}
			}
			
			StringBuffer selectRequest = new StringBuffer("");
			selectRequest.append("SELECT requestId, request.dueDate, " +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "relatedRequests, " +
	                "requestResolution, requestResolutionAccepted, requestResolutionDate, " +
	                "dateCreated, createdBy, " +
	                "lastUpdatedDate, lastUpdatedBy " +
	                "FROM isr_request request, " +
	                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
	                "org_chart_department fromDept, org_chart_department toDept, " +
	                "org_chart_country fromCountry, org_chart_country toCountry, " +
	                "isr_status status " +
	                "WHERE requestFromDept = fromDeptMapping.associativityId " +
	                "AND fromDeptMapping.deptCode = fromDept.code " +
	                "AND fromDeptMapping.countryCode = fromCountry.code " +
	                "AND requestToDept = toDeptMapping.associativityId " +
	                "AND toDeptMapping.deptCode = toDept.code " +
	                "AND toDeptMapping.countryCode = toCountry.code " +
	                "AND requestStatus = status.statusId " +
	                "AND requestId IN (" + sqlInClauseIds + ")");
			
			relatedRequestsCol = super.select(selectRequest.toString(), RequestObject.class, null, 0, -1);
			if(relatedRequestsCol != null) {
				if(relatedRequestsCol.size() > 0) {
					Application application = Application.getInstance();
					String userId = application.getCurrentUser().getId();
					PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
					HttpServletRequest httpRequest = Application.getThreadRequest();
					OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
			    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
			    	
					for(Iterator itr=relatedRequestsCol.iterator(); itr.hasNext(); ) {
						RequestObject requestObject = (RequestObject) itr.next();
						requestObject.setResolutionAttachments(getConsolidatedResolutionAttachment(requestObject.getRequestId()));
						
						if(requestObject.getRequestFromDept().equals(associatedCountryDept.getAssociativityId())) {
							String editRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/requestorEditRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
													+ requestObject.getRequestId() + "</a>";
							String resolveRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/requestorResolveRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
													+ requestObject.getRequestId() + "</a>";
							String viewRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/requestorViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
													+ requestObject.getRequestId() + "</a>";
	
							if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
									StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
									StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
									StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
								if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#990000; font-weight:bold;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			if(StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:red\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			
								if(permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST)) {
									requestObject.setRequestIdRequestorUrl(editRequestUrl);
								}
								else if(permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
									requestObject.setRequestIdRequestorUrl(viewRequestUrl);
								}
								else {
									requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
								}
							}
							else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus())) {
								requestObject.setRequestStatusName("<span style=\"color:#6099BF;\">" + requestObject.getRequestStatusName() + "</span>");
								
								if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST)) {
									requestObject.setRequestIdRequestorUrl(resolveRequestUrl);
								}
								else if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
										permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
									requestObject.setRequestIdRequestorUrl(viewRequestUrl);
								}
								else {
									requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
								}
							}
							else if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
									StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
								if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			if(StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			
								if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
										permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
									requestObject.setRequestIdRequestorUrl(viewRequestUrl);
								}
								else {
									requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
								}
							}
							else {
								requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
							}
						}
						else if(requestObject.getRequestToDept().equals(associatedCountryDept.getAssociativityId())){
							String processRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/attendantProcessRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
							+ requestObject.getRequestId() + "</a>";
							String viewRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/attendantViewRequest.jsp?requestId=" + requestObject.getRequestId()+ "\">"
							+ requestObject.getRequestId() + "</a>";
							if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
			        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
			        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
			        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
			        			if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#990000; font-weight:bold;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			if(StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:red\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			requestObject.setRequestIdRequestorUrl(processRequestUrl);
			        		}
			        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
			        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
			        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
			        			if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#6099BF;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			if(StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
			        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
			        			}
			        			else {
			        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
			        			}
			        		}
			        		else {
			        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
			        		}
						}
						else{
							
							String viewRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/attendantViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"+ requestObject.getRequestId() + "</a>";
							if (permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) 
								requestObject.setRequestIdRequestorUrl(viewRequestUrl);
							else
								requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
							
							if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
									StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
									StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
									StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
								if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#990000; font-weight:bold;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			if(StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:red\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
							}
							else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus())) {
								requestObject.setRequestStatusName("<span style=\"color:#6099BF;\">" + requestObject.getRequestStatusName() + "</span>");
							}
							else if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
									StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
								if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
			        			if(StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
			        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
			        			}
							}
						}
					}
				}
			}
		}
		
		return relatedRequestsCol;
	}
	
	public Collection getRelatedDept(String delimittedRelatedRequestIds) throws DaoException {
		if(delimittedRelatedRequestIds != null && !"".equals(delimittedRelatedRequestIds)) {
			String sqlInClauseIds = "";
			// This request has only 1 related request
			if(delimittedRelatedRequestIds.indexOf(",") == -1) {
				sqlInClauseIds = "'" + delimittedRelatedRequestIds + "'";
			}
			// This request has only more than 1 related request
			else {
				StringTokenizer stringTokenizer = new StringTokenizer(delimittedRelatedRequestIds, ",");
				while(stringTokenizer.hasMoreElements()) {
					if(sqlInClauseIds.length() > 0)
						sqlInClauseIds += ",";
					sqlInClauseIds += "'" + stringTokenizer.nextElement().toString() + "'";
				}
			}
			return super.select("SELECT associativityId, deptCode, countryCode " +
					"FROM org_chart_dept_country, isr_request " +
					"WHERE requestId IN (" + sqlInClauseIds + ") " +
					"AND requestToDept = associativityId", DepartmentCountryAssociativityObject.class, null, 0, -1);
		}
		else {
			return null;
		}
	}
	
	public Collection selectExpiredCompletedRequest(String condition ) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT requestId, dueDate," +
	                "requestFromDept, " +
	                "requestToDept, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "dateCreated, createdBy, " +
	                "lastUpdatedDate, lastUpdatedBy " +
	                "FROM isr_request " +
	                "WHERE requestStatus =  '" + StatusObject.STATUS_ID_COMPLETED + "' " + 
	                "AND TO_DAYS(NOW()) - TO_DAYS(requestResolutionDate) " + condition);
		
		return super.select(sql.toString(), RequestObject.class, null, 0, -1);
	}
	
	public void updateRequest(String requestId, String fieldName, String value) throws DaoException {
		super.update("UPDATE isr_request " +
				"SET " + fieldName + " = ?, " +
				"lastUpdatedDate = now(), " +
				"lastUpdatedBy = ? " +
				"WHERE requestId = ?", new Object[] {value, Application.getInstance().getCurrentUser().getUsername(), requestId});
	}
	
	public void updateRequestLastUpdatedDate(String requestId) throws DaoException {
		super.update("UPDATE isr_request " +
				"SET lastUpdatedDate = now(), " +
				"lastUpdatedBy = ? " +
				"WHERE requestId = ?", new Object[] {Application.getInstance().getCurrentUser().getUsername(), requestId});
	}
	
	public void acceptRejectRequestResolution(String requestId, boolean isAccepted) throws DaoException {
		StringBuffer sqlUpdateStatus = new StringBuffer("UPDATE isr_request " +
				"SET requestStatus = ?, " +
				"requestResolutionAccepted = ? " +
				"WHERE requestId = ?");
		
		if(isAccepted) {
			super.update(sqlUpdateStatus.toString(), new Object[] {StatusObject.STATUS_ID_CLOSE, "1", requestId});
			
			LogModel logModel = (LogModel) Application.getInstance().getModule(LogModel.class);
	        LogObject log = new LogObject();
	        log.setRequestId(requestId);
	        log.setLogAction(LogObject.LOG_ACTION_TYPE_STATUS);
	        log.setLogDescription(LogObject.LOG_DESC_STATUS_UPDATE_CLOSE);
	        logModel.insertLog(log);
		}
		else {
			super.update(sqlUpdateStatus.toString(), new Object[] {StatusObject.STATUS_ID_REOPEN, "0", requestId});
			
			LogModel logModel = (LogModel) Application.getInstance().getModule(LogModel.class);
	        LogObject log = new LogObject();
	        log.setRequestId(requestId);
	        log.setLogAction(LogObject.LOG_ACTION_TYPE_STATUS);
	        log.setLogDescription(LogObject.LOG_DESC_STATUS_UPDATE_REOPEN);
	        logModel.insertLog(log);
		}
		
		updateRequestLastUpdatedDate(requestId);
	}
	
	public void completeRequestResolution(String requestId) throws DaoException {
		super.update("UPDATE isr_request " +
				"SET requestStatus = ?, requestResolutionDate = now() " +
				"WHERE requestId = ?", new Object[] {StatusObject.STATUS_ID_COMPLETED, requestId});
		
		LogModel logModel = (LogModel) Application.getInstance().getModule(LogModel.class);
        LogObject log = new LogObject();
        log.setRequestId(requestId);
        log.setLogAction(LogObject.LOG_ACTION_TYPE_STATUS);
        log.setLogDescription(LogObject.LOG_DESC_STATUS_UPDATE_COMPLETED);
        logModel.insertLog(log);
        
        updateRequestLastUpdatedDate(requestId);
	}
	
	public int updateRequestStatus(String statusFrom, String statusTo, String[] ids) throws DaoException{
		Collection paramList = new ArrayList(); 
		StringBuffer sql = new StringBuffer("UPDATE isr_request " +
				"SET lastUpdatedDate = ?, " +
				"lastUpdatedBy = ?, " +
				"requestStatus = ? " +
				"WHERE requestStatus = ? " +
				"AND requestId IN (");
		
		Date now = Calendar.getInstance().getTime();
		paramList.add(now);
		paramList.add(Application.getInstance().getCurrentUser() != null ? Application.getInstance().getCurrentUser().getUsername() : "systemScheduler");
		paramList.add(statusTo);
		paramList.add(statusFrom);
		
		for(int i=0; i<ids.length; i++){
			if(i>0)
				sql.append(",");
			sql.append("?");
			paramList.add(ids[i]);
		}
		
		sql.append(") ");
		
		return super.update(sql.toString(), paramList.toArray());
	}
	
	public AttachmentObject getAttachment(String attachmentId) throws DaoException {
		AttachmentObject attachmentObject = null;
		
		Collection attachmentCols = super.select("SELECT attachmentId, " +
				"requestId, " +
				"fileName, " +
				"oriFileName, " +
				"dateCreated, " +
				"dateCreated " +
				"FROM isr_attachment " +
				"WHERE attachmentId = ?", AttachmentObject.class, new Object[] {attachmentId}, 0, 1);
		
		if(attachmentCols != null) {
			if(attachmentCols.size() > 0) {
				attachmentObject = (AttachmentObject) attachmentCols.iterator().next();
			}
		}
		
		return attachmentObject;
	}
	
	public Collection getAttachments(String requestId) throws DaoException {
		if(requestId != null && 
				!"".equals(requestId)) {
			return super.select("SELECT attachmentId, requestId, " +
					"fileName, oriFileName, " +
					"dateCreated, createdBy " +
					"FROM isr_attachment " +
					"WHERE requestId = ? " +
					"ORDER BY dateCreated", AttachmentObject.class, new Object[] {requestId}, 0, -1);
		}
		else {
			return null;
		}
	}
	
	public Collection getConsolidatedResolutionAttachment(String requestId) throws DaoException {
		return super.select("SELECT resolutionAttachmentId, requestId, suggestedResolutionId, " +
				"fileName, oriFileName, dateCreated, createdBy " +
				"FROM isr_resolution_attachment " +
				"WHERE requestId = ? " +
				"AND suggestedResolutionId = ''", ResolutionAttachmentObject.class, new Object[] {requestId}, 0, -1);
	}
	
	public ResolutionAttachmentObject getResolutionAttachment(String resolutionAttachmentId) throws DaoException {
		ResolutionAttachmentObject attachmentObject = null;
		
		Collection resolutionAttachmentCols = super.select("SELECT resolutionAttachmentId, requestId, suggestedResolutionId, " +
				"fileName, oriFileName, dateCreated, createdBy " +
				"FROM isr_resolution_attachment " +
				"WHERE resolutionAttachmentId = ? ", ResolutionAttachmentObject.class, new Object[] {resolutionAttachmentId}, 0, -1);
		if(resolutionAttachmentCols != null) {
			if(resolutionAttachmentCols.size() > 0) {
				attachmentObject = (ResolutionAttachmentObject) resolutionAttachmentCols.iterator().next();
			}
		}
		 
		return attachmentObject;
	}
	
	public Collection getResolutionAttachmentByUser(String requestId, String userId) throws DaoException {
		return super.select("SELECT resolutionAttachmentId, requestId, suggestedResolutionId, " +
				"fileName, oriFileName, dateCreated, createdBy " +
				"FROM isr_resolution_attachment " +
				"WHERE requestId = ? " +
				"AND createdBy = ?", ResolutionAttachmentObject.class, new Object[] {requestId, userId}, 0, -1);
	}
	
	public Collection getResolutionAttachmentBySuggestedResolution(String requestId, String suggestedResolutionId) throws DaoException {
    	return super.select("SELECT resolutionAttachmentId, requestId, suggestedResolutionId, " +
    			"fileName, oriFileName, " +
    			"dateCreated, createdBy " +
    			"FROM isr_resolution_attachment " +
    			"WHERE requestId = ? " +
    			"AND suggestedResolutionId = ?", ResolutionAttachmentObject.class, new Object[] {requestId, suggestedResolutionId}, 0, -1);
    }
	
	public void insertRemarks(RemarksObject remarks) throws DaoException {
		Application application = Application.getInstance();
		String userName = application.getCurrentUser().getName();
		
		if("".equals(remarks.getCreatedBy())) {
			remarks.setCreatedBy(userName);
        }
		if(remarks.getRemarksId().equals("")) {
            String newId = UuidGenerator.getInstance().getUuid();
            remarks.setRemarksId(newId);
        }
		
		super.update("INSERT INTO isr_remarks (" +
				"remarksId, requestId, remarks, " +
				"dateCreated, createdBy) VALUES (" +
				"#remarksId#, #requestId#, #remarks#, " +
				"now(), #createdBy#)", remarks);
		
		updateRequestLastUpdatedDate(remarks.getRequestId());
	}
	
	public void updateDueDate(String requestId, Date dueDate) throws DaoException {
		super.update("UPDATE isr_request " +
				"SET dueDate = ? WHERE requestId = ? ", new Object[] {dueDate, requestId});
	}
	
	public Collection getRemarks(String requestId, boolean remarksDesc) throws DaoException {
		if(requestId != null && 
				!"".equals(requestId)) {
			return super.select("SELECT remarksId, requestId, remarks, " +
					"dateCreated, createdBy " +
					"FROM isr_remarks " +
					"WHERE requestId = ? " +
					"ORDER BY dateCreated " + (remarksDesc? "desc" : ""), RemarksObject.class, new Object[] {requestId}, 0, -1);
		}
		else {
			return null;
		}
	}
	
	public void insertClarification(ClarificationObject clarification) throws DaoException {
		Application application = Application.getInstance();
		String userName = application.getCurrentUser().getName();
		
		if("".equals(clarification.getCreatedBy())) {
			clarification.setCreatedBy(userName);
        }
		if("".equals(clarification.getLastUpdatedBy())) {
			clarification.setLastUpdatedBy(userName);
        }
		if(clarification.getClarificationId().equals("")) {
            String newId = UuidGenerator.getInstance().getUuid();
            clarification.setClarificationId(newId);
        }
		
		super.update("INSERT INTO isr_clarification (" +
				"clarificationId, requestId, " +
				"clarificationQuestion, clarificationAnswer, " +
				"dateCreated, createdBy,  " +
				"lastUpdatedDate, lastUpdatedBy) VALUES (" +
				"#clarificationId#, #requestId#, " +
				"#clarificationQuestion#, #clarificationAnswer#, " +
				"now(), #createdBy#, " +
				"now(), #lastUpdatedBy#)", clarification);
		
		updateRequestLastUpdatedDate(clarification.getRequestId());
	}
	

	public void updateClarificationReply(ClarificationObject clarification) throws DaoException {
		Application application = Application.getInstance();
		String userName = application.getCurrentUser().getName();
		
		if("".equals(clarification.getLastUpdatedBy())) {
			clarification.setLastUpdatedBy(userName);
        }
		
		super.update("UPDATE isr_clarification " +
				"SET clarificationAnswer = #clarificationAnswer#, " +
				"lastUpdatedDate = now(), " +
				"lastUpdatedBy = #lastUpdatedBy# " +
				"WHERE clarificationId = #clarificationId#", clarification);
		
		updateRequestLastUpdatedDate(clarification.getRequestId());
	}
	
	public Collection getClarification(String requestId, boolean clarificationDesc) throws DaoException {
		if(requestId != null && 
				!"".equals(requestId)) {
			return super.select("SELECT clarificationId, requestId, " +
					"clarificationQuestion, clarificationAnswer, " +
					"dateCreated, createdBy, " +
					"lastUpdatedDate, lastUpdatedBy " +
					"FROM isr_clarification " +
					"WHERE requestId = ? " +
					"ORDER BY dateCreated " + (clarificationDesc? "desc" : ""), ClarificationObject.class, new Object[] {requestId}, 0, -1);
		}
		else {
			return null;
		}
	}
	
	public void insertSuggestedResolution(SuggestedResolutionObject suggestedResolution) throws DaoException {
		Application application = Application.getInstance();
		String userName = application.getCurrentUser().getName();
		
		if("".equals(suggestedResolution.getCreatedBy())) {
			suggestedResolution.setCreatedBy(userName);
        }
		if("".equals(suggestedResolution.getSuggestedResolutionId())) {
			String newId = UuidGenerator.getInstance().getUuid();
			suggestedResolution.setSuggestedResolutionId(newId);
		}
		
		super.update("INSERT INTO isr_suggested_resolution (" +
				"suggestedResolutionId, requestId, resolution, " +
				"dateCreated, createdBy) VALUES (" +
				"#suggestedResolutionId#, #requestId#, #resolution#, " +
				"now(), #createdBy#)", suggestedResolution);
		
		updateRequestLastUpdatedDate(suggestedResolution.getRequestId());
	}
	
	public Collection getSuggestedResolution(String requestId, boolean suggestedResolutionDesc) throws DaoException {
		if(requestId != null && 
				!"".equals(requestId)) {
			return super.select("SELECT suggestedResolutionId, requestId, resolution, " +
					"dateCreated, createdBy " +
					"FROM isr_suggested_resolution " +
					"WHERE requestId = ? " +
					"ORDER BY dateCreated " + (suggestedResolutionDesc? "desc" : ""), SuggestedResolutionObject.class, new Object[] {requestId}, 0, -1);
		}
		else {
			return null;
		}
	}
	
    public Collection selectRequest(String searchValue, String statusId, String deptCode, boolean directedFromForceClosure,
            String sort, boolean desc, int start, int rows) throws DaoException {
    	Application application = Application.getInstance();
    	String userId = application.getCurrentUser().getId();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
	        StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT requestId, request.dueDate," +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "dateCreated, createdBy, " +
	                "lastUpdatedDate, lastUpdatedBy " +
	                "FROM isr_request request, " +
	                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
	                "org_chart_department fromDept, org_chart_department toDept, " +
	                "org_chart_country fromCountry, org_chart_country toCountry, " +
	                "isr_status status " +
	                "WHERE requestFromDept = fromDeptMapping.associativityId " +
	                "AND fromDeptMapping.deptCode = fromDept.code " +
	                "AND fromDeptMapping.countryCode = fromCountry.code " +
	                "AND requestToDept = toDeptMapping.associativityId " +
	                "AND toDeptMapping.deptCode = toDept.code " +
	                "AND toDeptMapping.countryCode = toCountry.code " +
	                "AND requestStatus = status.statusId " +
	                "AND requestFromDept = '" + associatedCountryDept.getAssociativityId() + "' ");
	        if(searchValue != null && 
	                !"".equals(searchValue)) {
	            selectSql.append("AND (requestId LIKE '%" + searchValue + "%' ");
	            selectSql.append("OR requestSubject LIKE '%" + searchValue + "%') ");
	        }
	        if(statusId != null &&
	                !"".equals(statusId)) {
	        	if("active".equalsIgnoreCase(statusId)) {
	        		selectSql.append("AND (requestStatus = '" + StatusObject.STATUS_ID_NEW + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_REOPEN + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_CLARIFICATION + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_IN_PROGRESS + "') ");
	        	}
	        	else {
	        		selectSql.append("AND requestStatus = '" + statusId + "' ");
	        	}
	        }
	        if(deptCode != null &&
	                !"".equals(deptCode)) {
	            selectSql.append("AND requestToDept = '" + deptCode + "' ");
	        }
	        
	        if(sort != null &&
	                !"".equals(sort)) {
	        	if(sort.equals("requestIdRequestorUrl")) {
	        		selectSql.append("ORDER BY requestId ");
	        	}
	        	else {
	        		selectSql.append("ORDER BY " + sort + " ");
	        	}
	        }
	        else {
	            selectSql.append("ORDER BY status.statusOrder, dateCreated ");
	            desc = true;
	        }
	        if(desc) {
	            selectSql.append("desc ");
	        }
	        
	        Collection cols = super.select(selectSql.toString(), RequestObject.class, null, start, rows);
	        if(cols != null) {
	        	
	        	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
	        	HttpServletRequest httpRequest = Application.getThreadRequest();
	        	
	        	for(Iterator i=cols.iterator(); i.hasNext();) {
	        		RequestObject requestObject = (RequestObject) i.next();
	        		requestObject.setRequestSubject(HtmlFormatting.getEscapedXmlText(requestObject.getRequestSubject()));
	        		
	        		String editRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/requestorEditRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String resolveRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/requestorResolveRequest.jsp?requestId=" + requestObject.getRequestId() + (directedFromForceClosure? "&backToForceClosure=true" : "") + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String viewRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/requestorViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		
	        		if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
	        			if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus())) {
	        				requestObject.setRequestStatusName("<span style=\"color:#990000; font-weight:bold;\">" + requestObject.getRequestStatusName() + "</span>");
	        			}
	        			if(StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
	        				requestObject.setRequestStatusName("<span style=\"color:red\">" + requestObject.getRequestStatusName() + "</span>");
	        			}
	        			
	        			if(permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(editRequestUrl);
	        			}
	        			else if(permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus())) {
	        			requestObject.setRequestStatusName("<span style=\"color:#6099BF;\">" + requestObject.getRequestStatusName() + "</span>");
	        			
	        			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(resolveRequestUrl);
	        			}
	        			else if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
	        					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
	        			if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus())) {
	        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
	        			}
	        			if(StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
	        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
	        			}
	        			
	        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
	        					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else {
	        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        		}
	        	}
	        }
	        
	        return cols;
    	}
    	else {
    		return null;
    	}
    }
    
    public int selectRequestCount(String searchValue, String statusId, String deptCode) throws DaoException {
    	int totalRecord = 0;
    	
    	Application application = Application.getInstance();
    	String userId = application.getCurrentUser().getId();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
	    	StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT COUNT(requestId) as totalRecord " +
	        		"FROM isr_request request, " +
	                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
	                "org_chart_department fromDept, org_chart_department toDept, " +
	                "org_chart_country fromCountry, org_chart_country toCountry, " +
	                "isr_status status " +
	                "WHERE requestFromDept = fromDeptMapping.associativityId " +
	                "AND fromDeptMapping.deptCode = fromDept.code " +
	                "AND fromDeptMapping.countryCode = fromCountry.code " +
	                "AND requestToDept = toDeptMapping.associativityId " +
	                "AND toDeptMapping.deptCode = toDept.code " +
	                "AND toDeptMapping.countryCode = toCountry.code " +
	                "AND requestStatus = status.statusId " +
	                "AND requestFromDept = '" + associatedCountryDept.getAssociativityId() + "' ");
	        if(searchValue != null && 
	                !"".equals(searchValue)) {
	            selectSql.append("AND (requestId LIKE '%" + searchValue + "%' ");
	            selectSql.append("OR requestSubject LIKE '%" + searchValue + "%') ");
	        }
	        if(statusId != null &&
	                !"".equals(statusId)) {
	        	if("active".equalsIgnoreCase(statusId)) {
	        		selectSql.append("AND (requestStatus = '" + StatusObject.STATUS_ID_NEW + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_REOPEN + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_CLARIFICATION + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_IN_PROGRESS + "') ");
	        	}
	        	else {
	        		selectSql.append("AND requestStatus = '" + statusId + "' ");
	        	}
	        }
	        if(deptCode != null &&
	                !"".equals(deptCode)) {
	        	selectSql.append("AND requestToDept = '" + deptCode + "' ");
	        }
	        
	        Collection cols = super.select(selectSql.toString(), HashMap.class, null, 0, -1);
	        if(cols != null) {
	        	if(cols.size() > 0) {
	        		HashMap map = (HashMap) cols.iterator().next();
	        		totalRecord = Integer.parseInt(map.get("totalRecord").toString());
	        	}
	        }
    	}
    	
    	return totalRecord;
    }
    
    public Collection selectAttendingRequest(String searchValue, String searchAttribute, String statusId, String priorityByAdmin, String deptCode, String requestType, String assigneeName, boolean directedFromForceClosure,
            String sort, boolean desc, int start, int rows) throws DaoException {
    	Application application = Application.getInstance();
    	String userId = application.getCurrentUser().getId();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	boolean searchByAssignee = false;
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
    		PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    		boolean isAdmin = permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN);
    		
	        StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT request.requestId, request.dueDate," +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "request.dateCreated, request.createdBy, " +
	                "request.lastUpdatedDate, request.lastUpdatedBy ");
	        
	        if(isAdmin) {
		        selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status ");
	        }
	        else {
	        	selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status, " +
		                "isr_assignment assignment ");
	        }
	        
	        if(isAdmin) {
		        selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' ");
	        }
	        else {
	        	selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' " +
		                "AND assignment.requestId = request.requestId " +
		                "AND assignment.assigneeUserId = '" + userId + "' " +
		                "AND assignment.active = '1' ");
	        }
	        
	        if(searchAttribute != null &&
	        		!"".equals(searchAttribute)) {
	        	if(searchValue != null &&
	        			!"".equals(searchValue)) {
		        	if("requestId".equals(searchAttribute)) {
		        		selectSql.append("AND request.requestId LIKE '%" + searchValue + "%' ");
		        	}
		        	else if("requestSubject".equals(searchAttribute)) {
		        		selectSql.append("AND requestSubject LIKE '%" + searchValue + "%' ");
		        	}
		        	else if("createdBy".equals(searchAttribute)) {
		        		selectSql.append("AND createdBy LIKE '%" + searchValue + "%' ");
		        	}
	        	}
	        }
	        else {
	        	if(searchValue != null &&
	        			!"".equals(searchValue)) {
		        	selectSql.append("AND (request.requestId LIKE '%" + searchValue + "%' ");
		            selectSql.append("OR requestSubject LIKE '%" + searchValue + "%' ");
		            selectSql.append("OR createdBy LIKE '%" + searchValue + "%') ");
	        	}
	        }
	        
	        if(assigneeName != null && 
	                !"".equals(assigneeName)) {
	        	searchByAssignee = true;
	        }
	        if(statusId != null &&
	                !"".equals(statusId)) {
	        	if("active".equalsIgnoreCase(statusId)) {
	        		selectSql.append("AND (requestStatus = '" + StatusObject.STATUS_ID_NEW + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_REOPEN + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_CLARIFICATION + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_IN_PROGRESS + "') ");
	        	}
	        	else {
	        		selectSql.append("AND requestStatus = '" + statusId + "' ");
	        	}
	        }
	        if(priorityByAdmin != null &&
	        		!"".equals(priorityByAdmin)) {
	        	selectSql.append("AND requestPriorityByAdmin = '" + priorityByAdmin + "' ");
	        }
	        if(deptCode != null &&
	                !"".equals(deptCode)) {
	            selectSql.append("AND requestFromDept = '" + deptCode + "' ");
	        }
	        if(requestType != null && 
	        		!"".equals(requestType)) {
	        	selectSql.append("AND requestType = '" + requestType + "' ");
	        }
	        
	        if(sort != null &&
	                !"".equals(sort)) {
	        	if(sort.equals("requestIdRequestorUrl")) {
	        		selectSql.append("ORDER BY request.requestId ");
	        	}
	        	else {
	        		selectSql.append("ORDER BY " + sort + " ");
	        	}
	        }
	        else {
	            selectSql.append("ORDER BY status.statusOrder, dateCreated ");
	            desc = true;
	        }
	        if(desc) {
	            selectSql.append("desc ");
	        }
	        
	        Collection cols = super.select(selectSql.toString(), RequestObject.class, null, searchByAssignee ? 0 : start, searchByAssignee ? -1 : rows);
	        if(cols != null) {
	        	HttpServletRequest httpRequest = Application.getThreadRequest();
	        	SecurityService ss = (SecurityService) application.getService(SecurityService.class);
	        	int rowCount = 0;
	        	int indexCount = 0;
	        	int matchingRowCount = 0; // regardless if it was filtered, but as long as it matches the search requirement
	        	for(Iterator i=cols.iterator(); i.hasNext(); indexCount++) {
	        		RequestObject requestObject = (RequestObject) i.next();
	        		if((rowCount < rows || rows == -1) && (searchByAssignee ? indexCount >= start : true)) {
		        		requestObject.setRequestSubject(HtmlFormatting.getEscapedXmlText(requestObject.getRequestSubject()));
		        		
		        		try {
		        			User user = ss.getUser(requestObject.getLastUpdatedBy());
		        			if(user != null) {
		        				requestObject.setLastUpdatedBy(user.getUsername());
		        			}
		        		}
		        		catch(SecurityException error) {}
		        		
		        		String processRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/attendantProcessRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
												+ requestObject.getRequestId() + "</a>";
		        		String viewRequestUrl = "<a href=\"" + httpRequest.getContextPath() + "/ekms/isr/attendantViewRequest.jsp?requestId=" + requestObject.getRequestId() + (directedFromForceClosure? "&backToForceClosure=true" : "") + "\">"
												+ requestObject.getRequestId() + "</a>";
		        		
		        		if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
		        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
		        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
		        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
		        			if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus())) {
		        				requestObject.setRequestStatusName("<span style=\"color:#990000; font-weight:bold;\">" + requestObject.getRequestStatusName() + "</span>");
		        			}
		        			if(StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
		        				requestObject.setRequestStatusName("<span style=\"color:red\">" + requestObject.getRequestStatusName() + "</span>");
		        			}
		        			requestObject.setRequestIdRequestorUrl(processRequestUrl);
		        		}
		        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
		        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
		        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
		        			if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus())) {
		        				requestObject.setRequestStatusName("<span style=\"color:#6099BF;\">" + requestObject.getRequestStatusName() + "</span>");
		        			}
		        			if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus())) {
		        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
		        			}
		        			if(StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
		        				requestObject.setRequestStatusName("<span style=\"color:#5F6569;\">" + requestObject.getRequestStatusName() + "</span>");
		        			}
		        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
		        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
		        			}
		        			else {
		        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
		        			}
		        		}
		        		else {
		        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
		        		}
		        		
		        		Collection assigneeNames = getAssigneeNames(requestObject.getRequestId(), assigneeName);
		        		String assigneeNamesList = "";
		        		if(assigneeNames != null) {
		        			if(assigneeNames.size() > 0) {
		        				if(searchByAssignee ? matchingRowCount >= start : true) {
			        				int count=0;
			        				for(Iterator itr = assigneeNames.iterator(); itr.hasNext(); count++) {
			        					HashMap map = (HashMap) itr.next();
			        					String tempName = map.get("name").toString();
			        					if(count != 0)
			        						assigneeNamesList += ", ";
			        					assigneeNamesList += tempName; 
			        				}
			        				requestObject.setAssigneeList(assigneeNamesList);
			        				rowCount++;
		        				}
		        				else {
		        					i.remove();
		        				}
		        				matchingRowCount++;
		        			}
		        			else {
		        				if(searchByAssignee) {
		        					i.remove();
		        				}
		        				else {
		        					rowCount++;
		        				}
			        		}
		        		}
		        		else {
		        			if(searchByAssignee) {
		        				i.remove();
		        			}
		        			else {
		        				rowCount++;
		        			}
		        		}
		        		
		        		Collection suggestedResolutions = getSuggestedResolution(requestObject.getRequestId(), false);
		        		if(suggestedResolutions != null) {
		        			if(suggestedResolutions.size() > 0) {
		        				requestObject.setStaffResponded(true);
		        			}
		        		}
	        		}
	        		else {
	        			i.remove();
	        		}
	        	}
	        }
	        
	        return cols;
    	}
    	else {
    		return null;
    	}
    }
    
    public int selectAttendingRequestCount(String searchValue, String searchAttribute, String statusId, String priorityByAdmin, String deptCode, String requestType, String assigneeName) throws DaoException {
    	int totalRecord = 0;
    	boolean searchByAssignee = false;
    	Application application = Application.getInstance();
    	String userId = application.getCurrentUser().getId();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
    		PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    		boolean isAdmin = permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN);
    		
	    	StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT request.requestId requestId ");
	        if(isAdmin) {
		        selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status ");
	        }
	        else {
	        	selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status, " +
		                "isr_assignment assignment ");
	        }
	        
	        if(isAdmin) {
		        selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' ");
	        }
	        else {
	        	selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' " +
		                "AND assignment.requestId = request.requestId " +
		                "AND assignment.assigneeUserId = '" + userId + "' " +
		                "AND assignment.active = '1' ");
	        }
	        
	        if(searchAttribute != null &&
	        		!"".equals(searchAttribute)) {
	        	if(searchValue != null &&
	        			!"".equals(searchValue)) {
		        	if("requestId".equals(searchAttribute)) {
		        		selectSql.append("AND requestId.requestId LIKE '%" + searchValue + "%' ");
		        	}
		        	else if("requestSubject".equals(searchAttribute)) {
		        		selectSql.append("AND requestSubject LIKE '%" + searchValue + "%' ");
		        	}
		        	else if("createdBy".equals(searchAttribute)) {
		        		selectSql.append("AND createdBy LIKE '%" + searchValue + "%' ");
		        	}
	        	}
	        }
	        else {
	        	if(searchValue != null &&
	        			!"".equals(searchValue)) {
		        	selectSql.append("AND (requestId LIKE '%" + searchValue + "%' ");
		            selectSql.append("OR requestSubject LIKE '%" + searchValue + "%' ");
		            selectSql.append("OR createdBy LIKE '%" + searchValue + "%') ");
	        	}
	        }
	        if(assigneeName != null && 
	                !"".equals(assigneeName)) {
	        	searchByAssignee = true;
	        }
	        if(statusId != null &&
	                !"".equals(statusId)) {
	        	if("active".equalsIgnoreCase(statusId)) {
	        		selectSql.append("AND (requestStatus = '" + StatusObject.STATUS_ID_NEW + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_REOPEN + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_CLARIFICATION + "' " +
	        				"OR requestStatus = '" + StatusObject.STATUS_ID_IN_PROGRESS + "') ");
	        	}
	        	else {
	        		selectSql.append("AND requestStatus = '" + statusId + "' ");
	        	}
	        }
	        if(priorityByAdmin != null &&
	        		!"".equals(priorityByAdmin)) {
	        	selectSql.append("AND requestPriorityByAdmin = '" + priorityByAdmin + "' ");
	        }
	        if(deptCode != null &&
	                !"".equals(deptCode)) {
	        	selectSql.append("AND requestFromDept = '" + deptCode + "' ");
	        }
	        if(requestType != null &&
	                !"".equals(requestType)) {
	        	selectSql.append("AND requestType = '" + requestType + "' ");
	        }
	        
	        Collection cols = super.select(selectSql.toString(), HashMap.class, null, 0, -1);
	        if(cols != null) {
	        	for(Iterator i=cols.iterator(); i.hasNext();) {
	        		HashMap map = (HashMap) i.next();
	        		
	        		Collection assigneeNames = getAssigneeNames(map.get("requestId").toString(), assigneeName);
	        		if(assigneeNames != null) {
	        			if(assigneeNames.size() == 0) {
	        				if(searchByAssignee) {
	        					i.remove();
	        				}
	        			}
	        		}
	        		else {
	        			if(searchByAssignee) {
	        				i.remove();
	        			}
	        		}
	        	}
	        }
	        
	        totalRecord = cols.size();
    	}
    	
    	return totalRecord;
    }
    
    public Collection selectDailyDigestRequestByStatus(String statusId, String userId, int start, int rows) throws DaoException {
    	Application application = Application.getInstance();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
	        StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT request.requestId, request.dueDate," +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "request.dateCreated, request.createdBy, " +
	                "request.lastUpdatedDate, request.lastUpdatedBy " +
	                "FROM isr_request request, " +
	                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
	                "org_chart_department fromDept, org_chart_department toDept, " +
	                "org_chart_country fromCountry, org_chart_country toCountry, " +
	                "isr_status status, " +
	                "isr_request_log log " +
	                "WHERE requestFromDept = fromDeptMapping.associativityId " +
	                "AND fromDeptMapping.deptCode = fromDept.code " +
	                "AND fromDeptMapping.countryCode = fromCountry.code " +
	                "AND requestToDept = toDeptMapping.associativityId " +
	                "AND toDeptMapping.deptCode = toDept.code " +
	                "AND toDeptMapping.countryCode = toCountry.code " +
	                "AND requestStatus = status.statusId " +
	                "AND requestFromDept = '" + associatedCountryDept.getAssociativityId() + "' " +
	                "AND log.requestId = request.requestId " +
	                "AND log.dateCreated >= date_sub(now(), interval 1 day) ");
	        if(statusId != null &&
	                !"".equals(statusId)) {
	            selectSql.append("AND requestStatus = '" + statusId + "' ");
	            if(StatusObject.STATUS_ID_NEW.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_NEW_REQUEST + "' ");
	            }
	            else if(StatusObject.STATUS_ID_CLARIFICATION.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_CLARIFICATION + "' ");
	            }
	            else if(StatusObject.STATUS_ID_IN_PROGRESS.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_IN_PROGRESS + "' ");
	            }
	            else if(StatusObject.STATUS_ID_COMPLETED.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_COMPLETED + "' ");
	            }
	            else if(StatusObject.STATUS_ID_REOPEN.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_REOPEN + "' ");
	            }
	            else if(StatusObject.STATUS_ID_CLOSE.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_CLOSE + "' ");
	            }
	            else if(StatusObject.STATUS_ID_CANCEL.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_CANCEL + "' ");
	            }
	        }
	        selectSql.append("ORDER BY dateCreated desc ");
	        
	        Collection cols = super.select(selectSql.toString(), RequestObject.class, null, start, rows);
	        if(cols != null) {
	        	
	        	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
	        	
	        	for(Iterator i=cols.iterator(); i.hasNext();) {
	        		RequestObject requestObject = (RequestObject) i.next();
	        		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
	        		String siteUrl = "";
	        		try {
	        			siteUrl = setupModel.get("siteUrl");
	        		}
	        		catch(SetupException error) {
	        		}
	        		
	        		String editRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/requestorEditRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String resolveRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/requestorResolveRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String viewRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/requestorViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		
	        		if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
	        			if(permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(editRequestUrl);
	        			}
	        			else if(permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus())) {
	        			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(resolveRequestUrl);
	        			}
	        			else if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
	        					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
	        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
	        					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else {
	        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        		}
	        	}
	        }
	        
	        return cols;
    	}
    	else {
    		return null;
    	}
    }
    
    public Collection selectDailyDigestRequesterPendingClarification(String userId, int start, int rows) throws DaoException {
    	Application application = Application.getInstance();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
	        StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT distinct request.requestId,request.dueDate, " +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "request.dateCreated, request.createdBy, " +
	                "request.lastUpdatedDate, request.lastUpdatedBy " +
	                "FROM isr_request request, " +
	                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
	                "org_chart_department fromDept, org_chart_department toDept, " +
	                "org_chart_country fromCountry, org_chart_country toCountry, " +
	                "isr_status status, " +
	                "isr_clarification clarification " +
	                "WHERE requestFromDept = fromDeptMapping.associativityId " +
	                "AND fromDeptMapping.deptCode = fromDept.code " +
	                "AND fromDeptMapping.countryCode = fromCountry.code " +
	                "AND requestToDept = toDeptMapping.associativityId " +
	                "AND toDeptMapping.deptCode = toDept.code " +
	                "AND toDeptMapping.countryCode = toCountry.code " +
	                "AND requestStatus = status.statusId " +
	                "AND requestFromDept = '" + associatedCountryDept.getAssociativityId() + "' " +
	                "AND requestStatus = '" + StatusObject.STATUS_ID_CLARIFICATION + "' " +
	                "AND clarification.requestId = request.requestId " +
	                "AND (clarification.clarificationAnswer = '' OR clarification.clarificationAnswer is NULL) ");
	        selectSql.append("ORDER BY dateCreated desc ");
	        
	        Collection cols = super.select(selectSql.toString(), RequestObject.class, null, start, rows);
	        if(cols != null) {
	        	
	        	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
	        	
	        	for(Iterator i=cols.iterator(); i.hasNext();) {
	        		RequestObject requestObject = (RequestObject) i.next();
	        		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
	        		String siteUrl = "";
	        		try {
	        			siteUrl = setupModel.get("siteUrl");
	        		}
	        		catch(SetupException error) {
	        		}
	        		
	        		String editRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/requestorEditRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String resolveRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/requestorResolveRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String viewRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/requestorViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		
	        		if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
	        			if(permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(editRequestUrl);
	        			}
	        			else if(permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus())) {
	        			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(resolveRequestUrl);
	        			}
	        			else if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
	        					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else if(StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
	        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
	        					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else {
	        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        		}
	        	}
	        }
	        
	        return cols;
    	}
    	else {
    		return null;
    	}
    }
    
    public Collection selectDailyDigestAttendingRequestByStatus(String statusId, String userId, int start, int rows) throws DaoException {
    	Application application = Application.getInstance();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
    		PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    		boolean isAdmin = permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN);
    		
	        StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT request.requestId,request.dueDate, " +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "request.dateCreated, request.createdBy, " +
	                "request.lastUpdatedDate, request.lastUpdatedBy ");
	        
	        if(isAdmin) {
		        selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status, " +
		                "isr_request_log log ");
	        }
	        else {
	        	selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status, " +
		                "isr_assignment assignment, " +
		                "isr_request_log log ");
	        }
	        
	        if(isAdmin) {
		        selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' " +
		                "AND log.requestId = request.requestId " +
		                "AND log.dateCreated >= date_sub(now(), interval 1 day) ");
	        }
	        else {
	        	selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' " +
		                "AND assignment.requestId = request.requestId " +
		                "AND assignment.assigneeUserId = '" + userId + "' " +
		                "AND assignment.active = '1' " +
		                "AND log.requestId = request.requestId " +
		                "AND log.dateCreated >= date_sub(now(), interval 1 day) ");
	        }
	        
	        if(statusId != null &&
	                !"".equals(statusId)) {
	            selectSql.append("AND requestStatus = '" + statusId + "' ");
	            if(StatusObject.STATUS_ID_NEW.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_NEW_REQUEST + "' ");
	            }
	            else if(StatusObject.STATUS_ID_CLARIFICATION.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_CLARIFICATION + "' ");
	            }
	            else if(StatusObject.STATUS_ID_IN_PROGRESS.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_IN_PROGRESS + "' ");
	            }
	            else if(StatusObject.STATUS_ID_COMPLETED.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_COMPLETED + "' ");
	            }
	            else if(StatusObject.STATUS_ID_REOPEN.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_REOPEN + "' ");
	            }
	            else if(StatusObject.STATUS_ID_CLOSE.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_CLOSE + "' ");
	            }
	            else if(StatusObject.STATUS_ID_CANCEL.equals(statusId)) {
	            	selectSql.append("AND log.logDescription = '" + LogObject.LOG_DESC_STATUS_UPDATE_CANCEL + "' ");
	            }
	        }
	        selectSql.append("ORDER BY requestStatus, dateCreated desc ");
	        
	        Collection cols = super.select(selectSql.toString(), RequestObject.class, null, start, rows);
	        if(cols != null) {
	        	
	        	for(Iterator i=cols.iterator(); i.hasNext();) {
	        		RequestObject requestObject = (RequestObject) i.next();
	        		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
	        		String siteUrl = "";
	        		try {
	        			siteUrl = setupModel.get("siteUrl");
	        		}
	        		catch(SetupException error) {
	        		}
	        		
	        		String processRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/attendantProcessRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String viewRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/attendantViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		
	        		if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
	        			requestObject.setRequestIdRequestorUrl(processRequestUrl);
	        		}
	        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
	        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else {
	        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        		}
	        	}
	        }
	        
	        return cols;
    	}
    	else {
    		return null;
    	}
    }
    
    public Collection selectDailyDigestAttendingRequestNewAssignment(String userId, int start, int rows) throws DaoException {
    	Application application = Application.getInstance();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
    		PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    		
	        StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT request.requestId,request.dueDate, " +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "request.dateCreated, request.createdBy, " +
	                "request.lastUpdatedDate, request.lastUpdatedBy ");
	        selectSql.append("FROM isr_request request, " +
	                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
	                "org_chart_department fromDept, org_chart_department toDept, " +
	                "org_chart_country fromCountry, org_chart_country toCountry, " +
	                "isr_status status, " +
	                "isr_assignment assignment ");
	        selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
	                "AND fromDeptMapping.deptCode = fromDept.code " +
	                "AND fromDeptMapping.countryCode = fromCountry.code " +
	                "AND requestToDept = toDeptMapping.associativityId " +
	                "AND toDeptMapping.deptCode = toDept.code " +
	                "AND toDeptMapping.countryCode = toCountry.code " +
	                "AND requestStatus = status.statusId " +
	                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' " +
	                "AND assignment.requestId = request.requestId " +
	                "AND assignment.assigneeUserId = '" + userId + "' " +
	                "AND assignment.active = '1' " +
	                "AND assignment.dateCreated >= date_sub(now(), interval 1 day) ");
	        selectSql.append("ORDER BY dateCreated desc ");
	        
	        Collection cols = super.select(selectSql.toString(), RequestObject.class, null, start, rows);
	        if(cols != null) {
	        	
	        	for(Iterator i=cols.iterator(); i.hasNext();) {
	        		RequestObject requestObject = (RequestObject) i.next();
	        		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
	        		String siteUrl = "";
	        		try {
	        			siteUrl = setupModel.get("siteUrl");
	        		}
	        		catch(SetupException error) {
	        		}
	        		
	        		String processRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/attendantProcessRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String viewRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/attendantViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		
	        		if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
	        			requestObject.setRequestIdRequestorUrl(processRequestUrl);
	        		}
	        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
	        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else {
	        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        		}
	        	}
	        }
	        
	        return cols;
    	}
    	else {
    		return null;
    	}
    }
    
    public Collection selectDailyDigestAttendingRequestAnsweredClarification(String userId, int start, int rows) throws DaoException {
    	Application application = Application.getInstance();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
    		PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    		boolean isAdmin = permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN);
    		
	        StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT distinct request.requestId,request.dueDate, " +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "request.dateCreated, request.createdBy, " +
	                "request.lastUpdatedDate, request.lastUpdatedBy ");
	        
	        if(isAdmin) {
		        selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status, " +
		                "isr_clarification clarification ");
	        }
	        else {
	        	selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status, " +
		                "isr_assignment assignment, " +
		                "isr_clarification clarification ");
	        }
	        
	        if(isAdmin) {
		        selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' ");
	        }
	        else {
	        	selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' " +
		                "AND assignment.requestId = request.requestId " +
		                "AND assignment.assigneeUserId = '" + userId + "' " +
		                "AND assignment.active = '1' ");
	        }
	        selectSql.append("AND requestStatus = '" + StatusObject.STATUS_ID_IN_PROGRESS + "' " +
	        		"AND request.requestId = clarification.requestId " +
	        		"AND clarification.clarificationAnswer is not NULL " +
	        		"AND clarification.clarificationAnswer != '' " +
	        		"AND clarification.lastUpdatedDate >= date_sub(now(), interval 1 day) ");
	        
	        selectSql.append("ORDER BY dateCreated desc ");
	        
	        Collection cols = super.select(selectSql.toString(), RequestObject.class, null, start, rows);
	        if(cols != null) {
	        	
	        	for(Iterator i=cols.iterator(); i.hasNext();) {
	        		RequestObject requestObject = (RequestObject) i.next();
	        		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
	        		String siteUrl = "";
	        		try {
	        			siteUrl = setupModel.get("siteUrl");
	        		}
	        		catch(SetupException error) {
	        		}
	        		
	        		String processRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/attendantProcessRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String viewRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/attendantViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		
	        		if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
	        			requestObject.setRequestIdRequestorUrl(processRequestUrl);
	        		}
	        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
	        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else {
	        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        		}
	        	}
	        }
	        
	        return cols;
    	}
    	else {
    		return null;
    	}
    }
    
    public Collection selectDailyDigestReminder(String userId, int start, int rows) throws DaoException {
    	Application application = Application.getInstance();
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	ConfigModel configModel = (ConfigModel) application.getModule(ConfigModel.class);
    	String reminder=null;
		Collection reminderSetting = configModel.getConfigDetailsByType(ConfigDetailObject.REMINDER_SETTING, null);
		if(reminderSetting != null) {
			if(reminderSetting.size() > 0) {
				ConfigDetailObject fileSizeConfig = (ConfigDetailObject) reminderSetting.iterator().next();
				reminder=fileSizeConfig.getConfigDetailName();
			}
		}else{
			reminder=ConfigDetailObject.REMINDER_DEFAULT;
		}
		if(reminder!=null){
			Calendar date= Calendar.getInstance();
			date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH)-Integer.parseInt(reminder));
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			
    	if(associatedCountryDept.getAssociativityId() != null &&
    			!"".equals(associatedCountryDept.getAssociativityId())) {
    		PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    		boolean isAdmin = permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN);
    		
	        StringBuffer selectSql = new StringBuffer("");
	        selectSql.append("SELECT distinct request.requestId,request.dueDate, " +
	                "requestFromDept, concat(fromCountry.shortDesc, ' - ', fromDept.shortDesc) requestFromDeptName, " +
	                "requestToDept, concat(toCountry.shortDesc, ' - ', toDept.shortDesc) requestToDeptName, " +
	                "requestSubject, requestDescription, " +
	                "requestPriority, requestPriorityByAdmin, " +
	                "requestStatus, status.statusName requestStatusName, " +
	                "requestType, " +
	                "requestResolution, requestResolutionAccepted, " +
	                "request.dateCreated, request.createdBy, " +
	                "request.lastUpdatedDate, request.lastUpdatedBy ");
	        
	        if(isAdmin) {
		        selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status ");
	        }
	        else {
	        	selectSql.append("FROM isr_request request, " +
		                "org_chart_dept_country fromDeptMapping, org_chart_dept_country toDeptMapping, " +
		                "org_chart_department fromDept, org_chart_department toDept, " +
		                "org_chart_country fromCountry, org_chart_country toCountry, " +
		                "isr_status status, " +
		                "isr_assignment assignment ");
	        }
	        
	        if(isAdmin) {
		        selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' " +
		                "AND request.dueDate>=? " +
		                "AND requestStatus NOT IN ('"+StatusObject.STATUS_ID_COMPLETED+"','"+StatusObject.STATUS_ID_COMPLETED+"','"+StatusObject.STATUS_ID_CANCEL+"') ");		        		
	        }
	        else {
	        	selectSql.append("WHERE requestFromDept = fromDeptMapping.associativityId " +
		                "AND fromDeptMapping.deptCode = fromDept.code " +
		                "AND fromDeptMapping.countryCode = fromCountry.code " +
		                "AND requestToDept = toDeptMapping.associativityId " +
		                "AND toDeptMapping.deptCode = toDept.code " +
		                "AND toDeptMapping.countryCode = toCountry.code " +
		                "AND requestStatus = status.statusId " +
		                "AND requestToDept = '" + associatedCountryDept.getAssociativityId() + "' " +
		                "AND assignment.requestId = request.requestId " +
		                "AND assignment.assigneeUserId = '" + userId + "' " +
		                "AND assignment.active = '1' " +
		                "AND request.dueDate>=? " +
		                "AND requestStatus NOT IN ('"+StatusObject.STATUS_ID_COMPLETED+"','"+StatusObject.STATUS_ID_COMPLETED+"','"+StatusObject.STATUS_ID_CANCEL+"') ");
	        }

	        
	        selectSql.append("ORDER BY dateCreated desc ");
	        
	        Collection cols = super.select(selectSql.toString(), RequestObject.class, new Object[]{date}, start, rows);
	        if(cols != null) {
	        	
	        	for(Iterator i=cols.iterator(); i.hasNext();) {
	        		RequestObject requestObject = (RequestObject) i.next();
	        		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
	        		String siteUrl = "";
	        		try {
	        			siteUrl = setupModel.get("siteUrl");
	        		}
	        		catch(SetupException error) {
	        		}
	        		
	        		String processRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/attendantProcessRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		String viewRequestUrl = "<a href=\"" + siteUrl + "/ekms/isr/attendantViewRequest.jsp?requestId=" + requestObject.getRequestId() + "\">"
											+ requestObject.getRequestId() + "</a>";
	        		
	        		if(StatusObject.STATUS_ID_NEW.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLARIFICATION.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_IN_PROGRESS.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_REOPEN.equals(requestObject.getRequestStatus())) {
	        			requestObject.setRequestIdRequestorUrl(processRequestUrl);
	        		}
	        		else if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
	        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
	        				requestObject.setRequestIdRequestorUrl(viewRequestUrl);
	        			}
	        			else {
	        				requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        			}
	        		}
	        		else {
	        			requestObject.setRequestIdRequestorUrl(requestObject.getRequestId());
	        		}
	        	}
	        }
	        
	        return cols;
    	}else {
    		return null;
    	}
		}
    	else {
    		return null;
    	}
    }
    
    public void insertAssignment(String requestId, Map assigneeMap, boolean isAutoAssigned) throws DaoException {
    	Application application = Application.getInstance();
    	String userId = application.getCurrentUser().getId();
    	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    	String isrRole = "";
    	boolean isSuccess = true;
    	
    	if(!isAutoAssigned) {
	    	if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN)) {
	    		isrRole = ISRGroup.ROLE_DEPT_ADMIN;
	    	}
	    	else if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_SECTION_ADMIN)) {
	    		isrRole = ISRGroup.ROLE_SECTION_ADMIN;
	    	}
    	}
    	else {
    		isrRole = ISRGroup.ROLE_SYSTEM;
    	}
    	
    	Transaction tx = null;
        
        try {
            tx = getTransaction();
            tx.begin();
            /*tx.update("UPDATE isr_assignment " +
            		"SET active = '0' " +
            		"WHERE requestId = ? " +
            		"AND adminType = ?", new Object[] {requestId, isrRole});*/
            tx.update("DELETE FROM isr_assignment " +
            		"WHERE requestId = ? " +
            		"AND adminType = ?", new Object[] {requestId, isrRole});
            
            if(assigneeMap != null) {
                for (Iterator i=assigneeMap.keySet().iterator(); i.hasNext();) {
                    String assigneeUserId = (String)i.next();
                    String newId = UuidGenerator.getInstance().getUuid();
                    
                    tx.update("INSERT INTO isr_assignment (" +
                    		"assignmentId, requestId, assigneeUserId, adminType, active, " +
                    		"dateCreated, createdBy) VALUES (" +
                    		"?, ?, ?, ?, '1', " +
                    		"now(), ?)", 
                            new Object[] {newId, requestId, assigneeUserId, isrRole, userId});
                }
            }
            tx.commit();
        }
        catch(Exception error) {
        	isSuccess = false;
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException("Exception caught while updating assignment", error);
        }
        
        if(isSuccess)
        	updateRequestLastUpdatedDate(requestId);
    }
    
    /**
     * Warning: This method will delete all the assignment records automatically inserted by the system 
     * 	for dept admin, at the moment when a new request is created.
     * 	This method is written to delete previously auto assigned records for the purpose of a CR raised.
     * 	Never simply call this method without understanding of its impact.
     * @return total number of rows deleted  
     * @throws DaoException
     */
    public int deleteAllAutoDeptAssignment() throws DaoException {
    	Collection cols = super.select("select assigneeUserId " +
    			"from isr_assignment, isr_group_user, isr_group " +
    			"where assigneeUserId = isr_group_user.userId " +
    			"and isr_group_user.groupId = isr_group.id " +
    			"and isr_group.role = ? " +
    			"and isr_assignment.adminType = ?", HashMap.class, new Object[] {ISRGroup.ROLE_DEPT_ADMIN, ISRGroup.ROLE_SYSTEM}, 0, -1);
    	
    	String deletableAssigneeIds = "";
    	if(cols != null) {
    		for(Iterator itr=cols.iterator(); itr.hasNext();) {
    			HashMap map = (HashMap) itr.next();
    			if(deletableAssigneeIds.length() > 0)
    				deletableAssigneeIds += ",";
    			deletableAssigneeIds += "'" + map.get("assigneeUserId") + "'";
    		}
    	}
    	
    	if(!"".equals(deletableAssigneeIds)) {
	    	return super.update("DELETE FROM isr_assignment " +
	    			"WHERE assigneeUserId in (" + deletableAssigneeIds + ")", null);
    	}
    	else {
    		return 0;
    	}
    }
    
    public void insertAssignmentRemarks(AssignmentRemarksObject assignmentRemarks) throws DaoException {
    	Application application = Application.getInstance();
    	String userName = application.getCurrentUser().getName();
    	
    	if("".equals(assignmentRemarks.getCreatedBy())) {
    		assignmentRemarks.setCreatedBy(userName);
    	}
    	if("".equals(assignmentRemarks.getAssignmentRemarksId())) {
    		String newId = UuidGenerator.getInstance().getUuid();
    		assignmentRemarks.setAssignmentRemarksId(newId);
    	}
    	
    	super.update("INSERT INTO isr_assignment_remarks (" +
    			"assignmentRemarksId, requestId, assignmentRemarks, " +
    			"dateCreated, createdBy) VALUES (" +
    			"#assignmentRemarksId#, #requestId#, #assignmentRemarks#, " +
    			"now(), #createdBy#)", assignmentRemarks);
    	
    	updateRequestLastUpdatedDate(assignmentRemarks.getRequestId());
    }
    
    public Collection getAssignmentRemarks(String requestId, boolean assignmentRemarksDesc) throws DaoException {
    	if(requestId != null &&
    			!"".equals(requestId)) {
    		return super.select("SELECT assignmentRemarksId, requestId, assignmentRemarks, " +
    				"dateCreated, createdBy " +
    				"FROM isr_assignment_remarks " +
    				"WHERE requestId = ? " +
    				"ORDER BY dateCreated " + (assignmentRemarksDesc? "desc" : ""), AssignmentRemarksObject.class, new Object[] {requestId}, 0, -1);
    	}
    	else {
    		return null;
    	}
    }
    
    public Collection getAssigneeNames(String requestId, String searchAssigneeName) throws DaoException {
    	StringBuffer sql = new StringBuffer("SELECT requestId " +
    			"FROM isr_assignment assignment, security_user user " +
    			"WHERE assignment.requestId = ? " +
    			"AND assignment.active = '1' " +
    			"AND assignment.assigneeUserId = user.id ");
    	if(searchAssigneeName != null &&
				!"".equals(searchAssigneeName)) {
			/*sql.append("AND (firstName LIKE '%" + searchAssigneeName + "%' " +
					"OR lastName LIKE '%" + searchAssigneeName + "%') ");*/
    		sql.append("AND (concat(firstName, ' ', lastName) LIKE '%" + searchAssigneeName + "%') ");
		}
    	
    	Collection cols = super.select(sql.toString(), HashMap.class, new Object[] {requestId}, 0, -1);
    	if(cols != null) {
    		if(cols.size() > 0) {
    			String matchingRequestIds = "";
    			for(Iterator itr=cols.iterator(); itr.hasNext();) {
    				HashMap map = (HashMap) itr.next();
    				if(matchingRequestIds.length() != 0) 
    					matchingRequestIds += ",";
    				matchingRequestIds += "'" + map.get("requestId") + "'";
    			}
    			
    			return super.select("SELECT concat(firstName, ' ', lastName) name " +
    					"FROM isr_assignment assignment, security_user user " +
    					"WHERE assignment.requestId IN (" + matchingRequestIds + ") " +
    					"AND assignment.active = '1' " +
    					"AND assignment.assigneeUserId = user.id " +
    					"ORDER BY firstName, lastName", HashMap.class, null, 0, -1);
    		}
    	}
    	return null;
    }
    
    public Collection getDirectAssigneeNames(String userId, String requestId, String searchAssigneeName) throws DaoException {
    	Application application = Application.getInstance();
    	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    	String adminType = "";
    	
    	if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN)) {
    		adminType = ISRGroup.ROLE_DEPT_ADMIN;
    	}
    	else if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_SECTION_ADMIN)) {
    		adminType = ISRGroup.ROLE_SECTION_ADMIN;
    	}
    	
    	if(adminType != null && !"".equals(adminType)) {
    		StringBuffer sql = new StringBuffer("SELECT concat(firstName, ' ', lastName) name " +
	    			"FROM isr_assignment assignment, security_user user " +
	    			"WHERE assignment.requestId = ? " +
	    			"AND assignment.adminType = ? " +
	    			"AND assignment.active = '1' " +
	    			"AND assignment.assigneeUserId = user.id ");
    		if(searchAssigneeName != null &&
    				!"".equals(searchAssigneeName)) {
    			sql.append("AND (firstName LIKE '%" + searchAssigneeName + "%' " +
    					"OR lastName LIKE '%" + searchAssigneeName + "%') ");
    		}
	    	return super.select(sql.toString(), HashMap.class, new Object[] {requestId, adminType}, 0, -1);
    	}
    	else {
    		return null;
    	}
    }
    
    public Collection getFirstLevelAssignees(String requestId) throws DaoException {
    	return super.select("SELECT user.id, user.username, user.firstName, user.lastName " +
    			"FROM isr_assignment assignment, security_user user " +
    			"WHERE assignment.requestId = ? " +
    			"AND assignment.adminType = ? " +
    			"AND assignment.active = '1' " +
    			"AND assignment.assigneeUserId = user.id ", User.class, new Object[] {requestId, ISRGroup.ROLE_DEPT_ADMIN}, 0, -1);
    }
    
    public Collection getSecondLevelAssignees(String requestId) throws DaoException {
    	return super.select("SELECT user.id, user.username, user.firstName, user.lastName " +
    			"FROM isr_assignment assignment, security_user user " +
    			"WHERE assignment.requestId = ? " +
    			"AND assignment.adminType = ? " +
    			"AND assignment.active = '1' " +
    			"AND assignment.assigneeUserId = user.id ", User.class, new Object[] {requestId, ISRGroup.ROLE_SECTION_ADMIN}, 0, -1);
    }
    
    public Map getDirectAssigneeMap(String userId, String requestId) throws DaoException {
    	Application application = Application.getInstance();
    	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    	String adminType = "";
    	Map assigneeMap = new SequencedHashMap();
    	
    	if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN)) {
    		adminType = ISRGroup.ROLE_DEPT_ADMIN;
    	}
    	else if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_SECTION_ADMIN)) {
    		adminType = ISRGroup.ROLE_SECTION_ADMIN;
    	}
    	
    	if(adminType != null && !"".equals(adminType)) {
	    	Collection cols = super.select("SELECT user.id, user.username, user.firstName, user.lastName " +
	    			"FROM isr_assignment assignment, security_user user " +
	    			"WHERE assignment.requestId = ? " +
	    			"AND assignment.adminType = ? " +
	    			"AND assignment.active = '1' " +
	    			"AND assignment.assigneeUserId = user.id ", User.class, new Object[] {requestId, adminType}, 0, -1);
	    	
	    	if(cols != null) {
	    		if(cols.size() > 0) {
	    			User user;
	    			for(Iterator itr=cols.iterator(); itr.hasNext();) {
	    				user = (User) itr.next();
	    				assigneeMap.put(user.getId(), user.getName());
	    			}
	    		}
	    	}
    	}
    	
    	return assigneeMap;
    }
    
    public Collection selectAllStatus() throws DaoException {
    	return super.select("SELECT statusId, statusName " +
    			"FROM isr_status ORDER BY statusOrder", StatusObject.class, null, 0, -1);
    }
    
    public boolean withdrawRequest(String[] requestIds) throws DaoException {
    	boolean isSuccess = true;
    	
    	for(int i=0; i<requestIds.length; i++) {
    		StatusObject status = getRequestStatus(requestIds[i]);
        	
        	if(!status.getStatusId().equals(StatusObject.STATUS_ID_CLOSE) &&
        			!status.getStatusId().equals(StatusObject.STATUS_ID_COMPLETED) &&
        			!status.getStatusId().equals(StatusObject.STATUS_ID_CANCEL)) {
        		super.update("UPDATE isr_request " +
        				"SET requestStatus  = ? " +
        				"WHERE requestId = ?", new Object[] {StatusObject.STATUS_ID_CANCEL, requestIds[i]});
        		
        		LogModel logModel = (LogModel) Application.getInstance().getModule(LogModel.class);
    	        LogObject log = new LogObject();
    	        log.setRequestId(requestIds[i]);
    	        log.setLogAction(LogObject.LOG_ACTION_TYPE_STATUS);
    	        log.setLogDescription(LogObject.LOG_DESC_STATUS_UPDATE_CANCEL);
    	        logModel.insertLog(log);
        	}
        	else {
        		isSuccess = false;
        	}
    	}
    	
    	return isSuccess;
    }
    
    public StatusObject getRequestStatus(String requestId) throws DaoException {
    	StatusObject status = null;
    	
    	Collection cols = super.select("SELECT requestStatus statusId, status.statusName " +
    			"FROM isr_request, isr_status status " +
    			"WHERE requestId = ? " +
    			"AND requestStatus = status.statusId ", StatusObject.class, new Object[] {requestId}, 0, 1);
    	
    	if(cols != null) {
    		if(cols.size() > 0) {
    			status = (StatusObject) cols.iterator().next();
    		}
    	}
    	
    	return status;
    }
    
    public boolean isAccessibleRequest(String requestId, HttpServletRequest request) throws DaoException {
    	boolean isAccessibleRequest = false;
    	SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
    	Application application = Application.getInstance();
    	String userId = null;
    	if(request != null) {
    		userId = ss.getCurrentUser(request).getId();
    	}
    	else {
    		userId = application.getCurrentUser().getId();
    	}
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	boolean tryCheckingPermissionAtAttendingSide = false;
    	
    	Collection selectMatchingRequest = super.select("SELECT request.requestId requestId " +
    			"FROM isr_request request " +
    			"WHERE request.requestId  = ?" +
    			"AND request.requestFromDept = ?", HashMap.class, new Object[] {requestId, associatedCountryDept.getAssociativityId()}, 0, 1);
    	if(selectMatchingRequest != null) {
    		if(selectMatchingRequest.size() > 0) {
    			HashMap map = (HashMap) selectMatchingRequest.iterator().next();
    			
    			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST) ||
    					permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
    					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
    				isAccessibleRequest = true;
    			}
    		}
    		else {
    			tryCheckingPermissionAtAttendingSide = true;
    		}
    	}
    	else {
    		tryCheckingPermissionAtAttendingSide = true;
    	}
    	
    	if(!isAccessibleRequest && tryCheckingPermissionAtAttendingSide) {
    		
    		String sqlInClauseIds = "'"+requestId+"'";
    		RequestObject ro=getRequest(requestId, false, false, false, false);
    		if(ro.getRelatedRequests() != null && !"".equals(ro.getRelatedRequests())) {
    			
    			// This request has only 1 related request
    			if(ro.getRelatedRequests().indexOf(",") == -1) {
    				sqlInClauseIds += " OR request.requestId='" + ro.getRelatedRequests() + "'";
    			}
    			else{	
    				StringTokenizer stringTokenizer = new StringTokenizer(ro.getRelatedRequests(), ",");
    				while(stringTokenizer.hasMoreElements()) {
    					if(sqlInClauseIds.length() > 0)
    						sqlInClauseIds += " OR request.requestId=";
    					sqlInClauseIds += "'" + stringTokenizer.nextElement().toString() + "'";
    				}
    			}
    		}
    		StringBuffer selectAttendingRequestSql = new StringBuffer("SELECT request.requestId, " +
    				"requestStatus ");
    		if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN)) {
    			selectAttendingRequestSql.append("FROM isr_request request " +
    					"WHERE request.requestId =" + sqlInClauseIds + " " +
    					"AND request.requestToDept = '" + associatedCountryDept.getAssociativityId() + "' ");
    		}
    		else {
    			selectAttendingRequestSql.append("FROM isr_request request, isr_assignment assignment " +
    					"WHERE (request.requestId =" + sqlInClauseIds + ") " +
    					"AND request.requestToDept = '" + associatedCountryDept.getAssociativityId() + "' "+
    					"AND request.requestId = assignment.requestId " +
    					"AND assigneeUserId = '" + userId + "' " +
    					"AND assignment.active = '1'");
    		}
    		
    		Collection selectAttendingRequest = super.select(selectAttendingRequestSql.toString(), RequestObject.class, null, 0, 1);
    		if(selectAttendingRequest != null) {
    			if(selectAttendingRequest.size() > 0) {
    				RequestObject requestObject = (RequestObject) selectAttendingRequest.iterator().next();
    				if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
    					if(permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
    						isAccessibleRequest = true;
    					}
    				}
    				else {
    					isAccessibleRequest = true;
    				}
    			}
    		}
    	}
    	
    	return isAccessibleRequest;
    }
    
    public boolean isDownloadableAttachment(String attachmentId, HttpServletRequest request) throws DaoException {
    	boolean isDownloadableAttachment = false;
    	SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
    	Application application = Application.getInstance();
    	String userId = null;
    	if(request != null) {
    		userId = ss.getCurrentUser(request).getId();
    	}
    	else {
    		userId = application.getCurrentUser().getId();
    	}
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	boolean tryCheckingPermissionAtAttendingSide = false;
    	
    	Collection selectMatchingRequest = super.select("SELECT request.requestId requestId " +
    			"FROM isr_request request, isr_attachment attachment " +
    			"WHERE attachment.requestId = request.requestId " +
    			"AND attachment.attachmentId = ? " +
    			"AND request.requestFromDept = ?", HashMap.class, new Object[] {attachmentId, associatedCountryDept.getAssociativityId()}, 0, 1);
    	if(selectMatchingRequest != null) {
    		if(selectMatchingRequest.size() > 0) {
    			HashMap map = (HashMap) selectMatchingRequest.iterator().next();
    			
    			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST) ||
    					permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
    					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
    				isDownloadableAttachment = true;
    			}
    		}
    		else {
    			tryCheckingPermissionAtAttendingSide = true;
    		}
    	}
    	else {
    		tryCheckingPermissionAtAttendingSide = true;
    	}
    	
    	if(!isDownloadableAttachment && tryCheckingPermissionAtAttendingSide) {
    		StringBuffer selectAttendingRequestSql = new StringBuffer("SELECT request.requestId, " +
    				"requestStatus ");
    		if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN)) {
    			selectAttendingRequestSql.append("FROM isr_request request, isr_attachment attachment " +
    					"WHERE attachment.requestId = request.requestId " +
    					"AND attachment.attachmentId = '" + attachmentId + "' " +
    					"AND request.requestToDept = '" + associatedCountryDept.getAssociativityId() + "' ");
    		}
    		else {
    			selectAttendingRequestSql.append("FROM isr_request request, isr_attachment attachment, isr_assignment assignment " +
    					"WHERE attachment.requestId = request.requestId " +
    					"AND attachment.attachmentId = '" + attachmentId + "' " +
    					"AND request.requestToDept = '" + associatedCountryDept.getAssociativityId() + "' "+
    					"AND request.requestId = assignment.requestId " +
    					"AND assigneeUserId = '" + userId + "' " +
    					"AND assignment.active = '1'");
    		}
    		
    		Collection selectAttendingRequest = super.select(selectAttendingRequestSql.toString(), RequestObject.class, null, 0, 1);
    		if(selectAttendingRequest != null) {
    			if(selectAttendingRequest.size() > 0) {
    				RequestObject requestObject = (RequestObject) selectAttendingRequest.iterator().next();
    				if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
    					if(permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
    						isDownloadableAttachment = true;
    					}
    				}
    				else {
    					isDownloadableAttachment = true;
    				}
    			}
    		}
    	}
    	
    	return isDownloadableAttachment;
    }
    
    public boolean isDownloadableResolutionAttachment(String resolutionAttachmentId, HttpServletRequest request) throws DaoException {
    	boolean isDownloadableAttachment = false;
    	SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
    	Application application = Application.getInstance();
    	String userId = null;
    	if(request != null) {
    		userId = ss.getCurrentUser(request).getId();
    	}
    	else {
    		userId = application.getCurrentUser().getId();
    	}
    	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
    	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
    	boolean tryCheckingPermissionAtAttendingSide = false;
    	
    	Collection selectMatchingRequest = super.select("SELECT request.requestId requestId " +
    			"FROM isr_request request, isr_resolution_attachment attachment " +
    			"WHERE attachment.requestId = request.requestId " +
    			"AND attachment.resolutionAttachmentId = ? " +
    			"AND request.requestFromDept = ? ", HashMap.class, new Object[] {resolutionAttachmentId, associatedCountryDept.getAssociativityId()}, 0, 1);
    	if(selectMatchingRequest != null) {
    		if(selectMatchingRequest.size() > 0) {
    			HashMap map = (HashMap) selectMatchingRequest.iterator().next();
    			
    			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST) ||
    					permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
    					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
    				isDownloadableAttachment = true;
    			}
    		}
    		else {
    			tryCheckingPermissionAtAttendingSide = true;
    		}
    	}
    	else {
    		tryCheckingPermissionAtAttendingSide = true;
    	}
    	
    	if(!isDownloadableAttachment && tryCheckingPermissionAtAttendingSide) {
    		StringBuffer selectAttendingRequestSql = new StringBuffer("SELECT request.requestId, " +
    				"requestStatus ");
    		if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN)) {
    			selectAttendingRequestSql.append("FROM isr_request request, isr_resolution_attachment attachment " +
    					"WHERE attachment.requestId = request.requestId " +
    					"AND attachment.resolutionAttachmentId = '" + resolutionAttachmentId + "' " +
    					"AND request.requestToDept = '" + associatedCountryDept.getAssociativityId() + "' ");
    		}
    		else {
    			selectAttendingRequestSql.append("FROM isr_request request, isr_resolution_attachment attachment, isr_assignment assignment " +
    					"WHERE attachment.requestId = request.requestId " +
    					"AND attachment.resolutionAttachmentId = '" + resolutionAttachmentId + "' " +
    					"AND request.requestToDept = '" + associatedCountryDept.getAssociativityId() + "' "+
    					"AND request.requestId = assignment.requestId " +
    					"AND assigneeUserId = '" + userId + "' " +
    					"AND assignment.active = '1'");
    		}
    		
    		Collection selectAttendingRequest = super.select(selectAttendingRequestSql.toString(), RequestObject.class, null, 0, 1);
    		if(selectAttendingRequest != null) {
    			if(selectAttendingRequest.size() > 0) {
    				RequestObject requestObject = (RequestObject) selectAttendingRequest.iterator().next();
    				if(StatusObject.STATUS_ID_COMPLETED.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CLOSE.equals(requestObject.getRequestStatus()) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(requestObject.getRequestStatus())) {
    					if(permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_PROCESS_REQUEST)) {
    						isDownloadableAttachment = true;
    					}
    				}
    				else {
    					isDownloadableAttachment = true;
    				}
    			}
    		}
    	}
    	
    	return isDownloadableAttachment;
    }
    
    //Added
  
    public Collection getMutlipleDeptAdminEmailAdd(String strRequestId) throws DaoException {
    
    	String requestId = strRequestId;
    	for (int i=0; i<2; i++) {
    		String lastChar = requestId.substring(requestId.length()-1, requestId.length());
    		boolean isDigit = Character.isDigit(lastChar.charAt(0));
    		if (!isDigit) {requestId = requestId.substring(0, requestId.length()-1);}
    	}

    	//Collection colRecipient = new ArrayList();
    	String sqlDeptAdmin = "SELECT sec.email1, sec.id AS userId " + 
    			"FROM isr_request req " +  
    			"INNER JOIN org_chart_dept_country cou ON cou.associativityId = req.requestToDept " +  
    			"INNER JOIN org_chart_hierachy hie ON (hie.deptCode = cou.deptCode AND hie.countryCode = cou.countryCode) " + 
    			"INNER JOIN security_user sec ON (sec.id = hie.userId) " +  
    			"INNER JOIN isr_group_user igu ON (igu.userId = sec.id) " +  
    			"INNER JOIN isr_group ig ON (ig.id = igu.groupId AND ig.role = 'isr.role.deptAdmin') " +  
    			"WHERE req.requestId LIKE ? or req.requestId=?";
    
    	Collection colData = super.select(sqlDeptAdmin, HashMap.class, new Object[] {new String(requestId + "_"), requestId}, 0, -1);  

    	if(colData != null && colData.size() > 0)
    		return colData;    	    	
    	return null;
    }
    
    public Collection getDeptAdminEmailAdd(String strRequestId) throws DaoException {

    	//Collection colRecipient = new ArrayList();
    	String sqlDeptAdmin = "SELECT sec.email1, sec.id AS userId " + 
    			"FROM isr_request req " +  
    			"INNER JOIN org_chart_dept_country cou ON cou.associativityId = req.requestToDept " +  
    			"INNER JOIN org_chart_hierachy hie ON (hie.deptCode = cou.deptCode AND hie.countryCode = cou.countryCode) " + 
    			"INNER JOIN security_user sec ON (sec.id = hie.userId) " +  
    			"INNER JOIN isr_group_user igu ON (igu.userId = sec.id) " +  
    			"INNER JOIN isr_group ig ON (ig.id = igu.groupId AND ig.role = 'isr.role.deptAdmin') " +  
    			"WHERE req.requestId = '" +  strRequestId + "'";
    
    	Collection colData = super.select(sqlDeptAdmin, HashMap.class, null, 0, -1);  

    	if(colData != null && colData.size() > 0)
    		return colData;    	    	
    	return null;
    }

    public Collection getAssigneeEmailAdd(String strRequestId) throws DaoException{
    	
    	String sqlAssignee = " SELECT sec.email1, sec.id AS userId FROM isr_assignment ia " +
		"INNER JOIN security_user sec ON ia.assigneeUserId = sec.id " +
		"WHERE requestId ='" +  strRequestId + "'";
    	
    	Collection colAssignee = super.select(sqlAssignee, HashMap.class, null, 0, -1); 
    	
    	if (colAssignee != null && colAssignee.size() > 0 )
    		return colAssignee;
    	return null;
    }
    
    public Collection getRequestorEmailAdd(String strRequestId) throws DaoException{
    	
    	String sql = "SELECT u.email1, u.id AS userId FROM isr_request isr INNER JOIN security_user u ON isr.createdById = u.id WHERE isr.requestId = ?";
    	
    	Collection colRequestor = super.select(sql, HashMap.class, new Object[]{(String) strRequestId}, 0, -1);
    
    	if (colRequestor != null && colRequestor.size() > 0 )
    		return colRequestor;
    	return null;
    }
    

    public Collection getAssigneeUserId (String strRequestId)throws DaoException{
    	String sql = "SELECT assigneeUserId  AS userId FROM isr_assignment WHERE requestId=?";
    	Collection colData = super.select(sql, HashMap.class, new Object[]{(String)strRequestId}, 0, -1);
    	if (colData != null && colData.size() > 0)
    		return colData;
    	return null;
    }
   
	//ADDED
	public void updateClarificationRecipientId(ClarificationObject clarification)throws DaoException{
		
		super.update("UPDATE isr_clarification " +
				"SET createdById = #createdById# "+
				"WHERE clarificationId = #clarificationId#", clarification);
	}	

	public Collection getClarificationRecipientId(ClarificationObject clarification)throws DaoException{	

		String sql ="SELECT u.id AS userId , u.email1 FROM isr_clarification isr INNER JOIN security_user u ON isr.createdById = u.id WHERE isr.clarificationId = ?";
			Collection coldata = super.select(sql, HashMap.class, new Object[]{(String)clarification.getClarificationId()}, 0, 1);
		if(coldata.size() > 0)
			return coldata;
		return null;		
	}	
	
	public Collection getSystemAdminUserId()throws DaoException {
			
			String sqlAdmin= "SELECT id AS userId FROM security_user WHERE username='admin'";
			String sqlManager ="SELECT id AS userId FROM security_user WHERE username='manager'";
			
			Collection colData = super.select(sqlAdmin, HashMap.class, null, 0, 1);
			
			if (colData != null && colData.size() > 0){
				return colData;
			}
			else{
				colData = super.select(sqlManager, HashMap.class, null, 0, 1);
				if (colData != null && colData.size() > 0)
					return colData;
				return null;	
			}				
		}
	
	//Update Existing Table
	public void updateExistingTable()throws DaoException{
		String sqlUserId = "SELECT u.id AS userId, i.requestId AS requestId " +
					"FROM isr_request i " + 
					"INNER JOIN org_chart_dept_country o ON i.requestFromDept = o.associativityId " + 
					"INNER JOIN org_chart_hierachy h ON (h.deptCode = o.deptCode AND h.countryCode = o.countryCode) " +
					"INNER JOIN security_user u ON (h.userId = u.id AND i.createdBy = CONCAT(u.firstName, ' ', u.lastName) ) " +
					"WHERE i.createdById= ''";	
		
		Collection colUserId = super.select(sqlUserId, HashMap.class, null, 0, -1);
		
		if(colUserId != null && colUserId.size() > 0){			
		   	for (Iterator i=colUserId.iterator(); i.hasNext();) {
		 		HashMap map = (HashMap)i.next();
		 		try{
		 		super.update("UPDATE isr_request " +
						"SET createdById ='" + map.get("userId").toString() +"' WHERE requestId ='" +map.get("requestId").toString() + "' ", null);
		 		}catch (Exception e){
		 			 Log.getLog(getClass()).error("Error updating existing isr_request table ", e);	 
		 		}
		   	}
		}
		

		String sqluserId = "SELECT u.id AS userId, c.clarificationId AS clarificationId , c.requestId, CONCAT(u.firstName, ' ', u.lastName)name " +
						"FROM isr_clarification c " + 
						"INNER JOIN isr_request i ON c.requestId = i.requestId "+
						"INNER JOIN org_chart_dept_country o ON i.requestToDept = o.associativityId " +
						"INNER JOIN org_chart_hierachy h ON (h.deptCode = o.deptCode AND h.countryCode = o.countryCode) " +
						"INNER JOIN security_user u ON (h.userId = u.id AND c.createdBy = CONCAT(u.firstName, ' ', u.lastName) ) " +
						"WHERE c.createdById= ''";
		

		Collection colData = super.select(sqluserId, HashMap.class, null, 0, -1);
		
		if(colData != null && colData.size() > 0){			
		   	for (Iterator i=colData.iterator(); i.hasNext();) {
		 		HashMap map = (HashMap)i.next();
		 		try{
		 		super.update("UPDATE isr_clarification " +
						"SET createdById ='" + map.get("userId").toString() +"' WHERE clarificationId ='" +map.get("clarificationId").toString() + "' ", null);
		 		}catch (Exception e){
		 			 Log.getLog(getClass()).error("Error updating existing isr_clarification table ", e);	 
		 		}
		   	}
		}
		
	}
    
}
