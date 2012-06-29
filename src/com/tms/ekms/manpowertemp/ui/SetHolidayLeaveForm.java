package com.tms.ekms.manpowertemp.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;


import com.tms.ekms.manpowertemp.model.ManpowerLeaveObject;
import com.tms.ekms.manpowertemp.model.ManpowerModule;
import com.tms.ekms.manpowertemp.model.SetHolidayLeaveObject;
import com.tms.ekms.security.ui.LeaveUserSelectBox;
import com.tms.fms.facility.ui.UnitUserSelectBox;

public class SetHolidayLeaveForm extends Form {
	
	// select leave
	private Label lbType;
	private Radio radLeave;
	private Radio radHoliday;
	private Label lbLeaveType;
	private SelectBox leaveType;
	private Label lbHoliday;
	
	//select holiday
	private TextField holiday;
	private Label lbDateFrom;
	private DatePopupField dateFrom;
	private Label lbDateTo;
	private DatePopupField dateTo;
	private UnitUserSelectBox userSelectBox;

	private Button btnsave;
	private Button btncancel;
 
	private String cancelUrl = "leaveView.jsp";
	public String id;
	public String manpowerId;
	public String action;
	public String page = "leave";

	public SetHolidayLeaveForm() {
	}

	public void init() {

		String msgNotEmpty = "Mandatory Field";
		setMethod("POST");
		setColumns(2);
		
		// select leave
		lbType = new Label("lbType", "Type");
		addChild(lbType);

		radLeave = new Radio("radLeave", "Leave");
		radLeave.setGroupName("category");
		if (getPage().equals("leave")){
			radLeave.setChecked(true);
			setCancelUrl("leaveView.jsp");
		}
		addChild(radLeave);
		radLeave.setOnClick("isLeave('true');");

		radHoliday = new Radio("radHoliday", "Holiday");
		radHoliday.setGroupName("category");
		if (getPage().equals("holiday")){
			radHoliday.setChecked(true);
			setCancelUrl("holidayView.jsp");
		}
		addChild(radHoliday);
		radHoliday.setOnClick("isLeave('false');");

		lbLeaveType = new Label("lbLeaveType", "Leave Type");
		addChild(lbLeaveType);
		leaveType = new SelectBox("leaveType");
		leaveType.addChild(new ValidatorNotEmpty("leaveTypeNotEmpty",msgNotEmpty));

		ManpowerModule mod = (ManpowerModule) Application.getInstance().getModule(ManpowerModule.class);
		Collection leaveTypeList = new ArrayList();
		try {
			leaveTypeList = mod.getLeaveType();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		leaveType.addOption("-1", "-- Please Select --");
		
		Map mpkey = new SequencedHashMap();				
		mpkey.put("-1", "-----None-----");
		if(leaveTypeList.size() > 0){
			for (Iterator iterator = leaveTypeList.iterator(); iterator.hasNext();) {
				Map mp = (Map) iterator.next();
				mpkey.put(mp.get("id").toString(), mp.get("leaveTypeName").toString());
				leaveType.setOptionMap(mpkey);
			}
		}else
			leaveType.setOptionMap(mpkey);
		
		addChild(leaveType);
		
		// select holiday
		lbHoliday = new Label("lbHoliday", "Holiday");
		addChild(lbHoliday);
		holiday = new TextField("holiday");
		holiday.setSize("50");
		holiday.setMaxlength("255");
		//holiday.addChild(new ValidatorNotEmpty("HolidayNotEmpty", msgNotEmpty));
		addChild(holiday);

		lbDateFrom = new Label("lbDateFrom", "Date From");
		addChild(lbDateFrom);
		dateFrom = new DatePopupField("dateFrom");
		dateFrom.setFormat("dd-MM-yyyy");
		addChild(dateFrom);

		lbDateTo = new Label("lbDateTo", "Date To");
		addChild(lbDateTo);
		dateTo = new DatePopupField("dateTo");
		dateTo.setFormat("dd-MM-yyyy");
		addChild(dateTo);
		
		//userSelectBox
		userSelectBox = new UnitUserSelectBox("userSelectBox");
		userSelectBox.setSortable(false);
	    addChild(userSelectBox);
	    userSelectBox.init();
	   
		btnsave = new Button("btnsave", Application.getInstance().getMessage("fms.manpower.form.btnSubmit","Save"));
		btncancel = new Button("btncancel", Application.getInstance().getMessage("fms.manpower.form.btnCancel", "Cancel"));
		addChild(btnsave);
		addChild(btncancel);
	}

	public void onRequest(Event evt) {
		removeChildren();
		init();
		super.onRequest(evt);
		this.setInvalid(false);

		if(evt.getParameter("action")!=null){
			if(evt.getParameter("action").equals("edit")){
				populateData(evt.getParameter("id"));
				setAction("edit");
				setId(evt.getParameter("id"));
			}else{
				init();
				setAction("add");
				setId("");
			}
		}else{			
			if (evt.getParameter("page")!=null){
				if (evt.getParameter("page").equals("leave")){
					setPage("leave");
				} else {
					setPage("holiday");
				}
			}
			init();
			setAction("add");
			setId("");
		}
		
	}
	
	public void populateData(String id){
		ManpowerModule mod=(ManpowerModule) Application.getInstance().getModule(ManpowerModule.class);
		SetHolidayLeaveObject hObject =new SetHolidayLeaveObject();
		try{
			hObject=mod.getHolidayLeave(id);
			if (hObject != null && hObject.getId() != null) {
				if("L".equals(hObject.getType())){
					radLeave.setChecked(true);
					//userSelectBox.setIds(arg0)
					setCancelUrl("leaveView.jsp");
				} else if ("H".equals(hObject.getType())) {
					radHoliday.setChecked(true);
					setCancelUrl("holidayView.jsp");
				}
				
				leaveType.setSelectedOptions(new String[]{hObject.getLeaveType()});
				if(hObject.getDateFrom()!=null){
					dateFrom.setDate(hObject.getDateFrom());
				}else{
					dateFrom.setDate(null);
				}
				if(hObject.getDateTo()!=null){
					dateTo.setDate(hObject.getDateTo());
				}else{
					dateTo.setDate(null);
				}
				if(hObject.getHoliday()!=null){
			    	holiday.setValue(hObject.getHoliday().toString());
			    }else{
			    	holiday.setValue("");
			    }
				
				Collection mp = mod.getManpowerLeaveByLeaveId(hObject.getId());
				if (mp != null && mp.size()>0){
					Collection mpl = new ArrayList(); 
					for (Iterator m = mp.iterator();m.hasNext();){
						ManpowerLeaveObject mlo = (ManpowerLeaveObject)m.next();
						
						mpl.add(mlo.getManpowerId());
						userSelectBox.setIds((String[])mpl.toArray(new String[0]));
					}
					
				}
			}
		}catch (Exception ex){
			Log.getLog(getClass()).error(ex.toString());
		}
	}

	/*public Forward actionPerformed(Event event) {
		Forward forward = new Forward();
		if (btncancel.getAbsoluteName().equals(findButtonClicked(event))) {
			init();
			id=null;
			return new Forward(CANCEL_FORM_ACTION);
		}
		forward = super.actionPerformed(event);
		return forward;
	}*/
	public Forward onSubmit(Event evt) {
	    Forward result = super.onSubmit(evt);

	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);

	    //if the cancel button was pressed
	    if (buttonName != null && btncancel.getAbsoluteName().equals(buttonName)) {
	      //init();
	      return new Forward(Form.CANCEL_FORM_ACTION, getCancelUrl(), true);
	    }
	    else {return result;}
	  }


