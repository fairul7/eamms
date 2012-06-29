package com.tms.hr.orgChart.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.ekms.security.ui.UsersSelectBox;
import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;
import com.tms.hr.orgChart.model.StaffHierachy;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 16, 2006
 * Time: 4:00:31 PM
 */
public class StaffHierachyForm extends Form {
    public static String FORWARD_EXIST = "exist";
    public static String FORWARD_SAVED = "saved";
    public static String FORWARD_UPDATED = "updated";
    public static String FORWARD_RECOVERED = "recovered";
    private String userId = "";
    private SingleUserSelectBox staffName;
    private SelectBox selTitle;
    private SelectBox selDept;
    private SelectBox selStation;
    private SelectBox selCountry;
    
    private CheckBox chkActive;
    private UsersSelectBox communication;
    private UsersSelectBox subordinates;
    private Button btnSave;
    private Button btnCancel;
    private Map countries = new LinkedHashMap();
    private Map titles = new LinkedHashMap();
    private Map stations = new LinkedHashMap();
    private Map depts = new LinkedHashMap();
    private Boolean editMode = new Boolean(false);
    
    private CheckBox chkHod;
    private SelectBox selGender;
    private TextField tfStaffNumber;
    private TextField tfHouseTel;
    private TextField tfOfficeDirectLine;
    private TextField tfOfficeGenLine;
    private TextField tfMobile;
    private TextField tfPassportNumber;
    private DatePopupField dfPassportExpiryDate;
    private TextField tfIcNumber;
    private DatePopupField dfDob;
    private TextBox remarks;
    private TextField tfEmail;
    
    public void init(){
        initForm();
    }
    
