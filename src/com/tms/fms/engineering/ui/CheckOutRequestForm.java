package com.tms.fms.engineering.ui;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.metaparadigm.jsonrpc.JSONRPCBridge;
import com.tms.fms.department.ui.PopupHODSelectBox;
import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.ui.validator.ValidatorItemBarcode;
import com.tms.fms.widgets.ExtendedTextField;
import com.tms.jsonUtil.JSONUtil;

public class CheckOutRequestForm extends Form{
	public static final String FORWARD_ADD_SUCCESS = "success";
	public static final String FORWARD_ADD_SUCCESS_NOT_IN_RATE_CARD = "success_extra";
	public static final String FORWARD_ADD_FAIL = "fail";
	public static final String FORWARD_MORE="more";
	
	private PopupHODSelectBox psusbCheckOutBy;
	private Label requestTitle;
	private Label assignmentCode;
	private TextField takenBy;
	private TextField preparedBy;
	private TextField assignmentLocation;
	private ExtendedTextField tfItem[];
	private Panel pnButton;
	private Button btnSubmit;
	private Button btnMore;
	private Button btnCancel;
	private Hidden validateRequestId;
	
	private boolean isMore=false;
	private int count=0;
	private String cancelUrl = "requestDetails.jsp?requestId=";
	private String whoModifyId = "";
	
	private String assignmentId;
	private String requestId;
	private String page;
	private EngineeringRequest eRequest = null;
	private Collection requestedItemsList=new ArrayList();
	private Collection requestedPreparedItemsList=new ArrayList();
	
	private String action;

	public void onRequest(Event event) {
		FacilityModule facilityModule = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
		JSONRPCBridge jsonBridge = JSONUtil.getJsonBridge(event.getRequest());
		jsonBridge.registerObject("facilityModule", facilityModule);
		
		if(!isMore){
			initForm();
			populateFields();
		}
    }
	
	public void initForm() {
	    Application application = Application.getInstance();
	    String msgNotEmpty  = application.getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");

	    removeChildren();
	    setMethod("post");
	    
	    requestTitle = new Label("requestTitle");
	    addChild(requestTitle);
	    
	    assignmentCode = new Label("assignmentCode");
	    addChild(assignmentCode);
	    
	    psusbCheckOutBy = new PopupHODSelectBox("psusbCheckOutBy");
	    psusbCheckOutBy.addChild(new ValidatorNotEmpty("psusbCheckOutByNotEmpty", msgNotEmpty));
	    String[] userId = new String[1];
	    userId[0] = Application.getInstance().getCurrentUser().getId();
	    psusbCheckOutBy.setIds(userId);
	    addChild(psusbCheckOutBy);
	    psusbCheckOutBy.init();
	    
	    takenBy = new TextField("takenBy");
	    takenBy.setSize("25");
	    takenBy.addChild(new ValidatorNotEmpty("takenByNotEmpty", msgNotEmpty));
	    addChild(takenBy);

	    preparedBy = new TextField("preparedBy");
	    preparedBy.addChild(new ValidatorNotEmpty("preparedByNotEmpty", msgNotEmpty));
	    preparedBy.setSize("25");
	    addChild(preparedBy);
	    
	    assignmentLocation = new TextField("assignmentLocation");
	    assignmentLocation.addChild(new ValidatorNotEmpty("assignmentLocationNotEmpty", msgNotEmpty));
	    assignmentLocation.setSize("25");
	    addChild(assignmentLocation);
	    
	    tfItem = new ExtendedTextField[20];
	    for(count=0; count<20; count++){
	    	addChild(new Label("lbItem"+count, application.getMessage("fms.facility.form.checkOutItem"+(count+1), "Check Out Item"+(count+1))));
		    tfItem[count] = new ExtendedTextField("tfItem"+count);
		    tfItem[count].setMaxlength("255");
		    tfItem[count].setSize("25");		    
		    tfItem[count].setOnKeyUp("nextbox(this,'assignmentCheckOut.form.tfItem"+ (count+1) +"')");
		    tfItem[count].setOnChange("displayItem(this,'assignmentCheckOut.form.tfItem"+ (count) +"', 'labelItemName"+ (count+1) +"')");
		    if(action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)){
		    	tfItem[count].addChild(new ValidatorItemBarcode("tfItem"+count+"IsValid", ValidatorItemBarcode.PREPARE_CHECK_OUT));
		    }else{
		    	tfItem[count].addChild(new ValidatorItemBarcode("tfItem"+count+"IsValid", ValidatorItemBarcode.CHECK_OUT));	
		    }
		    addChild(tfItem[count]);
	    }
	    