	public Forward onValidate(Event evt) {
		Forward forw = null;
		ManpowerModule mod = (ManpowerModule) Application.getInstance().getModule(ManpowerModule.class);
		boolean isexist=false;
		try{
			isexist=mod.isExistHoliday(holiday.getValue().toString());
		}catch (DaoException e){
			e.printStackTrace();
		}
		
		
		////
		Calendar end = Calendar.getInstance();	
		end.setTime(dateTo.getDate());
		end.set(Calendar.HOUR_OF_DAY,23);
		end.set(Calendar.MINUTE,59);
		////
		
		
		if (getAction().equals("edit")){
			ManpowerLeaveObject mlo=new ManpowerLeaveObject();
			SetHolidayLeaveObject hlob = new SetHolidayLeaveObject();
			hlob.setId(getId());
			hlob.setUpdatedBy(getWidgetManager().getUser().getId());
			hlob.setUpdatedDate(new Date());		
		
			if (radLeave.isChecked()) {
				if (userSelectBox.getIds().length > 0){
					hlob.setType("L");
					hlob.setLeaveType(getSelectBoxValue(leaveType));
					hlob.setDateFrom(dateFrom.getDate());
					hlob.setDateTo(end.getTime());
					mlo.setLeaveId(id);
					mod.updateHolidayLeave(hlob);
					mod.addManpowerLeave(hlob.getId(), userSelectBox.getIds());
					forw=new Forward("updateLeave");
				} else {
					userSelectBox.setInvalid(true);
					forw = new Forward("saveLeaveFailed");
				}
			} else if (radHoliday.isChecked()) {
				hlob.setType("H");
				hlob.setHoliday(holiday.getValue().toString());
				hlob.setDateFrom(dateFrom.getDate());
				hlob.setDateTo(end.getTime());
				if(!isexist){
					mod.updateHolidayLeave(hlob);
					forw=new Forward("updateHoliday");
				}else{
					forw=new Forward("duplicateHoliday");
				}
			}
		}else{
			ManpowerLeaveObject mlob=new ManpowerLeaveObject();
			SetHolidayLeaveObject hlo = new SetHolidayLeaveObject();
			hlo.setId(UuidGenerator.getInstance().getUuid());
			hlo.setCreatedBy(getWidgetManager().getUser().getId());
			hlo.setCreatedDate(new Date());
			if (radLeave.isChecked()) {
				if (userSelectBox.getIds().length > 0){
					hlo.setType("L");
					hlo.setLeaveType(getSelectBoxValue(leaveType));
					hlo.setDateFrom(dateFrom.getDate());
					hlo.setDateTo(end.getTime());
					mlob.setId(UuidGenerator.getInstance().getUuid());
					mlob.setLeaveId(hlo.getId());
					mod.addHolidayLeave(hlo);
					mod.addManpowerLeave(hlo.getId(), userSelectBox.getIds()); 	//// by RvP
					forw = new Forward("saveLeave");
				} else {
					userSelectBox.setInvalid(true);
					forw = new Forward("saveLeaveFailed");
				}
			} else if (radHoliday.isChecked()) {
				hlo.setType("H");
				hlo.setHoliday(holiday.getValue().toString());
				hlo.setDateFrom(dateFrom.getDate());
				hlo.setDateTo(end.getTime());
				if(!isexist){
					mod.addHolidayLeave(hlo);
					forw=new Forward("saveHoliday");
				}else{
					forw=new Forward("duplicateHoliday");
				}
			}
		}
		return forw;
	}

