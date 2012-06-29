package com.tms.collab.isr.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import com.tms.collab.isr.model.AttachmentObject;
import com.tms.collab.isr.model.ClarificationObject;
import com.tms.collab.isr.model.RemarksObject;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.RequestObject;
import com.tms.collab.isr.model.ResolutionAttachmentObject;
import com.tms.collab.isr.model.SuggestedResolutionObject;
import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.collab.isr.util.HtmlFormatting;

public class RequestorRequestResolve extends Form {
	private String requestId = "";
	protected boolean suggestedResolutionDesc = false;
	protected boolean remarksDesc = false;
	protected boolean clarificationDesc = false;
	protected Label requestingDepartment;
	protected Label attentionTo;
	protected Label subject;
	protected Label description;
	protected Label status;
	protected Label priority;
	protected Label[] uploadedAttachments;
	protected Panel attachmentPanel;
	protected Label dateCreated;
	protected Label dueDate;
	protected Collection relatedRequests;
	protected Label resolution;
	protected Panel resolutionAttachmentPanel;
	protected Panel suggestedResolutionPanel;
	protected Radio acceptResolution;
	protected Radio rejectResolution;
	protected Panel remarksPanel;
	protected Panel clarificationMessagesPanel;
	protected Button btnReopen;
	protected Button btnCancel;
	protected String autoClosureDay = "30";
	public static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_REJECTED = "rejected";
    public static final String FORWARD_ERROR = "error";
    
    public String getDefaultTemplate() {
        return "isr/requestorRequestResolve";
    }

    public void init(){
    	super.init();
	}
    
    public void onRequest(Event ev) {
    	relatedRequests = null;
    	attachmentPanel = null;
    	resolutionAttachmentPanel = null;
    	suggestedResolutionPanel = null;
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
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			ConfigModel configModel = (ConfigModel) application.getModule(ConfigModel.class);
			PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
			RequestObject requestObject = requestModel.getRequest(requestId, true, remarksDesc, true, clarificationDesc);
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
			
			Collection autoClosureSelect = configModel.getConfigDetailsByType(ConfigDetailObject.REQUEST_AUTO_CLOSE, null);
			if(autoClosureSelect != null) {
				if(autoClosureSelect.size() > 0) {
					ConfigDetailObject autoClosureConfig = (ConfigDetailObject) autoClosureSelect.iterator().next();
					autoClosureDay = autoClosureConfig.getConfigDetailName();
				}
			}
			
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
			dueDate = new Label("dueDate");
			if(requestObject.getDueDate()!=null)
				dueDate.setText(dueDateFormat.format(requestObject.getDueDate()));
			else{
					dueDate.setText("");
				}
			addChild(dueDate);
			
			relatedRequests = requestObject.getRelatedRequestsCol();
			
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
			
			DateFormat resolutionDateFormat = new SimpleDateFormat(application.getProperty("globalDatetimeLong"));
			StringBuffer resolutionMessage = new StringBuffer();
			if(requestObject.getRequestResolutionDate() != null) {
				resolutionMessage.append("<span style=\"color:#ED7B00;\">[");
				resolutionMessage.append(resolutionDateFormat.format(requestObject.getRequestResolutionDate()) + "]<br />");
			}
			else {
				resolutionMessage.append("<span style=\"color:#ED7B00;\">");
			}
			resolutionMessage.append(HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(requestObject.getRequestResolution())) + "</span> ");
			resolution = new Label("resolution", resolutionMessage.toString());
			addChild(resolution);
			
			Collection resolutionAttachmentCols = requestObject.getResolutionAttachments();
			if(resolutionAttachmentCols != null) {
				if(resolutionAttachmentCols.size() > 0) {
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
					
					addChild(resolutionAttachmentPanel);
				}
			}
			
