package kacang.services.security.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorEmail;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.services.security.User;
import kacang.services.security.Profileable;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.util.Encryption;
import java.util.*;


public class Profile extends Form
{
    public static final String DEFAULT_TEMPLATE = "security/profileForm";
    public static final String PROPERTY_LABEL = "kacang.services.security.profilers";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILURE = "fail";

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextBox address;
    private TextField postcode;
    private TextField city;
    private TextField state;
    private CountrySelectBox country;
    private TextField telOffice;
    private TextField telHome;
    private TextField telMobile;
    private TextField telFax;
    private TextField username;
    private Button update;
    private Button cancel;

    private Password currentPassword;
    private Password newPassword;
    private Password confirmPassword;

    private ValidatorNotEmpty validFirstName;
    private ValidatorNotEmpty validLastName;
    private ValidatorNotEmpty validUsername;
    private ValidatorNotEmpty validEmailEmpty;
    private ValidatorEmail validEmail;

    private Map messages;
    private Collection profilers;
    
    private TextField staffID;
    /*protected SelectBox sbDepartment;
    protected SelectBox sbUnit;*/
    
    
    public Profile()
    {
    }

    public Profile(String s)
    {
        super(s);
    }

    public void init()
    {
        setMethod("POST");

        /*try {
	    	FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
			Collection lstUnit = dao.selectUnit();
			//unit.addOption("-1", "-- Please Select --");			
		    if (lstUnit.size() > 0) {
		    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
		        	FMSUnit o = (FMSUnit)i.next();
		        	//unit.addOption(o.getId(),o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}*/
		
		message = "";

        firstName = new TextField("firstName");
        firstName.setSize("20");
        lastName = new TextField("lastName");
        lastName.setSize("20");
        email = new TextField("email");
        email.setSize("35");
        address = new TextBox("address");
        postcode = new TextField("postcode");
        postcode.setSize("5");
        city = new TextField("city");
        city.setSize("10");
        state = new TextField("state");
        state.setSize("10");
        country = new CountrySelectBox("country");
        telOffice = new TextField("telOffice");
        telOffice.setSize("20");
        telHome = new TextField("telHome");
        telHome.setSize("20");
        telMobile = new TextField("telMobile");
        telMobile.setSize("20");
        telFax = new TextField("fax");
        telFax.setSize("20");
        username = new TextField("username");
        username.setSize("20");
        currentPassword = new Password("currentPassword");
        newPassword = new Password("newPassword");
        confirmPassword = new Password("confirmPassword");
        update = new Button("update");
        String updateLabel = Application.getInstance().getMessage("profile.label.update","Update");
        String cancelLabel = Application.getInstance().getMessage("profile.label.cancel","Cancel");
        update.setText(updateLabel);
        cancel = new Button("cancel");
        cancel.setText(cancelLabel);

        validFirstName = new ValidatorNotEmpty("validFirstName");
        validFirstName.setText(Application.getInstance().getMessage("security.label.specifyFirstName","Please Specify Your First Name"));
        firstName.addChild(validFirstName);
        validLastName = new ValidatorNotEmpty("validLastName");
        validLastName.setText(Application.getInstance().getMessage("security.label.specifyLastName","Please Specify Your Last Name"));
        lastName.addChild(validLastName);
        validUsername = new ValidatorNotEmpty("validUsername");
        validUsername.setText(Application.getInstance().getMessage("security.label.specifyUsername","Please Specify Your Username"));
        username.addChild(validUsername);
        validEmailEmpty = new ValidatorNotEmpty("validEmailEmpty");
        validEmailEmpty.setText(Application.getInstance().getMessage("security.label.specifyEmail","Please Specify Your Email Address"));
        validEmail = new ValidatorEmail("validEmail");
        validEmail.setText(Application.getInstance().getMessage("security.label.invalidEmail","Invalid Email"));
        email.addChild(validEmailEmpty);
        email.addChild(validEmail);
        staffID = new TextField("staffID");
        staffID.setSize("20");
        
      ////
        /*String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
        String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
        
        sbDepartment = new SelectBox("sbDepartment");
	    sbDepartment.setOptions(initialSelect);
	    try {
			FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
			Collection lstDepartment = dao.selectDepartment();
		    if (lstDepartment.size() > 0) {
		    	for (Iterator i=lstDepartment.iterator(); i.hasNext();) {
		        	FMSDepartment o = (FMSDepartment)i.next();
		        	sbDepartment.setOptions(o.getId()+"="+o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		sbDepartment.addChild(new ValidatorSelectBoxNotEmpty("sbDepartmentNotEmpty", msgNotEmpty));
		sbDepartment.setOnChange("setDepartmentChange()");
	    addChild(sbDepartment);
	    
	    sbUnit = new SelectBox("sbUnit");
	    sbUnit.setOptions(initialSelect);
	    sbUnit.addChild(new ValidatorSelectBoxNotEmpty("sbUnitNotEmpty", msgNotEmpty));
	    addChild(sbUnit);*/
        ////

        addChild(firstName);
        addChild(lastName);
        addChild(email);
        addChild(address);
        addChild(postcode);
        addChild(city);
        addChild(state);
        addChild(country);
        addChild(telOffice);
        addChild(telHome);
        addChild(telMobile);
        addChild(telFax);
        addChild(username);
        addChild(newPassword);
        addChild(currentPassword);
        addChild(confirmPassword);
        addChild(update);
        addChild(cancel);
        addChild(staffID);
        initProfilers();

        refresh();
    }

