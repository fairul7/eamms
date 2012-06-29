package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.tms.fms.transport.model.*;

import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class VehicleForm extends Form{
	
	public static final String VEHICLE_ADD_SUCCESS = "vehicle.success";
	public static final String VEHICLE_ADD_FAIL = "vehicle.fail";
	public static final String VEHICLE_ADD_EXIST = "vehicle.exist";
	
	public static final String VEHICLE_ACTION_ADD = "vehicle.action.add";
	public static final String VEHICLE_ACTION_EDIT = "vehicle.action.edit";
	public static final String VEHICLE_ACTION_VIEW = "vehicle.action.view";
	
	private Label lbVehicleNum;
	private Label lbChannel;
	private Label lbCategory;
	private Label lbType;
	private Label lbEngineNo;
	private Label lbChasisNo;
	private Label lbMakeType;
	private Label lbModelName;
	private Label lbEngineCapacity;
	private Label lbColour;
	private Label lbBodyType;
	private Label lbLocation;
	private Label lbYear;
	private Label lbNCB;
	private Label lbRegistrationDate;
	private Label lbNumOfPassengers;
	private Label lbCharge;
	private Label lbMaintenanceType;
	private Label lbRTRenewalDate;
	private Label lbInsuranceDate;
	private Label lbWriteoffReason;
	private Label lbWriteoffBy;
	private Link liWriteoffLink;
	
	private TextField tfVehicleNum;
	private SelectBox sbChannel;
	private SelectBox sbCategory;
	private Radio rdManagementVehicle;
	private Radio rdPoolVehicle;
	private TextField tfEngineNo;
	private TextField tfChasisNo;
	private SelectBox sbMakeType;
	private TextField tfModelName;
	private TextField tfEngineCapacity;
	private TextField tfColour;
	private TextField tfBodyType;
	private TextField tfLocation;
	private TextField tfYear;
	private TextField tfNCB;
	private DatePopupField dpfRegistrationDate;
	private TextField tfNumOfPassengers;
	private SelectBox sbCharge;
	private TextField tfPerDay;
	private TextField tfPerHour;
	private Radio rdByKM;
	private Radio rdByMonth;
	private TextField tfByKM;
	private TextField tfByMonth;
	private DatePopupField dpfRenewalDate;
	private DatePopupField dpfInsuranceDate;
	private Button btnSubmit;
	private Button btnCancel;
	private Button btnEdit;
	private Button btnStatus;
	private Button btnMaintenanceService;
	private Button btnRoadTax;
	private DateField tmpDate;
	
	
	private String vehicle_num;
	private String action;
	private String cancelUrl = "";
	private String maintenanceUrl = "";
	private String roadTaxUrl = "";
	private String editUrl = "";
	private String statusUrl = "";
	private String whoModifyId = "";
	private String status = "1";
	
	public String getStatusUrl() {
		return statusUrl;
	}

	public void setStatusUrl(String statusUrl) {
		this.statusUrl = statusUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicle_num() {
		return vehicle_num;
	}
	
	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
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
	
	public void init() {
		if (action == null || (!VEHICLE_ACTION_ADD.equals(action) && !VEHICLE_ACTION_EDIT.equals(action) && !VEHICLE_ACTION_VIEW.equals(action))) {action = VEHICLE_ACTION_ADD;}
    }
	
	public void onRequest(Event event) {
		initForm();
	    if (VEHICLE_ACTION_EDIT.equals(action) || VEHICLE_ACTION_VIEW.equals(action)) {populateFields();}
    }

	public void initForm() {
		
	    Application application = Application.getInstance();
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
	    String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
	    String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");

	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.tran.form.vehicleNumber*", "Vehicle Number*")));
	    
	    if (VEHICLE_ACTION_ADD.equals(action)) {
		    tfVehicleNum = new TextField("tfVehicleNum");
		    tfVehicleNum.setSize("50");
		    tfVehicleNum.setMaxlength("255");
		    tfVehicleNum.addChild(new ValidatorNotEmpty("NameNotEmpty", msgNotEmpty));
			addChild(tfVehicleNum);
	    }
	    if (VEHICLE_ACTION_EDIT.equals(action) || VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbVehicleNum = new Label("lbVehicleNum");
	    	addChild(lbVehicleNum);
		}
	    
	    addChild(new Label("lb2", Application.getInstance().getMessage("fms.tran.form.channel*", "Channel*")));
	    
	    if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
		    sbChannel = new SelectBox("sbChannel");
		    sbChannel.setOptions(initialSelect);
		    try {
				TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
				Collection lstChannel = mod.selectSetupObject(SetupObject.SETUP_CHANNEL, "", "1", "name", false, 0, -1);
			    if (lstChannel.size() > 0) {
			    	for (Iterator i=lstChannel.iterator(); i.hasNext();) {
			        	SetupObject o = (SetupObject)i.next();
			        	sbChannel.setOptions(o.getSetup_id()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			sbChannel.addChild(new ValidatorSelectBoxNotEmpty("sbChannelNotEmpty", msgNotEmpty));
		    addChild(sbChannel);
	    }
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbChannel = new Label("lbChannel");
	    	addChild(lbChannel);
		} 
	    
	    addChild(new Label("lb3", Application.getInstance().getMessage("fms.tran.form.category", "Vehicle Category")));
	    
	    if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
		    sbCategory = new SelectBox("sbCategory");
		    sbCategory.setOptions(initialSelect);
		    try {
				TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
				Collection lstCategory = mod.selectSetupObject(SetupObject.SETUP_CATEGORY, "", "1", "name", false, 0, -1);
			    if (lstCategory.size() > 0) {
			    	for (Iterator i=lstCategory.iterator(); i.hasNext();) {
			        	SetupObject o = (SetupObject)i.next();
			        	sbCategory.setOptions(o.getSetup_id()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			sbCategory.addChild(new ValidatorSelectBoxNotEmpty("sbCategoryNotEmpty", msgNotEmpty));
			addChild(sbCategory);
	    }
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbCategory = new Label("lbCategory");
	    	addChild(lbCategory);
		}
	    
		addChild(new Label("lb4", Application.getInstance().getMessage("fms.tran.form.vehicleType", "Vehicle Type")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			Panel pnType = new Panel("pnType");
			rdManagementVehicle = new Radio("rdManagementVehicle", Application.getInstance().getMessage("fms.tran.setup.managementVehicle", "Management Vehicle"), true);
			rdPoolVehicle = new Radio("rdPoolVehicle", Application.getInstance().getMessage("fms.tran.setup.poolVehicle", "Pool Vehicle"));
			rdManagementVehicle.setGroupName("typeGroup");
			rdPoolVehicle.setGroupName("typeGroup");
			pnType.addChild(rdManagementVehicle);
			pnType.addChild(rdPoolVehicle);
			addChild(pnType);
		}
		if (VEHICLE_ACTION_VIEW.equals(action)) {
			lbType = new Label("lbType");
	    	addChild(lbType);
		}
		
		addChild(new Label("lb5", Application.getInstance().getMessage("fms.tran.form.engineNumber", "Engine No")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			tfEngineNo = new TextField("tfEngineNo");
			tfEngineNo.setSize("50");
			tfEngineNo.setMaxlength("255");
		    addChild(tfEngineNo);
		}
		if (VEHICLE_ACTION_VIEW.equals(action)) {
			lbEngineNo = new Label("lbEngineNo");
	    	addChild(lbEngineNo);
		}

	    addChild(new Label("lb6", Application.getInstance().getMessage("fms.tran.form.casisNo", "Casis No")));
	    
	    if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
		    tfChasisNo = new TextField("tfChasisNo");
		    tfChasisNo.setSize("50");
		    tfChasisNo.setMaxlength("255");
		    addChild(tfChasisNo);
	    }
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbChasisNo = new Label("lbChasisNo");
	    	addChild(lbChasisNo);
		}
	    
		addChild(new Label("lb7", Application.getInstance().getMessage("fms.tran.form.makeType", "Make Type")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			sbMakeType = new SelectBox("sbMakeType");
			sbMakeType.setOptions(initialSelect);
		    try {
				TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
				Collection lstMakeType = mod.selectSetupObject(SetupObject.SETUP_MAKE_TYPE, "", "1", "name", false, 0, -1);
			    if (lstMakeType.size() > 0) {
			    	for (Iterator i=lstMakeType.iterator(); i.hasNext();) {
			        	SetupObject o = (SetupObject)i.next();
			        	sbMakeType.setOptions(o.getSetup_id()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			sbMakeType.addChild(new ValidatorSelectBoxNotEmpty("sbMakeTypeNotEmpty", msgNotEmpty));
			addChild(sbMakeType);
		}
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbMakeType = new Label("lbMakeType");
	    	addChild(lbMakeType);
		}
		
		addChild(new Label("lb8", Application.getInstance().getMessage("fms.tran.form.modelName", "Model Name")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			tfModelName = new TextField("tfModelName");
			tfModelName.setSize("50");
			tfModelName.setMaxlength("255");
			addChild(tfModelName);
		}
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbModelName = new Label("lbModelName");
	    	addChild(lbModelName);
		}
		
	    addChild(new Label("lb9", Application.getInstance().getMessage("fms.tran.form.engineCapacity", "Engine Capacity")));
	    
	    if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
		    tfEngineCapacity = new TextField("tfEngineCapacity");
		    tfEngineCapacity.setSize("50");
		    tfEngineCapacity.setMaxlength("255");
		    tfEngineCapacity.addChild(new ValidatorIsNumeric("tfEngineCapacityIsNumberic", msgIsNumberic, false));
		    addChild(tfEngineCapacity);
	    }
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbEngineCapacity = new Label("lbEngineCapacity");
	    	addChild(lbEngineCapacity);
		}
	    
	    addChild(new Label("lb10", Application.getInstance().getMessage("fms.tran.form.color", "Color")));
	    
	    if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
		    tfColour = new TextField("tfColour");
		    tfColour.setSize("50");
		    tfColour.setMaxlength("255");
		    addChild(tfColour);
	    }
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbColour = new Label("lbColour");
	    	addChild(lbColour);
		}
	    
		addChild(new Label("lb11", Application.getInstance().getMessage("fms.tran.form.bodyType", "Body Type")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			tfBodyType = new TextField("tfBodyType");
			tfBodyType.setSize("50");
			tfBodyType.setMaxlength("255");
			tfBodyType.setValue("-");
			addChild(tfBodyType);
		}
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbBodyType = new Label("lbBodyType");
	    	addChild(lbBodyType);
		}
	    
		addChild(new Label("lb12", Application.getInstance().getMessage("fms.tran.form.location", "Location")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			tfLocation = new TextField("tfLocation");
			tfLocation.setSize("50");
			tfLocation.setMaxlength("255");
		    addChild(tfLocation);
		}
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbLocation = new Label("lbLocation");
	    	addChild(lbLocation);
		}
	    
	    addChild(new Label("lb13", Application.getInstance().getMessage("fms.tran.form.year", "Year")));
	    
	    if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
		    tfYear = new TextField("tfYear");
		    tfYear.setSize("20");
		    tfYear.setMaxlength("20");
		    tfYear.addChild(new ValidatorIsNumeric("tfYearIsNumberic", msgIsNumberic));
		    Calendar year = Calendar.getInstance();
		    tfYear.addChild(new ValidatorNumberInRange("tfYearIsInRange", Application.getInstance().getMessage("fms.tran.msg.invalidYear", "Invalid Year"), 1000, year.get(Calendar.YEAR)+1));
		    addChild(tfYear);
	    }
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbYear = new Label("lbYear");
	    	addChild(lbYear);
		}
	    
	    addChild(new Label("lb14", Application.getInstance().getMessage("fms.tran.form.ncb", "NCB")));
	    
	    if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
		    tfNCB = new TextField("tfNCB");
		    tfNCB.setSize("20");
		    tfNCB.setMaxlength("20");
		    tfNCB.addChild(new ValidatorIsNumeric("tfNCBIsNumberic", msgIsNumberic));
		    addChild(tfNCB);
	    }
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbNCB = new Label("lbNCB");
	    	addChild(lbNCB);
		}
	    
		addChild(new Label("lb15", Application.getInstance().getMessage("fms.tran.form.registrationDate", "Registration Date")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			dpfRegistrationDate = new DatePopupField("dpfRegistrationDate");
			dpfRegistrationDate.setFormat("dd-MM-yyyy");
			dpfRegistrationDate.setDate(new Date());
			tmpDate = new DateField("tmpDate");
			tmpDate.setDate(new Date());
			addChild(tmpDate);
			dpfRegistrationDate.addChild(new ValidatorDateCompare("dpfRegistrationDateIsValid", ValidatorDateCompare.COMPARE_LESS_EQUALS, tmpDate,  Application.getInstance().getMessage("fms.tran.msg.registrationDateCannotBeFutureDate", "Registration date cannot be future date")));
			addChild(dpfRegistrationDate);
		}
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbRegistrationDate = new Label("lbRegistrationDate");
	    	addChild(lbRegistrationDate);
		}
	    
		addChild(new Label("lb16", Application.getInstance().getMessage("fms.tran.form.numberOfPassengers", "Number Of Passengers")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			tfNumOfPassengers = new TextField("tfNumOfPassengers");
			tfNumOfPassengers.setSize("20");
			tfNumOfPassengers.setMaxlength("20");
			tfNumOfPassengers.addChild(new ValidatorIsNumeric("tfNumOfPassengersIsNumberic", msgIsNumberic));
		    addChild(tfNumOfPassengers);
		}
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbNumOfPassengers = new Label("lbNumOfPassengers");
	    	addChild(lbNumOfPassengers);
		}
	    
		addChild(new Label("lb17", Application.getInstance().getMessage("fms.tran.form.fuelType", "Fuel Type")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			sbCharge = new SelectBox("sbCharge");
			sbCharge.setOptions(initialSelect);
		    try {
				TransportDao dao = (TransportDao)Application.getInstance().getModule(TransportModule.class).getDao();
				Collection lstCharge = dao.selectRateCardName();
			    if (lstCharge.size() > 0) {
			    	for (Iterator i=lstCharge.iterator(); i.hasNext();) {
			    		RateCardObject o = (RateCardObject)i.next();
			        	sbCharge.setOptions(o.getSetup_id()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			sbCharge.addChild(new ValidatorSelectBoxNotEmpty("sbFuelTypeNotEmpty", msgNotEmpty));
			addChild(sbCharge);
		}
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbCharge = new Label("lbCharge");
	    	addChild(lbCharge);
		}
	    
		addChild(new Label("lb21", Application.getInstance().getMessage("fms.tran.form.preventiveMaintenance", "Preventive Maintenance")));
		
		if (VEHICLE_ACTION_ADD.equals(action) || VEHICLE_ACTION_EDIT.equals(action)) {
			Panel pnMaintenance1 = new Panel("pnMaintenance1");
			pnMaintenance1.setColumns(2);
			pnMaintenance1.addChild(new Label("lb22", Application.getInstance().getMessage("fms.tran.form.maintenanceType", "Maintenance Type")));
			Panel pnMaintenance2 = new Panel("pnMaintenance2");
			rdByKM = new Radio("rdByKM", Application.getInstance().getMessage("fms.tran.form.byKM", "By KM"), true);
			rdByKM.setGroupName("maintenanceType");
			rdByKM.setOnClick("hideShowPM()");
			addChild(rdByKM);
			pnMaintenance2.addChild(new Label("lb23", "\t"));
			pnMaintenance2.addChild(new Label("lb24", Application.getInstance().getMessage("fms.tran.form.every", "Every")));
			tfByKM = new TextField("tfByKM");
			tfByKM.setSize("15");
			tfByKM.setMaxlength("15");
			tfByKM.setValue("0");
			tfByKM.addChild(new ValidatorIsNumeric("tfByKMIsNumberic", msgIsNumberic));
			pnMaintenance2.addChild(tfByKM);
			pnMaintenance2.addChild(new Label("lb25", Application.getInstance().getMessage("fms.tran.form.km", "km")));
			pnMaintenance1.addChild(pnMaintenance2);
			pnMaintenance1.addChild(new Label("lb26", ""));
			Panel pnMaintenance3 = new Panel("pnMaintenance3");
			rdByMonth = new Radio("rdByMonth", Application.getInstance().getMessage("fms.tran.form.byMonth", "By Month"));
			rdByMonth.setGroupName("maintenanceType");
			rdByMonth.setOnClick("hideShowPM()");
			addChild(rdByMonth);
			pnMaintenance3.addChild(new Label("lb27", "\t"));
			pnMaintenance3.addChild(new Label("lb28", Application.getInstance().getMessage("fms.tran.form.every", "Every")));
			tfByMonth = new TextField("tfByMonth");
			tfByMonth.setSize("15");
			tfByMonth.setMaxlength("15");
			tfByMonth.setValue("0");
			tfByMonth.addChild(new ValidatorIsNumeric("tfByMonthIsNumberic", msgIsNumberic));
			pnMaintenance3.addChild(tfByMonth);
			pnMaintenance3.addChild(new Label("lb29", Application.getInstance().getMessage("fms.tran.form.month", "month(s)")));
			pnMaintenance1.addChild(pnMaintenance3);
			//pnMaintenance1.addChild(new Label("lb30", Application.getInstance().getMessage("fms.tran.form.roadTaxRenewalDate", "Road Tax Renewal Date")));
			//dpfRenewalDate = new DatePopupField("dpfRenewalDate");
			//dpfRenewalDate.setFormat("dd-MM-yyyy");
			//dpfRenewalDate.setDate(new Date());
			//pnMaintenance1.addChild(dpfRenewalDate);
			//pnMaintenance1.addChild(new Label("lb31", Application.getInstance().getMessage("fms.tran.form.insuranceDate", "Insurance Date")));
			//dpfInsuranceDate = new DatePopupField("dpfInsuranceDate");
			//dpfInsuranceDate.setFormat("dd-MM-yyyy");
			//dpfInsuranceDate.setDate(new Date());
			//pnMaintenance1.addChild(dpfInsuranceDate);
			addChild(pnMaintenance1);
		}
	    if (VEHICLE_ACTION_VIEW.equals(action)) {
	    	lbMaintenanceType = new Label("lbMaintenanceType");
	    	addChild(lbMaintenanceType);
	    	//lbRTRenewalDate = new Label("lbRTRenewalDate");
	    	//addChild(lbRTRenewalDate);
	    	//lbInsuranceDate = new Label("lbInsuranceDate");
	    	//addChild(lbInsuranceDate);
	    	
	    	lbWriteoffReason = new Label("lbWriteoffReason");
	    	addChild(lbWriteoffReason);
	    	lbWriteoffBy = new Label("lbWriteoffBy");
	    	addChild(lbWriteoffBy);
	    	liWriteoffLink = new Link("liWriteoffLink");
	    	addChild(liWriteoffLink);
		}
	    
		addChild(new Label("lbbutton", ""));
		Panel pnButton = new Panel("pnButton");
	    if (VEHICLE_ACTION_ADD.equals(action)) {
	    	btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.tran.submit", "Submit"));
	    	pnButton.addChild(btnSubmit);
	    	btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.tran.cancel", "Cancel"));
		    pnButton.addChild(btnCancel);
	    }
		if (VEHICLE_ACTION_EDIT.equals(action)) {
			btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.tran.update", "Update"));
			pnButton.addChild(btnSubmit);
			btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.tran.cancel", "Cancel"));
		    pnButton.addChild(btnCancel);
		}   
		if (VEHICLE_ACTION_VIEW.equals(action)) {
			btnEdit = new Button("btnEdit", Application.getInstance().getMessage("fms.tran.form.edit", "Edit Details"));
			pnButton.addChild(btnEdit);
			btnMaintenanceService = new Button("btnMaintenanceService", Application.getInstance().getMessage("fms.tran.form.maintenanceService", "Maintenance Service"));
			pnButton.addChild(btnMaintenanceService);
			btnRoadTax = new Button("btnRoadTax", Application.getInstance().getMessage("fms.tran.form.roadTax", "Road Tax/ Insurance Tracking"));
			pnButton.addChild(btnRoadTax);
			btnStatus = new Button("btnStatus", Application.getInstance().getMessage("fms.tran.form.statusLogs", "Status Logs"));
			pnButton.addChild(btnStatus);
			btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.tran.back", "Back"));
		    pnButton.addChild(btnCancel);
		}
	    addChild(pnButton);
	}
	
	public void populateFields() {
		TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
	    VehicleObject v = new VehicleObject();
	    try {
	    	v = module.getVehicle(vehicle_num);
	    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	    	
	    	lbVehicleNum.setText(v.getVehicle_num());
	    	
	    	if(VEHICLE_ACTION_VIEW.equals(action)){
	    		lbChannel.setText(v.getChannel_name());
		    	lbCategory.setText(v.getCategory_name());
		    	lbType.setText((v.getType() == "M")? Application.getInstance().getMessage("fms.tran.setup.managementVehicle", "Management Vehicle"):Application.getInstance().getMessage("fms.tran.setup.poolVehicle", "Pool Vehicle"));
		    	lbEngineNo.setText(v.getEngine_num());
		    	lbChasisNo.setText(v.getCasis_no());
		    	lbMakeType.setText(v.getMaketype_name());
		    	lbModelName.setText(v.getModel());
		    	lbEngineCapacity.setText(v.getEngine_cap());
		    	lbColour.setText(v.getColor());
		    	lbBodyType.setText(v.getBodytype_name());
		    	lbLocation.setText(v.getLocation());
		    	lbYear.setText(v.getYear());
		    	lbNCB.setText(v.getNcb());
		    	lbRegistrationDate.setText(formatter.format(v.getReg_date()));
		    	lbNumOfPassengers.setText(Integer.toString(v.getPassenger_cap()));
		    	lbCharge.setText(v.getCharge_name());
		    	String mType = "";
		    	if(v.getMaintain_type() == "K" || "K".equals(v.getMaintain_type())){
		    		mType = Application.getInstance().getMessage("fms.tran.form.byKM", "By KM");
		    		mType = mType + " - " + Application.getInstance().getMessage("fms.tran.form.every", "Every") + " ";
		    		mType = mType + v.getBy_km() + " " + Application.getInstance().getMessage("fms.tran.form.km", "km");
		    	}else{
		    		mType = Application.getInstance().getMessage("fms.tran.form.byMonth", "By Month");
		    		mType = mType + " - " + Application.getInstance().getMessage("fms.tran.form.every", "Every") + " ";
		    		mType = mType + v.getBy_month() + " " + Application.getInstance().getMessage("fms.tran.form.month", "Month(s)");
		    	}
		    	
		    	lbMaintenanceType.setText(mType);
		    	//lbRTRenewalDate.setText(Application.getInstance().getMessage("fms.tran.form.roadTaxRenewalDate", "Road Tax Renewal Date") + ": "+ formatter.format(v.getRoadtax_date()));
		    	//lbInsuranceDate.setText(Application.getInstance().getMessage("fms.tran.form.insuranceDate", "Insurance Date") + ": " + formatter.format(v.getIns_date()));
		    	
		    	status = v.getStatus();
		    	
		    	if("2".equals(v.getStatus())){
		    		WriteoffObject w = module.getWriteoff(vehicle_num);
		    		lbWriteoffReason.setText(w.getReason());
		    		lbWriteoffBy.setText(w.getCreatedby_name());
		    		if(!"".equals(w.getFile_name())){
		    			liWriteoffLink.setText(w.getFile_name());
		    			liWriteoffLink.setUrl("/storage"+w.getFile_path());
		    		}else{
		    			liWriteoffLink.setText(" - ");
		    			liWriteoffLink.setUrl("");
		    		}
		    		
		    	}
	    	}else{
	    		sbChannel.setSelectedOption(v.getChannel_id());
	    		sbCategory.setSelectedOption(v.getCategory_id());
	    		rdManagementVehicle.setChecked("M".equals(v.getType()));
	    		rdPoolVehicle.setChecked("P".equals(v.getType()));
	    		tfEngineNo.setValue(v.getEngine_num());
	    		tfChasisNo.setValue(v.getCasis_no());
	    		sbMakeType.setSelectedOption(v.getMaketype_id());
	    		tfModelName.setValue(v.getModel());
	    		tfEngineCapacity.setValue(v.getEngine_cap());
	    		tfColour.setValue(v.getColor());
	    		tfBodyType.setValue(v.getBodytype_name());
	    		tfLocation.setValue(v.getLocation());
	    		tfYear.setValue(v.getYear());
	    		tfNCB.setValue(v.getNcb());
	    		dpfRegistrationDate.setValue(formatter.format(v.getReg_date()));
	    		tfNumOfPassengers.setValue(v.getPassenger_cap());
	    		sbCharge.setSelectedOption(v.getCharge_id());
	    		//tfPerDay.setValue(v.getRental_pd());
	    		//tfPerHour.setValue(v.getRental_ph());
	    		if(v.getMaintain_type() == "K" || "K".equals(v.getMaintain_type())){
	    			rdByKM.setChecked(true);
	    			tfByKM.setValue(v.getBy_km());
	    		}else{
	    			rdByMonth.setChecked(true);
	    			tfByMonth.setValue(v.getBy_month());
	    		}
	    		
	    		//dpfRenewalDate.setValue(formatter.format(v.getRoadtax_date()));
	    		//dpfInsuranceDate.setValue(formatter.format(v.getIns_date()));
	    	}
	    }catch (Exception e) {
	    	Log.getLog(getClass()).error(e.toString());
	    }
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
	    }else if(VEHICLE_ACTION_VIEW.equals(action)){
	    	if (buttonName != null && btnMaintenanceService.getAbsoluteName().equals(buttonName)) {
		    	init();
		    	return new Forward(Form.CANCEL_FORM_ACTION, maintenanceUrl, true);
		    }else if (buttonName != null && btnRoadTax.getAbsoluteName().equals(buttonName)) {
		    	init();
		    	return new Forward(Form.CANCEL_FORM_ACTION, roadTaxUrl, true);
		    }else if (buttonName != null && btnEdit.getAbsoluteName().equals(buttonName)) {
		    	init();
		    	return new Forward(Form.CANCEL_FORM_ACTION, editUrl, true);
	    	}else if (buttonName != null && btnStatus.getAbsoluteName().equals(buttonName)) {
		    	init();
		    	return new Forward(Form.CANCEL_FORM_ACTION, statusUrl, true);
	    	}else{
	    		return result;
	    	}
	    }else {
	    	return result;
	    }
	}
	
	public Forward onValidate(Event event) {

		if (VEHICLE_ACTION_EDIT.equals(action) || VEHICLE_ACTION_ADD.equals(action)) {
			VehicleObject v = new VehicleObject();
			TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
			
			if(VEHICLE_ACTION_EDIT.equals(action)){
				try {
					v = module.getVehicle(vehicle_num);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
			}
			
			if (VEHICLE_ACTION_ADD.equals(action)){
				try {
					v = module.getVehicle(tfVehicleNum.getValue().toString());
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
				if(v.getVehicle_num() != null && !"".equals(v.getVehicle_num()) && !"null".equals(v.getVehicle_num())){
					return new Forward(VEHICLE_ADD_EXIST);
				}
			}
			
			v.setChannel_id(getSelectBoxValue(sbChannel));
			v.setCategory_id(getSelectBoxValue(sbCategory));
			if(rdManagementVehicle.isChecked()){
				v.setType("M");
			}else if(rdPoolVehicle.isChecked()){
				v.setType("P");
			}
			v.setEngine_num(tfEngineNo.getValue().toString());
			v.setCasis_no(tfChasisNo.getValue().toString());
			v.setMaketype_id(getSelectBoxValue(sbMakeType));
			v.setModel(tfModelName.getValue().toString());
			v.setEngine_cap(tfEngineCapacity.getValue().toString());
			v.setColor(tfColour.getValue().toString());
			v.setBodytype_name("");
			v.setLocation(tfLocation.getValue().toString());
			v.setYear(tfYear.getValue().toString());
			v.setNcb(tfNCB.getValue().toString());
			v.setReg_date(dpfRegistrationDate.getDate());
			v.setPassenger_cap(Integer.parseInt(tfNumOfPassengers.getValue().toString()));
			v.setCharge_id(getSelectBoxValue(sbCharge));
			
			if(rdByKM.isChecked()){
				v.setMaintain_type("K");
			}else if(rdByMonth.isChecked()){
				v.setMaintain_type("M");
			}
			v.setBy_km(tfByKM.getValue().toString());
			v.setBy_month(tfByMonth.getValue().toString());
			//v.setRoadtax_date(dpfRenewalDate.getDate());
			//v.setIns_date(dpfInsuranceDate.getDate());
			
			if (VEHICLE_ACTION_ADD.equals(action)) {
				try {
					v.setCreatedby(getWhoModifyId());
					v.setCreatedby_date(new Date());
					v.setVehicle_num(tfVehicleNum.getValue().toString());
					vehicle_num = tfVehicleNum.getValue().toString();
					v.setStatus("1");
					
					module.insertvehicle(v);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(VEHICLE_ADD_FAIL);} 
			}
			if (VEHICLE_ACTION_EDIT.equals(action)) {
				try {
					v.setUpdatedby(getWhoModifyId());
					v.setUpdatedby_date(new Date());
					
					module.updateVehicle(v);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(VEHICLE_ADD_FAIL);
				} 
			}
	    }
	    return new Forward(VEHICLE_ADD_SUCCESS);
	}
	
	public String getDefaultTemplate() {
		return "fms/transport/vehicleForm";
	}

	public String getMaintenanceUrl() {
		return maintenanceUrl;
	}

	public void setMaintenanceUrl(String maintenanceUrl) {
		this.maintenanceUrl = maintenanceUrl;
	}

	public String getRoadTaxUrl() {
		return roadTaxUrl;
	}

	public void setRoadTaxUrl(String roadTaxUrl) {
		this.roadTaxUrl = roadTaxUrl;
	}

	public String getEditUrl() {
		return editUrl;
	}

	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}
	
	private String getSelectBoxValue(SelectBox sb) {
	    if (sb != null) {
	    	Map selected = sb.getSelectedOptions();
	    	if (selected.size() == 1) {
	    		return (String)selected.keySet().iterator().next();
	    	}
	    }
	    return null;
	}
	
}
