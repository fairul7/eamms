package com.tms.cms.digest.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;

public class DigestIssueFormOpen extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/digestIssueOpenForm";
    public static final String FORWARD_SUBMIT = "submit";
    protected String digestIssueId;
    protected Label digestIssueName; 
    protected Button submit;

    public DigestIssueFormOpen()
    {
    }

    public DigestIssueFormOpen(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        digestIssueName = new Label("digestIssueName");               
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.cancel"));       
        addChild(digestIssueName);
        addChild(submit);

        setMethod("post");
    }

    public Forward onSubmit(Event evt)
    {
    	Forward fwd=super.onSubmit(evt);
    	if(submit.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward(FORWARD_SUBMIT);

        return fwd;
        
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public void onRequest(Event evt)
    {

    	init();
        DigestModule digest = (DigestModule) Application.getInstance().getModule(DigestModule.class);
		try {
			DaoQuery query= new DaoQuery();
			query.addProperty(new OperatorEquals("cms_digest_issue.digestIssueId", digestIssueId, DaoOperator.OPERATOR_AND));
			Collection digestIssue = digest.getDigestIssueMain(query);
			
            
	        for (Iterator i = digestIssue.iterator(); i.hasNext();)
	        {
	        	DigestIssueDataObject dido = (DigestIssueDataObject) i.next();
	        	digestIssueName.setText(dido.getDigestIssueName());
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

	public String getDigestIssueId() {
		return digestIssueId;
	}

	public void setDigestIssueId(String digestIssueId) {
		this.digestIssueId = digestIssueId;
	}

	public Label getDigestIssueName() {
		return digestIssueName;
	}

	public void setDigestIssueName(Label digestIssueName) {
		this.digestIssueName = digestIssueName;
	}
}

