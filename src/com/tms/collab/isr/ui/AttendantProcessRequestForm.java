package com.tms.collab.isr.ui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.isr.model.AssignmentRemarksObject;
import com.tms.collab.isr.model.AttachmentObject;
import com.tms.collab.isr.model.ClarificationObject;
import com.tms.collab.isr.model.LogModel;
import com.tms.collab.isr.model.LogObject;
import com.tms.collab.isr.model.RemarksObject;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.RequestObject;
import com.tms.collab.isr.model.ResolutionAttachmentObject;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.model.SuggestedResolutionObject;
import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.collab.isr.setting.model.EmailSetting;
import com.tms.collab.isr.util.HtmlFormatting;

public class AttendantProcessRequestForm extends Form {
	private String requestId = "";
	private boolean isAdmin = false;
	private boolean saveWithAttachment = false;
	protected boolean clarificationDesc = false;
	protected boolean assignmentRemarksDesc = false;
	protected boolean suggestedResolutionDesc = false;
	// Main
	protected Label requestingDepartment;
	protected Label attentionTo;
	protected Label subject;
	protected Label description;
	protected Label status;
	protected Label priority;	
	protected Label dueDate;
	protected Label[] uploadedAttachments;
	protected Panel attachmentPanel;
	protected Label dateCreated;
	protected Radio actionClarification;
	protected Radio actionResolution;
	protected Radio actionAssign;
	protected SelectBox priorityByAdmin;
	protected String priorityByAdminString = "";
	protected SelectBox requestType;
	protected String requestTypeString = "";
	protected Panel remarksPanel;
	// Clarification
	protected TextBox clarificationQuestion;
	// Clarification Messages
	protected Panel clarificationMessagesPanel;
	// Assign
	protected ComboSelectBox assignTo;
	protected TextBox assignmentRemarks;
	// Assignment Remarks
	protected Panel assignmentRemarksPanel;
	// Resolution
	protected TextBox consolidatedResolution;
	protected TextBox suggestedResolution;
	protected FileUpload[] resolutionAttachments;
	protected CheckBox checkCompleted;
	protected Panel resolutionAttachmentPanel;
	protected Panel suggestedResolutionPanel;
	protected Collection relatedRequests;
	// Validators
	protected ValidatorNotEmptySelectBox priorityByAdminVNE;
	protected ValidatorNotEmpty clarificationQuestionVNE;
	protected ValidatorNotEmpty assignToVNE;
	protected ValidatorNotEmpty assignmentRemarksVNE;
	protected ValidatorNotEmpty consolidatedResolutionVNE;
	protected ValidatorNotEmpty suggestedResolutionVNE;
	// Buttons
	protected Button btnSubmit;
	protected Button btnCancel;
	protected String maxUploadSize = "1MB";
	protected String autoClosureDay = "30";
	public static final int MAX_UPLOAD = 3;
	public static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_COMPLETED = "completed";
	public static final String FORWARD_ASSIGNED = "assigned";
    public static final String FORWARD_ERROR = "error"; 
    public static final String FORWARD_ATTACHMENT_ERROR = "attachment error";
    public static final String ILLEGAL_FILE_TYPE = "illegal file type";
    public static final String ILLEGAL_FILE_SIZE = "illegal file size";

    RequestObject requestObject = null;
    
    public String getDefaultTemplate() {
        return "isr/attendantProcessRequest";
    }

    public void init(){
    	super.init();
	}
    
    public void onRequest(Event event) {
    	isAdmin = false;
    	saveWithAttachment = false;
    	uploadedAttachments = null;
    	attachmentPanel = null;
    	actionClarification = null;
    	actionAssign = null;
    	actionResolution = null;
    	remarksPanel = null;
    	clarificationMessagesPanel = null;
    	assignmentRemarksPanel = null;
    	resolutionAttachmentPanel = null;
    	suggestedResolutionPanel = null;
    	relatedRequests = null;
    	
    	initForm(event);
    }
    
