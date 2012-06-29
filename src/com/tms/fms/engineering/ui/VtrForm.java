package com.tms.fms.engineering.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.FileUpload;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.core.model.InvalidKeyException;
import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.VtrService;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;
import com.tms.fms.widgets.BoldLabel;
import com.tms.fms.widgets.ExtendedSelectBox;

public class VtrForm extends ServiceForm {
	protected EngineeringRequest request = new EngineeringRequest();
	protected SingleFacilitySelectBox facilitySelectBox;
	protected ExtendedSelectBox serviceParticulars;
	protected DatePopupField requiredDate;
	protected DatePopupField requiredDateTo;
	protected TextField duration;
	protected TextField noOfCopies;
	protected TextField location;
	protected TextBox remarks;
	protected TimeField requiredFrom;
	protected TimeField requiredTo;
	protected SelectBox formatFrom;
	protected SelectBox formatTo;
	protected SelectBox formatIngestFrom;
	protected SelectBox formatIngestTo;
	protected SelectBox conversionFrom;
	protected SelectBox conversionTo;
	protected FileUpload attachment;
	protected Radio rdBBYes;
	protected Radio rdBBNo;
	protected Panel pnBB;
	protected Panel timePanel1;
	protected Panel timePanel2;
	protected Panel timePanel3;
	protected Panel timePanel4;
	protected Panel attachmentPanel;
	protected Panel buttonPanel;
	protected Label lbFacility;
	protected Label lbBlockBooking;
	protected Label lbServiceParticulars;
	//protected Label lbRequiredDate;
	protected Label lbDateRequiredFrom;
	protected Label lbDateRequiredTo;
	protected Label lbRequiredTime;
	protected Label lbFormat;
	protected Label lbConversion;
	protected Label lbDuration;
	protected Label lbNoOfCopies;
	protected Label lbAttachment;
	protected Label lbRemarks;
	protected Label lbAttachmentList;
	protected Label lbLocation;
	
