package com.tms.cms.core.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.services.security.ui.UsersSelectBox;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.util.Log;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentSubscription;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class EditContentSubscriptionForm extends Form {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILURE = "failure";

    protected String id;
    protected UsersSelectBox users;
    protected Button submit;
    protected Button cancel;

    public EditContentSubscriptionForm() {
        super();
    }

    public EditContentSubscriptionForm(String name) {
        super(name);
    }

    public void init() {
        Application app = Application.getInstance();
        users = new UsersSelectBox("users");
        submit = new Button("submit", app.getMessage("general.label.submit", "Submit"));
        cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        addChild(users);
        addChild(submit);
        addChild(cancel);

        users.init();
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, null);
        if (co != null) {
            setId(co.getId());
        }
        initForm(evt);
    }

    public void initForm(Event evt) {
        try {
			setMethod("post");
            String contentId = getId();
            Collection userIdList = new ArrayList();

            // retrieve subscription listing
            Application app = Application.getInstance();
            ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
            Collection list = cm.getSubscriptionsByContent(contentId, 0, -1);
            for (Iterator i = list.iterator(); i.hasNext();) {
                ContentSubscription cs = (ContentSubscription)i.next();
                userIdList.add(cs.getUserId());
            }

            // retrieve users in order
            Collection userList = new ArrayList();
            if (userIdList.size() > 0) {
                DaoQuery q = new DaoQuery();
                q.addProperty(new OperatorIn("id", userIdList.toArray(), DaoOperator.OPERATOR_AND));
                SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
                userList = security.getUsers(q, 0, -1, "firstName", false);
            }

            // get user ids
            userIdList = new ArrayList();
            for (Iterator i = userList.iterator(); i.hasNext();) {
                User u = (User)i.next();
                userIdList.add(u.getId());
            }

            // set selected users
            String[] userIdArray = (String[])userIdList.toArray(new String[0]);
            users.setIds(userIdArray);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving subscription list", e);
        }

    }

    public Forward onValidate(Event evt) {
        try {
        	String buttonClicked = findButtonClicked(evt);
        	if(submit.getAbsoluteName().equals(buttonClicked)){
            String contentId = getId();
            if (contentId != null && contentId.trim().length() > 0) {
                String[] selectedUsers = users.getIds();

                Application app = Application.getInstance();
                ContentManager cm = (ContentManager)app.getModule(ContentManager.class);

                cm.unsubscribeByContent(contentId, null);
                cm.subscribeByContent(contentId, selectedUsers);
            }
            return new Forward(FORWARD_SUCCESS);
            }
        	else{
        		return new Forward(Form.CANCEL_FORM_ACTION);
        	}
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error("Error updating subscription", e);
            return new Forward(FORWARD_FAILURE);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