    public void initForm(Event event) {
    	setMethod("POST");
		removeChildren();
		setColumns(2);
		
		if(requestId != null &&
				!"".equals(requestId)) {
			Application application = Application.getInstance();
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			ConfigModel configModel = (ConfigModel) application.getModule(ConfigModel.class);
			PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
			
			// First time processing this request, status automatically changed to In Progress
			StatusObject requestStatus = requestModel.getRequestStatus(requestId);
			if(StatusObject.STATUS_ID_NEW.equals(requestStatus.getStatusId())) {
				boolean isSuccess = requestModel.updateRequest(requestId, "requestStatus", StatusObject.STATUS_ID_IN_PROGRESS);
				if(isSuccess) {
					LogModel logModel = (LogModel) Application.getInstance().getModule(LogModel.class);
			        LogObject log = new LogObject();
			        log.setRequestId(requestId);
			        log.setLogAction(LogObject.LOG_ACTION_TYPE_STATUS);
			        log.setLogDescription(LogObject.LOG_DESC_STATUS_UPDATE_IN_PROGRESS);
			        logModel.insertLog(log);
				}
			}
			
			// Main
			requestObject = requestModel.getRequest(requestId, true, false, true, clarificationDesc);
			String userId = application.getCurrentUser().getId();
			isAdmin = permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN);
			String requesterUsername = "(" + requestObject.getCreatedBy() + ")";
			
			requestingDepartment = new Label("requestingDepartment");
			requestingDepartment.setText("<strong>" + requestObject.getRequestFromDeptName() + " " + requesterUsername + "</strong>");
			addChild(requestingDepartment);
			
			attentionTo = new Label("attentionTo");
			attentionTo.setText(requestObject.getRequestToDeptName());
			addChild(attentionTo);
			
			subject = new Label("subject");
			subject.setText(HtmlFormatting.getEscapedXmlText(requestObject.getRequestSubject()));
			addChild(subject);
			
			description = new Label("description");
			description.setText(HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(requestObject.getRequestDescription())));
			addChild(description);
			
			status = new Label("status");
			status.setText("<strong>" + requestObject.getRequestStatusName() + "</strong>");
			addChild(status);
			
			priority = new Label("priority");
			priority.setText("<strong>" + requestObject.getRequestPriority() + "</strong>");
			addChild(priority);
			
			attachmentPanel = new Panel("attachmentPanel");
			attachmentPanel.setColumns(1);
			Collection attachmentCols = requestObject.getAttachments();
			if(attachmentCols != null) {
				if(attachmentCols.size() > 0) {
					uploadedAttachments = new Label[attachmentCols.size()];
					String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
					
					int i=0; 
					for(Iterator itr=attachmentCols.iterator(); itr.hasNext(); i++) {
						AttachmentObject attachmentObject = (AttachmentObject) itr.next();
						uploadedAttachments[i] = new Label("uploadedAttachments" + i);
						uploadedAttachments[i].setText("<a href=\"" + contextPath + "/isr/downloadAttachment?attachmentId=" + attachmentObject.getAttachmentId() + "\">" +
								attachmentObject.getOriFileName() + "</a>");
						attachmentPanel.addChild(uploadedAttachments[i]);
					}
				}
			}
			addChild(attachmentPanel);
			
			DateFormat dateCreatedFormat = new SimpleDateFormat(application.getProperty("globalDatetimeLong"));
			DateFormat dueDateFormat = new SimpleDateFormat(application.getProperty("globalDateLong"));
			dateCreated = new Label("dateCreated");
			dateCreated.setText(dateCreatedFormat.format(requestObject.getDateCreated()));
			addChild(dateCreated);
			dueDate=new Label("dueDate");
			if(requestObject.getDueDate()!=null)
			dueDate.setText(dueDateFormat.format(requestObject.getDueDate()));
			else{
				dueDate.setText("");
			}
			addChild(dueDate);
			boolean expandAssignToByDefault = false;
			if(event.getRequest().getParameter("expandAssign") != null) {
				if("true".equals(event.getRequest().getParameter("expandAssign"))) {
					expandAssignToByDefault = true;
				}
			}
			
			if(permissionModel.hasPermission(userId, ISRGroup.PERM_CLARIFICATION)) {
				actionClarification = new Radio("actionClarification");
				actionClarification.setText(application.getMessage("isr.label.clarification", "Clarification"));
				actionClarification.setGroupName("actions");
				actionClarification.setChecked(false);
				actionClarification.setOnClick("javascript:expandClarification()");
				addChild(actionClarification);
			}
			
			actionResolution = new Radio("actionResolution");
			actionResolution.setText(application.getMessage("isr.label.resolution", "Resolution"));
			actionResolution.setGroupName("actions");
			actionResolution.setChecked(!expandAssignToByDefault);
			actionResolution.setOnClick("javascript:expandResolution()");
			addChild(actionResolution);
			
			// Only Dept Admin and Section Admin granted with permission to assign, can assign the request to another subordinate
			if(permissionModel.hasPermission(userId, ISRGroup.PERM_ASSIGN) && 
					isAdmin ||
					permissionModel.hasISRRole(userId, ISRGroup.ROLE_SECTION_ADMIN)) {
				actionAssign = new Radio("actionAssign");
				actionAssign.setText(application.getMessage("isr.label.assign", "Assign"));
				actionAssign.setGroupName("actions");
				actionAssign.setChecked(expandAssignToByDefault);
				actionAssign.setOnClick("javascript:expandAssign()");
				addChild(actionAssign);
			}
			