	private String getSelectBoxValue(SelectBox sb) {
		if (sb != null) {
			Map selected = sb.getSelectedOptions();
			if (selected.size() == 1) {
				return (String) selected.keySet().iterator().next();
			}
		}
		return null;
	}

	public String getDefaultTemplate() {
		return "fms/holidayLeaveSetupTemplate";
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public SelectBox getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(SelectBox leaveType) {
		this.leaveType = leaveType;
	}

	public Label getLbType() {
		return lbType;
	}

	public void setLbType(Label lbType) {
		this.lbType = lbType;
	}

	public Label getLbLeaveType() {
		return lbLeaveType;
	}

	public void setLbLeaveType(Label lbLeaveType) {
		this.lbLeaveType = lbLeaveType;
	}

	public Label getLbDateFrom() {
		return lbDateFrom;
	}

	public void setLbDateFrom(Label lbDateFrom) {
		this.lbDateFrom = lbDateFrom;
	}

	public DatePopupField getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(DatePopupField dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Label getLbDateTo() {
		return lbDateTo;
	}

	public void setLbDateTo(Label lbDateTo) {
		this.lbDateTo = lbDateTo;
	}

	public DatePopupField getDateTo() {
		return dateTo;
	}

	public void setDateTo(DatePopupField dateTo) {
		this.dateTo = dateTo;
	}

	public Button getBtnsave() {
		return btnsave;
	}

	public void setBtnsave(Button btnsave) {
		this.btnsave = btnsave;
	}

	public Button getBtncancel() {
		return btncancel;
	}

	public void setBtncancel(Button btncancel) {
		this.btncancel = btncancel;
	}

	public Label getLbHoliday() {
		return lbHoliday;
	}

	public void setLbHoliday(Label lbHoliday) {
		this.lbHoliday = lbHoliday;
	}

	public TextField getHoliday() {
		return holiday;
	}

	public void setHoliday(TextField holiday) {
		this.holiday = holiday;
	}

	public Radio getRadLeave() {
		return radLeave;
	}

	public void setRadLeave(Radio radLeave) {
		this.radLeave = radLeave;
	}

	public Radio getRadHoliday() {
		return radHoliday;
	}

	public void setRadHoliday(Radio radHoliday) {
		this.radHoliday = radHoliday;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getManpowerId() {
		return manpowerId;
	}

	public void setManpowerId(String manpowerId) {
		this.manpowerId = manpowerId;
	}

	public UnitUserSelectBox getUserSelectBox() {
		return userSelectBox;
	}

	public void setUserSelectBox(UnitUserSelectBox userSelectBox) {
		this.userSelectBox = userSelectBox;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

}