	private Collection files = new ArrayList();
	private String fileId = "";
	private String action = "";
	private String lbService = "";
	
	
	public void init() {
		if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {
			type = "Add";
		}
	}
	
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
						StorageFile file = new StorageFile(module.VTR_FILE_PATH + "/" + delFile.getFileId(), delFile.getFileName());
						StorageFile file2 = new StorageFile(module.VTR_FILE_PATH, delFile.getFileId());
						try {
							dao.deleteFile(file);
							dao.deleteFile(file2);
							module.deleteVtrFile(delFile.getFileId());
						} catch (DaoException e) {
							Log.getLog(getClass()).error("Error on deleting VTR file - DaoException ");
						} catch (InvalidKeyException e) {
							Log.getLog(getClass()).error("Error on deleting VTR file - InvalidKeyException ");
						}
					}
				}
			}
		}
		
		files = module.getFiles(id);
		initForm();
		if ("Edit".equals(type)) {
			
			populateFields();
		} else {
			request = module.getRequestWithService(requestId);
		}
	}

	public String getDefaultTemplate(){
		return "fms/engineering/vtrFormTemplate";
	}
	
	public void initForm() {
		setMethod("post");
		setColumns(2);
		Application app = Application.getInstance();
		
		lbFacility = new BoldLabel("lbFacility");
		lbFacility.setAlign("right");
		lbFacility.setText(app.getMessage("fms.facility.label.facility")+"*");
		addChild(lbFacility);
		
		facilitySelectBox= new SingleFacilitySelectBox("facilitySelectBox");
		facilitySelectBox.addChild(new ValidatorSelectBox("facilitySelectBoxValidator","",""));
		facilitySelectBox.setServiceId(serviceId);
		facilitySelectBox.init();
		addChild(facilitySelectBox);
		
		lbServiceParticulars = new BoldLabel("lbServiceParticulars");
		lbServiceParticulars.setAlign("right");
		lbServiceParticulars.setText(app.getMessage("fms.facility.label.serviceParticulars")+"*");
		addChild(lbServiceParticulars);
		
		serviceParticulars= new ExtendedSelectBox("serviceParticulars");
		//serviceParticulars.setOptionMap(EngineeringModule.SERVICE_PARTICULARS_MAP);
		
		for(Iterator itr=EngineeringModule.SERVICE_PARTICULARS_MAP.keySet().iterator();itr.hasNext();){
			String key=(String)itr.next();
			serviceParticulars.setOptions(key+"="+(String)EngineeringModule.SERVICE_PARTICULARS_MAP.get(key));
			serviceParticulars.setOnChange("populateClientName()");
		}
		addChild(serviceParticulars);
		
		lbBlockBooking = new BoldLabel("lbBlockBooking");
		lbBlockBooking.setAlign("right");
		lbBlockBooking.setText(app.getMessage("fms.facility.label.blockBooking"));
		addChild(lbBlockBooking);
		
		pnBB = new Panel("pnBB");
		rdBBYes = new Radio("rdBBYes", "Yes");
		rdBBYes.setGroupName("blockBooking");
		pnBB.addChild(rdBBYes);
		rdBBNo = new Radio("rdBBNo", "No");
		rdBBNo.setGroupName("blockBooking");
		rdBBNo.setChecked(true);
		pnBB.addChild(rdBBNo);
		addChild(pnBB);
		
		lbDateRequiredFrom = new BoldLabel("lbDateRequiredFrom");
		lbDateRequiredFrom.setAlign("right");
		lbDateRequiredFrom.setText(app.getMessage("fms.facility.label.requiredFrom")+"*");
		addChild(lbDateRequiredFrom);
		
		requiredDate = new DatePopupField("requiredFrom");
		requiredDate.setDate(dtRequiredFrom);
		addChild(requiredDate);
		
		lbDateRequiredTo = new BoldLabel("lbDateRequiredTo");
		lbDateRequiredTo.setAlign("right");
		lbDateRequiredTo.setText(app.getMessage("fms.facility.label.requiredTo")+"*");
		addChild(lbDateRequiredTo);
		
		requiredDateTo = new DatePopupField("requiredTo");
		requiredDateTo.setDate(dtRequiredTo);
		addChild(requiredDateTo);
		
//		lbRequiredDate = new BoldLabel("lbRequiredDate");
//		lbRequiredDate.setAlign("right");
//		lbRequiredDate.setText(app.getMessage("fms.facility.label.requiredDate")+"*");
//		addChild(lbRequiredDate);
//		
//		requiredDate = new DatePopupField("requiredDate");
//		requiredDate.setDate(dtRequiredFrom);
//		addChild(requiredDate);
		
		lbRequiredTime = new BoldLabel("lbRequiredTime");
		lbRequiredTime.setAlign("right");
		lbRequiredTime.setText(app.getMessage("fms.facility.label.requiredTime")+"*");
		addChild(lbRequiredTime);
		
		timePanel1= new Panel("timePanel1");
		timePanel1.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		requiredFrom = new TimeField("requiredFrom");
		requiredFrom.setTemplate("calendar/fromTimefield");
		timePanel1.addChild(requiredFrom);
		timePanel1.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		requiredTo = new TimeField("requiredTo");
		requiredTo.setTemplate("calendar/toTimeField");
		timePanel1.addChild(requiredTo);
		addChild(timePanel1);
		
		lbFormat = new BoldLabel("lbFormat");
		lbFormat.setAlign("right");
		lbFormat.setText(app.getMessage("fms.facility.label.format"));
		addChild(lbFormat);
		
		timePanel2= new Panel("timePanel2");
		timePanel2.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		formatFrom = new SelectBox("formatFrom");
		formatFrom.setOptionMap(EngineeringModule.VTR_FORMAT_MAP);
		timePanel2.addChild(formatFrom);
		timePanel2.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		formatTo = new SelectBox("formatTo");
		formatTo.setOptionMap(EngineeringModule.VTR_FORMAT_MAP);
		timePanel2.addChild(formatTo);
		addChild(timePanel2);
		
		timePanel4 = new Panel("timePanel4");
		timePanel4.addChild(new Label("from", app.getMessage("fms.facility.label.from")));
		formatIngestFrom = new SelectBox("formatIngestFrom");
		formatIngestFrom.setOptionMap(EngineeringModule.VTR_INGEST_QC_FORMAT_MAP);
		timePanel4.addChild(formatIngestFrom);
		timePanel4.addChild(new Label("to", app.getMessage("fms.facility.label.to")));
		formatIngestTo = new SelectBox("formatIngestTo");
		formatIngestTo.setOptionMap(EngineeringModule.VTR_INGEST_QC_FORMAT_MAP);
		timePanel4.addChild(formatIngestTo);
		addChild(timePanel4);
		
		lbConversion = new BoldLabel("lbConversion");
		lbConversion.setAlign("right");
		lbConversion.setText(app.getMessage("fms.facility.label.conversion"));
		addChild(lbConversion);
		
		timePanel3= new Panel("timePanel3");
		timePanel3.addChild(new Label("from",app.getMessage("fms.facility.label.from")));
		conversionFrom = new SelectBox("conversionFrom");
		conversionFrom.setOptionMap(EngineeringModule.VTR_CONVERSION_MAP);
		timePanel3.addChild(conversionFrom);
		timePanel3.addChild(new Label("to"," "+app.getMessage("fms.facility.label.to")));
		conversionTo = new SelectBox("conversionTo");
		conversionTo.setOptionMap(EngineeringModule.VTR_CONVERSION_MAP);
		timePanel3.addChild(conversionTo);
		addChild(timePanel3);
		
		lbDuration= new BoldLabel("lbDuration");
		lbDuration.setAlign("right");
		lbDuration.setText(app.getMessage("fms.facility.label.duration"));
		addChild(lbDuration);
		
		duration = new TextField("duration");
		//duration.addChild(new ValidatorIsNumeric("visDuration","",false));
		
		duration.setTemplate("roTextField"); // readonly text field
		duration.setSize("20");
		addChild(duration);
		
		lbNoOfCopies= new BoldLabel("lbNoOfCopies");
		lbNoOfCopies.setAlign("right");
		lbNoOfCopies.setText(app.getMessage("fms.facility.label.noOfCopies") + "*");
		addChild(lbNoOfCopies);
		
		noOfCopies = new TextField("noOfCopies");
		noOfCopies.addChild(new ValidatorIsNumeric("visNoOfCopies","",false));
		noOfCopies.setSize("6");
		addChild(noOfCopies);
		
		lbAttachment= new BoldLabel("lbAttachment");
		lbAttachment.setAlign("right");
		lbAttachment.setText(app.getMessage("fms.facility.label.attachment"));
		addChild(lbAttachment);
		
		lbAttachmentList= new Label("attachmentList");
		String content="<table class='borderTable' width='80%'>";
		
		//Iterate files
		if (files.size() > 0) {
			Iterator itr = files.iterator();
			while (itr.hasNext()){
				VtrService file = (VtrService)itr.next();
				content+="<tr><td><a onClick=\"javascript:window.open('/storage" + file.getFilePath() + "', 'openAttachment', 'height=350,width=250,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');return false;\">" + file.getFileName() +"</a></td>" +
						"<td><a href='?id="+ id + "&amp;serviceId=" + serviceId + "&amp;do=delete&amp;idfile=" + file.getFileId() + "'>Remove</a></td></tr>";
			}
		}
		
		content+="</table><br>";
		lbAttachmentList.setText(content);
		lbAttachmentList.setEscapeXml(false);
		
		attachment = new FileUpload("attachment");
		attachmentPanel=new Panel("attachmentPanel");
		attachmentPanel.addChild(lbAttachmentList);
		attachmentPanel.addChild(attachment);
		addChild(attachmentPanel);
		
		lbLocation = new BoldLabel("lbLocation");
		lbLocation.setAlign("right");
		lbLocation.setText(app.getMessage("fms.facility.form.location"));
		addChild(lbLocation);
		
		location =new TextField("location");
		addChild(location);
		
		lbRemarks = new BoldLabel("lbRemarks");
		lbRemarks.setAlign("right");
		lbRemarks.setText(app.getMessage("fms.request.label.remarks"));
		addChild(lbRemarks);
		
		remarks =new TextBox("remarks");
		remarks.setRows("3");
		addChild(remarks);
		
		populateButtons();
		submit.setOnClick("return saveIt()");
		
		
	}
	
	public void populateButtons() {
		Application app = Application.getInstance();
		buttonPanel = new Panel("panel");
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		cancel.setOnClick("window.close();");
		buttonPanel.addChild(submit);
		buttonPanel.addChild(cancel);
		addChild(new Label(("tupuku")));
		addChild(buttonPanel);
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		VtrService s=module.getVtrService(id);
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		request = module.getRequestWithService(requestId);
		
		facilitySelectBox.setServiceId(serviceId);
		facilitySelectBox.setIds(new String[]{s.getFacilityId()});
		serviceParticulars.setSelectedOption(s.getService());
		lbService = s.getService();
		if ("1".equals(s.getBlockBooking())){
			rdBBYes.setChecked(true);
			rdBBNo.setChecked(false);
		} else {
			rdBBNo.setChecked(true);
			rdBBYes.setChecked(false);
		}
		requiredDate.setDate(s.getRequiredDate());
		requiredDateTo.setDate(s.getRequiredDateTo());
		WidgetUtil.populateTimeField(requiredFrom,s.getRequiredFrom());
		WidgetUtil.populateTimeField(requiredTo,s.getRequiredTo());
		formatFrom.setSelectedOption(s.getFormatFrom());
		formatTo.setSelectedOption(s.getFormatTo());
		formatIngestFrom.setSelectedOption(s.getFormatFrom());
		formatIngestTo.setSelectedOption(s.getFormatTo());
		conversionFrom.setSelectedOption(s.getConversionFrom());
		conversionTo.setSelectedOption(s.getConversionTo());
		duration.setValue(s.getDuration());
		noOfCopies.setValue(s.getNoOfCopies());
		location.setValue(s.getLocation());
		remarks.setValue(s.getRemarks());
	}

	public Forward onValidate(Event event) {
			
			VtrService service = new VtrService();
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			EngineeringDao dao = (EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			
			service.setRequestId(requestId);
			service.setServiceId(serviceId);
			service.setFacilityId(facilitySelectBox.getSelectedId());
			service.setBlockBooking(rdBBYes.isChecked()?"1":"0");
			service.setService(WidgetUtil.getSbValue(serviceParticulars));
			service.setRequiredDate(requiredDate.getDate());
			service.setRequiredDateTo(requiredDateTo.getDate());
			/*service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
			service.setRequiredTo(WidgetUtil.getTime(requiredTo));*/	
			
			if ("3".equals(service.getService()) || "4".equals(service.getService()) || "5".equals(service.getService())){
				service.setFormatFrom(WidgetUtil.getSbValue(formatIngestFrom));
				service.setFormatTo(WidgetUtil.getSbValue(formatIngestTo));
			} else {
				service.setFormatFrom(WidgetUtil.getSbValue(formatFrom));
				service.setFormatTo(WidgetUtil.getSbValue(formatTo));
			}
			service.setConversionFrom(WidgetUtil.getSbValue(conversionFrom));
			service.setConversionTo(WidgetUtil.getSbValue(conversionTo));
			try{service.setDuration((String)duration.getValue());}catch(Exception e){service.setDuration("0"); }
			try{service.setNoOfCopies(Integer.parseInt((String)noOfCopies.getValue()));}catch(Exception e){service.setNoOfCopies(0); }
			service.setRemarks((String)remarks.getValue());
			service.setLocation((String)location.getValue());
			service.setSubmitted("0");
			// get internal rate and external rate from rate card
			RateCard rc = mod.getRateCardDetail(service.getFacilityId());
			service.setInternalRate(rc.getInternalRate());
			service.setExternalRate(rc.getExternalRate());
			
			if(service.getRequiredDate().after(service.getRequiredDateTo())){
				requiredDateTo.setInvalid(true);
				this.setInvalid(true);
				return new Forward();
			}
			
			if (service.getRequiredDate().before(request.getRequiredFrom()) || service.getRequiredDate().after(request.getRequiredTo())){
				requiredDate.setInvalid(true);
				this.setInvalid(true);
				return new Forward();
			}
			
			if (service.getRequiredDateTo().before(request.getRequiredFrom()) || service.getRequiredDateTo().after(request.getRequiredTo())){
				requiredDateTo.setInvalid(true);
				this.setInvalid(true);
				return new Forward();
			}
			
			//============== START : date and time checking =============================
			service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
			
			int hour = requiredTo.getHour(); int minute = requiredTo.getMinute();
			if(requiredDate.getDate().compareTo(requiredDateTo.getDate())==0){
				if(WidgetUtil.getTime(requiredTo).compareTo(WidgetUtil.getTime(requiredFrom))< 0){
					if(hour != 0){
						requiredFrom.setInvalid(true);
						this.setInvalid(true);
						return new Forward("checkTimeFrom");
					}
				}
				
				if(hour == 0){
					if(minute > 0){
						Calendar timeT = Calendar.getInstance();
						timeT.setTime(dtRequiredTo);
						timeT.add(Calendar.DATE, 1);
						timeT.set(Calendar.HOUR_OF_DAY, hour);
						timeT.set(Calendar.MINUTE, minute);
						requiredTo.setDate(timeT.getTime());
						service.setRequiredDateTo(timeT.getTime());
						service.setRequiredTo(WidgetUtil.getTime(requiredTo));
					}else{
						Calendar timeT = Calendar.getInstance();
						timeT.set(Calendar.HOUR_OF_DAY, 23);
						timeT.set(Calendar.MINUTE, 59);
						requiredTo.setDate(timeT.getTime());
						service.setRequiredTo(WidgetUtil.getTime(requiredTo));		
					}
				}else{
					service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
					service.setRequiredTo(WidgetUtil.getTime(requiredTo));
				}
			}else{
				if(hour == 0 && minute == 0){
					/*kuarkan alert
					 * 	tanya kalau btul nak return on 00:00
					 * 	klik ok 
					 * 		save 00:00 on dat day
					 *  klik cancel
					 *  	save date yg dia pilih
					 * */ 
					service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
					Calendar timeT = Calendar.getInstance();
					timeT.set(Calendar.HOUR_OF_DAY, 23);
					timeT.set(Calendar.MINUTE, 59);
					requiredTo.setDate(timeT.getTime());
					service.setRequiredTo(WidgetUtil.getTime(requiredTo));		
				}else{
					service.setRequiredFrom(WidgetUtil.getTime(requiredFrom));
					service.setRequiredTo(WidgetUtil.getTime(requiredTo));
				}
			}
			//============== END : date and time checking =============================
			
			// get facility name for logging
			service.setFacility(facilitySelectBox.getSelectedText());
			
			if ("Add".equals(type)) {
				try {
					module.insertVtrService(service);		
					if(EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()))
						return new Forward("MODIFIED");
					//return new Forward("ADDED");	
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward("FAILED");
				} 
			}
			if ("Edit".equals(type)) {
				try {
					service.setId(id);
					module.updateVtrService(service);
					if(EngineeringModule.ASSIGNMENT_STATUS.equals(request.getStatus()))
						return new Forward("MODIFIED");
					//return new Forward("ADDED");
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward("FAILED");
				} 
			}
			
			uploadVtrAttachment(service, module, dao, event);
			
		return new Forward("ADDED");
	}

	public void uploadVtrAttachment(VtrService service, EngineeringModule module, EngineeringDao dao, Event event){
		// for uploading vtr attachment file
		try {
			if (attachment.getValue() != null){
				service.setFileId(UuidGenerator.getInstance().getUuid());
				StorageFile file = new StorageFile(module.VTR_FILE_PATH + "/" + service.getFileId(), attachment.getStorageFile(event.getRequest()));
				
				service.setFileName(file.getName());
				service.setFilePath(file.getAbsolutePath());
				
				dao.storeFile(file);
				module.insertVtrAttachment(service);
			}
		} catch (IOException ioe){
			Log.getLog(getClass()).error(ioe);
		} catch (StorageException se){
			Log.getLog(getClass()).error(se);
		} catch (Exception e) {
		}
		// end for uploading vtr attachment file
	}
	
	public Collection getFiles() {
		return files;
	}

	public void setFiles(Collection files) {
		this.files = files;
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

	public SingleFacilitySelectBox getFacilitySelectBox() {
		return facilitySelectBox;
	}

	public void setFacilitySelectBox(SingleFacilitySelectBox facilitySelectBox) {
		this.facilitySelectBox = facilitySelectBox;
	}

	public ExtendedSelectBox getServiceParticulars() {
		return serviceParticulars;
	}

	public void setServiceParticulars(ExtendedSelectBox serviceParticulars) {
		this.serviceParticulars = serviceParticulars;
	}

	public Panel getPnBB() {
		return pnBB;
	}

	public void setPnBB(Panel pnBB) {
		this.pnBB = pnBB;
	}

	public DatePopupField getRequiredDate() {
		return requiredDate;
	}

	public void setRequiredDate(DatePopupField requiredDate) {
		this.requiredDate = requiredDate;
	}

	public DatePopupField getRequiredDateTo() {
		return requiredDateTo;
	}

	public void setRequiredDateTo(DatePopupField requiredDateTo) {
		this.requiredDateTo = requiredDateTo;
	}

	public Panel getTimePanel1() {
		return timePanel1;
	}

	public void setTimePanel1(Panel timePanel1) {
		this.timePanel1 = timePanel1;
	}

	public Panel getTimePanel2() {
		return timePanel2;
	}

	public void setTimePanel2(Panel timePanel2) {
		this.timePanel2 = timePanel2;
	}

	public Panel getTimePanel4() {
		return timePanel4;
	}

	public void setTimePanel4(Panel timePanel4) {
		this.timePanel4 = timePanel4;
	}

	public Panel getAttachmentPanel() {
		return attachmentPanel;
	}

	public void setAttachmentPanel(Panel attachmentPanel) {
		this.attachmentPanel = attachmentPanel;
	}

	public TextField getDuration() {
		return duration;
	}

	public void setDuration(TextField duration) {
		this.duration = duration;
	}

	public Panel getTimePanel3() {
		return timePanel3;
	}

	public void setTimePanel3(Panel timePanel3) {
		this.timePanel3 = timePanel3;
	}

	public TextField getNoOfCopies() {
		return noOfCopies;
	}

	public void setNoOfCopies(TextField noOfCopies) {
		this.noOfCopies = noOfCopies;
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

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public String getLbService() {
		return lbService;
	}

	public void setLbService(String lbService) {
		this.lbService = lbService;
	}
}
