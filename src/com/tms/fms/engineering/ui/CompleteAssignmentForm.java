package com.tms.fms.engineering.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.core.model.InvalidKeyException;
import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.VtrService;

public class CompleteAssignmentForm extends Form {	
	private String id;
	
	protected DatePopupField completionDate;
	protected TimeField completionTime;
	protected TextBox remarks;
	protected FileUpload attachment;
	
	protected Panel attachmentPanel;
	
	private Label assignmentId;
	private Label requestTitle;
	private Label program;
	private Label description;
	private Label assignTo;
	private Label requiredDateFrom;
	private Label requiredDateTo;
	private Label requiredTimeFrom;
	private Label requiredTimeTo;
	private Label manpower;
	private Label lbAttachmentList;
	
	protected Button attach;
	protected Button submit;
	protected Button cancel;
	
	private Collection files = new ArrayList();
	private String cancelUrl 				= "myAssignmentDetails.jsp?id=";
	private String completeAssignmentUrl 	= "completeMyAssignment.jsp?id=";
	private String unfulfilledAssignmentUrl = "unfulfillMyAssignment.jsp?id=";
	private String fileId 	= "";
	private String action 	= "";
	private Date requiredFrom;
	private Date requiredTo;
	
	public void onRequest(Event event) {
		
		files = null;
		
		fileId = event.getRequest().getParameter("idfile");
		action = event.getRequest().getParameter("do");
		
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		EngineeringDao dao = (EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
		
		if (action != null && !action.equals("")){
			if (action.equals("delete")){
				if (fileId != null && !fileId.equals("")){
					VtrService delFile = module.getFile(fileId);
					if (delFile.getFileId() != null){
						StorageFile file = new StorageFile(module.ENGINEERING_ASSIGNMENT_FILE_PATH  + delFile.getFileId(), delFile.getFileName());
						StorageFile file2 = new StorageFile(module.ENGINEERING_ASSIGNMENT_FILE_PATH, delFile.getFileId());
						try {
							dao.deleteFile(file);
							dao.deleteFile(file2);
							module.deleteVtrFile(delFile.getFileId());
						} catch (DaoException e) {
							Log.getLog(getClass()).error("Error on deleting file - DaoException ");
						} catch (InvalidKeyException e) {
							Log.getLog(getClass()).error("Error on deleting file - InvalidKeyException ");
						}
					}
				}
			}
		}
		
		if (id!=null) {
			files = module.getManpowerFiles(id);
		}
		init();
		populateFields();
	}

	public void init() {
		Application app=Application.getInstance();
		setMethod("post");
				
		assignmentId = new Label("assignmentId");
		addChild(assignmentId);
		
		requestTitle = new Label("requestTitle");
		addChild(requestTitle);
		
		program = new Label("program");
		addChild(program);
		
		requiredDateFrom = new Label("requiredDateFrom");
		addChild(requiredDateFrom);
		
		requiredDateTo = new Label("requiredDateTo");
		addChild(requiredDateTo);
		
		requiredTimeFrom = new Label("requiredTimeFrom");
		addChild(requiredTimeFrom);
		
		requiredTimeTo = new Label("requiredTimeTo");
		addChild(requiredTimeTo);

		completionDate = new DatePopupField("completionDate");
		completionDate.setFormat("dd-MM-yyyy");
    	completionDate.setDate(new Date());
		addChild(completionDate);
		
		completionTime = new TimeField("completionTime");
		addChild(completionTime);
		
		remarks = new TextBox("remarks");
		remarks.setRows("4");
		remarks.setCols("50");
		addChild(remarks);
		
		lbAttachmentList= new Label("attachmentList");
		String content="<table class='borderTable' width='60%'>";
		
		//Iterate files
		if (files.size() > 0) {
			Iterator itr = files.iterator();
			while (itr.hasNext()){
				EngineeringRequest file = (EngineeringRequest)itr.next();
				content += "<tr><td><a href='" + file.getFilePath() + "'>" + file.getFileName() +"</a></td>";
				content += "<td><a href='?id="+ id + "&amp;do=delete&amp;idfile=" + file.getFileId() + "'>Remove</a><br /></td></tr>";
			}
		}
		
		content+="</table>";
		lbAttachmentList.setText(content);
		lbAttachmentList.setEscapeXml(false);
		
		attachment = new FileUpload("attachment");
		
		attachmentPanel=new Panel("attachmentPanel");
		attachmentPanel.addChild(lbAttachmentList);
		attachmentPanel.addChild(attachment);
		addChild(attachmentPanel);
		
		attach = new Button("attach", "Attach");
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		addChild(attach);
		addChild(submit);
		addChild(cancel);
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		EngineeringRequest eRequest = module.getAssignment(id);
		
		if (eRequest != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			String reqDate = sdf.format(eRequest.getRequiredFrom()) + " - " + sdf.format(eRequest.getRequiredTo());
			
			assignmentId.setText(eRequest.getAssignmentCode());
			requestTitle.setText(eRequest.getTitle());
			program.setText(eRequest.getProgramName());		
			requiredDateFrom.setText(sdf.format(eRequest.getRequiredFrom()));
			requiredDateTo.setText(sdf.format(eRequest.getRequiredTo()));
			requiredTimeFrom.setText(eRequest.getFromTime());
			requiredTimeTo.setText(eRequest.getToTime());
			remarks.setValue(eRequest.getRemarks());
			
			requiredFrom = eRequest.getRequiredFrom();
			requiredFrom.setHours(Integer.parseInt(eRequest.getFromTime().substring(0,2)));
			requiredFrom.setMinutes(Integer.parseInt(eRequest.getFromTime().substring(3)));
			
			requiredTo = eRequest.getRequiredTo();
			requiredTo.setHours(Integer.parseInt(eRequest.getToTime().substring(0,2)));
			requiredTo.setMinutes(Integer.parseInt(eRequest.getToTime().substring(3)));
		}
	}
	
	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);
		Application application = Application.getInstance();
		EngineeringModule module = (EngineeringModule)application.getModule(EngineeringModule.class);
		EngineeringDao dao = (EngineeringDao)application.getModule(EngineeringModule.class).getDao();
		EngineeringRequest er = new EngineeringRequest();
		
	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);

	    //if the cancel button was pressed
	    if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	      	return new Forward(Form.CANCEL_FORM_ACTION, getCancelUrl() + getId(), true);
