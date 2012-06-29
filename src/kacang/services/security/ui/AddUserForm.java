package kacang.services.security.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.CountrySelectBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.register.model.FMSRegisterManager;

public class AddUserForm extends BaseUserForm {
    public static final String ADD_USER_ID = "ANONYMOUS";
    private static final String FORWARD_SUCCESS = "kacang.services.security.ui.AddUserForm.success";
    private static final String FORWARD_FAIL = "kacang.services.security.ui.AddUserForm.fail";

    protected ValidatorNotEmpty validPassword;
    protected ValidatorNotEmpty validConfirmPassword;
    protected String type = "add";

    public AddUserForm() {
    }

    public AddUserForm(String name) {
        super(name);
    }

    public void init() {
        super.init();

        country.setValue(CountrySelectBox.DEFAULT_VALUE);
        validPassword = new ValidatorNotEmpty("validPassword");
        validConfirmPassword = new ValidatorNotEmpty("validCurrentPassword");

        password.addChild(validPassword);
        confirmPassword.addChild(validConfirmPassword);
        
        
    }

    public void initGroups() {
        Map map = getGroupList();
        if (!(map.isEmpty()))
            groups.setLeftValues(map);
    }

    private Map getGroupList() {
        Map map = new SequencedHashMap();
        Collection list = new ArrayList();
        try {
            list = ((SecurityService) Application.getInstance().getService(SecurityService.class)).getGroups(new DaoQuery(), 0, -1, "groupName", false);
            for (Iterator i = list.iterator(); i.hasNext();) {
                Group group = (Group) i.next();
                map.put(group.getId(), group.getGroupName());
            }
        } catch (Exception e) {
            Log.getLog(AddUserForm.class).error(e.toString(), e);
        }
        return map;
    }

    public String getId() {
        return ADD_USER_ID;
    }
        
    public Forward onValidate(Event evt) {
        setMessage("");
        if (!isState()) {
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) application.getService(SecurityService.class);
            
            if("-1".equals((String)department.getSelectedOptions().keySet().iterator().next())){
            	department.setInvalid(true);           
                setInvalid(true);;
            }
                  
            if("-1".equals((String)unit.getSelectedOptions().keySet().iterator().next())){
            	unit.setInvalid(true);           
                setInvalid(true);
            }
            
            if (password.getValue().equals(confirmPassword.getValue())) {
                try {
                    DaoQuery properties = new DaoQuery();
                    properties.addProperty(new OperatorEquals("username", username.getValue(), DaoOperator.OPERATOR_AND));
                    Collection validGroup = service.getUsers(properties, 0, 1, null, false);
                    if (validGroup.size() <= 0) {
                        boolean validEmail = true;
                        validGroup = new ArrayList();
                        if(!(email.getValue() == null || "".equals(email.getValue().toString().trim())))
                        {
                            properties = new DaoQuery();
                            properties.addProperty(new OperatorEquals("email1", email.getValue(), DaoOperator.OPERATOR_AND));
                            validGroup = service.getUsers(properties, 0, 1, null, false);
                        }
                        else
                        {
                            if("true".equals(Application.getInstance().getProperty(SecurityService.PROPERTY_EMAIL_COMPULSORY)))
                                validEmail = false;
                        }
                        if(validEmail)
                        {
                            if (validGroup.size() <= 0) {
                                //Creating Group
                                UuidGenerator generator = UuidGenerator.getInstance();
                                User user = buildUser(generator.getUuid());
                                
                                if(!"-1".equals(user.getProperty("department"))){
	                                user.setProperty("weakpass", password.getValue());
	                                //service.addUser(user, true);
	                                FMSRegisterManager FM = (FMSRegisterManager) application.getModule(FMSRegisterManager.class);
	                                FM.addUser(user, true);
	
	                                //-- Logging transaction
	                                User currentUser = getWidgetManager().getUser();
	                                Log.getLog(getClass()).info("User [id:" + user.getId() + ",username:" + user.getUsername() + ",name:" + user.getName() + "] created by [" + currentUser.getUsername() + "] on " + new Date());
	                                Log.getLog(getClass()).write(new Date(), user.getId(), currentUser.getId(), "Security: User created",
										"User " + user.getName() + " with ID " + user.getId() + " created successfully", evt.getRequest().getRemoteAddr(),
										evt.getRequest().getSession().getId());
	
	                                //Adding Groups
	                                Collection groupList = (List) groups.getRightSelect().getValue();
	                                if (groupList != null) {
	                                    if (groupList.size() > 0) {
	                                        service.assignGroups(user.getId(), (String[]) groupList.toArray());
	                                    }
	                                }
                                } else {
                            		department.setInvalid(true);
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
                    Log.getLog(AddGroupForm.class).error(e.toString(), e);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    
}