		pnButton = new Panel("pnButton");
		btnMore = new Button("btnMore", application.getMessage("fms.facility.more", "More"));
	    pnButton.addChild(btnMore);
	    btnSubmit = new Button("btnSubmit", application.getMessage("fms.facility.done", "Done"));
    	pnButton.addChild(btnSubmit);
	    btnCancel = new Button("btnCancel", application.getMessage("fms.facility.cancel", "Cancel"));
		pnButton.addChild(btnCancel);
		
	    addChild(pnButton);  
	    
		// hidden field validateRequestId
	    validateRequestId = new Hidden("validateRequestId");
		validateRequestId.setValue(requestId);
		addChild(validateRequestId);
	}
	
	private void populateFields(){
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		
		eRequest = module.getRequest(requestId);
		
		if (eRequest != null) {
			requestTitle.setText(eRequest.getTitle());
		}
		
		FacilityModule facmodule=(FacilityModule)Application.getInstance().getModule(FacilityModule.class);
		//requestedItemsList = facmodule.getAssignmentListByRequestId(requestId, true, null);
		requestedItemsList = facmodule.getRequestItemsForCheckedOut(requestId, true);
		
		if(action.equals(EngineeringModule.ACTION_CHECKOUT)){
			//requestedPreparedItemsList = facmodule.getAssignmentListByRequestId(requestId, true, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT);
			requestedPreparedItemsList = facmodule.getPreparedItems(requestId);
		}
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl + requestId + "&page=" + getPage(), true);
	    } else {
	    	if (buttonName != null && btnMore.getAbsoluteName().equals(buttonName)) {
	    		isMore = true;
	    	} else {
	    		isMore = false;
	    	}
	    
			// consistency checking
	    	String postedRequestId = (String) validateRequestId.getValue();
			if (!postedRequestId.equals(requestId)) {
				evt.getRequest().setAttribute("postedRequestId", postedRequestId);
				setInvalid(true);
				return new Forward("consistencyError");
			}
	    
			return result;
	    }
	}
	
	public Forward onValidate(Event event) {
		boolean success = true;
		boolean empty = true;
		boolean isInRateCard = true;

		String message = "";
		EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
		FacilityModule mod = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
		Application app = Application.getInstance();
		SecurityService security = (SecurityService) app.getService(SecurityService.class);
		String user = "";

		try {
			user = security.getUser(psusbCheckOutBy.getId()).getUsername();
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error getUser(1)", e);
		}

		for (int i = 0; i < count; i++) {
			String code = tfItem[i].getValue().toString();

			if (!"".equals(code.trim())) {
				empty = false;
				FacilityObject item = mod.getItem(code);

				if (!(item.getBarcode() == null || "".equals(item.getBarcode()))) {
					Collection col = module.getCategoryByBarcode(item.getBarcode());

					if (col != null && col.size() > 0) {
						isInRateCard = true;
					} else {
						isInRateCard = false;
					}
					if (isInRateCard) {
							RateCard rc = (RateCard) col.iterator().next();

							// checks if the item status is not Check Out or Check In
							if (!FacilityModule.ITEM_STATUS_CHECKED_OUT.equals(item.getStatus())) {
								boolean showTodayOnly = (page != null && page.equals("today"));
								Collection checkEquipmentCol = module.getEquipmentListFromRequestId(requestId, rc.getCategoryId(), showTodayOnly);

								if (checkEquipmentCol != null && checkEquipmentCol.size() > 0) {
									Iterator iterator = checkEquipmentCol.iterator();
									if (iterator.hasNext()) {
										EngineeringRequest er = (EngineeringRequest) iterator.next();

										er.setBarcode(item.getBarcode());
										if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
											er.setPrepareCheckOutBy(user);
										} else {
											er.setCheckedOutBy(user);
										}

										er.setTakenBy((String) takenBy.getValue());
										er.setPreparedBy((String) preparedBy.getValue());
										er.setAssignmentLocation((String) assignmentLocation.getValue());

										if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
											module.updatePrepareCheckOutEquipment(er);
											if (!FacilityModule.ITEM_STATUS_CHECKED_OUT.equals(item.getStatus())) {
												item.setStatus(FacilityModule.ITEM_STATUS_PREPARE_CHECKOUT);
											}
										} else {
											module.updateCheckOutEquipment(er);
											if (FacilityModule.ITEM_STATUS_CHECKED_IN.equals(item.getStatus())|| FacilityModule.ITEM_STATUS_PREPARE_CHECKOUT.equals(item.getStatus())) {
												item.setStatus(FacilityModule.ITEM_STATUS_CHECKED_OUT);
											}
										}
									}
									// update status Item to check out or prepare check out
									mod.updateItem(item);

								} else {
									//do extra checkout 
									Collection extraEquipmentCol = module.getExtraEquipmentListFromRequestId(requestId, null ,rc.getCategoryId());
									doExtraCheckOutItem(extraEquipmentCol, user, item, rc.getCategoryId());
								}

							}
					} else {
						//do extra checkout 
						Collection extraEquipmentCol = module.getExtraEquipmentListFromRequestId(requestId,code, null);
						doExtraCheckOutItem(extraEquipmentCol, user, item, null);
					}

					tfItem[i].setValue("");
					
					//update checkout flag
					EngineeringDao dao = (EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
					EngineeringRequest eRequest = new EngineeringRequest();
					try {
						eRequest=dao.selectRequest(requestId);
						eRequest.setCheckoutFlag("1");
						dao.updateCheckoutFlag(eRequest);
					} catch (DaoException e1) {
						Log.getLog(getClass()).error(e1.toString(), e1);
					}
				}
			}
		}

		if (empty) {
			success = false;
			message = Application.getInstance().getMessage("fms.facility.msg.pleaseKeyInAtLeastOneBarcode");
		}

		if (success) {
			if (isMore) {
				for (count = 0; count < 20; count++) {
					tfItem[count].setValue("");
				}
				if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
					return new Forward("more_prepare");
				} else {
					return new Forward(FORWARD_MORE);
				}

			} else {
				if (isInRateCard) {
					if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
						return new Forward("successPrepare");
					} else {
						return new Forward(FORWARD_ADD_SUCCESS);
					}

				} else {
					/*if (!update) {
						if (action.equals(EngineeringModule.ACTION_CHECKOUT)) {
							return new Forward("fail_extra_checkout");
						} else {
							return new Forward("fail_extra_prepare");
						}

					} else {*/
						if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
							return new Forward("success_extra_prepare");
						} else {
							return new Forward(FORWARD_ADD_SUCCESS_NOT_IN_RATE_CARD);
						}
					//}

				}
			}
		} else {
			HttpServletRequest request = event.getRequest();
			request.setAttribute("message", message);
			event.setRequest(request);
			return new Forward(FORWARD_ADD_FAIL);
		}
	}	
	
	public void doExtraCheckOutItem(Collection checkEquipmentCol, String user, FacilityObject item, String categoryCodeId){
		boolean update=false;
		EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
		
		FacilityModule mod = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
		if (checkEquipmentCol != null&& checkEquipmentCol.size() > 0) {
			for (Iterator iterator = checkEquipmentCol.iterator(); iterator.hasNext();) {
				EngineeringRequest erc = (EngineeringRequest) iterator.next();

				erc.setAssignmentEquipmentId(erc.getAssignmentEquipmentId());
				erc.setBarcode(item.getBarcode());
				if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
					erc.setPrepareCheckOutBy(user);
				} else {
					erc.setCheckedOutBy(user);
				}

				erc.setTakenBy((String) takenBy.getValue());
				erc.setPreparedBy((String) preparedBy.getValue());
				erc.setAssignmentLocation((String) assignmentLocation.getValue());
				erc.setGroupId(erc.getGroupId());
				String status = "";
				String statusItem = "";
				if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
					status = EngineeringModule.ACTION_PREPARE_CHECKOUT;
					statusItem = FacilityModule.ITEM_STATUS_PREPARE_CHECKOUT;
				} else {
					status = EngineeringModule.ACTION_CHECKOUT;
					if (FacilityModule.ITEM_STATUS_CHECKED_IN.equals(item.getStatus()) || FacilityModule.ITEM_STATUS_PREPARE_CHECKOUT.equals(item.getStatus())) {
						statusItem = FacilityModule.ITEM_STATUS_CHECKED_OUT;
					}

				}
				update = module.updateExtraCheckOutEquipment(erc, status);
				if (update) {
					item.setStatus(statusItem);
					if (statusItem != null && !statusItem.equals("")) {
						mod.updateItem(item);
					}

				}

			}
		} else {
			//Collection groupIdsList=module.getGroupIdsList(requestId);
			EngineeringRequest ercObj = new EngineeringRequest();
			ercObj=module.getTopGroupId(requestId);
			String groupId="";
			if(ercObj!=null){
				groupId=ercObj.getGroupId();
			}
			if(groupId!=null && !groupId.equals("")){
				Collection colCopyEquip = module.getAssignmentEquipmentByGroup(groupId);
				if(colCopyEquip!=null && colCopyEquip.size()>0){
					for (Iterator iterator = colCopyEquip.iterator(); iterator.hasNext();) {
						EngineeringRequest erc = (EngineeringRequest) iterator.next();
						
						//EngineeringRequest erc = new EngineeringRequest();

						erc.setBarcode(item.getBarcode());
						if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
							erc.setPrepareCheckOutBy(user);
						} else {
							erc.setCheckedOutBy(user);
						}
						Calendar cal = Calendar.getInstance();
			            cal.set(Calendar.HOUR_OF_DAY, 0);
			            cal.set(Calendar.MINUTE, 0);
			            cal.set(Calendar.SECOND, 0);
			            cal.set(Calendar.MILLISECOND, 0);
			            
			            
						erc.setRequiredFrom(cal.getTime());
						erc.setRequiredTo(cal.getTime());
						erc.setTakenBy((String) takenBy.getValue());
						erc.setPreparedBy((String) preparedBy.getValue());
						erc.setAssignmentLocation((String) assignmentLocation.getValue());
						erc.setGroupId(groupId);
						if(categoryCodeId!=null && !categoryCodeId.equals("")){
							erc.setRateCardCategoryId(categoryCodeId);
						}

						if (action.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)) {
							module.insertPrepareCheckOutEquipmentByGroup(erc);
							if (FacilityModule.ITEM_STATUS_CHECKED_IN.equals(item.getStatus())) {
								item.setStatus(FacilityModule.ITEM_STATUS_PREPARE_CHECKOUT);
								mod.updateItem(item);
							}
						} else {
							module.insertCheckOutEquipmentByGroup(erc);
							if (FacilityModule.ITEM_STATUS_CHECKED_IN.equals(item.getStatus()) || FacilityModule.ITEM_STATUS_PREPARE_CHECKOUT.equals(item.getStatus())) {
								item.setStatus(FacilityModule.ITEM_STATUS_CHECKED_OUT);
								mod.updateItem(item);
							}
						}
						update=true;
					}
				}
			}
			
			
			
		}
	}
	public String getDefaultTemplate() {
		return "fms/engineering/checkOutRequestFormTemplate";
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getWhoModifyId() {
		return whoModifyId;
	}

	public void setWhoModifyId(String whoModifyId) {
		this.whoModifyId = whoModifyId;
	}

	public Label getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(Label requestTitle) {
		this.requestTitle = requestTitle;
	}

	public Label getAssignmentCode() {
		return assignmentCode;
	}

	public void setAssignmentCode(Label assignmentCode) {
		this.assignmentCode = assignmentCode;
	}

	public PopupHODSelectBox getPsusbCheckOutBy() {
		return psusbCheckOutBy;
	}

	public void setPsusbCheckOutBy(PopupHODSelectBox psusbCheckOutBy) {
		this.psusbCheckOutBy = psusbCheckOutBy;
	}


	public Panel getPnButton() {
		return pnButton;
	}

	public TextField getTakenBy() {
		return takenBy;
	}

	public void setTakenBy(TextField takenBy) {
		this.takenBy = takenBy;
	}

	public TextField getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(TextField preparedBy) {
		this.preparedBy = preparedBy;
	}

	public TextField getAssignmentLocation() {
		return assignmentLocation;
	}

	public void setAssignmentLocation(TextField assignmentLocation) {
		this.assignmentLocation = assignmentLocation;
	}

	public ExtendedTextField[] getTfItem() {
		return tfItem;
	}

	public void setTfItem(ExtendedTextField[] tfItem) {
		this.tfItem = tfItem;
	}

	public void setPnButton(Panel pnButton) {
		this.pnButton = pnButton;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public Button getBtnMore() {
		return btnMore;
	}

	public void setBtnMore(Button btnMore) {
		this.btnMore = btnMore;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public Collection getRequestedItemsList() {
		return requestedItemsList;
	}

	public void setRequestedItemsList(Collection requestedItemsList) {
		this.requestedItemsList = requestedItemsList;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Collection getRequestedPreparedItemsList() {
		return requestedPreparedItemsList;
	}

	public void setRequestedPreparedItemsList(Collection requestedPreparedItemsList) {
		this.requestedPreparedItemsList = requestedPreparedItemsList;
	}

	
}