			// Dept Admin can update priority, while others view it as text
			priorityByAdmin = new SelectBox("priorityByAdmin");
			priorityByAdmin.setMultiple(false);
			priorityByAdmin.setOptionMap(configModel.getPriorityOptionsMap());
			priorityByAdmin.setSelectedOption(requestObject.getRequestPriorityByAdmin()); 
			if(isAdmin) {
				priorityByAdminVNE = new ValidatorNotEmptySelectBox("priorityByAdminVNE", application.getMessage("isr.validator.notEmpty"));
				priorityByAdmin.addChild(priorityByAdminVNE);
			}
			addChild(priorityByAdmin);
			priorityByAdminString = requestObject.getRequestPriorityByAdmin();
			
			// Request Type
			requestType = new SelectBox("requestType");
			requestType.setMultiple(false);
			requestType.setOptionMap(configModel.getRequestTypeOptionsMap());
			requestType.setSelectedOption(requestObject.getRequestType()); 			
			addChild(requestType);
			requestTypeString = requestObject.getRequestType();
			
			if(actionClarification != null) {
				clarificationQuestion = new TextBox("clarificationQuestion");
				clarificationQuestion.setCols("70");
				clarificationQuestion.setRows("5");
				clarificationQuestionVNE = new ValidatorNotEmpty("clarificationQuestionVNE", application.getMessage("isr.validator.notEmpty"));
				clarificationQuestion.addChild(clarificationQuestionVNE);
				addChild(clarificationQuestion);
			}
			
			// Buttons
			btnSubmit = new Button("btnSubmit", application.getMessage("isr.label.submit"));
	        btnSubmit.setOnClick("return confirmSubmit()");
	        addChild(btnSubmit);
			
			// Clarification Messages
			Collection clarificationMessages = requestObject.getClarification();
			if(clarificationMessages != null) { 
				if(clarificationMessages.size() > 0) {
					clarificationMessagesPanel = new Panel("clarificationMessagesPanel");
					clarificationMessagesPanel.setColumns(1);
					Label[] clarificationMessagesQ = new Label[clarificationMessages.size()];
					Label[] clarificationMessagesA = new Label[clarificationMessages.size()];
					DateFormat dmyFormat = new SimpleDateFormat(ClarificationObject.DATE_TIME_FORMAT);
					
					int i=0;
					for(Iterator itr=clarificationMessages.iterator(); itr.hasNext(); i++) {
						ClarificationObject clarificationObject = (ClarificationObject) itr.next();
						
						StringBuffer question = new StringBuffer("<div>[");
						question.append(clarificationObject.getCreatedBy() + " ");
						question.append(dmyFormat.format(clarificationObject.getDateCreated()) + "]");
						question.append("<br />" + HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(clarificationObject.getClarificationQuestion())));
						clarificationMessagesQ[i] = new Label("clarificationMessagesQ" + i, question.toString() + "</div>");
						clarificationMessagesPanel.addChild(clarificationMessagesQ[i]);
						
						if(!"".equals(clarificationObject.getClarificationAnswer()) &&
								clarificationObject.getClarificationAnswer() != null) {	
							//StringBuffer answer = new StringBuffer("<span style=\"color:red;\">[");
							StringBuffer answer = new StringBuffer("<div id=\"altItem\">[");
							answer.append(clarificationObject.getLastUpdatedBy() + " ");
							answer.append(dmyFormat.format(clarificationObject.getLastUpdatedDate()) + "]");
							answer.append("<br />" + HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(clarificationObject.getClarificationAnswer())));
							clarificationMessagesA[i] = new Label("clarificationMessagesA" + i, answer.toString() + "</div>");
							clarificationMessagesPanel.addChild(clarificationMessagesA[i]);
						}
					}
					
