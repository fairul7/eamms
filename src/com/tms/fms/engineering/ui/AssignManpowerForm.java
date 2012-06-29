package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.engineering.model.CheckAvailabilityModule;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.ui.UnitApprovalUserSelectBox;
import com.tms.fms.facility.ui.UnitUserSelectBox;
import com.tms.fms.register.model.FMSRegisterManager;

/**
 * 
 * @author fahmi
 *
 */
public class AssignManpowerForm extends Form {	
	
	private UnitApprovalUserSelectBox userSelectBox;
	private UnitUserSelectBox altUserSelectBox;
	
	private String id;
	private String act;
	private String unitId;
	
	protected Button cancel;
	protected Button submit;
	protected Button submitDiffMan;
	protected Button cancelDiffMan;
	protected Panel buttonPanel;
	protected Panel buttonPanelDiff;
	protected Panel pnType;
	
	protected EngineeringRequest request;	
	
	private Collection manpowers = new ArrayList();
	
	private Label requiredDate;
	private Label requiredTime;
	
	private String cancelUrl 				= "requestAssignmentDetails.jsp?requestId=";
	private String requestId				= "";
	
	@Override
	public void init() {
		initForm();
		manpowers = new ArrayList();
	}
	
	public void onRequest(Event event) {
		this.setInvalid(false);
		//manpowers = new ArrayList();
		//initForm();
		if ("asg".equals(act)){
			init();
		}
		
		populateFields();
	}
	
	public void populateButtons() {
		Application app = Application.getInstance();
		buttonPanel = new Panel("panel");
		buttonPanelDiff = new Panel("panelDiff");
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		submitDiffMan = new Button("submitDiffMan", app.getMessage("fms.facility.submit"));
		cancelDiffMan = new Button("cancelDiffMan", app.getMessage("fms.facility.cancel"));
		buttonPanel.addChild(submit);
		buttonPanel.addChild(cancel);
		
		buttonPanelDiff.addChild(submitDiffMan);
		buttonPanelDiff.addChild(cancelDiffMan);
		
		addChild(new Label(("tupuku")));
		addChild(buttonPanel);
		addChild(buttonPanelDiff);
	}

	public void initForm() {
		removeChildren();
		setMethod("post");
		
		requiredDate = new Label("requiredDate");
		addChild(requiredDate);
		
		requiredTime =new Label("requiredTime");
		addChild(requiredTime);		
		
		userSelectBox=new UnitApprovalUserSelectBox("userSelectBox");
	    userSelectBox.init();
	    addChild(userSelectBox);
	    
	    // For those who has the permission to assign different manpower show the altUserSelectBox
	    altUserSelectBox = new UnitUserSelectBox("altUserSelectBox");
	    altUserSelectBox.init();
	    addChild(altUserSelectBox);
	    
		populateButtons();
		
	}
	