//	    } else if (buttonName != null && attach.getAbsoluteName().equals(buttonName)) {
//	    	upload(er, module, dao, evt);
//	    	return new Forward("ATTACH");
	    } else {
	    	return result;
	    }
	}
	
	@Override
	public Forward onValidate(Event evt) {
		Application application = Application.getInstance();
		EngineeringModule module = (EngineeringModule)application.getModule(EngineeringModule.class);
		EngineeringDao dao = (EngineeringDao)application.getModule(EngineeringModule.class).getDao();
		EngineeringRequest er = new EngineeringRequest();
		
		String buttonName = findButtonClicked(evt);
		
		if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
			Date today = new Date();
			Date test = completionDate.getDate();
			test.setHours(completionTime.getHour());
			test.setMinutes(completionTime.getMinute());
			
			er.setAssignmentId(id);		
			er.setCompletionDate(test);
			er.setDescription((String)remarks.getValue());
			
			if(!module.isValidCompletion(requiredFrom, today, test)){
				return new Forward("INVALID-COMPLETION-DATE");
			}
			upload(er,module,dao,evt);
			module.completeAssignment(er);
			
			// check completed request
			EngineeringRequest erC = module.getAssignment(id);
			
			if (erC!=null) {
				module.updateRequestStatus(erC.getRequestId());
			}
			
			return new Forward("COMPLETE");
			
		} 
		
		return new Forward("");
	}
	
	public void upload(EngineeringRequest er, EngineeringModule module, EngineeringDao dao, Event evt) {
		try {
			if (attachment.getValue() != null){
				er.setFileId(UuidGenerator.getInstance().getUuid());
				StorageFile file = new StorageFile(module.ENGINEERING_ASSIGNMENT_FILE_PATH + er.getFileId(), attachment.getStorageFile(evt.getRequest()));
				
				er.setFileName(file.getName());
				er.setFilePath(file.getAbsolutePath());
				er.setAssignmentId(id);
			
				dao.storeFile(file);
				
				module.insertAttachment(er);
			}
			
		} catch (FileNotFoundException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} catch (StorageException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} catch (IOException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} catch (InvalidKeyException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/completeassignmenttemp";
	}

	public Label getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Label assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Label getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(Label requestTitle) {
		this.requestTitle = requestTitle;
	}

	public Label getProgram() {
		return program;
	}

	public void setProgram(Label program) {
		this.program = program;
	}

	public Label getRequiredDateFrom() {
		return requiredDateFrom;
	}

	public void setRequiredDateFrom(Label requiredDateFrom) {
		this.requiredDateFrom = requiredDateFrom;
	}

	public Label getRequiredDateTo() {
		return requiredDateTo;
	}

	public void setRequiredDateTo(Label requiredDateTo) {
		this.requiredDateTo = requiredDateTo;
	}

	public Label getRequiredTimeFrom() {
		return requiredTimeFrom;
	}

	public void setRequiredTimeFrom(Label requiredTimeFrom) {
		this.requiredTimeFrom = requiredTimeFrom;
	}

	public Label getRequiredTimeTo() {
		return requiredTimeTo;
	}

	public void setRequiredTimeTo(Label requiredTimeTo) {
		this.requiredTimeTo = requiredTimeTo;
	}

	public Label getManpower() {
		return manpower;
	}

	public void setManpower(Label manpower) {
		this.manpower = manpower;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Label getDescription() {
		return description;
	}

	public void setDescription(Label description) {
		this.description = description;
	}

	public Label getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Label assignTo) {
		this.assignTo = assignTo;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getCompleteAssignmentUrl() {
		return completeAssignmentUrl;
	}

	public void setCompleteAssignmentUrl(String completeAssignmentUrl) {
		this.completeAssignmentUrl = completeAssignmentUrl;
	}

	public String getUnfulfilledAssignmentUrl() {
		return unfulfilledAssignmentUrl;
	}

	public void setUnfulfilledAssignmentUrl(String unfulfilledAssignmentUrl) {
		this.unfulfilledAssignmentUrl = unfulfilledAssignmentUrl;
	}

	public DatePopupField getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(DatePopupField completionDate) {
		this.completionDate = completionDate;
	}

	public TimeField getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(TimeField completionTime) {
		this.completionTime = completionTime;
	}
	

	public TextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(TextBox remarks) {
		this.remarks = remarks;
	}

	public FileUpload getAttachment() {
		return attachment;
	}

	public void setAttachment(FileUpload attachment) {
		this.attachment = attachment;
	}

	public Label getLbAttachmentList() {
		return lbAttachmentList;
	}

	public void setLbAttachmentList(Label lbAttachmentList) {
		this.lbAttachmentList = lbAttachmentList;
	}

	public Collection getFiles() {
		return files;
	}

	public void setFiles(Collection files) {
		this.files = files;
	}

	public Panel getAttachmentPanel() {
		return attachmentPanel;
	}

	public void setAttachmentPanel(Panel attachmentPanel) {
		this.attachmentPanel = attachmentPanel;
	}

	public Button getAttach() {
		return attach;
	}

	public void setAttach(Button attach) {
		this.attach = attach;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
}
