package com.tms.cms.digest.ui;

import java.util.Collection;
import java.util.Iterator;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;

public class SetupDigestIssueForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/setupDigestIssueForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";
    protected String type;
    protected String digestIssueId;
    protected TextField digestIssueName; 
    protected Button submit;
    protected Button cancel;

    public SetupDigestIssueForm()
    {
    }

    public SetupDigestIssueForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        digestIssueName = new TextField("digestIssueName");       
        digestIssueName.setSize("15");
        ValidatorNotEmpty vne = new ValidatorNotEmpty("vne");
        digestIssueName.addChild(vne);
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.save"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("digest.label.cancel"));
        addChild(digestIssueName);
        addChild(submit);
        addChild(cancel);

        setMethod("post");
    }

    public void onRequest(Event evt)
    {
    	if("edit".equals(type)){
    		DigestModule digest = (DigestModule) Application.getInstance().getModule(DigestModule.class);
    		try {
    			DaoQuery query= new DaoQuery();
    			query.addProperty(new OperatorEquals("digestIssueId", digestIssueId, DaoOperator.OPERATOR_AND));
    			Collection digestIssue = digest.getSetupDigestIssue(query);
    			
                
    	        for (Iterator i = digestIssue.iterator(); i.hasNext();)
    	        {
    	        	DigestIssueDataObject dido = (DigestIssueDataObject) i.next();
    	        	digestIssueName.setValue(dido.getDigestIssueName());
    	        }
    		} catch (DigestException e) {
    			// TODO Auto-generated catch block
    			Log.getLog(getClass()).error(e.toString(), e);
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
            	DigestIssueDataObject dido= new DigestIssueDataObject();
            	UuidGenerator uuid=UuidGenerator.getInstance();
                	DaoQuery query = new DaoQuery();
                	if("edit".equals(type)){
                		query.addProperty(new OperatorEquals("digestIssueName", digestIssueName.getValue(), DaoOperator.OPERATOR_AND));
                		query.addProperty(new OperatorEquals("digestIssueID", digestIssueId, DaoOperator.OPERATOR_NAN));
                        Collection list = module.getSetupDigestIssue(query);
                        if(list.size() <= 0)
                        {                    	
                        	dido.setDigestIssue(digestIssueId);
                        	dido.setDigestIssueName(digestIssueName.getValue().toString());    
                        	module.updateSetupDigestIssue(dido);
                            init();
                        	forward = new Forward(FORWARD_SUCCESS);
                        }
                        else
                        {
                            message = Application.getInstance().getMessage("digest.label.digestIssueInUsed");
                            digestIssueName.setInvalid(true);                       
                        }	
                	}
                	else{
                		query.addProperty(new OperatorEquals("digestIssueName", digestIssueName.getValue(), DaoOperator.OPERATOR_AND));
                        
                        Collection list = module.getSetupDigestIssue(query);
                        if(list.size() <= 0)
                        {                    	
                        	dido.setDigestIssue(uuid.getUuid());
                        	dido.setDigestIssueName(digestIssueName.getValue().toString());    
                        	module.insertSetupDigestIssue(dido);
                            init();
                        	forward = new Forward(FORWARD_SUCCESS);
                        }
                        else
                        {
                            message = Application.getInstance().getMessage("digest.label.digestIssueInUsed");
                            digestIssueName.setInvalid(true);                       
                        }	
                	}
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

	public TextField getDigestIssueName() {
		return digestIssueName;
	}

	public void setDigestIssueName(TextField digestIssueName) {
		this.digestIssueName = digestIssueName;
	}

	public String getDigestIssueId() {
		return digestIssueId;
	}

	public void setDigestIssueId(String digestIssueId) {
		this.digestIssueId = digestIssueId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
