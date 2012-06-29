package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;

public class AssignmentDetailsFormByRequest extends Form{
	protected String assignmentId;
	protected String requestId;
	protected String groupId;
	protected String page;
	protected EngineeringRequest request=new EngineeringRequest();
	protected Hidden validateRequestId;
	protected Button checkOut;
	protected Button checkIn;
	protected Button printCheckOut;
	protected Button cancel;	
	protected Button prepareCheckout;
	protected Button notUtilized;
	public String action;
	public String assignmentEquipmentId;
	public Date startDate;
	public Date endDate;
	
	protected Collection requestItemsList=new ArrayList();
	protected Collection requestExtraItemsList=new ArrayList();
	
	public AssignmentDetailsFormByRequest() {
	}

	public AssignmentDetailsFormByRequest(String s) {super(s);}

	public void init() {
		Application app=Application.getInstance();
		FacilityModule facmodule=(FacilityModule) app.getModule(FacilityModule.class);
		
		setMethod("post");
		
		checkOut = new Button("checkOut", app.getMessage("fms.facility.table.checkOut"));
		checkIn = new Button("checkIn", app.getMessage("fms.facility.table.checkIn"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));		
		printCheckOut = new Button("printCheckOut", Application.getInstance().getMessage("fms.facility.reprint"));
		prepareCheckout = new Button("prepareCheckout", app.getMessage("fms.facility.prepareCheckout"));
		notUtilized = new Button("notUtilized", app.getMessage("fms.facility.notUtilized"));
		validateRequestId = new Hidden("validateRequestId");
		
		boolean isNotUtilized = facmodule.isItemNotUtilized(requestId);
		
		addChild(checkOut);
		addChild(checkIn);
		addChild(printCheckOut);
		addChild(cancel);
		addChild(prepareCheckout);
		addChild(notUtilized);
		addChild(validateRequestId);
		
		if(isNotUtilized){
			notUtilized.setHidden(true);
		}else{
			notUtilized.setHidden(false);
		}
	}
	
	public void onRequest(Event arg0) {
		FacilityModule facmodule=(FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
		
		if(action!=null && !action.equals("") && action.equals("remove")){
			if(assignmentId!=null && !assignmentId.equals("")){
				facmodule.removePrepareItem(assignmentId, "assignmentId");
			}
			if(assignmentEquipmentId!=null && !assignmentEquipmentId.equals("")){
				facmodule.removePrepareItem(assignmentEquipmentId, "assignmentEquipmentId");
			}
		}
		
		init();
		try {
			startDate = dao.selectAssignmentCheckOutDetailsDate(requestId, "FROM");
			endDate = dao.selectAssignmentCheckOutDetailsDate(requestId, "TO");
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
		populateRequest();
	}
	
	private void populateRequest() {
		EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
		FacilityModule facmodule=(FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		
		boolean isCheckout = false;
		
		if(page!=null && page.equals("today")){
			//childItems = facmodule.getAssignmentListByRequestId2(requestId, true);
			requestItemsList = facmodule.getRequestItems(requestId, true);
			requestExtraItemsList = facmodule.getExtraRequestItems(requestId, true);
		}else{
			//childItems = facmodule.getAssignmentListByRequestId2(requestId, false);
			requestItemsList = facmodule.getRequestItems(requestId, false);
			requestExtraItemsList = facmodule.getExtraRequestItems(requestId, false);
		}
		
		// set hidden field
		validateRequestId.setValue(requestId);
		
		/*if (groupId != null && !"".equals(groupId)){
			
			childItems = uDao.getChildFacilityAssignmentsByGroupIdMap(groupId);
		}*/
		
		

		if (requestItemsList!=null && requestItemsList.size() >0){
			for (Iterator i = requestItemsList.iterator();i.hasNext();){
				Assignment asg = (Assignment) i.next();

				if (asg.getRateCardCategoryName() == null){
					asg.setRateCardCategoryName(uDao.getRateCardCategoryByBarcode(asg.getBarcode()));
				}
				if (asg.getStatus().equals("O")) {
					isCheckout = true;
				}
			}
		}
		
		if (requestExtraItemsList!=null && requestExtraItemsList.size() >0){
			for (Iterator i = requestExtraItemsList.iterator();i.hasNext();){
				Assignment asg = (Assignment) i.next();
				
				if (asg.getRateCardCategoryName() == null){
					asg.setRateCardCategoryName(uDao.getRateCardCategoryByBarcode(asg.getBarcode()));
				}
				if (asg.getStatus().equals("O")) {
					isCheckout = true;
				}
			}
		}
		
		if (page != null && page.equals("all")) {
			prepareCheckout.setHidden(true);
		} else {
			if (isCheckout) {
				prepareCheckout.setHidden(true);
			} else {
				prepareCheckout.setHidden(false);
			}
		}
		
		request=module.getRequestWithService(requestId);
	}
	
	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			if ("all".equals(getPage())){
				return new Forward(Form.CANCEL_FORM_ACTION, "assignmentListing.jsp", true);
			} else {
				return new Forward(Form.CANCEL_FORM_ACTION, "todaysAssignment.jsp", true);
			}
		}  else {return result;}
	}
	
	public Forward onValidate(Event arg0) {
		String buttonName = findButtonClicked(arg0);
		String formRequestId = (String) validateRequestId.getValue();
		
		if (buttonName != null && checkOut.getAbsoluteName().equals(buttonName)) {
			return new Forward("","checkoutRequestForm.jsp?id="+formRequestId+"&page="+getPage()+"&action="+EngineeringModule.ACTION_CHECKOUT,true);
		}else if (buttonName != null && prepareCheckout.getAbsoluteName().equals(buttonName)) {
			return new Forward("","checkoutRequestForm.jsp?id="+formRequestId+"&page="+getPage()+"&action="+EngineeringModule.ACTION_PREPARE_CHECKOUT,true);
		
		} else if (buttonName != null && checkIn.getAbsoluteName().equals(buttonName)) {
			return new Forward("","assignmentDirectCheckIn.jsp?requestId="+formRequestId,true);
		
		}else if (buttonName != null && notUtilized.getAbsoluteName().equals(buttonName)) {
			return new Forward("notUtilized");
			//return new Forward("");
			
		}else if (buttonName != null && printCheckOut.getAbsoluteName().equals(buttonName)) {
			return new Forward("PRINTOUT");
		}
		
		return new Forward("");
	}
	
	public String getDefaultTemplate() {
		return "fms/facility/requestDetailsFormTemplate";
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

	/*public Collection getChildItems() {
		return childItems;
	}

	public void setChildItems(Collection childItems) {
		this.childItems = childItems;
	}*/

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Button getPrepareCheckout() {
		return prepareCheckout;
	}

	public void setPrepareCheckout(Button prepareCheckout) {
		this.prepareCheckout = prepareCheckout;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAssignmentEquipmentId() {
		return assignmentEquipmentId;
	}

	public void setAssignmentEquipmentId(String assignmentEquipmentId) {
		this.assignmentEquipmentId = assignmentEquipmentId;
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

	public Button getNotUtilized() {
		return notUtilized;
	}

	public void setNotUtilized(Button notUtilized) {
		this.notUtilized = notUtilized;
	}

	public Button getPrintCheckOut() {
		return printCheckOut;
	}

	public void setPrintCheckOut(Button printCheckOut) {
		this.printCheckOut = printCheckOut;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getPageText() {
		if (page != null && page.equals("today")) {
			return "Today";
		} else {
			return "All";
		}
	}
}