    public void initForm() {
    	Application app = Application.getInstance();
        setMethod("POST");
        setWidth("100%");
        setColumns(2);
        staffName = new SingleUserSelectBox("staffName");
        
        staffName.addChild(new ValidatorNotEmpty("vne", app.getMessage("orgChart.general.warn.empty")));
        Label lblName = new Label("lblName", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.hierachy.label.staffName") +" *</span>");
        lblName.setAlign("right");
        addChild(lblName);
        if(userId != null && !"".equals(userId)) {
        	SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
        	String userName = "";
        	try {
        		User user = securityService.getUser(userId);
        		if(user != null) {
        			userName = user.getName();
        		}
        	}
        	catch(SecurityException error) {}
        	addChild(new Label("lblUserName", userName));
        }
        else {
        	addChild(staffName);
        }
        
        selTitle = new SelectBox("selTitle");
        selTitle.addChild(new ValidateSelectBox("vsbTitle", app.getMessage("orgChart.hierachy.warn.selectTitle")));
        Label lblTitle = new Label("lblTitle", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.hierachy.label.staffTitle") + " </span>");
        lblTitle.setAlign("right");
        addChild(lblTitle);
        addChild(selTitle);

        selCountry = new SelectBox("selCountry");
        selCountry.addChild(new ValidateSelectBox("vsbCountry", app.getMessage("orgChart.hierachy.warn.selectCountry")));
        selCountry.setOnChange("javascript:setDeptCountryChange()");
        Label lblCountry = new Label("lblCountry", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.country.label.country")+" </span>");
        lblCountry.setAlign("right");
        addChild(lblCountry);
        addChild(selCountry);
        
        selDept = new SelectBox("selDept");
        selDept.addChild(new ValidateSelectBox("vsbDept", app.getMessage("orgChart.hierachy.warn.selectDept")));
        Label lblDept = new Label("lblDept", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.department.label.department")+" </span>");
        lblDept.setAlign("right");
        addChild(lblDept);
        addChild(selDept);

        selStation = new SelectBox("selStation");
        selStation.addChild(new ValidateSelectBox("vsbStation", app.getMessage("orgChart.hierachy.warn.selectStation")));
        Label lblStation = new Label("lblStation", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.station.label.station")+" </span>");
        lblStation.setAlign("right");
        addChild(lblStation);
        addChild(selStation);
        
        tfStaffNumber = new TextField("tfStaffNumber");
        Label lblStaffNumber = new Label("lblStaffNumber", "<span class=\"fieldTitle\">" + "Staff Number" + "</span>");
        lblStaffNumber.setAlign("right");
        addChild(lblStaffNumber);
        tfStaffNumber.setSize("25");
        addChild(tfStaffNumber);

        tfHouseTel = new TextField("tfHouseTel");
        tfHouseTel.setSize("25");
        Label lblHouseTel = new Label("lblHouseTel", "<span class=\"fieldTitle\">" + "Contact telephone house" + "</span>");
        lblHouseTel.setAlign("right");
        addChild(lblHouseTel);
        addChild(tfHouseTel);

        tfOfficeDirectLine = new TextField("tfOfficeDirectLine");
        tfOfficeDirectLine.setSize("25");
        Label lblOfficeDirectLine = new Label("lblOfficeDirectLine", "<span class=\"fieldTitle\">" + "Contact telephone office <br>(direct line)" + "</span>");
        lblOfficeDirectLine.setAlign("right");
        addChild(lblOfficeDirectLine);
        addChild(tfOfficeDirectLine);
        
        tfOfficeGenLine = new TextField("tfOfficeGenLine");
        tfOfficeGenLine.setSize("25");
        Label lblOfficeGenLine = new Label("lblOfficeGenLine", "<span class=\"fieldTitle\">" + "Contact telephone office <br>(general line & extension)" + "</span>");
        lblOfficeGenLine.setAlign("right");
        addChild(lblOfficeGenLine);
        addChild(tfOfficeGenLine);
        
        tfMobile = new TextField("tfMobile");
        tfMobile.setSize("25");
        Label lblMobile = new Label("lblMobile", "<span class=\"fieldTitle\">" + "Contact telephone handphone" + "</span>");
        lblMobile.setAlign("right");
        addChild(lblMobile);
        addChild(tfMobile);
        
        tfPassportNumber = new TextField("tfPassportNumber");
        tfPassportNumber.setSize("25");
        Label lblPassportNumber = new Label("lblPassportNumber", "<span class=\"fieldTitle\">" + "Passport Number" + "</span>");
        lblPassportNumber.setAlign("right");
        addChild(lblPassportNumber);
        addChild(tfPassportNumber);
        
        dfPassportExpiryDate = new DatePopupField("dfPassportExpiryDate");
        dfPassportExpiryDate.setOptional(true);
        Label lblPassportExpiryDate = new Label("lblPassportExpiryDate", "<span class=\"fieldTitle\">" + "Passport Expiry Date" + "</span>");
        lblPassportExpiryDate.setAlign("right");
        addChild(lblPassportExpiryDate);
        addChild(dfPassportExpiryDate);
        
        tfIcNumber = new TextField("tfIcNumber");
        tfIcNumber.setSize("25");
        Label lblIcNumber = new Label("lblIcNumber", "<span class=\"fieldTitle\">" + "IC Number" + "</span>");
        lblIcNumber.setAlign("right");
        addChild(lblIcNumber);
        addChild(tfIcNumber);
        
        ValidatorEmail vdEml = new ValidatorEmail("vdEml", "Invalid email");
        tfEmail = new TextField("tfEmail");
        tfEmail.addChild(vdEml);
        tfEmail.setSize("50");
        
        Label lblEmail = new Label("lblEmail", "<span class=\"fieldTitle\">" + "Email" + "</span>");
        lblEmail.setAlign("right");
        addChild(lblEmail);
        addChild(tfEmail);
        
        dfDob = new DatePopupField("dfDob");
        dfDob.setOptional(true);
        Label lblDob = new Label("lblDob", "<span class=\"fieldTitle\">" + "Date of Birth" + "</span>");
        lblDob.setAlign("right");
        addChild(lblDob);
        addChild(dfDob);
        
        Map gender = new LinkedHashMap();
        gender.put("m", "Male");
        gender.put("f", "Female");
        selGender = new SelectBox("selGender");
        selGender.setOptionMap(gender);
        Label lblGender = new Label("lblGender", "<span class=\"fieldTitle\">" + "Gender" + "</span>");
        lblGender.setAlign("right");
        addChild(lblGender);
        addChild(selGender);
        
        remarks = new TextBox("remarks");
        remarks.setRows("3");
        remarks.setCols("60");
        Label lblRemarks = new Label("lblRemarks", "<span class=\"fieldTitle\">" + "Remarks" + "</span>");
        lblRemarks.setAlign("right");
        addChild(lblRemarks);
        addChild(remarks);
        
        chkHod = new CheckBox("chkHod");
        Label lblHod = new Label("lblHod", "<span class=\"fieldTitle\">" + "Head Of Department" + "</span>");
        lblHod.setAlign("right");
        addChild(lblHod);
        addChild(chkHod);
        
        chkActive = new CheckBox("chkActive");
        Label lblActive = new Label("lblActive", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.active") + "</span>");
        lblActive.setAlign("right");
        addChild(lblActive);
        addChild(chkActive);

        communication = new UsersSelectBox("communication");
        Label lblCommunicate = new Label("lblCommunicate", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.hierachy.label.communicate") + "</span>");
        lblCommunicate.setAlign("right");
        addChild(lblCommunicate);
        addChild(communication);

        subordinates = new UsersSelectBox("subordinates");
        Label lblSubordinates = new Label("lblSubordinates", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.hierachy.label.subordinates") + "</span>");
        lblSubordinates.setAlign("right");
        addChild(lblSubordinates);
        addChild(subordinates);

        btnSave = new Button("btnSave", app.getMessage("general.label.save","Save"));
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        Panel panel = new Panel("btnPanel");
        panel.setColspan(2);
        panel.setAlign(Panel.ALIGH_MIDDLE);
        panel.addChild(btnSave);
        panel.addChild(btnCancel);
        addChild(panel);

         // init OrgSetup Objects
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            Collection countriesCol = oc.findAllSetup(OrgChartHandler.TYPE_COUNTRY, null, 0, -1, "shortDesc", false, true);
            Collection titlesCol = oc.findAllSetup(OrgChartHandler.TYPE_TITLE, null, 0, -1, "shortDesc", false, true);
            Collection stationsCol = oc.findAllSetup(OrgChartHandler.TYPE_STATION, null, 0, -1, "shortDesc", false, true);
            Collection deptsCol = oc.findAllSetup(OrgChartHandler.TYPE_DEPT, null, 0, -1, "shortDesc", false, true);


            countries.put("---", app.getMessage("general.hierachy.selectCountry"));
            for(Iterator itr = countriesCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                countries.put(obj.getCode(), obj.getShortDesc());
            }

            titles.put("---", app.getMessage("general.hierachy.selectTitle"));
            for(Iterator itr = titlesCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                titles.put(obj.getCode(), obj.getShortDesc());
            }

            stations.put("---", app.getMessage("general.hierachy.selectStation"));
            for(Iterator itr = stationsCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                stations.put(obj.getCode(), obj.getShortDesc());
            }

            depts.put("---", app.getMessage("general.hierachy.selectDept"));
            for(Iterator itr = deptsCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                depts.put(obj.getCode(), obj.getShortDesc());
            }

        selCountry.setOptionMap(countries);
        selDept.setOptionMap(depts);
        selStation.setOptionMap(stations);
        selTitle.setOptionMap(titles);

        staffName.init();
        communication.init();
        subordinates.init();
    }

    public void onRequest(Event evt) {
        if(evt.getParameter("userId") != null && !evt.getParameter("userId").equals("")){
            editMode = Boolean.TRUE;
            removeChildren();
            initForm();
            
            initAllFieldValues(evt.getParameter("userId"));
        }else{ 
        	editMode = Boolean.FALSE;
        	User user = (User)evt.getRequest().getSession().getAttribute("hierachy_user");
        	if(user != null){
        		tfMobile.setValue((String)user.getProperty("telMobile"));
        		tfOfficeGenLine.setValue((String)user.getProperty("telOffice"));
        		tfEmail.setValue((String)user.getProperty("email1"));
        		
        		Application app = Application.getInstance();
    	    	OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
    	    	if(oc.hierachyDeleted(user.getId())) {
    	    		initAllFieldValues(user.getId());
    	    		evt.getRequest().setAttribute("hierachyRecovered", "true");
    	    	}
    	    	
    	    	evt.getRequest().getSession().setAttribute("hierachy_user", null);
        	}
        }
    }
    
    public void initAllFieldValues(String userId) {
    	if(userId != null && !"".equals(userId)) {
	    	Application app = Application.getInstance();
	    	OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
	        StaffHierachy sh = oc.findStaffHierachy(userId);
	        chkHod.setChecked(sh.isHod());
	        selGender.setSelectedOption(sh.getGender());
	        tfStaffNumber.setValue((String)sh.getStaffNumber());
	        tfHouseTel.setValue((String)sh.getContactHouseTelNumber());
	        tfOfficeDirectLine.setValue((String)sh.getContactOfficeDirectLineNumber());
	        tfOfficeGenLine.setValue((String)sh.getContactOfficeGeneralLineNumber());
	        tfMobile.setValue((String)sh.getContactHpNumber());
	        tfPassportNumber.setValue((String)sh.getPassportNumber());
	        
	        if(sh.getPassportExpiryDate() != null)
	        	dfPassportExpiryDate.setDate(sh.getPassportExpiryDate());
	        
	        tfIcNumber.setValue((String)sh.getIcNumber());
	        
	        if(sh.getDateOfBirth() != null)
	        	dfDob.setDate(sh.getDateOfBirth());
	        
	        remarks.setValue((String)sh.getRemarks());
	        tfEmail.setValue((String)sh.getEmail());
	        selCountry.setSelectedOption(sh.getCountryCode());
	        selDept.setSelectedOption(sh.getDeptCode());
	        selStation.setSelectedOption(sh.getStationCode());
	        selTitle.setSelectedOption(sh.getTitleCode());
	        chkActive.setChecked(sh.isActive());
	        if(sh.getSubordinateUsers() != null){
	            subordinates.setOptions(sh.getSubordinateUsers(), "id", "name");
	        }
	        if(sh.getCommunicateUsers() != null){
	            communication.setOptions(sh.getCommunicateUsers(), "id", "name");
	        }
    	}
    }

    public Forward onValidate(Event evt) {
        Application app = Application.getInstance();
        super.onValidate(evt);
        if(findButtonClicked(evt).equals(btnSave.getAbsoluteName())){
            StaffHierachy sh = new StaffHierachy();
            sh.setUserId((userId != null && !"".equals(userId)) ? userId : staffName.getId());
            sh.setActive(chkActive.isChecked());
            
            sh.setHod(chkHod.isChecked());
            List genders = (List)selGender.getValue();
            if(genders != null && genders.size() > 0){
                sh.setGender((String) genders.get(0));
            }
            sh.setStaffNumber(tfStaffNumber.getValue().toString());
            sh.setContactHouseTelNumber(tfHouseTel.getValue().toString());
            sh.setContactOfficeDirectLineNumber(tfOfficeDirectLine.getValue().toString());
            sh.setContactOfficeGeneralLineNumber(tfOfficeGenLine.getValue().toString());
            sh.setContactHpNumber(tfMobile.getValue().toString());
            sh.setPassportNumber(tfPassportNumber.getValue().toString());
            sh.setPassportExpiryDate(dfPassportExpiryDate.getDate());
            sh.setIcNumber(tfIcNumber.getValue().toString());
            sh.setDateOfBirth(dfDob.getDate());
            sh.setRemarks(remarks.getValue().toString());
            sh.setEmail(tfEmail.getValue().toString());

            String[] comIds = communication.getIds();
            for(int i = 0; i < comIds.length; i++){
                sh.addCommunicatesId(comIds[i]);
            }

            String[] subIds = subordinates.getIds();
            for(int i = 0; i < subIds.length; i++){
                sh.addSubordinatesId(subIds[i]);
            }

            List countries = (List) selCountry.getValue();
            if(countries != null && countries.size() > 0){
                sh.setCountryCode((String) countries.get(0));
            }

            List depts = (List) selDept.getValue();
            if(depts != null && depts.size() > 0){
                sh.setDeptCode((String) depts.get(0));
            }

            List stations = (List) selStation.getValue();
            if(stations != null && stations.size() > 0){
                sh.setStationCode((String) stations.get(0));
            }

            List titles = (List) selTitle.getValue();
            if(titles != null && titles.size() > 0){
                sh.setTitleCode((String) titles.get(0));
            }

            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            // if is not edit
            if(!editMode.booleanValue()){
                if(!oc.hierachyExist(sh.getUserId())){
                	if(!oc.hierachyDeleted(sh.getUserId())) {
	                    oc.saveHierachy(sh);
	                    return new Forward(FORWARD_SAVED);
                	}
                	else {
                		oc.undeleteHierachy(sh.getUserId());
                		oc.saveHierachy(sh);
                		return new Forward(FORWARD_RECOVERED);
                	}
                }else return new Forward(FORWARD_EXIST);
            }else{
                // if edit
                oc.saveHierachy(sh);
                return new Forward(FORWARD_UPDATED);
            }
        }
        return new Forward("error");
    }

    public class ValidateSelectBox extends Validator{
        public ValidateSelectBox(String name, String text){
            super(name);
            setText(text);
        }

        public boolean validate(FormField formField) {
            List list = (List) formField.getValue();
            String s = (String) list.get(0);
            if(s.startsWith("---")){
                return false;
            }
            return true;
        }
    }

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public CheckBox getChkActive() {
		return chkActive;
	}

	public UsersSelectBox getCommunication() {
		return communication;
	}

	public Map getCountries() {
		return countries;
	}

	public Map getDepts() {
		return depts;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public SelectBox getSelCountry() {
		return selCountry;
	}

	public SelectBox getSelDept() {
		return selDept;
	}

	public SelectBox getSelStation() {
		return selStation;
	}

	public SelectBox getSelTitle() {
		return selTitle;
	}

	public SingleUserSelectBox getStaffName() {
		return staffName;
	}

	public Map getStations() {
		return stations;
	}

	public UsersSelectBox getSubordinates() {
		return subordinates;
	}

	public Map getTitles() {
		return titles;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}   
}