			if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST)) {
		        btnReopen = new Button("btnReopen", application.getMessage("isr.label.reopen"));
		        btnReopen.setOnClick("return confirmReject()");
		        addChild(btnReopen);
			}
			
			// Clarification Messages
			Collection clarificationMessages = requestObject.getClarification();
			if(clarificationMessages != null) { 
				if(clarificationMessages.size() > 0) {
					Label[] clarificationMessagesQ = new Label[clarificationMessages.size()];
					Label[] clarificationMessagesA = new Label[clarificationMessages.size()];
					DateFormat dmyFormat = new SimpleDateFormat(ClarificationObject.DATE_TIME_FORMAT);
					
					int i=0;
					for(Iterator itr=clarificationMessages.iterator(); itr.hasNext(); i++) {
						if(clarificationMessagesPanel == null) {
							clarificationMessagesPanel = new Panel("clarificationMessagesPanel");
							clarificationMessagesPanel.setColumns(1);
						}
						
						ClarificationObject clarificationObject = (ClarificationObject) itr.next();
						
						StringBuffer question = new StringBuffer("<div>[");
						question.append(clarificationObject.getCreatedBy() + " ");
						question.append(dmyFormat.format(clarificationObject.getDateCreated()) + "]");
						question.append("<br />" + HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(clarificationObject.getClarificationQuestion())));
						clarificationMessagesQ[i] = new Label("clarificationMessagesQ" + i, question.toString() + "</div>");
						clarificationMessagesPanel.addChild(clarificationMessagesQ[i]);
						
						if(!"".equals(clarificationObject.getClarificationAnswer()) &&
								clarificationObject.getClarificationAnswer() != null) {	
							StringBuffer answer = new StringBuffer("<div id=\"altItem\">[");
							answer.append(clarificationObject.getLastUpdatedBy());
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
						
						StringBuffer previousRemarks = new StringBuffer("[");
						previousRemarks.append(dmyFormat.format(remarksObject.getDateCreated()) + "] ");
						previousRemarks.append(HtmlFormatting.getEscapedXmlText(HtmlFormatting.getHtmlText(remarksObject.getRemarks())));
						
						lblRemarksMessages[i] = new Label("lblRemarksMessages" + i, previousRemarks.toString());
						remarksPanel.addChild(lblRemarksMessages[i]);
					}
					
					addChild(remarksPanel);
				}
			}
			
			// Suggested Resolution
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
		}
    }
    
    public Forward onValidate(Event event) {
    	if(requestId != null &&
    			!"".equals(requestId)) {
    		Application application = Application.getInstance();
    		RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
	    	boolean isSuccess = requestModel.acceptRejectRequestResolution(requestId, false);
	    	
	    	if(isSuccess) {
	    		return new Forward(FORWARD_REJECTED);
	    	}
	    	else {
	    		return new Forward(FORWARD_ERROR);
	    	}
    	}
    	else {
    		return new Forward(FORWARD_ERROR);
    	}
    }
    
	public String getAutoClosureDay() {
		return autoClosureDay;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Radio getAcceptResolution() {
		return acceptResolution;
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

	public Button getBtnReopen() {
		return btnReopen;
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
	
	public Label getPriority() {
		return priority;
	}

	public Radio getRejectResolution() {
		return rejectResolution;
	}

	public Panel getRemarksPanel() {
		return remarksPanel;
	}

	public Label getRequestingDepartment() {
		return requestingDepartment;
	}

	public Label getResolution() {
		return resolution;
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

	public Panel getSuggestedResolutionPanel() {
		return suggestedResolutionPanel;
	}

	public boolean isSuggestedResolutionDesc() {
		return suggestedResolutionDesc;
	}

	public void setSuggestedResolutionDesc(boolean suggestedResolutionDesc) {
		this.suggestedResolutionDesc = suggestedResolutionDesc;
	}

	public Label getDueDate() {
		return dueDate;
	}

	public void setDueDate(Label dueDate) {
		this.dueDate = dueDate;
	}
    
}
