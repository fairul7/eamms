package kacang.services.security.ui;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorBase;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorNotIn;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.Encryption;
import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.register.model.FMSRegisterManager;

import java.util.*;

public class EditUserForm extends BaseUserForm {
    private static final String FORWARD_SUCCESS = "kacang.services.security.ui.EditUserForm.success";
    private static final String FORWARD_FAIL = "kacang.services.security.ui.EditUserForm.fail";
    protected String type = "edit";
    
    public EditUserForm() {
    }

    public EditUserForm(String name) {
        super(name);
    }

    public void init() {
        //Retrieving Id
        if (!(id == null)) {
            if (!(id.equals(""))) {
                super.init();
                initUser();
            }
        }
    }

    public void onRequest(Event evt)
    {
        if (!(id == null || "".equals(id)))
        {
            super.onRequest(evt);
            initUser();
        }
    }

    private void initUser()
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        FMSRegisterManager FM = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
        try {
            //User user = service.getUser(id);
        	User user = FM.getUser(id);
            if(username == null)
                super.init();
            username.setValue(user.getUsername());
            firstName.setValue(user.getProperty("firstName"));
            lastName.setValue(user.getProperty("lastName"));
            email.setValue(user.getProperty("email1"));
            address.setValue(user.getProperty("address"));
            postcode.setValue(user.getProperty("postcode"));
            city.setValue(user.getProperty("city"));
            stateAddress.setValue(user.getProperty("state"));
            country.setValue(user.getProperty("country"));
            telOffice.setValue(user.getProperty("telOffice"));
            telHome.setValue(user.getProperty("telHome"));
            telMobile.setValue(user.getProperty("telMobile"));
            fax.setValue(user.getProperty("fax"));
            ////
            department.setSelectedOptions(new String[]{(String)user.getProperty("department")});
            unit.setSelectedOptions(new String[]{(String)user.getProperty("unit")});
            staffID.setValue(user.getProperty("property1"));
            
            if (user.isActive())
                active.setChecked(true);
            else
                active.setChecked(false);
        } catch (Exception e) {
            Log.getLog(EditUserForm.class).error(e.toString(), e);
        }
    }

    public void initGroups() {
        if (!(id == null)) {
            if (!(id.equals(""))) {
                Collection list;
                Map leftUsers = new SequencedHashMap();
                Map rightUsers = new SequencedHashMap();
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                try {
                    Group group;
                    list = service.getUserGroups(id, new DaoQuery(), 0, -1, "groupName", false);
                    for (Iterator i = list.iterator(); i.hasNext();) {
                        group = (Group) i.next();
                        rightUsers.put(group.getId(), group.getGroupName());
                    }
                    DaoQuery properties = new DaoQuery();
                    properties.addProperty(new OperatorNotIn("id", rightUsers.keySet().toArray(), OperatorBase.OPERATOR_AND));
                    list = service.getGroups(properties, 0, -1, "groupName", false);
                    for (Iterator i = list.iterator(); i.hasNext();) {
                        group = (Group) i.next();
                        leftUsers.put(group.getId(), group.getGroupName());
                    }
                } catch (Exception e) {
                    Log.getLog(EditUserForm.class).error(e.toString(), e);
                }
                groups.setLeftValues(leftUsers);
                groups.setRightValues(rightUsers);
            }
        }
    }

    public Forward onValidate(Event evt) {
        setMessage("");
        if (!isState()) {
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) application.getService(SecurityService.class);
            if (password.getValue().equals(confirmPassword.getValue())) {
                try {
                    DaoQuery properties = new DaoQuery();
                    properties.addProperty(new OperatorEquals("username", username.getValue(), DaoOperator.OPERATOR_AND));
                    properties.addProperty(new OperatorEquals("id", id, DaoOperator.OPERATOR_NAN));
                    Collection validGroup = service.getUsers(properties, 0, -1, null, false);
                    if (validGroup.isEmpty()) {
                        boolean emailValid = true;
                        if (email.getValue() != null && email.getValue().toString().trim().length() > 0) {
                            properties = new DaoQuery();
                            properties.addProperty(new OperatorEquals("email1", email.getValue(), DaoOperator.OPERATOR_AND));
                            properties.addProperty(new OperatorEquals("id", id, DaoOperator.OPERATOR_NAN));
                            validGroup = service.getUsers(properties, 0, 1, null, false);
                        }
                        else
                        {
                            if("true".equals(Application.getInstance().getProperty(SecurityService.PROPERTY_EMAIL_COMPULSORY)))
                                emailValid = false;
                        }
                        if(emailValid)
                        {
                            if (validGroup.size() <= 0) {
                            	User user = buildUser(id);
                            	
                            	if(!"-1".equals(user.getProperty("department"))){
	                                user.setProperty("weakpass", Encryption.encrypt((String)password.getValue(), id));
	                                //Inserting Password Into Object Of No Attept To Change Has Been Detected
	                                if (((String) password.getValue()).length() <= 0) {
	                                    User currentUser = service.getUser(id);
	                                    user.setPassword(currentUser.getPassword());
	                                    user.setProperty("weakpass", currentUser.getProperty("weakpass"));
	                                }
	                                //service.updateUser(user);
	                                FMSRegisterManager FM = (FMSRegisterManager) application.getModule(FMSRegisterManager.class);
	                                FM.updateUser(user);
	                                
	                                //-- Logging transaction
	                                User currentUser = getWidgetManager().getUser();
	                                Log.getLog(getClass()).info("User [id:" + user.getId() + ",username:" + user.getUsername() + ",name:" + user.getName() + "] updated by [" + currentUser.getUsername() + "] on " + new Date());
	                                Log.getLog(getClass()).write(new Date(), user.getId(), currentUser.getId(), "Security: User updated",
										"User " + user.getName() + " with ID " + user.getId() + " updated successfully", evt.getRequest().getRemoteAddr(),
										evt.getRequest().getSession().getId());
	
	                                //Adding Groups
	                                Collection groupList = (List) groups.getRightSelect().getValue();
	                                service.unassignGroups(id);
	                                if (groupList != null) {
	                                    if (groupList.size() > 0) {
	                                        service.assignGroups(id, (String[]) groupList.toArray());
	                                    }
	                                }
                            	} else {
                            		department.setInvalid(true);
                            		setMessage("Please select department !");
                            		return new Forward(FORWARD_FAIL, null, false);
                            	}
                            } else {
                                setMessage(application.getMessage("security.error.emailInUse", "Email Address Already In Use"));
                                return new Forward(FORWARD_FAIL, null, false);
                            }
                        }
                        else
                        {
                            setMessage("Invalid Email Address");
                            return new Forward(FORWARD_FAIL, null, false);
                        }
                    } else {
                        setMessage(application.getMessage("security.error.usernameInUse", "Username Already In Use"));
                        return new Forward(FORWARD_FAIL, null, false);
                    }
                } catch (Exception e) {
                    Log.getLog(EditGroupForm.class).error(e.toString(), e);
                    return new Forward(FORWARD_FAIL, null, false);
                }
            } else {
                setMessage(application.getMessage("security.error.passwordNotMatching", "Password And Confirmation Do Not Match"));
                return new Forward(FORWARD_FAIL, null, false);
            }
        }
        init();
        setState(true);
        return new Forward(FORWARD_SUCCESS, null, false);
    }

    public String getDefaultTemplate() {
        if (id.equals(""))
            return "";
        else
            return super.getDefaultTemplate();
    }

    public void setId(String id)
    {
        super.setId(id);
        initUser();
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
}
