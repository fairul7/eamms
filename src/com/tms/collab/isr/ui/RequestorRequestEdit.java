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
import kacang.stdui.DatePopupField;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import com.tms.collab.isr.model.AttachmentObject;
import com.tms.collab.isr.model.ClarificationObject;
import com.tms.collab.isr.model.RemarksObject;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.RequestObject;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.collab.isr.setting.model.EmailSetting;
import com.tms.collab.isr.util.HtmlFormatting;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;

public class RequestorRequestEdit extends Form {
	private String requestId = "";
	private String clarificationId = "";
	private boolean saveWithAttachment = false;
	protected boolean addRemarks = true;
	protected boolean addClarification = false;
	protected boolean remarksDesc = false;
	protected boolean clarificationDesc = false;
	protected Label requestingDepartment;
	protected Label attentionTo;
	protected Label subject;
	protected Label description;
	protected Label status;
	protected Label priority;
	protected Label[] uploadedAttachments;
	protected FileUpload[] attachments;
	protected Panel attachmentPanel;
	protected Label dateCreated;
	protected Collection relatedRequests;
	protected Panel remarksPanel;
	protected TextBox addRemarksTextBox;
	protected Label clarification;
	protected TextBox replyToClarification;
	protected Panel clarificationMessagesPanel;
	protected DatePopupField dueDate;
	protected Button btnSubmit;
	protected Button btnCancel;
	protected Button btnSend;
	protected String maxUploadSize = "1MB";
	protected CheckBox checkSendAnotherDept;
	protected ComboSelectBox multipleAttentionTo;
	protected ValidatorNotEmpty attentionToVNE;
	public static final int MAX_UPLOAD = 3;
	public static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_EMPTY_INPUT = "empty input";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_ATTACHMENT_ERROR = "attachment error";
    public static final String ILLEGAL_FILE_TYPE = "illegal file type";
    public static final String ILLEGAL_FILE_SIZE = "illegal file size";
    private RequestObject requestObject;
    
    public String getDefaultTemplate() {
        return "isr/requestorRequestEdit";
    }
    
    public void init(){
    	super.init();
    	Application application = Application.getInstance();
    
	}
    
    public void onRequest(Event ev) {
    	saveWithAttachment = false;
    	addRemarks = true;
    	addClarification = false;
    	relatedRequests = null;
    	attachmentPanel = null;
    	remarksPanel = null;
    	clarificationMessagesPanel = null;
    	
    	initForm(ev);
    }
    
