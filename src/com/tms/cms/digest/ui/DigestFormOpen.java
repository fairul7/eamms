package com.tms.cms.digest.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.cms.digest.model.DigestDataObject;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;

public class DigestFormOpen extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/digestOpenForm";
    public static final String FORWARD_SUBMIT = "submit";
    protected String digestId;
    protected Label digestName; 
    protected Button submit;

    public DigestFormOpen()
    {
    }

    public DigestFormOpen(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        digestName = new Label("digestName");               
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.cancel"));       
        addChild(digestName);
        addChild(submit);

        setMethod("post");
    }

    public Forward onSubmit(Event evt)
    {
    	Forward fwd=super.onSubmit(evt);
    	if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
    		DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
    		String digestIssueId="";
    		try {
    			DaoQuery query= new DaoQuery();
    			query.addProperty(new OperatorEquals("cms_digest.digestId", digestId, DaoOperator.OPERATOR_AND));
    			Collection digest = digestModule.getDigestMain(query);
    			
                
    	        for (Iterator i = digest.iterator(); i.hasNext();)
    	        {
    	        	DigestDataObject dido = (DigestDataObject) i.next();
    	        	digestIssueId=dido.getDigestIssueId();
    	        }
    		} catch (DigestException e) {
    			// TODO Auto-generated catch block
    			Log.getLog(getClass()).error(e.toString(), e);
    		}      
            return new Forward(FORWARD_SUBMIT,"digest.jsp?digestIssueId="+digestIssueId,true);
    	}

        return fwd;
        
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public void onRequest(Event evt)
    {

    	init();
        DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
		try {
			DaoQuery query= new DaoQuery();
			query.addProperty(new OperatorEquals("cms_digest.digestId", digestId, DaoOperator.OPERATOR_AND));
			Collection digest = digestModule.getDigestMain(query);
			
            
	        for (Iterator i = digest.iterator(); i.hasNext();)
	        {
	        	DigestDataObject dido = (DigestDataObject) i.next();
	        	digestName.setText(dido.getDigestName());
	        }
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
		}      
    }
    
    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

	public String getDigestId() {
		return digestId;
	}

	public void setDigestId(String digestId) {
		this.digestId = digestId;
	}

	public Label getDigestName() {
		return digestName;
	}

	public void setDigestName(Label digestName) {
		this.digestName = digestName;
	}
}


