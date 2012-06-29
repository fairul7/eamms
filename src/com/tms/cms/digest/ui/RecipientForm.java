package com.tms.cms.digest.ui;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentSubscription;
import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;
import com.tms.cms.digest.model.RecipientDataObject;

public class RecipientForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/recipientsForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_EDIT_SUCCESS = "editsuccess";
    public static final String FORWARD_FAILED = "fail";
    protected String type;
    protected String recipientId;
    protected UsersSelectBox recipientsUser;
    protected TextField recipientsName; 
    protected ValidatorNotEmpty vrecipientsName; 
    protected Button submit;
    protected Button cancel;

    public RecipientForm()
    {
    }

    public RecipientForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        recipientsName = new TextField("recipientsName");       
        recipientsName.setSize("15");
        vrecipientsName = new ValidatorNotEmpty("vrecipientsName");
        recipientsName.addChild(vrecipientsName);
        recipientsUser = new UsersSelectBox("recipientsUser");
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.save"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("digest.label.cancel"));
        addChild(recipientsUser);
        addChild(recipientsName);
        addChild(submit);
        addChild(cancel);
        recipientsUser.init();
        setMethod("post");
    }
    
    public void onRequest(Event evt) {
    	if("edit".equals(type)){
    		try
    		{
    		Collection userIdList = new ArrayList();
            // retrieve subscription listing
            Application app = Application.getInstance();
            DigestModule dm = (DigestModule)app.getModule(DigestModule.class);
            RecipientDataObject ro=dm.getDigestRecipientsDo(recipientId);
            recipientsName.setValue(ro.getRecipientName());
            Collection list = dm.getDigestUser(recipientId);
            for (Iterator i = list.iterator(); i.hasNext();) {
            	RecipientDataObject rdo = (RecipientDataObject)i.next();
                userIdList.add(rdo.getUserRecipientId());
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
            recipientsUser.setIds(userIdArray);
    		}catch(Exception e){
    			 Log.getLog(getClass()).error("Error retrieving recipients Users:", e);
    		}
    	}
    }
    
    public Forward onSubmit(Event evt)
    {
    	Forward fwd=super.onSubmit(evt);
    	if(cancel.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward(FORWARD_CANCEL);

        return fwd;
        
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }
    
    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        try
        {
            if(submit.getAbsoluteName().equals(findButtonClicked(evt)))
            {
            	DigestModule module = (DigestModule) Application.getInstance().getModule(DigestModule.class);
            	RecipientDataObject rdo= new RecipientDataObject();
            	UuidGenerator uuid=UuidGenerator.getInstance();
            	String recId=uuid.getUuid();
            	String[] selectedUsers = recipientsUser.getIds();
            	rdo.setRecipientId(recId);
            	rdo.setRecipientName(recipientsName.getValue().toString());
            	if("edit".equals(type)){
            		rdo.setRecipientId(recipientId);
            		module.updateRecipients(rdo,selectedUsers);
            		
            	}else if("add".equals(type)){
            		module.insertRecipients(rdo,selectedUsers);
            	}
                forward = new Forward(FORWARD_SUCCESS);  
            }
        }
        catch(Exception e)
        {
        	Log.getLog(getClass()).error(e.toString(), e);
            forward = new Forward(FORWARD_FAILED);
        }
        return forward;
    }


    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

	public TextField getRecipientsName() {
		return recipientsName;
	}

	public void setRecipientsName(TextField recipientsName) {
		this.recipientsName = recipientsName;
	}

	public UsersSelectBox getRecipientsUser() {
		return recipientsUser;
	}

	public void setRecipientsUser(UsersSelectBox recipientsUser) {
		this.recipientsUser = recipientsUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}
}