	private void populateFields(){
		Application app = Application.getInstance();
		EngineeringModule module = (EngineeringModule) app.getModule(EngineeringModule.class);
		FMSRegisterManager manager = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
		SetupModule setMod=(SetupModule) app.getModule(SetupModule.class);
		EngineeringRequest eRequest = module.getAssignment(getId());
		Collection userCol = null;
		String[] manpowerId = null;
		String[] manpowerAltId = null;
		manpowers = new ArrayList();
		
		Date stDate=eRequest.getRequiredFrom();
		Date edDate=eRequest.getRequiredTo();
		String fromTime = eRequest.getFromTime();
		String toTime = eRequest.getToTime();
		
		if (eRequest!=null){
			try {
				userCol = manager.getUnitUsers(unitId, null, generateDaoProperties(), 0, -1, "firstName", false);
				
				for( Iterator iterate = userCol.iterator(); iterate.hasNext(); ){
					User user = (User) iterate.next();
					userSelectBox.addOption(user.getId(), user.getName());
				}
			} catch (DaoException e) {
				 Log.getLog(getClass()).error(e.toString());
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			String reqDate = sdf.format(eRequest.getRequiredFrom()) + " - " + sdf.format(eRequest.getRequiredTo());
			try {
				manpowerId = userSelectBox.getIds();
				if(!altUserSelectBox.getOptionMap().isEmpty()){
					manpowerAltId = altUserSelectBox.getIds();
				}
				addUserList(module, manpowerId, stDate, edDate, fromTime,toTime);
				
				addUserList(module, manpowerAltId, stDate, edDate, fromTime,toTime);
				
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
			}
			requiredDate.setText(reqDate);
			requiredTime.setText(eRequest.getFromTime() + " - " + eRequest.getToTime());
			requestId = eRequest.getRequestId();
		}
	}

	private void addUserList(EngineeringModule module, String[] manpowerId,
			Date stDate, Date edDate, String fromTime, String toTime)
			throws DaoException {
		if (manpowerId!=null && manpowerId.length >0){
			for (int i=0; i < manpowerId.length; i++){
				HashMap map;
				map = (HashMap) module.selectManpowerByCompetencyId(manpowerId[i], id, stDate, edDate, fromTime, toTime);
				manpowers.add(map);
			}
		}
	}
	

	
	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);
		
	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);

	    //if the cancel button was pressed
	    if (buttonName != null && (cancel.getAbsoluteName().equals(buttonName) || cancelDiffMan.getAbsoluteName().equals(buttonName))) {
	    	init();
	      	return new Forward("cancel");
	    } else {
	    	act = "submit";
	    	return result;
	    }
	}
	
	public Forward onValidate(Event event) {
		String buttonName = findButtonClicked(event);
		if(buttonName != null && submit.getAbsoluteName().equals(buttonName)){
			return createUserAssignmentList(event, userSelectBox); 
		}else if(buttonName != null && submitDiffMan.getAbsoluteName().equals(buttonName)){
			return createUserAssignmentList(event, altUserSelectBox); 	
		}
		return null;
	}
	
	protected DaoQuery generateDaoProperties() {
        DaoQuery properties = new DaoQuery();
        properties.addProperty(new OperatorEquals("u.active", "1", DaoOperator.OPERATOR_AND));
        return properties;
    }
	
	private Forward createUserAssignmentList(Event event, UsersSelectBox user) {
		try {
			
			String[] manpowerId = null;
			manpowers = new ArrayList();
			//dateSelected = new ArrayList();
			
			Application app = Application.getInstance();
			EngineeringModule module = (EngineeringModule)app.getModule(EngineeringModule.class);
			EngineeringRequest eRequest = module.getAssignment(id);
			
			Date stDate=eRequest.getRequiredFrom();
			Date edDate=eRequest.getRequiredTo();
			String fromTime = eRequest.getFromTime();
			String toTime = eRequest.getToTime();
			
			// compare date checking : start v.s. end
			if(stDate.after(edDate)){
				this.setInvalid(true);
			}		
			
			// get selected manpower type (from popup) 
			manpowerId = user.getIds();
			
			if (manpowerId!=null && manpowerId.length >0){
				HttpServletRequest request = event.getRequest();
				request.setAttribute("selectedKeys", manpowerId);
				event.setRequest(request);	    		
				
				request.setAttribute("id", id);
				event.setRequest(request);
				
				for (int i=0; i < manpowerId.length; i++){
					HashMap map = (HashMap) module.selectManpowerByCompetencyId(manpowerId[i], id, stDate, edDate, fromTime, toTime);
					manpowers.add(map);
				}
			}
			
			//initForm();
			return new Forward("VIEW", "assignManpower.jsp?id=" + getId(), true);
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.toString()); 
			return new Forward("FAILED");
		}
	}
	
	public String getDefaultTemplate(){
		return "fms/assignmanpower";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}	

	public UnitApprovalUserSelectBox getUserSelectBox() {
		return userSelectBox;
	}

	public void setUserSelectBox(UnitApprovalUserSelectBox userSelectBox) {
		this.userSelectBox = userSelectBox;
	}
	
	public UnitUserSelectBox getAltUserSelectBox() {
		return altUserSelectBox;
	}

	public void setAltUserSelectBox(UnitUserSelectBox altUserSelectBox) {
		this.altUserSelectBox = altUserSelectBox;
	}

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}
	
	public Panel getButtonPanelDiff() {
		return buttonPanelDiff;
	}

	public void setButtonPanelDiff(Panel buttonPanelDiff) {
		this.buttonPanelDiff = buttonPanelDiff;
	}

	public Panel getPnType() {
		return pnType;
	}

	public void setPnType(Panel pnType) {
		this.pnType = pnType;
	}

	public Collection getManpowers() {
		return manpowers;
	}

	public void setManpowers(Collection manpowers) {
		this.manpowers = manpowers;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}	
}