    private void initProfilers()
    {
        String profiles = Application.getInstance().getProperty(PROPERTY_LABEL);
        profilers = new ArrayList();
        if(!(profiles == null || "".equals(profiles)))
        {
            StringTokenizer tokenizer = new StringTokenizer(profiles, ",");
            while(tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken();
                try
                {
                    Profileable profile = (Profileable) Class.forName(token).newInstance();
                    profile.init(getWidgetManager().getUser());
                    Widget widget = profile.getWidget();
                    widget.setName(profile.getProfileableName());
                    addChild(widget);
                    widget.init();
                    profilers.add(profile);

                }
                catch(Exception e)
                {
                    Log.getLog(Profile.class).error(e);
                }
            }
        }
    }

    private void refresh()
    {
    	User user = getWidgetManager().getUser();
        if(user != null)
        {
            messages = new HashMap();
            this.setInvalid(false);

            firstName.setValue(user.getProperty("firstName"));
            firstName.setInvalid(false);
            lastName.setValue(user.getProperty("lastName"));
            lastName.setInvalid(false);
            email.setValue(user.getProperty("email1"));
            email.setInvalid(false);
            address.setValue(user.getProperty("address"));
            address.setInvalid(false);
            postcode.setValue(user.getProperty("postcode"));
            postcode.setInvalid(false);
            city.setValue(user.getProperty("city"));
            city.setInvalid(false);
            state.setValue(user.getProperty("state"));
            state.setInvalid(false);
            country.setValue(user.getProperty("country"));
            country.setInvalid(false);
            telOffice.setValue(user.getProperty("telOffice"));
            telOffice.setInvalid(false);
            telHome.setValue(user.getProperty("telHome"));
            telHome.setInvalid(false);
            telMobile.setValue(user.getProperty("telMobile"));
            telMobile.setInvalid(false);
            telFax.setValue(user.getProperty("fax"));
            telFax.setInvalid(false);
            username.setValue(user.getUsername());
            username.setInvalid(false);
            staffID.setValue(user.getProperty("property1"));
            staffID.setInvalid(false);
            /*sbDepartment.setSelectedOption((String)user.getProperty("department"));
            sbDepartment.setInvalid(false);
            sbUnit.setSelectedOption((String)user.getProperty("unit"));
            sbUnit.setInvalid(false);*/
        }
    }

