package com.tms.cms.digest.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;
import com.tms.cms.digest.model.MailingListDataObject;
import com.tms.cms.digest.model.RecipientDataObject;

public class MailingListForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "digest/mailingListForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";
    protected SelectBox digestIssueList;
    protected TextField mailingListName; 
    protected ButtonGroup emailFormat;
    protected Radio[] format; 
    protected RecipientsSelectBox recipients; 
    protected Button submit;
    protected Button cancel;

    public MailingListForm()
    {
    }

    public MailingListForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren(); 
        format = new Radio[2];
        format[0]= new Radio("digestFormat");
        format[0].setText(Application.getInstance().getMessage("digest.label.digestFormat"));
        format[0].setChecked(true);
        format[1]= new Radio("newsFormat");
        format[1].setText(Application.getInstance().getMessage("digest.label.newsFormat"));
        emailFormat=new ButtonGroup("emailFormat");
        emailFormat.setColumns(2);
        emailFormat.addButtons(format);
        recipients = new RecipientsSelectBox("recipients");
        mailingListName = new TextField("mailingListName");       
        mailingListName.setSize("30");
        ValidatorNotEmpty vne = new ValidatorNotEmpty("vne");
        mailingListName.addChild(vne);
        digestIssueList = new SelectBox("digestIssueList");       
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("digest.label.save"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("digest.label.cancel"));
        addChild(digestIssueList);
        addChild(mailingListName);
        addChild(emailFormat);
        addChild(recipients);
        addChild(submit);
        addChild(cancel);
        recipients.init();
        setMethod("post");
    }

    public Forward onSubmit(Event evt)
    {
    	Forward fwd=super.onSubmit(evt);
    	if(cancel.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward(FORWARD_CANCEL);
    	String[] selectedRecipients = recipients.getIds();
    	if(selectedRecipients.length<1){
    		recipients.setInvalid(true);
    		this.setInvalid(true);
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
			Collection digestIssue = digest.getDigestIssueMain(new DaoQuery());
			
	        for (Iterator i = digestIssue.iterator(); i.hasNext();)
	        {
	        	DigestIssueDataObject dido = (DigestIssueDataObject) i.next();
	        	SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy"); 
	        	digestIssueList.setOptions(dido.getDigestIssueId()+"||"+dido.getDigestIssueName()+"="+dido.getDigestIssueName()+" - "+sdf.format(dido.getDateCreate()));
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
            	MailingListDataObject mldo= new MailingListDataObject();
            	UuidGenerator uuid=UuidGenerator.getInstance();
            	String mailingId=uuid.getUuid();
            	mldo.setMailingListId(mailingId);
            	mldo.setMailingListName((String)mailingListName.getValue());
            	mldo.setLastEditBy(evt.getWidgetManager().getUser().getId());
            	mldo.setDateCreate(new Date());
            	mldo.setLastEditDate(new Date());
            	List dilist = (List) digestIssueList.getValue();
                String listData=(String) dilist.get(0);
                mldo.setDigestIssue(listData.substring(0,listData.indexOf("||")));
                mldo.setDigestIssueName(listData.substring(listData.indexOf("||")+2,listData.length()));
                if(format[0].isChecked())
                {
                	mldo.setMailFormat("digestFormat");
                }else if(format[1].isChecked())
                {
                	mldo.setMailFormat("newsFormat");
                }
                String[] selectedRecipients = recipients.getIds();
                
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("recipientId", selectedRecipients, DaoOperator.OPERATOR_AND));
                mldo.setRecipients(module.getDigestRecipients(query));                
                module.insertMailingList(mldo);
                if(format[0].isChecked())
                {
                forward = new Forward(FORWARD_SUCCESS,"mailingListOpen.jsp?mailingListId="+mailingId,true); 
                }else if(format[1].isChecked())
                {
                	forward = new Forward(FORWARD_SUCCESS,"mailingListNewsOpen.jsp?mailingListId="+mailingId,true); 
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

	public SelectBox getDigestIssueList() {
		return digestIssueList;
	}

	public void setDigestIssueList(SelectBox digestIssueList) {
		this.digestIssueList = digestIssueList;
	}

	public ButtonGroup getEmailFormat() {
		return emailFormat;
	}

	public void setEmailFormat(ButtonGroup emailFormat) {
		this.emailFormat = emailFormat;
	}

	public TextField getMailingListName() {
		return mailingListName;
	}

	public void setMailingListName(TextField mailingListName) {
		this.mailingListName = mailingListName;
	}

	public RecipientsSelectBox getRecipients() {
		return recipients;
	}

	public void setRecipients(RecipientsSelectBox recipients) {
		this.recipients = recipients;
	}
}
