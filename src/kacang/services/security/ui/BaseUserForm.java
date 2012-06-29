package kacang.services.security.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.tms.fms.department.model.FMSDepartmentDao;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.register.model.FMSRegisterManager;

import kacang.model.DaoException;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Encryption;
import kacang.util.Log;
import kacang.Application;

public abstract class BaseUserForm extends Form {
    public static final String DEFAULT_TEMPLATE = "security/userForm";

    protected TextField firstName;
    protected TextField lastName;
    protected TextField email;
    //Contact Information
    protected TextBox address;
    protected TextField postcode;
    protected TextField city;
    protected TextField stateAddress;
    protected CountrySelectBox country;
    protected TextField telOffice;
    protected TextField telHome;
    protected TextField telMobile;
    protected TextField fax;
    //Authentication Information
    protected TextField username;
    protected Password password;
    protected Password confirmPassword;
    protected ComboSelectBox groups;
    protected CheckBox active;
    //Validators
    protected ValidatorNotEmpty validUsername;
    protected ValidatorNotEmpty validFirstName;
    protected ValidatorNotEmpty validLastName;
    protected ValidatorNotEmpty validEmailNotEmpty;
    protected ValidatorEmail validEmail;

    protected Button userButton;
    protected Button cancelButton;
    protected String id;
    protected boolean state = false;
    protected String message;
    
    ////add for fms department
    protected SelectBox department;
    protected SelectBox unit;
    protected String unitId;
    protected TextField staffID;
    

    public BaseUserForm() {
    }

    public BaseUserForm(String name) {
        super(name);
    }