    public void onRequest(Event event)
    {
        refresh();
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = null;
        messages = new HashMap();
        if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
            forward = new Forward(FORWARD_CANCEL);
        else
        {
            forward = super.onSubmit(event);
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = service.getCurrentUser(event.getRequest());
            //Validating email uniqueness
            if(!(email.getValue() == null || "".equals(email.getValue())))
            {
                try
                {
                    DaoQuery query = new DaoQuery();
                    query.addProperty(new OperatorEquals("email1", email.getValue(), DaoOperator.OPERATOR_AND));
                    query.addProperty(new OperatorEquals("id", user.getId(), DaoOperator.OPERATOR_NAN));
                    Collection list = service.getUsers(query, 0, 1, null, false);
                    if(list.size() > 0)
                    {
                        email.setInvalid(true);
                        this.setInvalid(true);
                        messages.put("email", Application.getInstance().getMessage("security.label.emailInUse","Email Already In Use"));
                    }
                }
                catch (SecurityException e)
                {
                    Log.getLog(Profile.class).error(e);
                }
            }
            //Validating username uniqueness
            if(!(username.getValue() == null || "".equals(username.getValue())))
            {
                try
                {
                    DaoQuery query = new DaoQuery();
                    query.addProperty(new OperatorEquals("username", username.getValue(), DaoOperator.OPERATOR_AND));
                    query.addProperty(new OperatorEquals("id", user.getId(), DaoOperator.OPERATOR_NAN));
                    Collection list = service.getUsers(query, 0, 1, null, false);
                    if(list.size() > 0)
                    {
                        username.setInvalid(true);
                        this.setInvalid(true);
                        messages.put("username", Application.getInstance().getMessage("security.label.usernameInUse","Username Already In Use"));
                    }
                }
                catch(Exception e)
                {
                    Log.getLog(Profile.class).error(e);
                }
            }
            //Verifying passwords
            if ((currentPassword.getValue() != null && !"".equals(currentPassword.getValue()))
            ||
            (newPassword.getValue() != null && !"".equals(newPassword.getValue()))
            ||
            (confirmPassword.getValue() != null && !"".equals(confirmPassword.getValue()))
            ) {
                String password = Encryption.encrypt((String) currentPassword.getValue());
                if(!(password.equals(user.getPassword())))
                {
                    currentPassword.setInvalid(true);
                    this.setInvalid(true);
                    messages.put("currentPassword", Application.getInstance().getMessage("security.label.incorrectPassword","Incorrect Password"));
                }
            }
            if(!((newPassword.getValue() == null || "".equals(newPassword.getValue())) || (confirmPassword.getValue() == null || "".equals(confirmPassword.getValue()))))
            {
                if(!(newPassword.getValue().equals(confirmPassword.getValue())))
                {
                    newPassword.setInvalid(true);
                    confirmPassword.setInvalid(true);
                    this.setInvalid(true);
                    messages.put("newPassword", Application.getInstance().getMessage("security.label.newConfirmationNoMatch","New And Confirmation Passwords Do Not Match"));
                    messages.put("confirmPassword", Application.getInstance().getMessage("security.label.newConfirmationNoMatch","New And Confirmation Passwords Do Not Match"));
                }
            }
        }
        return forward;
    }

