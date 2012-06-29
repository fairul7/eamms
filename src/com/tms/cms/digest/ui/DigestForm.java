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
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.digest.model.DigestDataObject;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;

public class DigestForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/digestForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";
    protected String digestIssueId;
    protected SelectBox digestList;
    protected TextField digestName; 
    protected Button submit;
    protected Button cancel;

    public DigestForm()
    {
    }

    public DigestForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        digestName = new TextField("digestName");       
        digestName.setSize("15");
        digestList = new SelectBox("digestList");
        digestList.setOptions("none=Please Select");
        digestList.setOnChange("sbInit()");
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.save"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("digest.label.cancel"));
        addChild(digestList);
        addChild(digestName);
        addChild(submit);
        addChild(cancel);

        setMethod("post");
    }

    public Forward onSubmit(Event evt)
    {
    	Forward fwd=super.onSubmit(evt);
    	if(cancel.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward(FORWARD_CANCEL,"digest.jsp?digestIssueId="+digestIssueId,true);
    	else{
    		List dilist = (List) digestList.getValue();
            String listData=(String) dilist.get(0);
            if("none".equals(listData)){
            	String data=(String)digestName.getValue();
            	if("".equals(data)|| data.trim().length()<1){
            		digestName.setInvalid(true);
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
        DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
		try {
			Collection digest = digestModule.getSetupDigest(new DaoQuery());
			
	        for (Iterator i = digest.iterator(); i.hasNext();)
	        {
	        	DigestDataObject ddo = (DigestDataObject) i.next();
	        	digestList.setOptions(ddo.getDigestId()+"||"+ddo.getDigestName()+"="+ddo.getDigestName());
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
            	DigestDataObject ddo= new DigestDataObject();
            	UuidGenerator uuid=UuidGenerator.getInstance();
            	String digestId=uuid.getUuid();
            	ddo.setDigestIssueId(digestIssueId);
            	ddo.setDigestId(digestId);
            	ddo.setLastEditBy(evt.getWidgetManager().getUser().getId());
            	ddo.setDateCreate(new Date());
            	ddo.setLastEditDate(new Date());
            	List dilist = (List) digestList.getValue();
                String listData=(String) dilist.get(0);
                
                if("none".equals(listData)){
                	DaoQuery query = new DaoQuery();
                    query.addProperty(new OperatorEquals("digestName", digestName.getValue(), DaoOperator.OPERATOR_AND));
                    
                    Collection list = module.getSetupDigest(query);
                    if(list.size() <= 0)
                    {
                    	
                    	ddo.setDigest(uuid.getUuid());
                    	ddo.setDigestName(digestName.getValue().toString());    
                    	module.insertDigest(ddo,true);
                    }
                    else
                    {
                        message = Application.getInstance().getMessage("digest.label.digestInUsed");
                        digestName.setInvalid(true);
                        
                    }
                }else{
                	ddo.setDigest(listData.substring(0,listData.indexOf("||")));
                	module.insertDigest(ddo,false);
                }  
                forward = new Forward(FORWARD_SUCCESS,"content.jsp?digestId="+digestId,true);  
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

	public String getDigestIssueId() {
		return digestIssueId;
	}

	public void setDigestIssueId(String digestIssueId) {
		this.digestIssueId = digestIssueId;
	}

	public SelectBox getDigestList() {
		return digestList;
	}

	public void setDigestList(SelectBox digestList) {
		this.digestList = digestList;
	}

	public TextField getDigestName() {
		return digestName;
	}

	public void setDigestName(TextField digestName) {
		this.digestName = digestName;
	}
}

