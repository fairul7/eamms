package com.tms.collab.qmessaging.ui;

import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.stdui.Button;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.collab.messaging.model.*;

import javax.mail.internet.AddressException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 28, 2004
 * Time: 2:34:16 PM
 * To change this template use Options | File Templates.
 */
public class QuickMessengerForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "qmessaging/qMessengerForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "failed";

    private String userId;
    private User user;
    private TextBox body;
    private Button submit;
    private Button cancel;
    private ValidatorNotEmpty validBody;

    public QuickMessengerForm()
    {
    }

    public QuickMessengerForm(String s)
    {
        super(s);
    }

    public void init()
    {
        setMethod("POST");
        if(user != null)
        {
            validBody = new ValidatorNotEmpty("validBody");

            body = new TextBox("body");
            body.setCols("50");
            body.setRows("10");
            body.addChild(validBody);
            submit = new Button("submit");
            submit.setText(Application.getInstance().getMessage("qmessaging.label.send","Send"));
            cancel = new Button("cancel");
            cancel.setText(Application.getInstance().getMessage("qmessaging.label.cancel","Cancel"));

            addChild(body);
            addChild(submit);
            addChild(cancel);
        }
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward = null;
        if(user != null)
        {
            if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward((FORWARD_CANCEL));
            else
                forward = super.actionPerformed(event);
        }
        return forward;
    }

    public Forward onValidate(Event event)
    {
        Forward forward = null;
        if(user != null)
        {
            try
            {
                User currentUser = getWidgetManager().getUser();
                MessagingModule module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                IntranetAccount intranetAccount = module.getIntranetAccountByUserId(currentUser.getId());
                SmtpAccount smtpAccount = module.getSmtpAccountByUserId(currentUser.getId());
                //Formulating Message Object
                Message message = new Message();
                message.setMessageId(UuidGenerator.getInstance().getUuid());
                message.setFrom(intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN);
                message.setToIntranetList(Util.convertStringToIntranetRecipientsList(user.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN));
                message.setSubject(MessagingModule.QM_SUBJECT);
                message.setBody((String) body.getValue());
                message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                message.setDate(new Date());
                module.sendMessage(smtpAccount, message, currentUser.getId());
                forward = new Forward(FORWARD_SUCCESS);
            }
            catch (MessagingException e)
            {
                Log.getLog(QuickMessengerForm.class).error(e.getMessage(), e);
                forward = new Forward(FORWARD_FAILED);
            }
            catch (AddressException e)
            {
                Log.getLog(QuickMessengerForm.class).error(e.getMessage(), e);
                forward = new Forward(FORWARD_FAILED);
            }
        }
        return forward;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    //Getter and Setter
    public TextBox getBody()
    {
        return body;
    }

    public void setBody(TextBox body)
    {
        this.body = body;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try
        {
            user = service.getUser(userId);
        }
        catch (SecurityException e)
        {
            Log.getLog(QuickMessengerForm.class).error(e.getMessage(), e);
        }
        init();
    }
}
