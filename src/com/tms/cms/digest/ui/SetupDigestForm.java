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
import com.tms.cms.digest.model.DigestDataObject;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;

public class SetupDigestForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/setupDigestForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";
    protected String type;
    protected String digestId;
    protected TextField digestName; 
    protected Button submit;
    protected Button cancel;

    public SetupDigestForm()
    {
    }

    public SetupDigestForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        digestName = new TextField("digestName");       
        digestName.setSize("15");
        ValidatorNotEmpty vne = new ValidatorNotEmpty("vne");
        digestName.addChild(vne);
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.save"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("digest.label.cancel"));
        addChild(digestName);
        addChild(submit);
        addChild(cancel);

        setMethod("post");
    }

    public void onRequest(Event evt)
    {
    	if("edit".equals(type)){
    		DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
    		try {
    			DaoQuery query= new DaoQuery();
    			query.addProperty(new OperatorEquals("digestId", digestId, DaoOperator.OPERATOR_AND));
    			Collection digest = digestModule.getSetupDigest(query);
    			
                
    	        for (Iterator i = digest.iterator(); i.hasNext();)
    	        {
    	        	DigestDataObject ddo = (DigestDataObject) i.next();
    	        	digestName.setValue(ddo.getDigestName());
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
            	DigestDataObject ddo= new DigestDataObject();
            	UuidGenerator uuid=UuidGenerator.getInstance();
                	DaoQuery query = new DaoQuery();
                	if("edit".equals(type)){
                		query.addProperty(new OperatorEquals("digestName", digestName.getValue(), DaoOperator.OPERATOR_AND));
                		query.addProperty(new OperatorEquals("digestID", digestId, DaoOperator.OPERATOR_NAN));
                        Collection list = module.getSetupDigest(query);
                        if(list.size() <= 0)
                        {                    	
                        	ddo.setDigest(digestId);
                        	ddo.setDigestName(digestName.getValue().toString());    
                        	module.updateSetupDigest(ddo);
                            init();
                        	forward = new Forward(FORWARD_SUCCESS);
                        }
                        else
                        {
                            message = Application.getInstance().getMessage("digest.label.digestInUsed");
                            digestName.setInvalid(true);                       
                        }	
                	}
                	else{
                		query.addProperty(new OperatorEquals("digestName", digestName.getValue(), DaoOperator.OPERATOR_AND));
                        
                        Collection list = module.getSetupDigest(query);
                        if(list.size() <= 0)
                        {                    	
                        	ddo.setDigest(uuid.getUuid());
                        	ddo.setDigestName(digestName.getValue().toString());    
                        	module.insertSetupDigest(ddo);
                            init();
                        	forward = new Forward(FORWARD_SUCCESS);
                        }
                        else
                        {
                            message = Application.getInstance().getMessage("digest.label.digestInUsed");
                            digestName.setInvalid(true);                       
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
    
	public String getDigestId() {
		return digestId;
	}

	public void setDigestId(String digestId) {
		this.digestId = digestId;
	}

	public TextField getDigestName() {
		return digestName;
	}

	public void setDigestName(TextField digestName) {
		this.digestName = digestName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

