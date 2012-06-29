package com.tms.collab.isr.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.stdui.Button;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.model.AttachmentObject;
import com.tms.collab.isr.model.RemarksObject;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.RequestObject;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.collab.isr.setting.model.EmailSetting;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class NewRequestForm extends Form {
	protected String requestingDepartment = "";
	protected SelectBox attentionTo; // Selection of single dept is not in used
	protected ComboSelectBox multipleAttentionTo;
	protected TextField subject;
	protected TextBox description;
	protected SelectBox priority;
	protected FileUpload[] attachments;
	protected DatePopupField dueDate;
	protected Button btnSubmit;
	protected Button btnCancel;
	protected String maxUploadSize = "1MB";
	public static final int MAX_UPLOAD = 3;
	public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_ATTACHMENT_ERROR = "attachment error";
    public static final String ILLEGAL_FILE_TYPE = "illegal file type";
    public static final String ILLEGAL_FILE_SIZE = "illegal file size";
    
    public void init(){
    	super.init();
	}
    
    public void onRequest(Event ev) {
    	initForm();
    }
    
    public void initForm() {
    	setMethod("POST");
		removeChildren();
		setColumns(2);
		
		Application application = Application.getInstance();
		RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
		ConfigModel configModel = (ConfigModel) application.getModule(ConfigModel.class);
		
		requestingDepartment = requestModel.getDeptName(application.getCurrentUser().getId());
		
		/*attentionTo = new SelectBox("attentionTo");
		attentionTo.setMultiple(false);
		attentionTo.setOptionMap(getAttendingDeptOptionsMap());
		attentionTo.addChild(new ValidatorNotEmptySelectBox("attentionToVNE", application.getMessage("isr.validator.notEmpty")));*/
		
		multipleAttentionTo = new MultipleDeptComboSelect("multipleAttentionTo");
		
		subject = new TextField("subject");
		subject.setMaxlength("100");
		subject.addChild(new ValidatorNotEmpty("subjectVNE", application.getMessage("isr.validator.notEmpty")));
		
		description = new TextBox("description");
		description.setCols("70");
		description.setRows("5");
		description.addChild(new ValidatorNotEmpty("descriptionVNE", application.getMessage("isr.validator.notEmpty")));
		
		priority = new SelectBox("priority");
		priority.setMultiple(false);
		priority.setOptionMap(configModel.getPriorityOptionsMap());
		priority.addChild(new ValidatorNotEmptySelectBox("priorityVNE", application.getMessage("isr.validator.notEmpty")));
		
		attachments = new FileUpload[3];
		Panel attachmentPanel = new Panel("attachmentPanel");
		attachmentPanel.setColumns(1);
		attachmentPanel.addChild(new Label("lblAllowedAttachmentFileExt", 
				"<p>" + application.getMessage("isr.label.allowedFileExtensions") + " - " +
				getAllowedAttachmentFileExtList() + "</p>"));
		for(int i=0; i<MAX_UPLOAD; i++) {
			attachments[i] = new FileUpload("attachments" + i);
			attachments[i].setSize("50");
			attachments[i].setOnChange("javascript:checkDuplicatedFilenames(this)");
			attachmentPanel.addChild(attachments[i]);
		}
		
		Collection maxFileSizeSelect = configModel.getConfigDetailsByType(ConfigDetailObject.FILE_SIZE_UPLOAD, null);
		if(maxFileSizeSelect != null) {
			if(maxFileSizeSelect.size() > 0) {
				ConfigDetailObject fileSizeConfig = (ConfigDetailObject) maxFileSizeSelect.iterator().next();
				maxUploadSize = fileSizeConfig.getConfigDetailName();
			}
		}
		dueDate=new DatePopupField("dueDate");
		dueDate.setOptional(true);
		Panel btnPanel = new Panel("btnPanel");
        btnPanel.setColumns(2);
        
        btnSubmit = new Button("btnSubmit", application.getMessage("isr.label.submit"));
        btnSubmit.setOnClick("return confirmSubmit()");
        btnPanel.addChild(btnSubmit);
        
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, application.getMessage("isr.label.cancel"));
        btnCancel.setOnClick("return confirmCancel()");
        btnPanel.addChild(btnCancel);
        
        Label lblRequestingDept = new Label("lblRequestingDept", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.requestingDept", "Requesting Department") + "</span>");
        lblRequestingDept.setAlign("right");
        addChild(lblRequestingDept);
        addChild(new Label("lblRequestingDeptValue", "<strong>" + requestingDepartment + "</strong>"));
        
        Label lblAttentionTo = new Label("lblAttentionTo", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.attentionTo", "Attention To") + " *" + "</span>");
        lblAttentionTo.setAlign("right");
        addChild(lblAttentionTo);
        addChild(multipleAttentionTo);
        multipleAttentionTo.init();
		multipleAttentionTo.getRightSelect().addChild(new ValidatorNotEmpty("attentionToVNE", application.getMessage("isr.validator.notEmpty")));
		
		Label lblSubject = new Label("lblSubject", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.subject", "Subject") + " *" + "</span>");
		lblSubject.setAlign("right");
        addChild(lblSubject);
        addChild(subject);
        
        Label lblDescription = new Label("lblDescription", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.description", "Description") + " *" + "</span>");
        lblDescription.setAlign("right");
        addChild(lblDescription);
        addChild(description);
        
        Label lblPriority = new Label("lblPriority", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.priority", "Priority") + " *" + "</span>");
        lblPriority.setAlign("right");
        addChild(lblPriority);
        addChild(priority);
        
        Label lblAttachment = new Label("lblAttachment", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.attachment", "Attachment") + " (max " + maxUploadSize + " each)" + "</span>");
        lblAttachment.setAlign("right");
        addChild(lblAttachment);
        addChild(attachmentPanel);
        
        Label lblDueDate = new Label("lblDueDate", "<span class=\"fieldTitle\">" + application.getMessage("isr.message.dueDate", "Due Date") + "</span>");
        lblDueDate.setAlign("right");
        addChild(lblDueDate);
        addChild(dueDate);
        addChild(new Label("dummyLb1", ""));
        addChild(btnPanel);
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
    
    private Map getAttendingDeptOptionsMap() {
    	Map optionsMap = new SequencedHashMap();
    	
    	Application application = Application.getInstance();
    	OrgChartHandler orgChartModel = (OrgChartHandler) application.getModule(OrgChartHandler.class);
    	
    	Collection deptCols = orgChartModel.selectDepartmentCountryAssociativity(null, null, null, "countryDesc, deptDesc", false, 0, -1);
    	DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(application.getCurrentUser().getId());
    	
    	optionsMap.put("", "---" + application.getMessage("isr.label.pleaseSelect") + "---");
    	for(Iterator i=deptCols.iterator(); i.hasNext();) {
    		DepartmentCountryAssociativityObject obj = (DepartmentCountryAssociativityObject) i.next();
    		if(! obj.getAssociativityId().equals(requesterDept.getAssociativityId())) {
    			optionsMap.put(obj.getAssociativityId(), 
    					obj.getCountryDesc() + " - " + obj.getDeptDesc());
    		}
    	}
    	
    	return optionsMap;
    }
    
    public Forward onSubmit(Event event) {
    	Forward forward = super.onSubmit(event);
        boolean illegalFileType = false;
        ArrayList illegalFiles = null;
        
    	try {
    		AttachmentObject attachmentObject = new AttachmentObject();
    		Map allowedFileExt = attachmentObject.getAllowedFileExt();
    		if(attachments !=null)
    		{	
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
	    		}
	    	}
    	}
    	}
    	catch(IOException error) {
    		Log.getLog(getClass()).error(error, error);
    	}
    	
    	if(illegalFileType) {
    		event.getRequest().setAttribute("illegalFiles", illegalFiles);
    		return new Forward(ILLEGAL_FILE_TYPE);
    	}
    	else {
    		return forward;
    	}
    }
    
    public Forward onValidate(Event event) {
    	Application application = Application.getInstance();
    	RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
    	OrgChartHandler orgChartModel = (OrgChartHandler) application.getModule(OrgChartHandler.class);
		RequestObject request = new RequestObject();
        int errorCode = 0;
		
		DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(application.getCurrentUser().getId());
		//String selectedAttendingDept = (String) attentionTo.getSelectedOptions().keySet().iterator().next();
		String selectedPriority = (String) priority.getSelectedOptions().keySet().iterator().next();
		
		request.setRequestFromDept(requesterDept.getAssociativityId());
    	//request.setRequestToDept(selectedAttendingDept);
		request.setRequestMultipleToDept(multipleAttentionTo.getRightValues());
    	request.setRequestSubject(subject.getValue().toString());
    	request.setRequestDescription(description.getValue().toString());
    	request.setRequestPriority(selectedPriority);
    	request.setRequestStatus(StatusObject.STATUS_ID_NEW);
    	request.setDueDate(dueDate.getDate());
        errorCode = requestModel.insertRequest(request, attachments, event);
        
    	if(errorCode == 0) {
    		//AutoSendEmail for new Request submitted
    		SendEmailMemo  mailObj = new SendEmailMemo();
    		mailObj.sendNotification(EmailSetting.NEW_REQUEST, null, request, null, null, null);
    		//testingEmail abc = new testingEmail();
    		//abc.execute();
    		return new Forward(FORWARD_SUCCESS);
    	}
        else if(errorCode == 2) {
            return new Forward(FORWARD_ATTACHMENT_ERROR);
        }
    	else {
    		return new Forward(FORWARD_ERROR);
    	}
	}

	public FileUpload[] getAttachments() {
		return attachments;
	}

	public SelectBox getAttentionTo() {
		return attentionTo;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public TextBox getDescription() {
		return description;
	}

	public String getMaxUploadSize() {
		return maxUploadSize;
	}

	public ComboSelectBox getMultipleAttentionTo() {
		return multipleAttentionTo;
	}

	public SelectBox getPriority() {
		return priority;
	} 

	public String getRequestingDepartment() {
		return requestingDepartment;
	}

	public TextField getSubject() {
		return subject;
	}
}