    public void init() {
        setMethod("post");

        Application application = Application.getInstance();

        firstName = new TextField("firstName");
        firstName.setSize("20");
        lastName = new TextField("lastName");
        lastName.setSize("20");
        email = new TextField("title");
        email.setSize("35");
        address = new TextBox("address");
        address.setCols("35");
        address.setRows("7");
        postcode = new TextField("postcode");
        postcode.setSize("5");
        city = new TextField("city");
        city.setSize("20");
        stateAddress = new TextField("stateAddress");
        stateAddress.setSize("15");
        country = new CountrySelectBox("country");
        //country.setSize("15");
        telOffice = new TextField("telOffice");
        telOffice.setSize("15");
        telHome = new TextField("telHome");
        telHome.setSize("15");
        telMobile = new TextField("telMobile");
        telMobile.setSize("15");
        staffID = new TextField("staffID");
        staffID.setSize("15");
        fax = new TextField("telFax");
        fax.setSize("15");
        username = new TextField("username");
        username.setSize("15");
        password = new Password("password");
        password.setSize("15");
        confirmPassword = new Password("confirmPassword");
        confirmPassword.setSize("15");
        groups = new ComboSelectBox("groups");
        active = new CheckBox("active");
        userButton = new Button("userButton");
        userButton.setText(application.getMessage("security.label.save", "Save"));
        cancelButton = new Button("cancelButton");
        cancelButton.setText(application.getMessage("security.label.cancel", "Cancel"));

        validUsername = new ValidatorNotEmpty("validUsername");
        validFirstName = new ValidatorNotEmpty("validFirstName");
        validLastName = new ValidatorNotEmpty("validLastName");
        validEmailNotEmpty = new ValidatorNotEmpty("validEmailNotEmpty");
        validEmail = new ValidatorEmail("validEmail");

        firstName.addChild(validFirstName);
        lastName.addChild(validLastName);
        username.addChild(validUsername);
        email.addChild(validEmailNotEmpty);
        email.addChild(validEmail);
        
        
        department = new SelectBox("selecttype");
        
		Map cmap = null;
        try {
            cmap = ((FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class)).getFMSDepartments();
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        department.addOption("-1", "-- Please Select --");
        for (Iterator i = cmap.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            department.addOption(key, (String) cmap.get(key));            
        }
        department.setOnChange("setDepartmentChange()");
        
       
        unit = new SelectBox("unit");
        try {
	    	FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
			Collection lstUnit = dao.selectUnit();
			unit.addOption("-1", "-- Please Select --");
//		    if (lstUnit.size() > 0) {
//		    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
//		        	FMSUnit o = (FMSUnit)i.next();
//		        	unit.addOption(o.getId(),o.getName());
//		        }
//		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}

		try {
			FMSRegisterManager FM = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
	   		User user = FM.getUser(id);
	   		
	   		unitId = (String) user.getProperty("unit");
		} catch (Exception e) {}
		
        addChild(firstName);
        addChild(lastName);
        addChild(email);
        addChild(address);
        addChild(postcode);
        addChild(city);
        addChild(stateAddress);
        addChild(country);
        addChild(telOffice);
        addChild(telHome);
        addChild(telMobile);
        addChild(staffID);
        addChild(fax);
        addChild(username);
        addChild(password);
        addChild(confirmPassword);
        addChild(groups);
        addChild(active);
        addChild(userButton);
        addChild(cancelButton);
        addChild(department);
        addChild(unit);

        groups.init();
        initGroups();
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public abstract void initGroups();

    public void onRequest(Event evt) {
        initGroups();
    }

    public Forward onSubmit(Event evt) {
    	          
        Forward forward = new Forward();
        if (cancelButton.getAbsoluteName().equals(findButtonClicked(evt)))
            setState(true);
        else 
	        forward = super.onSubmit(evt);
        
	        return forward;
        
    }

    public TextField getFirstName() {
        return firstName;
    }

    public void setFirstName(TextField firstName) {
        this.firstName = firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public void setLastName(TextField lastName) {
        this.lastName = lastName;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public TextBox getAddress() {
        return address;
    }

    public void setAddress(TextBox address) {
        this.address = address;
    }

    public TextField getPostcode() {
        return postcode;
    }

    public void setPostcode(TextField postcode) {
        this.postcode = postcode;
    }

    public TextField getCity() {
        return city;
    }

    public void setCity(TextField city) {
        this.city = city;
    }

    public TextField getStateAddress() {
        return stateAddress;
    }

    public void setStateAddress(TextField stateAddress) {
        this.stateAddress = stateAddress;
    }

    public CountrySelectBox getCountry() {
        return country;
    }

    public void setCountry(CountrySelectBox country) {
        this.country = country;
    }

    public TextField getTelOffice() {
        return telOffice;
    }

    public void setTelOffice(TextField telOffice) {
        this.telOffice = telOffice;
    }

    public TextField getTelHome() {
        return telHome;
    }

    public void setTelHome(TextField telHome) {
        this.telHome = telHome;
    }

    public TextField getTelMobile() {
        return telMobile;
    }

    public void setTelMobile(TextField telMobile) {
        this.telMobile = telMobile;
    }

    public TextField getFax() {
        return fax;
    }

    public void setFax(TextField fax) {
        this.fax = fax;
    }

    public TextField getUsername() {
        return username;
    }

    public void setUsername(TextField username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Password getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(Password confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public ComboSelectBox getGroups() {
        return groups;
    }

    public void setGroups(ComboSelectBox groups) {
        this.groups = groups;
    }

    public Button getUserButton() {
        return userButton;
    }

    public void setUserButton(Button userButton) {
        this.userButton = userButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public CheckBox getActive() {
        return active;
    }

    public void setActive(CheckBox active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected User buildUser(String id) {
        User user = new User();

        
        user.setId(id);
        user.setUsername((String) username.getValue());
        user.setPassword(Encryption.encrypt((String) password.getValue()));
        user.setProperty("firstName", firstName.getValue());
        user.setProperty("lastName", lastName.getValue());
        user.setProperty("email1", email.getValue());
        user.setProperty("address", address.getValue());
        user.setProperty("postcode", postcode.getValue());
        user.setProperty("city", city.getValue());
        user.setProperty("state", stateAddress.getValue());
        user.setProperty("country", country.getValue());
        user.setProperty("telOffice", telOffice.getValue());
        user.setProperty("telHome", telHome.getValue());
        user.setProperty("telMobile", telMobile.getValue());
        user.setProperty("fax", fax.getValue());
       /* if("-1".equals(department.getSelectedOptions().keySet().iterator().next())){
        	department.setInvalid(true);
        	setInvalid(true);
        }*/
        user.setProperty("department", (String)department.getSelectedOptions().keySet().iterator().next());
        user.setProperty("unit", (String)unit.getSelectedOptions().keySet().iterator().next());
        user.setProperty("property1", staffID.getValue());
        
        if (active.isChecked())
            user.setProperty("active", "1");
        else
            user.setProperty("active", "0");

        return user;
    }

	public SelectBox getDepartment() {
		return department;
	}

	public void setDepartment(SelectBox department) {
		this.department = department;
	}

	public SelectBox getUnit() {
		return unit;
	}

	public void setUnit(SelectBox unit) {
		this.unit = unit;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public TextField getStaffID() {
		return staffID;
	}

	public void setStaffID(TextField staffID) {
		this.staffID = staffID;
	}
	
	
}