					if(clarificationMessagesPanel != null) {
						addChild(clarificationMessagesPanel);
					}
				}
			}

			// Remarks
			Collection remarksMessages = requestObject.getRemarks();
			if(remarksMessages != null) {
				if(remarksMessages.size() > 0) {
					remarksPanel = new Panel("remarksPanel");
					remarksPanel.setColumns(1);
					Label[] lblRemarksMessages = new Label[remarksMessages.size()];
					DateFormat dmyFormat = new SimpleDateFormat(RemarksObject.DATE_TIME_FORMAT);
					
					int i=0;
					for(Iterator itr=remarksMessages.iterator(); itr.hasNext(); i++) {
						RemarksObject remarksObject = (RemarksObject) itr.next();
						
						StringBuffer previousRemarks = new StringBuffer("<span style=\"color:#ED7B00;\">[");
						previousRemarks.append(dmyFormat.format(remarksObject.getDateCreated()) + "] ");
						previousRemarks.append(HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(remarksObject.getRemarks())) + "</span>");
						
						lblRemarksMessages[i] = new Label("lblRemarksMessages" + i, previousRemarks.toString());
						remarksPanel.addChild(lblRemarksMessages[i]);
					}
					
					addChild(remarksPanel);
				}
			}
			
			// Assign
			if(actionAssign != null) {
				// Assign To
				assignTo = new DirectSubordinatesComboSelect("assignTo");
		        addChild(assignTo);
		        assignTo.init();
		        assignToVNE = new ValidatorNotEmpty("assignToVNE", application.getMessage("isr.validator.notEmpty"));
		        assignTo.getRightSelect().addChild(assignToVNE);
		        
		        // Initialize The Left and Right Values
		        Map directAssigneeMap = requestModel.getDirectAssigneeMap(userId, requestId);
		        Iterator itAssignedSubordinatesGroup = directAssigneeMap.keySet().iterator();
		        Map allSubordinatesGroup = assignTo.getLeftValues();
		        while(itAssignedSubordinatesGroup.hasNext())
		        {
		        	allSubordinatesGroup.remove(itAssignedSubordinatesGroup.next());
		        }
		        assignTo.setLeftValues(allSubordinatesGroup);
		        assignTo.setRightValues(directAssigneeMap);
		        
		        assignmentRemarks = new TextBox("assignmentRemarks");
		        assignmentRemarks.setCols("70");
		        assignmentRemarks.setRows("5");
		        assignmentRemarksVNE = new ValidatorNotEmpty("assignmentRemarksVNE", application.getMessage("isr.validator.notEmpty"));
		        assignmentRemarks.addChild(assignmentRemarksVNE);
		        addChild(assignmentRemarks);
			}
			
			// Posted Assignment Remarks
			Collection postedAssignmentRemarks = requestModel.getAssignmentRemarks(requestId, assignmentRemarksDesc);
			if(postedAssignmentRemarks != null) {
				if(postedAssignmentRemarks.size() > 0) {
					assignmentRemarksPanel = new Panel("assignmentRemarksPanel");
					assignmentRemarksPanel.setColumns(1);
					AssignmentRemarksObject assignmentRemarksObject;
					Label[] lblPostedAssignmentRemarks = new Label[postedAssignmentRemarks.size()];
					DateFormat dmyFormat = new SimpleDateFormat(RemarksObject.DATE_TIME_FORMAT);
					int i=0;
					for(Iterator itr=postedAssignmentRemarks.iterator(); itr.hasNext(); i++) {
						String divId = "";
						if(i%2 != 0) {
							divId = "id=\"altItem\"";
						}
						
						assignmentRemarksObject = (AssignmentRemarksObject) itr.next();
						StringBuffer postedAssignmentRemarksString = new StringBuffer("<div " + divId + ">[");
						postedAssignmentRemarksString.append(assignmentRemarksObject.getCreatedBy() + " " + dmyFormat.format(assignmentRemarksObject.getDateCreated()) + "]");
						postedAssignmentRemarksString.append("<br />" + HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(assignmentRemarksObject.getAssignmentRemarks())) + "</div>");
						
						lblPostedAssignmentRemarks[i] = new Label("lblPostedAssignmentRemarks" + i, postedAssignmentRemarksString.toString());
						assignmentRemarksPanel.addChild(lblPostedAssignmentRemarks[i]);
					}
					addChild(assignmentRemarksPanel);
				}
			}
			
			// Resolution 
			if(isAdmin) {
				consolidatedResolution = new TextBox("consolidatedResolution");
				consolidatedResolution.setCols("70");
				consolidatedResolution.setRows("5");
				consolidatedResolution.setValue(requestObject.getRequestResolution());
				consolidatedResolutionVNE = new ValidatorNotEmpty("consolidatedResolutionVNE", application.getMessage("isr.validator.notEmpty"));
				consolidatedResolution.addChild(consolidatedResolutionVNE);
				addChild(consolidatedResolution);
				
				/*checkCompleted = new CheckBox("checkCompleted");
				checkCompleted.setText(application.getMessage("isr.label.completed"));
				addChild(checkCompleted);*/
			}
			else {
				suggestedResolution = new TextBox("suggestedResolution");
				suggestedResolution.setCols("70");
				suggestedResolution.setRows("5");
				suggestedResolutionVNE = new ValidatorNotEmpty("suggestedResolutionVNE", application.getMessage("isr.validator.notEmpty"));
				suggestedResolution.addChild(suggestedResolutionVNE);
				addChild(suggestedResolution);
			}
			
			Collection maxFileSizeSelect = configModel.getConfigDetailsByType(ConfigDetailObject.FILE_SIZE_UPLOAD, null);
			if(maxFileSizeSelect != null) {
				if(maxFileSizeSelect.size() > 0) {
					ConfigDetailObject fileSizeConfig = (ConfigDetailObject) maxFileSizeSelect.iterator().next();
					maxUploadSize = fileSizeConfig.getConfigDetailName();
				}
			}
			Collection resolutionAttachmentCols = requestModel.getResolutionAttachmentByUser(requestId, userId);
			int uploadedResolutionAttachmentCount = 0;
			if(resolutionAttachmentCols != null) {
				if(resolutionAttachmentCols.size() > 0) {
					uploadedResolutionAttachmentCount = resolutionAttachmentCols.size();
					// Dept Admin will see the uploaded resolution attachments along with FileUpload objects
					if(isAdmin) {
						resolutionAttachmentPanel = new Panel("resolutionAttachmentPanel");
						resolutionAttachmentPanel.setColumns(1);
						
						Label[] uploadedResolutionAttachments = new Label[resolutionAttachmentCols.size()];
						String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
						
						int i=0; 
						for(Iterator itr=resolutionAttachmentCols.iterator(); itr.hasNext(); i++) {
							ResolutionAttachmentObject resolutionAttachmentObject = (ResolutionAttachmentObject) itr.next();
							uploadedResolutionAttachments[i] = new Label("uploadedResolutionAttachments" + i);
							uploadedResolutionAttachments[i].setText("<a href=\"" + contextPath + "/isr/downloadResolutionAttachment?resolutionAttachmentId=" + resolutionAttachmentObject.getResolutionAttachmentId() + "\">" +
									resolutionAttachmentObject.getOriFileName() + "</a>");
							resolutionAttachmentPanel.addChild(uploadedResolutionAttachments[i]);
						}
					}
					else {
						if(MAX_UPLOAD != uploadedResolutionAttachmentCount) {
							resolutionAttachmentPanel = new Panel("resolutionAttachmentPanel");
							resolutionAttachmentPanel.setColumns(1);
						}
						else {
							resolutionAttachmentPanel = null;
						}
					}
				}
			}
			if(MAX_UPLOAD != uploadedResolutionAttachmentCount) {
				if(resolutionAttachmentPanel == null) {
					resolutionAttachmentPanel = new Panel("resolutionAttachmentPanel");
					resolutionAttachmentPanel.setColumns(1);
				}
				resolutionAttachments = new FileUpload[MAX_UPLOAD - uploadedResolutionAttachmentCount];
				resolutionAttachmentPanel.addChild(new Label("lblAllowedAttachmentFileExt", 
						"<p>" + application.getMessage("isr.label.allowedFileExtensions") + " - " +
						getAllowedAttachmentFileExtList() + "</p>"));
				for(int i=0; i<resolutionAttachments.length; i++) {
					resolutionAttachments[i] = new FileUpload("resolutionAttachments" + i);
					resolutionAttachments[i].setSize("50");
					resolutionAttachments[i].setOnChange("javascript:checkDuplicatedFilenames(this)");
					resolutionAttachmentPanel.addChild(resolutionAttachments[i]);
				}
			}
			if(resolutionAttachmentPanel != null) {
				addChild(resolutionAttachmentPanel);
			}
			
			// Suggested Resolutions
			Collection suggestedResolutionCols = requestModel.getSuggestedResolution(requestId, suggestedResolutionDesc);
			if(suggestedResolutionCols != null) {
				if(suggestedResolutionCols.size() > 0) {
					suggestedResolutionPanel = new Panel("suggestedResolutionPanel");
					suggestedResolutionPanel.setColumns(1);
					SuggestedResolutionObject suggestedResolutionObject;
					DateFormat dmyFormat = new SimpleDateFormat(SuggestedResolutionObject.DATE_TIME_FORMAT);
					Label[] lblSuggestedResolutions = new Label[suggestedResolutionCols.size()];
					int i=0;
					for(Iterator itr=suggestedResolutionCols.iterator(); itr.hasNext(); i++) {
						String divId = "";
						if(i%2 != 0) {
							divId = "id=\"altItem\"";
						}
						suggestedResolutionObject = (SuggestedResolutionObject) itr.next();
						StringBuffer postedSuggestedResolution = new StringBuffer("<div " + divId + ">[");
						postedSuggestedResolution.append(suggestedResolutionObject.getCreatedBy() + " " + dmyFormat.format(suggestedResolutionObject.getDateCreated()) + "]");
						postedSuggestedResolution.append("<br />" + HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(suggestedResolutionObject.getResolution())));
						
						// Each suggested resolution might have its associative attachment. If yes, show them together
						Collection associatedResolutionAttachments = requestModel.getResolutionAttachmentBySuggestedResolution(requestId, suggestedResolutionObject.getSuggestedResolutionId());
						if(associatedResolutionAttachments != null) {
							if(associatedResolutionAttachments.size() > 0) {
								String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
								for(Iterator itrAttachment=associatedResolutionAttachments.iterator(); itrAttachment.hasNext(); ) {
									ResolutionAttachmentObject resolutionAttachmentObject = (ResolutionAttachmentObject) itrAttachment.next();
									postedSuggestedResolution.append("<br />&nbsp;&nbsp;&nbsp;<a href=\"" + contextPath + "/isr/downloadResolutionAttachment?resolutionAttachmentId=" + resolutionAttachmentObject.getResolutionAttachmentId() + "\">" +
											resolutionAttachmentObject.getOriFileName() + "</a>");
								}
								postedSuggestedResolution.append("</div>");
							}
							else {
								postedSuggestedResolution.append("</div>");
							}
						}
						else {
							postedSuggestedResolution.append("</div>");
						}
						
						lblSuggestedResolutions[i] = new Label("lblSuggestedResolutions" + i, postedSuggestedResolution.toString());
						suggestedResolutionPanel.addChild(lblSuggestedResolutions[i]);
					}
					addChild(suggestedResolutionPanel);
				}
			}
			
			relatedRequests = requestObject.getRelatedRequestsCol();
		}		
    }
    
    private String getAllowedAttachmentFileExtList() {
    	ConfigModel model = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
		ConfigDetailObject config = new ConfigDetailObject();
		Collection cols;
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.ALLOWED_FILE_EXTENSION, null);
		String fileExtList = "";
		int j=0;
		for(Iterator i=cols.iterator(); i.hasNext(); j++) {
			config = (ConfigDetailObject) i.next();
			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
				if(j != 0)
					fileExtList += ", ";
				fileExtList += config.getConfigDetailName();
			}
		}
		
		if("".equals(fileExtList)) {
			fileExtList = "None";
		}
		
		return fileExtList;
    }
    
    public Forward onSubmit(Event event) {
    	Forward forward = super.onSubmit(event);
    	boolean illegalFileType = false;
        ArrayList illegalFiles = null;
        
    	if(actionClarification != null) {
	    	if(!actionClarification.isChecked()) {
	    		clarificationQuestionVNE.setInvalid(false);
	    		clarificationQuestion.setInvalid(false);
	    	}
    	}
    	
    	if(actionAssign != null) {
	    	if(!actionAssign.isChecked()) {
	    		if(assignTo != null) {
	    			assignToVNE.setInvalid(false);
	    			assignTo.setInvalid(false);
	    		}
	    		if(assignmentRemarks != null) {
		    		assignmentRemarksVNE.setInvalid(false);
		    		assignmentRemarks.setInvalid(false);
	    		}
	    	}
    	}
    	
    	if(actionResolution.isChecked()) {
    		try {
        		ResolutionAttachmentObject attachmentObject = new ResolutionAttachmentObject();
        		Map allowedFileExt = attachmentObject.getAllowedFileExt();
        		
        		if(resolutionAttachments != null) {
    		    	for(int i=0; i<resolutionAttachments.length; i++) {
    		    		StorageFile sf = resolutionAttachments[i].getStorageFile(event.getRequest());
    		    		if(sf != null) {
    		    			int lastIndexOfDot = sf.getName().lastIndexOf(".");
    		    			String fileExt = "";
    		    			if(lastIndexOfDot != -1) {
    		    				fileExt = sf.getName().substring(lastIndexOfDot, sf.getName().length());
    		    			}
    		    			if(! allowedFileExt.containsKey(fileExt.toLowerCase())) {
    		    				this.setInvalid(true);
    		    				illegalFileType = true;
    		    				
    		    				if(illegalFiles == null) {
    		    					illegalFiles = new ArrayList();
    		    					
    		    				}
    		    				illegalFiles.add(sf.getName());
    		    			}
    		    			else {
    		    				long maxBytes = Long.parseLong(maxUploadSize.substring(0, maxUploadSize.length() - 2));
    		    				String byteUnit = maxUploadSize.substring(maxUploadSize.length() - 2, maxUploadSize.length());
    		    				
    		    				if(byteUnit.equalsIgnoreCase("MB")) {
    		    					maxBytes *= 1024 * 1024;
    		    				}
    		    				else if(byteUnit.equalsIgnoreCase("KB")) {
    		    					maxBytes *= 1024;
    		    				}
    		    				
    		    				if(sf.getSize() > maxBytes) {
    		    					this.setInvalid(true);
    		    					return new Forward(ILLEGAL_FILE_SIZE);
    		    				}
    		    			}
    		    			
    		    			saveWithAttachment = true;
    		    		}
    		    	}
        		}
        	}
        	catch(IOException error) {
        		Log.getLog(getClass()).error(error, error);
        	}
    	}
    	else {
    		if(consolidatedResolution != null) {
    			consolidatedResolutionVNE.setInvalid(false);
    			consolidatedResolution.setInvalid(false);
    		}
    		if(suggestedResolution != null) {
    			suggestedResolutionVNE.setInvalid(false);
    			suggestedResolution.setInvalid(false);
    		}
    	}
    	
    	boolean isInvalid = false;
    	if(priorityByAdminVNE != null) {
    		if(priorityByAdminVNE.isInvalid()) {
    			isInvalid = true;
    		}
    	}
    	if(!isInvalid && actionClarification != null) {
	    	if(clarificationQuestionVNE.isInvalid()) {
				isInvalid = true;
			}
    	}
		if(!isInvalid && actionAssign != null) {
			if(assignToVNE.isInvalid() ||
					assignmentRemarksVNE.isInvalid()) {
				isInvalid = true;
			}
		}
		if(!isInvalid && consolidatedResolution != null) {
			if(consolidatedResolutionVNE.isInvalid()) {
				isInvalid = true;
			}
		}
		if(!isInvalid && suggestedResolution != null) {
			if(suggestedResolutionVNE.isInvalid()) {
				isInvalid = true;
			}
		}
		this.setInvalid(isInvalid);
    	
		if(illegalFileType) {
    		event.getRequest().setAttribute("illegalFiles", illegalFiles);
    		return new Forward(ILLEGAL_FILE_TYPE);
    	}
    	else {
    		return forward;
    	}
    }
    
    public Forward onValidate(Event event) {

    	if(requestId != null &&
    			!"".equals(requestId)) {
    		Application application = Application.getInstance();
    		RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
    		boolean isSuccess = true;
    		boolean isCompleted = false;
    		boolean isAssigned = false;
    		
    		
    		SendEmailMemo  mailObj = new SendEmailMemo();
    		
    		if(isAdmin) {
    			String selectedPriority = (String) priorityByAdmin.getSelectedOptions().keySet().iterator().next();
	    		isSuccess = requestModel.updateRequest(requestId, "requestPriorityByAdmin", selectedPriority);
	    		
	    		String selectedRequestType = (String) requestType.getSelectedOptions().keySet().iterator().next();
	    		isSuccess = requestModel.updateRequest(requestId, "requestType", selectedRequestType);
    		}
    		
    		if(actionClarification != null) {
		    	if(actionClarification.isChecked()) {
		    		ClarificationObject clarificationObject = new ClarificationObject();
		    		clarificationObject.setRequestId(requestId);
		    		clarificationObject.setClarificationQuestion(clarificationQuestion.getValue().toString());
		    		isSuccess = requestModel.insertClarification(clarificationObject);
		    		if(isSuccess) {
		    			requestModel.updateRequest(requestId, "requestStatus", StatusObject.STATUS_ID_CLARIFICATION);
		    			LogModel logModel = (LogModel) Application.getInstance().getModule(LogModel.class);
				        LogObject log = new LogObject();
				        log.setRequestId(requestId);
				        log.setLogAction(LogObject.LOG_ACTION_TYPE_STATUS);
				        log.setLogDescription(LogObject.LOG_DESC_STATUS_UPDATE_CLARIFICATION);
				        logModel.insertLog(log);
				    	//AutoSend Email	
				        mailObj.sendNotification(EmailSetting.CLARIFICATION, null, requestObject,
			    		clarificationObject, null, null);
			    			
		    		}
		    	}
    		}
	    	
	    	if(actionAssign != null) {
		    	if(actionAssign.isChecked()) {
		    		isSuccess = requestModel.insertAssignment(requestId, assignTo.getRightValues());
		    		
		    		AssignmentRemarksObject assignmentRemarksObject = new AssignmentRemarksObject();
		    		assignmentRemarksObject.setRequestId(requestId);
		    		assignmentRemarksObject.setAssignmentRemarks(assignmentRemarks.getValue().toString());
		    		isSuccess = requestModel.insertAssignmentRemarks(assignmentRemarksObject);
		    		if(isSuccess) {
		    			isAssigned = true;
		    		   	//AutoSend Email
				       mailObj.sendNotification(EmailSetting.ASSIGNMMENT, null, requestObject,
			    		null, null, assignmentRemarksObject);
		    		}
		    		
		    	}
	    	}
	    	
	    	if(actionResolution.isChecked()) {
	    		String suggestedResolutionId = "";
	    		if(suggestedResolution != null) {
	    			SuggestedResolutionObject suggestedResolutionObject = new SuggestedResolutionObject();
	    			suggestedResolutionId = UuidGenerator.getInstance().getUuid();
	    			suggestedResolutionObject.setSuggestedResolutionId(suggestedResolutionId);
	    			suggestedResolutionObject.setRequestId(requestId);
	    			suggestedResolutionObject.setResolution(suggestedResolution.getValue().toString());
	    			isSuccess = requestModel.insertSuggestedResolution(suggestedResolutionObject);
	    		   	//AutoSend Email
			        mailObj.sendNotification( EmailSetting.RESOLUTION, null, requestObject,
		    		null, null, null);
	    		}
	    		
	    		if(isAdmin) {
	    			isSuccess = requestModel.updateRequest(requestId, "requestResolution", consolidatedResolution.getValue().toString());
	    			isSuccess = requestModel.completeRequestResolution(requestId);
    				isCompleted = true;
	    			/*if(checkCompleted.isChecked()) {
	    				isSuccess = requestModel.completeRequestResolution(requestId);
	    				isCompleted = true;
	    			}*/
    			   	//AutoSend Email
			        mailObj.sendNotification(EmailSetting.RESOLUTION, null, requestObject,
		    		null, null, null);
	    		}
	
	    		if(isSuccess && saveWithAttachment) {
	    			isSuccess = requestModel.uploadResolutionAttachments(event.getRequest(), requestId, suggestedResolutionId, resolutionAttachments, event);
	    		}
	    		
	    	}
	    	
	    	if(isSuccess) {
	    		if(isCompleted){
	    		
		    		
		    	/*	if (strAction.equals(CLARIFICATION))
		    		//mailObj.sendNotification(strAction, userId, null, requestObject,
		    		//		 ClarificationObject clarificationObject, SuggestedResolutionObject suggestedResolutionObject, AssignmentRemarksObject assignmentRemarksObject ){
*/		    			 
	    			return new Forward(FORWARD_COMPLETED);
	    		}
	    		else if(isAssigned)
	    			return new Forward(FORWARD_ASSIGNED);
	    		else
	    			return new Forward(FORWARD_SUCCESS);
	    	}
	    	else {
	    		return new Forward(FORWARD_ERROR);
	    	}
    	}
    	else {
    		return new Forward(FORWARD_ERROR);
    	}
    }

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Radio getActionAssign() {
		return actionAssign;
	}

	public Radio getActionClarification() {
		return actionClarification;
	}

	public Radio getActionResolution() {
		return actionResolution;
	}

	public TextBox getAssignmentRemarks() {
		return assignmentRemarks;
	}

	public Panel getAssignmentRemarksPanel() {
		return assignmentRemarksPanel;
	}

	public ComboSelectBox getAssignTo() {
		return assignTo;
	}

	public Panel getAttachmentPanel() {
		return attachmentPanel;
	}

	public Label getAttentionTo() {
		return attentionTo;
	}

	public String getAutoClosureDay() {
		return autoClosureDay;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public Panel getClarificationMessagesPanel() {
		return clarificationMessagesPanel;
	}

	public TextBox getClarificationQuestion() {
		return clarificationQuestion;
	}

	public CheckBox getCheckCompleted() {
		return checkCompleted;
	}

	public TextBox getConsolidatedResolution() {
		return consolidatedResolution;
	}

	public Label getDateCreated() {
		return dateCreated;
	}

	public Label getDescription() {
		return description;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public String getMaxUploadSize() {
		return maxUploadSize;
	}

	public Label getPriority() {
		return priority;
	}

	public SelectBox getPriorityByAdmin() {
		return priorityByAdmin;
	}

	public String getPriorityByAdminString() {
		return priorityByAdminString;
	}

	public Panel getRemarksPanel() {
		return remarksPanel;
	}

	public Label getRequestingDepartment() {
		return requestingDepartment;
	}

	public Panel getResolutionAttachmentPanel() {
		return resolutionAttachmentPanel;
	}

	public Label getStatus() {
		return status;
	}

	public Label getSubject() {
		return subject;
	}

	public TextBox getSuggestedResolution() {
		return suggestedResolution;
	}

	public Panel getSuggestedResolutionPanel() {
		return suggestedResolutionPanel;
	}

	public Label[] getUploadedAttachments() {
		return uploadedAttachments;
	}
	
	public boolean isClarificationDesc() {
		return clarificationDesc;
	}

	public void setClarificationDesc(boolean clarificationDesc) {
		this.clarificationDesc = clarificationDesc;
	}

	public boolean isAssignmentRemarksDesc() {
		return assignmentRemarksDesc;
	}

	public void setAssignmentRemarksDesc(boolean assignmentRemarksDesc) {
		this.assignmentRemarksDesc = assignmentRemarksDesc;
	}

	public SelectBox getRequestType() {
		return requestType;
	}

	public String getRequestTypeString() {
		return requestTypeString;
	}

	public boolean isSuggestedResolutionDesc() {
		return suggestedResolutionDesc;
	}

	public void setSuggestedResolutionDesc(boolean suggestedResolutionDesc) {
		this.suggestedResolutionDesc = suggestedResolutionDesc;
	}

	public Collection getRelatedRequests() {
		return relatedRequests;
	}

	public Label getDueDate() {
		return dueDate;
	}

	public void setDueDate(Label dueDate) {
		this.dueDate = dueDate;
	}
}