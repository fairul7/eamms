package com.tms.hr.orgChart.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.validator.Validator;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;
import com.tms.hr.orgChart.model.StaffHierachy;

public class SearchHierachyForm extends Form {
    private String userId = "";
    private Label selTitle;
    private Label selDept;
    private Label selStation;
    private Label selCountry;
    
    /*private CheckBox chkActive;
    private UsersSelectBox communication;
    private UsersSelectBox subordinates;
    private Button btnSave;*/
    private Button btnCancel;
    private Collection countries;
    private Collection titles;
    private Collection stations;
    private Collection depts;
    /*private Boolean editMode = new Boolean(false);
    
    private CheckBox chkHod;*/
    private Label selGender;
    private Label tfStaffNumber;
    private Label tfHouseTel;
    private Label tfOfficeDirectLine;
    private Label tfOfficeGenLine;
    private Label tfMobile;
    private Label tfPassportNumber;
    private Label dfPassportExpiryDate;
    private Label tfIcNumber;
    private Label dfDob;
    private Label remarks;
    private Label tfEmail;
    
    public void init(){
        initForm();
    }
    
    public void initForm() {
    	Application app = Application.getInstance();
        setMethod("POST");
        setWidth("100%");
        setColumns(2);
        /*staffName = new SingleUserSelectBox("staffName");
        
        staffName.addChild(new ValidatorNotEmpty("vne", app.getMessage("orgChart.general.warn.empty")));*/
        Label lblName = new Label("lblName", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.hierachy.label.staffName") +" </span>");
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
        	Label user = new Label("lblUserName", userName);  
      
        	addChild(user);
        }
       /* else {
        	addChild(staffName);
        }*/
        
        selTitle = new Label("selTitle");    
        Label lblTitle = new Label("lblTitle", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.hierachy.label.staffTitle") + " </span>");
        lblTitle.setAlign("right");
        addChild(lblTitle);
        addChild(selTitle);

        selCountry = new Label("selCountry");
        Label lblCountry = new Label("lblCountry", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.country.label.country")+" </span>");
        lblCountry.setAlign("right");
        addChild(lblCountry);
        addChild(selCountry);
        
        selDept = new Label("selDept");
        Label lblDept = new Label("lblDept", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.department.label.department")+" </span>");
        lblDept.setAlign("right");
        addChild(lblDept);
        addChild(selDept);

        selStation = new Label("selStation");
        Label lblStation = new Label("lblStation", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.station.label.station")+" </span>");
        lblStation.setAlign("right");
        addChild(lblStation);
        addChild(selStation);
        
        tfStaffNumber = new Label("tfStaffNumber");
        Label lblStaffNumber = new Label("lblStaffNumber", "<span class=\"fieldTitle\">" + "Staff Number" + "</span>");
        lblStaffNumber.setAlign("right");
        addChild(lblStaffNumber);
        /*tfStaffNumber.setSize("25");*/
        addChild(tfStaffNumber);
        
        tfHouseTel = new Label("tfHouseTel");
        /*tfHouseTel.setSize("25");*/
        Label lblHouseTel = new Label("lblHouseTel", "<span class=\"fieldTitle\">" + "Contact telephone house" + "</span>");
        lblHouseTel.setAlign("right");
        addChild(lblHouseTel);
        addChild(tfHouseTel);

        tfOfficeDirectLine = new Label("tfOfficeDirectLine");
        /*tfOfficeDirectLine.setSize("25");*/
        Label lblOfficeDirectLine = new Label("lblOfficeDirectLine", "<span class=\"fieldTitle\">" + "Contact telephone office <br>(direct line)" + "</span>");
        lblOfficeDirectLine.setAlign("right");
        addChild(lblOfficeDirectLine);
        addChild(tfOfficeDirectLine);
        
        tfOfficeGenLine = new Label("tfOfficeGenLine");
        /*tfOfficeGenLine.setSize("25");*/
        Label lblOfficeGenLine = new Label("lblOfficeGenLine", "<span class=\"fieldTitle\">" + "Contact telephone office <br>(general line & extension)" + "</span>");
        lblOfficeGenLine.setAlign("right");
        addChild(lblOfficeGenLine);
        addChild(tfOfficeGenLine);
        
        tfMobile = new Label("tfMobile");
        /*tfMobile.setSize("25");*/
        Label lblMobile = new Label("lblMobile", "<span class=\"fieldTitle\">" + "Contact telephone handphone" + "</span>");
        lblMobile.setAlign("right");
        addChild(lblMobile);
        addChild(tfMobile);
        
        tfPassportNumber = new Label("tfPassportNumber");
        /*tfPassportNumber.setSize("25");*/
        Label lblPassportNumber = new Label("lblPassportNumber", "<span class=\"fieldTitle\">" + "Passport Number" + "</span>");
        lblPassportNumber.setAlign("right");
        addChild(lblPassportNumber);
        addChild(tfPassportNumber);
        
        dfPassportExpiryDate = new Label("dfPassportExpiryDate");
        /*dfPassportExpiryDate.setOptional(true);*/
        Label lblPassportExpiryDate = new Label("lblPassportExpiryDate", "<span class=\"fieldTitle\">" + "Passport Expiry Date" + "</span>");
        lblPassportExpiryDate.setAlign("right");
        addChild(lblPassportExpiryDate);
        addChild(dfPassportExpiryDate);
        
        tfIcNumber = new Label("tfIcNumber");
        /*tfIcNumber.setSize("25");*/
        Label lblIcNumber = new Label("lblIcNumber", "<span class=\"fieldTitle\">" + "IC Number" + "</span>");
        lblIcNumber.setAlign("right");
        addChild(lblIcNumber);
        addChild(tfIcNumber);
        
        /*ValidatorEmail vdEml = new ValidatorEmail("vdEml", "Invalid email");
        */tfEmail = new Label("tfEmail");
        /*tfEmail.addChild(vdEml);
        tfEmail.setSize("50");*/
        Label lblEmail = new Label("lblEmail", "<span class=\"fieldTitle\">" + "Email" + "</span>");
        lblEmail.setAlign("right");
        addChild(lblEmail);
        addChild(tfEmail);
        
        dfDob = new Label("dfDob");
        /*dfDob.setOptional(true);*/
        Label lblDob = new Label("lblDob", "<span class=\"fieldTitle\">" + "Date of Birth" + "</span>");
        lblDob.setAlign("right");
        addChild(lblDob);
        addChild(dfDob);
        
        /*Map gender = new LinkedHashMap();
        gender.put("m", "Male");
        gender.put("f", "Female");*/
        selGender = new Label("selGender");
        /*selGender.setOptionMap(gender);*/
        Label lblGender = new Label("lblGender", "<span class=\"fieldTitle\">" + "Gender" + "</span>");
        lblGender.setAlign("right");
        addChild(lblGender);
        addChild(selGender);
        
        remarks = new Label("remarks");
        /*remarks.setRows("3");
        remarks.setCols("60");*/
        Label lblRemarks = new Label("lblRemarks", "<span class=\"fieldTitle\">" + "Remarks" + "</span>");
        lblRemarks.setAlign("right");
        addChild(lblRemarks);
        addChild(remarks);
        
        /*chkHod = new CheckBox("chkHod");
        addChild(new Label("lblHod", "<span class=\"fieldTitle\">" + "Head Of Department" + "</span>", "right"));
        addChild(chkHod);*/
        
        /*chkActive = new CheckBox("chkActive");
        addChild(new Label("lblActive", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.active") + "</span>", "right"));
        addChild(chkActive);*/

        /*communication = new UsersSelectBox("communication");
        addChild(new Label("lblCommunicate", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.hierachy.label.communicate") + "</span>", "right"));
        addChild(communication);

        subordinates = new UsersSelectBox("subordinates");
        addChild(new Label("lblSubordinates", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.hierachy.label.subordinates") + "</span>", "right"));
        addChild(subordinates);

        btnSave = new Button("btnSave", app.getMessage("general.label.save","Save"));*/
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        Panel panel = new Panel("btnPanel");
        panel.setColspan(2);
        panel.setAlign(Panel.ALIGH_MIDDLE);
        /*panel.addChild(btnSave);*/
        panel.addChild(btnCancel);
        addChild(panel);

         // init OrgSetup Objects
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            countries = oc.findAllSetup(OrgChartHandler.TYPE_COUNTRY, null, 0, -1, "shortDesc", false, true);
            titles = oc.findAllSetup(OrgChartHandler.TYPE_TITLE, null, 0, -1, "shortDesc", false, true);
            stations = oc.findAllSetup(OrgChartHandler.TYPE_STATION, null, 0, -1, "shortDesc", false, true);
            depts = oc.findAllSetup(OrgChartHandler.TYPE_DEPT, null, 0, -1, "shortDesc", false, true);

            
       /* selCountry.setOptionMap(countries);
        selDept.setOptionMap(depts);
        selStation.setOptionMap(stations);
        selTitle.setOptionMap(titles);

        staffName.init();
        communication.init();
        subordinates.init();*/
    }

    public void onRequest(Event evt) {
        if(evt.getParameter("userId") != null && !evt.getParameter("userId").equals("")){
            /*editMode = Boolean.TRUE;*/
            removeChildren();
            initForm();
            
            initAllFieldValues(evt.getParameter("userId"));
        }else{ 
        	/*editMode = Boolean.FALSE;
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
        	}*/
        }
    }
    
    public void initAllFieldValues(String userId) {
    	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
    	if(userId != null && !"".equals(userId)) {
	    	Application app = Application.getInstance();
	    	OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
	        StaffHierachy sh = oc.findStaffHierachy(userId);
	        /*chkHod.setChecked(sh.isHod());*/
	        if("m".equals(sh.getGender()))
	        	selGender.setText("Male");
	        else if("f".equals(sh.getGender()))
	        	selGender.setText("Female");
	        
	        tfStaffNumber.setText((String)sh.getStaffNumber());
	        tfHouseTel.setText((String)sh.getContactHouseTelNumber());
	        tfOfficeDirectLine.setText((String)sh.getContactOfficeDirectLineNumber());
	        tfOfficeGenLine.setText((String)sh.getContactOfficeGeneralLineNumber());
	        tfMobile.setText((String)sh.getContactHpNumber());
	        tfPassportNumber.setText((String)sh.getPassportNumber());
	        
	        if(sh.getPassportExpiryDate() != null)
	        	dfPassportExpiryDate.setText(sdf.format(sh.getPassportExpiryDate()));
	        
	        tfIcNumber.setText((String)sh.getIcNumber());
	        
	        if(sh.getDateOfBirth() != null)
	        	dfDob.setText(sdf.format(sh.getDateOfBirth()));
	        
	        remarks.setText((String)sh.getRemarks());
	        tfEmail.setText((String)sh.getEmail());

            for(Iterator itr = countries.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                if(sh.getCountryCode().equals(obj.getCode()))
                	selCountry.setText(obj.getShortDesc());
            }
     
            for(Iterator itr = titles.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                if(sh.getTitleCode().equals(obj.getCode()))
                	selTitle.setText(obj.getShortDesc());
            }

            for(Iterator itr = stations.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                if(sh.getStationCode().equals(obj.getCode()))
                	selStation.setText(obj.getShortDesc());
            }

            for(Iterator itr = depts.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                if(sh.getDeptCode().equals(obj.getCode()))
                	selDept.setText(obj.getShortDesc());
            }
	        
	        /*chkActive.setChecked(sh.isActive());
	        if(sh.getSubordinateUsers() != null){
	            subordinates.setOptions(sh.getSubordinateUsers(), "id", "name");
	        }
	        if(sh.getCommunicateUsers() != null){
	            communication.setOptions(sh.getCommunicateUsers(), "id", "name");
	        }*/
    	}
    }

    public Forward onValidate(Event evt) {
        Application app = Application.getInstance();
        super.onValidate(evt);
        
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Collection getCountries() {
		return countries;
	}

	public void setCountries(Collection countries) {
		this.countries = countries;
	}

	public Collection getDepts() {
		return depts;
	}

	public void setDepts(Collection depts) {
		this.depts = depts;
	}

	public Collection getStations() {
		return stations;
	}

	public void setStations(Collection stations) {
		this.stations = stations;
	}

	public Collection getTitles() {
		return titles;
	}

	public void setTitles(Collection titles) {
		this.titles = titles;
	}


	
    
}
