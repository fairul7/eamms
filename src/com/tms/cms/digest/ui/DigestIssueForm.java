package com.tms.cms.digest.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;
import com.tms.collab.project.WormsHandler;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class DigestIssueForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/digestIssueForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";
    protected SelectBox digestIssueList;
    protected TextField digestIssueName; 
    protected Button submit;
    protected Button cancel;

    public DigestIssueForm()
    {
    }

    public DigestIssueForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        digestIssueName = new TextField("digestIssueName");       
        digestIssueName.setSize("15");
        digestIssueList = new SelectBox("digestIssueList");
        digestIssueList.setOptions("none=Please Select");
        digestIssueList.setOnChange("sbInit()");
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.save"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("digest.label.cancel"));
        addChild(digestIssueList);
        addChild(digestIssueName);
        addChild(submit);
        addChild(cancel);

        setMethod("post");
    }

    public Forward onSubmit(Event evt)
    {
    	Forward fwd=super.onSubmit(evt);
    	if(cancel.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward(FORWARD_CANCEL);
    	else{
    		List dilist = (List) digestIssueList.getValue();
            String listData=(String) dilist.get(0);
            if("none".equals(listData)){
            	String data=(String)digestIssueName.getValue();
            	if("".equals(data)|| data.trim().length()<1){
            		digestIssueName.setInvalid(true);
            		this.setInvalid(true);
            	}
            }
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
        DigestModule digest = (DigestModule) Application.getInstance().getModule(DigestModule.class);
		try {
			Collection digestIssue = digest.getSetupDigestIssue(new DaoQuery());
			
	        for (Iterator i = digestIssue.iterator(); i.hasNext();)
	        {
	        	DigestIssueDataObject dido = (DigestIssueDataObject) i.next();
	        	digestIssueList.setOptions(dido.getDigestIssueId()+"||"+dido.getDigestIssueName()+"="+dido.getDigestIssueName());
	        }
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
		}      
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
            	String digestIssueId=uuid.getUuid();
            	dido.setDigestIssueId(digestIssueId);
            	dido.setLastEditBy(evt.getWidgetManager().getUser().getId());
            	dido.setDateCreate(new Date());
            	dido.setLastEditDate(new Date());
            	List dilist = (List) digestIssueList.getValue();
                String listData=(String) dilist.get(0);
                
                if("none".equals(listData)){
                	DaoQuery query = new DaoQuery();
                    query.addProperty(new OperatorEquals("digestIssueName", digestIssueName.getValue(), DaoOperator.OPERATOR_AND));
                    
                    Collection list = module.getSetupDigestIssue(query);
                    if(list.size() <= 0)
                    {
                    	
                    	dido.setDigestIssue(uuid.getUuid());
                    	dido.setDigestIssueName(digestIssueName.getValue().toString());    
                    	module.insertDigestIssue(dido,true);
                    }
                    else
                    {
                        message = Application.getInstance().getMessage("digest.label.digestIssueInUsed");
                        digestIssueName.setInvalid(true);
                        
                    }
                }else{
                	dido.setDigestIssue(listData.substring(0,listData.indexOf("||")));
                	module.insertDigestIssue(dido,false);
                }  
                forward = new Forward(FORWARD_SUCCESS,"digest.jsp?digestIssueId="+digestIssueId,true);  
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

	public SelectBox getDigestIssueList() {
		return digestIssueList;
	}

	public void setDigestIssueList(SelectBox digestIssueList) {
		this.digestIssueList = digestIssueList;
	}

	public TextField getDigestIssueName() {
		return digestIssueName;
	}

	public void setDigestIssueName(TextField digestIssueName) {
		this.digestIssueName = digestIssueName;
	}
}
