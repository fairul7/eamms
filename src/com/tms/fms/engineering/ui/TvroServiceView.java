package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Label;
import kacang.ui.Event;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.OtherService;
import com.tms.fms.engineering.model.TvroService;
import com.tms.fms.util.WidgetUtil;

/**
 * @author fahmi
 *
 */
public class TvroServiceView extends ServiceForm {	
	private Label facility;
	private Label feedTitle;
	private Label location;
	private Label requiredDate;
	private Label requiredTime;
	private Label timeZone;
	private Label totalTime;
	private Label remarks;
	private Label blockBooking;
	
	public void init() {
	}
	
	public void onRequest(Event event) {
		initForm();
		if ("View".equals(type)) {
			populateFields();
		}
	}

	public void initForm() {
		setMethod("post");
				
		facility = new Label("facility");
		addChild(facility);
		
		feedTitle = new Label("feedTitle");
		addChild(feedTitle);
		
		location = new Label("location");
		addChild(location);
		
		requiredDate = new Label("requiredDate");
		addChild(requiredDate);
		
		requiredTime =new Label("requiredTime");
		addChild(requiredTime);
		
		timeZone = new Label("timeZon");
		addChild(timeZone);
		
		totalTime = new Label("totalTime");
		addChild(totalTime);
		
		remarks = new Label("remarks");
		addChild(remarks);
		
		blockBooking = new Label("blockBooking");
		addChild(blockBooking);
		
		populateButtons();
		
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		TvroService s=module.getTvroService(id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String strRequiredTime = "";
		
		serviceId=s.getServiceId();
		requestId=s.getRequestId();
		facility.setText(s.getFacility());
		feedTitle.setText(s.getFeedTitle());
		location.setText(s.getLocation());
		requiredDate.setText(sdf.format(s.getRequiredDate()) + " - " + sdf.format(s.getRequiredDateTo()));
		blockBooking.setText(s.getBlockBooking().equals("1")?"Yes":"No");
		
		//if (s.getFromTime().equals(s.getToTime())){
		//	strRequiredTime = s.getFromTime();
		//} else {
			strRequiredTime = s.getFromTime() + " - " + s.getToTime();
		//}
		requiredTime.setText(strRequiredTime);
		timeZone.setText((String) module.TIMEZONES.get(s.getTimezone()));
		totalTime.setText(s.getTotalTimeReq()+" "+s.getTimeMeasureLabel() );
		remarks.setText(s.getRemarks());
		
		buttonPanel.setHidden(true);		
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/engineering/tvroviewtemp";
	}

	public Label getFacility() {
		return facility;
	}

	public void setFacility(Label facility) {
		this.facility = facility;
	}

	public Label getFeedTitle() {
		return feedTitle;
	}

	public void setFeedTitle(Label feedTitle) {
		this.feedTitle = feedTitle;
	}

	public Label getLocation() {
		return location;
	}

	public void setLocation(Label location) {
		this.location = location;
	}

	public Label getRequiredDate() {
		return requiredDate;
	}

	public void setRequiredDate(Label requiredDate) {
		this.requiredDate = requiredDate;
	}

	public Label getRequiredTime() {
		return requiredTime;
	}

	public void setRequiredTime(Label requiredTime) {
		this.requiredTime = requiredTime;
	}

	public Label getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Label timeZone) {
		this.timeZone = timeZone;
	}

	public Label getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Label totalTime) {
		this.totalTime = totalTime;
	}

	public Label getRemarks() {
		return remarks;
	}

	public void setRemarks(Label remarks) {
		this.remarks = remarks;
	}

	public Label getBlockBooking() {
		return blockBooking;
	}

	public void setBlockBooking(Label blockBooking) {
		this.blockBooking = blockBooking;
	}
	
}