    public void initForm(Event ev) {
    	setMethod("POST");
		removeChildren();
		setColumns(2);
		
		if(requestId != null &&
				!"".equals(requestId)) {
			Application application = Application.getInstance();
			//System.out.println("application.getThreadRequest().getRemoteAddr(); "  +  application.getThreadRequest().getRemoteAddr());
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			ConfigModel configModel = (ConfigModel) application.getModule(ConfigModel.class);
			PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
			requestObject = requestModel.getRequest(requestId, true, remarksDesc, true, clarificationDesc);
			String userId = application.getCurrentUser().getId();
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
			
			Collection maxFileSizeSelect = configModel.getConfigDetailsByType(ConfigDetailObject.FILE_SIZE_UPLOAD, null);
			if(maxFileSizeSelect != null) {
				if(maxFileSizeSelect.size() > 0) {
					ConfigDetailObject fileSizeConfig = (ConfigDetailObject) maxFileSizeSelect.iterator().next();
					maxUploadSize = fileSizeConfig.getConfigDetailName();
				}
			}
			attachmentPanel = new Panel("attachmentPanel");
			attachmentPanel.setColumns(1);
			Collection attachmentCols = requestObject.getAttachments();
			int uploadedAttachmentCount = 0;
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
					uploadedAttachmentCount = attachmentCols.size();
				}
			}
			if(uploadedAttachmentCount != MAX_UPLOAD) {
				if(permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST)) {
					attachments = new FileUpload[MAX_UPLOAD - uploadedAttachmentCount];
					attachmentPanel.addChild(new Label("lblAllowedAttachmentFileExt", 
							"<p>" + application.getMessage("isr.label.allowedFileExtensions") + " - " +
							getAllowedAttachmentFileExtList() + "</p>"));
					for(int i=0; i<attachments.length; i++) {
						attachments[i] = new FileUpload("attachments" + i);
						attachments[i].setSize("50");
						attachments[i].setOnChange("javascript:checkDuplicatedFilenames(this)");
						attachmentPanel.addChild(attachments[i]);
					}
				}
			}
			addChild(attachmentPanel);
			
			DateFormat dateCreatedFormat = new SimpleDateFormat(application.getProperty("globalDatetimeLong"));
			dateCreated = new Label("dateCreated");
			dateCreated.setText(dateCreatedFormat.format(requestObject.getDateCreated()));
			addChild(dateCreated);
			
			dueDate=new DatePopupField("dueDate");
			dueDate.setOptional(true);
			if(requestObject.getDueDate()!=null)
			dueDate.setDate(requestObject.getDueDate());
			addChild(dueDate);
			
			relatedRequests = requestObject.getRelatedRequestsCol();
			
			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST)) {
				checkSendAnotherDept = new CheckBox("checkSendAnotherDept");
				checkSendAnotherDept.setOnClick("javascript:togglePanelVisibility('sendToAnotherDeptPanel')");
				addChild(checkSendAnotherDept);
				multipleAttentionTo = new MultipleDeptComboSelect("multipleAttentionTo");
				addChild(multipleAttentionTo);
		        multipleAttentionTo.init();
		        btnSend = new Button("btnSend", application.getMessage("isr.label.send", "Send"));
		        btnSend.setOnClick("return confirmSendOtherDept()");
		        addChild(btnSend);
		        
		        Map filteredDeptMap = getFilteredDeptMap(multipleAttentionTo.getLeftValues(), 
		        		(!"".equals(requestObject.getRelatedRequests()) && requestObject.getRelatedRequests() != null ? requestObject.getRelatedRequests() + "," + requestId : requestId));
		        
		        if(filteredDeptMap.size() == 0) {
		        	removeChild(checkSendAnotherDept);
		        	removeChild(multipleAttentionTo);
		        	removeChild(btnSend);
		        	
		        	checkSendAnotherDept = null;
		        	multipleAttentionTo = null;
		        	btnSend = null;
		        }
		        else {
		        	multipleAttentionTo.setLeftValues(filteredDeptMap);
		        	attentionToVNE = new ValidatorNotEmpty("attentionToVNE", application.getMessage("isr.validator.notEmpty"));
		        	multipleAttentionTo.getRightSelect().addChild(attentionToVNE);
		        }
			}
			
			/*if(requestObject.getRequestMultipleToDept() != null) {
				relatedRequests = new Label("relatedRequests");
				Map relatedRequestsMap = requestObject.getRequestMultipleToDept();
				StringBuffer relatedRequestsStr = new StringBuffer("");
				int i=0;
				for(Iterator itr=relatedRequestsMap.keySet().iterator(); itr.hasNext(); i++) {
					String tempRequestId = itr.next().toString();
					String tempStatusId = relatedRequestsMap.get(tempRequestId).toString();
					if(i > 0) 
						relatedRequestsStr.append(", ");
					
					String editRequestUrl = "<a href=\"" + ev.getRequest().getContextPath() + "/ekms/isr/requestorEditRequest.jsp?requestId=" + tempRequestId + "\">"
										+ tempRequestId + "</a>";
					String resolveRequestUrl = "<a href=\"" + ev.getRequest().getContextPath() + "/ekms/isr/requestorResolveRequest.jsp?requestId=" + tempRequestId + "\">"
										+ tempRequestId + "</a>";
					String viewRequestUrl = "<a href=\"" + ev.getRequest().getContextPath() + "/ekms/isr/requestorViewRequest.jsp?requestId=" + tempRequestId + "\">"
										+ tempRequestId + "</a>";
					
					if(StatusObject.STATUS_ID_NEW.equals(tempStatusId) ||
	        				StatusObject.STATUS_ID_IN_PROGRESS.equals(tempStatusId) ||
	        				StatusObject.STATUS_ID_CLARIFICATION.equals(tempStatusId) ||
	        				StatusObject.STATUS_ID_REOPEN.equals(tempStatusId)) {
	        			if(permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST)) {
	        				relatedRequestsStr.append(editRequestUrl);
	        			}
	        			else if(permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				relatedRequestsStr.append(viewRequestUrl);
	        			}
	        			else {
	        				relatedRequestsStr.append(tempRequestId);
	        			}
	        		}
	        		else if(StatusObject.STATUS_ID_COMPLETED.equals(tempStatusId)) {
	        			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST)) {
	        				relatedRequestsStr.append(resolveRequestUrl);
	        			}
	        			else if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
	        					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				relatedRequestsStr.append(viewRequestUrl);
	        			}
	        			else {
	        				relatedRequestsStr.append(tempRequestId);
	        			}
	        		}
	        		else if(StatusObject.STATUS_ID_CLOSE.equals(tempStatusId) ||
	        				StatusObject.STATUS_ID_CANCEL.equals(tempStatusId)) {
	        			if (permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST) ||
	        					permissionModel.hasPermission(userId, ISRGroup.PERM_VIEW_REQUEST)) {
	        				relatedRequestsStr.append(viewRequestUrl);
	        			}
	        			else {
	        				relatedRequestsStr.append(tempRequestId);
	        			}
	        		}
	        		else {
	        			relatedRequestsStr.append(tempRequestId);
	        		}
				}
				relatedRequests.setText(relatedRequestsStr.toString());
				addChild(relatedRequests);
			}*/
			
			if(permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST)) {
		        btnSubmit = new Button("btnSubmit", application.getMessage("isr.label.submit"));
		        btnSubmit.setOnClick("return confirmSubmit()");
		        addChild(btnSubmit);
			}
			
			// Clarification and Past Messages
			Collection clarificationMessages = requestObject.getClarification();
			if(clarificationMessages != null) { 
				if(clarificationMessages.size() > 0) {
					Label[] clarificationMessagesQ = new Label[clarificationMessages.size()];
					Label[] clarificationMessagesA = new Label[clarificationMessages.size()];
					DateFormat dmyFormat = new SimpleDateFormat(ClarificationObject.DATE_TIME_FORMAT);
					
					int i=0;
					for(Iterator itr=clarificationMessages.iterator(); itr.hasNext();) {
						ClarificationObject clarificationObject = (ClarificationObject) itr.next();
						if(!"".equals(clarificationObject.getClarificationAnswer()) &&
								clarificationObject.getClarificationAnswer() != null) {
							StringBuffer question = new StringBuffer("[");
							question.append(clarificationObject.getCreatedBy() + " ");
							question.append(dmyFormat.format(clarificationObject.getDateCreated()) + "]");
							question.append("<br />" + HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(clarificationObject.getClarificationQuestion())));
							
							StringBuffer answer = new StringBuffer("<div id=\"altItem\">[");
							answer.append(clarificationObject.getLastUpdatedBy());
							answer.append(dmyFormat.format(clarificationObject.getLastUpdatedDate()) + "]");
							answer.append("<br />" + HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(clarificationObject.getClarificationAnswer())));
							
							clarificationMessagesQ[i] = new Label("clarificationMessagesQ" + i, question.toString());
							clarificationMessagesA[i] = new Label("clarificationMessagesA" + i, answer.toString() + "</span>");
							
							if(clarificationMessagesPanel == null) {
								clarificationMessagesPanel = new Panel("clarificationMessagesPanel");
								clarificationMessagesPanel.setColumns(1);
							}
							clarificationMessagesPanel.addChild(clarificationMessagesQ[i]);
							clarificationMessagesPanel.addChild(clarificationMessagesA[i]);
							
							i++;
						}
						else {
							addClarification = true;
							addRemarks = false;
							clarificationId = clarificationObject.getClarificationId();
							
							StringBuffer question = new StringBuffer("[");
							question.append(clarificationObject.getCreatedBy() + " ");
							question.append(dmyFormat.format(clarificationObject.getDateCreated()) + "]");
							question.append("<br />" + HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(clarificationObject.getClarificationQuestion())));
							
							clarification = new Label("clarification", question.toString());
							replyToClarification = new TextBox("replyToClarification");
							replyToClarification.setCols("70");
							replyToClarification.setRows("5");
							replyToClarification.addChild(new ValidatorNotEmpty("replyToClarificationVNE", application.getMessage("isr.validator.notEmpty")));
							
							addChild(clarification);
							addChild(replyToClarification);
						}
					}
					
					if(clarificationMessagesPanel != null) {
						addChild(clarificationMessagesPanel);
					}
				}
			}
			
			// Remarks
			if(addRemarks) {
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
							
							StringBuffer previousRemarks = new StringBuffer("[");
							previousRemarks.append(dmyFormat.format(remarksObject.getDateCreated()) + "] ");
							previousRemarks.append(HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(remarksObject.getRemarks())));
							
							lblRemarksMessages[i] = new Label("lblRemarksMessages" + i, previousRemarks.toString());
							remarksPanel.addChild(lblRemarksMessages[i]);
						}
						
						addChild(remarksPanel);
					}
				}
				
				if(permissionModel.hasPermission(userId, ISRGroup.PERM_EDIT_REQUEST)) {
					addRemarksTextBox = new TextBox("addRemarksTextBox");
					addRemarksTextBox.setCols("70");
					addRemarksTextBox.setRows("5");
					addChild(addRemarksTextBox);
				}
			}
		}
    }
    
    private Map getFilteredDeptMap(Map oriDeptMap, String delimitedRelatedRequestIds) {
    	RequestModel requestModel = (RequestModel) Application.getInstance().getModule(RequestModel.class);
    	Collection relatedDept = requestModel.getRelatedDept(delimitedRelatedRequestIds);
    	
    	if(relatedDept != null) {
    		for(Iterator itr=relatedDept.iterator(); itr.hasNext(); ) {
    			DepartmentCountryAssociativityObject obj = (DepartmentCountryAssociativityObject) itr.next();
    			oriDeptMap.remove(obj.getAssociativityId());
    		}
    	}
    	
    	return oriDeptMap;
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
    	saveWithAttachment = false;
    	boolean illegalFileType = false;
        ArrayList illegalFiles = null;
        if(checkSendAnotherDept==null || !checkSendAnotherDept.isChecked()) {
        	if(checkSendAnotherDept!=null)
        	attentionToVNE.setInvalid(false);
        	this.setInvalid(false);
	    	try {
	    		AttachmentObject attachmentObject = new AttachmentObject();
	    		Map allowedFileExt = attachmentObject.getAllowedFileExt();
	    		
	    		if(attachments != null) {
			    	for(int i=0; i<attachments.length; i++) {
			    		StorageFile sf = attachments[i].getStorageFile(event.getRequest());
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
	    	
	    	if(!saveWithAttachment) {
	    		if(addRemarks) {
	    			if("".equals(addRemarksTextBox.getValue()) && dueDate.getDate()==null) {
	    				return new Forward(FORWARD_EMPTY_INPUT);
	    			}
	    		}
	    	}
	    	
	    	if(illegalFileType) {
	    		event.getRequest().setAttribute("illegalFiles", illegalFiles);
	    		return new Forward(ILLEGAL_FILE_TYPE);
	    	}
	    	else {
	    		return forward;
	    	}
        }
        return forward;
    }
    
    public Forward onValidate(Event event) {
    	if(requestId != null &&
    			!"".equals(requestId)) {
	    	Application application = Application.getInstance();
	    	RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
	    	boolean isSuccess = true;
	    	int errorCode = 0;

	    	if(checkSendAnotherDept!=null && checkSendAnotherDept.isChecked()) {
	    		RequestObject duplicatingRequest = new RequestObject();
	    		duplicatingRequest.setRequestFromDept(requestObject.getRequestFromDept());
	    		duplicatingRequest.setRequestMultipleToDept(multipleAttentionTo.getRightValues());
	    		duplicatingRequest.setRequestSubject(requestObject.getRequestSubject());
	    		duplicatingRequest.setRequestDescription(requestObject.getRequestStatus());
	    		duplicatingRequest.setRequestPriority(requestObject.getRequestPriority());
	    		duplicatingRequest.setRequestStatus(StatusObject.STATUS_ID_NEW);
	            errorCode = requestModel.cloneRequest(requestObject, duplicatingRequest);
	            
	            if(errorCode == 0) {
	        		return new Forward(FORWARD_SUCCESS);
	        	}
	            else if(errorCode == 2) {
	                return new Forward(FORWARD_ATTACHMENT_ERROR);
	            }
	        	else {
	        		return new Forward(FORWARD_ERROR);
	        	}
	    	}
	    	else {
		    	if(saveWithAttachment) {
		    		isSuccess = requestModel.uploadAttachments(event.getRequest(), requestId, attachments, event);
		    	}
		    	if(addRemarks) {
			    	if(!"".equals(addRemarksTextBox.getValue().toString())) {
			    		RemarksObject remarksObject = new RemarksObject();
			    		remarksObject.setRequestId(requestId);
			    		remarksObject.setRemarks(addRemarksTextBox.getValue().toString());
			    		isSuccess = requestModel.insertRemarks(remarksObject);
			    		// AutoSendEmail for updating new remarks requestObject
			    		SendEmailMemo  mailObj = new SendEmailMemo();
			    		mailObj.sendNotification(EmailSetting.REMARKS, remarksObject, requestObject , null, null, null);
			    		
			    	}
			    	if(!(dueDate.getDate()==null)){
			    		isSuccess = requestModel.updateDueDate(requestId, dueDate.getDate());
			    	}
		    	}
		    	if(addClarification) {
		    		if(!"".equals(clarificationId)) {
			    		ClarificationObject clarificationObject = new ClarificationObject();
			    		clarificationObject.setRequestId(requestId);
			    		clarificationObject.setClarificationId(clarificationId);
			    		clarificationObject.setClarificationAnswer(replyToClarification.getValue().toString());
			    		isSuccess = requestModel.updateClarificationReply(clarificationObject);
			    		//AutoSend Email	
			    		SendEmailMemo  mailObj = new SendEmailMemo();
				        mailObj.sendNotification(EmailSetting.CLARIFICATION_REPLY, null, requestObject,
			    		clarificationObject, null, null);
				        
			    		if(isSuccess) {
			    			requestModel.updateRequestStatus(StatusObject.STATUS_ID_CLARIFICATION, StatusObject.STATUS_ID_IN_PROGRESS, new String[] {requestId});
			    		}
		    		}
		    		else {
		    			isSuccess = false;
		    		}
		    	}
	    	}
	    	
	    	if(isSuccess) {
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

	public boolean isAddClarification() {
		return addClarification;
	}

	public boolean isAddRemarks() {
		return addRemarks;
	}

	public TextBox getAddRemarksTextBox() {
		return addRemarksTextBox;
	}

	public FileUpload[] getAttachments() {
		return attachments;
	}

	public Panel getAttachmentPanel() {
		return attachmentPanel;
	}

	public Label getAttentionTo() {
		return attentionTo;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnSend() {
		return btnSend;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public Label getClarification() {
		return clarification;
	}

	public Panel getClarificationMessagesPanel() {
		return clarificationMessagesPanel;
	}

	public Label getDateCreated() {
		return dateCreated;
	}

	public Label getDescription() {
		return description;
	}

	public String getMaxUploadSize() {
		return maxUploadSize;
	}

	public Label getPriority() {
		return priority;
	}

	public Panel getRemarksPanel() {
		return remarksPanel;
	}

	public TextBox getReplyToClarification() {
		return replyToClarification;
	}

	public Label getRequestingDepartment() {
		return requestingDepartment;
	}

	public Label getStatus() {
		return status;
	}

	public Label getSubject() {
		return subject;
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

	public Collection getRelatedRequests() {
		return relatedRequests;
	}

	public boolean isRemarksDesc() {
		return remarksDesc;
	}

	public void setRemarksDesc(boolean remarksDesc) {
		this.remarksDesc = remarksDesc;
	}

	public CheckBox getCheckSendAnotherDept() {
		return checkSendAnotherDept;
	}

	public ComboSelectBox getMultipleAttentionTo() {
		return multipleAttentionTo;
	}

	public DatePopupField getDueDate() {
		return dueDate;
	}

	public void setDueDate(DatePopupField dueDate) {
		this.dueDate = dueDate;
	}
}
