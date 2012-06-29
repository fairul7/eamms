package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;

import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.facility.model.FacilityModule;

public class PrintAssignmentByGroupAssignmentId extends Form {
	
	protected String assignmentId;
	protected String requestId;
	protected String groupId;
	protected EngineeringRequest request=new EngineeringRequest();
	protected Assignment assignment=new Assignment();
	protected Button checkOut;
	protected Button checkIn;
	protected Button cancel;	
	private Date checkedOutDate;
	private String storeLocation;
	private String userLogin; 
	private String page;
	private Date reqFrom;
	private Date reqTo;
	
	protected Collection requestItemsList=new ArrayList();
	protected Collection requestExtraItemsList=new ArrayList();
	
	
	public PrintAssignmentByGroupAssignmentId() {
	}

	public PrintAssignmentByGroupAssignmentId(String s) {super(s);}

	public void init() {
	}
	
	public void onRequest(Event arg0) {
		init();
		populateRequest();
	}
	
	private void populateRequest(){
		setMethod("post");
		EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
		FacilityModule facmodule=(FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		
		if(page!=null && page.equals("today")){
			requestItemsList = facmodule.selectRequestItemsGroupAssignment(requestId, null, reqFrom, reqTo,true);
			requestExtraItemsList = facmodule.selectExtraRequestItemsByGroup(requestId, null, reqFrom, reqTo,true);
		}
		else{
			requestItemsList = facmodule.selectRequestItemsGroupAssignment(requestId, null, reqFrom, reqTo,false);
			requestExtraItemsList = facmodule.selectExtraRequestItemsByGroup(requestId, null, reqFrom, reqTo, false);
		}
		
		String myUsername = Application.getInstance().getCurrentUser().getUsername();
		
		Date latest = null;
		if (requestItemsList!=null && requestItemsList.size() >0){
			Collection newList = new ArrayList();
			for (Iterator i = requestItemsList.iterator();i.hasNext();){
				Assignment asg = (Assignment) i.next();
				String checkedOutBy = asg.getCheckedOutBy();
				String checkedInBy = asg.getCheckedInBy();
				
				// record latest checked out by user
				if (myUsername.equals(checkedOutBy) && (latest == null || asg.getCheckedOutDate().after(latest))) {
					latest = asg.getCheckedOutDate();
				}
				
				// show only checked out by user & not checked in yet
				if (myUsername.equals(checkedOutBy) && checkedInBy == null) {
					newList.add(asg);
				} else {
					continue;
				}
				
				if (asg.getRateCardCategoryName() == null){
					asg.setRateCardCategoryName(uDao.getRateCardCategoryByBarcode(asg.getBarcode()));
				}
			}
			requestItemsList = newList;
		}
		
		if (requestExtraItemsList!=null && requestExtraItemsList.size() >0){
			Collection newList = new ArrayList();
			for (Iterator i = requestExtraItemsList.iterator();i.hasNext();){
				Assignment asg = (Assignment) i.next();
				String checkedOutBy = asg.getCheckedOutBy();
				String checkedInBy = asg.getCheckedInBy();
				
				// record latest checked out by user
				if (myUsername.equals(checkedOutBy) && (latest == null || asg.getCheckedOutDate().after(latest))) {
					latest = asg.getCheckedOutDate();
				}
				
				// show only checked out by user & not checked in yet
				if (myUsername.equals(checkedOutBy) && checkedInBy == null) {
					newList.add(asg);
				} else {
					continue;
				}
				
				if (asg.getRateCardCategoryName() == null){
					asg.setRateCardCategoryName(uDao.getRateCardCategoryByBarcode(asg.getBarcode()));
				}
			}
			requestExtraItemsList = newList;
		}
		
		assignment=uDao.getLatestAssignmentByRequestId(requestId);
		checkedOutDate = latest;
		userLogin = Application.getInstance().getCurrentUser().getName();
		
		request=module.getRequestWithService(requestId);
	}
	
	public String getDefaultTemplate() {
		return "fms/facility/printAssignmentCheckOutByGroup";
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Button checkIn) {
		this.checkIn = checkIn;
	}

	public Button getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Button checkOut) {
		this.checkOut = checkOut;
	}

	public EngineeringRequest getRequest() {
		return request;
	}

	public void setRequest(EngineeringRequest request) {
		this.request = request;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getCheckedOutDate() {
		return checkedOutDate;
	}

	public void setCheckedOutDate(Date checkedOutDate) {
		this.checkedOutDate = checkedOutDate;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public Collection getRequestItemsList() {
		return requestItemsList;
	}

	public void setRequestItemsList(Collection requestItemsList) {
		this.requestItemsList = requestItemsList;
	}

	public Collection getRequestExtraItemsList() {
		return requestExtraItemsList;
	}

	public void setRequestExtraItemsList(Collection requestExtraItemsList) {
		this.requestExtraItemsList = requestExtraItemsList;
	}

	public Date getReqFrom() {
		return reqFrom;
	}

	public void setReqFrom(Date reqFrom) {
		this.reqFrom = reqFrom;
	}

	public Date getReqTo() {
		return reqTo;
	}

	public void setReqTo(Date reqTo) {
		this.reqTo = reqTo;
	}
	
}
