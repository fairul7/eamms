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

import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;
import com.tms.cms.digest.model.MailingListDataObject;
import com.tms.cms.digest.model.RecipientDataObject;

public class MailingListFormOpen extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/mailingListFormOpen";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";
    protected String mailingListId;
    protected String type;
    protected Label digestIssueList;
    protected Label mailingListName; 
    protected Label emailFormat;
    protected Label recipients; 
    protected Button submit; 
    protected Button send;  
    //protected Button print;
    

    public MailingListFormOpen()
    {
    }

    public MailingListFormOpen(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();         
        emailFormat=new Label("emailFormat");
        recipients = new Label("recipients");
        mailingListName = new Label("mailingListName");       
        digestIssueList = new Label("digestIssueList");       
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.cancel"));
        send = new Button("send");
        if("news".equals(type))
        send.setText(Application.getInstance().getMessage("digest.label.sendNewsList"));
        else
        	send.setText(Application.getInstance().getMessage("digest.label.sendOutIssue"));
        //print = new Button("print");
        //print.setText(Application.getInstance().getMessage("digest.label.printIssue"));
        addChild(digestIssueList);
        addChild(mailingListName);
        addChild(emailFormat);
        addChild(recipients);
        //addChild(print);
        addChild(send);
        addChild(submit);
        recipients.init();
        setMethod("post");
    }

    public Forward onSubmit(Event evt)
    {
    	Forward fwd=super.onSubmit(evt);
    	if(submit.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward(FORWARD_CANCEL);    	
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
			DaoQuery query=new DaoQuery();
			query.addProperty(new OperatorEquals("mailingListId", mailingListId, DaoOperator.OPERATOR_AND));
            
			Collection mail = digest.getMailingList(query);
			
	        for (Iterator i = mail.iterator(); i.hasNext();)
	        {
	        	MailingListDataObject mldo = (MailingListDataObject) i.next();
	        	digestIssueList.setText(mldo.getDigestIssueName());
	        	mailingListName.setText(mldo.getMailingListName());
	        	emailFormat.setText(mldo.getEmailFormat());
	        	DaoQuery query2=new DaoQuery();
				query2.addProperty(new OperatorEquals("mailingListId", mldo.getMailingListId(), DaoOperator.OPERATOR_AND));
				mldo.setRecipients(digest.getMailingRecipients(query2));
				String recipient="";
				for (Iterator j = mldo.getRecipients().iterator(); j.hasNext();)
		        {
					RecipientDataObject rdo = (RecipientDataObject) j.next();
					recipient+=rdo.getRecipientName()+",";
		        }
				if(recipient.length()>0)
					recipient=recipient.substring(0,recipient.length()-1);
				recipients.setText(recipient);
	        }
	
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			Log.getLog(getClass()).error(e.toString(), e);
		}      
    }
    
    public Forward onValidate(Event evt)
    {
        Forward forward = null;      
        if(send.getAbsoluteName().equals(findButtonClicked(evt))){
        	DigestModule digest = (DigestModule) Application.getInstance().getModule(DigestModule.class);
        	DaoQuery query=new DaoQuery();
			query.addProperty(new OperatorEquals("mailingListId", mailingListId, DaoOperator.OPERATOR_AND));
            
			Collection mail;
			MailingListDataObject mldo=new MailingListDataObject();
			try {
				mail = digest.getMailingList(query);
	        for (Iterator i = mail.iterator(); i.hasNext();)
	        {
	        	mldo = (MailingListDataObject) i.next();	
	        	mldo.setUser(evt.getWidgetManager().getUser());
	        	DaoQuery query2=new DaoQuery();
				query2.addProperty(new OperatorEquals("mailingListId", mldo.getMailingListId(), DaoOperator.OPERATOR_AND));
				mldo.setRecipients(digest.getMailingRecipients(query2));	
				DaoQuery query3=new DaoQuery();
				query3.addProperty(new OperatorEquals("digestIssueId", mldo.getDigestIssue(), DaoOperator.OPERATOR_AND));
				mldo.setDigest(digest.getDigestMain(query3));
				mldo.setEmailFormatType("Issue List");
	        }
	        if("newsFormat".equals(mldo.getMailFormat())){	        	
	        	digest.sendLocalNewsList(mldo);
	        }else{
	        	digest.sendDigestIssueList(mldo);
	        }
	        return new Forward("successissue");
			} catch (DigestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
        }/*else if(print.getAbsoluteName().equals(findButtonClicked(evt))){
        	getWidgetManager().removeAttribute("printmailingListId");
        	getWidgetManager().setAttribute("printmailingListId",mailingListId);
        	return new Forward("print");
        }*/


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

	public Label getDigestIssueList() {
		return digestIssueList;
	}

	public void setDigestIssueList(Label digestIssueList) {
		this.digestIssueList = digestIssueList;
	}

	public Label getEmailFormat() {
		return emailFormat;
	}

	public void setEmailFormat(Label emailFormat) {
		this.emailFormat = emailFormat;
	}

	public String getMailingListId() {
		return mailingListId;
	}

	public void setMailingListId(String mailingListId) {
		this.mailingListId = mailingListId;
	}

	public Label getMailingListName() {
		return mailingListName;
	}

	public void setMailingListName(Label mailingListName) {
		this.mailingListName = mailingListName;
	}

	public Label getRecipients() {
		return recipients;
	}

	public void setRecipients(Label recipients) {
		this.recipients = recipients;
	}
	public Button getSend() {
		return send;
	}

	public void setSend(Button send) {
		this.send = send;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*public Button getPrint() {
		return print;
	}

	public void setPrint(Button print) {
		this.print = print;
	}*/
}

