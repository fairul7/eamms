package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.VtrService;

public class VtrView extends ServiceForm {
	
	private Label facility;
	private Label particular;
	private Label dateRequired;
	private Label timeRequired;
	private Label conversion;
	private Label duration;
	private Label copies;
	private Label attachment;
	private Label description;
	private Label blockBooking;
	private Label location;
	private Collection files = new ArrayList();
	
	public void init() {
	}
	
	public void onRequest(Event event) {
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		files = null;
		files = module.getFiles(id);
		
		initForm();
		if ("View".equals(type)) {
			populateFields();
		}
	}

	public void initForm() {
		setMethod("post");
				
		facility = new Label("facility");
		addChild(facility);
		
		particular = new Label("particular");
		addChild(particular);
		
		dateRequired = new Label("dateRequired");
		addChild(dateRequired);	
		
		timeRequired = new Label("timeRequired");
		addChild(timeRequired);
		
		blockBooking = new Label("blockBooking");
		addChild(blockBooking);
		
		conversion = new Label("conversion");
		addChild(conversion);
		
		duration = new Label("duration");
		addChild(duration);
		
		copies = new Label("copies");
		addChild(copies);
		
		attachment = new Label("attachment");
		addChild(attachment);
		
		location = new Label("location");
		addChild(location);
		
		description = new Label("description");
		addChild(description);
		
		populateButtons();
		
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		VtrService s=module.getVtrService(id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String strTime = "";
		
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		facility.setText(s.getFacility());
		particular.setText(s.getServiceName());
		dateRequired.setText(sdf.format(s.getRequiredDate()) + " - " + sdf.format(s.getRequiredDateTo()));
		blockBooking.setText(s.getBlockBooking().equals("1")?"Yes":"No");
		location.setText(s.getLocation());
		//if (s.getRequiredFrom().equals(s.getRequiredTo())){
		//	strTime = s.getRequiredFrom();
		//} else {
			strTime = s.getRequiredFrom() + " - " + s.getRequiredTo();
		//}
		timeRequired.setText(strTime);
		conversion.setText(s.getConversionFromLabel() + " - " + s.getConversionToLabel());
		duration.setText(s.getDuration());
		copies.setText(Integer.toString(s.getNoOfCopies()));
		description.setText(s.getRemarks());
		description.setEscapeXml(false);
		
		String content="<table class='borderTable' width='80%'>";
		
		//Iterate files
		if (files.size() > 0) {
			Iterator itr = files.iterator();
			while (itr.hasNext()){
				VtrService file = (VtrService)itr.next();
				content+="<tr><td><a onClick=\"javascript:window.open('/storage" + file.getFilePath() + "', 'openAttachment', 'height=350,width=250,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');return false;\">" + file.getFileName() +"</a></td></tr>";
			}
		}
		
		content+="</table><br>";
		attachment.setText(content);
		attachment.setEscapeXml(false);
		
		buttonPanel.setHidden(true);
	}	
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/vtrviewtemp";
	}

	public Label getFacility() {
		return facility;
	}

	public void setFacility(Label facility) {
		this.facility = facility;
	}

	public Label getParticular() {
		return particular;
	}

	public void setParticular(Label particular) {
		this.particular = particular;
	}

	public Label getDateRequired() {
		return dateRequired;
	}

	public void setDateRequired(Label dateRequired) {
		this.dateRequired = dateRequired;
	}

	public Label getTimeRequired() {
		return timeRequired;
	}

	public void setTimeRequired(Label timeRequired) {
		this.timeRequired = timeRequired;
	}

	public Label getConversion() {
		return conversion;
	}

	public void setConversion(Label conversion) {
		this.conversion = conversion;
	}

	public Label getDuration() {
		return duration;
	}

	public void setDuration(Label duration) {
		this.duration = duration;
	}

	public Label getCopies() {
		return copies;
	}

	public void setCopies(Label copies) {
		this.copies = copies;
	}

	public Label getAttachment() {
		return attachment;
	}

	public void setAttachment(Label attachment) {
		this.attachment = attachment;
	}

	public Label getDescription() {
		return description;
	}

	public void setDescription(Label description) {
		this.description = description;
	}

	public Collection getFiles() {
		return files;
	}

	public void setFiles(Collection files) {
		this.files = files;
	}

	public Label getBlockBooking() {
		return blockBooking;
	}

	public void setBlockBooking(Label blockBooking) {
		this.blockBooking = blockBooking;
	}

	public Label getLocation() {
		return location;
	}

	public void setLocation(Label location) {
		this.location = location;
	}
	
}