    public Forward onValidate(Event event)
    {
        Forward forward = null;
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = service.getCurrentUser(event.getRequest());
        user.setProperty("firstName", firstName.getValue());
        user.setProperty("lastName", lastName.getValue());
        user.setProperty("email1", email.getValue());
        user.setProperty("address", address.getValue());
        user.setProperty("postcode", postcode.getValue());
        user.setProperty("city", city.getValue());
        user.setProperty("state", state.getValue());
        user.setProperty("country", country.getValue());
        user.setProperty("telOffice", telOffice.getValue());
        user.setProperty("telHome", telHome.getValue());
        user.setProperty("telMobile", telMobile.getValue());
        user.setProperty("fax", telFax.getValue());
        user.setProperty("property1", staffID.getValue());
        /*user.setProperty("department", getSelectBoxValue(sbDepartment));
        user.setProperty("unit", getSelectBoxValue(sbUnit));*/
        user.setUsername((String) username.getValue());
        if(!((currentPassword.getValue() == null || "".equals(currentPassword.getValue())) || (newPassword.getValue() == null || "".equals(newPassword.getValue())) || (confirmPassword.getValue() == null || "".equals(confirmPassword.getValue()))))
        {
            user.setPassword(Encryption.encrypt((String) newPassword.getValue()));
            user.setProperty("weakpass", Encryption.encrypt((String) newPassword.getValue(), user.getId()));
        }
        try
        {
            service.updateUser(user);
            service.loginWithEncryptedPassword(event.getRequest(), user.getUsername(), user.getPassword());
            forward = new Forward(FORWARD_SUCCESS);
            //Invoking process() method for profilers
            for(Iterator i = profilers.iterator(); i.hasNext();)
            {
                Profileable profiler = (Profileable) i.next();
                profiler.process(user);
            }
        }
        catch(Exception e)
        {
            forward = new Forward(FORWARD_FAILURE);
        }
        return forward;
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
    
    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    //Getters and Setters
    public TextField getFirstName()
    {
        return firstName;
    }

    public void setFirstName(TextField firstName)
    {
        this.firstName = firstName;
    }

    public TextField getLastName()
    {
        return lastName;
    }

    public void setLastName(TextField lastName)
    {
        this.lastName = lastName;
    }

    public TextField getEmail()
    {
        return email;
    }

    public void setEmail(TextField email)
    {
        this.email = email;
    }

    public TextBox getAddress()
    {
        return address;
    }

    public void setAddress(TextBox address)
    {
        this.address = address;
    }

    public TextField getPostcode()
    {
        return postcode;
    }

    public void setPostcode(TextField postcode)
    {
        this.postcode = postcode;
    }

    public TextField getCity()
    {
        return city;
    }

    public void setCity(TextField city)
    {
        this.city = city;
    }

    public TextField getState()
    {
        return state;
    }

    public void setState(TextField state)
    {
        this.state = state;
    }

    public CountrySelectBox getCountry()
    {
        return country;
    }

    public void setCountry(CountrySelectBox country)
    {
        this.country = country;
    }

    public TextField getTelOffice()
    {
        return telOffice;
    }

    public void setTelOffice(TextField telOffice)
    {
        this.telOffice = telOffice;
    }

    public TextField getTelHome()
    {
        return telHome;
    }

    public void setTelHome(TextField telHome)
    {
        this.telHome = telHome;
    }

    public TextField getTelMobile()
    {
        return telMobile;
    }

    public void setTelMobile(TextField telMobile)
    {
        this.telMobile = telMobile;
    }

    public TextField getTelFax()
    {
        return telFax;
    }

    public void setTelFax(TextField telFax)
    {
        this.telFax = telFax;
    }

    public TextField getUsername()
    {
        return username;
    }

    public void setUsername(TextField username)
    {
        this.username = username;
    }

    public Password getCurrentPassword()
    {
        return currentPassword;
    }

    public void setCurrentPassword(Password currentPassword)
    {
        this.currentPassword = currentPassword;
    }

    public Password getNewPassword()
    {
        return newPassword;
    }

    public void setNewPassword(Password newPassword)
    {
        this.newPassword = newPassword;
    }

    public Password getConfirmPassword()
    {
        return confirmPassword;
    }

    public void setConfirmPassword(Password confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }

    public Button getUpdate()
    {
        return update;
    }

    public void setUpdate(Button update)
    {
        this.update = update;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public Map getMessages()
    {
        return messages;
    }

    public void setMessages(Map messages)
    {
        this.messages = messages;
    }

    public Collection getProfilers()
    {
        return profilers;
    }

    public void setProfilers(Collection profilers)
    {
        this.profilers = profilers;
    }

	public TextField getStaffID() {
		return staffID;
	}

	public void setStaffID(TextField staffID) {
		this.staffID = staffID;
	}

	/*public SelectBox getSbDepartment() {
		return sbDepartment;
	}

	public void setSbDepartment(SelectBox sbDepartment) {
		this.sbDepartment = sbDepartment;
	}

	public SelectBox getSbUnit() {
		return sbUnit;
	}

	public void setSbUnit(SelectBox sbUnit) {
		this.sbUnit = sbUnit;
	}*/
}
